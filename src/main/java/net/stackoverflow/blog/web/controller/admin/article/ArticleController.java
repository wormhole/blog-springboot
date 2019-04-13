package net.stackoverflow.blog.web.controller.admin.article;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.BaseDTO;
import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.common.Response;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.dto.ArticleDTO;
import net.stackoverflow.blog.pojo.po.ArticlePO;
import net.stackoverflow.blog.pojo.po.CategoryPO;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

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
 * 文章管理接口
 *
 * @author 凉衫薄
 */
@Controller
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
     * 文章管理页面跳转
     *
     * @return 返回ModelAndView对象
     */
    @RequestMapping(value = "/admin/article/article-manage", method = RequestMethod.GET)
    public ModelAndView management() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/article/article-manage");
        return mv;
    }

    /**
     * 文章编辑页面跳转
     *
     * @param id 文章id
     * @return 返回ModelAndView对象
     */
    @RequestMapping(value = "/admin/article/edit", method = RequestMethod.GET)
    public ModelAndView article(@RequestParam(value = "id", required = false) String id) {
        ModelAndView mv = new ModelAndView();

        List<CategoryPO> categoryPOs = categoryService.selectByCondition(new HashMap<>());

        //判断是否是从文章管理页面跳转过来的
        if (id != null) {
            ArticlePO articlePO = articleService.selectById(id);
            ArticleDTO articleDTO = new ArticleDTO();
            BeanUtils.copyProperties(articleDTO, articlePO);
            articleDTO.setArticleCode(urlToCode(articlePO.getUrl()));
            mv.addObject("selected", articlePO.getCategoryId());
            mv.addObject("article", articleDTO);
        } else {
            mv.addObject("selected", categoryService.selectByCondition(new HashMap<String, Object>() {{
                put("code", "uncategory");
            }}).get(0).getId());
        }

        mv.addObject("categoryList", categoryPOs);
        mv.setViewName("/admin/article/edit");

        return mv;
    }

    /**
     * 通过文章url获取code
     *
     * @param url 文章url
     * @return 返回文章编码
     */
    private String urlToCode(String url) {
        String[] paths = url.split("/");
        return paths[paths.length - 1];
    }

    /**
     * 通过code转url
     *
     * @param code 文章编码
     * @return 返回文章url
     */
    private String codeToUrl(String code) {
        return "/article" + DateUtils.getDatePath() + code;
    }

    /**
     * 保存文章
     *
     * @param dto     公共dto参数
     * @param session 会话对象
     * @return 返回Response对象
     */
    @RequestMapping(value = "/api/admin/article/insert", method = RequestMethod.POST)
    public Response save(@RequestBody BaseDTO dto, HttpSession session) {
        Response response = new Response();

        //从公共dto中提取articleDTO对象
        List<ArticleDTO> articleDTOs = (List<ArticleDTO>) (Object) getDTO("article", ArticleDTO.class, dto);
        if (CollectionUtils.isEmpty(articleDTOs)) {
            throw new BusinessException("找不到请求数据");
        }
        ArticleDTO articleDTO = articleDTOs.get(0);

        //校验字段
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<ArticleDTO>> violations = validator.validate(articleDTO, ArticleDTO.InsertGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);
        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式错误", map);
        }

        //检验url是否重复
        articleDTO.setUrl(codeToUrl(articleDTO.getArticleCode()));
        if (articleService.selectByUrl(articleDTO.getUrl()) != null) {
            throw new BusinessException("url重复");
        }

        UserPO userPO = (UserPO) session.getAttribute("user");
        ArticlePO article = new ArticlePO();
        BeanUtils.copyProperties(articleDTO, article);
        article.setCreateDate(new Date());
        article.setModifyDate(new Date());
        article.setUserId(userPO.getId());
        article.setHits(0);
        article.setLikes(0);
        article.setVisible(1);
        articleService.insert(article);
        response.setStatus(Response.SUCCESS);
        response.setMessage("保存成功");

        return response;
    }

    /**
     * 更新文章
     *
     * @param dto 公共dto对象
     * @return 返回Response对象
     */
    @RequestMapping(value = "/api/admin/article/update", method = RequestMethod.POST)
    public Response update(@RequestBody BaseDTO dto) {
        Response response = new Response();

        //从公共dto中提取articleDTO对象
        List<ArticleDTO> articleDTOs = (List<ArticleDTO>) (Object) getDTO("article", ArticleDTO.class, dto);
        if (CollectionUtils.isEmpty(articleDTOs)) {
            throw new BusinessException("找不到请求数据");
        }
        ArticleDTO articleDTO = articleDTOs.get(0);

        //校验字段
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<ArticleDTO>> violations = validator.validate(articleDTO, ArticleDTO.UpdateGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);
        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式错误", map);
        }

        ArticlePO articlePO = articleService.selectById(articleDTO.getId());
        if (articlePO == null) {
            throw new BusinessException("未找到文章");
        }

        //校验url是否重复
        String[] paths = articlePO.getUrl().split("/");
        paths[paths.length - 1] = articleDTO.getArticleCode();
        String url = String.join("/", paths);

        if (!urlToCode(articlePO.getUrl()).equals(articleDTO.getArticleCode()) && (articleService.selectByUrl(url) != null)) {
            throw new BusinessException("url重复");
        }

        ArticlePO updateArticlePO = new ArticlePO();
        BeanUtils.copyProperties(articleDTO, updateArticlePO);
        updateArticlePO.setModifyDate(new Date());
        updateArticlePO.setUrl(url);
        articleService.update(updateArticlePO);
        response.setStatus(Response.SUCCESS);
        response.setMessage("文章更新成功");

        return response;
    }

    /**
     * 图片上传
     *
     * @param multipartFile multipartFile对象
     * @return 返回Map对象
     */
    @RequestMapping(value = "/api/admin/article/image", method = RequestMethod.POST)
    public Map image(@RequestParam("editormd-image-file") MultipartFile multipartFile) {
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
     * 分页查询文章列表
     *
     * @param page  分页参数
     * @param limit 每页数量
     * @return 返回Response对象
     */
    @RequestMapping(value = "/api/admin/article/list", method = RequestMethod.GET)
    public Response list(@RequestParam(value = "page") String page, @RequestParam(value = "limit") String limit) {
        Response response = new Response();

        Page pageParam = new Page(Integer.valueOf(page), Integer.valueOf(limit), null);
        List<ArticlePO> articlePOs = articleService.selectByPage(pageParam);

        int count = articleService.selectByCondition(new HashMap<>()).size();
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
            articleDTO.setUrl(articlePO.getUrl());
            if (articlePO.getVisible() == 0) {
                articleDTO.setVisibleTag("否");
            } else {
                articleDTO.setVisibleTag("是");
            }
            articleDTOs.add(articleDTO);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        map.put("items", articleDTOs);
        response.setStatus(Response.SUCCESS);
        response.setMessage("查询成功");
        response.setData(map);
        return response;
    }

    /**
     * 文章删除
     *
     * @param dto 公共dto对象
     * @return
     */
    @RequestMapping(value = "/article/delete", method = RequestMethod.POST)
    public Response delete(@RequestBody BaseDTO dto) {
        Response response = new Response();

        List<ArticleDTO> articleDTOs = (List<ArticleDTO>) (Object) getDTO("article", ArticleDTO.class, dto);
        if (CollectionUtils.isEmpty(articleDTOs)) {
            throw new BusinessException("找不到请求数据");
        }

        //校验字段
        for (ArticleDTO articleDTO : articleDTOs) {
            Validator validator = validatorFactory.getValidator();
            Set<ConstraintViolation<ArticleDTO>> violations = validator.validate(articleDTO, ArticleDTO.DeleteGroup.class);
            Map<String, String> map = ValidationUtils.errorMap(violations);

            if (!CollectionUtils.isEmpty(map)) {
                throw new BusinessException("字段格式错误", map);
            }
        }

        //获取id
        List<String> ids = new ArrayList<>();
        for (ArticleDTO articleDTO : articleDTOs) {
            ids.add(articleDTO.getId());
        }
        articleService.batchDeleteById(ids);

        response.setStatus(Response.SUCCESS);
        response.setMessage("删除成功");
        return response;
    }

    /**
     * 设置文章是否显示
     *
     * @param dto 公共dto对象
     * @return 返回Response对象
     */
    @RequestMapping(value = "/api/admin/article/visible", method = RequestMethod.POST)
    public Response visible(@RequestBody BaseDTO dto) {
        Response response = new Response();

        //从公共dto中提取articleDTO对象
        List<ArticleDTO> articleDTOs = (List<ArticleDTO>) (Object) getDTO("article", ArticleDTO.class, dto);
        if (CollectionUtils.isEmpty(articleDTOs)) {
            throw new BusinessException("找不到请求数据");
        }
        ArticleDTO articleDTO = articleDTOs.get(0);

        //校验字段
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<ArticleDTO>> violations = validator.validate(articleDTO, ArticleDTO.VisibleGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);
        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式错误", map);
        }

        ArticlePO articlePO = new ArticlePO();
        BeanUtils.copyProperties(articleDTO, articlePO);

        if (articleService.update(articlePO) != null) {
            response.setStatus(Response.SUCCESS);
            if (articlePO.getVisible() == 0) {
                response.setMessage("隐藏成功");
            } else {
                response.setMessage("显示成功");
            }
        } else {
            if (articlePO.getVisible() == 0) {
                throw new BusinessException("隐藏失败");
            } else {
                throw new BusinessException("显示失败");
            }
        }
        return response;
    }

    /**
     * 导出markdown格式备份
     *
     * @param id 文章主键
     * @return 返回ResponseEntity对象
     * @throws IOException
     */
    @RequestMapping(value = "/api/admin/article/export", method = RequestMethod.GET)
    public ResponseEntity<byte[]> export(@RequestParam("id") String id) throws IOException {
        ArticlePO articlePO = articleService.selectById(id);
        String filename = articlePO.getTitle() + ".md";
        filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");

        InputStream is = new ByteArrayInputStream(articlePO.getArticleMd().getBytes("UTF-8"));
        byte[] body = new byte[is.available()];
        is.read(body);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename=" + filename);
        HttpStatus status = HttpStatus.OK;
        ResponseEntity<byte[]> entity = new ResponseEntity<>(body, headers, status);
        return entity;
    }
}
