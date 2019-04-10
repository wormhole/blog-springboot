package net.stackoverflow.blog.web.controller.api.admin;

import net.stackoverflow.blog.common.BaseController;
import net.stackoverflow.blog.common.Response;
import net.stackoverflow.blog.exception.BusinessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 图片管理接口Controller
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping("/api/admin")
public class ImageController extends BaseController {

    /**
     * 图片删除接口 /api/admin/image/delete
     * 方法 POST
     *
     * @param request
     * @param url
     * @return
     */
    @RequestMapping(value = "/image/delete", method = RequestMethod.POST)
    public Response delete(HttpServletRequest request, @RequestParam("url") String url) {
        Response response = new Response();
        String rootDir = request.getServletContext().getRealPath("");
        File file = new File(rootDir, url);
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
