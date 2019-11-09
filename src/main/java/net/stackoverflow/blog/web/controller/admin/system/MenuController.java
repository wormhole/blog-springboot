package net.stackoverflow.blog.web.controller.admin.system;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.common.Result;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.entity.Menu;
import net.stackoverflow.blog.pojo.vo.MenuVO;
import net.stackoverflow.blog.service.MenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 菜单管理Controller
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping(value = "/admin/system")
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;

    /**
     * 菜单管理页面跳转
     *
     * @return 返回ModelAndView对象
     */
    @RequestMapping(value = "/menu_management", method = RequestMethod.GET)
    public ModelAndView management() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/system/menu_management");
        return mv;
    }

    /**
     * 查询菜单列表接口
     *
     * @param page  分页参数
     * @param limit 每页数量
     * @return
     */
    @RequestMapping(value = "/list_menu", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity list(@RequestParam(value = "page") String page, @RequestParam(value = "limit") String limit) {

        //分页查询
        Page pageParam = new Page(Integer.valueOf(page), Integer.valueOf(limit), null);
        List<Menu> menus = menuService.selectByPage(pageParam);
        int count = menuService.selectByCondition(new HashMap<>(16)).size();

        List<MenuVO> menuVOs = new ArrayList<>();
        for (Menu menu : menus) {
            MenuVO menuVO = new MenuVO();
            BeanUtils.copyProperties(menu, menuVO);
            menuVO.setName(HtmlUtils.htmlEscape(menu.getName()));
            if (menu.getDeleteAble() == 0) {
                menuVO.setDeleteTag("否");
            } else {
                menuVO.setDeleteTag("是");
            }
            menuVOs.add(menuVO);
        }

        Map<String, Object> map = new HashMap<>(16);
        map.put("count", count);
        map.put("items", menuVOs);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setMessage("菜单查询成功");
        result.setData(map);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 删除菜单接口
     *
     * @param menuVO
     * @param errors
     * @param request
     * @return
     */
    @RequestMapping(value = "/delete_menu", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity delete(@Validated(MenuVO.DeleteGroup.class) @RequestBody MenuVO menuVO, Errors errors, HttpServletRequest request) {

        //校验数据
        checkErrors(errors);

        //判断该菜单是否可以被删除
        Menu menu = menuService.selectById(menuVO.getId());
        if (menu == null) {
            throw new BusinessException("未找到该菜单或该菜单不允许删除");
        }
        if (menu.getDeleteAble() == 0) {
            throw new BusinessException("该菜单不允许被删除");
        }

        //删除菜单，并更新ServletContext中的属性
        menuService.delete(menuVO.getId());

        ServletContext application = request.getServletContext();
        List<Menu> menus = menuService.selectByCondition(new HashMap<>(16));
        application.setAttribute("menu", menus);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setMessage("删除成功");

        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 新增菜单接口
     *
     * @param menuVO
     * @param errors
     * @param request
     * @return
     */
    @RequestMapping(value = "/insert_menu", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity insert(@Validated(MenuVO.InsertGroup.class) @RequestBody MenuVO menuVO, Errors errors, HttpServletRequest request) {

        //校验数据
        checkErrors(errors);

        Menu menu = new Menu();
        BeanUtils.copyProperties(menuVO, menu);
        menu.setDeleteAble(1);
        menu.setDate(new Date());
        menuService.insert(menu);

        //更新ServletContext中的属性
        ServletContext application = request.getServletContext();
        List<Menu> menus = menuService.selectByCondition(new HashMap<>(16));
        application.setAttribute("menu", menus);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setMessage("菜单新增成功");

        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 更新菜单接口
     *
     * @param menuVO
     * @param errors
     * @param request
     * @return
     */
    @RequestMapping(value = "/update_menu", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity update(@Validated(MenuVO.UpdateGroup.class) @RequestBody MenuVO menuVO, Errors errors, HttpServletRequest request) {

        //校验数据
        checkErrors(errors);

        //检查菜单是否可以被删除
        Menu menu = menuService.selectById(menuVO.getId());
        if (menu == null) {
            throw new BusinessException("未找到该菜单");
        }
        if (menu.getDeleteAble() == 0) {
            throw new BusinessException("该菜单不允许被修改");
        }

        //更新菜单，并更新ServletContext中的属性
        Menu updateMenu = new Menu();
        BeanUtils.copyProperties(menuVO, updateMenu);
        menuService.update(updateMenu);
        ServletContext application = request.getServletContext();
        List<Menu> menus = menuService.selectByCondition(new HashMap<>(16));
        application.setAttribute("menu", menus);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setMessage("更新成功");

        return new ResponseEntity(result, HttpStatus.OK);
    }
}
