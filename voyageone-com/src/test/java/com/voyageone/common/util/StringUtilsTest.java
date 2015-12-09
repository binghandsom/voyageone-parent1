package com.voyageone.common.util;

import org.junit.Test;

/**
 * Created by DELL on 2015/12/9.
 */
public class StringUtilsTest {

    @Test
    public void testEncodeBase64() {
        String aa = "运动包/户外包/配件>运动首饰";
        String encode = StringUtils.encodeBase64(aa);
        System.out.println(encode);
        String aa1 = StringUtils.decodeBase64(encode);
        System.out.println(aa1);
    }
}
