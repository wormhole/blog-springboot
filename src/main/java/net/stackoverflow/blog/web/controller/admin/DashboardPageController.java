package net.stackoverflow.blog.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 后台管理系统仪表盘页
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping("/admin")
public class DashboardPageController {

    /**
     * 仪表盘页跳转
     *
     * @return 返回ModelAndView
     */
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ModelAndView dashboard() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/dashboard");
        return mv;
    }

}
