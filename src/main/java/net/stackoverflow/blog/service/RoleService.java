package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.po.PermissionPO;
import net.stackoverflow.blog.pojo.po.RolePO;
import net.stackoverflow.blog.pojo.po.RolePermissionPO;

import java.util.List;
import java.util.Map;

/**
 * 角色服务接口
 *
 * @author 凉衫薄
 */
public interface RoleService {

    List<RolePO> selectByPage(Page page);

    List<RolePO> selectByCondition(Map<String, Object> searchMap);

    RolePO selectById(String id);

    RolePO insert(RolePO role);

    int batchInsert(List<RolePO> roles);

    RolePO deleteById(String id);

    int batchDeleteById(List<String> ids);

    RolePO update(RolePO role);

    int batchUpdate(List<RolePO> roles);

    RolePermissionPO grantPermission(String roleId, String permissionId);

    RolePermissionPO revokePermission(String roleId, String permissionId);

    List<PermissionPO> getPermissionByRoleId(String roleId);

}
