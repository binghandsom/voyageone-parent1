package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.components.liquifire.service.LiquidFireClient;
import com.voyageone.service.dao.cms.CmsMtImageCreateFileDao;
import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import com.voyageone.service.model.cms.CmsMtImageCreateTemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class LiquidFireImageService {
    @Autowired
    CmsMtImageCreateFileDao daoCmsMtImageCreateFile;
    @Autowired
    CmsMtImageCreateTemplateService serviceCmsMtImageCreateTemplate;

    public void createImage(CmsMtImageCreateFileModel modelFile) throws Exception {
        try {
            CmsMtImageCreateTemplateModel modelTemplate = serviceCmsMtImageCreateTemplate.select(modelFile.getTemplateId());//获取模板
            Assert.notNull(modelTemplate,(modelFile.getTemplateId()+"生成模板找不到"));
            String filePath = createImage(modelTemplate.getContent(), modelFile.getVparam(), Long.toString(modelFile.getHashCode()));//返回本地文件路径
            modelFile.setFilePath(filePath);
            modelFile.setState(1);
            daoCmsMtImageCreateFile.update(modelFile);
        }
        catch (IllegalArgumentException ex)
        {
            throw  new BusinessException("100101",ex.getMessage(),ex);
        }
        catch (Exception ex)
        {
            throw  new BusinessException("1001","生成图片错误",ex);
        }

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
