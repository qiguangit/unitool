package com.qiguangit.unitool.util;

import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BeanUtils {
    public static Map<String, Object> bean2Map(Object bean) {
        Map<String, Object> map = new HashMap<>();
        callGetterOrSetter(bean, map, true);
        return map;
    }

    public static <T> T map2Bean(Map<String, Object> map, Class<T> klass) {
        T t = null;
        try {
            t = klass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
        callGetterOrSetter(t, map, false);
        return t;
    }

    private static void callGetterOrSetter(Object o, Map<String, Object> map, boolean getter) {
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(o.getClass());
        } catch (IntrospectionException e) {
            e.printStackTrace();
            return;
        }
        Arrays.stream(beanInfo.getPropertyDescriptors())
              .filter(descriptor -> {
                  String name = descriptor.getName();
                  return !"class".equals(name) && !"id".equals(name);
              })
              .collect(Collectors.toList())
              .forEach(descriptor -> {
                  try {
                      final String fieldName = descriptor.getName();
                      if (getter) {
                          final Method readMethod = descriptor.getReadMethod();
                          map.put(fieldName, readMethod.invoke(o));
                      } else {
                          Method writeMethod = descriptor.getWriteMethod();
                          if (map.containsKey(fieldName)) {
                              writeMethod.invoke(o, map.get(fieldName));
                          }
                      }
                  } catch (IllegalAccessException | InvocationTargetException e) {
                      e.printStackTrace();
                  }
              });

    }

    /**
     *
     * @return
     */
    private static String field2Setter(String fieldName) {
        if (StringUtils.isBlank(fieldName)) {
            return "";
        }
        return "set" + fieldName.substring(0, 1)
                .toUpperCase()
                .concat(fieldName.substring(1));
    }
}
