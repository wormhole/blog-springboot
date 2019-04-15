package net.stackoverflow.blog;

import net.stackoverflow.blog.pojo.po.MenuPO;
import net.stackoverflow.blog.service.MenuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogApplicationTests {

    @Autowired
    private MenuService service;

    @Test
    public void contextLoads() {

        MenuPO menu = new MenuPO();
        menu.setName("主页");
        menu.setUrl("/");
        menu.setDeleteAble(1);
        menu.setDate(new Date());
        List<MenuPO> list = new ArrayList<>();
        list.add(menu);
        service.batchInsert(list);
        System.out.println();
    }

}
