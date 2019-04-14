package net.stackoverflow.blog.web.controller.front;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.BaseDTO;
import net.stackoverflow.blog.common.Response;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.dto.ArticleDTO;
import net.stackoverflow.blog.pojo.dto.CommentDTO;
import net.stackoverflow.blog.pojo.po.ArticlePO;
import net.stackoverflow.blog.pojo.po.CommentPO;
import net.stackoverflow.blog.service.ArticleService;
import net.stackoverflow.blog.service.CategoryService;
import net.stackoverflow.blog.service.CommentService;
import net.stackoverflow.blog.service.UserService;
import net.stackoverflow.blog.util.CollectionUtils;
import net.stackoverflow.blog.util.ValidationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * 文章页面跳转
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
    @Autowired
    private ValidatorFactory validatorFactory;
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

    /**
     * 评论接口
     *
     * @param dto 前端传来的公共DTO
     * @return 返回Response对象
     */
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    @ResponseBody
    public Response insertComment(@RequestBody BaseDTO dto) {
        Response response = new Response();

        //获取评论dto对象
        List<CommentDTO> commentDTOs = (List<CommentDTO>) (Object) getDTO("comment", CommentDTO.class, dto);
        if (CollectionUtils.isEmpty(commentDTOs)) {
            throw new BusinessException("找不到请求数据");
        }
        CommentDTO commentDTO = commentDTOs.get(0);

        //校验字段
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CommentDTO>> violations = validator.validate(commentDTO, CommentDTO.InsertGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);

        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式错误", map);
        }

        //获取评论的文章po
        ArticlePO articlePO = articleService.selectByUrl(commentDTO.getUrl());
        if (articlePO == null) {
            throw new BusinessException("找不到该文章");
        }

        //评论插入数据库
        CommentPO commentPO = new CommentPO();
        BeanUtils.copyProperties(commentDTO, commentPO);
        commentPO.setDate(new Date());
        commentPO.setArticleId(articlePO.getId());
        commentPO.setReview(0);
        commentService.insert(commentPO);

        response.setStatus(Response.SUCCESS);
        response.setMessage("评论成功");

        return response;
    }

    /**
     * 点赞接口
     *
     * @param dto     前端传来的公共DTO对象
     * @param session 会话对象
     * @return 返回Response对象
     */
    @RequestMapping(value = "/like", method = RequestMethod.POST)
    @ResponseBody
    public Response like(@RequestBody BaseDTO dto, HttpSession session) {
        Response response = new Response();

        //从公共dto中获取articleDTO对象
        List<ArticleDTO> articleDTOs = (List<ArticleDTO>) (Object) getDTO("article", ArticleDTO.class, dto);
        if (CollectionUtils.isEmpty(articleDTOs)) {
            throw new BusinessException("找不到请求数据");
        }
        ArticleDTO articleDTO = articleDTOs.get(0);

        //校验字段
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<ArticleDTO>> violations = validator.validate(articleDTO, ArticleDTO.LikeGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);

        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式错误", map);
        }

        //检验同一次会话是否重复点赞
        Boolean isLike = (Boolean) session.getAttribute(articleDTO.getUrl());
        if (isLike != null && !isLike) {
            ArticlePO articlePO = articleService.selectByUrl(articleDTO.getUrl());
            articlePO.setLikes(articlePO.getLikes() + 1);
            articleService.update(articlePO);
            session.setAttribute(articleDTO.getUrl(), true);
            response.setStatus(Response.SUCCESS);
            response.setMessage("点赞成功");
            response.setData(articlePO.getLikes());
        } else {
            throw new BusinessException("不能重复点赞或找不到该url对应文章");
        }

        return response;
    }
}
