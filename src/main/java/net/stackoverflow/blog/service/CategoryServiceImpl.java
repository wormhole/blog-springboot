package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.ArticleDao;
import net.stackoverflow.blog.dao.CategoryDao;
import net.stackoverflow.blog.pojo.entity.Article;
import net.stackoverflow.blog.pojo.entity.Category;
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
    public List<Category> selectByPage(Page page) {
        return categoryDao.selectByPage(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Category> selectByCondition(Map<String, Object> searchMap) {
        return categoryDao.selectByCondition(searchMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category selectById(String id) {
        return categoryDao.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category insert(Category category) {
        categoryDao.insert(category);
        return categoryDao.selectById(category.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<Category> list) {
        return categoryDao.batchInsert(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category deleteById(String id) {
        Category category = categoryDao.selectById(id);

        Category unCategory = categoryDao.selectByCondition(new HashMap<String, Object>() {{
            put("code", "uncategory");
        }}).get(0);
        List<Article> articleList = articleDao.selectByCondition(new HashMap<String, Object>() {{
            put("categoryId", category.getId());
        }});

        if (articleList.size() != 0) {
            for (Article article : articleList) {
                article.setCategoryId(unCategory.getId());
            }
            articleDao.batchUpdate(articleList);
        }

        categoryDao.deleteById(id);
        return category;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteById(List<String> list) {
        for (String id : list) {
            Category category = categoryDao.selectById(id);

            Category unCategory = categoryDao.selectByCondition(new HashMap<String, Object>() {{
                put("code", "uncategory");
            }}).get(0);
            List<Article> articleList = articleDao.selectByCondition(new HashMap<String, Object>() {{
                put("categoryId", category.getId());
            }});
            if (articleList.size() != 0) {
                for (Article article : articleList) {
                    article.setCategoryId(unCategory.getId());
                }
                articleDao.batchUpdate(articleList);
            }
        }
        return categoryDao.batchDeleteById(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category update(Category category) {
        categoryDao.update(category);
        return categoryDao.selectById(category.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<Category> list) {
        return categoryDao.batchUpdate(list);
    }

}
