package com.voyageone.service.impl.cms.imagecreate;
import com.voyageone.common.Snowflake.FactoryIdWorker;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.HashCodeUtil;
import com.voyageone.service.dao.cms.CmsMtImageCreateFileDao;
import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import com.voyageone.service.model.cms.CmsMtImageCreateTemplateModel;
import com.voyageone.service.model.openapi.*;
import com.voyageone.service.model.openapi.image.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsMtImageCreateFileService {
    @Autowired
    CmsMtImageCreateFileDao dao;

    @Autowired
    TransactionRunner transactionRunnerCms2;

    @Autowired
    CmsMtImageCreateTemplateService serviceCmsMtImageCreateTemplate;
    @Autowired
    AliYunOSSFileService serviceAliYunOSSFile;
    @Autowired
    USCDNFileService serviceUSCDNFile;
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

    public GetImageRespone getImage(String channelId, int templateId, String file, String vparam, String requesttQueryString, String Creater) throws Exception {
        GetImageRespone result = new GetImageRespone();
        CmsMtImageCreateFileModel modelFile = null;
        try {
            long hashCode = getHashCode(channelId, templateId, file, vparam);// HashCodeUtil.getHashCode(requesttQueryString);//hashCode做缓存key
            modelFile = getByHashCode(hashCode);
            if (modelFile == null) {//1.创建记录信息
                modelFile = createCmsMtImageCreateFile(channelId, templateId, file, vparam, Creater, hashCode);
            }
            if (modelFile.getState() == 0) { //2.生成图片
                serviceLiquidFireImage.createImage(modelFile);
            }
            if (modelFile.getOssState() == 0) { //3.上传图片到阿里云OSS
                serviceAliYunOSSFile.upload(modelFile);
            }
            if (modelFile.getChannelId().equals("001") && modelFile.getUscdnState() == 0) {//4.上传 uscdn
                serviceUSCDNFile.upload(modelFile);
            }
        } catch (OpenApiException ex) {//4.处理业务异常
            result.setErrorCode(ex.getErrorCode());
            result.setErrorMsg(ex.getMsg());
            if (ex.getSuppressed() != null) {
                long requestId = FactoryIdWorker.nextId();//生成错误请求唯一id
                LOG.error("getImage requestId:" + requestId, ex);
            }
        } catch (Exception ex) { //5.未知异常
            long requestId = FactoryIdWorker.nextId();//生成错误请求唯一id
            LOG.error("getImage requestId:" + requestId, ex);
            result.setRequestId(requestId);
            result.setErrorCode(ImageErrorEnum.SystemError.getCode());
            result.setErrorMsg(ImageErrorEnum.SystemError.getMsg());
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

    private CmsMtImageCreateFileModel createCmsMtImageCreateFile(String channelId, int templateId, String file, String vparam, String Creater, long hashCode) throws OpenApiException {
        CmsMtImageCreateTemplateModel modelTemplate = serviceCmsMtImageCreateTemplate.select(templateId);
        if (modelTemplate == null) {
            throw new OpenApiException(ImageErrorEnum.ImageTemplateNotNull, "TemplateId:" + templateId);
        }
        final String ossFilePath = "products/" + channelId + "/" + modelTemplate.getWidth() + "x" + modelTemplate.getHeight() + "/" + Integer.toString(templateId) + "/" + file + ".jpg";
        final String USCDNFilePath = ImageConfig.getUSCDNWorkingDirectory() + "/products/" + channelId + "/" + modelTemplate.getWidth() + "x" + modelTemplate.getHeight() + "/" + Integer.toString(templateId) + "/" + file + ".jpg";
        CmsMtImageCreateFileModel modelFile = new CmsMtImageCreateFileModel();
        modelFile.setChannelId(channelId);
        modelFile.setVparam(vparam);
        modelFile.setTemplateId(templateId);
        modelFile.setFile(file);//文件名字
        modelFile.setHashCode(hashCode);
        modelFile.setCreated(new Date());
        modelFile.setCreater(Creater);
        modelFile.setModifier(Creater);
        modelFile.setOssFilePath(ossFilePath);
        modelFile.setUsCdnFilePath(USCDNFilePath);
        modelFile.setState(0);
        modelFile.setOssState(0);
        dao.insert(modelFile);
        return modelFile;
    }

    //获取模板
    private CmsMtImageCreateTemplateModel getCmsMtImageCreateTemplate(CmsMtImageCreateTemplateModel modelTemplate, int templateId) {
        if (modelTemplate == null) {
            modelTemplate = serviceCmsMtImageCreateTemplate.select(templateId);
        }
        return modelTemplate;
    }

    public long getHashCode(String channelId, int templateId, String file, String vparam) {
        String parameter = channelId + templateId + file + vparam;
        return HashCodeUtil.getHashCode(parameter);
    }

    public AddListRespone addList(AddListParameter parameter) {
        AddListRespone result = new AddListRespone();
        try {
            checkAddListParameter(parameter);
            for (CreateImageParameter imageInfo : parameter.getData()) {
                long hashCode = getHashCode(imageInfo.getChannelId(), imageInfo.getTemplateId(), imageInfo.getFile(), imageInfo.getVParam());
                if (!existsHashCode(hashCode)) {//1.创建记录信息
                    createCmsMtImageCreateFile(imageInfo.getChannelId(), imageInfo.getTemplateId(), imageInfo.getFile(), imageInfo.getVParam(), "system addList", hashCode);
                }
            }
        } catch (OpenApiException ex) {
            result.setErrorCode(ex.getErrorCode());
            result.setErrorMsg(ex.getMsg());
            if (ex.getSuppressed() != null) {
                long requestId = FactoryIdWorker.nextId();//生成错误请求唯一id
                LOG.error("AddList requestId:" + requestId, ex);
            }
        } catch (Exception ex) {
            long requestId = FactoryIdWorker.nextId();//生成错误请求唯一id
            LOG.error("AddList requestId:" + requestId, ex);
            result.setRequestId(requestId);
            result.setErrorCode(ImageErrorEnum.SystemError.getCode());
            result.setErrorMsg(ImageErrorEnum.SystemError.getMsg());
        }
        return result;
    }
    public  void  checkAddListParameter(AddListParameter parameter) throws OpenApiException {
        for (CreateImageParameter imageInfo : parameter.getData()) {
            if (StringUtil.isEmpty(imageInfo.getChannelId())) {
                throw new OpenApiException(ImageErrorEnum.ChannelIdNotNull);
            }
            if (imageInfo.getTemplateId() != 0) {
                throw new OpenApiException(ImageErrorEnum.ImageTemplateNotNull);
            }
            if (StringUtil.isEmpty(imageInfo.getFile())) {
                throw new OpenApiException(ImageErrorEnum.FileNotNull);
            }
            if (StringUtil.isEmpty(imageInfo.getVParam())) {
                throw new OpenApiException(ImageErrorEnum.VParamNotNull);
            }
        }
    }
    public boolean existsHashCode(long hashCode) {
        return getByHashCode(hashCode) != null; //加缓存判断
    }
}

