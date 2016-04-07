package com.voyageone.service.impl.jumei;
import com.voyageone.common.help.DateHelp;
import com.voyageone.service.dao.jumei.*;
import com.voyageone.service.daoext.jumei.CmsBtJmPromotionExportTaskDaoExt;
import com.voyageone.service.impl.Excel.ExcelException;
import com.voyageone.service.impl.Excel.ExportExcelInfo;
import com.voyageone.service.impl.Excel.ExportFileExcelUtil;
import com.voyageone.service.impl.jumei.enumjm.EnumJMProductImportColumn;
import com.voyageone.service.impl.jumei.enumjm.EnumJMSkuImportColumn;
import com.voyageone.service.model.jumei.*;
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
    public CmsBtJmPromotionExportTaskModel get(int id) {
        return dao.select(id);
    }
    public int update(CmsBtJmPromotionExportTaskModel entity) {
        return dao.update(entity);
    }
    public int create(CmsBtJmPromotionExportTaskModel entity) {
        return dao.insert(entity);
    }
    public void Export(int JmBtPromotionExportTaskId) throws FileNotFoundException {
        CmsBtJmPromotionExportTaskModel model = dao.select(1);
        String fileName = "/usr/JMExport" + "/Product" + DateHelp.DateToString(new Date(), "yyyyMMddHHmmss") + ".xls";
        ExportExcelInfo<Map<String, Object>> productInfo = exportProduct(fileName);
        ExportExcelInfo<Map<String, Object>> skuInfo = exportSku(fileName);
        try {
            ExportFileExcelUtil.exportExcel(fileName, productInfo, skuInfo);
        } catch (ExcelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private ExportExcelInfo<Map<String, Object>> exportProduct(String fileName) {
        List<EnumJMProductImportColumn> listEnumJMProductImportColumn = EnumJMProductImportColumn.getList();
        ExportExcelInfo<Map<String, Object>> info = new ExportExcelInfo(null);
        info.setFileName("Product");
        info.setSheet("Product");
        info.setDisplayColumnName(true);
        for (EnumJMProductImportColumn o : listEnumJMProductImportColumn) {
            info.addExcelColumn(o.getExcelColumn());
        }
        return info;
    }
    private ExportExcelInfo<Map<String, Object>> exportSku(String fileName) {
        List<EnumJMSkuImportColumn> listEnumColumn = EnumJMSkuImportColumn.getList();
        ExportExcelInfo<Map<String, Object>> info = new ExportExcelInfo(null);
        info.setFileName("Product");
        info.setSheet("Sku");
        info.setDisplayColumnName(true);
        for (EnumJMSkuImportColumn o : listEnumColumn) {
            info.addExcelColumn(o.getExcelColumn());
        }
        return info;
    }
    public List<CmsBtJmPromotionExportTaskModel> getByPromotionId(int promotionId) {
        return daoExt.getByPromotionId(promotionId);
    }
}

