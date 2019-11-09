package net.stackoverflow.blog;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

@SpringBootApplication
@MapperScan(basePackages = {"net.stackoverflow.blog.dao.*"}, sqlSessionFactoryRef = "sqlSessionFactory")
@EnableCaching
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
public class BlogApplication {

    public static void main(String[] args) {
        initDataBase();
        SpringApplication.run(BlogApplication.class, args);
    }

    /**
     * 初始化数据库
     */
    private static void initDataBase() {
        String[] scripts = new String[]{"sql/blog.sql", "sql/setting.sql", "sql/menu.sql", "sql/user.sql", "sql/role.sql", "sql/permission.sql",
                "sql/role_permission.sql", "sql/user_role.sql", "sql/category.sql", "sql/article.sql", "sql/comment.sql", "sql/visit.sql",
                "sql/init.sql"};
        try {
            Properties props = Resources.getResourceAsProperties("application.properties");
            String server = props.getProperty("spring.datasource.server");
            String username = props.getProperty("spring.datasource.username");
            String password = props.getProperty("spring.datasource.password");
            String driver = props.getProperty("spring.datasource.driver-class-name");
            String isExistSQL = "SELECT count(SCHEMA_NAME) as COUNT FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME='blog'";

            Class.forName(driver);
            Connection conn = DriverManager.getConnection(server, username, password);
            PreparedStatement ps = conn.prepareStatement(isExistSQL);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt("COUNT") == 0) {
                ScriptRunner runner = new ScriptRunner(conn);
                runner.setErrorLogWriter(null);
                runner.setLogWriter(null);
                for (int i = 0; i < scripts.length; i++) {
                    runner.runScript(new InputStreamReader(Resources.getResourceAsStream(scripts[i]), "UTF-8"));
                }
            }
            ps.close();
            conn.close();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
