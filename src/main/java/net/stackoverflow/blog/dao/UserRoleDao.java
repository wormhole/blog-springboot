package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.po.UserRolePO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 用户-角色中间表Mapper接口
 *
 * @author 凉衫薄
 */
@Mapper
public interface UserRoleDao {

    List<UserRolePO> selectByPage(Page page);

    List<UserRolePO> selectByCondition(Map<String, Object> searchMap);

    UserRolePO selectById(String id);

    int insert(UserRolePO userRole);

    int batchInsert(List<UserRolePO> userRoles);

    int deleteById(String id);

    int batchDeleteById(List<String> ids);

    int update(UserRolePO userRole);

    int batchUpdate(List<UserRolePO> userRoles);

}
