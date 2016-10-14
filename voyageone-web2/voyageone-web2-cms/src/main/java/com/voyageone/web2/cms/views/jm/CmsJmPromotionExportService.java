package com.voyageone.web2.cms.views.jm;


import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ExcelUtils;
import com.voyageone.common.util.FileUtils;
import com.voyageone.service.impl.CmsProperty;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionSpecialExtensionModel;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.cms.bean.CmsPromotionExportBean;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * 聚美活动新增商品
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsJmPromotionExportService extends BaseViewService {

    @Autowired
    private CmsBtJmPromotionService cmsBtJmPromotionService;

    public byte[] doExportJmPromotionFile(Integer jmPromotionId) {

        CmsBtJmPromotionModel cmsBtJmPromotionModel = cmsBtJmPromotionService.select(jmPromotionId);
        CmsBtJmPromotionSpecialExtensionModel jmPromotionSpecialExtensionModel = cmsBtJmPromotionService.getJmPromotionSpecial(jmPromotionId);

        try {
            return writeRecordToJmPromotionInfo(cmsBtJmPromotionModel, jmPromotionSpecialExtensionModel, Arrays.asList("aaaa","vvv"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] writeRecordToJmPromotionInfo(CmsBtJmPromotionModel cmsBtJmPromotionModel, CmsBtJmPromotionSpecialExtensionModel cmsBtJmPromotionSpecialExtensionModel, List<String> HashIdList) throws Exception {
        String templatePath = "/usr/web/contents/cms/file_template/JMPromotion-template.xlsx";


        $info("准备打开文档 [ %s ]", templatePath);

        try (InputStream inputStream = new FileInputStream(templatePath);
             Workbook book = WorkbookFactory.create(inputStream)) {
            Sheet sheet = book.getSheetAt(0);
            Row styleRow = FileUtils.row(sheet, 2);
            CellStyle unlock = styleRow.getRowStyle();
            int rowIndex = 1;

            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionSpecialExtensionModel.getDisplayPlatform(), unlock);
            rowIndex++;
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex), 2, cmsBtJmPromotionModel.getCmsBtJmMasterBrandId(), unlock);
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 3, cmsBtJmPromotionModel.getBrand(), unlock);

            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionModel.getActivityPcId(), unlock);
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionModel.getActivityAppId(), unlock);
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionSpecialExtensionModel.getSessionType(), unlock);
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionSpecialExtensionModel.getSessionCategory(), unlock);
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionSpecialExtensionModel.getPreDisplayChannel(), unlock);
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionSpecialExtensionModel.getMainTitle(), unlock);
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionSpecialExtensionModel.getMarketingTitle(), unlock);
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionSpecialExtensionModel.getMarketingCopywriter(), unlock);
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionSpecialExtensionModel.getPromotionalCopy(), unlock);
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionSpecialExtensionModel.getPcPageId(), unlock);
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionSpecialExtensionModel.getAppPageId(), unlock);
            String date = DateTimeUtil.format(cmsBtJmPromotionModel.getPrePeriodStart(), null) + " - " + DateTimeUtil.format(cmsBtJmPromotionModel.getPrePeriodEnd(), null);
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, date, unlock);
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, DateTimeUtil.format(cmsBtJmPromotionModel.getActivityStart(), null), unlock);
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, DateTimeUtil.format(cmsBtJmPromotionModel.getActivityEnd(), null), unlock);
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionSpecialExtensionModel.getSyncMobile(), unlock);
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionSpecialExtensionModel.getShowHiddenDeal(), unlock);
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionSpecialExtensionModel.getShowSoldOutDeal(), unlock);
            rowIndex++;
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionSpecialExtensionModel.getShareTitle(), unlock);
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionSpecialExtensionModel.getShareContent(), unlock);
            rowIndex++;
            for(String hashId : HashIdList){
                ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, hashId, unlock);
            }


            $info("文档写入完成");

            try (OutputStream outputStream = new FileOutputStream("/usr/web/contents/cms/file_template/JMPromotion.xlsx")) {
                book.write(outputStream);
                outputStream.close();
                book.close();
            }
//            /* 返回值设定 */
//            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//
//                book.write(outputStream);
//
//                $info("已写入输出流");
//
//                return outputStream.toByteArray();
//            }
        }

        return null;
    }

}

