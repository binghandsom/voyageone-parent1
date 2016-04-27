package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.common.Snowflake.FactoryIdWorker;
import com.voyageone.components.uscdn.service.USCDNClient;
import com.voyageone.service.dao.cms.CmsMtImageCreateFileDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import com.voyageone.service.model.openapi.OpenApiException;
import com.voyageone.service.model.openapi.image.ImageErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class USCDNFileService extends BaseService {

    @Autowired
    CmsMtImageCreateFileDao daoCmsMtImageCreateFile;
    private void upload(String UsCdnFilePath, String localFilePath) throws OpenApiException {
        try {
            USCDNClient client = new USCDNClient(ImageConfig.getUSCDNUrl(), ImageConfig.getUSCDNUserName(), ImageConfig.getUSCDNPassword(), ImageConfig.getUSCDNWorkingDirectory());
            client.uploadFile(UsCdnFilePath, localFilePath);
        } catch (Exception ex) {
            throw new OpenApiException(ImageErrorEnum.USCDNUploadError, "上传USCDN错误", ex);
        }
    }
    public void upload(CmsMtImageCreateFileModel modelFile) throws OpenApiException {
        if (modelFile.getUscdnState() == 0) {//上传USCDN
            upload(modelFile.getUsCdnFilePath(), modelFile.getFilePath());
            modelFile.setUscdnState(1);
            daoCmsMtImageCreateFile.update(modelFile);
        }
    }
    public void jobUpload(int CmsMtImageCreateFileRowId) {
        CmsMtImageCreateFileModel modelFile = null;
        try {
            modelFile = daoCmsMtImageCreateFile.select(CmsMtImageCreateFileRowId);
            upload(modelFile);
        } catch (OpenApiException ex)//业务异常
        {
            if (modelFile != null) {
                modelFile.setErrorCode(ex.getErrorCode());
                modelFile.setErrorMsg(ex.getMsg());
                daoCmsMtImageCreateFile.update(modelFile);
            }
        } catch (Exception ex)//未知异常
        {
            long requestId= FactoryIdWorker.nextId();
            $error("jobUpload",ex);

        }
    }
}
