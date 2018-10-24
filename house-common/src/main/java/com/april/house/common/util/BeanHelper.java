package com.april.house.common.util;

import com.april.house.common.model.ModelBean;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * BeanUtils和PropertyUtils区别
 * 1. BeanUtils以字符串的形式对javabean进行转换，而PropertyUtils是以原本的类型对javabean进行操作，
 *    如果类型不对，就会有argument type dismatch异常
 * 2. PropertyUtils.getProperty方法获得的属性值的类型为该属性本来的类型，
 *    BeanUtils.getProperty则是讲该属性的值转换为字符串后才返回
 */

public class BeanHelper {
    private static final Logger logger = LogManager.getLogger(BeanHelper.class);
    private static final String updateTimeKey = "updateTime";
    private static final String createTimeKey = "createTime";

    public static <T> void setDefaultProp(T target, Class<T> clazz) {
        innerDefault(target, clazz, PropertyUtils.getPropertyDescriptors(clazz), "default");
    }
    public static <T> void onUpdate(T target) {
        try {
            PropertyUtils.setProperty(target, updateTimeKey, new Date(System.currentTimeMillis()));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("setProperty " + updateTimeKey + "failed for " + target.getClass());
            return;
        }
    }

    private static <T> void innerDefault(T target, Class<?> clazz, PropertyDescriptor[] descriptors, String type) {
        for (PropertyDescriptor descriptor : descriptors) {
            String fieldName = descriptor.getName();
            Object value;
            try {
                value = PropertyUtils.getProperty(target, fieldName);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(String.format("can not set property when get for %s and clazz %s field %s", target, clazz, fieldName));
            }
            //获取属性的类
            //class1.isAssignbaleFrom(class2)方法此class1对象所表示的类或接口与指定的class2参数所表示的类
            //或接口是否相同，或是否是其超类或超接口
            if (String.class.isAssignableFrom(descriptor.getPropertyType()) && value == null) {
                try {
                    PropertyUtils.setProperty(target, fieldName, "");
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(String.format("can not set property when set for %s and clazz %s field %s", target, clazz, fieldName));
                }
            } else if (Number.class.isAssignableFrom(descriptor.getPropertyType()) && value == null) {
                try {
                    /**
                     * 注意这里不能使用PropertyUtils来进行属性设置。
                     * 因为按照上文注释，PropertyUtils是以原本类型对javabean进行设置的。
                     * 这里Number是一个父类，而target的属性的具体类型是Number的子类。
                     * 如果用PropertyUtils，会报argument type dismatch 错误
                     */
                    //PropertyUtils.setProperty(target, fieldName, 0);
                    BeanUtils.setProperty(target, fieldName, 0);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(String.format("can not set property when set for %s and clazz %s field %s", target, clazz, fieldName));
                }
            } else if (type.equals("insert") && Date.class.isAssignableFrom(descriptor.getPropertyType()) && value == null) {
                try {
                    BeanUtils.setProperty(target, fieldName, new Date(0));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(String.format("can not set property when set for %s and clazz %s field %s", target, clazz, fieldName));
                }
            }
        }
    }

    public static <T> void onInsert(T target) {
        Class<?> clazz = target.getClass();
        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(target);
        innerDefault(target, clazz, descriptors, "insert");
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        try {
            BeanUtils.setProperty(target, updateTimeKey, date);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format("can not set property when set for %s and clazz %s field %s, %n exception: %s", target, clazz, updateTimeKey, e));
        }

        try {
            BeanUtils.setProperty(target, createTimeKey, date);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format("can not set property when set for %s and clazz %s field %s, %n exception: %s", target, clazz, createTimeKey, e));
        }
    }

    public static void main(String[] args) {
        ModelBean mb = new ModelBean();
        System.out.println("mb = " + mb);
//        BeanHelper.setDefaultProp(mb, ModelBean.class);
        BeanHelper.onInsert(mb);
        System.out.println("After Set Default Prop");
        System.out.println("mb = " + mb);


    }
}
