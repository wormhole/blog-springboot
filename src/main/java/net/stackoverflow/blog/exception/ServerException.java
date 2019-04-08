package net.stackoverflow.blog.exception;

/**
 * 服务器异常类
 *
 * @author 凉衫薄
 */
public class ServerException extends RuntimeException {
    public ServerException(String message) {
        super(message);
    }
}
