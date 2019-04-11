package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.po.CommentPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 评论表DAO
 *
 * @author 凉衫薄
 */
@Mapper
public interface CommentDao {

    List<CommentPO> selectByPage(Page page);

    List<CommentPO> selectByCondition(Map<String, Object> searchMap);

    CommentPO selectById(String id);

    int insert(CommentPO comment);

    int batchInsert(List<CommentPO> list);

    int deleteById(String id);

    int batchDeleteById(List<String> list);

    int update(CommentPO comment);

    int batchUpdate(List<CommentPO> list);

}
