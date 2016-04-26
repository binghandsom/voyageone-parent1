package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.components.uscdn.service.USCDNClient;
import com.voyageone.service.dao.cms.CmsMtImageCreateFileDao;
import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import com.voyageone.service.model.openapi.OpenApiException;
import com.voyageone.service.model.openapi.ProductGetImageErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
public class USCDNFileService {
    @Autowired
    CmsMtImageCreateFileDao daoCmsMtImageCreateFile;

    private void upload(String UsCdnFilePath, String localFilePath) throws OpenApiException {
        try {
            USCDNClient client = new USCDNClient(ImageConfig.getUSCDNUrl(), ImageConfig.getUSCDNUserName(), ImageConfig.getUSCDNPassword(), ImageConfig.getUSCDNWorkingDirectory());
            client.uploadFile(UsCdnFilePath, localFilePath);
        } catch (Exception ex) {
            throw new OpenApiException(ProductGetImageErrorEnum.USCDNUploadError, "上传USCDN错误", ex);
        }
    }
    public void upload(CmsMtImageCreateFileModel modelFile) throws OpenApiException {
        if (modelFile.getUscdnState() == 0) {//上传USCDN
            upload(modelFile.getUsCdnFilePath(), modelFile.getFilePath());
            modelFile.setUscdnState(1);
            daoCmsMtImageCreateFile.update(modelFile);
        }
    }
}
