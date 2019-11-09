package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 分类表DAO
 *
 * @author 凉衫薄
 */
@Repository
public interface CategoryDao {

    List<Category> selectByPage(Page page);

    List<Category> selectByCondition(Map<String, Object> searchMap);

    Category selectById(String id);

    int insert(Category category);

    int batchInsert(List<Category> list);

    int delete(String id);

    int batchDelete(List<String> list);

    int update(Category category);

    int batchUpdate(List<Category> list);

}
