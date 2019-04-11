package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.po.UserPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 用户Mapper接口
 *
 * @author 凉衫薄
 */
@Mapper
public interface UserDao {

    List<UserPO> selectByPage(Page page);

    List<UserPO> selectByCondition(Map<String, Object> searchMap);

    UserPO selectById(String id);

    int insert(UserPO user);

    int batchInsert(List<UserPO> users);

    int deleteById(String id);

    int batchDeleteById(List<String> ids);

    int update(UserPO user);

    int batchUpdate(List<UserPO> users);
}
