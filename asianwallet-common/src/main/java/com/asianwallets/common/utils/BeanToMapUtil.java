package com.asianwallets.common.utils;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shenxinran
 * @Date: 2019/3/1 14:06
 * @Description: javabean转map
 */
public class BeanToMapUtil {
    /**
     * java类转换成map集合
     *
     * @param obj
     * @return
     */
    public static HashMap<String, Object> beanToMap(Object obj) {
        HashMap<String, Object> params = new HashMap<String, Object>(0);
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (!"class".equals(name)) {
                    params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    public static Map<String, String> beanToStringMap(Object obj) {
        Map<String, String> params = new HashMap<>();
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (!"class".equals(name) && !StringUtils.isEmpty(String.valueOf(propertyUtilsBean.getNestedProperty(obj, name)))) {
                    params.put(name, String.valueOf(propertyUtilsBean.getNestedProperty(obj, name)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    /**
     * 跳过空值
     *
     * @param obj
     * @return
     */
    public static HashMap<String, Object> beanToMapWithoutNull(Object obj) {
        HashMap<String, Object> params = new HashMap<String, Object>(0);
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (propertyUtilsBean.getNestedProperty(obj, name) == null) {
                    continue;
                }
                if (!"class".equals(name)) {
                    params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

}
