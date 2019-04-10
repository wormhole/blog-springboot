package net.stackoverflow.blog.util;

import javax.validation.ConstraintViolation;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 校验工具类
 *
 * @author 凉衫薄
 */
public class ValidationUtils {

    /**
     * 包装验证结果
     *
     * @param violations
     * @return
     */
    public static <T> Map<String, String> errorMap(Set<ConstraintViolation<T>> violations) {
        Map<String, String> map = new TreeMap<>();
        Iterator<ConstraintViolation<T>> iter = violations.iterator();
        while (iter.hasNext()) {
            ConstraintViolation<T> violation = iter.next();
            String key = violation.getPropertyPath().toString();
            if (map.get(key) == null) {
                map.put(key, violation.getMessage());
            } else {
                String errInfo = map.get(key);
                errInfo += "；" + violation.getMessage();
                map.replace(key, errInfo);
            }
        }
        return map;
    }
}
