package com.voyageone.common.util;

import org.springframework.cglib.beans.BeanCopier;

public class BeanUtil {

    /**
     * bean Copy
     */
    public static void copy(Object source, Object target) {
        BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), false);
        copier.copy(source, target, null);
    }
}
