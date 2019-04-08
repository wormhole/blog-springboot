package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.Permission;

import java.util.List;
import java.util.Map;

/**
 * 权限服务接口
 *
 * @author 凉衫薄
 */
public interface PermissionService {

    List<Permission> selectByPage(Page page);

    List<Permission> selectByCondition(Map<String, Object> searchMap);

    Permission selectById(String id);

    Permission insert(Permission permission);

    int batchInsert(List<Permission> permissions);

    Permission deleteById(String id);

    int batchDeleteById(List<String> ids);

    Permission update(Permission permission);

    int batchUpdate(List<Permission> permissions);

}
