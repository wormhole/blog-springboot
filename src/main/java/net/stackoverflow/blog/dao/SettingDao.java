package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.po.SettingPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 设置信息表DAO
 *
 * @author 凉衫薄
 */
@Mapper
public interface SettingDao {

    List<SettingPO> selectByPage(Page page);

    List<SettingPO> selectByCondition(Map<String, Object> searchMap);

    SettingPO selectById(String id);

    int insert(SettingPO setting);

    int batchInsert(List<SettingPO> list);

    int deleteById(String id);

    int batchDeleteById(List<String> list);

    int update(SettingPO setting);

    int batchUpdate(List<SettingPO> list);

}
