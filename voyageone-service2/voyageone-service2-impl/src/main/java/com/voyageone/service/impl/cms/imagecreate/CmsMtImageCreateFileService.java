package com.voyageone.service.impl.cms.imagecreate;
import com.voyageone.common.Snowflake.FactoryIdWorker;
import com.voyageone.common.util.HashCodeUtil;
import com.voyageone.components.aliyun.AliYunOSSClient;
import com.voyageone.components.liquifire.service.LiquidFireClient;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.dao.cms.CmsMtImageCreateFileDao;
import com.voyageone.service.dao.jumei.*;
import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import com.voyageone.service.model.cms.CmsMtImageCreateTemplateModel;
import com.voyageone.service.model.jumei.*;
import com.voyageone.service.model.openapi.OpenApiException;
import com.voyageone.service.model.openapi.ProductGetImageErrorEnum;
import com.voyageone.service.model.openapi.ProductGetImageRespone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(CmsMtImageCreateFileService.class);
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

    public ProductGetImageRespone getImage(String channelId, int templateId, String file, String vparam, String requesttQueryString, String Creater) throws Exception {
        ProductGetImageRespone result = new ProductGetImageRespone();
        CmsMtImageCreateFileModel modelFile = null;
        try {
            long hashCode = HashCodeUtil.getHashCode(requesttQueryString);//hashCode做缓存key
            modelFile = getByHashCode(hashCode);
            if (modelFile == null) {//1.创建记录信息
                modelFile = createCmsMtImageCreateFile(channelId, templateId, file, vparam, requesttQueryString, Creater, modelFile, hashCode);
            }
            if (modelFile.getState() == 0) { //2.生成图片
                serviceLiquidFireImage.createImage(modelFile);
            }
            if (modelFile.getOssState() == 0) { //3.上传图片到阿里云OSS
                serviceAliYunOSSFile.putOSS(modelFile);
            }
        } catch (OpenApiException ex) {//4.处理业务异常
            result.setErrorCode(ex.getErrorCode());
            result.setErrorMsg(ex.getMsg());
        } catch (Exception ex) { //5.未知异常
            long requestId = FactoryIdWorker.nextId();//生成错误请求唯一id
            LOG.error("getImage requestId:" + requestId, ex);
            result.setRequestId(requestId);
            result.setErrorCode(ProductGetImageErrorEnum.SystemError.getCode());
            result.setErrorMsg(ProductGetImageErrorEnum.SystemError.getMsg());
        }
        if (result.getErrorCode() > 0) {//6.保存报错错误信息
            modelFile.setErrorMsg(result.getRequestId() + ":" + result.getErrorMsg());
            modelFile.setErrorCode(result.getErrorCode());
            dao.update(modelFile);
        } else {
            result.getResultData().setOSSFilePath(modelFile.getOssFilePath());
            result.getResultData().setUsCDNFilePath(modelFile.getUsCdnFilePath());
        }
        return result;
    }

    private CmsMtImageCreateFileModel createCmsMtImageCreateFile(String channelId, int templateId, String file, String vparam, String requesttQueryString, String Creater, CmsMtImageCreateFileModel modelFile, long hashCode) {
        CmsMtImageCreateTemplateModel modelTemplate = serviceCmsMtImageCreateTemplate.select(templateId);
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
        modelFile.setState(0);
        modelFile.setOssState(0);
        dao.insert(modelFile);
        return modelFile;
    }

    //获取模板
    public CmsMtImageCreateTemplateModel getCmsMtImageCreateTemplate(CmsMtImageCreateTemplateModel modelTemplate, int templateId) {
        if (modelTemplate == null) {
            modelTemplate = serviceCmsMtImageCreateTemplate.select(templateId);
        }
        return modelTemplate;
    }
}

