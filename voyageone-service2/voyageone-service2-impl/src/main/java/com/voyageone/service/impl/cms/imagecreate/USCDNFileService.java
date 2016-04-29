package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.components.uscdn.service.USCDNClient;
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

    @Retryable(maxAttempts = 3)
    public void upload(CmsMtImageCreateFileModel modelFile) throws OpenApiException {
        //上传USCDN
        if (modelFile.getUscdnState() == 0) {
            upload(modelFile.getUsCdnFilePath(), modelFile.getFilePath());
            modelFile.setUscdnState(1);
            //清楚报错信息
            modelFile.setErrorCode(0);
            modelFile.setErrorMsg("");
        }
    }

//    public void jobUpload(int CmsMtImageCreateFileRowId) {
//        CmsMtImageCreateFileModel modelFile = null;
//        try {
//            modelFile = daoCmsMtImageCreateFile.select(CmsMtImageCreateFileRowId);
//            upload(modelFile);
//        } catch (OpenApiException ex) {
//            //业务异常
//            if (modelFile != null) {
//                modelFile.setErrorCode(ex.getErrorCode());
//                modelFile.setErrorMsg(ex.getMsg());
//                daoCmsMtImageCreateFile.update(modelFile);
//            }
//        } catch (Exception ex) {
//            //未知异常
//            long requestId = FactoryIdWorker.nextId();//生成错误请求唯一id
//            $error("jobUpload requestId:" + requestId, ex);
//            issueLog.log(ex, ErrorType.OpenAPI, SubSystem.COM, "jobUpload requestId:" + requestId);
//            if (modelFile != null) {
//                modelFile.setErrorCode(ImageErrorEnum.SystemError.getCode());
//                modelFile.setErrorMsg("requestId:" + requestId + ex.getMessage());
//                daoCmsMtImageCreateFile.update(modelFile);
//            }
//        }
//    }
}
