package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.po.PermissionPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 权限Mapper接口
 *
 * @author 凉衫薄
 */
@Mapper
public interface PermissionDao {

    List<PermissionPO> selectByPage(Page page);

    List<PermissionPO> selectByCondition(Map<String, Object> searchMap);

    PermissionPO selectById(String id);

    int insert(PermissionPO permission);

    int batchInsert(List<PermissionPO> permissions);

    int deleteById(String id);

    int batchDeleteById(List<String> ids);

    int update(PermissionPO permission);

    int batchUpdate(List<PermissionPO> permissions);

}
