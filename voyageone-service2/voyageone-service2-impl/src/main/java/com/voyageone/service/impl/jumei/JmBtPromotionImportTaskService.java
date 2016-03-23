package com.voyageone.service.impl.jumei;
import com.voyageone.common.help.DateHelp;
import com.voyageone.common.util.ExcelUtils;
import com.voyageone.service.dao.jumei.*;
import com.voyageone.service.impl.jumei.enumjm.EnumJMProductImportColumn;
import com.voyageone.service.model.jumei.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class JmBtPromotionImportTaskService {
    @Autowired
    JmBtPromotionImportTaskDao dao;

    public JmBtPromotionImportTaskModel get(int id) {
        return dao.get(id);
    }

    public List<JmBtPromotionImportTaskModel> getList() {
        return dao.getList();
    }

    public int update(JmBtPromotionImportTaskModel entity) {
        return dao.update(entity);
    }

    public int create(JmBtPromotionImportTaskModel entity) {
        return dao.create(entity);
    }

    public  void  Import(int JmBtPromotionImportTaskId) throws IOException, InvalidFormatException {
        String filePath = "/usr/JMImport" + "/Product20160323144200.xls";
        File excelFile = new File(filePath);
        InputStream fileInputStream = new FileInputStream(excelFile);
        Workbook book = WorkbookFactory.create(fileInputStream);
        Sheet productSheet = book.getSheet("Product");
        Sheet skuSheet = book.getSheet("Sku");
        int LastCellNum = productSheet.getRow(0).getLastCellNum();
        int LastRowNum= productSheet.getLastRowNum();
        for (int i=1;i<LastRowNum;i++) {
            JmBtPromotionProductModel model=new JmBtPromotionProductModel();
            model.setProductCode(ExcelUtils.getString(productSheet.getRow(i), EnumJMProductImportColumn.ProductCode.getExcelColumn().getOrderIndex()));
        }
    }
    public boolean isProductHeadRow(Row row) {
        return false;
    }
}

