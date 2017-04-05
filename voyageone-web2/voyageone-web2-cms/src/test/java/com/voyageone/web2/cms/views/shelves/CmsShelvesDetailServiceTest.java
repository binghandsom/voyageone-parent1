package com.voyageone.web2.cms.views.shelves;

import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.CmsBtShelvesInfoBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by james on 2016/11/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:META-INF/context-web2.xml","classpath*:META-INF/context-web2-mvc.xml"})
public class CmsShelvesDetailServiceTest {
    @Autowired
    CmsShelvesDetailService cmsShelvesDetailService;

    @Test
    public void exportAppImage() throws Exception {
        FileUtils.deleteAllFilesOfDir(new File("H:\\shelves\\shelves1"));
        byte[]imageBuf = cmsShelvesDetailService.exportAppImage(1);
        try(FileOutputStream fileOutputStream = new FileOutputStream((new File("H:\\shelves\\merge.png")))) {
            fileOutputStream.write(imageBuf);
        }catch (Exception e){

        }
    }

    @Test
    public void getShelvesInfo() throws Exception {

//        CacheHelper.getValueOperation().set("ShelvesMonitor_" + 1, 1, 10, TimeUnit.SECONDS);

        List<CmsBtShelvesInfoBean> cmsBtShelvesInfoBeen = cmsShelvesDetailService.getShelvesInfo(Arrays.asList(1), false, "test", "000");
        System.out.print(JacksonUtil.bean2Json(cmsBtShelvesInfoBeen));
    }

    @Test
    public void getAppImage() throws Exception {

        try {
            List<String> images = new ArrayList<>();
            images.add("C:\\usr\\web\\contents\\cms\\shelves\\shelves1\\05098AA.jpg");
            images.add("C:\\usr\\web\\contents\\cms\\shelves\\shelves1\\05098AA.jpg");
            images.add("C:\\usr\\web\\contents\\cms\\shelves\\shelves1\\05098AA.jpg");
            images.add("C:\\usr\\web\\contents\\cms\\shelves\\shelves1\\05098AA.jpg");
            byte[]  a = cmsShelvesDetailService.createAppImage(images,2);
            try(FileOutputStream fileOutputStream = new FileOutputStream((new File("H:\\shelves\\merge.png")))) {
                fileOutputStream.write(a);
            }catch (Exception e){

            }
            Integer i=0;
            i++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public byte[] creatImage(List<String> urls, int col){

        try {
            InputStream imagein = new FileInputStream(urls.get(0));
            BufferedImage image = ImageIO.read(imagein);
            imagein.close();

            Integer width = image.getWidth();
            Integer height = image.getHeight();
            List<List<String>> urlSplit = CommonUtil.splitList(urls,col);
            BufferedImage combined = new BufferedImage(width * col , height*urlSplit.size(), BufferedImage.TYPE_INT_RGB);
            Graphics g = combined.getGraphics();
            for(int i = 0;i<urlSplit.size(); i++){
                for(int j = 0;j<urlSplit.get(i).size(); j++){
                    InputStream temp = new FileInputStream(urlSplit.get(i).get(j));
                    BufferedImage imageTemp = ImageIO.read(temp);
                    g.drawImage(imageTemp, j*width, i*height, null);
                    temp.close();
                }
            }
            ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream();
            // Save as new image
            ImageIO.write(combined, "PNG", bufferedOutputStream);
            return bufferedOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}