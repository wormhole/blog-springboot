package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.po.PermissionPO;

import java.util.List;
import java.util.Map;

/**
 * 权限服务接口
 *
 * @author 凉衫薄
 */
public interface PermissionService {

    List<PermissionPO> selectByPage(Page page);

    List<PermissionPO> selectByCondition(Map<String, Object> searchMap);

    PermissionPO selectById(String id);

    PermissionPO insert(PermissionPO permissionPO);

    int batchInsert(List<PermissionPO> permissionPOs);

    PermissionPO deleteById(String id);

    int batchDeleteById(List<String> ids);

    PermissionPO update(PermissionPO permissionPO);

    int batchUpdate(List<PermissionPO> permissionPOs);

}
