package net.stackoverflow.blog.web.controller.page.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 后台管理页面跳转Controller
 *
 * @author 凉衫薄
 */
@Controller
public class AdminIndexPageController {

    /**
     * 后台管理主页跳转 /admin/index
     * 方法 GET
     *
     * @return
     */
    @RequestMapping(value = "/admin/index", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/index");
        return mv;
    }

    /**
     * 太管管理主页跳转 /admin
     * 方法 GET
     *
     * @return
     */
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ModelAndView admin() {
        return index();
    }
}
