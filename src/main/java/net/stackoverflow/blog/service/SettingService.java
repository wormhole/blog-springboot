package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.Setting;

import java.util.List;
import java.util.Map;

/**
 * 设置服务类接口
 *
 * @author 凉衫薄
 */
public interface SettingService {

    List<Setting> selectByPage(Page page);

    List<Setting> selectByCondition(Map<String, Object> searchMap);

    Setting selectById(String id);

    Setting insert(Setting settingPO);

    int batchInsert(List<Setting> settingPOs);

    Setting delete(String id);

    int batchDelete(List<String> ids);

    Setting update(Setting settingPO);

    int batchUpdate(List<Setting> settingPOs);

}
