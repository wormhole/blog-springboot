package net.stackoverflow.blog.web.controller.page.admin.setting;

import net.stackoverflow.blog.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 博客设置页面跳转Controller
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping("/admin/setting")
public class SettingPageController extends BaseController {

    @RequestMapping(value = "/configure", method = RequestMethod.GET)
    public ModelAndView setting() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/setting/configure");
        return mv;
    }
}
