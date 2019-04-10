package net.stackoverflow.blog.web.controller.api.admin;

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

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 数据库备份接口Controller
 *
 * @author 凉衫薄
 */
@Controller
@PropertySource(value = {"classpath:application.properties"})
@RequestMapping("/api/admin")
public class BackupController {

    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.host}")
    private String host;
    @Value("${spring.datasource.db}")
    private String db;

    /**
     * 导出sql备份文件 /api/admin/backup/sql
     * 方法 GET
     *
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/backup/sql", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> exportSql(HttpServletRequest request) throws IOException {
        String filename = "blog.sql";
        String backupPath = request.getServletContext().getRealPath("WEB-INF/backup");
        DBUtils.backup(host, username, password, backupPath, filename, db);

        InputStream is = new FileInputStream(new File(backupPath, filename));
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
