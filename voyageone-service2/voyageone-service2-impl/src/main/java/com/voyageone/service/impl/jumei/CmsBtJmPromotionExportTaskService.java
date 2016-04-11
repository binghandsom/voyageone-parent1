package com.voyageone.service.impl.jumei;
import com.voyageone.common.help.DateHelp;
import com.voyageone.common.util.ExceptionUtil;
import com.voyageone.service.dao.jumei.*;
import com.voyageone.service.daoext.jumei.CmsBtJmProductImagesDaoExt;
import com.voyageone.service.daoext.jumei.CmsBtJmPromotionExportTaskDaoExt;
import com.voyageone.service.daoext.jumei.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.daoext.jumei.CmsBtJmPromotionSkuDaoExt;
import com.voyageone.service.impl.Excel.ExcelException;
import com.voyageone.service.impl.Excel.ExportExcelInfo;
import com.voyageone.service.impl.Excel.ExportFileExcelUtil;
import com.voyageone.service.impl.jumei.enumjm.*;
import com.voyageone.service.model.jumei.*;
import com.voyageone.service.model.util.MapModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotionExportTaskService {
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

    public int update(CmsBtJmPromotionExportTaskModel entity) {
        return dao.update(entity);
    }

    public int insert(CmsBtJmPromotionExportTaskModel entity) {
        return dao.insert(entity);
    }

    public void export(int JmBtPromotionExportTaskId) throws IOException, ExcelException {
        CmsBtJmPromotionExportTaskModel model = dao.select(JmBtPromotionExportTaskId);
        String fileName = "Product" + DateHelp.DateToString(new Date(), "yyyyMMddHHmmssSSS") + ".xls";
        String filePath = "/usr/JMExport/" + fileName;
        model.setBeginTime(new Date());
        int TemplateType = model.getTemplateType();
        try {
            if (TemplateType == 0) {//导入模板
                List<Map<String, Object>> listProduct = daoExtCmsBtJmPromotionProduct.getListCmsBtJmImportProductByPromotionId(model.getCmsBtJmPromotionId());
                List<Map<String, Object>> listSku = daoExtCmsBtJmPromotionSku.getListCmsBtJmImportSkuByPromotionId(model.getCmsBtJmPromotionId());
                List<Map<String, Object>> listSpecialImage = getListSpecialImage(model.getCmsBtJmPromotionId(), listProduct);
                export(filePath, listProduct, listSku, listSpecialImage, false);
                model.setFileName(fileName);
            } else if (TemplateType == 1) {//价格修正模板
                List<Map<String, Object>> list = daoExtCmsBtJmPromotionSku.getJmSkuPriceInfoListByPromotionId(model.getCmsBtJmPromotionId());
                ExportExcelInfo<Map<String, Object>> info = getSkuPriceInfo(list);
                ExportFileExcelUtil.exportExcel(filePath, info);
                model.setFileName(fileName);
            } else if (TemplateType == 2) {//专场模板
                List<Map<String, Object>> list = daoExtCmsBtJmPromotionProduct.getExportInfoListByPromotionId(model.getCmsBtJmPromotionId());
                List<Map<String, Object>> pcList = new ArrayList<>();
                List<Map<String, Object>> appList = new ArrayList<>();
                loadPCAppList(list, pcList, appList);
                ExportExcelInfo<Map<String, Object>> infoPC = getJMPCPromotionProcuctExportInfo(pcList, "PC端模块");
                ExportExcelInfo<Map<String, Object>> infoApp = getJMPCPromotionProcuctExportInfo(appList, "App端模块");
                ExportFileExcelUtil.exportExcel(filePath, infoPC, infoApp);
                model.setFileName(fileName);
            }
        } catch (Exception ex) {
            model.setErrorMsg(ExceptionUtil.getErrorMsg(ex));
            model.setErrorCode(1);
            ex.printStackTrace();
        }
        model.setIsExport(true);
        model.setEndTime(new Date());
        dao.update(model);
    }
     List<Map<String,Object>> getListSpecialImage(int promotionId,List<Map<String,Object>> listProduct) {
         List<Map<String, Object>> result = new ArrayList<>();
         List<CmsBtJmProductImagesModel> listCmsBtJmProductImagesModel = daoExtCmsBtJmProductImages.getListByPromotionId(promotionId);
         Map<String, Object> mapSpecialImage = null;
         boolean isImage=false;
         for (Map<String, Object> mapProduct : listProduct) {
             String productCode = mapProduct.get("productCode").toString();
             List<CmsBtJmProductImagesModel> codeProductImagesList = getListCmsBtJmProductImagesModelByProdcutCode(listCmsBtJmProductImagesModel, productCode);
             mapSpecialImage = mapSpecialImage = new HashMap<>();

             isImage = false;
             for (CmsBtJmProductImagesModel model : codeProductImagesList) {

                 if (model.getImageType() == 3)//参数图
                 {
                     isImage = true;
                     mapSpecialImage.put("propertyImage" + model.getImageIndex(), model.getOriginUrl());
                 } else if (model.getImageType() == 8)//商品定制图
                 {
                     isImage = true;
                     mapSpecialImage.put("specialImage" + model.getImageIndex(), model.getOriginUrl());
                 } else if (model.getImageType() == 1 || model.getImageType() == 2 || model.getImageType() == 7)//1:白底方图 ；2:商品详情图；7：竖图
                 {
                     isImage = true;
                     mapSpecialImage.put("productImageUrlKey" + model.getImageIndex(), model.getProductImageUrlKey());
                 } else {
                     continue;
                 }
             }
             if (isImage) {
                 mapSpecialImage.put("productCode", productCode);
                 result.add(mapSpecialImage);
             }
         }
         return result;
     }
     public List<CmsBtJmProductImagesModel> getListCmsBtJmProductImagesModelByProdcutCode( List<CmsBtJmProductImagesModel> listCmsBtJmProductImagesModel ,String productCode) {
         List<CmsBtJmProductImagesModel> result = new ArrayList<>();
         for (CmsBtJmProductImagesModel model : listCmsBtJmProductImagesModel) {
             if (model.getProductCode().equals(productCode)) {
                 result.add(model);
             }
         }
         return result;
     }
    public void loadPCAppList( List<Map<String, Object>> list,List<Map<String, Object>> mapPcList,List<Map<String, Object>> mapAppList) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            if (!StringUtils.isEmpty(map.get("appId"))) {
                mapAppList.add(map);
            }
            if (!StringUtils.isEmpty(map.get("pcId"))) {
                mapPcList.add(map);
            }
        }
    }
    private ExportExcelInfo<Map<String, Object>> getJMPCPromotionProcuctExportInfo( List<Map<String, Object>> dataSource,String sheetName) {
        List<EnumJMPCPromotionProcuctExportColumn> listEnumColumn = EnumJMPCPromotionProcuctExportColumn.getList();
        ExportExcelInfo<Map<String, Object>> info = new ExportExcelInfo(null);
        info.setFileName("Product");
        info.setSheet(sheetName);
        info.setDisplayColumnName(true);
        info.setDataSource(dataSource);
        for (EnumJMPCPromotionProcuctExportColumn o : listEnumColumn) {
            info.addExcelColumn(o.getExcelColumn());
        }
        return info;
    }
    private ExportExcelInfo<Map<String, Object>> getSkuPriceInfo( List<Map<String, Object>> dataSource) {
        List<EnumJMSkuPriceExportColumn> listEnumColumn = EnumJMSkuPriceExportColumn.getList();
        ExportExcelInfo<Map<String, Object>> info = new ExportExcelInfo(null);
        info.setFileName("Product");
        info.setSheet("价格修正");
        info.setDisplayColumnName(true);
        info.setDataSource(dataSource);
        for (EnumJMSkuPriceExportColumn o : listEnumColumn) {
            info.addExcelColumn(o.getExcelColumn());
        }
        return info;
    }
    public void export(String fileName, List<Map<String, Object>> dataSourceProduct, List<Map<String, Object>> dataSourceSku, List<Map<String, Object>> dataSourceSpecialImageInfo,boolean isErrorColumn) {
        ExportExcelInfo<Map<String, Object>> productInfo = getExportProductInfo(fileName, dataSourceProduct);
        ExportExcelInfo<Map<String, Object>> skuInfo = getExportSkuInfo(fileName, dataSourceSku);
        ExportExcelInfo<Map<String, Object>> specialImageInfo = getExportSpecialImageInfo(fileName, dataSourceSpecialImageInfo);
        if (isErrorColumn) {
            productInfo.addExcelColumn(productInfo.getErrorColumn());
            skuInfo.addExcelColumn(skuInfo.getErrorColumn());
            specialImageInfo.addExcelColumn(specialImageInfo.getErrorColumn());
        }
        try {
            ExportFileExcelUtil.exportExcel(fileName, productInfo, skuInfo, specialImageInfo);
        } catch (ExcelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ExportExcelInfo<Map<String, Object>> getExportProductInfo(String fileName, List<Map<String, Object>> dataSource) {
        List<EnumJMProductImportColumn> listEnumJMProductImportColumn = EnumJMProductImportColumn.getList();
        ExportExcelInfo<Map<String, Object>> info = new ExportExcelInfo(null);
        info.setFileName("Product");
        info.setSheet("Product");
        info.setDisplayColumnName(true);
        info.setDataSource(dataSource);
        for (EnumJMProductImportColumn o : listEnumJMProductImportColumn) {
            info.addExcelColumn(o.getExcelColumn());
        }
        return info;
    }

    private ExportExcelInfo<Map<String, Object>> getExportSkuInfo(String fileName, List<Map<String, Object>> dataSource) {
        List<EnumJMSkuImportColumn> listEnumColumn = EnumJMSkuImportColumn.getList();
        ExportExcelInfo<Map<String, Object>> info = new ExportExcelInfo(null);
        info.setFileName("Product");
        info.setSheet("Sku");
        info.setDisplayColumnName(true);
        info.setDataSource(dataSource);
        for (EnumJMSkuImportColumn o : listEnumColumn) {
            info.addExcelColumn(o.getExcelColumn());
        }
        return info;
    }

    private ExportExcelInfo<Map<String, Object>> getExportSpecialImageInfo(String fileName, List<Map<String, Object>> dataSource) {
        List<EnumJMSpecialImageImportColumn> listEnumColumn = EnumJMSpecialImageImportColumn.getList();
        ExportExcelInfo<Map<String, Object>> info = new ExportExcelInfo(null);
        info.setFileName("Product");
        info.setSheet("SpecialImage");
        info.setDisplayColumnName(true);
        info.setDataSource(dataSource);
        for (EnumJMSpecialImageImportColumn o : listEnumColumn) {
            info.addExcelColumn(o.getExcelColumn());
        }
        return info;
    }

    public List<CmsBtJmPromotionExportTaskModel> getByPromotionId(int promotionId) {
        return daoExt.getByPromotionId(promotionId);
    }
}

