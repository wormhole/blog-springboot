package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.PermissionDao;
import net.stackoverflow.blog.pojo.po.PermissionPO;
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
    public List<PermissionPO> selectByPage(Page page) {
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
    public List<PermissionPO> selectByCondition(Map<String, Object> searchMap) {
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
    public PermissionPO selectById(String id) {
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
    public PermissionPO insert(PermissionPO permission) {
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
    public int batchInsert(List<PermissionPO> permissions) {
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
    public PermissionPO deleteById(String id) {
        PermissionPO permission = dao.selectById(id);
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
    public PermissionPO update(PermissionPO permission) {
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
    public int batchUpdate(List<PermissionPO> permissions) {
        return dao.batchUpdate(permissions);
    }

}
