package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.SettingDao;
import net.stackoverflow.blog.pojo.po.SettingPO;
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
    public List<SettingPO> selectByPage(Page page) {
        return dao.selectByPage(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SettingPO> selectByCondition(Map<String, Object> searchMap) {
        return dao.selectByCondition(searchMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SettingPO selectById(String id) {
        return dao.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SettingPO insert(SettingPO setting) {
        dao.insert(setting);
        return dao.selectById(setting.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<SettingPO> settings) {
        return dao.batchInsert(settings);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SettingPO deleteById(String id) {
        SettingPO setting = dao.selectById(id);
        dao.deleteById(id);
        return setting;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteById(List<String> ids) {
        return dao.batchDeleteById(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SettingPO update(SettingPO setting) {
        dao.update(setting);
        return dao.selectById(setting.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<SettingPO> settings) {
        return dao.batchUpdate(settings);
    }

}
