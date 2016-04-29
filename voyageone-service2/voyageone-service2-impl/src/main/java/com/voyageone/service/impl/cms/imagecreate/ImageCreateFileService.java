package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.common.Snowflake.FactoryIdWorker;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.HashCodeUtil;
import com.voyageone.service.bean.openapi.OpenApiException;
import com.voyageone.service.bean.openapi.image.ImageErrorEnum;
import com.voyageone.service.dao.cms.CmsMtImageCreateFileDao;
import com.voyageone.service.dao.cms.CmsMtImageCreateTaskDetailDao;
import com.voyageone.service.dao.cms.CmsMtImageCreateTemplateDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import com.voyageone.service.model.cms.CmsMtImageCreateTaskDetailModel;
import com.voyageone.service.model.cms.CmsMtImageCreateTemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    private AliYunOSSFileService serviceAliYunOSSFile;
    @Autowired
    private USCDNFileService serviceUSCDNFile;
    @Autowired
    private LiquidFireImageService serviceLiquidFireImage;
    @Autowired
    TransactionRunner transactionRunner;
    @Autowired
    CmsMtImageCreateTaskDetailDao daoCmsMtImageCreateTaskDetail;
    @Autowired
    ImagePathCache imagePathCache;


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
    public CmsMtImageCreateFileModel createCmsMtImageCreateFile(String channelId, int templateId, String file, String vparam, String Creater, long hashCode, boolean isUploadUSCDN) throws OpenApiException {
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
        modelFile.setFilePath("");
        modelFile.setState(0);
        modelFile.setOssState(0);
        modelFile.setErrorCode(0);
        modelFile.setErrorMsg("");
        modelFile.setUploadUsCdn(isUploadUSCDN);
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
            imagePathCache.set(modelFile.getHashCode(),modelFile.getOssFilePath());
            $info("CmsMtImageCreateTaskJobService:onStartup ok result; cId:=[%s],templateId=[%s],file=[%s],vparam=[%s],filePath=[%s] model.id=[%s]",
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
            this.changeModel(modelFile);//一起保存
        }
        modelTaskDetail.setEndTime(new Date());//执行结束时间
        daoCmsMtImageCreateTaskDetail.update(modelTaskDetail);
    }

    public boolean createAndUploadImage(CmsMtImageCreateFileModel modelFile) throws Exception {
        boolean isCreateNewFile = false;
        if (modelFile.getState() == 0) {
            CmsMtImageCreateTemplateModel modelTemplate = this.getCmsMtImageCreateTemplate(modelFile.getTemplateId());//获取模板
            if (modelTemplate == null) {
                throw new OpenApiException(ImageErrorEnum.ImageTemplateNotNull, "TemplateId:" + modelFile.getTemplateId());
            }
            $info("CmsImageFileService:getImage get template end; cId:=[%s],templateId=[%s],file=[%s],vparam=[%s],hashCode=[%s] model.id=[%s]", modelFile.getChannelId(), modelFile.getTemplateId(), modelFile.getFile(), modelFile.getVparam(), modelFile.getHashCode(), modelFile.getId());
            //2.生成图片 from LiquidFire
            serviceLiquidFireImage.createImage(modelFile, modelTemplate);
            $info("CmsImageFileService:getImage create image file end; cId:=[%s],templateId=[%s],file=[%s],vparam=[%s],hashCode=[%s] model.id=[%s]", modelFile.getChannelId(), modelFile.getTemplateId(), modelFile.getFile(), modelFile.getVparam(), modelFile.getHashCode(), modelFile.getId());
            isCreateNewFile = true;
        }
        if (modelFile.getOssState() == 0) {
            //3.上传图片到阿里云OSS
            serviceAliYunOSSFile.upload(modelFile);
            $info("CmsImageFileService:getImage upload oss image file end; cId:=[%s],templateId=[%s],file=[%s],vparam=[%s],hashCode=[%s] model.id=[%s]", modelFile.getChannelId(), modelFile.getTemplateId(), modelFile.getFile(), modelFile.getVparam(), modelFile.getHashCode(), modelFile.getId());
        }
        if (modelFile.isUploadUsCdn() && modelFile.getUscdnState() == 0) {
            //4.上传 uscdn
            serviceUSCDNFile.upload(modelFile);
            $info("CmsImageFileService:getImage upload uscnd image file end; cId:=[%s],templateId=[%s],file=[%s],vparam=[%s],hashCode=[%s] model.id=[%s]", modelFile.getChannelId(), modelFile.getTemplateId(), modelFile.getFile(), modelFile.getVparam(), modelFile.getHashCode(), modelFile.getId());
        }
        return isCreateNewFile;
    }
}
