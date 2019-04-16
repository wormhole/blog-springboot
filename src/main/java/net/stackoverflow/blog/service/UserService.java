package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.po.PermissionPO;
import net.stackoverflow.blog.pojo.po.RolePO;
import net.stackoverflow.blog.pojo.po.UserPO;
import net.stackoverflow.blog.pojo.po.UserRolePO;

import java.util.List;
import java.util.Map;

/**
 * 用户服务接口
 *
 * @author 凉衫薄
 */
public interface UserService {

    List<UserPO> selectByPage(Page page);

    List<UserPO> selectByCondition(Map<String, Object> searchMap);

    UserPO selectById(String id);

    UserPO insert(UserPO userPO);

    int batchInsert(List<UserPO> userPOs);

    UserPO deleteById(String id);

    int batchDeleteById(List<String> ids);

    UserPO update(UserPO userPO);

    int batchUpdate(List<UserPO> userPOs);

    UserRolePO grantRole(String userId, String roleCode);

    UserRolePO revokeRole(String userId, String roleCode);

    List<RolePO> getRoleByUserId(String userId);

    List<PermissionPO> getPermissionByUserId(String userId);
}
