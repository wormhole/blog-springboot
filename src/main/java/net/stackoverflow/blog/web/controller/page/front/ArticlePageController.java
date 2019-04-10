package net.stackoverflow.blog.web.controller.page.front;

import net.stackoverflow.blog.pojo.dto.ArticleDTO;
import net.stackoverflow.blog.pojo.dto.CommentDTO;
import net.stackoverflow.blog.pojo.entity.Article;
import net.stackoverflow.blog.pojo.entity.Comment;
import net.stackoverflow.blog.service.ArticleService;
import net.stackoverflow.blog.service.CategoryService;
import net.stackoverflow.blog.service.CommentService;
import net.stackoverflow.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前端文章页面跳转控制器
 *
 * @author 凉衫薄
 */
@Controller
public class ArticlePageController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 跳转到文章单页 /article/{year}/{month}/{day}/{articleCode}
     * 方法 GET
     *
     * @param year
     * @param month
     * @param day
     * @param articleCode
     * @return 返回ModelAndView, 查找成功时, 视图设置为文章视图, 否则设为404视图
     */
    @RequestMapping(value = "/article/{year}/{month}/{day}/{articleCode}", method = RequestMethod.GET)
    public ModelAndView article(@PathVariable("year") String year, @PathVariable("month") String month, @PathVariable("day") String day, @PathVariable("articleCode") String articleCode, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        String url = "/article/" + year + "/" + month + "/" + day + "/" + articleCode;

        Article article = articleService.selectByUrl(url);
        if (article != null) {
            article.setHits(article.getHits() + 1);
            articleService.update(article);

            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setTitle(HtmlUtils.htmlEscape(article.getTitle()));
            articleDTO.setAuthor(HtmlUtils.htmlEscape(userService.selectById(article.getUserId()).getNickname()));
            articleDTO.setCategoryName(categoryService.selectById(article.getCategoryId()).getName());
            articleDTO.setCommentCount(commentService.selectByCondition(new HashMap<String, Object>() {{
                put("articleId", article.getId());
            }}).size());
            articleDTO.setHits(article.getHits());
            articleDTO.setLikes(article.getLikes());
            articleDTO.setCreateDate(article.getCreateDate());
            articleDTO.setArticleMd(article.getArticleMd());

            List<Comment> comments = commentService.selectByCondition(new HashMap<String, Object>() {{
                put("articleId", article.getId());
                put("review", 1);
            }});
            List<CommentDTO> dtos = new ArrayList<>();
            for (Comment comment : comments) {
                CommentDTO commentDTO = new CommentDTO();
                commentDTO.setNickname(HtmlUtils.htmlEscape(comment.getNickname()));
                commentDTO.setDate(comment.getDate());
                commentDTO.setContent(HtmlUtils.htmlEscape(comment.getContent()));
                if (comment.getReplyTo() != null) {
                    commentDTO.setReplyTo(HtmlUtils.htmlEscape(comment.getReplyTo()));
                }
                if (comment.getWebsite() != null) {
                    commentDTO.setWebsite(comment.getWebsite());
                } else {
                    commentDTO.setWebsite("javascript:;");
                }
                dtos.add(commentDTO);
            }

            mv.addObject("article", articleDTO);
            mv.addObject("commentList", dtos);
            mv.addObject("title", ((Map<String, Object>) request.getServletContext().getAttribute("setting")).get("title") + " - " + articleDTO.getTitle());
            mv.setViewName("/article");
        } else {
            mv.setStatus(HttpStatus.NOT_FOUND);
            mv.setViewName("/error/404");
        }

        if (session.getAttribute(url) == null) {
            session.setAttribute(url, false);
        }
        Boolean isLike = (Boolean) session.getAttribute(url);
        mv.addObject("isLike", isLike);
        return mv;
    }
}
