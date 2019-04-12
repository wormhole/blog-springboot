package net.stackoverflow.blog.web.controller.api.admin;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.Response;
import net.stackoverflow.blog.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * 图片管理接口Controller
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping("/api/admin")
public class ImageController extends BaseController {

    @Value("${server.upload.path}")
    private String path;

    /**
     * 图片删除接口 /api/admin/image/delete
     * 方法 POST
     *
     * @param url
     * @return
     */
    @RequestMapping(value = "/image/delete", method = RequestMethod.POST)
    public Response delete(@RequestParam("url") String url) {
        Response response = new Response();
        url = url.replaceFirst("/upload", "");
        File file = new File(path, url);
        if (file.exists()) {
            file.delete();
            response.setStatus(Response.SUCCESS);
            response.setMessage("图片删除成功");
        } else {
            throw new BusinessException("图片删除失败");
        }
        return response;
    }
}
