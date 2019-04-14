package net.stackoverflow.blog.web.controller.admin.user;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.BaseDTO;
import net.stackoverflow.blog.common.Response;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.dto.UserDTO;
import net.stackoverflow.blog.pojo.po.UserPO;
import net.stackoverflow.blog.service.UserService;
import net.stackoverflow.blog.util.CollectionUtils;
import net.stackoverflow.blog.util.PasswordUtils;
import net.stackoverflow.blog.util.ValidationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 个人信息维护接口
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping(value = "/admin/user")
public class PersonalController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisCacheManager redisCacheManager;
    @Autowired
    private ValidatorFactory validatorFactory;

    /**
     * 个人信息维护页面跳转
     *
     * @return
     */
    @RequestMapping(value = "/personal", method = RequestMethod.GET)
    public ModelAndView personal() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/user/personal");
        return mv;
    }

    /**
     * 用户信息更新接口
     *
     * @param type    更新类型：1、base基础数据，2、password密码
     * @param dto     公共dto对象
     * @param session 会话对象
     * @return 返回Response对象
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Response updateUser(@RequestParam("type") String type, @RequestBody BaseDTO dto, HttpSession session) {
        Response response = new Response();

        //从公共dto中获取userDTO
        List<UserDTO> userDTOs = (List<UserDTO>) (Object) getDTO("user", UserDTO.class, dto);
        if (CollectionUtils.isEmpty(userDTOs)) {
            throw new BusinessException("找不到请求数据");
        }
        UserDTO userDTO = userDTOs.get(0);
        UserPO user = (UserPO) session.getAttribute("user");

        if (type.equals("base")) {
            //校验数据
            Validator validator = validatorFactory.getValidator();
            Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO, UserDTO.UpdateBaseGroup.class);
            Map<String, String> map = ValidationUtils.errorMap(violations);
            if (!CollectionUtils.isEmpty(map)) {
                throw new BusinessException("字段格式出错", map);
            }

            //校验邮箱是否重复
            if (!userDTO.getEmail().equals(user.getEmail()) && userService.selectByCondition(new HashMap<String, Object>() {{
                put("email", userDTO.getEmail());
            }}).size() != 0) {
                throw new BusinessException("邮箱已经存在");
            }

            UserPO updateUser = new UserPO();
            BeanUtils.copyProperties(userDTO, updateUser);
            updateUser.setId(user.getId());

            //删除缓存
            if (!updateUser.getEmail().equals(user.getEmail())) {
                Cache authenticationCache = redisCacheManager.getCache("authentication");
                authenticationCache.evict("shiro:authentication:" + user.getEmail());
                Cache authorizationCache = redisCacheManager.getCache("authorization");
                authorizationCache.evict("shiro:authorization:" + user.getEmail());
            }

            //更新用户基础信息
            UserPO newUser = userService.update(updateUser);
            session.setAttribute("user", newUser);
            response.setStatus(Response.SUCCESS);
            response.setMessage("基础信息修改成功");

        } else if (type.equals("password")) {
            //校验数据
            Validator validator = validatorFactory.getValidator();
            Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO, UserDTO.UpdatePasswordGroup.class);
            Map<String, String> map = ValidationUtils.errorMap(violations);
            if (!CollectionUtils.isEmpty(map)) {
                throw new BusinessException("字段格式出错", map);
            }

            //检查是否与旧密码匹配
            if (!user.getPassword().equals(PasswordUtils.encryptPassword(user.getSalt(), userDTO.getOldPassword()))) {
                throw new BusinessException("旧密码不匹配");
            }

            UserPO updateUser = new UserPO();
            BeanUtils.copyProperties(userDTO, updateUser);
            updateUser.setId(user.getId());
            updateUser.setEmail(user.getEmail());
            updateUser.setSalt(PasswordUtils.getSalt());
            updateUser.setPassword(PasswordUtils.encryptPassword(updateUser.getSalt(), updateUser.getPassword()));

            //删除缓存
            Cache authenticationCache = redisCacheManager.getCache("authentication");
            authenticationCache.evict("shiro:authentication:" + user.getEmail());
            Cache authorizationCache = redisCacheManager.getCache("authorization");
            authorizationCache.evict("shiro:authorization:" + user.getEmail());

            UserPO newUser = userService.update(updateUser);
            session.setAttribute("user", newUser);
            response.setStatus(Response.SUCCESS);
            response.setMessage("修改成功");

        } else {
            throw new BusinessException("未知请求参数type");
        }

        return response;
    }
}
