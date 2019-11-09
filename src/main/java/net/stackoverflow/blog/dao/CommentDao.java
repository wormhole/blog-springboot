package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.Comment;
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

    List<Comment> selectByPage(Page page);

    List<Comment> selectByCondition(Map<String, Object> searchMap);

    Comment selectById(String id);

    int insert(Comment comment);

    int batchInsert(List<Comment> list);

    int delete(String id);

    int batchDelete(List<String> list);

    int update(Comment comment);

    int batchUpdate(List<Comment> list);

}
