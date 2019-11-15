package net.stackoverflow.blog.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * mybatis分页查询，条件查询
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
public class Page implements Serializable {

    private int page;
    private int start;
    private int limit;
    private String order;
    private String sort;
    private Map<String, Object> searchMap;

    public Page(int page, int limit, String order, String sort, Map<String, Object> searchMap) {
        this.page = page;
        this.start = (page - 1) * limit;
        this.limit = limit;
        this.order = order;
        this.sort = sort;
        this.searchMap = searchMap;
    }
}
