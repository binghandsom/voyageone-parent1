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
    public String uploadImage(byte[] bytes, String strOssFilePath) throws IOException {
//        logger.info(String.format("retry:%d!", retry++));
        try (ByteArrayInputStream in = new ByteArrayInputStream(bytes);){
            AliYunOSSClient client = new AliYunOSSClient(ALIYUN_ENDPOINT, ALIYUN_ACCESS_KEYID, ALIYUN_ACCESS_KEYSECRET);
            return client.putOSS(in, ALIYUN_BUCKETNAME, strOssFilePath);
        }
    }

}
