package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.SettingDao;
import net.stackoverflow.blog.pojo.entity.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 设置服务实现类
 *
 * @author 凉衫薄
 */
@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingDao dao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Setting> selectByPage(Page page) {
        return dao.selectByPage(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Setting> selectByCondition(Map<String, Object> searchMap) {
        return dao.selectByCondition(searchMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Setting selectById(String id) {
        return dao.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Setting insert(Setting setting) {
        dao.insert(setting);
        return dao.selectById(setting.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<Setting> list) {
        return dao.batchInsert(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Setting deleteById(String id) {
        Setting setting = dao.selectById(id);
        dao.deleteById(id);
        return setting;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteById(List<String> list) {
        return dao.batchDeleteById(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Setting update(Setting setting) {
        dao.update(setting);
        return dao.selectById(setting.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<Setting> list) {
        return dao.batchUpdate(list);
    }

}
