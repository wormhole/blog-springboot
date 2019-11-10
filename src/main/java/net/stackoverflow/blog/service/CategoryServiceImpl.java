package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.ArticleDao;
import net.stackoverflow.blog.dao.CategoryDao;
import net.stackoverflow.blog.pojo.entity.Article;
import net.stackoverflow.blog.pojo.entity.Category;
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

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @return 返回结果集
     */
    @Override
    public List<Category> selectByPage(Page page) {
        return categoryDao.selectByPage(page);
    }

    /**
     * 条件查询
     *
     * @param searchMap 条件查询参数
     * @return 返回结果集
     */
    @Override
    public List<Category> selectByCondition(Map<String, Object> searchMap) {
        return categoryDao.selectByCondition(searchMap);
    }

    /**
     * 根据id查询
     *
     * @param id 分类主键
     * @return 返回查询结果
     */
    @Override
    public Category selectById(String id) {
        Category category = (Category) RedisCacheUtils.get("category:" + id);
        if (category != null) {
            return category;
        } else {
            category = categoryDao.selectById(id);
            if (category != null) {
                RedisCacheUtils.set("category:" + id, category, 1800);
            }
            return category;
        }
    }

    /**
     * 根据主键批量查询分类
     *
     * @param ids 主键列表
     * @return 结果集
     */
    @Override
    public List<Category> selectByIds(List<String> ids) {
        return categoryDao.selectByIds(ids);
    }

    /**
     * 新增分类
     *
     * @param category 新增的分类实体对象
     * @return 新增后的分类实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category insert(Category category) {
        categoryDao.insert(category);
        RedisCacheUtils.set("category:" + category.getId(), category, 1800);
        return category;
    }

    /**
     * 批量新增
     *
     * @param categorys 批量新增的文章列表
     * @return 返回批量新增的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<Category> categorys) {
        return categoryDao.batchInsert(categorys);
    }

    /**
     * 根据id删除分类
     *
     * @param id 被删除的分类主键
     * @return 返回被删除的分类实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category delete(String id) {
        Category category = categoryDao.selectById(id);

        //将所有该分类的文章设成未分类
        Category unCategory = categoryDao.selectByCondition(new HashMap<String, Object>(16) {{
            put("code", "uncategory");
        }}).get(0);
        List<Article> articles = articleDao.selectByCondition(new HashMap<String, Object>(16) {{
            put("categoryId", category.getId());
        }});

        if (articles != null && articles.size() != 0) {
            for (Article article : articles) {
                article.setCategoryId(unCategory.getId());
                RedisCacheUtils.set("article:" + article.getId(), article, 1800);
                RedisCacheUtils.set("article:" + article.getUrl(), article, 1800);
            }
            articleDao.batchUpdate(articles);
        }

        categoryDao.delete(id);
        RedisCacheUtils.del("category:" + id);
        return category;
    }

    /**
     * 根据id批量删除分类
     *
     * @param ids 被删除的分类id列表
     * @return 返回被删除的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<String> ids) {
        for (String id : ids) {
            Category category = categoryDao.selectById(id);

            //将该分类下的文章都设成未分类
            Category unCategory = categoryDao.selectByCondition(new HashMap<String, Object>(16) {{
                put("code", "uncategory");
            }}).get(0);
            List<Article> articles = articleDao.selectByCondition(new HashMap<String, Object>(16) {{
                put("categoryId", category.getId());
            }});
            if (articles != null && articles.size() != 0) {
                for (Article article : articles) {
                    article.setCategoryId(unCategory.getId());
                    RedisCacheUtils.set("article:" + article.getId(), article, 1800);
                    RedisCacheUtils.set("article:" + article.getUrl(), article, 1800);
                }
                articleDao.batchUpdate(articles);
            }
            RedisCacheUtils.del("category:" + id);
        }
        return categoryDao.batchDelete(ids);
    }

    /**
     * 更新分类
     *
     * @param category 被更新的分类实体对象
     * @return 返回更新后的分类实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category update(Category category) {
        categoryDao.update(category);
        Category newCategory = categoryDao.selectById(category.getId());
        RedisCacheUtils.set("category:" + newCategory.getId(), newCategory, 1800);
        return newCategory;
    }

    /**
     * 批量更新
     *
     * @param categorys 批量更新的分类列表
     * @return 返回被更新的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<Category> categorys) {
        int result = categoryDao.batchUpdate(categorys);
        for (Category category : categorys) {
            RedisCacheUtils.del("category:" + category.getId());
        }
        return result;
    }

}
