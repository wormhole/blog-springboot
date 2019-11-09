package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.RolePermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 角色-权限中间表Mapper接口
 *
 * @author 凉衫薄
 */
@Mapper
public interface RolePermissionDao {

    List<RolePermission> selectByPage(Page page);

    List<RolePermission> selectByCondition(Map<String, Object> searchMap);

    RolePermission selectById(String id);

    int insert(RolePermission rolePermission);

    int batchInsert(List<RolePermission> rolePermissions);

    int delete(String id);

    int batchDelete(List<String> ids);

    int update(String id);

    int batchUpdate(List<RolePermission> rolePermissions);
}
