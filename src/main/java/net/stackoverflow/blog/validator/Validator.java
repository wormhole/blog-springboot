package net.stackoverflow.blog.validator;

import java.util.Map;

/**
 * 字段校验器接口
 *
 * @author 凉衫薄
 */
public interface Validator<T> {

    /**
     * 字段校验方法
     *
     * @param t
     * @return 返回结果集
     */
    Map<String, String> validate(T t);
}
