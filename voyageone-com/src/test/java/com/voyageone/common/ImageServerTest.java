package com.voyageone.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class ImageServerTest {

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