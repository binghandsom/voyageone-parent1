package com.voyageone.service.impl.cms.jumei2;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ExceptionUtil;
import com.voyageone.common.util.excel.ExcelColumn;
import com.voyageone.common.util.excel.ExcelException;
import com.voyageone.common.util.excel.ExportExcelInfo;
import com.voyageone.common.util.excel.ExportFileExcelUtil;
import com.voyageone.service.dao.cms.CmsBtJmPromotionExportTaskDao;
import com.voyageone.service.daoext.cms.CmsBtJmProductImagesDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionExportTaskDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionSkuDaoExt;
import com.voyageone.service.impl.cms.jumei.enumjm.*;
import com.voyageone.service.model.cms.CmsBtJmProductImagesModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionExportTaskModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotionExportTask3Service {
    @Autowired
    CmsBtJmPromotionExportTaskDao dao;
    @Autowired
    CmsBtJmPromotionExportTaskDaoExt daoExt;
    @Autowired
    CmsBtJmPromotionSkuDaoExt daoExtCmsBtJmPromotionSku;
    @Autowired
    CmsBtJmPromotionProductDaoExt daoExtCmsBtJmPromotionProduct;
    @Autowired
    CmsBtJmProductImagesDaoExt daoExtCmsBtJmProductImages;
    public CmsBtJmPromotionExportTaskModel get(int id) {
        return dao.select(id);
    }
    public List<CmsBtJmPromotionExportTaskModel> getByPromotionId(int promotionId) {
        return daoExt.getByPromotionId(promotionId);
    }
    public void export(int JmBtPromotionExportTaskId, String exportPath) throws IOException, ExcelException {
        CmsBtJmPromotionExportTaskModel model = dao.select(JmBtPromotionExportTaskId);
        String fileName = "Product" + DateTimeUtil.format(new Date(), "yyyyMMddHHmmssSSS") + ".xls";
        //"/usr/JMExport/"
        String filePath = exportPath + "/" + fileName;
        model.setBeginTime(new Date());
        int TemplateType = model.getTemplateType();
        try {
            dao.update(model);
            List<Map<String, Object>> listProduct = daoExtCmsBtJmPromotionProduct.getExportListByPromotionId(model.getCmsBtJmPromotionId());
            List<Map<String, Object>> listSku = daoExtCmsBtJmPromotionSku.getExportListByPromotionId(model.getCmsBtJmPromotionId());
            export(filePath, listProduct, listSku, false);
            model.setSuccessRows(listProduct.size());
            model.setFileName(fileName);
        } catch (Exception ex) {
            model.setErrorMsg(ExceptionUtil.getErrorMsg(ex));
            model.setErrorCode(1);
            ex.printStackTrace();
        }
        model.setIsExport(true);
        model.setEndTime(new Date());
        dao.update(model);
    }
    public void export(String fileName, List<Map<String, Object>> dataSourceProduct, List<Map<String, Object>> dataSourceSku, boolean isErrorColumn) {
        ExportExcelInfo<Map<String, Object>> productInfo = getProductExportExcelInfo(dataSourceProduct,isErrorColumn);
        ExportExcelInfo<Map<String, Object>> skuInfo = getSkuExportExcelInfo(dataSourceSku,isErrorColumn);
        try {
            ExportFileExcelUtil.exportExcel(fileName, productInfo, skuInfo);
        } catch (ExcelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  ExportExcelInfo<Map<String, Object>> getProductExportExcelInfo(List<Map<String, Object>> dataSource,boolean isErrorColumn) {
        ExportExcelInfo<Map<String, Object>> info = new ExportExcelInfo(null);
        info.setFileName("Product");
        info.setSheet("Product");
        info.setDisplayColumnName(true);
        info.setDataSource(dataSource);
        info.addExcelColumn("商品代码","productCode", "cms_bt_jm_promotion_product");
        info.addExcelColumn("APP端模块ID","appId", "cms_bt_jm_promotion_product");
        info.addExcelColumn("PC端模块ID","pcId", "cms_bt_jm_promotion_product");
        info.addExcelColumn("Deal每人限购","limit", "cms_bt_jm_promotion_product");
        info.addExcelColumn( "专场标签（以|分隔）","promotion_tag", "cms_bt_jm_promotion_product");


        info.addExcelColumn("商品英文名称","foreignLanguageName", "cms_bt_jm_product");
        info.addExcelColumn("商品中文名称","productNameCn", "cms_bt_jm_product");
        info.addExcelColumn( "长标题","productLongName", "cms_bt_jm_product");
        info.addExcelColumn("中标题","productMediumName", "cms_bt_jm_product");
        info.addExcelColumn("短标题","productShortName", "cms_bt_jm_product");
        info.addExcelColumn("自定义搜索词","searchMetaTextCustom", "cms_bt_jm_product");
        info.addExcelColumn( "英文产地","origin", "cms_bt_jm_product");
        info.addExcelColumn( "中文产地","addressOfProduce", "cms_bt_jm_product");
        info.addExcelColumn( "适合人群","applicableCrowd", "cms_bt_jm_product");
        info.addExcelColumn( "适合人群","specialNote", "cms_bt_jm_product");
        info.addExcelColumn( "英文颜色","colorEn", "cms_bt_jm_product");
        info.addExcelColumn( "中文颜色","attribute", "cms_bt_jm_product");
        info.addExcelColumn("主数据品牌名称","brandName", "cms_bt_jm_product");
        info.addExcelColumn("聚美品牌名称","brandName", "cms_bt_jm_product");
        info.addExcelColumn("商品类别","productType", "cms_bt_jm_product");
        info.addExcelColumn("尺码类别","sizeType", "cms_bt_jm_product");
        info.addExcelColumn("使用方法_产品介绍","productDesEn", "cms_bt_jm_product");
        info.addExcelColumn( "使用方法_产品介绍","productDesCn", "cms_bt_jm_product");
        if (isErrorColumn) {
            info.addExcelColumn(info.getErrorColumn());
        }
        return info;
    }
    public   ExportExcelInfo<Map<String, Object>>  getSkuExportExcelInfo(List<Map<String, Object>> dataSource,boolean isErrorColumn) {
        ExportExcelInfo<Map<String, Object>> info = new ExportExcelInfo(null);
        info.setFileName("Product");
        info.setSheet("Sku");
        info.setDisplayColumnName(true);
        info.setDataSource(dataSource);
        info.addExcelColumn("商品代码", "productCode", "cms_bt_jm_promotion_sku");
        info.addExcelColumn("APP端模块ID", "skuCode", "cms_bt_jm_promotion_sku");
        info.addExcelColumn("PC端模块ID", "dealPrice", "cms_bt_jm_promotion_sku");
        info.addExcelColumn("Deal每人限购", "marketPrice", "cms_bt_jm_promotion_sku");
        info.addExcelColumn("规格", "format", "cms_bt_jm_promotion_sku");
        info.addExcelColumn("商品条形码", "upc", "cms_bt_jm_promotion_sku");
        info.addExcelColumn("尺码(VO系统)", "cmsSize", "cms_bt_jm_promotion_sku");
        info.addExcelColumn("容量/尺码（聚美系统", "jmSize", "cms_bt_jm_promotion_sku");
        info.addExcelColumn("海外官网价格", "msrpUsd", "cms_bt_jm_promotion_sku");
        info.addExcelColumn("中国官网价格", "msrpRmb", "cms_bt_jm_promotion_sku");
        info.addExcelColumn("中国指导售价", "retailPrice", "cms_bt_jm_promotion_sku");
        info.addExcelColumn("中国最终售价", "salePrice", "cms_bt_jm_promotion_sku");
        if (isErrorColumn) {
            info.addExcelColumn(info.getErrorColumn());
        }
        return info;
    }
    public  void  insert(CmsBtJmPromotionExportTaskModel model) {
        model.setErrorMsg("");
        model.setBeginTime(DateTimeUtil.getCreatedDefaultDate());
        model.setEndTime(DateTimeUtil.getCreatedDefaultDate());
        model.setErrorCode(0);
        model.setIsExport(false);
        model.setSuccessRows(0);
        model.setFileName("");
        model.setFilePath("");
        dao.insert(model);
    }
}

