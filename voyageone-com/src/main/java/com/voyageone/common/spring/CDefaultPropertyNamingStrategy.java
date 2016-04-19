package com.voyageone.common.spring;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;

/**
 * Created by admin on 2014/8/18.
 */
public class CDefaultPropertyNamingStrategy extends PropertyNamingStrategy.PropertyNamingStrategyBase {
     @Override
    public  String translate(String propertyName)
    {
      // String name = propertyName.replaceAll("^\\w", propertyName.substring(0, 1).toUpperCase());
       //return name;
        return  propertyName;
    }
    @Override
    public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName)
    {
        return translate(defaultName);
    }

    @Override
    public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName)
    {
        String name=method.getName();
        if (name.startsWith("get"))
        {
            return  name.substring(3);
        }
        return name.substring(2);
    }

    @Override
    public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName)
    {
        String name=method.getName();
        return  name.substring(3);
       // return translate(defaultName);
    }

    String translateMethodName(AnnotatedMethod method, String defaultName)
    {
        String name=method.getName();
        if (name.startsWith("get"))
        {
           return  name.substring(3);
        }
        return name.substring(2);
    }
    @Override
    public String nameForConstructorParameter(MapperConfig<?> config, AnnotatedParameter ctorParam,
                                              String defaultName)
    {
        return translate(defaultName);
    }
}
