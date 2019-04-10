package net.stackoverflow.blog.web.controller.page.admin.backup;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 数据库备份控制器
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping("/admin/backup")
public class BackupPageController {

    /**
     * 数据库备份页面跳转 /admin/backup/backup
     * 方法 GET
     *
     * @return
     */
    @RequestMapping(value = "/backup", method = RequestMethod.GET)
    public ModelAndView backup() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/backup/backup");
        return mv;
    }

}
