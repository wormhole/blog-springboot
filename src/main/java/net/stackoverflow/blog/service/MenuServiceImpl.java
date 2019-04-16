package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.MenuDao;
import net.stackoverflow.blog.pojo.po.MenuPO;
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
    @Transactional(rollbackFor = Exception.class)
    public List<MenuPO> selectByPage(Page page) {
        return dao.selectByPage(page);
    }

    /**
     * 条件查询
     *
     * @param searchMap 条件查询参数
     * @return 返回查询的结果集
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MenuPO> selectByCondition(Map<String, Object> searchMap) {
        return dao.selectByCondition(searchMap);
    }

    /**
     * 根据主键查询
     *
     * @param id 菜单主键
     * @return 返回菜单PO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MenuPO selectById(String id) {
        MenuPO menuPO = (MenuPO) RedisCacheUtils.get("menu:" + id);
        if (menuPO != null) {
            return menuPO;
        } else {
            menuPO = dao.selectById(id);
            if (menuPO != null) {
                RedisCacheUtils.set("menu:" + id, menuPO);
            }
            return menuPO;
        }
    }

    /**
     * 新增菜单
     *
     * @param menuPO 新增菜单PO
     * @return 返回新增的菜单PO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MenuPO insert(MenuPO menuPO) {
        dao.insert(menuPO);
        RedisCacheUtils.set("menu:" + menuPO.getId(), menuPO);
        return menuPO;
    }

    /**
     * 批量新增菜单
     *
     * @param menuPOs
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<MenuPO> menuPOs) {
        return dao.batchInsert(menuPOs);
    }

    /**
     * 根据主键删除
     *
     * @param id 被删除的菜单主键
     * @return 返回被删除的菜单PO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MenuPO deleteById(String id) {
        MenuPO menuPO = dao.selectById(id);
        dao.deleteById(id);
        RedisCacheUtils.del("menu:" + id);
        return menuPO;
    }

    /**
     * 批量删除菜单
     *
     * @param ids 被删除的菜单主键列表
     * @return 返回删除的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteById(List<String> ids) {
        int result = dao.batchDeleteById(ids);
        for (String id : ids) {
            RedisCacheUtils.del("menu:" + id);
        }
        return result;
    }

    /**
     * 更新菜单
     *
     * @param menuPO 被更新的菜单PO
     * @return 返回被更新的菜单PO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MenuPO update(MenuPO menuPO) {
        dao.update(menuPO);
        MenuPO newMenuPO = dao.selectById(menuPO.getId());
        RedisCacheUtils.set("menu:" + newMenuPO.getId(), newMenuPO);
        return newMenuPO;
    }

    /**
     * 批量更新菜单
     *
     * @param menuPOs 被更新的菜单列表
     * @return 返回更新的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<MenuPO> menuPOs) {
        int result = dao.batchUpdate(menuPOs);
        for (MenuPO menuPO : menuPOs) {
            RedisCacheUtils.del("menu:" + menuPO.getId());
        }
        return result;
    }

}
