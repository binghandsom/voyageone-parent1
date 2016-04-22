package com.voyageone.service.impl.cms.imagecreate;

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
        AliYunOSSClient client = new AliYunOSSClient(ImageConfig.getAliYunEndpoint(), ImageConfig.getAliYunAccessKeyId(), ImageConfig.getAliYunAccessKeySecret());
        client.putOSS(filefullName, "shenzhen-vo", keySuffixWithSlash);
    }
    public void putOSS(CmsMtImageCreateFileModel modelFile) throws FileNotFoundException {
        if (modelFile.getOssState() == 0) {//上传阿里OSS
            putOSS(modelFile.getFilePath(), modelFile.getOssFilePath());
            modelFile.setOssState(1);
            daoCmsMtImageCreateFile.update(modelFile);
        }
    }
}
