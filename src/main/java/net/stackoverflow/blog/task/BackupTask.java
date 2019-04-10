package net.stackoverflow.blog.task;

import net.stackoverflow.blog.util.DBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

/**
 * 数据库备份任务
 *
 * @author 凉衫薄
 */
@Component
public class BackupTask {

    @Autowired
    private ServletContext servletContext;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.host}")
    private String host;
    @Value("${spring.datasource.db}")
    private String db;

    @Scheduled(initialDelay = 10000, fixedRate = 600000)
    @Async
    public void backup() {
        String backupPath = servletContext.getRealPath("WEB-INF/backup");
        System.out.println(backupPath);
        DBUtils.backup(host, username, password, backupPath, "blog.sql", db);
    }
}
