package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.liquifire.service.LiquidFireClient;
import com.voyageone.service.dao.cms.CmsMtImageCreateFileDao;
import com.voyageone.service.dao.cms.CmsMtImageCreateTemplateDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import com.voyageone.service.model.cms.CmsMtImageCreateTemplateModel;
import com.voyageone.service.bean.openapi.OpenApiException;
import com.voyageone.service.bean.openapi.image.ImageErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;

@Service
@EnableRetry
public class LiquidFireImageService extends BaseService {
    @Autowired
    CmsMtImageCreateFileDao daoCmsMtImageCreateFile;
    @Autowired
    CmsMtImageCreateTemplateDao cmsMtImageCreateTemplateDao;

    @Retryable(maxAttempts = 3)
    public void createImage(CmsMtImageCreateFileModel modelFile, CmsMtImageCreateTemplateModel modelTemplate) throws Exception {
        try {
            String filePath = createImage(modelTemplate.getContent(), modelFile.getVparam(), Long.toString(modelFile.getHashCode()));//返回本地文件路径
            modelFile.setFilePath(filePath);
            modelFile.setState(1);
            //清楚报错信息
            modelFile.setErrorCode(0);
            modelFile.setErrorMsg("");
        } catch (OpenApiException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new OpenApiException(ImageErrorEnum.LiquidCreateImageError, ex);
        }
    }

//    public void createImage(int CmsMtImageCreateFileId, CmsMtImageCreateTemplateModel modelTemplate) throws Exception {
//        CmsMtImageCreateFileModel modelFile = null;
//        try {
//            modelFile = daoCmsMtImageCreateFile.select(CmsMtImageCreateFileId);
//            createImage(modelFile, modelTemplate);
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
//            $error("createImage requestId:" + requestId, ex);
//            issueLog.log(ex, ErrorType.OpenAPI, SubSystem.COM, "createImage requestId:" + requestId);
//            if (modelFile != null) {
//                modelFile.setErrorCode(ImageErrorEnum.SystemError.getCode());
//                modelFile.setErrorMsg("requestId:" + requestId + ex.getMessage());
//                daoCmsMtImageCreateFile.update(modelFile);
//            }
//        }
//    }

    //调用Liquid接口创建图片
    private String createImage(String templateContent, String vparam, String fileName) throws Exception {
        LiquidFireClient client = new LiquidFireClient(ImageConfig.getLiquidFireUrl(), ImageConfig.getLiquidFireImageSavePath());
        String[] list = JacksonUtil.ToObjectFromJson(vparam, String[].class);
        for (int i = 0; i < list.length; i++) {
            list[i] = list[i].replace("&", "＆");
        }
        String source = String.format(templateContent, list);
        return client.getImage(source, fileName, ImageConfig.getImageProxyIP(), ImageConfig.getImageProxyPort());
    }
}
