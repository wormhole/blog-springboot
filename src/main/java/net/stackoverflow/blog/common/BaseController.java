package net.stackoverflow.blog.common;

import net.stackoverflow.blog.exception.ServerException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 控制器基类
 *
 * @author 凉衫薄
 */
public class BaseController {

    /**
     * DTO转VO
     *
     * @param clazzMap
     * @param dto
     * @return
     */
    protected Map<String, List<Object>> dto2vo(Map<String, Class> clazzMap, CommonDTO dto) {

        Map<String, List<Object>> map = new HashMap<>();
        Set<String> key = clazzMap.keySet();
        Map<String, Map<String, Object>[]> data = dto.getData();
        Iterator<String> it = key.iterator();

        while (it.hasNext()) {
            String name = it.next();

            if (data.containsKey(name)) {
                Field[] fields = clazzMap.get(name).getDeclaredFields();
                List<Object> vos = new ArrayList<>();

                for (int i = 0; i < data.get(name).length; i++) {
                    try {
                        Object vo = Class.forName(clazzMap.get(name).getName()).newInstance();
                        for (Field field : fields) {
                            String attr = field.getName();
                            String upName = attr.substring(0, 1).toUpperCase() + attr.substring(1);
                            Method method = clazzMap.get(name).getMethod("set" + upName, field.getType());
                            method.invoke(vo, data.get(name)[i].get(attr));
                        }
                        vos.add(vo);
                    } catch (Exception e) {
                        throw new ServerException("dto2vo转换错误");
                    }
                }
                map.put(name, vos);
            }
        }

        return map;
    }

    /**
     * DTO转VO
     *
     * @param name
     * @param clazz
     * @param dto
     * @return
     */
    protected List<Object> dto2vo(String name, Class clazz, CommonDTO dto) {
        Map<String, Class> classMap = new HashMap<String, Class>() {{
            put(name, clazz);
        }};
        Map<String, List<Object>> voMap = dto2vo(classMap, dto);
        return voMap.get(name);
    }
}