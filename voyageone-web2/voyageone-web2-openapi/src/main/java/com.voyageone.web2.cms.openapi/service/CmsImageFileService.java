package com.voyageone.web2.cms.openapi.service;
import com.voyageone.common.Snowflake.FactoryIdWorker;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.service.bean.openapi.OpenApiException;
import com.voyageone.service.bean.openapi.OpenApiSubError;
import com.voyageone.service.bean.openapi.image.*;
import com.voyageone.service.dao.cms.CmsMtImageCreateTaskDao;
import com.voyageone.service.dao.cms.CmsMtImageCreateTaskDetailDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.imagecreate.ImageCreateFileService;
import com.voyageone.service.impl.cms.imagecreate.ImagePathCache;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import com.voyageone.service.model.cms.CmsMtImageCreateTaskDetailModel;
import com.voyageone.service.model.cms.CmsMtImageCreateTaskModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsImageFileService extends BaseService {
    @Autowired
    private ImageCreateFileService imageCreateFileService;
    @Autowired
    private MqSender sender;
    @Autowired
    CmsMtImageCreateTaskDao daoCmsMtImageCreateTask;
    @Autowired
    CmsMtImageCreateTaskDetailDao daoCmsMtImageCreateTaskDetail;

    public GetImageResultBean checkGetImageParameter(String channelId, int templateId, String file, String vparam) {
        GetImageResultBean resultBean = new GetImageResultBean();
        if (StringUtils.isEmpty(channelId)) {
            resultBean.setSubEnumError(ImageErrorEnum.ChannelIdNotNull);
        }
        if (templateId == 0) {
            resultBean.setSubEnumError(ImageErrorEnum.ImageTemplateNotNull);
        }
        if (StringUtils.isEmpty(file)) {
            resultBean.setSubEnumError(ImageErrorEnum.FileNotNull);
        }
        if (StringUtil.isEmpty(vparam)) {
            resultBean.setSubEnumError(ImageErrorEnum.VParamNotNull);
        }
        if(resultBean.getSubErrorList()!=null&&resultBean.getSubErrorList().size()>0)
        {
            resultBean.setEnumError(ImageErrorEnum.ParametersRequired);
        }
        return resultBean;
    }
    public GetImageResultBean getImage(String channelId, int templateId, String file, boolean isUploadUSCDN, String vparam, String creater) throws Exception {

        GetImageResultBean result=null;
        CmsMtImageCreateFileModel modelFile = null;
        boolean isCreateNewFile = false;
        try {
             result = checkGetImageParameter(channelId, templateId, file, vparam);//check
            if (result.getErrorCode() > 0) {
                return result;
            }
            //hashCode做缓存key
            long hashCode = imageCreateFileService.getHashCode(channelId, templateId, file, vparam);
           String ossFilePath=ImagePathCache.get(hashCode);
            if(!StringUtil.isEmpty(ossFilePath))
            {
                GetImageResultData resultData=new GetImageResultData();
                resultData.setFilePath(ossFilePath);
                result.setResultData(resultData);
                return  result;
            }
            $info("CmsImageFileService:getImage create hashCode end; cId:=[%s],templateId=[%s],file=[%s],vparam=[%s],hashCode=[%s]", channelId, templateId, file, vparam, hashCode);
            //getModel
            modelFile = imageCreateFileService.getModelByHashCode(hashCode);
            $info("CmsImageFileService:getImage get db record end; cId:=[%s],templateId=[%s],file=[%s],vparam=[%s],hashCode=[%s] model=[%s]", channelId, templateId, file, vparam, hashCode, modelFile);
            if (modelFile == null) {
                //1.创建记录信息
                modelFile = imageCreateFileService.createCmsMtImageCreateFile(channelId, templateId, file, vparam, creater, hashCode, isUploadUSCDN);
                $info("CmsImageFileService:getImage create db record end; cId:=[%s],templateId=[%s],file=[%s],vparam=[%s],hashCode=[%s] model.id=[%s]", channelId, templateId, file, vparam, hashCode, modelFile.getId());
            }
            //.创建并上传图片
            isCreateNewFile = imageCreateFileService.createAndUploadImage(modelFile);
            ImagePathCache.set(hashCode,modelFile.getOssFilePath());
        } catch (OpenApiException ex) {
            //4.处理业务异常
            result.setErrorCode(ex.getErrorCode());
            result.setErrorMsg(ex.getMsg());
            if (ex.getSuppressed() != null) {
                long requestId = FactoryIdWorker.nextId();//生成错误请求唯一id
                $error("getImage requestId:" + requestId, ex);
            }
        } catch (Exception ex) {
            //5.未知异常
            long requestId = FactoryIdWorker.nextId();
            $error("getImage requestId:" + requestId, ex);
            result.setRequestId(requestId);
            result.setErrorCode(ImageErrorEnum.SystemError.getCode());
            result.setErrorMsg(ImageErrorEnum.SystemError.getMsg());
            //生成错误请求唯一id
            issueLog.log(ex, ErrorType.OpenAPI, SubSystem.COM);
        } finally {
            if (modelFile != null && modelFile.getFilePath() != null && isCreateNewFile) {
                try {
                    FileUtils.delFile(modelFile.getFilePath());
                } catch (Exception ignored) {
                }
            }
        }
        if (modelFile == null) return result;
        if (result.getErrorCode() > 0) {//6.保存报错错误信息
            $info("CmsImageFileService:getImage error result; cId:=[%s],templateId=[%s],file=[%s],vparam=[%s],error=[%s:%s] model.id=[%s]", channelId, templateId, file, vparam, result.getErrorCode(), result.getErrorMsg(), modelFile.getId());
            modelFile.setErrorMsg(result.getRequestId() + ":" + result.getErrorMsg());
            modelFile.setErrorCode(result.getErrorCode());
        } else {
            $info("CmsImageFileService:getImage ok result; cId:=[%s],templateId=[%s],file=[%s],vparam=[%s],filePath=[%s] model.id=[%s]", channelId, templateId, file, vparam, modelFile.getOssFilePath(), modelFile.getId());
            result.getResultData().setFilePath(modelFile.getOssFilePath());
        }
        imageCreateFileService.changeModel(modelFile);//保存
        return result;
    }



    public AddListResultBean addList(AddListParameter parameter) {
        AddListResultBean result = new AddListResultBean();
        try {
            checkAddListParameter(parameter);
            //保存事务待处理 超过一秒加事务
            CmsMtImageCreateTaskModel modelTask=new CmsMtImageCreateTaskModel();
            List<CmsMtImageCreateTaskDetailModel> listTaskDetail=new ArrayList<>();
            CmsMtImageCreateFileModel modelCmsMtImageCreateFile=null;
            for (CreateImageParameter imageInfo : parameter.getData()) {
                long hashCode = imageCreateFileService.getHashCode(imageInfo.getChannelId(), imageInfo.getTemplateId(), imageInfo.getFile(), imageInfo.getVParam());
                modelCmsMtImageCreateFile = imageCreateFileService.getModelByHashCode(hashCode);
                if (modelCmsMtImageCreateFile==null) {//1.创建记录信息
                   modelCmsMtImageCreateFile = imageCreateFileService.createCmsMtImageCreateFile(imageInfo.getChannelId(), imageInfo.getTemplateId(), imageInfo.getFile(), imageInfo.getVParam(), "system addList", hashCode,imageInfo.isUploadUsCdn());
                }
                CmsMtImageCreateTaskDetailModel detailModel = new CmsMtImageCreateTaskDetailModel();
                detailModel.setCmsMtImageCreateFileId(modelCmsMtImageCreateFile.getId());
                detailModel.setBeginTime(DateTimeUtil.getCreatedDefaultDate());
                detailModel.setEndTime(DateTimeUtil.getCreatedDefaultDate());
                detailModel.setCreater("");
                detailModel.setModifier("");
                detailModel.setCreated(new Date().toString());
                detailModel.setModified(new Date().toString());
                listTaskDetail.add(detailModel);
            }
            modelTask.setName(new Date().toString());
            modelTask.setBeginTime(DateTimeUtil.getCreatedDefaultDate());
            modelTask.setEndTime(DateTimeUtil.getCreatedDefaultDate());
            modelTask.setCreater("");
            modelTask.setModifier("");
            modelTask.setCreated(new Date().toString());
            modelTask.setModified(new Date().toString());
            daoCmsMtImageCreateTask.insert(modelTask);
            for (CmsMtImageCreateTaskDetailModel detailModel:listTaskDetail)
            {
                detailModel.setCmsMtImageCreateTaskId(modelTask.getId());
                daoCmsMtImageCreateTaskDetail.insert(detailModel);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("id",modelTask.getId());
            sender.sendMessage(MqRoutingKey.CMS_BATCH_CmsMtImageCreateTaskJob,map);
        } catch (OpenApiException ex) {
            result.setErrorCode(ex.getErrorCode());
            result.setErrorMsg(ex.getMsg());
            if (ex.getSuppressed() != null) {
                long requestId = FactoryIdWorker.nextId();//生成错误请求唯一id
                $error("AddList requestId:" + requestId, ex);
            }
        } catch (Exception ex) {
            long requestId = FactoryIdWorker.nextId();//生成错误请求唯一id
            $error("AddList requestId:" + requestId, ex);
            result.setRequestId(requestId);
            result.setErrorCode(ImageErrorEnum.SystemError.getCode());
            result.setErrorMsg(ImageErrorEnum.SystemError.getMsg());
        }
        return result;
    }

    public  void  checkAddListParameter(AddListParameter parameter) throws OpenApiException {
        if(parameter.getData().size()>100)
        {
           //throw  new OpenApiException("");
        }
        for (CreateImageParameter imageInfo : parameter.getData()) {
            if (StringUtil.isEmpty(imageInfo.getChannelId())) {
                throw new OpenApiException(ImageErrorEnum.ChannelIdNotNull);
            }
            if (imageInfo.getTemplateId() == 0) {
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
}

