package net.stackoverflow.blog.web.controller.admin.article;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.common.Result;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.entity.Category;
import net.stackoverflow.blog.pojo.vo.CategoryVO;
import net.stackoverflow.blog.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * 分类管理接口
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping("/admin/article")
public class CategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分类管理页面跳转
     *
     * @return 返回ModelAndView对象
     */
    @RequestMapping(value = "/category_management", method = RequestMethod.GET)
    public ModelAndView category() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/article/category_management");
        return mv;
    }

    /**
     * 新增分类接口
     *
     * @param categoryVO
     * @param errors
     * @return
     */
    @RequestMapping(value = "/insert_category", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity insert(@Validated(CategoryVO.InsertGroup.class) @RequestBody CategoryVO categoryVO, Errors errors) {

        //校验数据
        checkErrors(errors);

        Category category = new Category();
        BeanUtils.copyProperties(categoryVO, category);

        //检验名称和编码是否已经存在
        if (categoryService.selectByCondition(new HashMap<String, Object>(16) {{
            put("name", category.getName());
        }}).size() != 0) {
            throw new BusinessException("分类名已经存在");
        }
        if (categoryService.selectByCondition(new HashMap<String, Object>(16) {{
            put("code", category.getCode());
        }}).size() != 0) {
            throw new BusinessException("分类编码已经存在");
        }

        category.setDate(new Date());
        category.setDeleteAble(1);
        categoryService.insert(category);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setMessage("分类添加成功");

        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 分页查询分类列表
     *
     * @param page  分页参数
     * @param limit 每页数量
     * @return
     */
    @RequestMapping(value = "/list_category", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity list(@RequestParam(value = "page") String page, @RequestParam(value = "limit") String limit) {

        Page pageParam = new Page(Integer.valueOf(page), Integer.valueOf(limit), null);
        List<Category> categorys = categoryService.selectByPage(pageParam);
        int count = categoryService.selectByCondition(new HashMap<>(16)).size();

        List<CategoryVO> categoryVOs = new ArrayList<>();
        for (Category category : categorys) {
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category, categoryVO);
            if (category.getDeleteAble() == 0) {
                categoryVO.setDeleteTag("否");
            } else {
                categoryVO.setDeleteTag("是");
            }
            categoryVOs.add(categoryVO);
        }

        Map<String, Object> map = new HashMap<>(16);
        map.put("count", count);
        map.put("items", categoryVOs);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setMessage("查询成功");
        result.setData(map);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 删除分类接口
     *
     * @param categoryVO
     * @param errors
     * @return
     */
    @RequestMapping(value = "/delete_category", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity delete(@Validated(CategoryVO.DeleteGroup.class) @RequestBody CategoryVO categoryVO, Errors errors) {

        //校验数据
        checkErrors(errors);

        //校验分类是否可以被删除
        Category category = categoryService.selectById(categoryVO.getId());
        if (category == null) {
            throw new BusinessException("未找到该分类");
        }
        if (category.getDeleteAble() == 0) {
            throw new BusinessException("该分类不允许删除");
        }

        categoryService.delete(category.getId());

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setMessage("分类删除成功");

        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 更新分类接口
     *
     * @param categoryVO
     * @param errors
     * @return
     */
    @RequestMapping(value = "/update_category", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity update(@Validated(CategoryVO.UpdateGroup.class) @RequestBody CategoryVO categoryVO, Errors errors) {

        //校验数据
        checkErrors(errors);

        //校验分类是否可被更新
        Category oldCategory = categoryService.selectById(categoryVO.getId());
        if (oldCategory == null) {
            throw new BusinessException("未找到该分类");
        }
        if (oldCategory.getDeleteAble() == 0) {
            throw new BusinessException("该分类不允许修改");
        }

        //校验名称和编码是否重复
        if (!oldCategory.getName().equals(categoryVO.getName()) && categoryService.selectByCondition(new HashMap<String, Object>(16) {{
            put("name", categoryVO.getName());
        }}).size() != 0) {
            throw new BusinessException("新分类名已经存在");
        }
        if (!oldCategory.getCode().equals(categoryVO.getCode()) && categoryService.selectByCondition(new HashMap<String, Object>(16) {{
            put("code", categoryVO.getCode());
        }}).size() != 0) {
            throw new BusinessException("新分类编码已经存在");
        }

        Category category = new Category();
        BeanUtils.copyProperties(categoryVO, category);
        categoryService.update(category);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setMessage("更新成功");

        return new ResponseEntity(result, HttpStatus.OK);
    }
}
