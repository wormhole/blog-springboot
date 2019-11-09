package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 角色Mapper接口
 *
 * @author 凉衫薄
 */
@Repository
public interface RoleDao {

    List<Role> selectByPage(Page page);

    List<Role> selectByCondition(Map<String, Object> searchMap);

    Role selectById(String id);

    int insert(Role role);

    int batchInsert(List<Role> roles);

    int delete(String id);

    int batchDelete(List<String> ids);

    int update(Role role);

    int batchUpdate(List<Role> roles);

}
