package net.stackoverflow.blog.web.controller.api.admin;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.BaseDTO;
import net.stackoverflow.blog.common.Response;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.dto.UserDTO;
import net.stackoverflow.blog.pojo.entity.User;
import net.stackoverflow.blog.service.UserService;
import net.stackoverflow.blog.util.CollectionUtils;
import net.stackoverflow.blog.util.PasswordUtils;
import net.stackoverflow.blog.util.TransferUtils;
import net.stackoverflow.blog.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户数据获取接口Controller
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping("/api/admin")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisCacheManager redisCacheManager;
    @Autowired
    private ValidatorFactory validatorFactory;

    /**
     * 用户信息更新接口
     * /api/admin/user/update
     * 方法 POST
     *
     * @param type
     * @param dto
     * @param session
     * @return
     */
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public Response updateUser(@RequestParam("type") String type, @RequestBody BaseDTO dto, HttpSession session) {
        Response response = new Response();

        List<UserDTO> dtos = (List<UserDTO>) (Object) getDTO("user", UserDTO.class, dto);
        if (CollectionUtils.isEmpty(dtos)) {
            throw new BusinessException("找不到请求数据");
        }
        UserDTO userDTO = dtos.get(0);
        User user = (User) session.getAttribute("user");

        if (type.equals("base")) {

            Validator validator = validatorFactory.getValidator();
            Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO, UserDTO.UpdateBaseGroup.class);
            Map<String, String> map = ValidationUtils.errorMap(violations);

            if (!CollectionUtils.isEmpty(map)) {
                throw new BusinessException("字段格式出错", map);
            }

            if (!userDTO.getEmail().equals(user.getEmail()) && userService.selectByCondition(new HashMap<String, Object>() {{
                put("email", userDTO.getEmail());
            }}).size() != 0) {
                throw new BusinessException("邮箱已经存在");
            }

            User updateUser = (User) TransferUtils.dto2po(User.class, userDTO);
            updateUser.setId(user.getId());

            if (!updateUser.getEmail().equals(user.getEmail())) {
                Cache authenticationCache = redisCacheManager.getCache("authentication");
                authenticationCache.evict("shiro:authentication:" + user.getEmail());
                Cache authorizationCache = redisCacheManager.getCache("authorization");
                authorizationCache.evict("shiro:authorization:" + user.getEmail());
            }

            User newUser = userService.update(updateUser);
            session.setAttribute("user", newUser);
            response.setStatus(Response.SUCCESS);
            response.setMessage("基础信息修改成功");

        } else if (type.equals("password")) {

            Validator validator = validatorFactory.getValidator();
            Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO, UserDTO.UpdatePasswordGroup.class);
            Map<String, String> map = ValidationUtils.errorMap(violations);

            if (!CollectionUtils.isEmpty(map)) {
                throw new BusinessException("字段格式出错", map);
            }

            if (!user.getPassword().equals(PasswordUtils.encryptPassword(user.getSalt(), userDTO.getOldPassword()))) {
                throw new BusinessException("旧密码不匹配");
            }

            User updateUser = (User) TransferUtils.dto2po(User.class, userDTO);
            updateUser.setId(user.getId());
            updateUser.setEmail(user.getEmail());
            updateUser.setSalt(PasswordUtils.getSalt());
            updateUser.setPassword(PasswordUtils.encryptPassword(updateUser.getSalt(), updateUser.getPassword()));

            Cache authenticationCache = redisCacheManager.getCache("authentication");
            authenticationCache.evict("shiro:authentication:" + user.getEmail());
            Cache authorizationCache = redisCacheManager.getCache("authorization");
            authorizationCache.evict("shiro:authorization:" + user.getEmail());

            User newUser = userService.update(updateUser);
            session.setAttribute("user", newUser);
            response.setStatus(Response.SUCCESS);
            response.setMessage("修改成功");

        } else {
            throw new BusinessException("未知请求参数type");
        }

        return response;
    }
}
