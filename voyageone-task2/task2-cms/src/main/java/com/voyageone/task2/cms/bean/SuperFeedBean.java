package com.voyageone.task2.cms.bean;

import com.voyageone.common.util.MD5;
import com.voyageone.common.util.excel.ReflectUtil;
import org.apache.poi.util.SystemOutLogger;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

/**
 * @author james.li on 2016/5/9.
 * @version 2.0.0
 */
public class SuperFeedBean {
    public String beanToMd5(Object object, Set<String> noMd5Fields){
        noMd5Fields.add("md5");
        noMd5Fields.add("updateflag");
        List<Field> fields = ReflectUtil.getListField(object.getClass());
        StringBuffer temp = new StringBuffer();
        fields.stream()
                .sorted((o1, o2) -> o1.getName().compareTo(o2.getName()))
                .filter(field -> !noMd5Fields.contains(field.getName()))
                .forEach(field1 -> {
                    try {
                        Object v = ReflectUtil.getFieldValueByName(field1.getName(), object);
                        if (v != null){
                            temp.append(v.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        return MD5.getMD5(temp.toString());
    }
}
