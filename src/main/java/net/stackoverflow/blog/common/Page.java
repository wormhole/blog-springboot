package net.stackoverflow.blog.common;

import java.io.Serializable;
import java.util.Map;

/**
 * mybatis分页查询，条件查询
 *
 * @author 凉衫薄
 */
public class Page implements Serializable {

    private int page;
    private int start;
    private int limit;
    private Map<String, Object> searchMap;

    public Page() {

    }

    public Page(int page, int limit, Map<String, Object> searchMap) {
        this.page = page;
        this.start = (page - 1) * limit;
        this.limit = limit;
        this.searchMap = searchMap;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Map<String, Object> getSearchMap() {
        return searchMap;
    }

    public void setSearchMap(Map<String, Object> searchMap) {
        this.searchMap = searchMap;
    }
}
