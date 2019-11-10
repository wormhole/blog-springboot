package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * menu表DAO类
 *
 * @author 凉衫薄
 */
@Mapper
public interface MenuDao {

    List<Menu> selectByPage(Page page);

    List<Menu> selectByCondition(Map<String, Object> searchMap);

    Menu selectById(String id);

    List<Menu> selectByIds(List<String> ids);

    int insert(Menu menu);

    int batchInsert(List<Menu> list);

    int delete(String id);

    int batchDelete(List<String> list);

    int update(Menu menu);

    int batchUpdate(List<Menu> list);

}
