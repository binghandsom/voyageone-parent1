package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.components.aliyun.AliYunOSSClient;
import com.voyageone.service.dao.cms.CmsMtImageCreateFileDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import com.voyageone.service.bean.openapi.OpenApiException;
import com.voyageone.service.bean.openapi.image.ImageErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@EnableRetry
public class AliYunOSSFileService extends BaseService {
    @Autowired
    CmsMtImageCreateFileDao daoCmsMtImageCreateFile;

    private void upload(String filefullName, String keySuffixWithSlash) throws OpenApiException {
        try {
            AliYunOSSClient client = new AliYunOSSClient(ImageConfig.getAliYunEndpoint(), ImageConfig.getAliYunAccessKeyId(), ImageConfig.getAliYunAccessKeySecret());
            client.putOSS(filefullName, ImageConfig.getAliYunBucketName(), keySuffixWithSlash);
        } catch (Exception ex) {
            throw new OpenApiException(ImageErrorEnum.AliyunOSSUploadError, "上传阿里云OSS错误", ex);
        }
    }

    @Retryable(maxAttempts = 3)
    public void upload(CmsMtImageCreateFileModel modelFile) throws OpenApiException {
        //上传阿里OSS
        upload(modelFile.getFilePath(), modelFile.getOssFilePath());
        modelFile.setOssState(1);
        //清楚报错信息
        modelFile.setErrorCode(0);
        modelFile.setErrorMsg("");
    }
}
