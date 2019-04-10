package net.stackoverflow.blog.web.controller.page.front;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.dto.ArticleDTO;
import net.stackoverflow.blog.pojo.entity.Article;
import net.stackoverflow.blog.service.ArticleService;
import net.stackoverflow.blog.service.CategoryService;
import net.stackoverflow.blog.service.CommentService;
import net.stackoverflow.blog.service.UserService;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页跳转控制器
 *
 * @author 凉衫薄
 */
@Controller
public class IndexPageController {

    @Autowired
    private UserService userService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CommentService commentService;

    /**
     * 进入主页 /index
     * 方法 GET
     *
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(@RequestParam(value = "page", required = false, defaultValue = "1") String page, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        ServletContext application = request.getServletContext();
        Map<String, Object> settingMap = (Map<String, Object>) application.getAttribute("setting");
        int limit = Integer.valueOf((String) settingMap.get("limit"));

        int count = articleService.selectByCondition(new HashMap<String, Object>() {{
            put("visible", 1);
        }}).size();
        int pageCount = (count % limit == 0) ? count / limit : count / limit + 1;
        pageCount = pageCount == 0 ? 1 : pageCount;

        Integer p;
        try {
            p = Integer.parseInt(page);
        } catch (Exception e) {
            mv.setViewName("/error/404");
            mv.setStatus(HttpStatus.NOT_FOUND);
            return mv;
        }
        if (p < 1 || p > pageCount) {
            mv.setViewName("/error/404");
            mv.setStatus(HttpStatus.NOT_FOUND);
            return mv;
        }

        int start = (p - 2 < 1) ? 1 : p - 2;
        int end = (start + 4 > pageCount) ? pageCount : start + 4;
        if ((end - start) < 4) {
            start = (end - 4 < 1) ? 1 : end - 4;
        }

        Page page1 = new Page(p, limit, null);
        page1.setSearchMap(new HashMap<String, Object>() {{
            put("visible", 1);
        }});
        List<Article> articles = articleService.selectByPage(page1);
        List<ArticleDTO> articleDTOS = new ArrayList<>();
        for (Article article : articles) {
            ArticleDTO dto = new ArticleDTO();
            dto.setTitle(HtmlUtils.htmlEscape(article.getTitle()));
            dto.setAuthor(HtmlUtils.htmlEscape(userService.selectById(article.getUserId()).getNickname()));
            dto.setCategoryName(categoryService.selectById(article.getCategoryId()).getName());
            dto.setCommentCount(commentService.selectByCondition(new HashMap<String, Object>() {{
                put("articleId", article.getId());
            }}).size());
            dto.setHits(article.getHits());
            dto.setLikes(article.getLikes());
            dto.setUrl(article.getUrl());
            dto.setCreateDate(article.getCreateDate());
            dto.setPreview(HtmlUtils.htmlEscape(Jsoup.parse(article.getArticleHtml()).text()));
            articleDTOS.add(dto);
        }

        mv.addObject("articleList", articleDTOS);
        mv.addObject("start", start);
        mv.addObject("end", end);
        mv.addObject("page", p);
        mv.addObject("pageCount", pageCount);
        mv.addObject("path", "/");
        mv.addObject("select", "/");
        mv.addObject("header", "最新文章");

        mv.setViewName("/index");
        return mv;
    }

    /**
     * 主页跳转 /
     * 方法 GET
     *
     * @param page
     * @param request
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView root(@RequestParam(value = "page", required = false, defaultValue = "1") String page, HttpServletRequest request) {
        return index(page, request);
    }
}
