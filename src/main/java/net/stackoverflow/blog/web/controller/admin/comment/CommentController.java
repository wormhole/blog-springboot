package net.stackoverflow.blog.web.controller.admin.comment;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.BaseDTO;
import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.common.Response;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.dto.CommentDTO;
import net.stackoverflow.blog.pojo.po.CommentPO;
import net.stackoverflow.blog.service.ArticleService;
import net.stackoverflow.blog.service.CommentService;
import net.stackoverflow.blog.util.CollectionUtils;
import net.stackoverflow.blog.util.ValidationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * 评论管理接口
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping(value = "/admin/comment")
public class CommentController extends BaseController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ValidatorFactory validatorFactory;

    /**
     * 评论管理页面跳转
     *
     * @return
     */
    @RequestMapping(value = "/comment-manage", method = RequestMethod.GET)
    public ModelAndView management() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/comment/comment-manage");
        return mv;
    }

    /**
     * 分页查询接口
     *
     * @param page  分页参数
     * @param limit 每页数量
     * @return 返回Response对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Response list(@RequestParam(value = "page") String page, @RequestParam(value = "limit") String limit) {
        Response response = new Response();

        //分页查询
        Page pageParam = new Page(Integer.valueOf(page), Integer.valueOf(limit), null);
        List<CommentPO> commentPOs = commentService.selectByPage(pageParam);
        int count = commentService.selectByCondition(new HashMap<>()).size();
        List<CommentDTO> commentDTOs = new ArrayList<>();

        //po转dto
        for (CommentPO commentPO : commentPOs) {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(commentPO, commentDTO);
            commentDTO.setNickname(HtmlUtils.htmlEscape(commentPO.getNickname()));
            commentDTO.setEmail(HtmlUtils.htmlEscape(commentPO.getEmail()));
            commentDTO.setContent(HtmlUtils.htmlEscape(commentPO.getContent()));
            commentDTO.setArticleTitle(HtmlUtils.htmlEscape(articleService.selectById(commentPO.getArticleId()).getTitle()));
            if (commentPO.getReview() == 0) {
                commentDTO.setReviewTag("否");
            } else {
                commentDTO.setReviewTag("是");
            }
            if (commentPO.getReplyTo() != null) {
                commentDTO.setReplyTo(HtmlUtils.htmlEscape(commentPO.getReplyTo()));
            }
            commentDTOs.add(commentDTO);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        map.put("items", commentDTOs);
        response.setStatus(Response.SUCCESS);
        response.setMessage("查询成功");
        response.setData(map);
        return response;
    }

    /**
     * 评论删除接口
     *
     * @param dto 公共dto对象
     * @return 返回Response对象
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Response delete(@RequestBody BaseDTO dto) {
        Response response = new Response();

        //从公共dto中提取commentDTO
        List<CommentDTO> commentDTOs = (List<CommentDTO>) (Object) getDTO("comment", CommentDTO.class, dto);
        if (CollectionUtils.isEmpty(commentDTOs)) {
            throw new BusinessException("找不到请求数据");
        }
        CommentDTO commentDTO = commentDTOs.get(0);

        //校验字段
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
     * 评论审核接口
     *
     * @param dto 公共dto对象
     * @return 返回Response对象
     */
    @RequestMapping(value = "/review", method = RequestMethod.POST)
    @ResponseBody
    public Response review(@RequestBody BaseDTO dto) {
        Response response = new Response();

        //从公共DTO中提取commentDTO
        List<CommentDTO> commentDTOs = (List<CommentDTO>) (Object) getDTO("comment", CommentDTO.class, dto);
        if (CollectionUtils.isEmpty(commentDTOs)) {
            throw new BusinessException("找不到请求数据");
        }
        CommentDTO commentDTO = commentDTOs.get(0);

        //校验字段
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CommentDTO>> violations = validator.validate(commentDTO, CommentDTO.ReviewGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);
        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式出错", map);
        }

        CommentPO commentPO = new CommentPO();
        BeanUtils.copyProperties(commentDTO, commentPO);

        if (commentService.update(commentPO) != null) {
            response.setStatus(Response.SUCCESS);
            if (commentPO.getReview() == 1) {
                response.setMessage("审核成功");
            } else {
                response.setMessage("撤回成功");
            }
        } else {
            if (commentPO.getReview() == 1) {
                throw new BusinessException("审核失败");
            } else {
                throw new BusinessException("撤回失败");
            }
        }
        return response;
    }
}
