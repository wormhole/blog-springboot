package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 权限Mapper接口
 *
 * @author 凉衫薄
 */
@Repository
public interface PermissionDao {

    List<Permission> selectByPage(Page page);

    List<Permission> selectByCondition(Map<String, Object> searchMap);

    Permission selectById(String id);

    int insert(Permission permission);

    int batchInsert(List<Permission> permissions);

    int delete(String id);

    int batchDelete(List<String> ids);

    int update(Permission permission);

    int batchUpdate(List<Permission> permissions);

}
