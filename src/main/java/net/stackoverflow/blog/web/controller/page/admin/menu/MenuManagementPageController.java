package net.stackoverflow.blog.web.controller.page.admin.menu;

import net.stackoverflow.blog.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 菜单管理页面跳转Controller
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping("/admin/menu")
public class MenuManagementPageController extends BaseController {

    @RequestMapping(value = "/menu-manage", method = RequestMethod.GET)
    public ModelAndView management() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/menu/menu-manage");
        return mv;
    }
}
