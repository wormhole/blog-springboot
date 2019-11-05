package net.stackoverflow.blog.common;

import java.io.Serializable;
import java.util.Map;

/**
 * 公共VO类
 *
 * @author 凉衫薄
 */
public class BaseVO implements Serializable {

    private Page page;

    private Map<String, Map<String, Object>[]> data;

    public BaseVO() {
    }

    public BaseVO(Page page, Map<String, Map<String, Object>[]> data) {
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
