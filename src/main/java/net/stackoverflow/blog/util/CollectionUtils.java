package net.stackoverflow.blog.util;

import java.util.Collection;
import java.util.Map;

/**
 * 字典工具类
 *
 * @author 凉衫薄
 */
public class CollectionUtils {

    /**
     * 判断是否为空
     *
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection collection) {
        return (collection == null || collection.size() == 0);
    }

    /**
     * 判断map是否为空
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map map) {
        return (map == null || map.size() == 0);
    }
}
