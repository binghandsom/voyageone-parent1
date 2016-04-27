package com.voyageone.service.impl.cms.imagecreate;
import com.voyageone.common.Snowflake.FactoryIdWorker;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.components.aliyun.AliYunOSSClient;
import com.voyageone.service.dao.cms.CmsMtImageCreateFileDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import com.voyageone.service.bean.openapi.OpenApiException;
import com.voyageone.service.bean.openapi.image.ImageErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AliYunOSSFileService extends BaseService {
    @Autowired
    CmsMtImageCreateFileDao daoCmsMtImageCreateFile;
    private void upload(String filefullName, String keySuffixWithSlash) throws OpenApiException {
        try {
            AliYunOSSClient client = new AliYunOSSClient(ImageConfig.getAliYunEndpoint(), ImageConfig.getAliYunAccessKeyId(), ImageConfig.getAliYunAccessKeySecret());
            client.putOSS(filefullName, "shenzhen-vo", keySuffixWithSlash);
        } catch (Exception ex) {
            throw new OpenApiException(ImageErrorEnum.AliyunOSSUploadError, "上传阿里云OSS错误", ex);
        }
    }
    public void upload(CmsMtImageCreateFileModel modelFile) throws OpenApiException {
        if (modelFile.getOssState() == 0) {//上传阿里OSS
            upload(modelFile.getFilePath(), modelFile.getOssFilePath());
            modelFile.setOssState(1);
            daoCmsMtImageCreateFile.update(modelFile);
        }
    }
    public void jobUpload(int CmsMtImageCreateFileId) throws OpenApiException {
        CmsMtImageCreateFileModel modelFile = null;
        try {
            modelFile = daoCmsMtImageCreateFile.select(CmsMtImageCreateFileId);
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
            long requestId = FactoryIdWorker.nextId();//生成错误请求唯一id
            $error("jobUpload requestId:" + requestId, ex);
            issueLog.log(ex, ErrorType.OpenAPI, SubSystem.COM,"jobUpload requestId:"+requestId);
            if (modelFile != null) {
                modelFile.setErrorCode(ImageErrorEnum.SystemError.getCode());
                modelFile.setErrorMsg("requestId:" + requestId + ex.getMessage());
                daoCmsMtImageCreateFile.update(modelFile);
            }
        }
    }
}
