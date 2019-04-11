package net.stackoverflow.blog.common;

import java.io.Serializable;
import java.util.Map;

/**
 * 公共DTO类
 *
 * @author 凉衫薄
 */
public class BaseDTO implements Serializable {

    private Page page;

    private Map<String, Map<String, Object>[]> data;

    public BaseDTO() {
    }

    public BaseDTO(Page page, Map<String, Map<String, Object>[]> data) {
        this.page = page;
        this.data = data;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Map<String, Map<String, Object>[]> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, Object>[]> data) {
        this.data = data;
    }
}
