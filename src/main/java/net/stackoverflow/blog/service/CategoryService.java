package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.po.CategoryPO;

import java.util.List;
import java.util.Map;

/**
 * 分类服务接口
 *
 * @author 凉衫薄
 */
public interface CategoryService {

    List<CategoryPO> selectByPage(Page page);

    List<CategoryPO> selectByCondition(Map<String, Object> searchMap);

    CategoryPO selectById(String id);

    CategoryPO insert(CategoryPO categoryPO);

    int batchInsert(List<CategoryPO> categoryPOs);

    CategoryPO deleteById(String id);

    int batchDeleteById(List<String> ids);

    CategoryPO update(CategoryPO categoryPO);

    int batchUpdate(List<CategoryPO> categoryPOs);
    
}
