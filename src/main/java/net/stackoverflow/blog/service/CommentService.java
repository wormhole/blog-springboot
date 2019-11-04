package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.Comment;

import java.util.List;
import java.util.Map;

/**
 * 评论服务接口
 *
 * @author 凉衫薄
 */
public interface CommentService {

    List<Comment> selectByPage(Page page);

    List<Comment> selectByCondition(Map<String, Object> searchMap);

    Comment selectById(String id);

    Comment insert(Comment commentPO);

    int batchInsert(List<Comment> commentPOs);

    Comment deleteById(String id);

    int batchDeleteById(List<String> ids);

    Comment update(Comment commentPO);

    int batchUpdate(List<Comment> commentPOs);

}
