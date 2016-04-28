package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.util.HashCodeUtil;
import com.voyageone.service.bean.openapi.OpenApiException;
import com.voyageone.service.bean.openapi.image.ImageErrorEnum;
import com.voyageone.service.dao.cms.CmsMtImageCreateFileDao;
import com.voyageone.service.dao.cms.CmsMtImageCreateTemplateDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import com.voyageone.service.model.cms.CmsMtImageCreateTemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chuanyu.liang on 2016/4/19.
 * @version 2.0.0
 */
@Service
public class ImageCreateFileService extends BaseService {
    @Autowired
    CmsMtImageCreateFileDao cmsMtImageCreateFileDao;

    @Autowired
    CmsMtImageCreateTemplateDao cmsMtImageCreateTemplateDao;

    @Autowired
    TransactionRunner transactionRunner;

    public CmsMtImageCreateFileModel getModel(int id) {
        return cmsMtImageCreateFileDao.select(id);
    }

    public CmsMtImageCreateFileModel getModelByHashCode(long hashCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("hashCode", hashCode);
        return cmsMtImageCreateFileDao.selectOne(map);
    }

    public boolean existsHashCode(long hashCode) {
        return getModelByHashCode(hashCode) != null; //加缓存判断
    }

    @VOTransactional
    public CmsMtImageCreateFileModel createCmsMtImageCreateFile(String channelId, int templateId, String file, String vparam, String Creater, long hashCode) throws OpenApiException {
        CmsMtImageCreateTemplateModel modelTemplate = cmsMtImageCreateTemplateDao.select(templateId);
        if (modelTemplate == null) {
            throw new OpenApiException(ImageErrorEnum.ImageTemplateNotNull, "TemplateId:" + templateId);
        }

        final String ossFilePath = getCreateFilePathName(modelTemplate, channelId, file);
        final String usCDNFilePath = ImageConfig.getUSCDNWorkingDirectory() + ossFilePath;
        CmsMtImageCreateFileModel modelFile = new CmsMtImageCreateFileModel();
        modelFile.setChannelId(channelId);
        modelFile.setVparam(vparam);
        modelFile.setTemplateId(templateId);
        modelFile.setFile(file);//文件名字
        modelFile.setHashCode(hashCode);
        modelFile.setOssFilePath(ossFilePath);
        modelFile.setUsCdnFilePath(usCDNFilePath);
        modelFile.setState(0);
        modelFile.setOssState(0);
        modelFile.setErrorCode(0);
        modelFile.setErrorMsg("");
        modelFile.setCreater(Creater);
        modelFile.setModifier(Creater);
        cmsMtImageCreateFileDao.insert(modelFile);
        return modelFile;
    }

    public String getCreateFilePathName(CmsMtImageCreateTemplateModel modelTemplate, String channelId, String file) {
        return "products/" + channelId + "/" + modelTemplate.getWidth() + "x" + modelTemplate.getHeight() + "/" + modelTemplate.getId() + "/" + file + ".jpg";
    }

    public long getHashCode(String channelId, int templateId, String file, String vparam) {
        String parameter = channelId + templateId + file + vparam;
        return HashCodeUtil.getHashCode(parameter);
    }

    @VOTransactional
    public int changeModel(CmsMtImageCreateFileModel entity) {
        return cmsMtImageCreateFileDao.update(entity);
    }


    //获取模板
    //获取模板
    public CmsMtImageCreateTemplateModel getCmsMtImageCreateTemplate(int templateId) {
        return cmsMtImageCreateTemplateDao.select(templateId);
    }
}
