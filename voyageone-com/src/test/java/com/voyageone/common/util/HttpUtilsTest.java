package com.voyageone.common.util;

import org.junit.Test;

import java.io.IOException;

/**
 * @author Jonas, 12/11/15
 * @version 1.3.0
 * @since 1.3.0
 */
public class HttpUtilsTest {

    @Test
    public void testPost() throws IOException {

        String cont = HttpUtils.post("https://120.26.219.17/jc.asia.dev/devicom_apps/catalog_request.php", null);

        System.out.println(cont);
    }

    @Test
    public void testExists() {
        Boolean cont = HttpUtils.exists("http://mce042-fs.nexcess.net:81/voyageone_image/018/018-51150191-1.jpg");
        System.out.println(String.valueOf(cont));
    }
}