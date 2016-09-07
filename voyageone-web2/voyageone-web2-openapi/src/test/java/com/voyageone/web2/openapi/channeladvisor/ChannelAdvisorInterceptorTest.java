package com.voyageone.web2.openapi.channeladvisor;

import com.voyageone.common.util.HttpUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChannelAdvisorInterceptorTest {

    @Test
    public void test1() throws IOException {
        String result = HttpUtils.get("http://localhost:8080/rest/channeladvisor/products");
        System.out.println(result);
    }

    @Test
    public void test2() throws Exception {
        Map<String, String> header = new HashMap<>();
        header.put("SellerID", "049beea8-bdd1-48f0-a930-e56e42f85458");

        String result = HttpUtils.get("http://localhost:8080/rest/channeladvisor/products", header);
        System.out.println(result);
    }

    @Test
    public void test3() throws Exception {
        Map<String, String> header = new HashMap<>();
        header.put("SellerID", "049beea8-bdd1-48f0-a930-e56e42f85458");
        header.put("SellerToken", "caf8e5ed-16c4-40d8-92ce-1ce86e03cac5-1");

        String result = HttpUtils.get("http://localhost:8080/rest/channeladvisor/products", header);
        System.out.println(result);
    }

    @Test
    public void test4() throws Exception {
        Map<String, String> header = new HashMap<>();
        header.put("SellerID", "049beea8-bdd1-48f0-a930-e56e42f85458");
        header.put("SellerToken", "caf8e5ed-16c4-40d8-92ce-1ce86e03cac5");

        String result = HttpUtils.get("http://localhost:8080/rest/channeladvisor/products", header);
        System.out.println(result);
    }
}
