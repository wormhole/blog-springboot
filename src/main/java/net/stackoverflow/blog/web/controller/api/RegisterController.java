package net.stackoverflow.blog.web.controller.api;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.BaseDTO;
import net.stackoverflow.blog.common.Response;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.dto.UserDTO;
import net.stackoverflow.blog.pojo.entity.Role;
import net.stackoverflow.blog.pojo.entity.User;
import net.stackoverflow.blog.service.RoleService;
import net.stackoverflow.blog.service.UserService;
import net.stackoverflow.blog.util.CollectionUtils;
import net.stackoverflow.blog.util.TransferUtils;
import net.stackoverflow.blog.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 注册接口Controller
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping("/api")
public class RegisterController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ValidatorFactory validatorFactory;

    /**
     * 注册接口 /register
     *
     * @param dto
     * @param session
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Response register(@RequestBody BaseDTO dto, HttpSession session) {

        Response response = new Response();

        List<UserDTO> dtos = (List<UserDTO>) (Object) getDTO("user", UserDTO.class, dto);
        if (CollectionUtils.isEmpty(dtos)) {
            throw new BusinessException("找不到请求数据");
        }
        UserDTO userDTO = dtos.get(0);

        List<User> users = userService.selectByCondition(new HashMap<String, Object>() {{
            put("deleteAble", 0);
        }});

        if (users.size() == 0) {

            Validator validator = validatorFactory.getValidator();
            Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO, UserDTO.RegisterGroup.class);
            Map<String, String> map = ValidationUtils.errorMap(violations);

            if (!CollectionUtils.isEmpty(map)) {
                throw new BusinessException("注册信息格式出错", map);
            }

            String vcode = (String) session.getAttribute("vcode");
            if (!vcode.equalsIgnoreCase(userDTO.getVcode())) {
                map.put("vcode", "验证码错误");
                throw new BusinessException("验证码错误", map);
            }

            if (userService.selectByCondition(new HashMap<String, Object>() {{
                put("email", userDTO.getEmail());
            }}).size() != 0) {
                map.put("email", "邮箱已经存在");
                throw new BusinessException("邮箱已经存在", map);
            }

            User user = (User) TransferUtils.dto2po(User.class, userDTO);
            user.setDeleteAble(0);
            User newUser = userService.insert(user);
            List<Role> roles = roleService.selectByCondition(new HashMap<String, Object>() {{
                put("code", "admin");
            }});
            userService.grantRole(newUser.getId(), roles.get(0).getId());
            response.setStatus(Response.SUCCESS);
            response.setMessage("注册成功");

        } else {
            throw new BusinessException("不能再进行注册");
        }

        return response;

    }

}
