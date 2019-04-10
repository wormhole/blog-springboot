package net.stackoverflow.blog.web.controller.page.front;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.dto.ArticleDTO;
import net.stackoverflow.blog.pojo.dto.CategoryDTO;
import net.stackoverflow.blog.pojo.entity.Article;
import net.stackoverflow.blog.pojo.entity.Category;
import net.stackoverflow.blog.service.ArticleService;
import net.stackoverflow.blog.service.CategoryService;
import net.stackoverflow.blog.service.CommentService;
import net.stackoverflow.blog.service.UserService;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
 * 前端分类页面跳转控制器
 *
 * @author 凉衫薄
 */
@Controller
public class CategoryPageController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentService commentService;

    /**
     * 跳转到某一分类所有文章显示页面 /category/{categoryCode}
     * 方法 GET
     *
     * @return 返回ModelAndView, 查找成功时返回分类页面, 否则返回404页面
     */
    @RequestMapping(value = "/category/{categoryCode}", method = RequestMethod.GET)
    public ModelAndView categoryArticle(@PathVariable("categoryCode") String categoryCode, @RequestParam(value = "page", required = false, defaultValue = "1") String page, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        ServletContext application = request.getServletContext();
        Map<String, Object> settingMap = (Map<String, Object>) application.getAttribute("setting");
        int limit = Integer.valueOf((String) settingMap.get("limit"));

        List<Category> categorys = categoryService.selectByCondition(new HashMap<String, Object>() {{
            put("code", categoryCode);
        }});
        if (categorys.size() != 0) {
            Category category = categorys.get(0);
            int count = articleService.selectByCondition(new HashMap<String, Object>() {{
                put("visible", 1);
                put("categoryId", category.getId());
            }}).size();
            int pageCount = (count % limit == 0) ? count / limit : count / limit + 1;
            pageCount = pageCount == 0 ? 1 : pageCount;

            Integer p;
            try {
                p = Integer.parseInt(page);
            } catch (Exception e) {
                mv.setStatus(HttpStatus.NOT_FOUND);
                mv.setViewName("/error/404");
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

            Page pageParam = new Page(p, limit, null);
            pageParam.setSearchMap(new HashMap<String, Object>() {{
                put("visible", 1);
                put("categoryId", category.getId());
            }});
            List<Article> articles = articleService.selectByPage(pageParam);
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
                dto.setPreview(Jsoup.parse(article.getArticleHtml()).text());
                dto.setUrl(article.getUrl());
                dto.setCreateDate(article.getCreateDate());
                articleDTOS.add(dto);
            }

            mv.addObject("articleList", articleDTOS);
            mv.addObject("start", start);
            mv.addObject("end", end);
            mv.addObject("page", p);
            mv.addObject("pageCount", pageCount);
            mv.addObject("path", "/category/" + categoryCode);
            mv.addObject("select", "/category");
            mv.addObject("header", category.getName());
            mv.setViewName("/index");
        } else {
            mv.setStatus(HttpStatus.NOT_FOUND);
            mv.setViewName("/error/404");
        }
        return mv;
    }

    /**
     * 分类页面跳转
     *
     * @return
     */
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public ModelAndView category() {
        ModelAndView mv = new ModelAndView();

        List<Category> categorys = categoryService.selectByCondition(new HashMap<>());
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        for (Category category : categorys) {
            CategoryDTO dto = new CategoryDTO();
            dto.setName(category.getName());
            dto.setCode(category.getCode());
            dto.setArticleCount(articleService.selectByCondition(new HashMap<String, Object>() {{
                put("visible", 1);
                put("categoryId", category.getId());
            }}).size());
            categoryDTOS.add(dto);
        }

        mv.addObject("categoryList", categoryDTOS);
        mv.addObject("select", "/category");
        mv.setViewName("/category");
        return mv;
    }
}
