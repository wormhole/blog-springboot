package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.CommentDao;
import net.stackoverflow.blog.pojo.entity.Comment;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Comment> selectByPage(Page page) {
        return dao.selectByPage(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Comment> selectByCondition(Map<String, Object> searchMap) {
        return dao.selectByCondition(searchMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment selectById(String id) {
        return dao.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment insert(Comment comment) {
        dao.insert(comment);
        return dao.selectById(comment.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<Comment> list) {
        return dao.batchInsert(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment deleteById(String id) {
        Comment comment = dao.selectById(id);
        dao.deleteById(id);
        return comment;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteById(List<String> list) {
        return dao.batchDeleteById(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment update(Comment comment) {
        dao.update(comment);
        return dao.selectById(comment.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<Comment> list) {
        return dao.batchUpdate(list);
    }

}
