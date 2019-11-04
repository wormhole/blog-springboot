package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.PermissionDao;
import net.stackoverflow.blog.pojo.entity.Permission;
import net.stackoverflow.blog.util.RedisCacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 权限服务实现类
 *
 * @author 凉衫薄
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao dao;

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @return 权限列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Permission> selectByPage(Page page) {
        return dao.selectByPage(page);
    }

    /**
     * 条件查询
     *
     * @param searchMap 查询参数
     * @return 权限列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Permission> selectByCondition(Map<String, Object> searchMap) {
        return dao.selectByCondition(searchMap);
    }

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 权限实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Permission selectById(String id) {
        Permission permissionPO = (Permission) RedisCacheUtils.get("permission:" + id);
        if (permissionPO != null) {
            return permissionPO;
        } else {
            permissionPO = dao.selectById(id);
            if (permissionPO != null) {
                RedisCacheUtils.set("permission:" + id, permissionPO);
            }
            return permissionPO;
        }
    }

    /**
     * 新增权限
     *
     * @param permissionPO 权限实体类
     * @return 新增的权限实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Permission insert(Permission permissionPO) {
        dao.insert(permissionPO);
        RedisCacheUtils.set("permission:" + permissionPO.getId(), permissionPO);
        return permissionPO;
    }

    /**
     * 批量新增
     *
     * @param permissionPOs 权限列表
     * @return 新增成功记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<Permission> permissionPOs) {
        return dao.batchInsert(permissionPOs);
    }

    /**
     * 根据主键删除权限
     *
     * @param id 主键
     * @return 被删除的权限实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Permission deleteById(String id) {
        Permission permissionPO = dao.selectById(id);
        dao.deleteById(id);
        RedisCacheUtils.del("permission:" + id);
        return permissionPO;
    }

    /**
     * 根据主键批量删除权限
     *
     * @param ids 权限主键列表
     * @return 被删除的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteById(List<String> ids) {
        int result = dao.batchDeleteById(ids);
        for (String id : ids) {
            RedisCacheUtils.del("permission:" + id);
        }
        return result;
    }

    /**
     * 更新权限
     *
     * @param permissionPO 权限实体类
     * @return 更新后的权限实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Permission update(Permission permissionPO) {
        dao.update(permissionPO);
        Permission newPermissionPO = dao.selectById(permissionPO.getId());
        RedisCacheUtils.set("permission" + newPermissionPO.getId(), newPermissionPO);
        return newPermissionPO;
    }

    /**
     * 批量更新权限
     *
     * @param permissionPOs 权限实体类列表
     * @return 更新的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<Permission> permissionPOs) {
        int result = dao.batchUpdate(permissionPOs);
        for (Permission permissionPO : permissionPOs) {
            RedisCacheUtils.del("permission:" + permissionPO.getId());
        }
        return result;
    }

}
