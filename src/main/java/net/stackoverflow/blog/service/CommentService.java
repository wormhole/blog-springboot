package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.po.CommentPO;

import java.util.List;
import java.util.Map;

/**
 * 评论服务接口
 *
 * @author 凉衫薄
 */
public interface CommentService {

    List<CommentPO> selectByPage(Page page);

    List<CommentPO> selectByCondition(Map<String, Object> searchMap);

    CommentPO selectById(String id);

    CommentPO insert(CommentPO commentPO);

    int batchInsert(List<CommentPO> commentPOs);

    CommentPO deleteById(String id);

    int batchDeleteById(List<String> ids);

    CommentPO update(CommentPO commentPO);

    int batchUpdate(List<CommentPO> commentPOs);

}
