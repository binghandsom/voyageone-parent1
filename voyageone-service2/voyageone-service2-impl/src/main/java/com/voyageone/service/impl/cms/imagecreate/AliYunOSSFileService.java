package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.components.aliyun.AliYunOSSClient;
import com.voyageone.service.dao.cms.CmsMtImageCreateFileDao;
import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import com.voyageone.service.model.openapi.OpenApiException;
import com.voyageone.service.model.openapi.image.ImageErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AliYunOSSFileService {
    @Autowired
    CmsMtImageCreateFileDao daoCmsMtImageCreateFile;
    private void upload(String filefullName, String keySuffixWithSlash) throws OpenApiException {
        try {
            AliYunOSSClient client = new AliYunOSSClient(ImageConfig.getAliYunEndpoint(), ImageConfig.getAliYunAccessKeyId(), ImageConfig.getAliYunAccessKeySecret());
            client.putOSS(filefullName, "shenzhen-vo", keySuffixWithSlash);
        } catch (Exception ex) {
            throw new OpenApiException(ImageErrorEnum.AliyunOSSUploadError,"上传阿里云OSS错误", ex);
        }
    }
    public void upload(CmsMtImageCreateFileModel modelFile) throws OpenApiException {
        if (modelFile.getOssState() == 0) {//上传阿里OSS
            upload(modelFile.getFilePath(), modelFile.getOssFilePath());
            modelFile.setOssState(1);
            daoCmsMtImageCreateFile.update(modelFile);
        }
    }
}
