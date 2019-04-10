package net.stackoverflow.blog.web.controller.page.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 注册页面控制器
 *
 * @author 凉衫薄
 */
@Controller
public class RegisterPageController {

    /**
     * 注册页面跳转 /register
     * 方法 GET
     *
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/register");
        return mv;
    }
}
