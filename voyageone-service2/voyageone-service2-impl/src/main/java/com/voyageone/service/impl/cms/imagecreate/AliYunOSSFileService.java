package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.components.aliyun.AliYunOSSClient;
import com.voyageone.service.dao.cms.CmsMtImageCreateFileDao;
import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
public class AliYunOSSFileService {
    @Autowired
    CmsMtImageCreateFileDao daoCmsMtImageCreateFile;
    private void putOSS(String filefullName, String keySuffixWithSlash) throws FileNotFoundException {
        try {
            AliYunOSSClient client = new AliYunOSSClient(ImageConfig.getAliYunEndpoint(), ImageConfig.getAliYunAccessKeyId(), ImageConfig.getAliYunAccessKeySecret());
            client.putOSS(filefullName, "shenzhen-vo", keySuffixWithSlash);
        } catch (Exception ex) {
            throw new BusinessException("1002", "上传阿里云OSS错误", ex);
        }
    }
    public void putOSS(CmsMtImageCreateFileModel modelFile) throws FileNotFoundException {
        if (modelFile.getOssState() == 0) {//上传阿里OSS
            putOSS(modelFile.getFilePath(), modelFile.getOssFilePath());
            modelFile.setOssState(1);
            daoCmsMtImageCreateFile.update(modelFile);
        }
    }
}
