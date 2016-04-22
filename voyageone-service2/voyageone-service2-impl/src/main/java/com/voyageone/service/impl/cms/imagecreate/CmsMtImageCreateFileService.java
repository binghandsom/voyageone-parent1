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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsMtImageCreateFileService {
    @Autowired
    CmsMtImageCreateFileDao dao;
    @Autowired
    CmsMtImageCreateTemplateService serviceCmsMtImageCreateTemplate;
@Autowired
    AliYunOSSFileService serviceAliYunOSSFile;
    @Autowired
    LiquidFireImageService serviceLiquidFireImage;
    public CmsMtImageCreateFileModel select(int id) {
        return dao.select(id);
    }

    public int update(CmsMtImageCreateFileModel entity) {
        return dao.update(entity);
    }

    public int create(CmsMtImageCreateFileModel entity) {
        return dao.insert(entity);
    }

    public CmsMtImageCreateFileModel getByHashCode(long hashCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("hashCode", hashCode);
        return dao.selectOne(map);
    }

    public Object getImage(String channelId, int templateId, String file, String vparam, String requesttQueryString, String Creater) throws Exception {
        CallResult result = new CallResult();
        long hashCode = HashCodeUtil.getHashCode(requesttQueryString);//hashCode做缓存key
        CmsMtImageCreateFileModel modelFile = getByHashCode(hashCode);
        CmsMtImageCreateTemplateModel modelTemplate = null;
        if (modelFile == null) {
            modelTemplate = serviceCmsMtImageCreateTemplate.select(templateId);
            final String ossFilePath = "products/" + channelId + "/" + modelTemplate.getWidth() + "x" + modelTemplate.getHeight() + "/" + Integer.toString(templateId) + "/" + file + ".jpg";
            modelFile = new CmsMtImageCreateFileModel();
            modelFile.setChannelId(channelId);
            modelFile.setVparam(vparam);
            modelFile.setTemplateId(templateId);
            modelFile.setFile(file);//文件名字
            modelFile.setHashCode(hashCode);
            modelFile.setRequestQueryString(requesttQueryString);
            modelFile.setCreated(new Date());
            modelFile.setCreater(Creater);
            modelFile.setModifier(Creater);
            modelFile.setOssFilePath(ossFilePath);
            modelFile.setState(1);
            modelFile.setOssState(1);
            dao.insert(modelFile);
        }
        serviceLiquidFireImage.createImage(modelFile);
        serviceAliYunOSSFile.putOSS(modelFile);
        result.setResultData(modelFile.getOssFilePath());
        return result;
    }




    //获取模板
    public CmsMtImageCreateTemplateModel getCmsMtImageCreateTemplate(CmsMtImageCreateTemplateModel modelTemplate, int templateId) {
        if (modelTemplate == null) {
            modelTemplate = serviceCmsMtImageCreateTemplate.select(templateId);
        }
        return modelTemplate;
    }




}

