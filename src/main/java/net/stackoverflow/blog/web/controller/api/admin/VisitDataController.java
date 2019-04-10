package net.stackoverflow.blog.web.controller.api.admin;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.common.Response;
import net.stackoverflow.blog.pojo.dto.VisitDTO;
import net.stackoverflow.blog.pojo.entity.Visit;
import net.stackoverflow.blog.service.VisitService;
import net.stackoverflow.blog.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 浏览量数据获取接口Controller
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping("/api/admin")
public class VisitDataController {

    @Autowired
    private VisitService visitService;

    /**
     * 获取三十天内的流量记录接口
     * /api/admin/visit/chart
     * 方法 GET
     *
     * @return
     */
    @RequestMapping(value = "/visit/chart", method = RequestMethod.GET)
    public Response flow() {
        Response response = new Response();
        List<String> dates = new ArrayList<>();
        List<Integer> visits = new ArrayList<>();
        Map<String, List> map = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
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
     * /api/admin/visit/list
     * 方法 GET
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/visit/list", method = RequestMethod.GET)
    @ResponseBody
    public Response today(@RequestParam(value = "page") String page, @RequestParam(value = "limit") String limit) {
        Response response = new Response();

        Page page1 = new Page(Integer.valueOf(page), Integer.valueOf(limit), null);

        List<Visit> visits = visitService.selectByPage(page1);
        int count = visitService.selectByCondition(new HashMap<>()).size();

        List<VisitDTO> dtos = new ArrayList<>();

        for (Visit visit : visits) {
            VisitDTO dto = new VisitDTO();
            dto.setIp(visit.getIp());
            dto.setUrl(visit.getUrl());
            dto.setStatus(visit.getStatus());
            dto.setAgent(visit.getAgent());
            dto.setReferer(visit.getReferer());
            dto.setDate(visit.getDate());
            dtos.add(dto);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        map.put("items", dtos);
        response.setStatus(Response.SUCCESS);
        response.setMessage("查询成功");
        response.setData(map);
        return response;
    }

    /**
     * 获取访问量和访客量数据接口
     * /api/admin/visit/count
     * 方法 GET
     *
     * @return
     */
    @RequestMapping(value = "/visit/count", method = RequestMethod.GET)
    @ResponseBody
    public Response count() {
        Response response = new Response();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
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
