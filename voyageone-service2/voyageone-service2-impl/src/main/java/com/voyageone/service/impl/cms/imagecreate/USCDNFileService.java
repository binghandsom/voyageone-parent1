package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.service.bean.openapi.OpenApiException;
import com.voyageone.service.dao.cms.CmsMtImageCreateFileDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@EnableRetry
public class USCDNFileService extends BaseService {

    @Autowired
    CmsMtImageCreateFileDao daoCmsMtImageCreateFile;

    private void upload(String UsCdnFilePath, String localFilePath) throws OpenApiException {

    }

    @Retryable(maxAttempts = 3)
    public void upload(CmsMtImageCreateFileModel modelFile) throws OpenApiException {

    }

    /**
     * REMOVE LiquidFire
     * liang 2016/06/29
     */
//    private void upload(String UsCdnFilePath, String localFilePath) throws OpenApiException {
//        try {
//            USCDNClient client = new USCDNClient(ImageConfig.getUSCDNUrl(), ImageConfig.getUSCDNUserName(), ImageConfig.getUSCDNPassword(), ImageConfig.getUSCDNWorkingDirectory());
//            client.uploadFile(UsCdnFilePath, localFilePath);
//        } catch (Exception ex) {
//            throw new OpenApiException(ImageErrorEnum.USCDNUploadError, "上传USCDN错误", ex);
//        }
//    }
//
//    @Retryable(maxAttempts = 3)
//    public void upload(CmsMtImageCreateFileModel modelFile) throws OpenApiException {
//        upload(modelFile.getUsCdnFilePath(), modelFile.getFilePath());
//        modelFile.setUscdnState(1);
//        //清楚报错信息
//        modelFile.setErrorCode(0);
//        modelFile.setErrorMsg("");
//    }
}
