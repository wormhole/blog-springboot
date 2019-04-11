package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.PermissionDao;
import net.stackoverflow.blog.dao.RoleDao;
import net.stackoverflow.blog.dao.RolePermissionDao;
import net.stackoverflow.blog.pojo.po.PermissionPO;
import net.stackoverflow.blog.pojo.po.RolePO;
import net.stackoverflow.blog.pojo.po.RolePermissionPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色服务实现类
 *
 * @author 凉衫薄
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RolePermissionDao rolePermissionDao;

    /**
     * 分页查询
     *
     * @param page 分页查询参数
     * @return 角色列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RolePO> selectByPage(Page page) {
        return roleDao.selectByPage(page);
    }

    /**
     * 条件查询
     *
     * @param searchMap 查询参数
     * @return 角色列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RolePO> selectByCondition(Map<String, Object> searchMap) {
        return roleDao.selectByCondition(searchMap);
    }

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 角色实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RolePO selectById(String id) {
        return roleDao.selectById(id);
    }

    /**
     * 新增角色
     *
     * @param role 角色实体类
     * @return 新增的角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RolePO insert(RolePO role) {
        roleDao.insert(role);
        return roleDao.selectById(role.getId());
    }

    /**
     * 批量新增
     *
     * @param roles 角色列表
     * @return 新增的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<RolePO> roles) {
        return roleDao.batchInsert(roles);
    }

    /**
     * 根据主键删除
     *
     * @param id 角色主键
     * @return 删除的角色实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RolePO deleteById(String id) {
        RolePO role = roleDao.selectById(id);
        roleDao.deleteById(id);
        return role;
    }

    /**
     * 根据主键批量删除
     *
     * @param ids 主键列表
     * @return 删除的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteById(List<String> ids) {
        return roleDao.batchDeleteById(ids);
    }

    /**
     * 更新角色
     *
     * @param role 角色实体类
     * @return 更新后的角色实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RolePO update(RolePO role) {
        roleDao.update(role);
        return roleDao.selectById(role.getId());
    }

    /**
     * 批量更新角色
     *
     * @param roles 角色列表
     * @return 更新的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<RolePO> roles) {
        return roleDao.batchUpdate(roles);
    }

    /**
     * 给角色赋予权限
     *
     * @param roleId 角色主键
     * @param permissionId 权限主键
     * @return 角色-权限中间表实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RolePermissionPO grantPermission(String roleId, String permissionId) {
        RolePO role = roleDao.selectById(roleId);
        PermissionPO permission = permissionDao.selectById(permissionId);

        if (role == null || permission == null) {
            return null;
        }

        RolePermissionPO rolePermission = new RolePermissionPO();
        rolePermission.setRoleId(roleId);
        rolePermission.setPermissionId(permissionId);
        rolePermissionDao.insert(rolePermission);

        return rolePermission;
    }

    /**
     * 给角色收回权限
     *
     * @param roleId 权限主键
     * @param permissionId 角色主键
     * @return 角色-权限中间表实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RolePermissionPO revokePermission(String roleId, String permissionId) {

        List<RolePermissionPO> rolePermissions = rolePermissionDao.selectByCondition(new HashMap<String, Object>() {{
            put("roleId", roleId);
            put("permissionId", permissionId);
        }});

        if (rolePermissions.size() == 0) {
            return null;
        }

        rolePermissionDao.deleteById(rolePermissions.get(0).getId());
        return rolePermissions.get(0);
    }

    /**
     * 获取权限列表
     *
     * @param roleId 角色主键
     * @return 权限列表
     */
    @Override
    public List<PermissionPO> getPermissionByRoleId(String roleId) {
        List<PermissionPO> permissions = null;

        List<RolePermissionPO> rolePermissions = rolePermissionDao.selectByCondition(new HashMap<String, Object>() {{
            put("roleId", roleId);
        }});
        if ((null != rolePermissions) && (rolePermissions.size() != 0)) {
            permissions = new ArrayList<>();
            for (RolePermissionPO rolePermission : rolePermissions) {
                PermissionPO permission = permissionDao.selectById(rolePermission.getPermissionId());
                permissions.add(permission);
            }
        }

        return permissions;
    }

}
