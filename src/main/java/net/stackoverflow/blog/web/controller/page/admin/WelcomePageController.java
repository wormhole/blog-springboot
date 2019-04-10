package net.stackoverflow.blog.web.controller.page.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 后台管理系统欢迎页跳转Controller
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping("/admin")
public class WelcomePageController {

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public ModelAndView welcome() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/welcome");
        return mv;
    }

}
