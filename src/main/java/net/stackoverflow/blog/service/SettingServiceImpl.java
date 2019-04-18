package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.SettingDao;
import net.stackoverflow.blog.pojo.po.SettingPO;
import net.stackoverflow.blog.util.CollectionUtils;
import net.stackoverflow.blog.util.RedisCacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @return 返回查询结果集
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SettingPO> selectByPage(Page page) {
        return dao.selectByPage(page);
    }

    /**
     * 条件查询
     *
     * @param searchMap 查询参数
     * @return 返回查询结果集
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SettingPO> selectByCondition(Map<String, Object> searchMap) {
        return dao.selectByCondition(searchMap);
    }

    /**
     * 根据主键查询
     *
     * @param id 查询主键
     * @return 返回查询的设置PO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SettingPO selectById(String id) {
        SettingPO settingPO = (SettingPO) RedisCacheUtils.get("setting:" + id);
        if (settingPO != null) {
            return settingPO;
        } else {
            settingPO = dao.selectById(id);
            if (settingPO != null) {
                RedisCacheUtils.set("setting:" + id, settingPO);
            }
            return settingPO;
        }
    }

    /**
     * 新增设置
     *
     * @param settingPO 新增的设置PO
     * @return 返回新增后的PO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SettingPO insert(SettingPO settingPO) {
        dao.insert(settingPO);
        RedisCacheUtils.set("setting:" + settingPO.getId(), settingPO);
        return settingPO;
    }

    /**
     * 批量新增
     *
     * @param settingPOs 批量新增的设置PO列表
     * @return 返回新增的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<SettingPO> settingPOs) {
        return dao.batchInsert(settingPOs);
    }

    /**
     * 根据id删除
     *
     * @param id 被删除的设置主键
     * @return 返回被删除的设置PO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SettingPO deleteById(String id) {
        SettingPO settingPO = dao.selectById(id);
        dao.deleteById(id);
        RedisCacheUtils.del("setting:" + id);
        return settingPO;
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteById(List<String> ids) {
        int result = dao.batchDeleteById(ids);
        for (String id : ids) {
            RedisCacheUtils.del("setting:" + id);
        }
        return result;
    }

    /**
     * 更新设置
     *
     * @param settingPO 被更新的设置PO
     * @return 返回更新后的设置PO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SettingPO update(SettingPO settingPO) {
        dao.update(settingPO);
        List<SettingPO> settingPOs = dao.selectByCondition(new HashMap<String, Object>() {{
            put("name", settingPO.getName());
        }});
        if (!CollectionUtils.isEmpty(settingPOs)) {
            RedisCacheUtils.set("setting:" + settingPOs.get(0).getId(), settingPOs.get(0));
            return settingPOs.get(0);
        } else {
            return null;
        }
    }

    /**
     * 批量更新设置
     *
     * @param settingPOs 批量更新的设置PO
     * @return 返回更新的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<SettingPO> settingPOs) {
        int result = dao.batchUpdate(settingPOs);
        for (SettingPO settingPO : settingPOs) {
            List<SettingPO> settingPOList = dao.selectByCondition(new HashMap<String, Object>() {{
                put("name", settingPO.getName());
            }});
            if (!CollectionUtils.isEmpty(settingPOList)) {
                RedisCacheUtils.set("setting:" + settingPOList.get(0).getId(), settingPOList.get(0));
            }
        }
        return result;
    }

}
