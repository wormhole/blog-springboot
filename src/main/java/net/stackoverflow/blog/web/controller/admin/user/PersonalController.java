package net.stackoverflow.blog.web.controller.admin.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.Result;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.entity.User;
import net.stackoverflow.blog.pojo.vo.UserVO;
import net.stackoverflow.blog.service.UserService;
import net.stackoverflow.blog.util.PasswordUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * 个人信息维护Controller
 *
 * @author 凉衫薄
 */
@Api(description = "个人信息维护")
@Controller
@RequestMapping(value = "/admin/user")
public class PersonalController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisCacheManager redisCacheManager;

    /**
     * 个人信息维护页面跳转
     *
     * @return
     */
    @ApiOperation(value = "个人信息维护页面跳转")
    @RequestMapping(value = "/personal_management", method = RequestMethod.GET)
    public ModelAndView personal() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/user/personal_management");
        return mv;
    }

    /**
     * 更新用户基础信息接口
     *
     * @param userVO
     * @param session
     * @return
     */
    @ApiOperation(value = "更新用户基础信息接口", response = Result.class)
    @RequestMapping(value = "/update_base", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity updateBase(@ApiParam(name = "userVO", value = "用户VO对象") @Validated(UserVO.UpdateBaseGroup.class) @RequestBody UserVO userVO, HttpSession session) {

        User user = (User) session.getAttribute("user");

        //校验邮箱是否重复
        if (!userVO.getEmail().equals(user.getEmail()) && userService.selectByCondition(new HashMap<String, Object>(16) {{
            put("email", userVO.getEmail());
        }}).size() != 0) {
            throw new BusinessException("邮箱已经存在");
        }

        User updateUser = new User();
        BeanUtils.copyProperties(userVO, updateUser);
        updateUser.setId(user.getId());

        //删除缓存
        if (!updateUser.getEmail().equals(user.getEmail())) {
            Cache authenticationCache = redisCacheManager.getCache("authentication");
            authenticationCache.evict("shiro:authentication:" + user.getEmail());
            Cache authorizationCache = redisCacheManager.getCache("authorization");
            authorizationCache.evict("shiro:authorization:" + user.getEmail());
        }

        //更新用户基础信息
        User newUser = userService.update(updateUser);
        session.setAttribute("user", newUser);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setMessage("基础信息修改成功");
        return new ResponseEntity(result, HttpStatus.OK);

    }

    /**
     * 更新用户密码接口
     *
     * @param userVO
     * @param session
     * @return
     */
    @ApiOperation(value = "更新用户密码接口", response = Result.class)
    @RequestMapping(value = "/update_password", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity updatePassword(@ApiParam(name = "userVO", value = "用户VO对象") @Validated(UserVO.UpdatePasswordGroup.class) @RequestBody UserVO userVO, HttpSession session) {

        User user = (User) session.getAttribute("user");

        //检查是否与旧密码匹配
        if (!user.getPassword().equals(PasswordUtils.encryptPassword(user.getSalt(), userVO.getOldPassword()))) {
            throw new BusinessException("旧密码不匹配");
        }

        User updateUser = new User();
        BeanUtils.copyProperties(userVO, updateUser);
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setSalt(PasswordUtils.getSalt());
        updateUser.setPassword(PasswordUtils.encryptPassword(updateUser.getSalt(), updateUser.getPassword()));

        //删除缓存
        Cache authenticationCache = redisCacheManager.getCache("authentication");
        authenticationCache.evict("shiro:authentication:" + user.getEmail());
        Cache authorizationCache = redisCacheManager.getCache("authorization");
        authorizationCache.evict("shiro:authorization:" + user.getEmail());

        User newUser = userService.update(updateUser);
        session.setAttribute("user", newUser);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setMessage("修改成功");

        return new ResponseEntity(result, HttpStatus.OK);
    }

}
