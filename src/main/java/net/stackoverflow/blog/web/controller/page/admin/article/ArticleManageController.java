package net.stackoverflow.blog.web.controller.page.admin.article;

import net.stackoverflow.blog.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 后台管理系统文章管理页面Controller
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping("/admin/article")
public class ArticleManageController extends BaseController {

    /**
     * 文章管理页面跳转Controller /article-manage
     * 方法 GET
     *
     * @return
     */
    @RequestMapping(value = "/article-manage", method = RequestMethod.GET)
    public ModelAndView management() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/article/article-manage");
        return mv;
    }

}
