package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.ArticleDao;
import net.stackoverflow.blog.dao.CategoryDao;
import net.stackoverflow.blog.pojo.po.ArticlePO;
import net.stackoverflow.blog.pojo.po.CategoryPO;
import net.stackoverflow.blog.util.RedisCacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类服务实现
 *
 * @author 凉衫薄
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private ArticleDao articleDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<CategoryPO> selectByPage(Page page) {
        return categoryDao.selectByPage(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<CategoryPO> selectByCondition(Map<String, Object> searchMap) {
        return categoryDao.selectByCondition(searchMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CategoryPO selectById(String id) {
        CategoryPO categoryPO = (CategoryPO) RedisCacheUtils.get("category:" + id);
        if (categoryPO != null) {
            return categoryPO;
        } else {
            categoryPO = categoryDao.selectById(id);
            if (categoryPO != null) {
                RedisCacheUtils.set("category:" + id, categoryPO);
            }
            return categoryPO;
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CategoryPO insert(CategoryPO category) {
        categoryDao.insert(category);
        RedisCacheUtils.set("category:" + category.getId(), category);
        return category;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<CategoryPO> categorys) {
        return categoryDao.batchInsert(categorys);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CategoryPO deleteById(String id) {
        CategoryPO category = categoryDao.selectById(id);

        CategoryPO unCategory = categoryDao.selectByCondition(new HashMap<String, Object>() {{
            put("code", "uncategory");
        }}).get(0);
        List<ArticlePO> articleList = articleDao.selectByCondition(new HashMap<String, Object>() {{
            put("categoryId", category.getId());
        }});

        if (articleList.size() != 0) {
            for (ArticlePO article : articleList) {
                article.setCategoryId(unCategory.getId());
                RedisCacheUtils.set("article:" + article.getId(), article);
                RedisCacheUtils.set("article:" + article.getUrl(), article);
            }
            articleDao.batchUpdate(articleList);
        }

        categoryDao.deleteById(id);
        RedisCacheUtils.del("category:" + id);
        return category;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteById(List<String> ids) {
        for (String id : ids) {
            CategoryPO category = categoryDao.selectById(id);

            CategoryPO unCategory = categoryDao.selectByCondition(new HashMap<String, Object>() {{
                put("code", "uncategory");
            }}).get(0);
            List<ArticlePO> articleList = articleDao.selectByCondition(new HashMap<String, Object>() {{
                put("categoryId", category.getId());
            }});
            if (articleList.size() != 0) {
                for (ArticlePO article : articleList) {
                    article.setCategoryId(unCategory.getId());
                    RedisCacheUtils.set("article:" + article.getId(), article);
                    RedisCacheUtils.set("article:" + article.getUrl(), article);
                }
                articleDao.batchUpdate(articleList);
            }
            RedisCacheUtils.del("category:" + id);
        }
        return categoryDao.batchDeleteById(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CategoryPO update(CategoryPO category) {
        categoryDao.update(category);
        RedisCacheUtils.set("category:" + category.getId(), category);
        return category;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<CategoryPO> categorys) {
        int result = categoryDao.batchUpdate(categorys);
        for (CategoryPO categoryPO : categorys) {
            RedisCacheUtils.set("category:" + categoryPO.getId(), categoryPO);
        }
        return result;
    }

}
