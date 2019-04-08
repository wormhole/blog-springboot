package net.stackoverflow.blog.common;

import java.io.Serializable;

/**
 * 响应信息
 *
 * @author 凉衫薄
 */
public class Response implements Serializable {

    public static final boolean SUCCESS = true;
    public static final boolean FAILURE = false;

    private boolean status;
    private String message;
    private Object data;

    public Response() {

    }

    public Response(boolean status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
