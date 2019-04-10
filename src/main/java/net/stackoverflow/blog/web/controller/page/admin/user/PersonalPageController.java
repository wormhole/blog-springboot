package net.stackoverflow.blog.web.controller.page.admin.user;

import net.stackoverflow.blog.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 个人信息维护页面控制器
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping("/admin/user")
public class PersonalPageController extends BaseController {

    @RequestMapping(value = "/personal", method = RequestMethod.GET)
    public ModelAndView personal() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/user/personal");
        return mv;
    }
}
