package com.voyageone.service.impl.jumei;
import com.voyageone.common.help.DateHelp;
import com.voyageone.service.dao.jumei.*;
import com.voyageone.service.daoext.jumei.CmsBtJmPromotionExportTaskDaoExt;
import com.voyageone.service.daoext.jumei.CmsBtJmPromotionSkuDaoExt;
import com.voyageone.service.impl.Excel.ExcelException;
import com.voyageone.service.impl.Excel.ExportExcelInfo;
import com.voyageone.service.impl.Excel.ExportFileExcelUtil;
import com.voyageone.service.impl.jumei.enumjm.EnumJMProductImportColumn;
import com.voyageone.service.impl.jumei.enumjm.EnumJMSkuImportColumn;
import com.voyageone.service.impl.jumei.enumjm.EnumJMSpecialImageImportColumn;
import com.voyageone.service.model.jumei.*;
import com.voyageone.service.model.util.MapModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
        CmsBtJmPromotionExportTaskModel model = dao.select(1);
        String fileName = "/usr/JMExport" + "/Product" + DateHelp.DateToString(new Date(), "yyyyMMddHHmmssSSS") + ".xls";
        int TemplateType = model.getTemplateType();
        if (TemplateType == 0) {//导入模板
            export(fileName, null, null, null, false);
        } else if (TemplateType == 1) {//价格修正模板
            List<Map<String, Object>> list = null;//daoExtCmsBtJmPromotionSku.getJmSkuPriceInfoListByPromotionId(model.getId());
            ExportExcelInfo<Map<String, Object>> info = getSkuPriceInfo(list);
            ExportFileExcelUtil.exportExcel(fileName, info);
        } else if (TemplateType == 2) {//专场模板

        }
    }
    private ExportExcelInfo<Map<String, Object>> getSkuPriceInfo( List<Map<String, Object>> dataSource) {
        List<EnumJMSpecialImageImportColumn> listEnumColumn = EnumJMSpecialImageImportColumn.getList();
        ExportExcelInfo<Map<String, Object>> info = new ExportExcelInfo(null);
        info.setFileName("Product");
        info.setSheet("价格修正");
        info.setDisplayColumnName(true);
        info.setDataSource(dataSource);
        for (EnumJMSpecialImageImportColumn o : listEnumColumn) {
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

