package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.MenuDao;
import net.stackoverflow.blog.pojo.entity.Menu;
import net.stackoverflow.blog.util.RedisCacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 菜单服务实现类
 *
 * @author 凉衫薄
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao dao;

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @return 返回查询的结果集
     */
    @Override
    public List<Menu> selectByPage(Page page) {
        return dao.selectByPage(page);
    }

    /**
     * 条件查询
     *
     * @param searchMap 条件查询参数
     * @return 返回查询的结果集
     */
    @Override
    public List<Menu> selectByCondition(Map<String, Object> searchMap) {
        return dao.selectByCondition(searchMap);
    }

    /**
     * 根据主键查询
     *
     * @param id 菜单主键
     * @return 返回菜单实体对象
     */
    @Override
    public Menu selectById(String id) {
        Menu menu = (Menu) RedisCacheUtils.get("menu:" + id);
        if (menu != null) {
            return menu;
        } else {
            menu = dao.selectById(id);
            if (menu != null) {
                RedisCacheUtils.set("menu:" + id, menu, 1800);
            }
            return menu;
        }
    }

    /**
     * 新增菜单
     *
     * @param menu 新增菜单实体对象
     * @return 返回新增的菜单实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Menu insert(Menu menu) {
        dao.insert(menu);
        RedisCacheUtils.set("menu:" + menu.getId(), menu, 1800);
        return menu;
    }

    /**
     * 批量新增菜单
     *
     * @param menus 新增菜单列表
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<Menu> menus) {
        return dao.batchInsert(menus);
    }

    /**
     * 根据主键删除
     *
     * @param id 被删除的菜单主键
     * @return 返回被删除的菜单实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Menu delete(String id) {
        Menu menu = dao.selectById(id);
        dao.delete(id);
        RedisCacheUtils.del("menu:" + id);
        return menu;
    }

    /**
     * 批量删除菜单
     *
     * @param ids 被删除的菜单主键列表
     * @return 返回删除的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<String> ids) {
        int result = dao.batchDelete(ids);
        for (String id : ids) {
            RedisCacheUtils.del("menu:" + id);
        }
        return result;
    }

    /**
     * 更新菜单
     *
     * @param menu 被更新的菜单实体对象
     * @return 返回被更新的菜单实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Menu update(Menu menu) {
        dao.update(menu);
        Menu newMenu = dao.selectById(menu.getId());
        RedisCacheUtils.set("menu:" + newMenu.getId(), newMenu, 1800);
        return newMenu;
    }

    /**
     * 批量更新菜单
     *
     * @param menus 被更新的菜单列表
     * @return 返回更新的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<Menu> menus) {
        int result = dao.batchUpdate(menus);
        for (Menu menu : menus) {
            RedisCacheUtils.del("menu:" + menu.getId());
        }
        return result;
    }

}
