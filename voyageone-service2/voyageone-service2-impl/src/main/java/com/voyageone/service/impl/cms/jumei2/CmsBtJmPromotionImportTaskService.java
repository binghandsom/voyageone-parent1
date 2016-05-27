package com.voyageone.service.impl.cms.jumei2;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.util.BigDecimalUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.MapUtil;
import com.voyageone.common.util.excel.ExcelColumn;
import com.voyageone.common.util.excel.ExcelImportUtil;
import com.voyageone.common.util.excel.ListHelp;
import com.voyageone.service.bean.cms.businessmodel.*;
import com.voyageone.service.bean.cms.jumei2.ProductImportBean;
import com.voyageone.service.bean.cms.jumei2.ProductSaveInfo;
import com.voyageone.service.bean.cms.jumei2.SkuImportBean;
import com.voyageone.service.dao.cms.*;
import com.voyageone.service.daoext.cms.*;
import com.voyageone.service.impl.cms.jumei.*;
import com.voyageone.service.impl.cms.jumei.enumjm.EnumJMProductImportColumn;
import com.voyageone.service.impl.cms.jumei.enumjm.EnumJMSkuImportColumn;
import com.voyageone.service.impl.cms.jumei.enumjm.EnumJMSpecialImageImportColumn;
import com.voyageone.service.model.cms.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotionImportTaskService {
    private static final int PlatformId = 27;
    @Autowired
    CmsBtJmPromotionImportTaskDao cmsBtJmPromotionImportTaskDao;
    @Autowired
    CmsBtJmPromotionImportTaskDaoExt cmsBtJmPromotionImportTaskDaoExt;
    @Autowired
    CmsBtJmPromotionDao daoCmsBtJmPromotion;
    @Autowired
    CmsBtJmPromotionProductDaoExt daoExtCmsBtJmPromotionProduct;
    @Autowired
    CmsBtTagDaoExt daoExtCmsBtTag;
    @Autowired
    CmsBtJmPromotionProductDao daoCmsBtJmPromotionProduct;
    @Autowired
    CmsBtJmPromotionTagProductDao daoCmsBtJmPromotionTagProduct;
    @Autowired
    CmsBtJmPromotionSkuDao daoCmsBtJmPromotionSku;
    @Autowired
    CmsBtJmPromotionSkuDaoExt daoExtCmsBtJmPromotionSku;
    @Autowired
    TransactionRunner transactionRunner;

    public void importFile(int JmBtPromotionImportTaskId, String importPath) throws Exception {
        String errorMsg = "";
        boolean isError = false;
        CmsBtJmPromotionImportTaskModel modelCmsBtJmPromotionImportTask = cmsBtJmPromotionImportTaskDao.select(JmBtPromotionImportTaskId);
        modelCmsBtJmPromotionImportTask.setBeginTime(new Date());
        try {
            cmsBtJmPromotionImportTaskDao.update(modelCmsBtJmPromotionImportTask);
            importExcel(modelCmsBtJmPromotionImportTask, importPath);
        } catch (Exception ex) {
            ex.printStackTrace();
            modelCmsBtJmPromotionImportTask.setErrorCode(1);
            modelCmsBtJmPromotionImportTask.setErrorMsg(ex.getMessage() + ex.getStackTrace());
            if (ex.getStackTrace().length > 0) {
                modelCmsBtJmPromotionImportTask.setErrorMsg(modelCmsBtJmPromotionImportTask.getErrorMsg() + ex.getStackTrace()[0].toString());
            }
        }
        modelCmsBtJmPromotionImportTask.setIsImport(true);
        modelCmsBtJmPromotionImportTask.setEndTime(new Date());
        cmsBtJmPromotionImportTaskDao.update(modelCmsBtJmPromotionImportTask);
    }

    private void importExcel(CmsBtJmPromotionImportTaskModel modelCmsBtJmPromotionImportTask, String importPath) throws Exception {
        boolean isError;
        CmsBtJmPromotionModel modelCmsBtJmPromotion = daoCmsBtJmPromotion.select(modelCmsBtJmPromotionImportTask.getCmsBtJmPromotionId());
        modelCmsBtJmPromotionImportTask.setBeginTime(new Date());
        //"/usr/JMImport/"
        String filePath = importPath + "/" + modelCmsBtJmPromotionImportTask.getFileName().trim();//"/Product20160324164706.xls";
        File excelFile = new File(filePath);
        InputStream fileInputStream = null;
        fileInputStream = new FileInputStream(excelFile);
        HSSFWorkbook book =  new HSSFWorkbook(fileInputStream);

        //读取product
        HSSFSheet productSheet = book.getSheet("Product");
        List<ProductImportBean> listProductImport = new ArrayList<>();//导入的集合
        List<Map<String, Object>> listProducctErrorMap = new ArrayList<>();//错误行集合  导出错误文件
        List<ExcelColumn> listProductColumn = getProductImportColumn();//配置列信息
        ExcelImportUtil.importSheet(productSheet, listProductColumn, listProductImport, listProducctErrorMap, ProductImportBean.class);

        //读取sku
        HSSFSheet skuSheet = book.getSheet("Sku");
        List<SkuImportBean> listSkuImport = new ArrayList<>();
        List<Map<String, Object>> listSkuErrorMap = new ArrayList<>();
        List<ExcelColumn> listSkuColumn = getSkuImportColumn();
        ExcelImportUtil.importSheet(skuSheet, listSkuColumn, listSkuImport, listSkuErrorMap, SkuImportBean.class);

        //check
        check(modelCmsBtJmPromotion,listProductImport,listSkuImport,listProducctErrorMap,listSkuErrorMap);//check 移除不能导入的product

        saveImport(modelCmsBtJmPromotion,listProductImport,listSkuImport);
        if (listProducctErrorMap.size() > 0 | listSkuErrorMap.size() > 0) {
            String failuresFileName = "error" + modelCmsBtJmPromotionImportTask.getFileName().trim();
            String errorfilePath = "/usr/JMExport/error" + modelCmsBtJmPromotionImportTask.getFileName().trim();
            // serviceCmsBtJmPromotionExportTask.export(errorfilePath, listProducctErrorMap, listSkuErrorMap, listSpecialErrorMap, true);
            modelCmsBtJmPromotionImportTask.setFailuresFileName(failuresFileName);
            modelCmsBtJmPromotionImportTask.setErrorCode(2);
            modelCmsBtJmPromotionImportTask.setFailuresRows(listProducctErrorMap.size());
        }
        modelCmsBtJmPromotionImportTask.setSuccessRows(listProductImport.size());
    }
    //check
    public void check(CmsBtJmPromotionModel model,List<ProductImportBean> listProductModel, List<SkuImportBean> listSkuModel,List<Map<String, Object>> listProducctErrorMap, List<Map<String, Object>> listSkuErrorMap) throws IllegalAccessException {
        List<ProductImportBean> listErroProduct=new ArrayList<>();
        for (ProductImportBean product : listProductModel) {
            if (daoExtCmsBtJmPromotionProduct.existsCode(model.getChannelId(), product.getProductCode(), model.getActivityStart(), model.getActivityEnd())) { //活动日期重叠
                product.setErrorMsg("活动日期有重叠");
                listErroProduct.add(product);
            }
        }
        listProductModel.removeAll(listErroProduct);//移除不能导入的 product
        listProducctErrorMap.addAll(MapUtil.toMapList(listErroProduct));
    }
    //save
    public void  saveImport( CmsBtJmPromotionModel model,List<ProductImportBean> listProductImport,List<SkuImportBean> listSkuImport) {
        List<ProductSaveInfo> listSaveInfo = new ArrayList<>();
        //初始化
        ProductSaveInfo saveInfo = null;
        for (ProductImportBean product : listProductImport) {
            saveInfo = loadSaveInfo(model, listSkuImport, product);
            listSaveInfo.add(saveInfo);
        }

        //保存
        for (ProductSaveInfo info : listSaveInfo) {
            saveProductSaveInfo(info);
        }
    }

    private void saveProductSaveInfo(ProductSaveInfo info) {
        //CmsBtJmPromotionProduct
        if (info.productModel.getId() == null || info.productModel.getId() == 0) {
            daoCmsBtJmPromotionProduct.insert(info.productModel);
        } else {
            daoCmsBtJmPromotionProduct.update(info.productModel);
        }

        //CmsBtJmPromotionSku
        for (CmsBtJmPromotionSkuModel sku : info.skuList) {
            sku.setCmsBtJmPromotionProductId(info.productModel.getId());
            if (sku.getId() == null || sku.getId() == 0) {
                daoCmsBtJmPromotionSku.insert(sku);
            } else {
                daoCmsBtJmPromotionSku.update(sku);
            }
        }

        //CmsBtJmPromotionTagProduct
        for (CmsBtJmPromotionTagProductModel tag : info.tagList) {
            tag.setCmsBtJmPromotionProductId(info.productModel.getId());
            if(tag.getId()==null||tag.getId()==0) {
                daoCmsBtJmPromotionTagProduct.insert(tag);
            }
        }
    }

    private ProductSaveInfo loadSaveInfo(CmsBtJmPromotionModel model, List<SkuImportBean> listSkuImport,ProductImportBean product) {
        ProductSaveInfo saveInfo= new ProductSaveInfo();
        saveInfo.productModel = daoExtCmsBtJmPromotionProduct.getByProductCode(product.getProductCode(), model.getChannelId(), model.getId());
        if (saveInfo.productModel == null) {
            saveInfo.productModel = new CmsBtJmPromotionProductModel();
        }
        saveInfo.productModel.setProductCode(product.getProductCode());
        saveInfo.productModel.setAppId(product.getAppId());
        saveInfo.productModel.setPcId(product.getPcId());
        saveInfo.productModel.setLimit(product.getLimit());
        saveInfo.productModel.setChannelId(model.getChannelId());
        saveInfo.productModel.setActivityStart(model.getActivityStart());
        saveInfo.productModel.setActivityEnd(model.getActivityEnd());

        //初始化CmsBtJmPromotionTagProductModel
        loadSaveTag(product.getPromotionTag(), saveInfo, model);

        //初始化CmsBtJmPromotionSkuModel
        List<SkuImportBean> listSku = getListSkuImportBeanByProductCode(listSkuImport, product.getProductCode());
        loadSaveSku(saveInfo, listSku);

        return saveInfo;
    }

    private void  loadSaveSku(ProductSaveInfo saveInfo,List<SkuImportBean> listImport) {
        CmsBtJmPromotionSkuModel skuModel = null;
        for (SkuImportBean skuImportBean : listImport) {
            if (saveInfo.productModel.getId() > 0) {
                skuModel = daoExtCmsBtJmPromotionSku.getBySkuCode(skuImportBean.getSkuCode(), saveInfo.productModel.getId());
            }
            if (skuModel == null) {
                skuModel = new CmsBtJmPromotionSkuModel();
            }
            skuModel.setDealPrice(new BigDecimal(skuImportBean.getDealPrice()));
            skuModel.setMarketPrice(new BigDecimal(skuImportBean.getMarketPrice()));
            skuModel.setChannelId(saveInfo.productModel.getChannelId());
            skuModel.setSkuCode(skuImportBean.getSkuCode());
            saveInfo.skuList.add(skuModel);
        }
    }
    private List<SkuImportBean> getListSkuImportBeanByProductCode(List<SkuImportBean> listSkuImport,String productCode) {
        List<SkuImportBean> listResult = new ArrayList<>();
        for (SkuImportBean sku : listSkuImport) {
            if (sku.getProductCode().equals(productCode)) {
                listResult.add(sku);
            }
        }
        return listResult;
    }
    private void loadSaveTag(String promotionTag, ProductSaveInfo saveInfo,CmsBtJmPromotionModel model) {
        if (StringUtils.isEmpty(promotionTag)) {
            return;
        }
        CmsBtJmPromotionTagProductModel tagProductModel = null;
        String[] tagList = promotionTag.split("|");

        //获取该活动的所有tag
        List<CmsBtTagModel> listCmsBtTag = daoExtCmsBtTag.selectListByParentTagId(model.getRefTagId());

        for (String tagName : tagList) {
            for (CmsBtTagModel tagModel : listCmsBtTag) {
                if (tagModel.getTagName().equals(tagName)) {
                    if (saveInfo.productModel.getId() > 0) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("cmsBtJmPromotionProductId", saveInfo.productModel.getId());//cms_bt_jm_promotion_product_id cms_bt_tag_id
                        map.put("cmsBtTagId", tagModel.getId());
                        tagProductModel = daoCmsBtJmPromotionTagProduct.selectOne(map);
                    }
                    if (tagProductModel == null) {
                        //不存在 添加tag
                        tagProductModel = new CmsBtJmPromotionTagProductModel();
                        tagProductModel.setChannelId(model.getChannelId());
                        tagProductModel.setCmsBtTagId(tagModel.getId());
                        tagProductModel.setTagName(tagModel.getTagName());
                        saveInfo.tagList.add(tagProductModel);
                    }
                }
                else
                {
                      // TODO: 2016/5/27  不做处理
                }
                tagProductModel=null;
            }
        }
    }
    public List<ExcelColumn> getProductImportColumn() {
        List<ExcelColumn> list = new ArrayList<>();
        list.add(new ExcelColumn("productCode", "cms_bt_jm_promotion_product", "商品代码"));
        list.add(new ExcelColumn("appId", "cms_bt_jm_promotion_product", "APP端模块ID"));
        list.add(new ExcelColumn("pcId", "cms_bt_jm_promotion_product", "PC端模块ID"));
        list.add(new ExcelColumn("limit", "cms_bt_jm_promotion_product", "Deal每人限购"));
        list.add(new ExcelColumn("promotion_tag", "cms_bt_jm_promotion_product", "专场标签（以|分隔）"));
        return list;
    }
    public List<ExcelColumn> getSkuImportColumn() {
        List<ExcelColumn> list = new ArrayList<>();
        list.add(new ExcelColumn("productCode", "cms_bt_jm_promotion_sku", "商品代码"));
        list.add(new ExcelColumn("skuCode", "cms_bt_jm_promotion_sku", "APP端模块ID"));
        list.add(new ExcelColumn("dealPrice", "cms_bt_jm_promotion_sku", "PC端模块ID"));
        list.add(new ExcelColumn("marketPrice", "cms_bt_jm_promotion_sku", "Deal每人限购"));
        return list;
    }
}
