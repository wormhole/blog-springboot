package net.stackoverflow.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.stackoverflow.blog.common.Page;
import xyz.stackoverflow.blog.dao.VisitorDao;
import xyz.stackoverflow.blog.pojo.po.VisitorPO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 访客服务实现类
 *
 * @author 凉衫薄
 */
@Service
public class VisitorServiceImpl implements VisitorService {

    @Autowired
    private VisitorDao dao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<VisitorPO> selectByPage(Page page) {
        return dao.selectByPage(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<VisitorPO> selectByCondition(Map<String, Object> searchMap) {
        return dao.selectByCondition(searchMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitorPO selectById(String id) {
        return dao.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitorPO insert(VisitorPO visitor) {
        dao.insert(visitor);
        return dao.selectById(visitor.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<VisitorPO> list) {
        return dao.batchInsert(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitorPO deleteById(String id) {
        VisitorPO visitor = dao.selectById(id);
        dao.deleteById(id);
        return visitor;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteById(List<String> list) {
        return dao.batchDeleteById(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitorPO update(VisitorPO visitor) {
        dao.update(visitor);
        return dao.selectById(visitor.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<VisitorPO> list) {
        return dao.batchUpdate(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<VisitorPO> selectByDate(Date startDate, Date endDate) {
        return dao.selectByDate(startDate, endDate);
    }

}
