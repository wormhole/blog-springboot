package net.stackoverflow.blog.web.controller.admin.article;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.common.Result;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.entity.Comment;
import net.stackoverflow.blog.pojo.vo.CommentVO;
import net.stackoverflow.blog.service.ArticleService;
import net.stackoverflow.blog.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论管理接口
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping(value = "/admin/article")
public class CommentController extends BaseController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleService articleService;

    /**
     * 评论管理页面跳转
     *
     * @return
     */
    @RequestMapping(value = "/comment_management", method = RequestMethod.GET)
    public ModelAndView management() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/article/comment_management");
        return mv;
    }

    /**
     * 分页查询接口
     *
     * @param page  分页参数
     * @param limit 每页数量
     * @return
     */
    @RequestMapping(value = "/list_comment", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity list(@RequestParam(value = "page") String page, @RequestParam(value = "limit") String limit) {

        //分页查询
        Page pageParam = new Page(Integer.valueOf(page), Integer.valueOf(limit), null);
        List<Comment> comments = commentService.selectByPage(pageParam);
        int count = commentService.selectByCondition(new HashMap<>(16)).size();
        List<CommentVO> commentVOs = new ArrayList<>();

        for (Comment comment : comments) {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(comment, commentVO);
            commentVO.setNickname(HtmlUtils.htmlEscape(comment.getNickname()));
            commentVO.setEmail(HtmlUtils.htmlEscape(comment.getEmail()));
            commentVO.setContent(HtmlUtils.htmlEscape(comment.getContent()));
            commentVO.setArticleTitle(HtmlUtils.htmlEscape(articleService.selectById(comment.getArticleId()).getTitle()));
            if (comment.getReview() == 0) {
                commentVO.setReviewTag("否");
            } else {
                commentVO.setReviewTag("是");
            }
            if (comment.getReplyTo() != null) {
                commentVO.setReplyTo(HtmlUtils.htmlEscape(comment.getReplyTo()));
            }
            commentVOs.add(commentVO);
        }

        Map<String, Object> map = new HashMap<>(16);
        map.put("count", count);
        map.put("items", commentVOs);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setMessage("查询成功");
        result.setData(map);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 评论删除接口
     *
     * @param commentVO
     * @param errors
     * @return
     */
    @RequestMapping(value = "/delete_comment", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity delete(@Validated(CommentVO.DeleteGroup.class) @RequestBody CommentVO commentVO, Errors errors) {

        //校验数据
        checkErrors(errors);

        Result result = new Result();
        if (commentService.delete(commentVO.getId()) != null) {
            result.setStatus(Result.SUCCESS);
            result.setMessage("评论删除成功");
        } else {
            throw new BusinessException("评论删除失败,找不到该评论");
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 审核评论接口
     *
     * @param commentVO
     * @param errors
     * @return
     */
    @RequestMapping(value = "/review_comment", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity review(@Validated(CommentVO.ReviewGroup.class) @RequestBody CommentVO commentVO, Errors errors) {

        //校验数据
        checkErrors(errors);

        Comment comment = new Comment();
        BeanUtils.copyProperties(commentVO, comment);

        Result result = new Result();
        if (commentService.update(comment) != null) {
            result.setStatus(Result.SUCCESS);
            if (comment.getReview() == 1) {
                result.setMessage("审核成功");
            } else {
                result.setMessage("撤回成功");
            }
        } else {
            if (comment.getReview() == 1) {
                throw new BusinessException("审核失败");
            } else {
                throw new BusinessException("撤回失败");
            }
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
