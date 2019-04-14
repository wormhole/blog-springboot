package net.stackoverflow.blog.web.controller.admin.article;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.BaseDTO;
import net.stackoverflow.blog.common.Response;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.dto.ArticleDTO;
import net.stackoverflow.blog.pojo.po.ArticlePO;
import net.stackoverflow.blog.pojo.po.CategoryPO;
import net.stackoverflow.blog.pojo.po.UserPO;
import net.stackoverflow.blog.service.ArticleService;
import net.stackoverflow.blog.service.CategoryService;
import net.stackoverflow.blog.util.CollectionUtils;
import net.stackoverflow.blog.util.DateUtils;
import net.stackoverflow.blog.util.ValidationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 文章编辑接口
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping(value = "/admin/article")
public class EditorController extends BaseController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ValidatorFactory validatorFactory;
    @Value("${server.upload.path}")
    private String path;

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
     * 文章编辑页面跳转
     *
     * @param id 文章id
     * @return 返回ModelAndView对象
     */
    @RequestMapping(value = "/article-edit", method = RequestMethod.GET)
    public ModelAndView article(@RequestParam(value = "id", required = false) String id) {
        ModelAndView mv = new ModelAndView();

        List<CategoryPO> categoryPOs = categoryService.selectByCondition(new HashMap<>());

        //判断是否是从文章管理页面跳转过来的
        if (id != null) {
            ArticlePO articlePO = articleService.selectById(id);
            ArticleDTO articleDTO = new ArticleDTO();
            BeanUtils.copyProperties(articlePO, articleDTO);
            articleDTO.setArticleCode(urlToCode(articlePO.getUrl()));
            mv.addObject("selected", articlePO.getCategoryId());
            mv.addObject("article", articleDTO);
        } else {
            mv.addObject("selected", categoryService.selectByCondition(new HashMap<String, Object>() {{
                put("code", "uncategory");
            }}).get(0).getId());
        }

        mv.addObject("categoryList", categoryPOs);
        mv.setViewName("/admin/article/article-edit");

        return mv;
    }

    /**
     * 保存文章
     *
     * @param dto     公共dto参数
     * @param session 会话对象
     * @return 返回Response对象
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
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
     * 图片上传
     *
     * @param multipartFile multipartFile对象
     * @return 返回Map对象
     */
    @RequestMapping(value = "/image", method = RequestMethod.POST)
    @ResponseBody
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
}
