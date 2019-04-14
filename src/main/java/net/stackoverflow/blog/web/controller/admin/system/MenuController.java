package net.stackoverflow.blog.web.controller.admin.system;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.BaseDTO;
import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.common.Response;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.dto.MenuDTO;
import net.stackoverflow.blog.pojo.po.MenuPO;
import net.stackoverflow.blog.service.MenuService;
import net.stackoverflow.blog.util.CollectionUtils;
import net.stackoverflow.blog.util.ValidationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * 菜单管理接口
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping(value = "/admin/system")
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private ValidatorFactory validatorFactory;

    /**
     * 菜单管理页面跳转
     *
     * @return 返回ModelAndView对象
     */
    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public ModelAndView management() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/system/menu");
        return mv;
    }

    /**
     * 查询菜单列表
     *
     * @param page  分页参数
     * @param limit 每页数量
     * @return 返回Response对象
     */
    @RequestMapping(value = "/menu/list", method = RequestMethod.GET)
    @ResponseBody
    public Response list(@RequestParam(value = "page") String page, @RequestParam(value = "limit") String limit) {
        Response response = new Response();

        //分页查询
        Page pageParam = new Page(Integer.valueOf(page), Integer.valueOf(limit), null);
        List<MenuPO> menuPOs = menuService.selectByPage(pageParam);
        int count = menuService.selectByCondition(new HashMap<>()).size();

        List<MenuDTO> menuDTOs = new ArrayList<>();
        for (MenuPO menuPO : menuPOs) {
            MenuDTO menuDTO = new MenuDTO();
            BeanUtils.copyProperties(menuPO, menuDTO);
            menuDTO.setName(HtmlUtils.htmlEscape(menuPO.getName()));
            if (menuPO.getDeleteAble() == 0) {
                menuDTO.setDeleteTag("否");
            } else {
                menuDTO.setDeleteTag("是");
            }
            menuDTOs.add(menuDTO);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        map.put("items", menuDTOs);
        response.setStatus(Response.SUCCESS);
        response.setMessage("菜单查询成功");
        response.setData(map);
        return response;
    }

    /**
     * 删除菜单
     *
     * @param dto     公共DTO
     * @param request HttpServletRequest对象
     * @return 返回Response对象
     */
    @RequestMapping(value = "/menu/delete", method = RequestMethod.POST)
    @ResponseBody
    public Response delete(@RequestBody BaseDTO dto, HttpServletRequest request) {
        Response response = new Response();

        //从公共dto中提取menuDTO
        List<MenuDTO> menuDTOs = (List<MenuDTO>) (Object) getDTO("menu", MenuDTO.class, dto);
        if (CollectionUtils.isEmpty(menuDTOs)) {
            throw new BusinessException("找不到请求数据");
        }
        MenuDTO menuDTO = menuDTOs.get(0);

        //校验字段
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<MenuDTO>> violations = validator.validate(menuDTO, MenuDTO.DeleteGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);
        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式出错", map);
        }

        //判断该菜单是否可以被删除
        MenuPO menu = menuService.selectById(menuDTO.getId());
        if (menu == null) {
            throw new BusinessException("未找到该菜单或该菜单不允许删除");
        }
        if (menu.getDeleteAble() == 0) {
            throw new BusinessException("该菜单不允许被删除");
        }

        //删除菜单，并更新ServletContext中的属性
        menuService.deleteById(menuDTO.getId());

        ServletContext application = request.getServletContext();
        List<MenuPO> menuPOs = menuService.selectByCondition(new HashMap<>());
        application.setAttribute("menu", menuPOs);

        response.setStatus(Response.SUCCESS);
        response.setMessage("删除成功");

        return response;
    }

    /**
     * 新增菜单
     *
     * @param dto     公共dto对象
     * @param request HttpServletRequest对象
     * @return 返回Response对象
     */
    @RequestMapping(value = "/menu/insert", method = RequestMethod.POST)
    @ResponseBody
    public Response insert(@RequestBody BaseDTO dto, HttpServletRequest request) {
        Response response = new Response();

        //从公共dto中提取menuDTO对象
        List<MenuDTO> menuDTOs = (List<MenuDTO>) (Object) getDTO("menu", MenuDTO.class, dto);
        if (CollectionUtils.isEmpty(menuDTOs)) {
            throw new BusinessException("找不到请求数据");
        }
        MenuDTO menuDTO = menuDTOs.get(0);

        //校验字段
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<MenuDTO>> violations = validator.validate(menuDTO, MenuDTO.InsertGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);
        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式出错", map);
        }

        MenuPO menuPO = new MenuPO();
        BeanUtils.copyProperties(menuDTO, menuPO);
        menuPO.setDeleteAble(1);
        menuPO.setDate(new Date());
        menuService.insert(menuPO);

        //更新ServletContext中的属性
        ServletContext application = request.getServletContext();
        List<MenuPO> menuPOs = menuService.selectByCondition(new HashMap<>());
        application.setAttribute("menu", menuPOs);

        response.setStatus(Response.SUCCESS);
        response.setMessage("菜单新增成功");

        return response;
    }

    /**
     * 菜单更新
     *
     * @param dto     公共dto对象
     * @param request HttpServletRequest对象
     * @return 返回Response对象
     */
    @RequestMapping(value = "/menu/update", method = RequestMethod.POST)
    @ResponseBody
    public Response update(@RequestBody BaseDTO dto, HttpServletRequest request) {
        Response response = new Response();

        //从公共dto中获取menuDTO对象
        List<MenuDTO> menuDTOs = (List<MenuDTO>) (Object) getDTO("menu", MenuDTO.class, dto);
        if (CollectionUtils.isEmpty(menuDTOs)) {
            throw new BusinessException("找不到请求数据");
        }
        MenuDTO menuDTO = menuDTOs.get(0);

        //校验字段
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<MenuDTO>> violations = validator.validate(menuDTO, MenuDTO.UpdateGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);
        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式错误", map);
        }

        //检查菜单是否可以被删除
        MenuPO menuPO = menuService.selectById(menuDTO.getId());
        if (menuPO == null) {
            throw new BusinessException("未找到该菜单");
        }
        if (menuPO.getDeleteAble() == 0) {
            throw new BusinessException("该菜单不允许被修改");
        }

        //更新菜单，并更新ServletContext中的属性
        MenuPO updateMenuPO = new MenuPO();
        BeanUtils.copyProperties(menuDTO, updateMenuPO);
        menuService.update(updateMenuPO);
        ServletContext application = request.getServletContext();
        List<MenuPO> menuPOs = menuService.selectByCondition(new HashMap<>());
        application.setAttribute("menu", menuPOs);
        response.setStatus(Response.SUCCESS);
        response.setMessage("更新成功");

        return response;
    }
}
