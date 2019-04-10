package net.stackoverflow.blog.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 类型转换工具类
 *
 * @author 凉衫薄
 */
public class TransferUtils {

    /**
     * dto转po
     *
     * @param poClass
     * @param dto
     * @return
     */
    public static Object dto2po(Class poClass, Object dto) {
        Field[] poFields = poClass.getDeclaredFields();
        Class dtoClass = dto.getClass();
        Object po = null;
        try {
            po = poClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        for (Field f : poFields) {
            String fieldName = f.getName();
            String dtoGetMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String poSetMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            Method dtoGetMethod = null;
            Method poSetMethod = null;

            try {
                dtoGetMethod = dtoClass.getMethod(dtoGetMethodName, null);
            } catch (NoSuchMethodException e) {
                continue;
            }

            try {
                poSetMethod = poClass.getMethod(poSetMethodName, f.getType());
                poSetMethod.invoke(po, dtoGetMethod.invoke(dto, null));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return po;
    }

    /**
     * po转dto
     *
     * @param dtoClass
     * @param po
     * @return
     */
    public static Object po2dto(Class dtoClass, Object po) {
        Field[] dtoFields = dtoClass.getDeclaredFields();
        Class poClass = po.getClass();
        Object dto = null;
        try {
            dto = dtoClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        for (Field f : dtoFields) {
            String fieldName = f.getName();
            String poGetMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String dtoSetMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            Method dtoSetMethod = null;
            Method poGetMethod = null;

            try {
                poGetMethod = poClass.getMethod(poGetMethodName, null);
            } catch (NoSuchMethodException e) {
                continue;
            }

            try {
                dtoSetMethod = dtoClass.getMethod(dtoSetMethodName, f.getType());
                dtoSetMethod.invoke(dto, poGetMethod.invoke(po, null));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return dto;
    }

}
