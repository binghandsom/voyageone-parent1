package com.voyageone.web2.cms.openapi.service;

import com.voyageone.common.idsnowflake.FactoryIdWorker;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.excel.ExcelColumn;
import com.voyageone.common.util.excel.ExcelImportUtil;
import com.voyageone.common.util.excel.ExportExcelInfo;
import com.voyageone.common.util.excel.ExportFileExcelUtil;
import com.voyageone.service.bean.openapi.OpenApiException;
import com.voyageone.service.bean.openapi.image.*;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.ImageTemplateService;
import com.voyageone.service.impl.cms.imagecreate.ImageConfig;
import com.voyageone.service.impl.cms.imagecreate.ImageCreateFileService;
import com.voyageone.service.impl.cms.imagecreate.ImagePathCache;
import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import com.voyageone.service.model.cms.CmsMtImageCreateImportModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * product Service
 *
 * @author peitao.sun 16/4/29
 * @version 2.0.1
 */
@Service
public class CmsImageFileService extends BaseService {
    @Autowired
    private ImageCreateFileService imageCreateFileService;
    @Autowired
    ImageTemplateService serviceCmsImageTemplate;
    @Autowired
    private ImagePathCache imagePathCache;

    private GetImageResultBean checkGetImageParameter(String channelId, long templateId, String file, String vparam) {
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
        if (resultBean.getSubErrorList() != null && !resultBean.getSubErrorList().isEmpty()) {
            resultBean.setEnumError(ImageErrorEnum.ParametersRequired);
        }
        return resultBean;
    }

    public GetImageResultBean getImage(String channelId, long templateId, String file, boolean isUploadUSCDN, boolean skipCache, String vparam, String creater) throws Exception {
        GetImageResultBean result = new GetImageResultBean();
        CmsMtImageCreateFileModel modelFile = null;
        boolean isCreateNewFile = false;
        try {
            result = checkGetImageParameter(channelId, templateId, file, vparam);//check
            if (result.getErrorCode() > 0) {
                return result;
            }
            CmsBtImageTemplateModel modelTemplate = serviceCmsImageTemplate.get(templateId);
            if (modelTemplate == null) {
                //模板不存在
                result.setEnumError(ImageErrorEnum.ParametersRequired);
                result.setSubEnumError(ImageErrorEnum.ImageTemplateNotNull);
                return result;
            }
            //hashCode做缓存key
            long hashCode = imageCreateFileService.getHashCode(channelId, templateId, file, vparam, modelTemplate.getTemplateModified());
            if (!skipCache) {
                String ossFilePath = imagePathCache.get(hashCode);
                if (!StringUtil.isEmpty(ossFilePath)) {
                    //图片已经生成 返回
                    GetImageResultData resultData = new GetImageResultData();
                    resultData.setFilePath(ossFilePath);
                    result.setResultData(resultData);
                    return result;
                }
            }
            //getModel
            modelFile = imageCreateFileService.getModelByHashCode(hashCode);
            if (modelFile == null) {
                //1.创建记录信息
                modelFile = imageCreateFileService.createCmsMtImageCreateFile(channelId, templateId, file, vparam, creater, hashCode, isUploadUSCDN);
            }
            //.创建并上传图片
            isCreateNewFile = imageCreateFileService.createAndUploadImage(modelFile, skipCache);
            imagePathCache.set(hashCode, modelFile.getOssFilePath());
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
            ex.printStackTrace();
            long requestId = FactoryIdWorker.nextId();
            $error("getImage requestId:" + requestId, ex);
            if (result != null) {
                result.setRequestId(requestId);
                result.setErrorCode(ImageErrorEnum.SystemError.getCode());
                result.setErrorMsg(ImageErrorEnum.SystemError.getMsg());
            }
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

        if (result.getErrorCode() > 0) {
            $info("CmsImageFileService:getImage error; params:[%s][%s][%s][%s][%s][%s],error=[%s:%s],model.id=[%s]", channelId, templateId, file, isUploadUSCDN, skipCache, vparam, result.getErrorCode(), result.getErrorMsg(), modelFile.getId());
            modelFile.setErrorMsg(result.getRequestId() + ":" + result.getErrorMsg());
            modelFile.setErrorCode(result.getErrorCode());
        } else {
            $info("CmsImageFileService:getImage OK; params:[%s][%s][%s][%s][%s][%s],model.id=[%s]", channelId, templateId, file, isUploadUSCDN, skipCache, vparam, modelFile.getId());
            result.getResultData().setFilePath(modelFile.getOssFilePath());
        }
        //保存
        imageCreateFileService.changeModel(modelFile);
        return result;
    }

    public AddListResultBean importImageCreateInfo(String path, String fileName, String Creater) throws Exception {
        CmsMtImageCreateImportModel importModel = new CmsMtImageCreateImportModel();
        importModel.setBeginTime(new Date());
        importModel.setErrorMsg("");
        importModel.setFailuresFileName("");
        importModel.setCreated(new Date());
        importModel.setModified(new Date());
        importModel.setIsImport(false);
        importModel.setErrorCode(0);
        String filePath = path + "/" + fileName;
        File excelFile = new File(filePath);
        InputStream fileInputStream = new FileInputStream(excelFile);
        HSSFWorkbook book = new HSSFWorkbook(fileInputStream);
        HSSFSheet productSheet = book.getSheet("Sheet1");
        List<CreateImageParameter> listModel = new ArrayList<>();//导入的集合
        List<Map<String, Object>> listErrorMap = new ArrayList<>();//错误行集合  导出错误文件
        List<ExcelColumn> listColumn = new ArrayList<>();    //配置列信息
        listColumn.add(new ExcelColumn("channelId", 1, "cms_mt_image_create_file", "渠道Id", false));
        listColumn.add(new ExcelColumn("templateId", 2, "cms_mt_image_create_file", "模板Id", false));
        listColumn.add(new ExcelColumn("file", 3, "cms_mt_image_create_file", "文件名", false));
        listColumn.add(new ExcelColumn("vParam", 4, "cms_mt_image_create_file", "参数Id", false));
        listColumn.add(new ExcelColumn("isUploadUsCdn", 5, "cms_mt_image_create_file", "是否上传美国Cdn", false));
        ExcelImportUtil.importSheet(productSheet, listColumn, listModel, listErrorMap, CreateImageParameter.class, 0);//

        if (listErrorMap.size() > 0 | listErrorMap.size() > 0 | listErrorMap.size() > 0) {
            //保存错误记录
            String failuresFileName = "error" + fileName;
            String errorfilePath = path + "/error" + fileName.trim();
            ExportExcelInfo info = new ExportExcelInfo(null);
            listColumn.add(info.getErrorColumn());
            info.setSheet("Sheet1");
            info.setDisplayColumnName(true);
            info.setDataSource(listErrorMap);
            info.setListColumn(listColumn);

            ExportFileExcelUtil.exportExcel(errorfilePath, info);//保存导出的错误文件
            importModel.setFailuresFileName(failuresFileName);
            importModel.setFailuresRows(listErrorMap.size());
        } else {
            importModel.setIsImport(true);
        }
        importModel.setFileName(fileName);
        importModel.setSuccessRows(listModel.size());
        importModel.setCmsMtImageCreateTaskId(0);
        importModel.setEndTime(new Date());
        importModel.setCreater(Creater);
        importModel.setModifier(Creater);
        AddListParameter parameter = new AddListParameter();
        parameter.setData(listModel);
        final AddListResultBean[] result = {null};
        result[0] = imageCreateFileService.addList(parameter, importModel);
        return result[0];
    }

    public AddListResultBean addListWithTrans(AddListParameter parameter) {
        if (parameter.getData().size() > ImageConfig.getMaxSize()) {
            //限制条数
            AddListResultBean resultBean = new AddListResultBean();
            resultBean.setErrorCode(ImageErrorEnum.ParametersOutSize.getCode());
            resultBean.setErrorMsg(ImageErrorEnum.ParametersOutSize.getMsg() + ImageConfig.getMaxSize());
            return resultBean;
        }
        return imageCreateFileService.addList(parameter, null);
    }

    public GetListResultBean getListResult(int taskId) {
        return imageCreateFileService.getListResult(taskId);
    }

}

