package net.stackoverflow.blog.task;

import net.stackoverflow.blog.util.DBUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 数据库备份任务
 *
 * @author 凉衫薄
 */
@Component
public class BackupTask {

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

    @Scheduled(initialDelay = 10000, fixedRate = 600000)
    @Async
    public void backup() {
        DBUtils.backup(host, username, password, path, "blog.sql", db);
    }
}
