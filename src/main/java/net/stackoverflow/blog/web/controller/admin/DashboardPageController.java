package net.stackoverflow.blog.web.controller.admin;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.common.Response;
import net.stackoverflow.blog.pojo.dto.VisitDTO;
import net.stackoverflow.blog.pojo.po.VisitPO;
import net.stackoverflow.blog.service.VisitService;
import net.stackoverflow.blog.util.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * 后台管理系统仪表盘页
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping("/admin")
public class DashboardPageController {

    @Autowired
    private VisitService visitService;

    /**
     * 仪表盘页跳转
     *
     * @return 返回ModelAndView
     */
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ModelAndView dashboard() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/dashboard");
        return mv;
    }

    /**
     * 获取三十天内流量数据
     *
     * @return 返回Response对象
     */
    @RequestMapping(value = "/dashboard/visit/chart", method = RequestMethod.GET)
    @ResponseBody
    public Response flow() {
        Response response = new Response();
        List<String> dates = new ArrayList<>();
        List<Integer> visits = new ArrayList<>();
        Map<String, List> map = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, -29);

        for (int i = 0; i < 30; i++) {
            Date startDate = calendar.getTime();
            calendar.add(Calendar.DATE, 1);
            Date endDate = calendar.getTime();
            int visitCount = visitService.selectByDate(startDate, endDate).size();
            dates.add(DateUtils.formatDate(startDate));
            visits.add(visitCount);
        }

        map.put("dateList", dates);
        map.put("visitList", visits);
        response.setStatus(Response.SUCCESS);
        response.setMessage("流量记录获取成功");
        response.setData(map);
        return response;
    }

    /**
     * 获取访问记录接口
     *
     * @param page  分页参数
     * @param limit 每页数量
     * @return 返回Response对象
     */
    @RequestMapping(value = "/dashboard/visit/list", method = RequestMethod.GET)
    @ResponseBody
    public Response today(@RequestParam(value = "page") String page, @RequestParam(value = "limit") String limit) {
        Response response = new Response();

        Page pageParam = new Page(Integer.valueOf(page), Integer.valueOf(limit), null);
        List<VisitPO> visitPOs = visitService.selectByPage(pageParam);
        int count = visitService.selectByCondition(new HashMap<>()).size();

        List<VisitDTO> visitDTOs = new ArrayList<>();
        for (VisitPO visitPO : visitPOs) {
            VisitDTO visitDTO = new VisitDTO();
            BeanUtils.copyProperties(visitPO, visitDTO);
            visitDTOs.add(visitDTO);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        map.put("items", visitDTOs);
        response.setStatus(Response.SUCCESS);
        response.setMessage("查询成功");
        response.setData(map);
        return response;
    }

    /**
     * 获取访问量和访客量数据接口
     *
     * @return 返回Response对象
     */
    @RequestMapping(value = "/dashboard/visit/count", method = RequestMethod.GET)
    @ResponseBody
    public Response count() {
        Response response = new Response();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date endDate = calendar.getTime();

        int todayVisit = visitService.selectByDate(startDate, endDate).size();
        int totalVisit = visitService.selectByCondition(new HashMap<>()).size();

        Map<String, Integer> map = new HashMap<>();
        map.put("todayVisit", todayVisit);
        map.put("totalVisit", totalVisit);

        response.setStatus(Response.SUCCESS);
        response.setMessage("获取成功");
        response.setData(map);

        return response;
    }

}
