package com.voyageone.base.dao.mongodb.test.dao.support;

import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DELL on 2015/10/29.
 */
public class BasePropertyPath  {

    private static final String DELIMITERS = "\\$\\.";
    private static final Pattern SPLITTER = Pattern.compile("(?:[%s]?([%s]*?[^%s]+))".replaceAll("%s", DELIMITERS));

    public static PropertyPath from(String source, TypeInformation<?> type) {

        Assert.hasText(source, "Source must not be null or empty!");
        Assert.notNull(type, "TypeInformation must not be null or empty!");

        List<String> iteratorSource = new ArrayList<String>();
        Matcher matcher = SPLITTER.matcher("$" + source);

        while (matcher.find()) {
            iteratorSource.add(matcher.group(1));
        }

        Iterator<String> parts = iteratorSource.iterator();

        PropertyPath result = null;
        Stack<PropertyPath> current = new Stack<PropertyPath>();

        Method create3;
        Method create2;
        try {
            create3 = PropertyPath.class.getDeclaredMethod("create", String.class, TypeInformation.class, List.class);
            create3.setAccessible(true);
            create2 = PropertyPath.class.getDeclaredMethod("create", String.class, Stack.class);
            create2.setAccessible(true);

            while (parts.hasNext()) {
                if (result == null) {
                    result = (PropertyPath)create3.invoke(null, parts.next(), type, current);
                    current.push(result);
                } else {
                    current.push((PropertyPath)create2.invoke(null, parts.next(), current));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return result;
    }

}
