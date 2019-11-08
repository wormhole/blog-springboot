package net.stackoverflow.blog.web.controller.admin.article;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.Result;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.entity.Article;
import net.stackoverflow.blog.pojo.entity.Category;
import net.stackoverflow.blog.pojo.entity.User;
import net.stackoverflow.blog.pojo.vo.ArticleVO;
import net.stackoverflow.blog.service.ArticleService;
import net.stackoverflow.blog.service.CategoryService;
import net.stackoverflow.blog.util.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @RequestMapping(value = "/article_editor", method = RequestMethod.GET)
    public ModelAndView article(@RequestParam(value = "id", required = false) String id) {
        ModelAndView mv = new ModelAndView();

        List<Category> categorys = categoryService.selectByCondition(new HashMap<>(16));

        //判断是否是从文章管理页面跳转过来的
        if (id != null) {
            Article article = articleService.selectById(id);
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(article, articleVO);
            articleVO.setArticleCode(urlToCode(article.getUrl()));
            mv.addObject("selected", article.getCategoryId());
            mv.addObject("article", articleVO);
        } else {
            mv.addObject("selected", categoryService.selectByCondition(new HashMap<String, Object>(16) {{
                put("code", "uncategory");
            }}).get(0).getId());
        }

        mv.addObject("categoryList", categorys);
        mv.setViewName("/admin/article/article_editor");

        return mv;
    }

    /**
     * 保存文章接口
     *
     * @param articleVO
     * @param errors
     * @param session
     * @return
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity insert(@Validated(ArticleVO.InsertGroup.class) @RequestBody ArticleVO articleVO, Errors errors, HttpSession session) {

        //校验数据
        if (errors.hasErrors()) {
            Map<String, String> errMap = new HashMap<>(16);
            List<ObjectError> oes = errors.getAllErrors();
            for (ObjectError oe : oes) {
                if (oe instanceof FieldError) {
                    FieldError fe = (FieldError) oe;
                    errMap.put(fe.getField(), oe.getDefaultMessage());
                } else {
                    errMap.put(oe.getObjectName(), oe.getDefaultMessage());
                }
            }
            throw new BusinessException("字段格式错误", errMap);
        }

        //检验url是否重复
        articleVO.setUrl(codeToUrl(articleVO.getArticleCode()));
        if (articleService.selectByUrl(articleVO.getUrl()) != null) {
            throw new BusinessException("url重复");
        }

        User user = (User) session.getAttribute("user");
        Article article = new Article();
        BeanUtils.copyProperties(articleVO, article);
        article.setCreateDate(new Date());
        article.setModifyDate(new Date());
        article.setUserId(user.getId());
        article.setHits(0);
        article.setLikes(0);
        article.setVisible(1);
        articleService.insert(article);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setMessage("保存成功");

        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 图片上传
     *
     * @param multipartFile multipartFile对象
     * @return 返回Map对象
     */
    @RequestMapping(value = "/upload_image", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity image(@RequestParam("editormd-image-file") MultipartFile multipartFile) {
        Map<String, Object> result = new HashMap<>(16);

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
            return new ResponseEntity(result, HttpStatus.OK);
        }

        String url = "/upload" + dataPath + fileName;
        result.put("success", 1);
        result.put("message", "上传成功");
        result.put("url", url);
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
