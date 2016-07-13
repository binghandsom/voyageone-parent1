package com.voyageone.common.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class HttpScene7Test {

    @Test
    public void uploadImageFile1() {
        HttpScene7.uploadImageFile("Testing", new File("E:/image6/0181-49169254-BUFFBEIGE-7.jpg"));
    }

    @Test
    public void uploadImageFile2() throws FileNotFoundException {
        HttpScene7.uploadImageFile("WMF/images/", "0181-49169254-BUFFBEIGE-7.jpg", new FileInputStream(new File("E:/image6/0181-49169254-BUFFBEIGE-7.jpg")));
    }
}
