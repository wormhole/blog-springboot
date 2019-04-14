package net.stackoverflow.blog.web.controller.admin.system;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.BaseDTO;
import net.stackoverflow.blog.common.Response;
import net.stackoverflow.blog.exception.BusinessException;
import net.stackoverflow.blog.pojo.dto.SettingDTO;
import net.stackoverflow.blog.pojo.po.SettingPO;
import net.stackoverflow.blog.service.SettingService;
import net.stackoverflow.blog.util.CollectionUtils;
import net.stackoverflow.blog.util.DateUtils;
import net.stackoverflow.blog.validator.SettingValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@Controller
@RequestMapping(value = "/admin/system")
public class SettingController extends BaseController {

    @Autowired
    private SettingService settingService;
    @Autowired
    private SettingValidator settingValidator;
    @Value("${server.upload.path}")
    private String path;

    /**
     * 配置页面跳转
     *
     * @return
     */
    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    public ModelAndView setting() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/system/setting");
        return mv;
    }

    /**
     * 更新基本设置
     *
     * @param dto     公共dto对象
     * @param request HttpServletRequest请求对象
     * @return 返回Response
     */
    @RequestMapping(value = "/setting/update", method = RequestMethod.POST)
    @ResponseBody
    public Response update(@RequestBody BaseDTO dto, HttpServletRequest request) {
        Response response = new Response();
        ServletContext application = request.getServletContext();

        //从公共DTO中提取settingDTO
        List<SettingDTO> settingDTOs = (List<SettingDTO>) (Object) getDTO("setting", SettingDTO.class, dto);
        if (CollectionUtils.isEmpty(settingDTOs)) {
            throw new BusinessException("找不到请求数据");
        }
        SettingDTO[] settingDTOArray = settingDTOs.toArray(new SettingDTO[0]);

        //校验字段
        Map<String, String> map = settingValidator.validate(settingDTOArray);
        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式错误", map);
        }

        for (SettingDTO settingDTO : settingDTOArray) {
            SettingPO setting = new SettingPO();
            BeanUtils.copyProperties(settingDTO, setting);
            settingService.update(setting);
        }

        //更新ServletContext中的属性
        List<SettingPO> settingPOs = settingService.selectByCondition(new HashMap<>());
        Map<String, Object> settingMap = new HashMap<>();
        for (SettingPO setting : settingPOs) {
            settingMap.put(setting.getName(), setting.getValue());
        }
        application.setAttribute("setting", settingMap);

        response.setStatus(Response.SUCCESS);
        response.setMessage("配置更改成功");

        return response;
    }

    /**
     * 更改头像
     *
     * @param request HttpServletRequest对象
     * @return 返回Response
     */
    @RequestMapping(value = "/setting/head", method = RequestMethod.POST)
    @ResponseBody
    public Response updateHead(HttpServletRequest request) {
        Response response = new Response();
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
            SettingPO setting = new SettingPO();
            setting.setName("head");
            setting.setValue(url);
            settingService.update(setting);

            Map<String, Object> settingMap = (Map<String, Object>) application.getAttribute("setting");
            settingMap.replace("head", url);
            application.setAttribute("setting", settingMap);

            response.setStatus(Response.SUCCESS);
            response.setMessage("修改成功");
            response.setData(setting);
        } catch (IOException e) {
            throw new BusinessException("头像上传失败");
        }
        return response;
    }
}
