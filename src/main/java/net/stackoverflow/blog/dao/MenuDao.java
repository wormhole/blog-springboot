package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.po.MenuPO;
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

    List<MenuPO> selectByPage(Page page);

    List<MenuPO> selectByCondition(Map<String, Object> searchMap);

    MenuPO selectById(String id);

    int insert(MenuPO menu);

    int batchInsert(List<MenuPO> list);

    int deleteById(String id);

    int batchDeleteById(List<String> list);

    int update(MenuPO menu);

    int batchUpdate(List<MenuPO> list);

}
