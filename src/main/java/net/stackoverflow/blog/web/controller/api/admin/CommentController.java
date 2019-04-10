package net.stackoverflow.blog.web.controller.api.admin;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.BaseDTO;
import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.common.Response;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.dto.CommentDTO;
import net.stackoverflow.blog.pojo.entity.Comment;
import net.stackoverflow.blog.service.ArticleService;
import net.stackoverflow.blog.service.CommentService;
import net.stackoverflow.blog.util.CollectionUtils;
import net.stackoverflow.blog.util.TransferUtils;
import net.stackoverflow.blog.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * 评论管理接口Controller
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping("/api/admin")
public class CommentController extends BaseController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ValidatorFactory validatorFactory;

    /**
     * 获取评论列表接口 /api/admin/comment/list
     * 方法 GET
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/comment/list", method = RequestMethod.GET)
    public Response list(@RequestParam(value = "page") String page, @RequestParam(value = "limit") String limit) {
        Response response = new Response();

        Page pageParam = new Page(Integer.valueOf(page), Integer.valueOf(limit), null);
        List<Comment> comments = commentService.selectByPage(pageParam);
        int count = commentService.selectByCondition(new HashMap<>()).size();
        List<CommentDTO> dtos = new ArrayList<>();

        for (Comment comment : comments) {
            CommentDTO dto = new CommentDTO();
            dto.setId(comment.getId());
            dto.setNickname(HtmlUtils.htmlEscape(comment.getNickname()));
            dto.setEmail(HtmlUtils.htmlEscape(comment.getEmail()));
            dto.setWebsite(comment.getWebsite());
            dto.setDate(comment.getDate());
            dto.setContent(HtmlUtils.htmlEscape(comment.getContent()));
            dto.setArticleTitle(HtmlUtils.htmlEscape(articleService.selectById(comment.getArticleId()).getTitle()));
            if (comment.getReview() == 0) {
                dto.setReviewTag("否");
            } else {
                dto.setReviewTag("是");
            }
            if (comment.getReplyTo() != null) {
                dto.setReplyTo(HtmlUtils.htmlEscape(comment.getReplyTo()));
            }
            dtos.add(dto);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        map.put("items", dtos);
        response.setStatus(Response.SUCCESS);
        response.setMessage("查询成功");
        response.setData(map);
        return response;
    }

    /**
     * 删除评论接口 /api/admin/comment/delete
     * 方法 POST
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/comment/delete", method = RequestMethod.POST)
    public Response delete(@RequestBody BaseDTO dto) {
        Response response = new Response();

        List<CommentDTO> dtos = (List<CommentDTO>) (Object) getDTO("comment", CommentDTO.class, dto);
        if (CollectionUtils.isEmpty(dtos)) {
            throw new BusinessException("找不到请求数据");
        }
        CommentDTO commentDTO = dtos.get(0);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CommentDTO>> violations = validator.validate(commentDTO, CommentDTO.DeleteGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);

        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式出错", map);
        }

        if (commentService.deleteById(commentDTO.getId()) != null) {
            response.setStatus(Response.SUCCESS);
            response.setMessage("评论删除成功");
        } else {
            throw new BusinessException("评论删除失败,找不到该评论");
        }

        return response;
    }

    /**
     * 审核评论接口 /api/admin/comment/review
     * 方法 POST
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/comment/review", method = RequestMethod.POST)
    public Response review(@RequestBody BaseDTO dto) {
        Response response = new Response();

        List<CommentDTO> dtos = (List<CommentDTO>) (Object) getDTO("comment", CommentDTO.class, dto);
        if (CollectionUtils.isEmpty(dtos)) {
            throw new BusinessException("找不到请求数据");
        }
        CommentDTO commentDTO = dtos.get(0);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CommentDTO>> violations = validator.validate(commentDTO, CommentDTO.ReviewGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);

        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式出错", map);
        }

        Comment comment = (Comment) TransferUtils.dto2po(Comment.class, commentDTO);

        if (commentService.update(comment) != null) {
            response.setStatus(Response.SUCCESS);
            if (comment.getReview() == 1) {
                response.setMessage("审核成功");
            } else {
                response.setMessage("撤回成功");
            }
        } else {
            if (comment.getReview() == 1) {
                throw new BusinessException("审核失败");
            } else {
                throw new BusinessException("撤回失败");
            }
        }
        return response;
    }
}
