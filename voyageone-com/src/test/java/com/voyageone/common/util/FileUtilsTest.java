package com.voyageone.common.util;

import org.junit.Test;

/**
 * Created by DELL on 2016/6/21.
 */
public class FileUtilsTest {

    @Test
    public void testMkdirPath() {
        FileUtils.mkdirPath("/usr/local/task/aab/");
    }
}
