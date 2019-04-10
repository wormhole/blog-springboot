package net.stackoverflow.blog.web.controller.page.admin.article;

import net.stackoverflow.blog.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 后台管理系统分类管理页面Controller
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping("/admin/article")
public class CategoryManagePageController extends BaseController {

    /**
     * 分类管理页面跳转 /admin/article/category-manage
     * 方法 GET
     *
     * @return
     */
    @RequestMapping(value = "/category-manage", method = RequestMethod.GET)
    public ModelAndView category() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/article/category-manage");
        return mv;
    }
}
