package net.stackoverflow.blog.web.controller.api.admin;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.BaseDTO;
import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.common.Response;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.dto.ArticleDTO;
import net.stackoverflow.blog.pojo.po.ArticlePO;
import net.stackoverflow.blog.pojo.po.UserPO;
import net.stackoverflow.blog.service.ArticleService;
import net.stackoverflow.blog.service.CategoryService;
import net.stackoverflow.blog.service.CommentService;
import net.stackoverflow.blog.service.UserService;
import net.stackoverflow.blog.util.CollectionUtils;
import net.stackoverflow.blog.util.DateUtils;
import net.stackoverflow.blog.util.ValidationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 文章管理接口Controller
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping("/api/admin")
public class ArticleController extends BaseController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ValidatorFactory validatorFactory;
    @Value("${server.upload.path}")
    private String path;

    /**
     * 通过文章url获取code
     *
     * @param url
     * @return
     */
    private String urlToCode(String url) {
        String[] paths = url.split("/");
        return paths[paths.length - 1];
    }

    /**
     * 通过code转url
     *
     * @param code
     * @return
     */
    private String codeToUrl(String code) {
        return "/article" + DateUtils.getDatePath() + code;
    }

    /**
     * 保存文章 /api/admin/article/insert
     * 方法 POST
     *
     * @param dto
     * @param session
     * @return
     */
    @RequestMapping(value = "/article/insert", method = RequestMethod.POST)
    public Response save(@RequestBody BaseDTO dto, HttpSession session) {
        Response response = new Response();

        List<ArticleDTO> dtos = (List<ArticleDTO>) (Object) getDTO("article", ArticleDTO.class, dto);
        if (CollectionUtils.isEmpty(dtos)) {
            throw new BusinessException("找不到请求数据");
        }
        ArticleDTO articleDTO = dtos.get(0);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<ArticleDTO>> violations = validator.validate(articleDTO, ArticleDTO.InsertGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);

        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式错误", map);
        }

        articleDTO.setUrl(codeToUrl(articleDTO.getArticleCode()));
        if (articleService.selectByUrl(articleDTO.getUrl()) != null) {
            throw new BusinessException("url重复");
        }

        UserPO user = (UserPO) session.getAttribute("user");
        ArticlePO article = new ArticlePO();
        BeanUtils.copyProperties(articleDTO, article);
        article.setCreateDate(new Date());
        article.setModifyDate(new Date());
        article.setUserId(user.getId());
        article.setHits(0);
        article.setLikes(0);
        article.setVisible(1);
        articleService.insert(article);
        response.setStatus(Response.SUCCESS);
        response.setMessage("保存成功");

        return response;
    }

    /**
     * 更新文章 /api/admin/article/update
     * 方法 POST
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/article/update", method = RequestMethod.POST)
    public Response update(@RequestBody BaseDTO dto) {
        Response response = new Response();

        List<ArticleDTO> dtos = (List<ArticleDTO>) (Object) getDTO("article", ArticleDTO.class, dto);
        if (CollectionUtils.isEmpty(dtos)) {
            throw new BusinessException("找不到请求数据");
        }
        ArticleDTO articleDTO = dtos.get(0);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<ArticleDTO>> violations = validator.validate(articleDTO, ArticleDTO.UpdateGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);

        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式错误", map);
        }

        ArticlePO article = articleService.selectById(articleDTO.getId());

        if (article == null) {
            throw new BusinessException("未找到文章");
        }

        String[] paths = article.getUrl().split("/");
        paths[paths.length - 1] = articleDTO.getArticleCode();
        String url = String.join("/", paths);

        if (!urlToCode(article.getUrl()).equals(articleDTO.getArticleCode()) && (articleService.selectByUrl(url) != null)) {
            throw new BusinessException("url重复");
        }

        ArticlePO updateArticle = new ArticlePO();
        BeanUtils.copyProperties(articleDTO, updateArticle);
        updateArticle.setModifyDate(new Date());
        updateArticle.setUrl(url);
        articleService.update(updateArticle);
        response.setStatus(Response.SUCCESS);
        response.setMessage("文章更新成功");

        return response;
    }

    /**
     * 保存图片 /api/admin/article/image
     * 方法 POST
     *
     * @param request
     * @param multipartFile
     * @return 返回Map
     */
    @RequestMapping(value = "/article/image", method = RequestMethod.POST)
    public Map image(HttpServletRequest request, @RequestParam("editormd-image-file") MultipartFile multipartFile) {
        Map<String, Object> result = new HashMap<>();

        String fileName = multipartFile.getOriginalFilename();
        String dataPath = DateUtils.getDatePath();
        String destDir = path + dataPath;

        File destDirFile = new File(destDir);
        if (!destDirFile.exists()) {
            destDirFile.mkdirs();
        }

        File destFile = new File(destDirFile, fileName);

        try {
            multipartFile.transferTo(destFile);
        } catch (IOException e) {
            result.put("success", 0);
            result.put("message", "上传失败");
            return result;
        }

        String url = "/upload" + dataPath + fileName;
        result.put("success", 1);
        result.put("message", "上传成功");
        result.put("url", url);
        return result;
    }

    /**
     * 获取文章 /api/admin/article/list
     * 方法 GET
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/article/list", method = RequestMethod.GET)
    public Response list(@RequestParam(value = "page") String page, @RequestParam(value = "limit") String limit) {
        Response response = new Response();

        Page pageParam = new Page(Integer.valueOf(page), Integer.valueOf(limit), null);
        List<ArticlePO> articles = articleService.selectByPage(pageParam);

        int count = articleService.selectByCondition(new HashMap<>()).size();
        List<ArticleDTO> dtos = new ArrayList<>();

        for (ArticlePO article : articles) {
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setId(article.getId());
            articleDTO.setTitle(HtmlUtils.htmlEscape(article.getTitle()));
            articleDTO.setAuthor(HtmlUtils.htmlEscape(userService.selectById(article.getUserId()).getNickname()));
            articleDTO.setCategoryName(categoryService.selectById(article.getCategoryId()).getName());
            articleDTO.setCreateDate(article.getCreateDate());
            articleDTO.setModifyDate(article.getModifyDate());
            articleDTO.setHits(article.getHits());
            articleDTO.setLikes(article.getLikes());
            articleDTO.setCommentCount(commentService.selectByCondition(new HashMap<String, Object>() {{
                put("articleId", article.getId());
            }}).size());
            articleDTO.setUrl(article.getUrl());
            if (article.getVisible() == 0) {
                articleDTO.setVisibleTag("否");
            } else {
                articleDTO.setVisibleTag("是");
            }
            dtos.add(articleDTO);
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
     * 文章删除 /api/admin/article/delete
     * 方法 POST
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/article/delete", method = RequestMethod.POST)
    public Response delete(@RequestBody BaseDTO dto) {
        Response response = new Response();

        List<ArticleDTO> dtos = (List<ArticleDTO>) (Object) getDTO("article", ArticleDTO.class, dto);
        if (CollectionUtils.isEmpty(dtos)) {
            throw new BusinessException("找不到请求数据");
        }

        for (ArticleDTO articleDTO : dtos) {
            Validator validator = validatorFactory.getValidator();
            Set<ConstraintViolation<ArticleDTO>> violations = validator.validate(articleDTO, ArticleDTO.DeleteGroup.class);
            Map<String, String> map = ValidationUtils.errorMap(violations);

            if (!CollectionUtils.isEmpty(map)) {
                throw new BusinessException("字段格式错误", map);
            }
        }

        List<String> ids = new ArrayList<>();
        for (ArticleDTO articleDTO : dtos) {
            ids.add(articleDTO.getId());
        }
        articleService.batchDeleteById(ids);

        response.setStatus(Response.SUCCESS);
        response.setMessage("删除成功");
        return response;
    }

    /**
     * 设置文章是否显示 /api/admin/article/visible
     * 方法 POST
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/article/visible", method = RequestMethod.POST)
    public Response visible(@RequestBody BaseDTO dto) {
        Response response = new Response();

        List<ArticleDTO> dtos = (List<ArticleDTO>) (Object) getDTO("article", ArticleDTO.class, dto);
        if (CollectionUtils.isEmpty(dtos)) {
            throw new BusinessException("找不到请求数据");
        }
        ArticleDTO articleDTO = dtos.get(0);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<ArticleDTO>> violations = validator.validate(articleDTO, ArticleDTO.VisibleGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);

        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式错误", map);
        }

        ArticlePO article = new ArticlePO();
        BeanUtils.copyProperties(articleDTO, article);

        if (articleService.update(article) != null) {
            response.setStatus(Response.SUCCESS);
            if (article.getVisible() == 0) {
                response.setMessage("隐藏成功");
            } else {
                response.setMessage("显示成功");
            }
        } else {
            if (article.getVisible() == 0) {
                throw new BusinessException("隐藏失败");
            } else {
                throw new BusinessException("显示失败");
            }
        }
        return response;
    }

    /**
     * 导出markdown格式备份 /api/admin/article/export
     * 方法 GET
     *
     * @param id
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/article/export", method = RequestMethod.GET)
    public ResponseEntity<byte[]> export(@RequestParam("id") String id) throws IOException {
        ArticlePO article = articleService.selectById(id);
        String filename = article.getTitle() + ".md";
        filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");

        InputStream is = new ByteArrayInputStream(article.getArticleMd().getBytes("UTF-8"));
        byte[] body = new byte[is.available()];
        is.read(body);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename=" + filename);
        HttpStatus status = HttpStatus.OK;
        ResponseEntity<byte[]> entity = new ResponseEntity<>(body, headers, status);
        return entity;
    }
}
