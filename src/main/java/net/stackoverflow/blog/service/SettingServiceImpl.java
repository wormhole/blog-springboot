package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.SettingDao;
import net.stackoverflow.blog.pojo.entity.Setting;
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
    public List<Setting> selectByPage(Page page) {
        return dao.selectByPage(page);
    }

    /**
     * 条件查询
     *
     * @param searchMap 查询参数
     * @return 返回查询结果集
     */
    @Override
    public List<Setting> selectByCondition(Map<String, Object> searchMap) {
        return dao.selectByCondition(searchMap);
    }

    /**
     * 根据主键查询
     *
     * @param id 查询主键
     * @return 返回查询的设置实体对象
     */
    @Override
    public Setting selectById(String id) {
        Setting setting = (Setting) RedisCacheUtils.get("setting:" + id);
        if (setting != null) {
            return setting;
        } else {
            setting = dao.selectById(id);
            if (setting != null) {
                RedisCacheUtils.set("setting:" + id, setting, 1800);
            }
            return setting;
        }
    }

    /**
     * 新增设置
     *
     * @param setting 新增的设置实体对象
     * @return 返回新增后的实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Setting insert(Setting setting) {
        dao.insert(setting);
        RedisCacheUtils.set("setting:" + setting.getId(), setting, 1800);
        return setting;
    }

    /**
     * 批量新增
     *
     * @param settings 批量新增的设置列表
     * @return 返回新增的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<Setting> settings) {
        return dao.batchInsert(settings);
    }

    /**
     * 根据id删除
     *
     * @param id 被删除的设置主键
     * @return 返回被删除的设置
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Setting delete(String id) {
        Setting setting = dao.selectById(id);
        dao.delete(id);
        RedisCacheUtils.del("setting:" + id);
        return setting;
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<String> ids) {
        int result = dao.batchDelete(ids);
        for (String id : ids) {
            RedisCacheUtils.del("setting:" + id);
        }
        return result;
    }

    /**
     * 更新设置
     *
     * @param setting 被更新的设置实体对象
     * @return 返回更新后的设置实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Setting update(Setting setting) {
        dao.update(setting);
        List<Setting> settings = dao.selectByCondition(new HashMap<String, Object>(16) {{
            put("name", setting.getName());
        }});
        if (!CollectionUtils.isEmpty(settings)) {
            RedisCacheUtils.set("setting:" + settings.get(0).getId(), settings.get(0));
            return settings.get(0);
        } else {
            return null;
        }
    }

    /**
     * 批量更新设置
     *
     * @param settings 批量更新的设置实体对象
     * @return 返回更新的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<Setting> settings) {
        int result = dao.batchUpdate(settings);
        for (Setting setting : settings) {
            RedisCacheUtils.del("setting:" + setting.getId());
        }
        return result;
    }

}
