package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.Menu;

import java.util.List;
import java.util.Map;

/**
 * 菜单服务接口
 *
 * @author 凉衫薄
 */
public interface MenuService {

    List<Menu> selectByPage(Page page);

    List<Menu> selectByCondition(Map<String, Object> searchMap);

    Menu selectById(String id);

    List<Menu> selectByIds(List<String> ids);

    Menu insert(Menu menu);

    int batchInsert(List<Menu> menus);

    Menu delete(String id);

    int batchDelete(List<String> ids);

    Menu update(Menu menu);

    int batchUpdate(List<Menu> menus);

}
