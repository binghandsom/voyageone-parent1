package com.voyageone.common.spring.serializer;

public abstract class CJacksonSerializerUtil {

    private static ThreadLocal<Boolean> currentThreadSerializerCustom = new ThreadLocal<>();

    public static void setCustom(boolean custom) {
        currentThreadSerializerCustom.set(custom);
    }

    public static boolean isCustom() {
        Boolean custom = currentThreadSerializerCustom.get();
        if (custom != null) {
            return custom;
        }
        return false;
    }
}
