package com.voyageone.common;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class ImageServerTest {

    @Before
    public void setup() {
        ImageServer.DEBUG = true;
    }

    @Test
    public void proxyDownloadImage1() throws Exception {
        try (InputStream image = ImageServer.proxyDownloadImage("http://image.voyageone.com.cn/cms/store/033/20170421090922.jpg", "033")) {
            byte[] byteArray = IOUtils.toByteArray(image);
            Assert.assertTrue(byteArray.length > 0);
        }
    }

    @Test
    public void proxyDownloadImage2() throws Exception {
        try (InputStream image = ImageServer.proxyDownloadImage("http://image.voyageone.com.cn/is/image/sneakerhead/022-022-RB21327105152-1",
                "928")) {
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

    @Test
    public void testMatch() {

        final String imageUrl = "http://s7d5.scene7.com/is/image/sneakerhead/sn%2D88%2Dpc%2Dshop?$name=Converse+Chuck+Taylor+All+Star+%E5%8C%A1%E5%A8%81%E7%94%B7%E9%9E%8B+%E6%97%B6%E5%B0%9A%E4%B8%AD%E5%B8%AE%E4%BC%91%E9%97%B2%E6%9D%BF%E9%9E%8B$&$img1=converse-chuck-taylor-all-star-syde-street-mid-155484c-1&$price=582";

        final Pattern pattern = Pattern.compile("^https?://.+?:?\\d*/is/image/sneakerhead/(.+)$");
        final Matcher matcher = pattern.matcher(imageUrl);

        System.out.println(matcher.matches());
        System.out.println(matcher.group(1));
    }
}