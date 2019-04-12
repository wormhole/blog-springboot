package net.stackoverflow.blog.web.controller.front;

import net.stackoverflow.blog.exception.VCodeException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 登陆接口
 *
 * @author 凉衫薄
 */
@Controller
public class LoginPageController {

    /**
     * 如果shiro认证出错，会继续通过以下mapping处理
     *
     * @param request HttpServletRequest对象
     * @return 返回ModelAndView
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request) {

        ModelAndView mv = new ModelAndView();

        String errorClassName = (String) request.getAttribute("shiroLoginFailure");
        if (AuthenticationException.class.getName().equals(errorClassName)) {
            mv.addObject("error", "用户名不存在");
        } else if (IncorrectCredentialsException.class.getName().equals(errorClassName)) {
            mv.addObject("error", "密码错误");
        } else if (VCodeException.class.getName().equals((errorClassName))) {
            mv.addObject("error", "验证码错误");
        } else if (errorClassName != null) {
            mv.addObject("error", errorClassName);
        }
        mv.setViewName("/login");
        return mv;
    }

    /**
     * 登陆页面跳转
     *
     * @return 返回ModelAndView
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/login");
        return mv;
    }
}
