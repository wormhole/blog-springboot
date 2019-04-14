package net.stackoverflow.blog.web.controller.front;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.BaseDTO;
import net.stackoverflow.blog.common.Response;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.dto.UserDTO;
import net.stackoverflow.blog.pojo.po.SettingPO;
import net.stackoverflow.blog.pojo.po.UserPO;
import net.stackoverflow.blog.service.SettingService;
import net.stackoverflow.blog.service.UserService;
import net.stackoverflow.blog.util.CollectionUtils;
import net.stackoverflow.blog.util.ValidationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
 * 注册接口
 *
 * @author 凉衫薄
 */
@Controller
public class RegisterPageController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private ValidatorFactory validatorFactory;

    /**
     * 注册页面跳转
     *
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/register");
        return mv;
    }

    /**
     * 注册接口
     *
     * @param dto     前端dto参数
     * @param session 会话对象
     * @return 返回Response对象
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Response register(@RequestBody BaseDTO dto, HttpSession session) {

        Response response = new Response();

        //从BaseDTO中获取具体dto对象
        List<UserDTO> userDTOs = (List<UserDTO>) (Object) getDTO("user", UserDTO.class, dto);
        if (CollectionUtils.isEmpty(userDTOs)) {
            throw new BusinessException("找不到请求数据");
        }
        UserDTO userDTO = userDTOs.get(0);

        //校验验证码
        String vcode = (String) session.getAttribute("vcode");
        if (!vcode.equalsIgnoreCase(userDTO.getVcode())) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("vcode", "验证码错误");
            throw new BusinessException("验证码错误", errorMap);
        }

        //检查系统是否开放注册
        List<SettingPO> settingPOs = settingService.selectByCondition(new HashMap<String, Object>() {{
            put("name", "register");
        }});
        if (settingPOs.size() != 0 && settingPOs.get(0).getValue().equals("0")) {
            throw new BusinessException("暂未开放注册，请联系管理员");
        }

        //校验数据
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO, UserDTO.RegisterGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);
        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("注册信息格式出错", map);
        }

        //校验邮箱是否已经被注册
        if (userService.selectByCondition(new HashMap<String, Object>() {{
            put("email", userDTO.getEmail());
        }}).size() != 0) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("email", "邮箱已经存在");
            throw new BusinessException("邮箱已经存在", errorMap);
        }

        UserPO user = new UserPO();
        BeanUtils.copyProperties(userDTO, user);
        user.setDeleteAble(1);
        userService.insert(user);
        response.setStatus(Response.SUCCESS);
        response.setMessage("注册成功");

        return response;
    }
}
