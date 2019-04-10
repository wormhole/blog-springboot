package net.stackoverflow.blog.web.controller.page.admin.media;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 图片管理页面跳转Controller
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping("/admin/media")
public class ImageManagePageController {

    /**
     * 页面跳转 /admin/media/image-manage
     * 方法 GET
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/image-manage", method = RequestMethod.GET)
    public ModelAndView image(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        String uploadDir = request.getServletContext().getRealPath("upload");

        Map<String, List<String>> imageMap = new TreeMap<>();

        traverseFolder(uploadDir, imageMap);
        mv.addObject("map", imageMap);
        mv.setViewName("/admin/media/image-manage");
        return mv;
    }

    /**
     * 递归遍历文件夹列出图片
     *
     * @param path
     * @param imageMap
     */
    public void traverseFolder(String path, Map<String, List<String>> imageMap) {

        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                return;
            } else {
                for (File sonFile : files) {
                    if (sonFile.isDirectory()) {
                        traverseFolder(sonFile.getAbsolutePath(), imageMap);
                    } else {
                        addUrlToMap(imageMap, sonFile.getAbsolutePath());
                    }
                }
            }
        }
    }

    /**
     * 图片url根据规则添加进map
     *
     * @param imageMap
     * @param path
     */
    public void addUrlToMap(Map<String, List<String>> imageMap, String path) {
        String str = File.separator.equals("/") ? "/" : "\\\\";
        String[] paths = path.split(str);
        String date = paths[paths.length - 4] + "/" + paths[paths.length - 3] + "/" + paths[paths.length - 2];
        String url = "/" + paths[paths.length - 5] + "/" + date + "/" + paths[paths.length - 1];
        if (imageMap.get(date) == null) {
            List<String> list = new ArrayList<>();
            list.add(url);
            imageMap.put(date, list);
        } else {
            List<String> list = imageMap.get(date);
            list.add(url);
            imageMap.put(date, list);
        }
    }
}
