package com.voyageone.service.impl.cms.jumei2;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.util.BigDecimalUtil;
import com.voyageone.common.util.excel.ExcelColumn;
import com.voyageone.common.util.excel.ExcelImportUtil;
import com.voyageone.service.bean.cms.businessmodel.*;
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

        JmProductImportAllInfo info = new JmProductImportAllInfo();
        info.setModelCmsBtJmPromotion(modelCmsBtJmPromotion);
        info.setListProductModel(listProductModel);
        info.setListSkuModel(listSkuModel);
        info.setListSpecialImageModel(listSpecialImageModel);
        // saveJmProductImportAllInfo(info, modelCmsBtJmPromotionImportTask.getCreater());
        if (listProducctErrorMap.size() > 0 | listSkuErrorMap.size() > 0 | listSpecialErrorMap.size() > 0) {
            String failuresFileName = "error" + modelCmsBtJmPromotionImportTask.getFileName().trim();
            String errorfilePath = "/usr/JMExport/error" + modelCmsBtJmPromotionImportTask.getFileName().trim();
            // serviceCmsBtJmPromotionExportTask.export(errorfilePath, listProducctErrorMap, listSkuErrorMap, listSpecialErrorMap, true);
            modelCmsBtJmPromotionImportTask.setFailuresFileName(failuresFileName);
            modelCmsBtJmPromotionImportTask.setErrorCode(2);
            modelCmsBtJmPromotionImportTask.setFailuresRows(listProducctErrorMap.size());
        }
        modelCmsBtJmPromotionImportTask.setSuccessRows(listProductModel.size());
    }

    public List<ExcelColumn> getProductImportColumn() {
        List<ExcelColumn> list = new ArrayList<>();
        list.add(new ExcelColumn("productCode", "", ""));
        return list;
    }
}
