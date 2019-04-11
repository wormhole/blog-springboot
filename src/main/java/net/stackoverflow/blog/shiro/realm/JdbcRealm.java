package net.stackoverflow.blog.shiro.realm;

import net.stackoverflow.blog.pojo.po.PermissionPO;
import net.stackoverflow.blog.pojo.po.RolePO;
import net.stackoverflow.blog.pojo.po.UserPO;
import net.stackoverflow.blog.service.UserService;
import net.stackoverflow.blog.shiro.SimpleByteSource;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义realm
 *
 * @author 凉衫薄
 */
public class JdbcRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 获取授权信息
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String email = (String) principalCollection.getPrimaryPrincipal();
        UserPO user = userService.selectByCondition(new HashMap<String, Object>() {{
            put("email", email);
        }}).get(0);
        SimpleAuthorizationInfo sa = new SimpleAuthorizationInfo();
        List<RolePO> roles = userService.getRoleByUserId(user.getId());
        List<PermissionPO> permissions = userService.getPermissionByUserId(user.getId());
        if (null != roles && roles.size() != 0) {
            Set<String> roleCodes = new HashSet<>();
            for (RolePO role : roles) {
                roleCodes.add(role.getCode());
            }
            sa.setRoles(roleCodes);
        }
        if (null != permissions && permissions.size() != 0) {
            Set<String> permissionCodes = new HashSet<>();
            for (PermissionPO permission : permissions) {
                permissionCodes.add(permission.getCode());
            }
            sa.setStringPermissions(permissionCodes);
        }
        return sa;
    }

    /**
     * 获取认证信息
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String email = (String) authenticationToken.getPrincipal();
        List<UserPO> list = userService.selectByCondition(new HashMap<String, Object>() {{
            put("email", email);
        }});
        if (list.size() == 0) {
            throw new AuthenticationException();
        }
        UserPO user = list.get(0);
        SimpleAuthenticationInfo sa = new SimpleAuthenticationInfo(user.getEmail(), user.getPassword(), new SimpleByteSource(user.getSalt()), getName());
        return sa;
    }
}