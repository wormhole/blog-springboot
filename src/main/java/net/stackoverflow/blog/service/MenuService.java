package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.po.MenuPO;

import java.util.List;
import java.util.Map;

/**
 * 菜单服务接口
 *
 * @author 凉衫薄
 */
public interface MenuService {

    List<MenuPO> selectByPage(Page page);

    List<MenuPO> selectByCondition(Map<String, Object> searchMap);

    MenuPO selectById(String id);

    MenuPO insert(MenuPO menu);

    int batchInsert(List<MenuPO> menus);

    MenuPO deleteById(String id);

    int batchDeleteById(List<String> ids);

    MenuPO update(MenuPO menu);

    int batchUpdate(List<MenuPO> menus);

}
