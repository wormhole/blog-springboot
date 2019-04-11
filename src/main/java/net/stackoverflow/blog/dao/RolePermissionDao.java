package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.po.RolePermissionPO;
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

    List<RolePermissionPO> selectByPage(Page page);

    List<RolePermissionPO> selectByCondition(Map<String, Object> searchMap);

    RolePermissionPO selectById(String id);

    int insert(RolePermissionPO rolePermission);

    int batchInsert(List<RolePermissionPO> rolePermissions);

    int deleteById(String id);

    int batchDeleteById(List<String> ids);

    int update(String id);

    int batchUpdate(List<RolePermissionPO> rolePermissions);
}
