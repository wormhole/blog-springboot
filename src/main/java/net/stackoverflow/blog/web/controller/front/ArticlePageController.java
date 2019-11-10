package net.stackoverflow.blog.web.controller.front;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.Result;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.entity.Article;
import net.stackoverflow.blog.pojo.entity.Comment;
import net.stackoverflow.blog.pojo.vo.ArticleVO;
import net.stackoverflow.blog.pojo.vo.CommentVO;
import net.stackoverflow.blog.service.ArticleService;
import net.stackoverflow.blog.service.CategoryService;
import net.stackoverflow.blog.service.CommentService;
import net.stackoverflow.blog.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 文章详情页Controller
 *
 * @author 凉衫薄
 */
@Controller
public class ArticlePageController extends BaseController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CommentService commentService;

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
        Article article = articleService.selectByUrl(url);
        if (article != null) {
            article.setHits(article.getHits() + 1);
            articleService.update(article);

            //查询文章，并赋值给VO
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(article, articleVO);
            articleVO.setTitle(HtmlUtils.htmlEscape(articleVO.getTitle()));
            articleVO.setAuthor(HtmlUtils.htmlEscape(userService.selectById(article.getUserId()).getNickname()));
            articleVO.setCategoryName(categoryService.selectById(article.getCategoryId()).getName());
            articleVO.setCommentCount(commentService.selectByCondition(new HashMap<String, Object>() {{
                put("articleId", article.getId());
            }}).size());

            //查询评论，并赋值给VO
            List<Comment> comments = commentService.selectByCondition(new HashMap<String, Object>() {{
                put("articleId", article.getId());
                put("review", 1);
            }});
            List<CommentVO> commentVOs = new ArrayList<>();
            for (Comment comment : comments) {
                CommentVO commentVO = new CommentVO();
                BeanUtils.copyProperties(comment, commentVO);
                commentVO.setNickname(HtmlUtils.htmlEscape(comment.getNickname()));
                commentVO.setContent(HtmlUtils.htmlEscape(comment.getContent()));
                if (comment.getReplyTo() != null) {
                    commentVO.setReplyTo(HtmlUtils.htmlEscape(comment.getReplyTo()));
                }
                if (comment.getWebsite() != null) {
                    commentVO.setWebsite(comment.getWebsite());
                } else {
                    commentVO.setWebsite("javascript:;");
                }
                commentVOs.add(commentVO);
            }

            //设置model
            mv.addObject("article", articleVO);
            mv.addObject("commentList", commentVOs);
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

    /**
     * 评论接口
     *
     * @param commentVO
     * @return
     */
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity insertComment(@Validated(CommentVO.InsertGroup.class) @RequestBody CommentVO commentVO) {

        //获取评论的文章
        Article article = articleService.selectByUrl(commentVO.getUrl());
        if (article == null) {
            throw new BusinessException("找不到该文章");
        }

        //评论插入数据库
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentVO, comment);
        comment.setDate(new Date());
        comment.setArticleId(article.getId());
        comment.setReview(0);
        commentService.insert(comment);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setMessage("评论成功");

        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 点赞接口
     *
     * @param articleVO
     * @param session
     * @return
     */
    @RequestMapping(value = "/like", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity like(@Validated(ArticleVO.LikeGroup.class) @RequestBody ArticleVO articleVO, HttpSession session) {

        Result result = new Result();

        //检验同一次会话是否重复点赞
        Boolean isLike = (Boolean) session.getAttribute(articleVO.getUrl());
        if (isLike != null && !isLike) {
            Article article = articleService.selectByUrl(articleVO.getUrl());
            article.setLikes(article.getLikes() + 1);
            articleService.update(article);
            session.setAttribute(articleVO.getUrl(), true);
            result.setStatus(Result.SUCCESS);
            result.setMessage("点赞成功");
            result.setData(article.getLikes());
            return new ResponseEntity(result, HttpStatus.OK);
        } else {
            throw new BusinessException("不能重复点赞或找不到该url对应文章");
        }

    }
}
