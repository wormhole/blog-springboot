package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.Permission;
import net.stackoverflow.blog.pojo.entity.Role;
import net.stackoverflow.blog.pojo.entity.RolePermission;

import java.util.List;
import java.util.Map;

/**
 * 角色服务接口
 *
 * @author 凉衫薄
 */
public interface RoleService {

    List<Role> selectByPage(Page page);

    List<Role> selectByCondition(Map<String, Object> searchMap);

    Role selectById(String id);

    Role insert(Role rolePO);

    int batchInsert(List<Role> rolePOs);

    Role deleteById(String id);

    int batchDeleteById(List<String> ids);

    Role update(Role rolePO);

    int batchUpdate(List<Role> rolePOs);

    RolePermission grantPermission(String roleId, String permissionId);

    RolePermission revokePermission(String roleId, String permissionId);

    List<Permission> getPermissionByRoleId(String roleId);

}
