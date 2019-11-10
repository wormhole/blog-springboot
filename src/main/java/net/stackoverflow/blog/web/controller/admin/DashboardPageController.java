package net.stackoverflow.blog.web.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.common.Result;
import net.stackoverflow.blog.pojo.entity.Visit;
import net.stackoverflow.blog.pojo.vo.VisitVO;
import net.stackoverflow.blog.service.VisitService;
import net.stackoverflow.blog.util.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * 后台管理系统仪表盘页Controller
 *
 * @author 凉衫薄
 */
@Api(value = "仪表盘接口")
@Controller
@RequestMapping("/admin/dashboard")
public class DashboardPageController {

    @Autowired
    private VisitService visitService;

    /**
     * 仪表盘页跳转
     *
     * @return 返回ModelAndView
     */
    @ApiOperation(value = "仪表盘跳转")
    @RequestMapping(method = RequestMethod.GET)
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
    @ApiOperation(value = "获取三十天内流量数据", response = Result.class)
    @RequestMapping(value = "/visit_chart", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity flow() {

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

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setMessage("流量记录获取成功");
        result.setData(map);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 获取访问记录接口
     *
     * @param page  当前页码
     * @param limit 每页数量
     * @return
     */
    @ApiOperation(value = "获取访问记录", response = Result.class)
    @RequestMapping(value = "/visit_list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity today(@ApiParam(name = "page", value = "当前页码") @RequestParam(value = "page") String page,
                                @ApiParam(name = "limit", value = "每页数量") @RequestParam(value = "limit") String limit) {

        Page pageParam = new Page(Integer.valueOf(page), Integer.valueOf(limit), null);
        List<Visit> visits = visitService.selectByPage(pageParam);
        int count = visitService.selectByCondition(new HashMap<>(16)).size();

        List<VisitVO> visitVOs = new ArrayList<>();
        for (Visit visit : visits) {
            VisitVO visitVO = new VisitVO();
            BeanUtils.copyProperties(visit, visitVO);
            visitVOs.add(visitVO);
        }

        Map<String, Object> map = new HashMap<>(16);
        map.put("count", count);
        map.put("items", visitVOs);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setMessage("查询成功");
        result.setData(map);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 获取访问量和访客量数据接口
     *
     * @return
     */
    @ApiOperation(value = "获取访问量接口", response = Result.class)
    @RequestMapping(value = "/visit_count", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity count() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date endDate = calendar.getTime();

        int todayVisit = visitService.selectByDate(startDate, endDate).size();
        int totalVisit = visitService.selectByCondition(new HashMap<>(16)).size();

        Map<String, Integer> map = new HashMap<>();
        map.put("todayVisit", todayVisit);
        map.put("totalVisit", totalVisit);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setMessage("获取成功");
        result.setData(map);

        return new ResponseEntity(result, HttpStatus.OK);
    }

}
