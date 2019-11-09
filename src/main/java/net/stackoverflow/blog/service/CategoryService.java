package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.Category;

import java.util.List;
import java.util.Map;

/**
 * 分类服务接口
 *
 * @author 凉衫薄
 */
public interface CategoryService {

    List<Category> selectByPage(Page page);

    List<Category> selectByCondition(Map<String, Object> searchMap);

    Category selectById(String id);

    Category insert(Category categoryPO);

    int batchInsert(List<Category> categoryPOs);

    Category delete(String id);

    int batchDelete(List<String> ids);

    Category update(Category categoryPO);

    int batchUpdate(List<Category> categoryPOs);
    
}
