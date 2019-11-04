package net.stackoverflow.blog.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 后台管理系统主页Controller
 *
 * @author 凉衫薄
 */
@Controller
public class AdminPageController {

    /**
     * 后台管理系统主页跳转
     *
     * @return 返回ModelAndView对象
     */
    @RequestMapping(value = {"/admin/index", "/admin"}, method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/index");
        return mv;
    }
}
