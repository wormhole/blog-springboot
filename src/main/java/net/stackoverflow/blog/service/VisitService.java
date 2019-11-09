package net.stackoverflow.blog.service;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.Visit;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 访问量服务类接口
 *
 * @author 凉衫薄
 */
public interface VisitService {

    List<Visit> selectByPage(Page page);

    List<Visit> selectByCondition(Map<String, Object> searchMap);

    Visit selectById(String id);

    Visit insert(Visit visit);

    int batchInsert(List<Visit> visits);

    Visit delete(String id);

    int batchDelete(List<String> ids);

    Visit update(Visit visit);

    int batchUpdate(List<Visit> visits);

    List<Visit> selectByDate(Date startDate, Date endDate);

}
