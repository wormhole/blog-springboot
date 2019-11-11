package net.stackoverflow.blog.web.controller.admin.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.Result;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.entity.Setting;
import net.stackoverflow.blog.pojo.vo.SettingVO;
import net.stackoverflow.blog.service.SettingService;
import net.stackoverflow.blog.util.DateUtils;
import net.stackoverflow.blog.validator.SettingVOValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统设置接口
 *
 * @author 凉衫薄
 */
@Api(description = "系统设置")
@Controller
@RequestMapping(value = "/admin/system")
public class SettingController extends BaseController {

    @Autowired
    private SettingService settingService;
    @Value("${server.upload.path}")
    private String path;

    /**
     * 配置页面跳转
     *
     * @return
     */
    @ApiOperation(value = "配置页面跳转")
    @RequestMapping(value = "/setting_management", method = RequestMethod.GET)
    public ModelAndView setting() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/system/setting_management");
        return mv;
    }

    /**
     * 更新基本设置
     *
     * @param settingVOs
     * @param request
     * @return
     */
    @ApiOperation(value = "更新基本配置接口", response = Result.class)
    @RequestMapping(value = "/update_setting", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity update(@ApiParam(name = "settingVOs", value = "设置VO对象列表") @RequestBody SettingVO[] settingVOs,
                                 HttpServletRequest request) {

        ServletContext application = request.getServletContext();

        //校验数据
        SettingVOValidator validator = new SettingVOValidator();
        Map<String, String> errMap = validator.validate(settingVOs);
        if (errMap != null) {
            throw new BusinessException("字段格式错误", errMap);
        }

        for (SettingVO settingVO : settingVOs) {
            Setting setting = new Setting();
            BeanUtils.copyProperties(settingVO, setting);
            settingService.update(setting);
        }

        //更新ServletContext中的属性
        List<Setting> settings = settingService.selectByCondition(new HashMap<>());
        Map<String, Object> settingMap = new HashMap<>();
        for (Setting setting : settings) {
            settingMap.put(setting.getName(), setting.getValue());
        }
        application.setAttribute("setting", settingMap);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setMessage("配置更改成功");

        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 更改头像
     *
     * @param request HttpServletRequest对象
     * @return
     */
    @ApiOperation(value = "更新头像接口", response = Result.class)
    @RequestMapping(value = "/update_head", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity updateHead(HttpServletRequest request) {
        ServletContext application = request.getServletContext();

        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multiRequest.getFile("headImg");
        String fileName = file.getOriginalFilename();
        String dataPath = DateUtils.getDatePath();
        String destDir = path + dataPath;

        File destDirFile = new File(destDir);
        if (!destDirFile.exists()) {
            destDirFile.mkdirs();
        }

        File destFile = new File(destDirFile, fileName);
        try {
            file.transferTo(destFile);
            String url = "/upload" + dataPath + fileName;

            List<Setting> settings = settingService.selectByCondition(new HashMap<String, Object>(16) {{
                put("name", "head");
            }});

            Setting setting = settings.get(0);
            setting.setValue(url);
            settingService.update(setting);

            Map<String, Object> settingMap = (Map<String, Object>) application.getAttribute("setting");
            settingMap.replace("head", url);
            application.setAttribute("setting", settingMap);

            Result result = new Result();
            result.setStatus(Result.SUCCESS);
            result.setMessage("修改成功");
            result.setData(setting);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (IOException e) {
            throw new BusinessException("头像上传失败");
        }
    }
}
