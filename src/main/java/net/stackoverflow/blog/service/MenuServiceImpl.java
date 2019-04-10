package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.MenuDao;
import net.stackoverflow.blog.pojo.entity.Menu;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Menu> selectByPage(Page page) {
        return dao.selectByPage(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Menu> selectByCondition(Map<String, Object> searchMap) {
        return dao.selectByCondition(searchMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Menu selectById(String id) {
        return dao.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Menu insert(Menu menu) {
        dao.insert(menu);
        return dao.selectById(menu.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<Menu> list) {
        return dao.batchInsert(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Menu deleteById(String id) {
        Menu menu = dao.selectById(id);
        dao.deleteById(id);
        return menu;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteById(List<String> list) {
        return dao.batchDeleteById(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Menu update(Menu menu) {
        dao.update(menu);
        return dao.selectById(menu.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<Menu> list) {
        return dao.batchUpdate(list);
    }

}
