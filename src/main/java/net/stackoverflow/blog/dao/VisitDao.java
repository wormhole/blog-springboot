package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.Visit;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 访问量DAO类
 *
 * @author 凉衫薄
 */
@Repository
public interface VisitDao {

    List<Visit> selectByPage(Page page);

    List<Visit> selectByCondition(Map<String, Object> searchMap);

    Visit selectById(String id);

    int insert(Visit visit);

    int batchInsert(List<Visit> list);

    int deleteById(String id);

    int batchDeleteById(List<String> list);

    int update(Visit visit);

    int batchUpdate(List<Visit> list);

    List<Visit> selectByDate(Date startDate, Date endDate);

}
