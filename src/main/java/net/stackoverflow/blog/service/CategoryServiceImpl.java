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

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @return 返回结果集
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<CategoryPO> selectByPage(Page page) {
        return categoryDao.selectByPage(page);
    }

    /**
     * 条件查询
     *
     * @param searchMap 条件查询参数
     * @return 返回结果集
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<CategoryPO> selectByCondition(Map<String, Object> searchMap) {
        return categoryDao.selectByCondition(searchMap);
    }

    /**
     * 根据id查询
     *
     * @param id 分类主键
     * @return 返回查询结果
     */
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

    /**
     * 新增分类
     *
     * @param categoryPO 新增的分类PO
     * @return 新增后的分类PO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CategoryPO insert(CategoryPO categoryPO) {
        categoryDao.insert(categoryPO);
        RedisCacheUtils.set("category:" + categoryPO.getId(), categoryPO);
        return categoryPO;
    }

    /**
     * 批量新增
     *
     * @param categoryPOs 批量新增的文章列表
     * @return 返回批量新增的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<CategoryPO> categoryPOs) {
        return categoryDao.batchInsert(categoryPOs);
    }

    /**
     * 根据id删除分类
     *
     * @param id 被删除的分类主键
     * @return 返回被删除的分类PO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CategoryPO deleteById(String id) {
        CategoryPO category = categoryDao.selectById(id);

        //将所有该分类的文章设成未分类
        CategoryPO unCategoryPO = categoryDao.selectByCondition(new HashMap<String, Object>() {{
            put("code", "uncategory");
        }}).get(0);
        List<ArticlePO> articlePOs = articleDao.selectByCondition(new HashMap<String, Object>() {{
            put("categoryId", category.getId());
        }});

        if (articlePOs != null && articlePOs.size() != 0) {
            for (ArticlePO articlePO : articlePOs) {
                articlePO.setCategoryId(unCategoryPO.getId());
                RedisCacheUtils.set("article:" + articlePO.getId(), articlePO);
                RedisCacheUtils.set("article:" + articlePO.getUrl(), articlePO);
            }
            articleDao.batchUpdate(articlePOs);
        }

        categoryDao.deleteById(id);
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
    public int batchDeleteById(List<String> ids) {
        for (String id : ids) {
            CategoryPO categoryPO = categoryDao.selectById(id);

            //将该分类下的文章都设成未分类
            CategoryPO unCategoryPO = categoryDao.selectByCondition(new HashMap<String, Object>() {{
                put("code", "uncategory");
            }}).get(0);
            List<ArticlePO> articlePOs = articleDao.selectByCondition(new HashMap<String, Object>() {{
                put("categoryId", categoryPO.getId());
            }});
            if (articlePOs != null && articlePOs.size() != 0) {
                for (ArticlePO articlePO : articlePOs) {
                    articlePO.setCategoryId(unCategoryPO.getId());
                    RedisCacheUtils.set("article:" + articlePO.getId(), articlePO);
                    RedisCacheUtils.set("article:" + articlePO.getUrl(), articlePO);
                }
                articleDao.batchUpdate(articlePOs);
            }
            RedisCacheUtils.del("category:" + id);
        }
        return categoryDao.batchDeleteById(ids);
    }

    /**
     * 更新分类
     *
     * @param categoryPO 被更新的分类PO类
     * @return 返回更新后的分类PO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CategoryPO update(CategoryPO categoryPO) {
        categoryDao.update(categoryPO);
        CategoryPO newCategoryPO = categoryDao.selectById(categoryPO.getId());
        RedisCacheUtils.set("category:" + newCategoryPO.getId(), newCategoryPO);
        return newCategoryPO;
    }

    /**
     * 批量更新
     *
     * @param categoryPOs 批量更新的分类列表
     * @return 返回被更新的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<CategoryPO> categoryPOs) {
        int result = categoryDao.batchUpdate(categoryPOs);
        for (CategoryPO categoryPO : categoryPOs) {
            RedisCacheUtils.del("category:" + categoryPO.getId());
        }
        return result;
    }

}
