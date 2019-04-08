package net.stackoverflow.blog.shiro.filter;

import com.alibaba.fastjson.JSON;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义UserFilter，前后端分离架构使用
 *
 * @author 凉衫薄
 */
public class AjaxUserFilter extends UserFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        PrintWriter out = httpServletResponse.getWriter();
        Map<String, Object> map = new HashMap<>();
        map.put("status", false);
        map.put("message", "需要登陆后才能访问");
        out.write(JSON.toJSONString(map));
        out.flush();
        out.close();
        return false;
    }
}
