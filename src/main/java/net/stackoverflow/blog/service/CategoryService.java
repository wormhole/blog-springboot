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

    CategoryPO insert(CategoryPO category);

    int batchInsert(List<CategoryPO> list);

    CategoryPO deleteById(String id);

    int batchDeleteById(List<String> list);

    CategoryPO update(CategoryPO category);

    int batchUpdate(List<CategoryPO> list);
    
}
