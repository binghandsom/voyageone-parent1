package com.voyageone.common.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 测试 json 序列化与反序列化的各种使用场景
 *
 * Created by jonas on 2016/11/23.
 */
public class JsonUtilTest {
    @Test
    public void testBean2JsonForException() {

        Exception e1 = new NullPointerException("some var");

        Exception e2 = new IllegalArgumentException("some var is null", e1);

        Exception e3 = new Exception("some exception", e2);

        String json = JsonUtil.bean2Json(e3);

        System.out.println(json);
    }
}