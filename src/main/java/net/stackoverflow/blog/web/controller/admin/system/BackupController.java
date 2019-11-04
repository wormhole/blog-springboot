package net.stackoverflow.blog.web.controller.admin.system;

import net.stackoverflow.blog.util.DBUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 数据库备份接口Controller
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping(value = "/admin/system")
@PropertySource(value = {"classpath:application.properties"})
public class BackupController {

    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.host}")
    private String host;
    @Value("${spring.datasource.db}")
    private String db;
    @Value("${server.backup.path}")
    private String path;

    /**
     * 备份页面跳转
     *
     * @return 返回ModelAndView对象
     */
    @RequestMapping(value = "/backup", method = RequestMethod.GET)
    public ModelAndView backup() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/system/backup");
        return mv;
    }

    /**
     * 导出数据库备份文件
     *
     * @return 返回ResponseEntity对象
     * @throws IOException
     */
    @RequestMapping(value = "/backup/db", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> exportSql() throws IOException {
        String filename = "blog.sql";
        DBUtils.backup(host, username, password, path, filename, db);

        InputStream is = new FileInputStream(new File(path, filename));
        byte[] body = new byte[is.available()];
        is.read(body);

        filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename=" + filename);
        HttpStatus status = HttpStatus.OK;
        ResponseEntity<byte[]> entity = new ResponseEntity<>(body, headers, status);
        return entity;
    }
}
