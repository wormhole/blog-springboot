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

    Setting insert(Setting setting);

    int batchInsert(List<Setting> list);

    Setting deleteById(String id);

    int batchDeleteById(List<String> list);

    Setting update(Setting setting);

    int batchUpdate(List<Setting> list);

}
