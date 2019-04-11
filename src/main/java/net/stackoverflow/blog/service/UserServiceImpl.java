package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.*;
import net.stackoverflow.blog.pojo.po.*;
import net.stackoverflow.blog.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务实现类
 *
 * @author 凉衫薄
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private RolePermissionDao rolePermissionDao;

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @return 用户列表
     */
    @Override
    public List<UserPO> selectByPage(Page page) {
        return userDao.selectByPage(page);
    }

    /**
     * 条件查询
     *
     * @param searchMap 查询参数
     * @return 用户列表
     */
    @Override
    public List<UserPO> selectByCondition(Map<String, Object> searchMap) {
        return userDao.selectByCondition(searchMap);
    }

    /**
     * 根据主键查询
     *
     * @param id 用户主键
     * @return 用户实体类
     */
    @Override
    public UserPO selectById(String id) {
        return userDao.selectById(id);
    }

    /**
     * 新增用户
     *
     * @param user 用户实体类
     * @return 新增后的用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserPO insert(UserPO user) {
        user.setSalt(PasswordUtils.getSalt());
        user.setPassword(PasswordUtils.encryptPassword(user.getSalt(), user.getPassword()));
        userDao.insert(user);
        return userDao.selectById(user.getId());
    }

    /**
     * 批量新增用户
     *
     * @param users 用户列表
     * @return 新增记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<UserPO> users) {
        for (UserPO user : users) {
            user.setSalt(PasswordUtils.getSalt());
            user.setPassword(PasswordUtils.encryptPassword(user.getSalt(), user.getPassword()));
        }
        return userDao.batchInsert(users);
    }

    /**
     * 根据主键删除用户
     *
     * @param id 用户主键
     * @return 用户实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserPO deleteById(String id) {
        UserPO user = userDao.selectById(id);
        userDao.deleteById(id);
        return user;
    }

    /**
     * 根据主键批量删除
     *
     * @param ids 主键列表
     * @return 删除记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteById(List<String> ids) {
        return userDao.batchDeleteById(ids);
    }

    /**
     * 更新用户
     *
     * @param user 用户实体类
     * @return 更新后的用户实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserPO update(UserPO user) {
        userDao.update(user);
        return userDao.selectById(user.getId());
    }

    /**
     * 批量更新用户
     *
     * @param users 用户列表
     * @return 更新的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<UserPO> users) {
        return userDao.batchUpdate(users);
    }

    /**
     * 授予用户角色
     *
     * @param roleId 角色主键
     * @param userId 用户主键
     * @return 用户-角色中间表实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserRolePO grantRole(String userId, String roleId) {

        UserPO user = userDao.selectById(userId);
        RolePO role = roleDao.selectById(roleId);

        if (user == null || role == null) {
            return null;
        }

        UserRolePO userRole = new UserRolePO();
        userRole.setRoleId(roleId);
        userRole.setUserId(userId);
        userRoleDao.insert(userRole);
        return userRole;
    }

    /**
     * 收回用户角色
     *
     * @param roleId 角色主键
     * @param userId 用户主键
     * @return 用户-角色中间表实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserRolePO revokeRole(String userId, String roleId) {

        List<UserRolePO> userRoles = userRoleDao.selectByCondition(new HashMap<String, Object>() {{
            put("userId", userId);
            put("roleId", roleId);
        }});

        if (userRoles.size() == 0) {
            return null;
        }

        userRoleDao.deleteById(userRoles.get(0).getId());
        return userRoles.get(0);

    }

    /**
     * 返回所有角色
     *
     * @param userId 用户主键
     * @return 角色列表
     */
    @Override
    public List<RolePO> getRoleByUserId(String userId) {
        List<UserRolePO> userRoles = userRoleDao.selectByCondition(new HashMap<String, Object>() {{
            put("userId", userId);
        }});
        List<RolePO> roles = null;
        if ((null != userRoles) && (userRoles.size() != 0)) {
            roles = new ArrayList<>();
            for (UserRolePO userRole : userRoles) {
                RolePO role = roleDao.selectById(userRole.getId());
                roles.add(role);
            }
        }
        return roles;
    }

    /**
     * 返回所有权限
     *
     * @param userId 用户主键
     * @return 权限列表
     */
    @Override
    public List<PermissionPO> getPermissionByUserId(String userId) {
        List<RolePO> roles = getRoleByUserId(userId);
        Map<String, PermissionPO> permissionMap = new HashMap<>();
        for (RolePO role : roles) {
            List<RolePermissionPO> rolePermissions = rolePermissionDao.selectByCondition(new HashMap<String, Object>() {{
                put("roleId", role.getId());
            }});
            if ((null != rolePermissions) && (rolePermissions.size() != 0)) {
                for (RolePermissionPO rolePermission : rolePermissions) {
                    PermissionPO permission = permissionDao.selectById(rolePermission.getPermissionId());
                    permissionMap.put(permission.getId(), permission);
                }
            }
        }
        List<PermissionPO> permissions = (List<PermissionPO>) permissionMap.values();
        return permissions;
    }

}
