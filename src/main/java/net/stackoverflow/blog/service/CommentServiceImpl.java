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
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
    public List<Comment> selectByCondition(Map<String, Object> searchMap) {
        return dao.selectByCondition(searchMap);
    }

    /**
     * 根据id查询评论
     *
     * @param id 评论id
     * @return 返回查询的评论PO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment selectById(String id) {
        Comment commentPO = (Comment) RedisCacheUtils.get("comment:" + id);
        if (commentPO != null) {
            return commentPO;
        } else {
            commentPO = dao.selectById(id);
            if (commentPO != null) {
                RedisCacheUtils.set("comment:" + id, commentPO);
            }
            return commentPO;
        }
    }

    /**
     * 新增评论
     *
     * @param commentPO 新增的评论PO
     * @return 返回新增的评论PO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment insert(Comment commentPO) {
        dao.insert(commentPO);
        RedisCacheUtils.set("comment:" + commentPO.getId(), commentPO);
        return dao.selectById(commentPO.getId());
    }

    /**
     * 批量新增
     *
     * @param commentPOs 新增的评论PO
     * @return 返回新增的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<Comment> commentPOs) {
        return dao.batchInsert(commentPOs);
    }

    /**
     * 根据ID删除评论
     *
     * @param id 被删除的评论ID
     * @return 返回被删除的评论PO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment deleteById(String id) {
        Comment commentPO = dao.selectById(id);
        dao.deleteById(id);
        RedisCacheUtils.del("comment:" + id);
        return commentPO;
    }

    /**
     * 根据id批量删除评论
     *
     * @param ids 评论id列表
     * @return 返回删除的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteById(List<String> ids) {
        int result = dao.batchDeleteById(ids);
        for (String id : ids) {
            RedisCacheUtils.del("comment:" + id);
        }
        return result;
    }

    /**
     * 更新评论
     *
     * @param commentPO 更新的评论PO
     * @return 返回被更新的评论PO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment update(Comment commentPO) {
        dao.update(commentPO);
        Comment newCommentPO = dao.selectById(commentPO.getId());
        RedisCacheUtils.set("comment:" + newCommentPO.getId(), newCommentPO);
        return newCommentPO;
    }

    /**
     * 批量更新
     *
     * @param commentPOs 被更新的评论列表
     * @return 返回更新的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<Comment> commentPOs) {
        int result = dao.batchUpdate(commentPOs);
        for (Comment commentPO : commentPOs) {
            RedisCacheUtils.del("comment:" + commentPO.getId());
        }
        return result;
    }

}
