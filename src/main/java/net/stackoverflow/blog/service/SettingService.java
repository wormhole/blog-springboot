package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.po.SettingPO;

import java.util.List;
import java.util.Map;

/**
 * 设置服务类接口
 *
 * @author 凉衫薄
 */
public interface SettingService {

    List<SettingPO> selectByPage(Page page);

    List<SettingPO> selectByCondition(Map<String, Object> searchMap);

    SettingPO selectById(String id);

    SettingPO insert(SettingPO settingPO);

    int batchInsert(List<SettingPO> settingPOs);

    SettingPO deleteById(String id);

    int batchDeleteById(List<String> ids);

    SettingPO update(SettingPO settingPO);

    int batchUpdate(List<SettingPO> settingPOs);

}
