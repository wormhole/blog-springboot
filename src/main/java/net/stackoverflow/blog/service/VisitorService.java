package net.stackoverflow.blog.service;

import xyz.stackoverflow.blog.common.Page;
import xyz.stackoverflow.blog.pojo.po.VisitorPO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 访客服务类接口
 *
 * @author 凉衫薄
 */
public interface VisitorService {

    List<VisitorPO> selectByPage(Page page);

    List<VisitorPO> selectByCondition(Map<String, Object> searchMap);

    VisitorPO selectById(String id);

    VisitorPO insert(VisitorPO visitor);

    int batchInsert(List<VisitorPO> list);

    VisitorPO deleteById(String id);

    int batchDeleteById(List<String> list);

    VisitorPO update(VisitorPO visitor);

    int batchUpdate(List<VisitorPO> list);

    List<VisitorPO> selectByDate(Date startDate, Date endDate);
}
