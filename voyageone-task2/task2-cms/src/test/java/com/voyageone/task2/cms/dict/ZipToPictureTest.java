package com.voyageone.task2.cms.dict;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by gjl on 2016/10/14.
 */
public class ZipToPictureTest {
    @Test
    public void main() throws Exception {
        //生成的ZIP文件名为Demo.zip
        String strZipName = "D:/picture/Demo_HhMmSs.zip";
        getUrlInputStream(strZipName);
        System.out.println("生成Demo.zip成功");
    }

    //根据传取的List<Map>创建压缩包的文件夹
    public void getUrlInputStream(String strZipName) throws IOException {
        byte[] buffer = new byte[1024];
        List<Map<String, String>> urlList = new ArrayList<>();
        Map<String, String> urlOne = new HashMap<>();
        urlOne.put("url", "http://www.52design.com/pic/20128/201286141927725.png");
        urlOne.put("name", "a1");
        urlOne.put("type", "picture1");
        Map<String, String> urlTwo = new HashMap<>();
        urlTwo.put("url", "http://www.52design.com/pic/20128/201286141927725.png");
        urlTwo.put("name", "a2");
        urlTwo.put("type", "picture2");
        urlList.add(urlOne);
        urlList.add(urlTwo);
        //压缩流
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(strZipName));
        for (Map<String, String> urlMap : urlList) {
            URL url = new URL(urlMap.get("url"));
            //截取后缀名称
            String suffix = FilenameUtils.getExtension(urlMap.get("url"));
            //Url
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //生成图片的名称
            out.putNextEntry(new ZipEntry(urlMap.get("type") + "\\" + urlMap.get("name") + "." + suffix));
            int len;
            InputStream inputStream = conn.getInputStream();
            //读入需要下载的文件的内容，打包到zip文件
            while ((len = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            inputStream.close();
            out.closeEntry();
        }
        out.close();
    }

}
