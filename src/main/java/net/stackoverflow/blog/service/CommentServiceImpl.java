package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.CommentDao;
import net.stackoverflow.blog.pojo.entity.Comment;
import net.stackoverflow.blog.util.RedisCacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 评论服务实现类
 *
 * @author 凉衫薄
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao dao;

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @return 返回分页查询的结果集
     */
    @Override
    public List<Comment> selectByPage(Page page) {
        return dao.selectByPage(page);
    }

    /**
     * 条件查询
     *
     * @param searchMap 条件查询参数
     * @return 返回查询的结果集
     */
    @Override
    public List<Comment> selectByCondition(Map<String, Object> searchMap) {
        return dao.selectByCondition(searchMap);
    }

    /**
     * 根据id查询评论
     *
     * @param id 评论id
     * @return 返回查询的评论实体对象
     */
    @Override
    public Comment selectById(String id) {
        Comment comment = (Comment) RedisCacheUtils.get("comment:" + id);
        if (comment != null) {
            return comment;
        } else {
            comment = dao.selectById(id);
            if (comment != null) {
                RedisCacheUtils.set("comment:" + id, comment, 1800);
            }
            return comment;
        }
    }

    /**
     * 根据主键批量查询评论
     *
     * @param ids 主键列表
     * @return 结果集
     */
    @Override
    public List<Comment> selectByIds(List<String> ids) {
        return dao.selectByIds(ids);
    }

    /**
     * 新增评论
     *
     * @param comment 新增的评论实体对象
     * @return 返回新增的评论实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment insert(Comment comment) {
        dao.insert(comment);
        RedisCacheUtils.set("comment:" + comment.getId(), comment, 1800);
        return dao.selectById(comment.getId());
    }

    /**
     * 批量新增
     *
     * @param comments 新增的评论实体对象
     * @return 返回新增的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<Comment> comments) {
        return dao.batchInsert(comments);
    }

    /**
     * 根据ID删除评论
     *
     * @param id 被删除的评论ID
     * @return 返回被删除的评论实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment delete(String id) {
        Comment comment = dao.selectById(id);
        dao.delete(id);
        RedisCacheUtils.del("comment:" + id);
        return comment;
    }

    /**
     * 根据id批量删除评论
     *
     * @param ids 评论id列表
     * @return 返回删除的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<String> ids) {
        int result = dao.batchDelete(ids);
        for (String id : ids) {
            RedisCacheUtils.del("comment:" + id);
        }
        return result;
    }

    /**
     * 更新评论
     *
     * @param comment 更新的评论实体对象
     * @return 返回被更新的评论实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment update(Comment comment) {
        dao.update(comment);
        Comment newComment = dao.selectById(comment.getId());
        RedisCacheUtils.set("comment:" + newComment.getId(), newComment, 1800);
        return newComment;
    }

    /**
     * 批量更新
     *
     * @param comments 被更新的评论列表
     * @return 返回更新的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<Comment> comments) {
        int result = dao.batchUpdate(comments);
        for (Comment commentPO : comments) {
            RedisCacheUtils.del("comment:" + commentPO.getId());
        }
        return result;
    }

}
