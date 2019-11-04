package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.User;
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

    List<User> selectByPage(Page page);

    List<User> selectByCondition(Map<String, Object> searchMap);

    User selectById(String id);

    int insert(User user);

    int batchInsert(List<User> users);

    int deleteById(String id);

    int batchDeleteById(List<String> ids);

    int update(User user);

    int batchUpdate(List<User> users);
}
