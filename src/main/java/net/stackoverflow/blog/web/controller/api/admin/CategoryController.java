package net.stackoverflow.blog.web.controller.api.admin;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.BaseDTO;
import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.common.Response;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.dto.CategoryDTO;
import net.stackoverflow.blog.pojo.entity.Category;
import net.stackoverflow.blog.service.CategoryService;
import net.stackoverflow.blog.util.CollectionUtils;
import net.stackoverflow.blog.util.ValidationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * 分类管理接口Controller
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping("/api/admin")
public class CategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ValidatorFactory validatorFactory;

    /**
     * 新增分类 /api/admin/category/insert
     * 方法 POST
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/category/insert", method = RequestMethod.POST)
    public Response insert(@RequestBody BaseDTO dto) {
        Response response = new Response();

        List<CategoryDTO> dtos = (List<CategoryDTO>) (Object) getDTO("category", CategoryDTO.class, dto);
        if (CollectionUtils.isEmpty(dtos)) {
            throw new BusinessException("找不到请求数据");
        }
        CategoryDTO categoryDTO = dtos.get(0);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(categoryDTO, CategoryDTO.InsertGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);

        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式错误", map);
        }

        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        if (categoryService.selectByCondition(new HashMap<String, Object>() {{
            put("name", category.getName());
        }}).size() != 0) {
            throw new BusinessException("分类名已经存在");
        }
        if (categoryService.selectByCondition(new HashMap<String, Object>() {{
            put("code", category.getCode());
        }}).size() != 0) {
            throw new BusinessException("分类编码已经存在");
        }

        category.setDate(new Date());
        category.setDeleteAble(1);
        categoryService.insert(category);
        response.setStatus(Response.SUCCESS);
        response.setMessage("分类添加成功");

        return response;
    }

    /**
     * 获取分类 /api/admin/category/list
     * 方法 GET
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/category/list", method = RequestMethod.GET)
    public Response list(@RequestParam(value = "page") String page, @RequestParam(value = "limit") String limit) {
        Response response = new Response();

        Page pageParam = new Page(Integer.valueOf(page), Integer.valueOf(limit), null);
        List<Category> categorys = categoryService.selectByPage(pageParam);
        int count = categoryService.selectByCondition(new HashMap<>()).size();

        List<CategoryDTO> dtos = new ArrayList<>();
        for (Category category : categorys) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(category.getId());
            categoryDTO.setName(category.getName());
            categoryDTO.setCode(category.getCode());
            if (category.getDeleteAble() == 0) {
                categoryDTO.setDeleteTag("否");
            } else {
                categoryDTO.setDeleteTag("是");
            }
            dtos.add(categoryDTO);
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
     * 删除分类 /api/admin/category/delete
     * 方法 POST
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/category/delete", method = RequestMethod.POST)
    public Response delete(@RequestBody BaseDTO dto) {
        Response response = new Response();

        List<CategoryDTO> dtos = (List<CategoryDTO>) (Object) getDTO("category", CategoryDTO.class, dto);
        if (CollectionUtils.isEmpty(dtos)) {
            throw new BusinessException("找不到请求数据");
        }
        CategoryDTO categoryDTO = dtos.get(0);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(categoryDTO, CategoryDTO.DeleteGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);

        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式错误", map);
        }

        Category category = categoryService.selectById(categoryDTO.getId());

        if (category == null) {
            throw new BusinessException("未找到该分类");
        }
        if (category.getDeleteAble() == 0) {
            throw new BusinessException("该分类不允许删除");
        }

        categoryService.deleteById(category.getId());
        response.setStatus(Response.SUCCESS);
        response.setMessage("分类删除成功");

        return response;
    }

    /**
     * 更新分类 /admin/article/category/update
     * 方法 POST
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/category/update", method = RequestMethod.POST)
    public Response update(@RequestBody BaseDTO dto) {
        Response response = new Response();

        List<CategoryDTO> dtos = (List<CategoryDTO>) (Object) getDTO("category", CategoryDTO.class, dto);
        if (CollectionUtils.isEmpty(dtos)) {
            throw new BusinessException("找不到请求数据");
        }
        CategoryDTO categoryDTO = dtos.get(0);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(categoryDTO, CategoryDTO.UpdateGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);

        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式错误", map);
        }

        Category oldCategory = categoryService.selectById(categoryDTO.getId());

        if (oldCategory == null) {
            throw new BusinessException("未找到该分类");
        }
        if (oldCategory.getDeleteAble() == 0) {
            throw new BusinessException("该分类不允许修改");
        }

        if (!oldCategory.getName().equals(categoryDTO.getName()) && categoryService.selectByCondition(new HashMap<String, Object>() {{
            put("name", categoryDTO.getName());
        }}).size() != 0) {
            throw new BusinessException("新分类名已经存在");
        }
        if (!oldCategory.getCode().equals(categoryDTO.getCode()) && categoryService.selectByCondition(new HashMap<String, Object>() {{
            put("code", categoryDTO.getCode());
        }}).size() != 0) {
            throw new BusinessException("新分类编码已经存在");
        }

        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryService.update(category);
        response.setStatus(Response.SUCCESS);
        response.setMessage("更新成功");

        return response;
    }
}
