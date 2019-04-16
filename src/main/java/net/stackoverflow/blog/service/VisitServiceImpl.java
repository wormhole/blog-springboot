package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.VisitDao;
import net.stackoverflow.blog.pojo.po.VisitPO;
import net.stackoverflow.blog.util.RedisCacheUtils;
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

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @return 返回查询结果集
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<VisitPO> selectByPage(Page page) {
        return dao.selectByPage(page);
    }

    /**
     * 条件查询
     *
     * @param searchMap 条件参数
     * @return 返回条件查询结果集
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<VisitPO> selectByCondition(Map<String, Object> searchMap) {
        return dao.selectByCondition(searchMap);
    }

    /**
     * 根据主键查询
     *
     * @param id 查询主键
     * @return 返回访客PO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitPO selectById(String id) {
        VisitPO visitPO = (VisitPO) RedisCacheUtils.get("visit:" + id);
        if (visitPO != null) {
            return visitPO;
        } else {
            visitPO = dao.selectById(id);
            if (visitPO != null) {
                RedisCacheUtils.set("visit:" + id, visitPO);
            }
            return visitPO;
        }
    }

    /**
     * 新增访客
     *
     * @param visitPO 新增访客PO
     * @return 返回新增的访客PO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitPO insert(VisitPO visitPO) {
        dao.insert(visitPO);
        RedisCacheUtils.set("visit:" + visitPO.getId(), visitPO);
        return visitPO;
    }

    /**
     * 批量新增访客
     *
     * @param visitPOs 批量新增访客PO列表
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<VisitPO> visitPOs) {
        return dao.batchInsert(visitPOs);
    }

    /**
     * 根据id删除
     *
     * @param id 被删除的访客主键
     * @return 返回被删除的访客PO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitPO deleteById(String id) {
        VisitPO visitPO = dao.selectById(id);
        dao.deleteById(id);
        RedisCacheUtils.del("visit:" + id);
        return visitPO;
    }

    /**
     * 根据id批量删除
     *
     * @param ids 被删除的访客主键列表
     * @return 返回被删除的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteById(List<String> ids) {
        int result = dao.batchDeleteById(ids);
        for (String id : ids) {
            RedisCacheUtils.del("visit:" + id);
        }
        return result;
    }

    /**
     * 更新访客
     *
     * @param visitPO 被更新的访客PO
     * @return 返回更新后的访客PO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitPO update(VisitPO visitPO) {
        dao.update(visitPO);
        VisitPO newVisitPO = dao.selectById(visitPO.getId());
        RedisCacheUtils.set("visit:" + visitPO.getId(), newVisitPO);
        return newVisitPO;
    }

    /**
     * 批量更新访客
     *
     * @param visitPOs 批量更新的访客PO列表
     * @return 返回更新的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<VisitPO> visitPOs) {
        int result = dao.batchUpdate(visitPOs);
        for (VisitPO visitPO : visitPOs) {
            RedisCacheUtils.del("visit:" + visitPO.getId());
        }
        return result;
    }

    /**
     * 根据时间区间查询
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 返回查询的结果集
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<VisitPO> selectByDate(Date startDate, Date endDate) {
        return dao.selectByDate(startDate, endDate);
    }

}
