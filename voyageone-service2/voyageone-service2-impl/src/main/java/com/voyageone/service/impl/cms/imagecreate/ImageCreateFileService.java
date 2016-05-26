package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.common.idsnowflake.FactoryIdWorker;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.HashCodeUtil;
import com.voyageone.service.bean.openapi.OpenApiException;
import com.voyageone.service.bean.openapi.image.*;
import com.voyageone.service.dao.cms.*;
import com.voyageone.service.daoext.cms.CmsMtImageCreateFileDaoExt;
import com.voyageone.service.daoext.cms.CmsMtImageCreateTaskDetailDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.ImageTemplateService;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.*;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author chuanyu.liang on 2016/4/19.
 * @version 2.0.0
 */
@Service
public class ImageCreateFileService extends BaseService {
    @Autowired
    private CmsMtImageCreateFileDao cmsMtImageCreateFileDao;
    @Autowired
    private CmsMtImageCreateFileDaoExt cmsMtImageCreateFileDaoExt;
    @Autowired
    private AliYunOSSFileService serviceAliYunOSSFile;
    @Autowired
    private USCDNFileService serviceUSCDNFile;
    @Autowired
    private LiquidFireImageService serviceLiquidFireImage;

    @Autowired
    private MqSender sender;
    @Autowired
    private CmsMtImageCreateTaskDao daoCmsMtImageCreateTask;
    @Autowired
    private CmsMtImageCreateTaskDetailDao daoCmsMtImageCreateTaskDetail;
    @Autowired
    private ImagePathCache imagePathCache;
    @Autowired
    private CmsMtImageCreateTaskDetailDaoExt daoExtCmsMtImageCreateTaskDetail;
    @Autowired
    private CmsMtImageCreateImportDao daoCmsMtImageCreateImport;
    @Autowired
    private ImageTemplateService serviceCmsImageTemplate;
    @Autowired
    private CmsMtImageCreateTaskService cmsMtImageCreateTaskService;

    public CmsMtImageCreateFileModel getModel(int id) {
        return cmsMtImageCreateFileDao.select(id);
    }

    public CmsMtImageCreateFileModel getModelByHashCode(long hashCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("hashCode", hashCode);
        return cmsMtImageCreateFileDao.selectOne(map);
    }

    public boolean existsHashCode(long hashCode) {
        //加缓存判断
        return getModelByHashCode(hashCode) != null;
    }

    @VOTransactional
    public CmsMtImageCreateFileModel createCmsMtImageCreateFile(String channelId, long templateId, String file, String vparam, String Creater, long hashCode, boolean isUploadUSCDN) throws OpenApiException {

        final String ossFilePath = getCreateFilePathName(templateId, channelId, file);
        final String usCDNFilePath = ImageConfig.getUSCDNWorkingDirectory() + ossFilePath;
        CmsMtImageCreateFileModel modelFile = new CmsMtImageCreateFileModel();
        modelFile.setChannelId(channelId);
        modelFile.setVparam(vparam);
        modelFile.setTemplateId(templateId);
        modelFile.setFile(file);//文件名字
        modelFile.setHashCode(hashCode);
        modelFile.setOssFilePath(ossFilePath);
        modelFile.setUsCdnFilePath(usCDNFilePath);
        modelFile.setFilePath("");
        modelFile.setState(0);
        modelFile.setOssState(0);
        modelFile.setUscdnState(0);
        modelFile.setErrorCode(0);
        modelFile.setErrorMsg("");
        modelFile.setIsUploadUsCdn(isUploadUSCDN);
        modelFile.setCreater(Creater);
        modelFile.setModifier(Creater);
        cmsMtImageCreateFileDao.insert(modelFile);
        return modelFile;
    }

    public String getCreateFilePathName(long templateId, String channelId, String file) {
        //return "products/" + channelId + "/" + modelTemplate.getWidth() + "x" + modelTemplate.getHeight() + "/" + modelTemplate.getId() + "/" + file + ".jpg";
        return "products/" + channelId + "/" + templateId + "/" + file + ".jpg";
    }

    public long getHashCode(String channelId, long templateId, String file, String vparam, String templateModified) {
        String parameter = channelId + templateId + file + vparam + templateModified;
        return HashCodeUtil.getHashCode(parameter);
    }

    @VOTransactional
    public int changeModel(CmsMtImageCreateFileModel entity) {
        return cmsMtImageCreateFileDao.update(entity);
    }

    //CmsMtImageCreateTaskDetailModel
    public void createAndUploadImage(CmsMtImageCreateTaskDetailModel modelTaskDetail) {
        boolean isCreateNewFile = false;
        CmsMtImageCreateFileModel modelFile = null;
        int errorCode = 0;
        String errorMsg = "";
        modelTaskDetail.setBeginTime(new Date());//执行开始时间
        try {
            int CmsMtImageCreateFileId = modelTaskDetail.getCmsMtImageCreateFileId();//isUploadUSCDN
            modelFile = cmsMtImageCreateFileDao.select(CmsMtImageCreateFileId);
            //.创建并上传图片
            isCreateNewFile = createAndUploadImage(modelFile);
            imagePathCache.set(modelFile.getHashCode(), modelFile.getOssFilePath());
            $info("CmsMtImageCreateTaskJobService:createAndUploadImage result; cId:=[%s],templateId=[%s],file=[%s],vparam=[%s],filePath=[%s] model.id=[%s]",
                    modelFile.getChannelId(), modelFile.getTemplateId(), modelFile.getFile(), modelFile.getVparam(), modelFile.getOssFilePath(), modelFile.getId());
        } catch (OpenApiException ex) {
            //4.处理业务异常
            errorCode = ex.getErrorCode();
            errorMsg = ex.getMsg();
            if (ex.getSuppressed() != null) {
                long requestId = FactoryIdWorker.nextId();//生成错误请求唯一id
                errorMsg = "requestId:" + requestId + " " + errorMsg;
                $error("getImage requestId:" + requestId, ex);
            }
        } catch (Exception ex) {
            //5.未知异常
            //生成错误请求唯一id
            issueLog.log(ex, ErrorType.OpenAPI, SubSystem.COM);
            long requestId = FactoryIdWorker.nextId();
            $error("getImage requestId:" + requestId, ex);
            errorCode = ImageErrorEnum.SystemError.getCode();
            errorMsg = "requestId:" + requestId + ex.getMessage();
        } finally {
            if (modelFile != null && modelFile.getFilePath() != null && isCreateNewFile) {
                try {
                    FileUtils.delFile(modelFile.getFilePath());
                } catch (Exception ignored) {
                }
            }
        }
        if (errorCode > 0) {
            if (modelFile != null) {
                modelFile.setErrorCode(errorCode);
                modelFile.setErrorMsg(errorMsg);
                modelFile.setState(0);
            }
            modelTaskDetail.setStatus(2);
        } else {
            modelTaskDetail.setStatus(1);
        }
        if (modelFile != null) {
            modelFile.setModified(new Date());
            //一起保存
            this.changeModel(modelFile);
        }
        //执行结束时间
        modelTaskDetail.setEndTime(new Date());
        daoCmsMtImageCreateTaskDetail.update(modelTaskDetail);
    }

    public boolean createAndUploadImage(CmsMtImageCreateFileModel modelFile) throws Exception {
        boolean isCreateNewFile = false;
        if (modelFile.getState() != 1 || modelFile.getOssState() != 1 || (modelFile.getIsUploadUsCdn() && modelFile.getUscdnState() != 1)) {
            CmsBtImageTemplateModel modelTemplate = serviceCmsImageTemplate.get(modelFile.getTemplateId());//获取模板
            if (modelTemplate == null) {
                throw new OpenApiException(ImageErrorEnum.ImageTemplateNotNull, "TemplateId:" + modelFile.getTemplateId());
            }
            //2.生成图片 from LiquidFire
            serviceLiquidFireImage.createImage(modelFile, modelTemplate.getImageTemplateContent());
            $info("CmsImageFileService:getImage create image file end; cId:=[%s],templateId=[%s],file=[%s],vparam=[%s],hashCode=[%s] model.id=[%s]", modelFile.getChannelId(), modelFile.getTemplateId(), modelFile.getFile(), modelFile.getVparam(), modelFile.getHashCode(), modelFile.getId());
            isCreateNewFile = true;

            //3.上传图片到阿里云OSS
            serviceAliYunOSSFile.upload(modelFile);
            $info("CmsImageFileService:getImage upload oss image file end; cId:=[%s],templateId=[%s],file=[%s],vparam=[%s],hashCode=[%s] model.id=[%s]", modelFile.getChannelId(), modelFile.getTemplateId(), modelFile.getFile(), modelFile.getVparam(), modelFile.getHashCode(), modelFile.getId());

            //4.上传 uscdn
            if (modelFile.getIsUploadUsCdn()) {
                serviceUSCDNFile.upload(modelFile);
                $info("CmsImageFileService:getImage upload uscnd image file end; cId:=[%s],templateId=[%s],file=[%s],vparam=[%s],hashCode=[%s] model.id=[%s]", modelFile.getChannelId(), modelFile.getTemplateId(), modelFile.getFile(), modelFile.getVparam(), modelFile.getHashCode(), modelFile.getId());
            }
        }
        return isCreateNewFile;
    }

    @VOTransactional
    public AddListResultBean addList(AddListParameter parameter, CmsMtImageCreateImportModel importModel) {
        $info("CmsImageFileService:addList start");
        Map<Long, CmsBtImageTemplateModel> mapTemplate = new HashMap<>();
        AddListResultBean result = new AddListResultBean();
        try {
            checkAddListParameter(parameter, mapTemplate);
            CmsMtImageCreateTaskModel modelTask = new CmsMtImageCreateTaskModel();
            List<CmsMtImageCreateTaskDetailModel> listTaskDetail = new ArrayList<>();
            CmsMtImageCreateFileModel modelCmsMtImageCreateFile;
            for (CreateImageParameter imageInfo : parameter.getData()) {
                String templateModified = mapTemplate.get(imageInfo.getTemplateId()).getTemplateModified();//
                long hashCode = getHashCode(imageInfo.getChannelId(), imageInfo.getTemplateId(), imageInfo.getFile(), imageInfo.getVParamStr(), templateModified);
                modelCmsMtImageCreateFile = getModelByHashCode(hashCode);
                if (modelCmsMtImageCreateFile == null) {//1.创建记录信息
                    modelCmsMtImageCreateFile = createCmsMtImageCreateFile(imageInfo.getChannelId(), imageInfo.getTemplateId(), imageInfo.getFile(), imageInfo.getVParamStr(), "SYSTEM", hashCode, imageInfo.isUploadUsCdn());
                }
                CmsMtImageCreateTaskDetailModel detailModel = new CmsMtImageCreateTaskDetailModel();
                detailModel.setCmsMtImageCreateFileId(modelCmsMtImageCreateFile.getId());
                detailModel.setBeginTime(DateTimeUtil.getCreatedDefaultDate());
                detailModel.setEndTime(DateTimeUtil.getCreatedDefaultDate());
                detailModel.setCreater("");
                detailModel.setModifier("");
                detailModel.setCreated(new Date());
                detailModel.setModified(new Date());
                listTaskDetail.add(detailModel);
            }
            modelTask.setName(new Date().toString());
            modelTask.setBeginTime(DateTimeUtil.getCreatedDefaultDate());
            modelTask.setEndTime(DateTimeUtil.getCreatedDefaultDate());
            modelTask.setCreater("");
            modelTask.setModifier("");
            modelTask.setCreated(new Date());
            modelTask.setModified(new Date());
            daoCmsMtImageCreateTask.insert(modelTask);
            $info("CmsImageFileService:daoCmsMtImageCreateTask.insert end");
            if (importModel != null) {
                importModel.setCmsMtImageCreateTaskId(modelTask.getId());
                importModel.setEndTime(new Date());
                daoCmsMtImageCreateImport.insert(importModel);
                $info("CmsImageFileService:daoCmsMtImageCreateImport.insert end");
            }
            for (CmsMtImageCreateTaskDetailModel detailModel : listTaskDetail) {
                detailModel.setCmsMtImageCreateTaskId(modelTask.getId());
                detailModel.setStatus(0);
            }
            daoExtCmsMtImageCreateTaskDetail.insertList(listTaskDetail);
            $info("CmsImageFileService:daoExtCmsMtImageCreateTaskDetail.insertList end");
            Map<String, Object> map = new HashMap<>();
            map.put("id", modelTask.getId());
            sender.sendMessage(MqRoutingKey.CMS_BATCH_CmsMtImageCreateTaskJob, map);
            result.setTaskId(modelTask.getId());
            $info("CmsImageFileService:sendMessage end");
        } catch (OpenApiException ex) {
            result.setErrorCode(ex.getErrorCode());
            result.setErrorMsg(ex.getMsg());
            if (ex.getSuppressed() != null) {
                long requestId = FactoryIdWorker.nextId();//生成错误请求唯一id
                $error("AddList requestId:" + requestId, ex);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            long requestId = FactoryIdWorker.nextId();//生成错误请求唯一id
            $error("AddList requestId:" + requestId, ex);
            result.setRequestId(requestId);
            result.setErrorCode(ImageErrorEnum.SystemError.getCode());
            result.setErrorMsg(ImageErrorEnum.SystemError.getMsg());
        } finally {
            mapTemplate.clear();
        }
        $info("CmsImageFileService:addList end");
        return result;
    }

    public void checkAddListParameter(AddListParameter parameter, Map<Long, CmsBtImageTemplateModel> mapTemplate) throws OpenApiException {

        for (CreateImageParameter imageInfo : parameter.getData()) {
            imageInfo.checkInputValue();
            //判断模板是否存在
            if (!mapTemplate.containsKey(imageInfo.getTemplateId())) {
                CmsBtImageTemplateModel modelTemplate = serviceCmsImageTemplate.get(imageInfo.getTemplateId());
                if (modelTemplate == null) {
                    throw new OpenApiException(ImageErrorEnum.ImageTemplateNotNull);
                } else {
                    mapTemplate.put(modelTemplate.getImageTemplateId(), modelTemplate);
                }
            }
        }
    }

    public GetListResultBean getListResult(int taskId) {
        $info(String.format("CmsImageFileService:getListResult start[%s]", taskId));
        GetListResultBean result = new GetListResultBean();
        if (taskId == 0) {
            //限制条数
            result.setErrorCode(ImageErrorEnum.TASKIDNotNull.getCode());
            result.setErrorMsg(ImageErrorEnum.TASKIDNotNull.getMsg());
            return result;
        }
        CmsMtImageCreateTaskModel cmsMtImageCreateTaskModel = cmsMtImageCreateTaskService.get(taskId);
        if (cmsMtImageCreateTaskModel == null) {
            result.setErrorCode(ImageErrorEnum.TASKNotNull.getCode());
            result.setErrorMsg(ImageErrorEnum.TASKNotNull.getMsg());
            return result;
        }

        List<CmsMtImageCreateFileModel> cmsMtImageCreateFiles = cmsMtImageCreateFileDaoExt.selectByTaskId(taskId);
        if (cmsMtImageCreateFiles != null && !cmsMtImageCreateFiles.isEmpty()) {
            result.setCmsMtImageCreateFiles(cmsMtImageCreateFiles);
        }
        $info(String.format("CmsImageFileService:getListResult end[%s]", taskId));
        return result;
    }
}
