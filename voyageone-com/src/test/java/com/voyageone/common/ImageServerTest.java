package com.voyageone.common;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:test-context.xml")
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

    @Test
    public void testMatch() {

        final String imageUrl = "http://s7d5.scene7.com/is/image/sneakerhead/sn%2D88%2Dpc%2Dshop?$name=Converse+Chuck+Taylor+All+Star+%E5%8C%A1%E5%A8%81%E7%94%B7%E9%9E%8B+%E6%97%B6%E5%B0%9A%E4%B8%AD%E5%B8%AE%E4%BC%91%E9%97%B2%E6%9D%BF%E9%9E%8B$&$img1=converse-chuck-taylor-all-star-syde-street-mid-155484c-1&$price=582";

        final Pattern pattern = Pattern.compile("https?://.+?:?\\d*?/(.+)$");
        final Matcher matcher = pattern.matcher(imageUrl);

        System.out.println(matcher.matches());
    }
}