package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.Setting;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 设置信息表DAO
 *
 * @author 凉衫薄
 */
@Repository
public interface SettingDao {

    List<Setting> selectByPage(Page page);

    List<Setting> selectByCondition(Map<String, Object> searchMap);

    Setting selectById(String id);

    int insert(Setting setting);

    int batchInsert(List<Setting> list);

    int delete(String id);

    int batchDelete(List<String> list);

    int update(Setting setting);

    int batchUpdate(List<Setting> list);

}
