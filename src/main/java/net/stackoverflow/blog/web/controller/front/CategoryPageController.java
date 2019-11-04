package net.stackoverflow.blog.web.controller.front;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.Article;
import net.stackoverflow.blog.pojo.entity.Category;
import net.stackoverflow.blog.pojo.vo.ArticleVO;
import net.stackoverflow.blog.pojo.vo.CategoryVO;
import net.stackoverflow.blog.service.ArticleService;
import net.stackoverflow.blog.service.CategoryService;
import net.stackoverflow.blog.service.CommentService;
import net.stackoverflow.blog.service.UserService;
import org.jsoup.Jsoup;
import org.springframework.beans.BeanUtils;
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
 * 分类页面Controller
 *
 * @author 凉衫薄
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
     * 分类页面跳转
     *
     * @return 返回ModelAndView
     */
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public ModelAndView categoryPage() {
        ModelAndView mv = new ModelAndView();

        //查询所有分类，并放入VO
        List<Category> categorys = categoryService.selectByCondition(new HashMap<>());
        List<CategoryVO> categoryVOs = new ArrayList<>();
        for (Category category : categorys) {
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category, categoryVO);
            categoryVO.setArticleCount(articleService.selectByCondition(new HashMap<String, Object>() {{
                put("visible", 1);
                put("categoryId", category.getId());
            }}).size());
            categoryVOs.add(categoryVO);
        }

        mv.addObject("categoryList", categoryVOs);
        mv.addObject("select", "/category");
        mv.setViewName("/category");
        return mv;
    }

    /**
     * 具体显示某个分类页面跳转
     *
     * @param categoryCode 分类编码
     * @param page         分页参数
     * @param request      HttpServletRequest请求对象
     * @return 返回ModelAndView对象
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
        //如果查找到有该分类，则获取所有该分类文章，否则返回404
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

            //计算前端分页组件的起止页
            int start = (p - 2 < 1) ? 1 : p - 2;
            int end = (start + 4 > pageCount) ? pageCount : start + 4;
            if ((end - start) < 4) {
                start = (end - 4 < 1) ? 1 : end - 4;
            }

            //查询该分类的所有文章，并放入VO
            Page pageParam = new Page(p, limit, null);
            pageParam.setSearchMap(new HashMap<String, Object>(2) {{
                put("visible", 1);
                put("categoryId", category.getId());
            }});
            List<Article> articles = articleService.selectByPage(pageParam);
            List<ArticleVO> articleVOs = new ArrayList<>();
            for (Article article : articles) {
                ArticleVO articleVO = new ArticleVO();
                BeanUtils.copyProperties(article, articleVO);
                articleVO.setTitle(HtmlUtils.htmlEscape(article.getTitle()));
                articleVO.setAuthor(HtmlUtils.htmlEscape(userService.selectById(article.getUserId()).getNickname()));
                articleVO.setCategoryName(categoryService.selectById(article.getCategoryId()).getName());
                articleVO.setCommentCount(commentService.selectByCondition(new HashMap<String, Object>() {{
                    put("articleId", article.getId());
                }}).size());
                articleVO.setPreview(Jsoup.parse(article.getArticleHtml()).text());
                articleVOs.add(articleVO);
            }

            //设置Model
            mv.addObject("articleList", articleVOs);
            mv.addObject("start", start);
            mv.addObject("end", end);
            mv.addObject("page", p);
            mv.addObject("pageCount", pageCount);
            mv.addObject("path", "/category/" + categoryCode);
            mv.addObject("select", "/category");
            mv.addObject("header", category.getName());
            mv.addObject("index", false);
            mv.setViewName("/index");
        } else {
            mv.setStatus(HttpStatus.NOT_FOUND);
            mv.setViewName("/error/404");
        }
        return mv;
    }
}
