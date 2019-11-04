package net.stackoverflow.blog.shiro.filter;

import net.stackoverflow.blog.pojo.entity.User;
import net.stackoverflow.blog.service.UserService;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.UserFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * 自定义UserFilter在记住密码登陆时,往session中添加用户
 *
 * @author 凉衫薄
 */
public class PageUserFilter extends UserFilter {

    private UserService userService;

    public PageUserFilter(UserService userService) {
        this.userService = userService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = this.getSubject(request, response);
        if (subject.getPrincipal() != null) {
            String email = (String) subject.getPrincipal();
            List<User> users = userService.selectByCondition(new HashMap<String, Object>() {{
                put("email", email);
            }});
            if (users.size() != 0) {
                ((HttpServletRequest) request).getSession().setAttribute("user", users.get(0));
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }
}
