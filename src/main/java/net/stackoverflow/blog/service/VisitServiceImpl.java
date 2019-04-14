package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.VisitDao;
import net.stackoverflow.blog.pojo.po.VisitPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 访问量服务实现类
 *
 * @author 凉衫薄
 */
@Service
public class VisitServiceImpl implements VisitService {

    @Autowired
    private VisitDao dao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<VisitPO> selectByPage(Page page) {
        return dao.selectByPage(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<VisitPO> selectByCondition(Map<String, Object> searchMap) {
        return dao.selectByCondition(searchMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitPO selectById(String id) {
        return dao.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitPO insert(VisitPO visit) {
        dao.insert(visit);
        return dao.selectById(visit.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<VisitPO> visits) {
        return dao.batchInsert(visits);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitPO deleteById(String id) {
        return dao.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteById(List<String> ids) {
        return dao.batchDeleteById(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitPO update(VisitPO visit) {
        dao.update(visit);
        return dao.selectById(visit.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<VisitPO> visits) {
        return dao.batchUpdate(visits);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<VisitPO> selectByDate(Date startDate, Date endDate) {
        return dao.selectByDate(startDate, endDate);
    }

}
