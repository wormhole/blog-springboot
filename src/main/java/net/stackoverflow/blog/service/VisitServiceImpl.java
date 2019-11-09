package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.dao.VisitDao;
import net.stackoverflow.blog.pojo.entity.Visit;
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
    public List<Visit> selectByPage(Page page) {
        return dao.selectByPage(page);
    }

    /**
     * 条件查询
     *
     * @param searchMap 条件参数
     * @return 返回条件查询结果集
     */
    @Override
    public List<Visit> selectByCondition(Map<String, Object> searchMap) {
        return dao.selectByCondition(searchMap);
    }

    /**
     * 根据主键查询
     *
     * @param id 查询主键
     * @return 返回访客实体对象
     */
    @Override
    public Visit selectById(String id) {
        Visit visit = (Visit) RedisCacheUtils.get("visit:" + id);
        if (visit != null) {
            return visit;
        } else {
            visit = dao.selectById(id);
            if (visit != null) {
                RedisCacheUtils.set("visit:" + id, visit, 1800);
            }
            return visit;
        }
    }

    /**
     * 新增访客
     *
     * @param visit 新增访客实体对象
     * @return 返回新增的访客实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Visit insert(Visit visit) {
        dao.insert(visit);
        RedisCacheUtils.set("visit:" + visit.getId(), visit, 1800);
        return visit;
    }

    /**
     * 批量新增访客
     *
     * @param visits 批量新增访客列表
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<Visit> visits) {
        return dao.batchInsert(visits);
    }

    /**
     * 根据id删除
     *
     * @param id 被删除的访客主键
     * @return 返回被删除的访客实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Visit delete(String id) {
        Visit visit = dao.selectById(id);
        dao.delete(id);
        RedisCacheUtils.del("visit:" + id);
        return visit;
    }

    /**
     * 根据id批量删除
     *
     * @param ids 被删除的访客主键列表
     * @return 返回被删除的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<String> ids) {
        int result = dao.batchDelete(ids);
        for (String id : ids) {
            RedisCacheUtils.del("visit:" + id);
        }
        return result;
    }

    /**
     * 更新访客
     *
     * @param visit 被更新的访客实体对象
     * @return 返回更新后的访客实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Visit update(Visit visit) {
        dao.update(visit);
        Visit newVisit = dao.selectById(visit.getId());
        RedisCacheUtils.set("visit:" + visit.getId(), newVisit, 1800);
        return newVisit;
    }

    /**
     * 批量更新访客
     *
     * @param visits 批量更新的访客列表
     * @return 返回更新的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<Visit> visits) {
        int result = dao.batchUpdate(visits);
        for (Visit visit : visits) {
            RedisCacheUtils.del("visit:" + visit.getId());
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
    public List<Visit> selectByDate(Date startDate, Date endDate) {
        return dao.selectByDate(startDate, endDate);
    }

}
