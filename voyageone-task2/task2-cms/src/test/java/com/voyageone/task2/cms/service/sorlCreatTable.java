package com.voyageone.task2.cms.service;

import com.voyageone.components.solr.bean.CmsProductSearchModel;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import com.voyageone.components.solr.bean.CmsProductSearchPlatformModel;
import org.junit.Test;

/**
 * Created by james on 2017/3/23.
 */
public class sorlCreatTable {

    @Test
    public void creatTable() {

        Class userCla = CmsProductSearchModel.class;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<field name=\"keywords\" type=\"text_en_splitting_tight\" multiValued=\"true\" indexed=\"true\"/>\n");

        stringBuffer.append(schema(CmsProductSearchModel.class,""));

        for(int i= 20;i<=33;i++) {
            stringBuffer.append(schema(CmsProductSearchPlatformModel.class, "P"+i+"_"));
        }
        System.out.println(stringBuffer.toString());
    }

    private StringBuffer schema(Class userCla, String prefix){
        Field[] fs = userCla.getDeclaredFields();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            if (f.getType().getTypeName().equals(String.class.getTypeName())) {
                String schema = "";
                if (f.getName().equals("id")) {
                    schema = String.format("<field name=\"%s\" type=\"%s\" required=\"true\" multiValued=\"false\" indexed=\"true\" stored=\"true\"/>\n", prefix+f.getName(), "string");
                } else if (f.getName().equals("productModel") || f.getName().equals("productCode")) {
                    schema = String.format("<field name=\"%s\" type=\"%s\" multiValued=\"false\" indexed=\"true\" stored=\"true\"/>\n", prefix+f.getName(), "lowercase");
                } else {
                    schema = String.format("<field name=\"%s\" type=\"%s\" multiValued=\"false\" indexed=\"true\" stored=\"true\"/>\n", prefix+f.getName(), "string");
                }
                stringBuffer.append(schema);
            } else if (f.getType().getTypeName().equals(Integer.class.getTypeName())) {
                String schema = String.format("<field name=\"%s\" type=\"%s\" multiValued=\"false\" indexed=\"true\" stored=\"true\"/>\n", prefix+f.getName(), "int");
                stringBuffer.append(schema);

            } else if (f.getType().getTypeName().equals(Long.class.getTypeName())) {
                String schema = String.format("<field name=\"%s\" type=\"%s\" multiValued=\"false\" indexed=\"true\" stored=\"true\"/>\n", prefix+f.getName(), "long");
                stringBuffer.append(schema);

            } else if (f.getType().getTypeName().equals(Double.class.getTypeName())) {
                String schema = String.format("<field name=\"%s\" type=\"%s\" multiValued=\"false\" indexed=\"true\" stored=\"true\"/>\n", prefix+f.getName(), "double");
                stringBuffer.append(schema);

            } else if (f.getType().isAssignableFrom(List.class)) {

                Type fc = f.getGenericType();
                if (fc instanceof ParameterizedType) // 【3】如果是泛型参数的类型
                {
                    ParameterizedType pt = (ParameterizedType) fc;

                    Class genericClazz = (Class) pt.getActualTypeArguments()[0];
                    if (genericClazz.getTypeName().equals(String.class.getTypeName())) {
                        String schema = String.format("<field name=\"%s\" type=\"%s\" multiValued=\"true\" indexed=\"true\" stored=\"true\"/>\n", prefix+f.getName(), "string");
                        stringBuffer.append(schema);
                    } else if (genericClazz.getTypeName().equals(Integer.class.getTypeName())) {
                        String schema = String.format("<field name=\"%s\" type=\"%s\" multiValued=\"true\" indexed=\"true\" stored=\"true\"/>\n", prefix+f.getName(), "int");
                        stringBuffer.append(schema);
                    }
                }
            }
        }
        return stringBuffer;
    }
}
