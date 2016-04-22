package com.voyageone.service.impl.cms.imagecreate;
import com.voyageone.common.util.HashCodeUtil;
import com.voyageone.components.aliyun.AliYunOSSClient;
import com.voyageone.components.liquifire.service.LiquidFireClient;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.dao.cms.CmsMtImageCreateFileDao;
import com.voyageone.service.dao.jumei.*;
import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import com.voyageone.service.model.cms.CmsMtImageCreateTemplateModel;
import com.voyageone.service.model.jumei.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsMtImageCreateFileService {
    @Autowired
    CmsMtImageCreateFileDao dao;
    @Autowired
    CmsMtImageCreateTemplateService serviceCmsMtImageCreateTemplate;

    public CmsMtImageCreateFileModel select(int id) {
        return dao.select(id);
    }

    public int update(CmsMtImageCreateFileModel entity) {
        return dao.update(entity);
    }

    public int create(CmsMtImageCreateFileModel entity) {
        return dao.insert(entity);
    }


    public Object getImage(String channelId, int templateId, String file, String vparam,String requesttQueryString,String Creater) throws Exception {
        CallResult result = new CallResult();
        long hashCode = HashCodeUtil.getHashCode(requesttQueryString);//hashCode做缓存key
        String fileName = Long.toString(hashCode);
        CmsMtImageCreateTemplateModel modelTemplate = serviceCmsMtImageCreateTemplate.select(templateId);
        String filePath = createImage(modelTemplate.getContent(), vparam, fileName);//返回本地文件路径
        final String ossFilePath = "products/" + channelId + "/" + modelTemplate.getWidth() + "x" + modelTemplate.getHeight() + "/" + Integer.toString(templateId) + "/" + file + ".jpg";
        putOSS(filePath, ossFilePath);
        CmsMtImageCreateFileModel modelFile = new CmsMtImageCreateFileModel();
        modelFile.setChannelId(channelId);
        modelFile.setVparam(vparam);
        modelFile.setTemplateId(templateId);
        modelFile.setFile(file);//文件名字
        modelFile.setHashCode(hashCode);
        modelFile.setRequestQueryString(requesttQueryString);
        modelFile.setCreated(new Date());
        modelFile.setCreater(Creater);
        modelFile.setModifier(Creater);
        modelFile.setFilePath(filePath);
        modelFile.setOssFilePath(ossFilePath);
        modelFile.setState(1);
        modelFile.setOssState(1);
        dao.insert(modelFile);
        result.setResultData(modelFile.getOssFilePath());
        return result;
    }
    private  void  putOSS(String filefullName,String keySuffixWithSlash) throws FileNotFoundException {
        AliYunOSSClient client = new AliYunOSSClient(ImageConfig.getAliYunEndpoint(), ImageConfig.getAliYunAccessKeyId(), ImageConfig.getAliYunAccessKeySecret());
        client.putOSS(filefullName,"shenzhen-vo", keySuffixWithSlash);
    }
    private String createImage(String templateContent, String vparam,String fileName) throws Exception {
        try {
            LiquidFireClient client = new LiquidFireClient(ImageConfig.getLiquidFireUrl(), ImageConfig.getLiquidFireImageSavePath());
            String[] vparamList = vparam.split(",");
            String source = String.format(templateContent, vparamList);
            String fullName = client.getImage(source, fileName);
            return fullName;
        }
        catch ( java.net.ConnectException ex)
        {
            ex.printStackTrace();
            throw  ex;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw  ex;
        }

    }
}

