package com.voyageone.common;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class ImageServerTest {
    @Test
    public void proxyDownloadImage() throws Exception {
        try (InputStream image = ImageServer.proxyDownloadImage(
                "http://s7d5.scene7.com/is/image/sneakerhead/spalding_718_small" +
                        "?$1200x1200$&$text1=111&$imagemoban=928-20170719140357-028-ps2871331-6",
                "001")) {

            byte[] byteArray = IOUtils.toByteArray(image);
            Assert.assertTrue(byteArray.length > 0);
        }
    }

    @Test
    public void test1() {
        String url = ImageServer.imageServerUrl("");
        assertEquals("http://10.0.0.44:2080/", url);
    }

    @Test
    public void testSendMain() {
        try {
            ImageServer.uploadImage("1000", "some", null);
        } catch (ImageServer.FailUploadingException e) {
            e.printStackTrace();
        }
    }
}