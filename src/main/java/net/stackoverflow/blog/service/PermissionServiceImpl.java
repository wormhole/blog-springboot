package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.PermissionDao;
import net.stackoverflow.blog.pojo.entity.Permission;
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
        return dao.selectById(id);
    }

    /**
     * 新增权限
     *
     * @param permission 权限实体类
     * @return 新增的权限实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Permission insert(Permission permission) {
        dao.insert(permission);
        return dao.selectById(permission.getId());
    }

    /**
     * 批量新增
     *
     * @param permissions 权限列表
     * @return 新增成功记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<Permission> permissions) {
        return dao.batchInsert(permissions);
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
        Permission permission = dao.selectById(id);
        dao.deleteById(id);
        return permission;
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
        return dao.batchDeleteById(ids);
    }

    /**
     * 更新权限
     *
     * @param permission 权限实体类
     * @return 更新后的权限实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Permission update(Permission permission) {
        dao.update(permission);
        return dao.selectById(permission.getId());
    }

    /**
     * 批量更新权限
     *
     * @param permissions 权限实体类列表
     * @return 更新的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<Permission> permissions) {
        return dao.batchUpdate(permissions);
    }

}
