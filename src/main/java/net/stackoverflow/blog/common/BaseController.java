package net.stackoverflow.blog.common;

import net.stackoverflow.blog.exception.ServerException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Controller基类
 *
 * @author 凉衫薄
 */
public class BaseController {

    /**
     * vo转po
     *
     * @param clazzMap
     * @param dto
     * @return
     */
    protected Map<String, List<Object>> vo2po(Map<String, Class> clazzMap, BaseVO dto) {

        Map<String, List<Object>> map = new HashMap<>();
        Set<String> key = clazzMap.keySet();
        Map<String, Map<String, Object>[]> data = dto.getData();
        Iterator<String> it = key.iterator();

        while (it.hasNext()) {
            String name = it.next();

            if (data.containsKey(name)) {
                Field[] fields = clazzMap.get(name).getDeclaredFields();
                List<Object> pos = new ArrayList<>();

                for (int i = 0; i < data.get(name).length; i++) {
                    try {
                        Object vo = Class.forName(clazzMap.get(name).getName()).newInstance();
                        for (Field field : fields) {
                            String attr = field.getName();
                            String upName = attr.substring(0, 1).toUpperCase() + attr.substring(1);
                            Method method = clazzMap.get(name).getMethod("set" + upName, field.getType());
                            method.invoke(vo, data.get(name)[i].get(attr));
                        }
                        pos.add(vo);
                    } catch (Exception e) {
                        throw new ServerException("vo2po转换错误");
                    }
                }
                map.put(name, pos);
            }
        }

        return map;
    }

    /**
     * vo2po
     *
     * @param name
     * @param clazz
     * @param vo
     * @return
     */
    protected List<Object> vo2po(String name, Class clazz, BaseVO vo) {
        Map<String, Class> classMap = new HashMap<String, Class>() {{
            put(name, clazz);
        }};
        Map<String, List<Object>> voMap = vo2po(classMap, vo);
        return voMap.get(name);
    }
}