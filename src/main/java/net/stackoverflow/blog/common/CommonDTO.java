package net.stackoverflow.blog.common;

import java.io.Serializable;
import java.util.Map;

/**
 * 公共DTO类
 *
 * @author 凉衫薄
 */
public class CommonDTO implements Serializable {

    private Page page;

    private Map<String, Map<String, Object>[]> data;

    public CommonDTO() {
    }

    public CommonDTO(Page page, Map<String, Map<String, Object>[]> data) {
        this.page = page;
        this.data = data;
    }

    public Map<String, Map<String, Object>[]> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, Object>[]> data) {
        this.data = data;
    }
}
