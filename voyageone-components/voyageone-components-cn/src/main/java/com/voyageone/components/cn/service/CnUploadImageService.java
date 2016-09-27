package com.voyageone.components.cn.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.masterdate.schema.utils.XmlUtils;
import com.voyageone.components.aliyun.AliYunOSSClient;
import com.voyageone.components.cn.CnBase;
import com.voyageone.components.cn.enums.CnConstants;
import com.voyageone.components.cn.enums.CnUpdateType;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 独立域名图片上传用
 *
 * @author morse on 2016/9/27
 * @version 2.6.0
 */
@Service
@EnableRetry
public class CnUploadImageService extends CnBase {

    private int retry = 0;
    private static int BUFFER_SIZE = 122880;

    private static final String ALIYUN_ENDPOINT ="http://oss-cn-hangzhou.aliyuncs.com";
    private static final String ALIYUN_ACCESS_KEYID ="8N4YxX9U04zHrMJu" ;
    private static final String ALIYUN_ACCESS_KEYSECRET ="zSkOk82RetLpwCe9igqCm9bSW9tuPy";
    private static final String ALIYUN_BUCKETNAME ="sneakerheadcn";

    @Retryable
    private void doUploadImage(String url, String strOssFilePath) throws IOException {

//        try {
//            downloadImage(url);
//        } catch (IOException e) {
//            logger.info(String.format("error! retry:%d", retry));
//        } catch (BusinessException e) {
//            logger.info(String.format("bus error! retry:%d", retry));
//        }
//        logger.info(String.format("读取图片[%s]成功!", url));
//        downloadImage("http://s7d5.scene7.com/is/image/sneakerhead/Target_20160527_x1200_1200x?$1200x1200$&$product=010-5190838801-0DTCN00-1-1");
//        throw new BusinessException(String.format("throws retry:%d", retry));


//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        URL imgUrl = new URL(url);
//        InputStream is = imgUrl.openStream();
//
//        byte[] byte_buf = new byte[1024];
//        int readBytes = 0;
//        while ((readBytes = is.read(byte_buf)) > 0) {
//            out.write(byte_buf, 0, readBytes);
//        }
//        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
//
//        String fileName = "D:\\a.jpg";
//        File file = new File(fileName);
//        if (!file.exists()) {
//            file.createNewFile();
//        }
//
//        FileOutputStream output = new FileOutputStream(fileName);
//        int bytesRead;
//        byte[] buf = new byte[BUFFER_SIZE];
//        while ((bytesRead = in.read(buf)) > 0) {
//            output.write(buf, 0, bytesRead);
//        }
//        output.close();
//        in.close();
//
//
    }

    /**
     * 读取图片
     */
    @Retryable
    public byte[] downloadImage(String url) throws IOException {
//        logger.info(String.format("retry:%d!" + url, retry++));
        URL imgUrl = new URL(url);
        try (InputStream inputStream = imgUrl.openStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            int bytesRead;
            byte[] buf = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }

            return outputStream.toByteArray();
        }
    }


    /**
     * 上传图片
     *
     * @param strOssFilePath OSS存放路径
     */
    @Retryable
    public void uploadImage(byte[] bytes, String strOssFilePath) throws IOException {
        logger.info(String.format("retry:%d!", retry++));
        try (ByteArrayInputStream in = new ByteArrayInputStream(bytes);){
            AliYunOSSClient client = new AliYunOSSClient(ALIYUN_ENDPOINT, ALIYUN_ACCESS_KEYID, ALIYUN_ACCESS_KEYSECRET);


        }
    }

}
