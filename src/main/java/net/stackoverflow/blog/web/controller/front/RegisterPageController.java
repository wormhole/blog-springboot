package net.stackoverflow.blog.web.controller.front;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.Result;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.entity.Setting;
import net.stackoverflow.blog.pojo.entity.User;
import net.stackoverflow.blog.pojo.vo.UserVO;
import net.stackoverflow.blog.service.SettingService;
import net.stackoverflow.blog.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户注册Controller
 *
 * @author 凉衫薄
 */
@Controller
public class RegisterPageController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private SettingService settingService;

    /**
     * 注册页面跳转
     *
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registerPage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/register");
        return mv;
    }

    /**
     * 用户注册接口
     *
     * @param userVO
     * @param session
     * @param errors
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity register(@Validated(UserVO.RegisterGroup.class) @RequestBody UserVO userVO, Errors errors, HttpSession session) {

        //校验数据
        if (errors.hasErrors()) {
            Map<String, String> errMap = new HashMap<>();
            List<ObjectError> oes = errors.getAllErrors();
            for (ObjectError oe : oes) {
                if (oe instanceof FieldError) {
                    FieldError fe = (FieldError) oe;
                    errMap.put(fe.getField(), oe.getDefaultMessage());
                } else {
                    errMap.put(oe.getObjectName(), oe.getDefaultMessage());
                }
            }
            throw new BusinessException("字段格式错误", errMap);
        }

        //校验验证码
        String vcode = (String) session.getAttribute("vcode");
        if (!vcode.equalsIgnoreCase(userVO.getVcode())) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("vcode", "验证码错误");
            throw new BusinessException("验证码错误", errorMap);
        }

        //检查系统是否开放注册
        List<Setting> settings = settingService.selectByCondition(new HashMap<String, Object>() {{
            put("name", "register");
        }});
        if (settings.size() != 0 && settings.get(0).getValue().equals("0")) {
            throw new BusinessException("暂未开放注册，请联系管理员");
        }

        //校验邮箱是否已经被注册
        if (userService.selectByCondition(new HashMap<String, Object>() {{
            put("email", userVO.getEmail());
        }}).size() != 0) {
            Map<String, String> errMap = new HashMap<>();
            errMap.put("email", "邮箱已经存在");
            throw new BusinessException("邮箱已经存在", errMap);
        }

        //保存至数据库
        User user = new User();
        BeanUtils.copyProperties(userVO, user);
        user.setDeleteAble(1);
        userService.insert(user);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setMessage("注册成功");
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
