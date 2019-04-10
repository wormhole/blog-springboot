package net.stackoverflow.blog.exception;

/**
 * 验证码错误异常类
 *
 * @author 凉衫薄
 */
public class VCodeException extends Exception {
    public VCodeException(String message) {
        super(message);
    }
}
