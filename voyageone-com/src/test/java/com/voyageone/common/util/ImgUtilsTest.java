package com.voyageone.common.util;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class ImgUtilsTest {

    @Test
    public void testImage() throws IOException {

        File image = new File("d:/new-balance-696-mrl696dn-1.jpg");
        InputStream input = new FileInputStream(image); ;
        String enCodeStr = ImgUtils.encodeToString(input, "jpg");

        File imageSave = new File("d:/1441943880.9928_1.jpg");
        if (imageSave.exists()) {
            imageSave.delete();
        }
        ImgUtils.decodeToImageFile(enCodeStr, imageSave);
    }
}