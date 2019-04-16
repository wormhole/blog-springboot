package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.*;
import net.stackoverflow.blog.pojo.po.*;
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
        UserPO userPO = (UserPO) RedisCacheUtils.get("user:" + id);
        if (userPO != null) {
            return userPO;
        } else {
            userPO = userDao.selectById(id);
            if (userPO != null) {
                RedisCacheUtils.set("user:" + id, userPO);
            }
            return userPO;
        }
    }

    /**
     * 新增用户
     *
     * @param userPO 用户实体类
     * @return 新增后的用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserPO insert(UserPO userPO) {
        userPO.setSalt(PasswordUtils.getSalt());
        userPO.setPassword(PasswordUtils.encryptPassword(userPO.getSalt(), userPO.getPassword()));
        userDao.insert(userPO);
        RedisCacheUtils.set("user:" + userPO.getId(), userPO);
        return userDao.selectById(userPO.getId());
    }

    /**
     * 批量新增用户
     *
     * @param userPOs 用户列表
     * @return 新增记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<UserPO> userPOs) {
        for (UserPO userPO : userPOs) {
            userPO.setSalt(PasswordUtils.getSalt());
            userPO.setPassword(PasswordUtils.encryptPassword(userPO.getSalt(), userPO.getPassword()));
        }
        return userDao.batchInsert(userPOs);
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
        UserPO userPO = userDao.selectById(id);
        userDao.deleteById(id);
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
    public int batchDeleteById(List<String> ids) {
        int result = userDao.batchDeleteById(ids);
        for (String id : ids) {
            RedisCacheUtils.del("user:" + id);
        }
        return result;
    }

    /**
     * 更新用户
     *
     * @param userPO 用户实体类
     * @return 更新后的用户实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserPO update(UserPO userPO) {
        userDao.update(userPO);
        UserPO newUserPO = userDao.selectById(userPO.getId());
        RedisCacheUtils.set("user:" + newUserPO.getId(), newUserPO);
        return newUserPO;
    }

    /**
     * 批量更新用户
     *
     * @param userPOs 用户列表
     * @return 更新的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<UserPO> userPOs) {
        int result = userDao.batchUpdate(userPOs);
        for (UserPO userPO : userPOs) {
            RedisCacheUtils.del("user:" + userPO.getId());
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
    public UserRolePO grantRole(String userId, String roleId) {

        UserPO userPO = userDao.selectById(userId);
        RolePO rolePO = roleDao.selectById(roleId);

        if (userPO == null || rolePO == null) {
            return null;
        }

        UserRolePO userRolePO = new UserRolePO();
        userRolePO.setRoleId(roleId);
        userRolePO.setUserId(userId);
        userRoleDao.insert(userRolePO);
        return userRolePO;
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

        List<UserRolePO> userRolePOs = userRoleDao.selectByCondition(new HashMap<String, Object>() {{
            put("userId", userId);
            put("roleId", roleId);
        }});

        if (userRolePOs.size() == 0) {
            return null;
        }

        userRoleDao.deleteById(userRolePOs.get(0).getId());
        return userRolePOs.get(0);

    }

    /**
     * 返回所有角色
     *
     * @param userId 用户主键
     * @return 角色列表
     */
    @Override
    public List<RolePO> getRoleByUserId(String userId) {
        List<UserRolePO> userRolePOs = userRoleDao.selectByCondition(new HashMap<String, Object>() {{
            put("userId", userId);
        }});
        List<RolePO> rolePOs = null;
        if ((null != userRolePOs) && (userRolePOs.size() != 0)) {
            rolePOs = new ArrayList<>();
            for (UserRolePO userRolePO : userRolePOs) {
                RolePO rolePO = roleDao.selectById(userRolePO.getId());
                rolePOs.add(rolePO);
            }
        }
        return rolePOs;
    }

    /**
     * 返回所有权限
     *
     * @param userId 用户主键
     * @return 权限列表
     */
    @Override
    public List<PermissionPO> getPermissionByUserId(String userId) {
        List<RolePO> rolePOs = getRoleByUserId(userId);
        Map<String, PermissionPO> permissionMap = new HashMap<>();
        for (RolePO rolePO : rolePOs) {
            List<RolePermissionPO> rolePermissionPOs = rolePermissionDao.selectByCondition(new HashMap<String, Object>() {{
                put("roleId", rolePO.getId());
            }});
            if ((null != rolePermissionPOs) && (rolePermissionPOs.size() != 0)) {
                for (RolePermissionPO rolePermissionPO : rolePermissionPOs) {
                    PermissionPO permission = permissionDao.selectById(rolePermissionPO.getPermissionId());
                    permissionMap.put(permission.getId(), permission);
                }
            }
        }
        List<PermissionPO> permissionPOs = (List<PermissionPO>) permissionMap.values();
        return permissionPOs;
    }

}
