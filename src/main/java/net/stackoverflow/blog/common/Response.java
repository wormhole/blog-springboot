package net.stackoverflow.blog.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 响应信息
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response implements Serializable {

    public final static Integer SUCCESS = 0;
    public final static Integer FAILURE = 1;
    public final static Integer SERVER_ERROR = -1;

    private Integer status;
    private String message;
    private Object data;
}
