package net.stackoverflow.blog.web.controller.front;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.dto.ArticleDTO;
import net.stackoverflow.blog.pojo.dto.CategoryDTO;
import net.stackoverflow.blog.pojo.po.ArticlePO;
import net.stackoverflow.blog.pojo.po.CategoryPO;
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
 * 分类页面跳转
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
    public ModelAndView category() {
        ModelAndView mv = new ModelAndView();

        //查询所有分类，并放入dto
        List<CategoryPO> categoryPOs = categoryService.selectByCondition(new HashMap<>());
        List<CategoryDTO> categoryDTOs = new ArrayList<>();
        for (CategoryPO categoryPO : categoryPOs) {
            CategoryDTO categoryDTO = new CategoryDTO();
            BeanUtils.copyProperties(categoryPO, categoryDTO);
            categoryDTO.setArticleCount(articleService.selectByCondition(new HashMap<String, Object>() {{
                put("visible", 1);
                put("categoryId", categoryPO.getId());
            }}).size());
            categoryDTOs.add(categoryDTO);
        }

        mv.addObject("categoryList", categoryDTOs);
        mv.addObject("select", "/category");
        mv.setViewName("/category");
        return mv;
    }

    /**
     * 具体显示某个分类页面跳转
     *
     * @param categoryCode 分类编码
     * @param page 分页参数
     * @param request HttpServletRequest请求对象
     * @return 返回ModelAndView对象
     */
    @RequestMapping(value = "/category/{categoryCode}", method = RequestMethod.GET)
    public ModelAndView categoryArticle(@PathVariable("categoryCode") String categoryCode, @RequestParam(value = "page", required = false, defaultValue = "1") String page, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        ServletContext application = request.getServletContext();
        Map<String, Object> settingMap = (Map<String, Object>) application.getAttribute("setting");
        int limit = Integer.valueOf((String) settingMap.get("limit"));

        List<CategoryPO> categoryPOs = categoryService.selectByCondition(new HashMap<String, Object>() {{
            put("code", categoryCode);
        }});
        //如果查找到有该分类，则获取所有该分类文章，否则返回404
        if (categoryPOs.size() != 0) {
            CategoryPO categoryPO = categoryPOs.get(0);
            int count = articleService.selectByCondition(new HashMap<String, Object>() {{
                put("visible", 1);
                put("categoryId", categoryPO.getId());
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

            //查询该分类的所有文章，并放入dto
            Page pageParam = new Page(p, limit, null);
            pageParam.setSearchMap(new HashMap<String, Object>() {{
                put("visible", 1);
                put("categoryId", categoryPO.getId());
            }});
            List<ArticlePO> articlePOs = articleService.selectByPage(pageParam);
            List<ArticleDTO> articleDTOs = new ArrayList<>();
            for (ArticlePO articlePO : articlePOs) {
                ArticleDTO articleDTO = new ArticleDTO();
                BeanUtils.copyProperties(articlePO, articleDTO);
                articleDTO.setTitle(HtmlUtils.htmlEscape(articlePO.getTitle()));
                articleDTO.setAuthor(HtmlUtils.htmlEscape(userService.selectById(articlePO.getUserId()).getNickname()));
                articleDTO.setCategoryName(categoryService.selectById(articlePO.getCategoryId()).getName());
                articleDTO.setCommentCount(commentService.selectByCondition(new HashMap<String, Object>() {{
                    put("articleId", articlePO.getId());
                }}).size());
                articleDTO.setPreview(Jsoup.parse(articlePO.getArticleHtml()).text());
                articleDTOs.add(articleDTO);
            }

            //设置Model
            mv.addObject("articleList", articleDTOs);
            mv.addObject("start", start);
            mv.addObject("end", end);
            mv.addObject("page", p);
            mv.addObject("pageCount", pageCount);
            mv.addObject("path", "/category/" + categoryCode);
            mv.addObject("select", "/category");
            mv.addObject("header", categoryPO.getName());
            mv.setViewName("/index");
        } else {
            mv.setStatus(HttpStatus.NOT_FOUND);
            mv.setViewName("/error/404");
        }
        return mv;
    }
}
