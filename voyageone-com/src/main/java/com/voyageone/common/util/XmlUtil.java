package com.voyageone.common.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.XMLWriter;

import java.io.*;

/**
 * Created by sn3 on 2015-06-02.
 */
public class XmlUtil {

    //保存XML为文件
    public static Boolean writeXml2File(String content,String fileName,String Path){
        String file = Path + fileName;
        try {
            Document doc = DocumentHelper.parseText(content);
            XMLWriter write;
            write = new XMLWriter(new FileOutputStream(file));
            write.write(doc);
            write.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    //保存XML为文件
    public static String readXml(String fileName,String Path){
        String content = "";
        try {
            String xml = Path + fileName;
            File file = new File(xml);
            FileInputStream is = new FileInputStream(xml);
            int size = (int) file.length();
            byte[] bytes = getBytes(is,size);

            assert bytes != null;

            content = new String(bytes, "UTF-8");
            is.close();
        } catch (IOException ignored) {

        }
        return content;
    }

    private static byte[] getBytes(InputStream inputStream,int size) {
        byte[] bytes = new byte[size];
        try {
            inputStream.read(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
