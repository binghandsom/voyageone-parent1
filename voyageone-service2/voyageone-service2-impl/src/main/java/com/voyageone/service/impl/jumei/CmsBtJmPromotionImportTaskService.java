package com.voyageone.service.impl.jumei;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.service.dao.jumei.*;
import com.voyageone.service.daoext.jumei.*;
import com.voyageone.service.impl.Excel.ExcelColumn;
import com.voyageone.service.impl.Excel.ExcelImportUtil;
import com.voyageone.service.impl.jumei.enumjm.EnumJMProductImportColumn;
import com.voyageone.service.impl.jumei.enumjm.EnumJMSkuImportColumn;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.jumei.*;
import com.voyageone.service.model.jumei.businessmodel.CmsBtJmImportProduct;
import com.voyageone.service.model.jumei.businessmodel.CmsBtJmImportSku;
import com.voyageone.service.model.jumei.businessmodel.CmsBtJmImportSpecialImage;
import com.voyageone.service.model.jumei.businessmodel.CmsBtJmProductImportSaveInfo;
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

    public CmsBtJmPromotionImportTaskModel get(int id) {
        return dao.select(id);
    }

    public int update(CmsBtJmPromotionImportTaskModel entity) {
        return dao.update(entity);
    }

    public int create(CmsBtJmPromotionImportTaskModel entity) {
        return dao.insert(entity);
    }

    public void Import(int JmBtPromotionImportTaskId) throws Exception {
        String errorMsg = "";
        boolean isError = false;
        CmsBtJmPromotionImportTaskModel modelCmsBtJmPromotionImportTask = dao.select(JmBtPromotionImportTaskId);
        CmsBtJmPromotionModel modelCmsBtJmPromotion = serviceCmsBtJmPromotion.select(modelCmsBtJmPromotionImportTask.getCmsBtJmPromotionId());
        modelCmsBtJmPromotionImportTask.setBeginTime(new Date());
        String filePath = "/usr/JMImport/" + modelCmsBtJmPromotionImportTask.getFileName().trim();//"/Product20160324164706.xls";
        File excelFile = new File(filePath);
        InputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(excelFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            errorMsg += e.getMessage();
            isError = true;
        }
        HSSFWorkbook book = null;
        try {
            book = new HSSFWorkbook(fileInputStream);
        } catch (IOException e) {
            throw e;
        }

        HSSFSheet productSheet = book.getSheet("Product");
        List<CmsBtJmImportProduct> listProductModel = new ArrayList<>();//导入的集合
        List<Map<String, Object>> listErrorMap = new ArrayList<>();//错误行集合  导出错误文件
        List<ExcelColumn> listProductColumn = EnumJMProductImportColumn.getListExcelColumn();//配置列信息
        ExcelImportUtil.importSheet(productSheet, listProductColumn, listProductModel, listErrorMap, CmsBtJmImportProduct.class);

        HSSFSheet skuSheet = book.getSheet("Sku");
        List<CmsBtJmImportSku> listSkuModel = new ArrayList<>();
        List<Map<String, Object>> listSkuErrorMap = new ArrayList<>();
        List<ExcelColumn> listSkuColumn = EnumJMSkuImportColumn.getListExcelColumn();
        ExcelImportUtil.importSheet(skuSheet, listSkuColumn, listSkuModel, listSkuErrorMap, CmsBtJmImportSku.class);

        HSSFSheet specialImageSheet = book.getSheet("Sku");
        List<CmsBtJmImportSpecialImage> listSpecialImageModel = new ArrayList<>();
        List<Map<String, Object>> listSpecialErrorMap = new ArrayList<>();
        List<ExcelColumn> listSpecialColumn = EnumJMSkuImportColumn.getListExcelColumn();
        ExcelImportUtil.importSheet(specialImageSheet, listSpecialColumn, listSpecialImageModel, listSpecialErrorMap, CmsBtJmImportSpecialImage.class);



        List<CmsBtJmProductImportSaveInfo> listCmsBtJmProductImportSaveInfo = loadListSaveInfo(modelCmsBtJmPromotionImportTask, modelCmsBtJmPromotion, listProductModel, listSkuModel,listSpecialImageModel);
        saveListCmsBtJmProductImportSaveInfo(listCmsBtJmProductImportSaveInfo);
    }

    private void saveListCmsBtJmProductImportSaveInfo(List<CmsBtJmProductImportSaveInfo> list) {
        for (CmsBtJmProductImportSaveInfo saveInfo : list) {
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
            }
            for (CmsBtJmPromotionSkuModel skuPromotionModel : saveInfo.getListPromotionSkuModel()) {
                if (skuPromotionModel.getId() == 0) {
                    daoCmsBtJmPromotionSku.insert(skuPromotionModel);
                } else {
                    daoCmsBtJmPromotionSku.update(skuPromotionModel);
                }
            }
            for (CmsMtMasterInfoModel model : saveInfo.getListCmsMtMasterInfoModel()) {
                if (model.getId() == 0) {
                    daoCmsMtMasterInfo.insert(model);
                } else {
                    daoCmsMtMasterInfo.update(model);
                }
            }
            for (CmsBtJmProductImagesModel model : saveInfo.getListCmsBtJmProductImagesModel()) {
                if (model.getId() == 0) {
                    daoCmsBtJmProductImages.insert(model);
                } else {
                    daoCmsBtJmProductImages.update(model);
                }
            }
        }
    }

    private List<CmsBtJmProductImportSaveInfo> loadListSaveInfo(CmsBtJmPromotionImportTaskModel modelCmsBtJmPromotionImportTask, CmsBtJmPromotionModel modelCmsBtJmPromotion, List<CmsBtJmImportProduct> listProductModel, List<CmsBtJmImportSku> listSkuModel,  List<CmsBtJmImportSpecialImage> listSpecialImageModel) {
        List<CmsBtJmProductImportSaveInfo> listCmsBtJmProductImportSaveInfo = new ArrayList<>();
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
            //活动商品  cms_bt_jm_promotion_product
            loadSavePromotionProductInfoByImportModel(modelCmsBtJmPromotionImportTask, modelCmsBtJmPromotion, saveInfo, oldProductModel, importProductModel);
            //sku     cms_bt_jm_promotion_sku  cms_bt_jm_sku
            loadSaveSkuInfoByImportModel(modelCmsBtJmPromotionImportTask, modelCmsBtJmPromotion, listSkuModel, saveInfo, importProductModel);
            //cms_mt_master_info
            loadCmsMtMasterInfoModel(importProductModel, modelCmsBtJmPromotion, saveInfo);

            listCmsBtJmProductImportSaveInfo.add(saveInfo);
        }
        //cms_bt_jm_product_images
        for (CmsBtJmImportSpecialImage specialImage : listSpecialImageModel) {
            loadCmsBtJmImagesModel(specialImage, modelCmsBtJmPromotion, saveInfo);
        }
        return listCmsBtJmProductImportSaveInfo;
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

    public void loadCmsMtMasterInfoModel(CmsBtJmImportProduct importProductModel, CmsBtJmPromotionModel modelCmsBtJmPromotion, CmsBtJmProductImportSaveInfo saveInfo) {
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
            saveInfo.getListCmsMtMasterInfoModel().add(specialNote);//特殊说明 data_type=3
        }
        //品牌故事图data_type=4
        CmsMtMasterInfoModel model4 = daoExtCmsMtMasterInfo.getByKey(PlatformId, modelCmsBtJmPromotion.getChannelId(), importProductModel.getBrandName(), importProductModel.getProductType(), 4);
        if (model4 == null) {
            model4 = new CmsMtMasterInfoModel();
            specialNote.setDataType(4);
            specialNote.setPlatformId(PlatformId);
            specialNote.setChannelId(modelCmsBtJmPromotion.getChannelId());
            specialNote.setBrandName(importProductModel.getBrandName());
            specialNote.setProductType(importProductModel.getProductType());
            saveInfo.getListCmsMtMasterInfoModel().add(model4);//特殊说明 data_type=3
            saveInfo.getListCmsMtMasterInfoModel().add(model4);//品牌故事图data_type=4
        }
        //尺码图data_type=5
        CmsMtMasterInfoModel model5 = daoExtCmsMtMasterInfo.getByKeySizeType(PlatformId, modelCmsBtJmPromotion.getChannelId(), importProductModel.getBrandName(), importProductModel.getProductType(), 5, importProductModel.getSizeType());
        if (model5 == null) {
            model5 = new CmsMtMasterInfoModel();
            specialNote.setDataType(5);
            specialNote.setPlatformId(PlatformId);
            specialNote.setChannelId(modelCmsBtJmPromotion.getChannelId());
            specialNote.setBrandName(importProductModel.getBrandName());
            specialNote.setSizeType(importProductModel.getSizeType());
            specialNote.setProductType(importProductModel.getProductType());
            saveInfo.getListCmsMtMasterInfoModel().add(model5);//尺码图data_type=5
        }
        //物流介绍data_type=6
        CmsMtMasterInfoModel model6 = daoExtCmsMtMasterInfo.getByKey(PlatformId, modelCmsBtJmPromotion.getChannelId(), importProductModel.getBrandName(), importProductModel.getProductType(), 6);
        if (model6 == null) {
            model6 = new CmsMtMasterInfoModel();
            specialNote.setDataType(6);
            specialNote.setPlatformId(PlatformId);
            specialNote.setChannelId(modelCmsBtJmPromotion.getChannelId());
            specialNote.setBrandName(importProductModel.getBrandName());
            specialNote.setProductType(importProductModel.getProductType());
            saveInfo.getListCmsMtMasterInfoModel().add(model6);//物流介绍data_type=6
        }
    }

    public void loadCmsBtJmImagesModel(CmsBtJmImportSpecialImage specialImageModel,CmsBtJmPromotionModel modelCmsBtJmPromotion, CmsBtJmProductImportSaveInfo saveInfo) {
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
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getPropertyImage1(), productCode,3,1);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getPropertyImage2(), productCode,3,2);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getPropertyImage3(), productCode,3,3);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getPropertyImage4(), productCode,3,4);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getPropertyImage5(), productCode,3,5);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getPropertyImage6(), productCode,3,6);

        //商品定制图
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getSpecialImage1(), productCode,8,1);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getSpecialImage2(), productCode,8,2);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getSpecialImage3(), productCode,8,3);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getSpecialImage4(), productCode,8,4);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getSpecialImage5(), productCode,8,5);
        addPropertyImage(modelCmsBtJmPromotion, saveInfo, specialImageModel.getSpecialImage6(), productCode,8,6);

        List<Integer> templateTypeList = new ArrayList();
        templateTypeList.add(1);//宝贝图
        templateTypeList.add(2);//详情图
        templateTypeList.add(3);//移动端宝贝图（竖图）
        List<CmsMtTemplateImagesModel> listCmsMtTemplateImages = daoExtCmsMtTemplateImages.getListByPlatformChannelTemplateType(PlatformId, modelCmsBtJmPromotion.getChannelId(), templateTypeList);
        addImageByProductImageUrlKey(specialImageModel.getProductImageUrlKey1(), specialImageModel, modelCmsBtJmPromotion, saveInfo, listCmsMtTemplateImages);
        addImageByProductImageUrlKey(specialImageModel.getProductImageUrlKey2(), specialImageModel, modelCmsBtJmPromotion, saveInfo, listCmsMtTemplateImages);
        addImageByProductImageUrlKey(specialImageModel.getProductImageUrlKey3(), specialImageModel, modelCmsBtJmPromotion, saveInfo, listCmsMtTemplateImages);
        addImageByProductImageUrlKey(specialImageModel.getProductImageUrlKey4(), specialImageModel, modelCmsBtJmPromotion, saveInfo, listCmsMtTemplateImages);
        addImageByProductImageUrlKey(specialImageModel.getProductImageUrlKey5(), specialImageModel, modelCmsBtJmPromotion, saveInfo, listCmsMtTemplateImages);
        addImageByProductImageUrlKey(specialImageModel.getProductImageUrlKey6(), specialImageModel, modelCmsBtJmPromotion, saveInfo, listCmsMtTemplateImages);
    }

    private void addPropertyImage(CmsBtJmPromotionModel modelCmsBtJmPromotion, CmsBtJmProductImportSaveInfo saveInfo, String propertyImage1, String productCode,int imageType,int index) {
        if(StringUtils.isEmpty(propertyImage1)) return;
        CmsBtJmProductImagesModel propertyImageModel = new CmsBtJmProductImagesModel();
        propertyImageModel.setChannelId(modelCmsBtJmPromotion.getChannelId());
        propertyImageModel.setProductImageUrlKey(productCode);
        propertyImageModel.setImageType(imageType);
        propertyImageModel.setImageIndex(index);
        propertyImageModel.setOriginUrl(propertyImage1);
        propertyImageModel.setCreater(modelCmsBtJmPromotion.getModifier());
        propertyImageModel.setCreated(new Date());
        propertyImageModel.setModifier(modelCmsBtJmPromotion.getModifier());
        saveInfo.getListCmsBtJmProductImagesModel().add(propertyImageModel);
    }

    private void addImageByProductImageUrlKey(String ProductImageUrlKey,CmsBtJmImportSpecialImage  specialImageModel, CmsBtJmPromotionModel modelCmsBtJmPromotion, CmsBtJmProductImportSaveInfo saveInfo, List<CmsMtTemplateImagesModel> listCmsMtTemplateImages) {
        if (StringUtils.isEmpty(ProductImageUrlKey)) return;
        for (CmsMtTemplateImagesModel templateImage : listCmsMtTemplateImages) {
            CmsBtJmProductImagesModel productImage = daoExtCmsBtJmProductImages.getByKey(modelCmsBtJmPromotion.getChannelId(), specialImageModel.getProductCode(), templateImage.getTemplateType(), 1);
            if (productImage == null) {
                productImage = new CmsBtJmProductImagesModel();
                productImage.setChannelId(modelCmsBtJmPromotion.getChannelId());
                productImage.setProductImageUrlKey(ProductImageUrlKey);
                productImage.setImageType(templateImage.getTemplateType());
                productImage.setOriginUrl(templateImage.getImageTemplateUrl() + ProductImageUrlKey);
                productImage.setCreater(modelCmsBtJmPromotion.getModifier());
                productImage.setImageType(1);
                productImage.setCreated(new Date());
                productImage.setModifier(modelCmsBtJmPromotion.getModifier());
                saveInfo.getListCmsBtJmProductImagesModel().add(productImage);
            }
        }
    }


    private void loadSaveSkuInfoByImportModel(CmsBtJmPromotionImportTaskModel modelCmsBtJmPromotionImportTask, CmsBtJmPromotionModel modelCmsBtJmPromotion, List<CmsBtJmImportSku> listSkuModel, CmsBtJmProductImportSaveInfo saveInfo, CmsBtJmImportProduct importProductModel) {
        List<CmsBtJmImportSku> listProductSku = getListCmsBtJmImportSkuByProductCode(listSkuModel, importProductModel.getProductCode());
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
    public void  saveList(List<CmsBtJmPromotionImportTaskModel> list) {
        for (CmsBtJmPromotionImportTaskModel model : list) {
                 dao.insert(model);
        }
    }
}