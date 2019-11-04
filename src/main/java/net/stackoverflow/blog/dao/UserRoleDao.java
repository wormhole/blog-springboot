package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 用户-角色中间表Mapper接口
 *
 * @author 凉衫薄
 */
@Mapper
public interface UserRoleDao {

    List<UserRole> selectByPage(Page page);

    List<UserRole> selectByCondition(Map<String, Object> searchMap);

    UserRole selectById(String id);

    int insert(UserRole userRole);

    int batchInsert(List<UserRole> userRoles);

    int deleteById(String id);

    int batchDeleteById(List<String> ids);

    int update(UserRole userRole);

    int batchUpdate(List<UserRole> userRoles);

}
