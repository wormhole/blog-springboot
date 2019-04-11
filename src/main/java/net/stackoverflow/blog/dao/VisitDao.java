package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.po.VisitPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 访问量DAO类
 *
 * @author 凉衫薄
 */
@Mapper
public interface VisitDao {

    List<VisitPO> selectByPage(Page page);

    List<VisitPO> selectByCondition(Map<String, Object> searchMap);

    VisitPO selectById(String id);

    int insert(VisitPO visit);

    int batchInsert(List<VisitPO> list);

    int deleteById(String id);

    int batchDeleteById(List<String> list);

    int update(VisitPO visit);

    int batchUpdate(List<VisitPO> list);

    List<VisitPO> selectByDate(Date startDate, Date endDate);

}
