package com.voyageone.service.impl.jumei;
import com.voyageone.common.help.DateHelp;
import com.voyageone.common.help.ListHelp;
import com.voyageone.service.dao.jumei.*;
import com.voyageone.service.impl.Excel.*;
import com.voyageone.service.impl.jumei.enumjm.EnumJMProductImportColumn;
import com.voyageone.service.impl.jumei.enumjm.EnumJMSkuImportColumn;
import com.voyageone.service.model.jumei.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class JmBtPromotionExportTaskService {
    @Autowired
    JmBtPromotionExportTaskDao dao;

    public JmBtPromotionExportTaskModel get(int id) {
        return dao.get(id);
    }

    public List<JmBtPromotionExportTaskModel> getList() {
        return dao.getList();
    }

    public int update(JmBtPromotionExportTaskModel entity) {
        return dao.update(entity);
    }

    public int create(JmBtPromotionExportTaskModel entity) {
        return dao.create(entity);
    }

    public  void  Export(int JmBtPromotionExportTaskId) throws FileNotFoundException {
        JmBtPromotionExportTaskModel model = dao.get(1);
        String fileName = "/usr/JMExport" + "/Product" + DateHelp.DateToString(new Date(), "yyyyMMddHHmmss") + ".xls";
        exportProduct(fileName);
        exportSku(fileName);
    }

    private void exportProduct(String fileName) {
        EnumJMProductImportColumn[] arraysEnumJMProductImportColumn = EnumJMProductImportColumn.values();
        List<EnumJMProductImportColumn> listEnumJMProductImportColumn = Arrays.asList(arraysEnumJMProductImportColumn);
        listEnumJMProductImportColumn.sort((a, b) -> {
            if (a.getOrderIndex() > b.getOrderIndex()) return 1;
            return -1;
        });
        ExportExcelInfo<Map<String, Object>> info = new ExportExcelInfo(null);
        info.setFileName("Product");
        info.setSheet("Product");
        for(EnumJMProductImportColumn o: listEnumJMProductImportColumn) {
            info.addExcelColumn(o.getColumnName(), o.getColumnName());
        }
        try {

            ExportFileExcelUtil.exportExcel(info, fileName);
        } catch (ExcelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void exportSku(String fileName) {
        EnumJMSkuImportColumn[] arraysEnumColumn = EnumJMSkuImportColumn.values();
        List<EnumJMSkuImportColumn> listEnumColumn = Arrays.asList(arraysEnumColumn);
        listEnumColumn.sort((a, b) -> {
            if (a.getOrderIndex() > b.getOrderIndex()) return 1;
            return -1;
        });
        ExportExcelInfo<Map<String, Object>> info = new ExportExcelInfo(null);
        info.setFileName("Product");
        info.setSheet("Sku");
        for(EnumJMSkuImportColumn o: listEnumColumn) {
            info.addExcelColumn(o.getColumnName(), o.getColumnName());
        }
        try {
            ExportFileExcelUtil.exportExcel(info, fileName);
        } catch (ExcelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

