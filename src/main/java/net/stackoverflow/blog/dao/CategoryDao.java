package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.po.CategoryPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 分类表DAO
 *
 * @author 凉衫薄
 */
@Mapper
public interface CategoryDao {

    List<CategoryPO> selectByPage(Page page);

    List<CategoryPO> selectByCondition(Map<String, Object> searchMap);

    CategoryPO selectById(String id);

    int insert(CategoryPO category);

    int batchInsert(List<CategoryPO> list);

    int deleteById(String id);

    int batchDeleteById(List<String> list);

    int update(CategoryPO category);

    int batchUpdate(List<CategoryPO> list);

}
