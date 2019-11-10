package net.stackoverflow.blog.web.controller.admin.article;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.common.Result;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.entity.Comment;
import net.stackoverflow.blog.pojo.vo.CommentVO;
import net.stackoverflow.blog.service.ArticleService;
import net.stackoverflow.blog.service.CommentService;
import net.stackoverflow.blog.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
@Api(value = "评论管理接口")
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
    @ApiOperation(value = "评论管理页面跳转")
    @RequestMapping(value = "/comment_management", method = RequestMethod.GET)
    public ModelAndView management() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/article/comment_management");
        return mv;
    }

    /**
     * 分页查询接口
     *
     * @param page  当前页码
     * @param limit 每页数量
     * @return
     */
    @ApiOperation(value = "分页获取评论接口", response = Result.class)
    @RequestMapping(value = "/list_comment", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity list(@ApiParam(name = "page", value = "当前页码") @RequestParam(value = "page") String page,
                               @ApiParam(name = "limit", value = "每页数量") @RequestParam(value = "limit") String limit) {

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
                commentVO.setReviewStr("否");
            } else {
                commentVO.setReviewStr("是");
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
     * @param ids 评论主键列表
     * @return ResponseEntity对象
     */
    @ApiOperation(value = "评论删除接口", response = Result.class)
    @RequestMapping(value = "/delete_comment", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity delete(@ApiParam(name = "ids", value = "评论主键列表") @RequestBody List<String> ids) {

        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException("被删除的评论主键不能为空");
        }

        Result result = new Result();
        if (commentService.batchDelete(ids) > 0) {
            result.setStatus(Result.SUCCESS);
            result.setMessage("评论删除成功");
        } else {
            throw new BusinessException("评论删除失败");
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 评论审核接口
     *
     * @param ids    评论主键列表
     * @param review 审核状态
     * @return
     */
    @ApiOperation(value = "审核评论接口", response = Result.class)
    @RequestMapping(value = "/review_comment", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity review(@ApiParam(name = "ids", value = "评论主键列表") @RequestBody List<String> ids,
                                 @ApiParam(name = "review", value = "审核状态,0-未审核,1-已审核") @RequestParam(name = "review") Integer review) {

        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException("主键不能为空");
        }

        Result result = new Result();
        List<Comment> comments = commentService.selectByIds(ids);
        for (Comment comment : comments) {
            comment.setReview(review);
        }
        commentService.batchUpdate(comments);
        if (review == 0) {
            result.setStatus(Result.SUCCESS);
            result.setMessage("撤回成功");
        } else {
            result.setStatus(Result.SUCCESS);
            result.setMessage("审核成功");
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
