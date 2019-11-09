package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.*;
import net.stackoverflow.blog.pojo.entity.*;
import net.stackoverflow.blog.util.PasswordUtils;
import net.stackoverflow.blog.util.RedisCacheUtils;
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
    public List<User> selectByPage(Page page) {
        return userDao.selectByPage(page);
    }

    /**
     * 条件查询
     *
     * @param searchMap 查询参数
     * @return 用户列表
     */
    @Override
    public List<User> selectByCondition(Map<String, Object> searchMap) {
        return userDao.selectByCondition(searchMap);
    }

    /**
     * 根据主键查询
     *
     * @param id 用户主键
     * @return 用户实体类对象
     */
    @Override
    public User selectById(String id) {
        User user = (User) RedisCacheUtils.get("user:" + id);
        if (user != null) {
            return user;
        } else {
            user = userDao.selectById(id);
            if (user != null) {
                RedisCacheUtils.set("user:" + id, user, 1800);
            }
            return user;
        }
    }

    /**
     * 新增用户
     *
     * @param user 用户实体类对象
     * @return 新增后的用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User insert(User user) {
        user.setSalt(PasswordUtils.getSalt());
        user.setPassword(PasswordUtils.encryptPassword(user.getSalt(), user.getPassword()));
        userDao.insert(user);
        RedisCacheUtils.set("user:" + user.getId(), user, 1800);
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
    public int batchInsert(List<User> users) {
        for (User user : users) {
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
    public User delete(String id) {
        User userPO = userDao.selectById(id);
        userDao.delete(id);
        RedisCacheUtils.del("user:" + id);
        return userPO;
    }

    /**
     * 根据主键批量删除
     *
     * @param ids 主键列表
     * @return 删除记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<String> ids) {
        int result = userDao.batchDelete(ids);
        for (String id : ids) {
            RedisCacheUtils.del("user:" + id);
        }
        return result;
    }

    /**
     * 更新用户
     *
     * @param user 用户实体类
     * @return 更新后的用户实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User update(User user) {
        userDao.update(user);
        User newUser = userDao.selectById(user.getId());
        RedisCacheUtils.set("user:" + newUser.getId(), newUser, 1800);
        return newUser;
    }

    /**
     * 批量更新用户
     *
     * @param users 用户列表
     * @return 更新的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<User> users) {
        int result = userDao.batchUpdate(users);
        for (User user : users) {
            RedisCacheUtils.del("user:" + user.getId());
        }
        return result;
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
    public UserRole grantRole(String userId, String roleId) {

        User user = userDao.selectById(userId);
        Role role = roleDao.selectById(roleId);

        if (user == null || role == null) {
            return null;
        }

        UserRole userRole = new UserRole();
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
    public UserRole revokeRole(String userId, String roleId) {

        List<UserRole> userRoles = userRoleDao.selectByCondition(new HashMap<String, Object>(16) {{
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
    public List<Role> getRoleByUserId(String userId) {
        List<UserRole> userRoles = userRoleDao.selectByCondition(new HashMap<String, Object>(16) {{
            put("userId", userId);
        }});
        List<Role> roles = null;
        if ((null != userRoles) && (userRoles.size() != 0)) {
            roles = new ArrayList<>();
            for (UserRole userRole : userRoles) {
                Role role = roleDao.selectById(userRole.getId());
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
    public List<Permission> getPermissionByUserId(String userId) {
        List<Role> roles = getRoleByUserId(userId);
        Map<String, Permission> permissionMap = new HashMap<>(16);
        for (Role role : roles) {
            List<RolePermission> rolePermissions = rolePermissionDao.selectByCondition(new HashMap<String, Object>(16) {{
                put("roleId", role.getId());
            }});
            if ((null != rolePermissions) && (rolePermissions.size() != 0)) {
                for (RolePermission rolePermission : rolePermissions) {
                    Permission permission = permissionDao.selectById(rolePermission.getPermissionId());
                    permissionMap.put(permission.getId(), permission);
                }
            }
        }
        List<Permission> permissions = (List<Permission>) permissionMap.values();
        return permissions;
    }

}
