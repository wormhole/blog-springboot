package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.po.RolePO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 角色Mapper接口
 *
 * @author 凉衫薄
 */
@Mapper
public interface RoleDao {

    List<RolePO> selectByPage(Page page);

    List<RolePO> selectByCondition(Map<String, Object> searchMap);

    RolePO selectById(String id);

    int insert(RolePO role);

    int batchInsert(List<RolePO> roles);

    int deleteById(String id);

    int batchDeleteById(List<String> ids);

    int update(RolePO role);

    int batchUpdate(List<RolePO> roles);

}
