package net.stackoverflow.blog.web.controller.page.admin.comment;

import net.stackoverflow.blog.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 评论管理页面跳转Controller
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping("/admin/comment")
public class CommentManagePageController extends BaseController {

    /**
     * 评论管理页面跳转 /admin/comment/comment-manage
     * 方法 GET
     *
     * @return
     */
    @RequestMapping(value = "/comment-manage", method = RequestMethod.GET)
    public ModelAndView management() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/comment/comment-manage");
        return mv;
    }
}
