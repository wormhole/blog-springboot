package net.stackoverflow.blog.web.controller.front;

import net.stackoverflow.blog.pojo.dto.ArticleDTO;
import net.stackoverflow.blog.pojo.dto.CommentDTO;
import net.stackoverflow.blog.pojo.po.ArticlePO;
import net.stackoverflow.blog.pojo.po.CommentPO;
import net.stackoverflow.blog.service.ArticleService;
import net.stackoverflow.blog.service.CategoryService;
import net.stackoverflow.blog.service.CommentService;
import net.stackoverflow.blog.service.UserService;
import org.springframework.beans.BeanUtils;
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
 * 文章页面跳转
 *
 * @author 凉衫薄
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
     * 文章页面跳转
     *
     * @param year        年
     * @param month       月
     * @param day         日
     * @param articleCode 文章编码
     * @param session     会话对象
     * @return 返回ModelAndView对象
     */
    @RequestMapping(value = "/article/{year}/{month}/{day}/{articleCode}", method = RequestMethod.GET)
    public ModelAndView article(@PathVariable("year") String year, @PathVariable("month") String month, @PathVariable("day") String day, @PathVariable("articleCode") String articleCode, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        String url = "/article/" + year + "/" + month + "/" + day + "/" + articleCode;

        //根据url查询文章，若未找到则返回404
        ArticlePO articlePO = articleService.selectByUrl(url);
        if (articlePO != null) {
            articlePO.setHits(articlePO.getHits() + 1);
            articleService.update(articlePO);

            //查询文章po，并赋值给dto
            ArticleDTO articleDTO = new ArticleDTO();
            BeanUtils.copyProperties(articlePO, articleDTO);
            articleDTO.setTitle(HtmlUtils.htmlEscape(articlePO.getTitle()));
            articleDTO.setAuthor(HtmlUtils.htmlEscape(userService.selectById(articlePO.getUserId()).getNickname()));
            articleDTO.setCategoryName(categoryService.selectById(articlePO.getCategoryId()).getName());
            articleDTO.setCommentCount(commentService.selectByCondition(new HashMap<String, Object>() {{
                put("articleId", articlePO.getId());
            }}).size());

            //查询评论po，并赋值给dto
            List<CommentPO> commentPOs = commentService.selectByCondition(new HashMap<String, Object>() {{
                put("articleId", articlePO.getId());
                put("review", 1);
            }});
            List<CommentDTO> commentDTOs = new ArrayList<>();
            for (CommentPO commentPO : commentPOs) {
                CommentDTO commentDTO = new CommentDTO();
                BeanUtils.copyProperties(commentPO, commentDTO);
                commentDTO.setNickname(HtmlUtils.htmlEscape(commentPO.getNickname()));
                commentDTO.setContent(HtmlUtils.htmlEscape(commentPO.getContent()));
                if (commentPO.getReplyTo() != null) {
                    commentDTO.setReplyTo(HtmlUtils.htmlEscape(commentPO.getReplyTo()));
                }
                if (commentPO.getWebsite() != null) {
                    commentDTO.setWebsite(commentPO.getWebsite());
                } else {
                    commentDTO.setWebsite("javascript:;");
                }
                commentDTOs.add(commentDTO);
            }

            //设置model
            mv.addObject("article", articleDTO);
            mv.addObject("commentList", commentDTOs);
            mv.addObject("title", ((Map<String, Object>) request.getServletContext().getAttribute("setting")).get("title") + " - " + articleDTO.getTitle());
            mv.setViewName("/article");
        } else {
            mv.setStatus(HttpStatus.NOT_FOUND);
            mv.setViewName("/error/404");
        }

        //记录该次会话是否已经点赞过，防止重复点赞
        if (session.getAttribute(url) == null) {
            session.setAttribute(url, false);
        }
        Boolean isLike = (Boolean) session.getAttribute(url);
        mv.addObject("isLike", isLike);
        return mv;
    }
}
