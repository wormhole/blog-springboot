package net.stackoverflow.blog.web.controller.admin.category;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.BaseDTO;
import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.common.Response;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.dto.CategoryDTO;
import net.stackoverflow.blog.pojo.po.CategoryPO;
import net.stackoverflow.blog.service.CategoryService;
import net.stackoverflow.blog.util.CollectionUtils;
import net.stackoverflow.blog.util.ValidationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * 分类管理接口
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping("/admin/category")
public class CategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ValidatorFactory validatorFactory;

    /**
     * 分类管理页面跳转
     *
     * @return 返回ModelAndView对象
     */
    @RequestMapping(value = "/category-manage", method = RequestMethod.GET)
    public ModelAndView category() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/category/category-manage");
        return mv;
    }

    /**
     * 新增分类
     *
     * @param dto 公共dto对象
     * @return 返回Response对象
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public Response insert(@RequestBody BaseDTO dto) {
        Response response = new Response();

        //从公共dto中提取categoryDTO对象
        List<CategoryDTO> categoryDTOs = (List<CategoryDTO>) (Object) getDTO("category", CategoryDTO.class, dto);
        if (CollectionUtils.isEmpty(categoryDTOs)) {
            throw new BusinessException("找不到请求数据");
        }
        CategoryDTO categoryDTO = categoryDTOs.get(0);

        //校验字段
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(categoryDTO, CategoryDTO.InsertGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);
        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式错误", map);
        }

        CategoryPO categoryPO = new CategoryPO();
        BeanUtils.copyProperties(categoryDTO, categoryPO);

        //检验名称和编码是否已经存在
        if (categoryService.selectByCondition(new HashMap<String, Object>() {{
            put("name", categoryPO.getName());
        }}).size() != 0) {
            throw new BusinessException("分类名已经存在");
        }
        if (categoryService.selectByCondition(new HashMap<String, Object>() {{
            put("code", categoryPO.getCode());
        }}).size() != 0) {
            throw new BusinessException("分类编码已经存在");
        }

        categoryPO.setDate(new Date());
        categoryPO.setDeleteAble(1);
        categoryService.insert(categoryPO);
        response.setStatus(Response.SUCCESS);
        response.setMessage("分类添加成功");

        return response;
    }

    /**
     * 分页查询分类列表
     *
     * @param page  分页参数
     * @param limit 每页数量
     * @return 返回Response对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Response list(@RequestParam(value = "page") String page, @RequestParam(value = "limit") String limit) {
        Response response = new Response();

        Page pageParam = new Page(Integer.valueOf(page), Integer.valueOf(limit), null);
        List<CategoryPO> categoryPOs = categoryService.selectByPage(pageParam);
        int count = categoryService.selectByCondition(new HashMap<>()).size();

        List<CategoryDTO> categoryDTOs = new ArrayList<>();
        for (CategoryPO categoryPO : categoryPOs) {
            CategoryDTO categoryDTO = new CategoryDTO();
            BeanUtils.copyProperties(categoryPO, categoryDTO);
            if (categoryPO.getDeleteAble() == 0) {
                categoryDTO.setDeleteTag("否");
            } else {
                categoryDTO.setDeleteTag("是");
            }
            categoryDTOs.add(categoryDTO);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        map.put("items", categoryDTOs);
        response.setStatus(Response.SUCCESS);
        response.setMessage("查询成功");
        response.setData(map);
        return response;
    }

    /**
     * 删除分类
     *
     * @param dto 公共dto对象
     * @return 返回Response对象
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Response delete(@RequestBody BaseDTO dto) {
        Response response = new Response();

        //从公共dto中获取categoryDTO对象
        List<CategoryDTO> categoryDTOs = (List<CategoryDTO>) (Object) getDTO("category", CategoryDTO.class, dto);
        if (CollectionUtils.isEmpty(categoryDTOs)) {
            throw new BusinessException("找不到请求数据");
        }
        CategoryDTO categoryDTO = categoryDTOs.get(0);

        //校验字段
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(categoryDTO, CategoryDTO.DeleteGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);
        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式错误", map);
        }

        //校验分类是否可以被删除
        CategoryPO categoryPO = categoryService.selectById(categoryDTO.getId());
        if (categoryPO == null) {
            throw new BusinessException("未找到该分类");
        }
        if (categoryPO.getDeleteAble() == 0) {
            throw new BusinessException("该分类不允许删除");
        }

        categoryService.deleteById(categoryPO.getId());
        response.setStatus(Response.SUCCESS);
        response.setMessage("分类删除成功");

        return response;
    }

    /**
     * 分类更新
     *
     * @param dto 公共dto对象
     * @return 返回Response对象
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Response update(@RequestBody BaseDTO dto) {
        Response response = new Response();

        //从公共dto中提取categoryDTO对象
        List<CategoryDTO> categoryDTOs = (List<CategoryDTO>) (Object) getDTO("category", CategoryDTO.class, dto);
        if (CollectionUtils.isEmpty(categoryDTOs)) {
            throw new BusinessException("找不到请求数据");
        }
        CategoryDTO categoryDTO = categoryDTOs.get(0);

        //校验字段
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(categoryDTO, CategoryDTO.UpdateGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);
        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式错误", map);
        }

        //校验分类是否可被更新
        CategoryPO oldCategoryPO = categoryService.selectById(categoryDTO.getId());
        if (oldCategoryPO == null) {
            throw new BusinessException("未找到该分类");
        }
        if (oldCategoryPO.getDeleteAble() == 0) {
            throw new BusinessException("该分类不允许修改");
        }

        //校验名称和编码是否重复
        if (!oldCategoryPO.getName().equals(categoryDTO.getName()) && categoryService.selectByCondition(new HashMap<String, Object>() {{
            put("name", categoryDTO.getName());
        }}).size() != 0) {
            throw new BusinessException("新分类名已经存在");
        }
        if (!oldCategoryPO.getCode().equals(categoryDTO.getCode()) && categoryService.selectByCondition(new HashMap<String, Object>() {{
            put("code", categoryDTO.getCode());
        }}).size() != 0) {
            throw new BusinessException("新分类编码已经存在");
        }

        CategoryPO categoryPO = new CategoryPO();
        BeanUtils.copyProperties(categoryDTO, categoryPO);
        categoryService.update(categoryPO);
        response.setStatus(Response.SUCCESS);
        response.setMessage("更新成功");

        return response;
    }
}
