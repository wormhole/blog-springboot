package net.stackoverflow.blog.web.controller.page.front;

import net.stackoverflow.blog.exception.VCodeException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 登陆页面控制器
 *
 * @author 凉衫薄
 */
@Controller
public class LoginPageController {

    /**
     * Shiro登陆失败跳转方法 /login
     * 方法 POST
     *
     * @param request
     * @return
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
     * 登陆页面跳转 /login
     * 方法 GET
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "/login";
    }
}
