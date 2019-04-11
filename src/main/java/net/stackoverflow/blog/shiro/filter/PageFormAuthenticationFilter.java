package net.stackoverflow.blog.shiro.filter;

import net.stackoverflow.blog.exception.VCodeException;
import net.stackoverflow.blog.pojo.po.UserPO;
import net.stackoverflow.blog.service.UserService;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * 自定义表单验证过滤器，用于验证码校验
 *
 * @author 凉衫薄
 */
public class PageFormAuthenticationFilter extends FormAuthenticationFilter {

    private UserService userService;

    public PageFormAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 验证验证码
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @throws Exception
     */
    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpSession session = httpServletRequest.getSession();
        String vcode = (String) session.getAttribute("vcode");
        String vcode1 = httpServletRequest.getParameter("vcode");
        if (vcode1 == null || vcode1.equalsIgnoreCase(vcode)) {
            return super.onPreHandle(request, response, mappedValue);
        } else {
            request.setAttribute(getFailureKeyAttribute(), VCodeException.class.getName());
            return true;
        }
    }

    /**
     * 认证成功后将用户对象添加到会话对象中
     *
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        String email = (String) subject.getPrincipal();
        UserPO user = userService.selectByCondition(new HashMap<String, Object>() {{
            put("email", email);
        }}).get(0);
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("user", user);
        return super.onLoginSuccess(token, subject, request, response);
    }
}