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

    Category insert(Category category);

    int batchInsert(List<Category> list);

    Category deleteById(String id);

    int batchDeleteById(List<String> list);

    Category update(Category category);

    int batchUpdate(List<Category> list);
    
}
