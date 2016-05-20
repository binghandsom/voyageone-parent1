package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.liquifire.service.LiquidFireClient;
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
public class LiquidFireImageService extends BaseService {
    @Autowired
    CmsMtImageCreateFileDao daoCmsMtImageCreateFile;

    //@Retryable(maxAttempts = 3)
    public void createImage(CmsMtImageCreateFileModel modelFile, String templateContent) throws Exception {
        try {
            String filePath = createImage(templateContent, modelFile.getVparam(), Long.toString(modelFile.getHashCode()));//返回本地文件路径
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

    //调用Liquid接口创建图片
    private String createImage(String templateContent, String vparam, String fileName) throws Exception {
        LiquidFireClient client = new LiquidFireClient(ImageConfig.getLiquidFireUrl(), ImageConfig.getLiquidFireImageSavePath());
        String source = getUrlParameter(templateContent, vparam);
        return client.getImage(source, fileName, ImageConfig.getImageProxyIP(), ImageConfig.getImageProxyPort());
    }

    private String getUrlParameter(String templateContent, String vparam) throws Exception {
        String templateContentTmp = templateContent;
        templateContentTmp = templateContentTmp.replace("\r\n", "");
        templateContentTmp = templateContentTmp.replace("\n", "");
        String[] list = JacksonUtil.ToObjectFromJson(vparam, String[].class);
        for (int i = 0; i < list.length; i++) {
            list[i] = list[i].replace("&", "＆");
        }
        return String.format(templateContentTmp, list);
    }
    public String getDownloadUrl(String templateContent, String vparam) throws Exception {
        LiquidFireClient client = new LiquidFireClient(ImageConfig.getLiquidFireUrl(), ImageConfig.getLiquidFireImageSavePath());
        String urlParameter = getUrlParameter(templateContent, vparam);
        return client.getDownloadUrl(urlParameter,"DownloadUrlFile");

    }
}
