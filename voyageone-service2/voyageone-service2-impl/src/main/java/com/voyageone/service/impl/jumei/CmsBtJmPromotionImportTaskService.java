package com.voyageone.service.impl.jumei;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.service.dao.jumei.*;
import com.voyageone.service.daoext.jumei.*;
import com.voyageone.service.impl.Excel.ExcelColumn;
import com.voyageone.service.impl.Excel.ExcelImportUtil;
import com.voyageone.service.impl.jumei.enumjm.EnumJMProductImportColumn;
import com.voyageone.service.impl.jumei.enumjm.EnumJMSkuImportColumn;
import com.voyageone.service.impl.jumei.enumjm.EnumJMSpecialImageImportColumn;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.jumei.*;
import com.voyageone.service.model.jumei.businessmodel.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.velocity.util.ArrayListWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotionImportTaskService {
    private static final int PlatformId = 27;
    @Autowired
    CmsBtJmPromotionImportTaskDao dao;
    @Autowired
    CmsBtJmPromotionImportTaskDaoExt daoExt;
    @Autowired
    CmsBtJmProductDao daoCmsBtJmProduct;
    @Autowired
    CmsBtJmSkuDao daoCmsBtJmSku;
    @Autowired
    CmsBtJmPromotionProductDao daoCmsBtJmPromotionProduct;
    @Autowired
    CmsBtJmPromotionSkuDao daoCmsBtJmPromotionSku;
    @Autowired
    CmsMtMasterInfoDao daoCmsMtMasterInfo;
    @Autowired
    CmsBtJmProductImagesDao daoCmsBtJmProductImages;
    @Autowired
    CmsBtJmProductService serviceCmsBtJmProduct;
    @Autowired
    CmsBtJmSkuService serviceCmsBtJmSku;
    @Autowired
    CmsBtJmPromotionProductService serviceCmsBtJmPromotionProduct;
    @Autowired
    CmsBtJmPromotionSkuService serviceCmsBtJmPromotionSku;
    @Autowired
    CmsBtJmProductDaoExt daoExtCmsBtJmProduct;
    @Autowired
    CmsBtJmSkuDaoExt daoExtCmsBtJmSku;
    @Autowired
    CmsBtJmPromotionProductDaoExt daoExtCmsBtJmPromotionProduct;
    @Autowired
    CmsBtJmPromotionSkuDaoExt daoExtCmsBtJmPromotionSku;
    @Autowired
    CmsBtJmPromotionService serviceCmsBtJmPromotion;
    @Autowired
    CmsMtTemplateImagesDaoExt daoExtCmsMtTemplateImages;
    @Autowired
    CmsMtMasterInfoDaoExt daoExtCmsMtMasterInfo;
    @Autowired
    CmsBtJmProductImagesDaoExt daoExtCmsBtJmProductImages;
    @Autowired
    CmsBtJmPromotionExportTaskService serviceCmsBtJmPromotionExportTask;

    public CmsBtJmPromotionImportTaskModel get(int id) {
        return dao.select(id);
    }

    public int update(CmsBtJmPromotionImportTaskModel entity) {
        return dao.update(entity);
    }

    public int create(CmsBtJmPromotionImportTaskModel entity) {
        return dao.insert(entity);
    }

    public void importFile(int JmBtPromotionImportTaskId) throws Exception {
        String errorMsg = "";
        boolean isError = false;
        CmsBtJmPromotionImportTaskModel modelCmsBtJmPromotionImportTask = dao.select(JmBtPromotionImportTaskId);
        modelCmsBtJmPromotionImportTask.setBeginTime(new Date());
        try {
            importExcel(modelCmsBtJmPromotionImportTask);
        } catch (Exception ex) {
            ex.printStackTrace();
            modelCmsBtJmPromotionImportTask.setErrorCode(1);
            modelCmsBtJmPromotionImportTask.setErrorMsg(ex.getMessage() + ex.getStackTrace());
            if(ex.getStackTrace().length>0)
            {
                modelCmsBtJmPromotionImportTask.setErrorMsg(modelCmsBtJmPromotionImportTask.getErrorMsg()+ex.getStackTrace()[0].toString());
            }
        }
        modelCmsBtJmPromotionImportTask.setIsImport(true);
        modelCmsBtJmPromotionImportTask.setEndTime(new Date());
        dao.update(modelCmsBtJmPromotionImportTask);
    }

    private void importExcel(CmsBtJmPromotionImportTaskModel modelCmsBtJmPromotionImportTask) throws Exception {
        boolean isError;
        CmsBtJmPromotionModel modelCmsBtJmPromotion = serviceCmsBtJmPromotion.select(modelCmsBtJmPromotionImportTask.getCmsBtJmPromotionId());
        modelCmsBtJmPromotionImportTask.setBeginTime(new Date());
        String filePath = "/usr/JMImport/" + modelCmsBtJmPromotionImportTask.getFileName().trim();//"/Product20160324164706.xls";
        File excelFile = new File(filePath);
        InputStream fileInputStream = null;
        fileInputStream = new FileInputStream(excelFile);
        HSSFWorkbook book = null;
        book = new HSSFWorkbook(fileInputStream);
        HSSFSheet productSheet = book.getSheet("Product");
        List<CmsBtJmImportProduct> listProductModel = new ArrayList<>();//导入的集合
        List<Map<String, Object>> listProducctErrorMap = new ArrayList<>();//错误行集合  导出错误文件
        List<ExcelColumn> listProductColumn = EnumJMProductImportColumn.getListExcelColumn();//配置列信息
        ExcelImportUtil.importSheet(productSheet, listProductColumn, listProductModel, listProducctErrorMap, CmsBtJmImportProduct.class);

        HSSFSheet skuSheet = book.getSheet("Sku");
        List<CmsBtJmImportSku> listSkuModel = new ArrayList<>();
        List<Map<String, Object>> listSkuErrorMap = new ArrayList<>();
        List<ExcelColumn> listSkuColumn = EnumJMSkuImportColumn.getListExcelColumn();
        ExcelImportUtil.importSheet(skuSheet, listSkuColumn, listSkuModel, listSkuErrorMap, CmsBtJmImportSku.class);

        HSSFSheet specialImageSheet = book.getSheet("SpecialImage");
        List<CmsBtJmImportSpecialImage> listSpecialImageModel = new ArrayList<>();
        List<Map<String, Object>> listSpecialErrorMap = new ArrayList<>();
        List<ExcelColumn> listSpecialColumn = EnumJMSpecialImageImportColumn.getListExcelColumn();
        ExcelImportUtil.importSheet(specialImageSheet, listSpecialColumn, listSpecialImageModel, listSpecialErrorMap, CmsBtJmImportSpecialImage.class);

        // List<CmsMtMasterInfoModel> listSaveCmsMtMasterInfoModel=new ArrayList<>();
        // List<CmsBtJmProductImportSaveInfo> listCmsBtJmProductImportSaveInfo=new ArrayList<>();
        CmsBtJmImportSaveInfo saveImportInfo = loadListSaveInfo(modelCmsBtJmPromotionImportTask, modelCmsBtJmPromotion, listProductModel, listSkuModel, listSpecialImageModel);
        saveListCmsBtJmProductImportSaveInfo(saveImportInfo);//保存

        if (listProducctErrorMap.size() > 0 | listSkuErrorMap.size() > 0 | listSpecialErrorMap.size() > 0) {
            String failuresFileName = "error" + modelCmsBtJmPromotionImportTask.getFileName().trim();
            String errorfilePath = "/usr/JMExport/error" + modelCmsBtJmPromotionImportTask.getFileName().trim();
            serviceCmsBtJmPromotionExportTask.export(errorfilePath, listProducctErrorMap, listSkuErrorMap, listSpecialErrorMap, true);
            modelCmsBtJmPromotionImportTask.setFailuresFileName(failuresFileName);
            modelCmsBtJmPromotionImportTask.setErrorCode(2);
            modelCmsBtJmPromotionImportTask.setFailuresRows(listProducctErrorMap.size());
        }
        modelCmsBtJmPromotionImportTask.setSuccessRows(listProductModel.size());
    }

    private void saveListCmsBtJmProductImportSaveInfo(CmsBtJmImportSaveInfo saveImportInfo) {
        Map<String, Integer> mapSkuCodeId = new HashMap<>();
        for (CmsBtJmProductImportSaveInfo saveInfo : saveImportInfo.getListProductSaveInfo()) {
            if (saveInfo.getProductModel().getId() == 0) {
                daoCmsBtJmProduct.insert(saveInfo.getProductModel());
            } else {
                daoCmsBtJmProduct.update(saveInfo.getProductModel());
            }
            saveInfo.getPromotionProductModel().setCmsBtJmProductId(saveInfo.getProductModel().getId());
            if (saveInfo.getPromotionProductModel().getId() == 0) {
                daoCmsBtJmPromotionProduct.insert(saveInfo.getPromotionProductModel());
            } else {
                daoCmsBtJmPromotionProduct.update(saveInfo.getPromotionProductModel());
            }
            for (CmsBtJmSkuModel skuModel : saveInfo.getListSkuModel()) {
                skuModel.setCmsBtJmProductId(saveInfo.getProductModel().getId());
                if (skuModel.getId() == 0) {
                    daoCmsBtJmSku.insert(skuModel);
                } else {
                    daoCmsBtJmSku.update(skuModel);
                }
                mapSkuCodeId.put(skuModel.getSkuCode(), skuModel.getId());
            }
            for (CmsBtJmPromotionSkuModel skuPromotionModel : saveInfo.getListPromotionSkuModel()) {
                skuPromotionModel.setCmsBtJmProductId(saveInfo.getProductModel().getId());
                skuPromotionModel.setCmsBtJmSkuId(mapSkuCodeId.get(skuPromotionModel.getSkuCode()));
                if (skuPromotionModel.getId() == 0) {
                    daoCmsBtJmPromotionSku.insert(skuPromotionModel);
                } else {
                    daoCmsBtJmPromotionSku.update(skuPromotionModel);
                }
            }
            for (CmsBtJmProductImagesModel model : saveInfo.getListCmsBtJmProductImagesModel()) {
                model.setCmsBtJmProductId(saveInfo.getProductModel().getId());
                if (model.getId() == 0) {
                    daoCmsBtJmProductImages.insert(model);
                } else {
                    daoCmsBtJmProductImages.update(model);
                }
            }
            mapSkuCodeId.clear();
        }
        for (CmsMtMasterInfoModel model : saveImportInfo.getMapSaveCmsMtMasterInfoModel().values()) {
            if (model.getId() == 0) {
                daoCmsMtMasterInfo.insert(model);
            } else {
                daoCmsMtMasterInfo.update(model);
            }
        }
    }

    private CmsBtJmImportSaveInfo loadListSaveInfo(CmsBtJmPromotionImportTaskModel modelCmsBtJmPromotionImportTask, CmsBtJmPromotionModel modelCmsBtJmPromotion, List<CmsBtJmImportProduct> listProductModel, List<CmsBtJmImportSku> listSkuModel, List<CmsBtJmImportSpecialImage> listSpecialImageModel) {
        CmsBtJmImportSaveInfo saveImportInfo=new CmsBtJmImportSaveInfo();
        List<CmsBtJmProductImportSaveInfo> listCmsBtJmProductImportSaveInfo = new ArrayList<>();
        List<CmsMtMasterInfoModel> listSaveCmsMtMasterInfoModel=new ArrayList<>();
        saveImportInfo.setListProductSaveInfo(listCmsBtJmProductImportSaveInfo);
        CmsBtJmProductImportSaveInfo saveInfo = null;
        CmsBtJmPromotionProductModel modelCmsBtJmPromotionProduct = null;
        CmsBtJmProductModel oldProductModel = null;
        for (CmsBtJmImportProduct importProductModel : listProductModel) {
            saveInfo = new CmsBtJmProductImportSaveInfo();
            //商品
            oldProductModel = daoExtCmsBtJmProduct.getByProductCodeChannelId(importProductModel.getProductCode(), modelCmsBtJmPromotion.getChannelId());
            importProductModel.setChannelId(modelCmsBtJmPromotion.getChannelId());
            if (oldProductModel != null) {
                if (oldProductModel.getState() == 0) {//未上新 全覆盖
                    importProductModel.setId(oldProductModel.getId());
                    saveInfo.setProductModel(importProductModel);//商品
                } else //已上新   待确认?
                {
                    loadOldProductModel(oldProductModel, importProductModel);//处理需要覆盖的字段
                    saveInfo.setProductModel(oldProductModel);
                }
            } else {
                saveInfo.setProductModel(importProductModel);//商品
            }
            //1.活动商品  cms_bt_jm_promotion_product
            loadSavePromotionProductInfoByImportModel(modelCmsBtJmPromotionImportTask, modelCmsBtJmPromotion, saveInfo, oldProductModel, importProductModel);

            //2.  cms_bt_jm_promotion_sku  cms_bt_jm_sku
            List<CmsBtJmImportSku> productCmsBtJmImportSkuList = getListCmsBtJmImportSkuByProductCode(listSkuModel, importProductModel.getProductCode());
            loadSaveSkuInfoByImportModel(modelCmsBtJmPromotionImportTask, modelCmsBtJmPromotion, productCmsBtJmImportSkuList, saveInfo, importProductModel);
            if(productCmsBtJmImportSkuList.size()>0)
            {
                saveInfo.getPromotionProductModel().setDealPrice(new BigDecimal(productCmsBtJmImportSkuList.get(0).getDealPrice()));
                saveInfo.getPromotionProductModel().setMarketPrice(new BigDecimal(productCmsBtJmImportSkuList.get(0).getMarketPrice()));
            }
            //3.cms_mt_master_info
            loadCmsMtMasterInfoModel(importProductModel, modelCmsBtJmPromotion, saveImportInfo);

            //4.cms_bt_jm_product_images
            List<CmsBtJmImportSpecialImage> productCmsBtJmImportSpecialImageList = getListCmsBtJmImportSpecialImageByProductCode(listSpecialImageModel, importProductModel.getProductCode());
            loadSaveCmsBtJmImportSpecialImage(saveInfo, productCmsBtJmImportSpecialImageList, modelCmsBtJmPromotion);

            listCmsBtJmProductImportSaveInfo.add(saveInfo);
            saveInfo = null;
        }
        //cms_bt_jm_product_images


        return saveImportInfo;
    }
    public  void  loadSaveCmsBtJmImportSpecialImage(CmsBtJmProductImportSaveInfo saveInfo,List<CmsBtJmImportSpecialImage> productCmsBtJmImportSpecialImageList, CmsBtJmPromotionModel modelCmsBtJmPromotion) {
        for (CmsBtJmImportSpecialImage specialImage : productCmsBtJmImportSpecialImageList) {
            loadCmsBtJmImagesModel(specialImage, modelCmsBtJmPromotion, saveInfo);
        }
    }
    private void loadOldProductModel(CmsBtJmProductModel oldProductModel, CmsBtJmImportProduct importProductModel) {
        if (!StringUtils.isEmpty(importProductModel.getAddressOfProduce())) {
            oldProductModel.setAddressOfProduce(importProductModel.getAddressOfProduce());
        }
        if (!StringUtils.isEmpty(importProductModel.getProductDesEn())) {
            oldProductModel.setProductDesEn(importProductModel.getProductDesEn());
        }
        if (!StringUtils.isEmpty(importProductModel.getProductDesCn())) {
            oldProductModel.setProductDesCn(importProductModel.getProductDesCn());
        }
        if (!StringUtils.isEmpty(importProductModel.getAvailablePeriod())) {
            oldProductModel.setAvailablePeriod(importProductModel.getAvailablePeriod());
        }
        if (!StringUtils.isEmpty(importProductModel.getApplicableCrowd())) {
            oldProductModel.setApplicableCrowd(importProductModel.getApplicableCrowd());
        }
        if (!StringUtils.isEmpty(importProductModel.getSearchMetaTextCustom())) {
            oldProductModel.setSearchMetaTextCustom(importProductModel.getSearchMetaTextCustom());
        }
        if (!StringUtils.isEmpty(importProductModel.getSpecialNote())) {
            oldProductModel.setSpecialNote(importProductModel.getSpecialNote());
        }
        if (!StringUtils.isEmpty(importProductModel.getHsCode())) {
            oldProductModel.setHsCode(importProductModel.getHsCode());
        }
        if (!StringUtils.isEmpty(importProductModel.getHsName())) {
            oldProductModel.setHsName(importProductModel.getHsName());
        }
        if (!StringUtils.isEmpty(importProductModel.getHsUnit())) {
            oldProductModel.setHsUnit(importProductModel.getHsUnit());
        }
    }
    private void loadSavePromotionProductInfoByImportModel(CmsBtJmPromotionImportTaskModel modelCmsBtJmPromotionImportTask, CmsBtJmPromotionModel modelCmsBtJmPromotion, CmsBtJmProductImportSaveInfo saveInfo, CmsBtJmProductModel oldProductModel, CmsBtJmImportProduct importProductModel) {
        CmsBtJmPromotionProductModel modelCmsBtJmPromotionProduct;
        modelCmsBtJmPromotionProduct = daoExtCmsBtJmPromotionProduct.getByProductCodeChannelIdCmsBtJmPromotionId(importProductModel.getProductCode(), modelCmsBtJmPromotion.getChannelId(), modelCmsBtJmPromotion.getId());
        if (modelCmsBtJmPromotionProduct == null) {
            modelCmsBtJmPromotionProduct = new CmsBtJmPromotionProductModel();
        }

        if (modelCmsBtJmPromotionProduct.getSynchState() == 2)//已经上传
        {
            modelCmsBtJmPromotionProduct.setLimit(importProductModel.getLimit());
        } else {
            if (oldProductModel != null) {
                modelCmsBtJmPromotionProduct.setState(oldProductModel.getState());
            }
            modelCmsBtJmPromotionProduct.setProductCode(importProductModel.getProductCode());
            if (!StringUtils.isEmpty(importProductModel.getAppId())) {
                modelCmsBtJmPromotionProduct.setAppId(importProductModel.getAppId());
            }
            if (!StringUtils.isEmpty(importProductModel.getPcId())) {
                modelCmsBtJmPromotionProduct.setPcId(importProductModel.getPcId());
            }
            modelCmsBtJmPromotionProduct.setCmsBtJmPromotionId(modelCmsBtJmPromotion.getId());
            modelCmsBtJmPromotionProduct.setChannelId(modelCmsBtJmPromotion.getChannelId());
            modelCmsBtJmPromotionProduct.setLimit(importProductModel.getLimit());
            //modelCmsBtJmPromotionProduct.setDealPrice();
            //modelCmsBtJmPromotionProduct.setMarketPrice();
            modelCmsBtJmPromotionProduct.setCreated(new Date());
            modelCmsBtJmPromotionProduct.setCreater(modelCmsBtJmPromotionImportTask.getCreater());
            saveInfo.setPromotionProductModel(modelCmsBtJmPromotionProduct);//活动商品
        }
    }
    public void loadCmsMtMasterInfoModel(CmsBtJmImportProduct importProductModel, CmsBtJmPromotionModel modelCmsBtJmPromotion,CmsBtJmImportSaveInfo saveImportInfo) {
/*
`cms_mt_master_info`
    data_type 数据类型（3:特殊说明；4：品牌故事图 ；5：尺码图； 6：物流介绍
    3.特殊说明 data_type=3  platform_id  channel_id brand_name  product_type   value1
    4.品牌故事图 data_type=4   platform_id  channel_id brand_name  product_type   value1(自己服务器的url)   value2(聚美服务器的url)
    5.尺码图data_type=5   platform_id  channel_id brand_name  size_type  product_type   value1(自己服务器的url)   value2(聚美服务器的url)
    6.物流介绍图data_type=6   platform_id  channel_id brand_name    product_type   value1(自己服务器的url)   value2(聚美服务器的url)
    导入时处理逻辑
    1.特殊说明  specialNote  进表cms_mt_master_info  specialNote 为空也要插入一条记录
    2.4：品牌故事图 ；5：尺码图； 6：物流介绍  分别查询是否存在  不存在插入记录     value1和value2 默认为空
* */
        //特殊说明 data_type=3

        CmsMtMasterInfoModel specialNote = daoExtCmsMtMasterInfo.getByKey(PlatformId, modelCmsBtJmPromotion.getChannelId(), importProductModel.getBrandName(), importProductModel.getProductType(), 3);
        //  CmsMtMasterInfoModel specialNote = new CmsMtMasterInfoModel();
        if (specialNote == null) {
            specialNote = new CmsMtMasterInfoModel();
            specialNote.setValue1(importProductModel.getSpecialNote());
            specialNote.setDataType(3);
            specialNote.setPlatformId(PlatformId);
            specialNote.setChannelId(modelCmsBtJmPromotion.getChannelId());
            specialNote.setBrandName(importProductModel.getBrandName());
            specialNote.setProductType(importProductModel.getProductType());
            saveImportInfo.add(specialNote);//特殊说明 data_type=3
        }
        //品牌故事图data_type=4
        CmsMtMasterInfoModel model4 = daoExtCmsMtMasterInfo.getByKey(PlatformId, modelCmsBtJmPromotion.getChannelId(), importProductModel.getBrandName(), importProductModel.getProductType(), 4);
        if (model4 == null) {
            model4 = new CmsMtMasterInfoModel();
            model4.setDataType(4);
            model4.setPlatformId(PlatformId);
            model4.setChannelId(modelCmsBtJmPromotion.getChannelId());
            model4.setBrandName(importProductModel.getBrandName());
            model4.setProductType(importProductModel.getProductType());
            saveImportInfo.add(model4);//品牌故事图data_type=4
        }
        //尺码图data_type=5
        CmsMtMasterInfoModel model5 = daoExtCmsMtMasterInfo.getByKeySizeType(PlatformId, modelCmsBtJmPromotion.getChannelId(), importProductModel.getBrandName(), importProductModel.getProductType(), 5, importProductModel.getSizeType());
        if (model5 == null) {
            model5 = new CmsMtMasterInfoModel();
            model5.setDataType(5);
            model5.setPlatformId(PlatformId);
            model5.setChannelId(modelCmsBtJmPromotion.getChannelId());
            model5.setBrandName(importProductModel.getBrandName());
            model5.setSizeType(importProductModel.getSizeType());
            model5.setProductType(importProductModel.getProductType());
            saveImportInfo.add(model5);//尺码图data_type=5
        }
        //物流介绍data_type=6
        CmsMtMasterInfoModel model6 = daoExtCmsMtMasterInfo.getByKey(PlatformId, modelCmsBtJmPromotion.getChannelId(), importProductModel.getBrandName(), importProductModel.getProductType(), 6);
        if (model6 == null) {
            model6 = new CmsMtMasterInfoModel();
            model6.setDataType(6);
            model6.setPlatformId(PlatformId);
            model6.setChannelId(modelCmsBtJmPromotion.getChannelId());
            model6.setBrandName(importProductModel.getBrandName());
            model6.setProductType(importProductModel.getProductType());
            saveImportInfo.add(model6);//物流介绍data_type=6
        }
    }
    public void loadCmsBtJmImagesModel(CmsBtJmImportSpecialImage specialImageModel, CmsBtJmPromotionModel modelCmsBtJmPromotion, CmsBtJmProductImportSaveInfo saveInfo) {
/*
1.表`cms_bt_jm_images`    `channel_id`
    `product_code`：
                     `image_type_name`：图片锁定条件（1:白底方图的情况下为"main_image"；2:商品详情图的情况下为"deal_image"； 3:参数图的情况下为"property_image"）
                        `image_type`：图片类型（1:白底方图 ；2:商品详情图 ；3:参数图 ；7：竖图）      `origin_url`      `jm_url`++++++++++++

2.表`cms_mt_template_images`  `platform_id` `channel_id` `template_type` 图片模板类别（1:宝贝图（白底方图）；2:；详情图（商品实拍图）；3：移动端宝贝图（竖图））
`image_template_url`

3.导入数据时图片处理逻辑
    1.导入字段 propertyImage   `cms_bt_jm_images`.`origin_url` `image_type`=3
    2.导入字段 productImageUrlKey1     productImageUrlKey2 productImageUrlKey3 productImageUrlKey4 productImageUrlKey5 productImageUrlKey6
        分别套模板cms_mt_template_images()每个都套1:宝贝图（白底方图）；2:；详情图（商品实拍图）；3：移动端宝贝图（竖图）） 保存到表 cms_bt_jm_images
* */

        String productCode = specialImageModel.getProductCode();
        //参数图片
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getPropertyImage1(), productCode, 3, 1);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getPropertyImage2(), productCode, 3, 2);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getPropertyImage3(), productCode, 3, 3);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getPropertyImage4(), productCode, 3, 4);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getPropertyImage5(), productCode, 3, 5);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getPropertyImage6(), productCode, 3, 6);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getPropertyImage7(), productCode, 3, 7);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getPropertyImage8(), productCode, 3, 8);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getPropertyImage9(), productCode, 3, 9);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getPropertyImage10(), productCode, 3, 10);

        //商品定制图
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getSpecialImage1(), productCode, 8, 1);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getSpecialImage2(), productCode, 8, 2);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getSpecialImage3(), productCode, 8, 3);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getSpecialImage4(), productCode, 8, 4);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getSpecialImage5(), productCode, 8, 5);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getSpecialImage6(), productCode, 8, 6);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getSpecialImage7(), productCode, 8, 7);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getSpecialImage8(), productCode, 8, 8);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getSpecialImage9(), productCode, 8, 9);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getSpecialImage10(), productCode, 8, 10);

        List<Integer> templateTypeList = new ArrayList();
        templateTypeList.add(1);//宝贝图
        templateTypeList.add(2);//详情图
        templateTypeList.add(7);//移动端宝贝图（竖图）
        List<CmsMtTemplateImagesModel> listCmsMtTemplateImages = daoExtCmsMtTemplateImages.getListByPlatformChannelTemplateType(PlatformId, modelCmsBtJmPromotion.getChannelId(), templateTypeList);
        addImageByProductImageUrlKey(specialImageModel.getProductImageUrlKey1(), specialImageModel, modelCmsBtJmPromotion, saveInfo, listCmsMtTemplateImages, 1);
        addImageByProductImageUrlKey(specialImageModel.getProductImageUrlKey2(), specialImageModel, modelCmsBtJmPromotion, saveInfo, listCmsMtTemplateImages, 2);
        addImageByProductImageUrlKey(specialImageModel.getProductImageUrlKey3(), specialImageModel, modelCmsBtJmPromotion, saveInfo, listCmsMtTemplateImages, 3);
        addImageByProductImageUrlKey(specialImageModel.getProductImageUrlKey4(), specialImageModel, modelCmsBtJmPromotion, saveInfo, listCmsMtTemplateImages, 4);
        addImageByProductImageUrlKey(specialImageModel.getProductImageUrlKey5(), specialImageModel, modelCmsBtJmPromotion, saveInfo, listCmsMtTemplateImages, 5);
        addImageByProductImageUrlKey(specialImageModel.getProductImageUrlKey6(), specialImageModel, modelCmsBtJmPromotion, saveInfo, listCmsMtTemplateImages, 6);
        addImageByProductImageUrlKey(specialImageModel.getProductImageUrlKey7(), specialImageModel, modelCmsBtJmPromotion, saveInfo, listCmsMtTemplateImages, 7);
        addImageByProductImageUrlKey(specialImageModel.getProductImageUrlKey8(), specialImageModel, modelCmsBtJmPromotion, saveInfo, listCmsMtTemplateImages, 8);
        addImageByProductImageUrlKey(specialImageModel.getProductImageUrlKey9(), specialImageModel, modelCmsBtJmPromotion, saveInfo, listCmsMtTemplateImages, 9);
        addImageByProductImageUrlKey(specialImageModel.getProductImageUrlKey10(), specialImageModel, modelCmsBtJmPromotion, saveInfo, listCmsMtTemplateImages, 10);
    }

    private void addPropertyImage(CmsBtJmPromotionModel modelCmsBtJmPromotion, CmsBtJmProductImportSaveInfo saveInfo, String propertyImage1, String productCode, int imageType, int imageIndex) {
        if (StringUtils.isEmpty(propertyImage1)) return;  //为空 不覆盖
        CmsBtJmProductImagesModel propertyImageModel = daoExtCmsBtJmProductImages.getByKey(modelCmsBtJmPromotion.getChannelId(), productCode, imageType, imageIndex);
        if (propertyImageModel == null) {
            propertyImageModel = new CmsBtJmProductImagesModel();
        }
        propertyImageModel.setChannelId(modelCmsBtJmPromotion.getChannelId());
        propertyImageModel.setProductCode(productCode);
        // propertyImageModel.setProductImageUrlKey(productCode);
        propertyImageModel.setImageType(imageType);
        propertyImageModel.setImageIndex(imageIndex);
        propertyImageModel.setOriginUrl(propertyImage1);
        propertyImageModel.setCreater(modelCmsBtJmPromotion.getModifier());
        propertyImageModel.setCreated(new Date());
        propertyImageModel.setModifier(modelCmsBtJmPromotion.getModifier());
        saveInfo.getListCmsBtJmProductImagesModel().add(propertyImageModel);
    }

    private void addImageByProductImageUrlKey(String ProductImageUrlKey, CmsBtJmImportSpecialImage specialImageModel, CmsBtJmPromotionModel modelCmsBtJmPromotion, CmsBtJmProductImportSaveInfo saveInfo, List<CmsMtTemplateImagesModel> listCmsMtTemplateImages, int imageIndex) {
        if (StringUtils.isEmpty(ProductImageUrlKey)) return; //为空 不覆盖
        for (CmsMtTemplateImagesModel templateImage : listCmsMtTemplateImages) {
            CmsBtJmProductImagesModel productImage = daoExtCmsBtJmProductImages.getByKey(modelCmsBtJmPromotion.getChannelId(), specialImageModel.getProductCode(), templateImage.getTemplateType(), imageIndex);
            if (productImage == null) {
                productImage = new CmsBtJmProductImagesModel();
            }
            productImage.setChannelId(modelCmsBtJmPromotion.getChannelId());
            productImage.setProductCode(specialImageModel.getProductCode());
            productImage.setProductImageUrlKey(ProductImageUrlKey);
            productImage.setImageType(templateImage.getTemplateType());
            productImage.setImageIndex(imageIndex);
            productImage.setOriginUrl(templateImage.getImageTemplateUrl() + ProductImageUrlKey);
            productImage.setCreater(modelCmsBtJmPromotion.getModifier());
            productImage.setCreated(new Date());
            productImage.setModifier(modelCmsBtJmPromotion.getModifier());
            saveInfo.getListCmsBtJmProductImagesModel().add(productImage);

        }
    }


    private void loadSaveSkuInfoByImportModel(CmsBtJmPromotionImportTaskModel modelCmsBtJmPromotionImportTask, CmsBtJmPromotionModel modelCmsBtJmPromotion, List<CmsBtJmImportSku> listProductSku, CmsBtJmProductImportSaveInfo saveInfo, CmsBtJmImportProduct importProductModel) {
        CmsBtJmSkuModel oldSkuModel = null;
        CmsBtJmPromotionSkuModel promotionSkuModel = null;
        for (CmsBtJmImportSku importSkuModel : listProductSku) {
            oldSkuModel = loadSku(modelCmsBtJmPromotion, saveInfo, importSkuModel);//sku
            loadPromotionSku(modelCmsBtJmPromotionImportTask, modelCmsBtJmPromotion, saveInfo, oldSkuModel, importSkuModel);//promotionSku
        }
    }

    private void loadPromotionSku(CmsBtJmPromotionImportTaskModel modelCmsBtJmPromotionImportTask, CmsBtJmPromotionModel modelCmsBtJmPromotion, CmsBtJmProductImportSaveInfo saveInfo, CmsBtJmSkuModel oldSkuModel, CmsBtJmImportSku importSkuModel) {
        CmsBtJmPromotionSkuModel promotionSkuModel;
        promotionSkuModel = daoExtCmsBtJmPromotionSku.getBySkuCodeChannelIdCmsBtJmPromotionId(importSkuModel.getSkuCode(), modelCmsBtJmPromotion.getChannelId(), modelCmsBtJmPromotion.getId());
        if (promotionSkuModel == null) {
            promotionSkuModel = new CmsBtJmPromotionSkuModel();
        }
        if (promotionSkuModel.getSynchState() == 2) {//已上传
            if (importSkuModel.getDealPrice() != 0) {
                promotionSkuModel.setDealPrice(new BigDecimal(importSkuModel.getDealPrice()));
            }
            if (importSkuModel.getMarketPrice() != 0) {
                promotionSkuModel.setMarketPrice(new BigDecimal(importSkuModel.getMarketPrice()));
            }
        } else {
            if (oldSkuModel != null) {
                promotionSkuModel.setState(oldSkuModel.getState());
            }
            promotionSkuModel.setDealPrice(new BigDecimal(importSkuModel.getDealPrice()));
            promotionSkuModel.setCmsBtJmPromotionId(modelCmsBtJmPromotion.getId());
            promotionSkuModel.setSkuCode(importSkuModel.getSkuCode());
            promotionSkuModel.setChannelId(modelCmsBtJmPromotion.getChannelId());
            //promotionSkuModel.setCmsBtJmProductId();
            //promotionSkuModel.setCmsBtJmSkuId();
            promotionSkuModel.setCreated(new Date());
            promotionSkuModel.setCreater(modelCmsBtJmPromotionImportTask.getCreater());
            promotionSkuModel.setJmSize(importSkuModel.getJmSize());
            promotionSkuModel.setModifier(modelCmsBtJmPromotionImportTask.getCreater());
            saveInfo.getListPromotionSkuModel().add(promotionSkuModel);//加入活动规格
        }
    }

    private CmsBtJmSkuModel loadSku(CmsBtJmPromotionModel modelCmsBtJmPromotion, CmsBtJmProductImportSaveInfo saveInfo, CmsBtJmImportSku importSkuModel) {
        CmsBtJmSkuModel oldSkuModel;
        oldSkuModel = daoExtCmsBtJmSku.getBySkuCodeChannelId(importSkuModel.getSkuCode(), modelCmsBtJmPromotion.getChannelId());
        importSkuModel.setChannelId(modelCmsBtJmPromotion.getChannelId());//渠道
        if (oldSkuModel != null) {
            if (oldSkuModel.getState() == 0)//未上新 全覆盖
            {
                importSkuModel.setId(oldSkuModel.getId());
                saveInfo.getListSkuModel().add(importSkuModel);//加入保存规格
            } else //已上新
            {

            }
        } else {
            saveInfo.getListSkuModel().add(importSkuModel);//加入保存规格
        }
        return oldSkuModel;
    }

    private List<CmsBtJmImportSpecialImage> getListCmsBtJmImportSpecialImageByProductCode(List<CmsBtJmImportSpecialImage> list, String productCode) {
        List<CmsBtJmImportSpecialImage> result = new ArrayList<>();
        for (CmsBtJmImportSpecialImage specialImage : list) {
            if (specialImage.getProductCode().equals(productCode)) {
                result.add(specialImage);
            }
        }
        return result;
    }
    private List<CmsBtJmImportSku> getListCmsBtJmImportSkuByProductCode(List<CmsBtJmImportSku> list, String productCode) {
        List<CmsBtJmImportSku> result = new ArrayList<>();
        for (CmsBtJmImportSku sku : list) {
            if (sku.getProductCode().equals(productCode)) {
                result.add(sku);
            }
        }
        return result;
    }
    public List<CmsBtJmPromotionImportTaskModel> getByPromotionId(int promotionId) {
        return daoExt.getByPromotionId(promotionId);
    }

    @VOTransactional
    public void saveList(List<CmsBtJmPromotionImportTaskModel> list) {
        for (CmsBtJmPromotionImportTaskModel model : list) {
            dao.insert(model);
        }
    }
}