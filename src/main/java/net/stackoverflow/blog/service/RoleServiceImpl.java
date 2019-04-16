package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.PermissionDao;
import net.stackoverflow.blog.dao.RoleDao;
import net.stackoverflow.blog.dao.RolePermissionDao;
import net.stackoverflow.blog.pojo.po.PermissionPO;
import net.stackoverflow.blog.pojo.po.RolePO;
import net.stackoverflow.blog.pojo.po.RolePermissionPO;
import net.stackoverflow.blog.util.RedisCacheUtils;
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
        RolePO rolePO = (RolePO) RedisCacheUtils.get("role:" + id);
        if (rolePO != null) {
            return rolePO;
        } else {
            rolePO = roleDao.selectById(id);
            if (rolePO != null) {
                RedisCacheUtils.set("role:" + id, rolePO);
            }
            return rolePO;
        }
    }

    /**
     * 新增角色
     *
     * @param rolePO 角色实体类
     * @return 新增的角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RolePO insert(RolePO rolePO) {
        roleDao.insert(rolePO);
        RedisCacheUtils.set("role:" + rolePO.getId(), rolePO);
        return rolePO;
    }

    /**
     * 批量新增
     *
     * @param rolePOs 角色列表
     * @return 新增的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<RolePO> rolePOs) {
        return roleDao.batchInsert(rolePOs);
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
        RolePO rolePO = roleDao.selectById(id);
        roleDao.deleteById(id);
        RedisCacheUtils.del("role:" + id);
        return rolePO;
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
        int result = roleDao.batchDeleteById(ids);
        for (String id : ids) {
            RedisCacheUtils.del("role:" + id);
        }
        return result;
    }

    /**
     * 更新角色
     *
     * @param rolePO 角色实体类
     * @return 更新后的角色实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RolePO update(RolePO rolePO) {
        roleDao.update(rolePO);
        RolePO newRolePO = roleDao.selectById(rolePO.getId());
        RedisCacheUtils.set("role:" + newRolePO.getId(), newRolePO);
        return newRolePO;
    }

    /**
     * 批量更新角色
     *
     * @param rolePOs 角色列表
     * @return 更新的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<RolePO> rolePOs) {
        int result = roleDao.batchUpdate(rolePOs);
        for (RolePO rolePO : rolePOs) {
            RedisCacheUtils.del("role:" + rolePO.getId());
        }
        return result;
    }

    /**
     * 给角色赋予权限
     *
     * @param roleId       角色主键
     * @param permissionId 权限主键
     * @return 角色-权限中间表实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RolePermissionPO grantPermission(String roleId, String permissionId) {
        RolePO rolePO = roleDao.selectById(roleId);
        PermissionPO permissionPO = permissionDao.selectById(permissionId);

        if (rolePO == null || permissionPO == null) {
            return null;
        }

        RolePermissionPO rolePermissionPO = new RolePermissionPO();
        rolePermissionPO.setRoleId(roleId);
        rolePermissionPO.setPermissionId(permissionId);
        rolePermissionDao.insert(rolePermissionPO);

        return rolePermissionPO;
    }

    /**
     * 给角色收回权限
     *
     * @param roleId       权限主键
     * @param permissionId 角色主键
     * @return 角色-权限中间表实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RolePermissionPO revokePermission(String roleId, String permissionId) {

        List<RolePermissionPO> rolePermissionPOs = rolePermissionDao.selectByCondition(new HashMap<String, Object>() {{
            put("roleId", roleId);
            put("permissionId", permissionId);
        }});

        if (rolePermissionPOs.size() == 0) {
            return null;
        }

        rolePermissionDao.deleteById(rolePermissionPOs.get(0).getId());
        return rolePermissionPOs.get(0);
    }

    /**
     * 获取权限列表
     *
     * @param roleId 角色主键
     * @return 权限列表
     */
    @Override
    public List<PermissionPO> getPermissionByRoleId(String roleId) {
        List<PermissionPO> permissionPOs = null;

        List<RolePermissionPO> rolePermissionPOs = rolePermissionDao.selectByCondition(new HashMap<String, Object>() {{
            put("roleId", roleId);
        }});
        if ((null != rolePermissionPOs) && (rolePermissionPOs.size() != 0)) {
            permissionPOs = new ArrayList<>();
            for (RolePermissionPO rolePermissionPO : rolePermissionPOs) {
                PermissionPO permissionPO = permissionDao.selectById(rolePermissionPO.getPermissionId());
                permissionPOs.add(permissionPO);
            }
        }

        return permissionPOs;
    }

}
