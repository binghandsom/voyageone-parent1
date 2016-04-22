package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.components.liquifire.service.LiquidFireClient;
import com.voyageone.service.dao.cms.CmsMtImageCreateFileDao;
import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import com.voyageone.service.model.cms.CmsMtImageCreateTemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LiquidFireImageService {
    @Autowired
    CmsMtImageCreateFileDao daoCmsMtImageCreateFile;
    @Autowired
    CmsMtImageCreateTemplateService serviceCmsMtImageCreateTemplate;

    public void createImage(CmsMtImageCreateFileModel modelFile) throws Exception {
        CmsMtImageCreateTemplateModel modelTemplate = serviceCmsMtImageCreateTemplate.select(modelFile.getTemplateId());//获取模板
        String filePath = createImage(modelTemplate.getContent(), modelFile.getVparam(), Long.toString(modelFile.getHashCode()));//返回本地文件路径
        modelFile.setFilePath(filePath);
        modelFile.setState(1);
        daoCmsMtImageCreateFile.update(modelFile);
    }

    //调用Liquid接口创建图片
    private String createImage(String templateContent, String vparam, String fileName) throws Exception {
        try {
            LiquidFireClient client = new LiquidFireClient(ImageConfig.getLiquidFireUrl(), ImageConfig.getLiquidFireImageSavePath());
            String[] vparamList = vparam.split(",");
            String source = String.format(templateContent, vparamList);
            String fullName = client.getImage(source, fileName);
            return fullName;
        } catch (java.net.ConnectException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
