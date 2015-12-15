package com.voyageone.common.util;

import org.junit.Test;

/**
 * @author Jonas, 12/11/15
 * @version 1.3.0
 * @since 1.3.0
 */
public class HttpUtilsTest {

    @Test
    public void testPost() {

        String cont = HttpUtils.post("https://120.26.219.17/jc.asia.dev/devicom_apps/catalog_request.php", null);

        System.out.println(cont);
    }
}