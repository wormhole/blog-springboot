package net.stackoverflow.blog.web.listener;

import net.stackoverflow.blog.pojo.entity.Menu;
import org.apache.ibatis.io.Resources;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * 初始化监听器
 *
 * @author 凉衫薄
 */
public class InitListener implements ServletContextListener {

    /**
     * 初始化数据库及表
     *
     * @param event
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        initContext(event);
    }

    /**
     * 初始化上下文
     *
     * @param event
     */
    private void initContext(ServletContextEvent event) {
        ServletContext application = event.getServletContext();
        try {
            Properties props = Resources.getResourceAsProperties("application.properties");
            String url = props.getProperty("spring.datasource.url");
            String username = props.getProperty("spring.datasource.username");
            String password = props.getProperty("spring.datasource.password");
            String driver = props.getProperty("spring.datasource.driver-class-name");

            String sql1 = "select * from setting";
            String sql2 = "select * from menu order by date";

            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql1);
            ResultSet rs = ps.executeQuery();

            Map<String, Object> map = new HashMap<>();
            while (rs.next()) {
                map.put(rs.getString("name"), rs.getObject("value"));
            }

            List<Menu> list = new ArrayList<>();
            ps = conn.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {
                Menu menu = new Menu();
                menu.setId(rs.getString("id"));
                menu.setName(rs.getString("name"));
                menu.setUrl(rs.getString("url"));
                list.add(menu);
            }

            ps.close();
            conn.close();
            application.setAttribute("setting", map);
            application.setAttribute("menu", list);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
