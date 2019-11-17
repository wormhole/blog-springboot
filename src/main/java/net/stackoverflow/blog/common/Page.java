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
    private int offset;
    private int limit;
    private Map<String, Object> searchMap;

    public Page(int page, int limit, Map<String, Object> searchMap) {
        this.page = page;
        this.offset = (page - 1) * limit;
        this.limit = limit;
        this.searchMap = searchMap;
    }
}
