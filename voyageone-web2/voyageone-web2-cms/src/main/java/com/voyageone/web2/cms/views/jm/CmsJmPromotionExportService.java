package com.voyageone.web2.cms.views.jm;


import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ExcelUtils;
import com.voyageone.common.util.FileUtils;
import com.voyageone.service.bean.cms.jumei.CmsBtJmPromotionExportBean;
import com.voyageone.service.bean.cms.jumei.CmsBtJmPromotionSaveBean;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.impl.CmsProperty;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionSpecialExtensionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.cms.bean.CmsPromotionExportBean;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 聚美活动新增商品
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsJmPromotionExportService extends BaseViewService {

    @Autowired
    private CmsBtJmPromotionService cmsBtJmPromotionService;

    @Autowired
    private CmsBtJmPromotionProductDaoExt cmsBtJmPromotionProductDaoExt;

    public byte[] doExportJmPromotionFile(Integer jmPromotionId) {

        String templatePath = "/usr/web/contents/cms/file_template/JMPromotion-template.xlsx";
        CmsBtJmPromotionSaveBean cmsBtJmPromotionSaveBean = cmsBtJmPromotionService.getEditModel(jmPromotionId, true);

        $info("准备打开文档 [ %s ]", templatePath);

        try (InputStream inputStream = new FileInputStream(templatePath)) {
            Workbook book = WorkbookFactory.create(inputStream);
            writeRecordToJmPromotionInfo(book, cmsBtJmPromotionSaveBean, Arrays.asList("aaaa", "vvv"));
            writeRecordToJmModeInfoMain(book,cmsBtJmPromotionSaveBean.getTagList());

            $info("文档写入完成");
            try (OutputStream outputStream = new FileOutputStream("/usr/web/contents/cms/file_template/JMPromotion" + DateTimeUtil.format(new Date(), DateTimeUtil.DATE_TIME_FORMAT_2) + ".xlsx")) {
                book.write(outputStream);
                outputStream.close();
                book.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writeRecordToJmPromotionInfo(Workbook book, CmsBtJmPromotionSaveBean cmsBtJmPromotionSaveBean, List<String> HashIdList) throws Exception {
        CmsBtJmPromotionExportBean cmsBtJmPromotionExportBean = new CmsBtJmPromotionExportBean(cmsBtJmPromotionSaveBean);
        Sheet sheet = book.getSheetAt(0);
        Row styleRow = FileUtils.row(sheet, 2);
        CellStyle unlock = styleRow.getRowStyle();
        int rowIndex = 1;

        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportBean.getExtModel().getDisplayPlatform(), unlock);
        rowIndex++;
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportBean.getBrandString(), unlock);

        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportBean.getModel().getActivityPcId(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportBean.getModel().getActivityAppId(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportBean.getExtModel().getSessionType(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportBean.getExtModel().getSessionCategory(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportBean.getExtModel().getPreDisplayChannel(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportBean.getExtModel().getMainTitle(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportBean.getExtModel().getMarketingTitle(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportBean.getExtModel().getMarketingCopywriter(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportBean.getExtModel().getPromotionalCopy(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportBean.getExtModel().getPcPageId(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportBean.getExtModel().getAppPageId(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, DateTimeUtil.format(cmsBtJmPromotionExportBean.getModel().getPrePeriodStart(), null), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, DateTimeUtil.format(cmsBtJmPromotionExportBean.getModel().getActivityStart(), null), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, DateTimeUtil.format(cmsBtJmPromotionExportBean.getModel().getActivityEnd(), null), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportBean.getExtModel().getSyncMobile(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportBean.getExtModel().getShowHiddenDeal(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportBean.getExtModel().getShowSoldOutDeal(), unlock);
        rowIndex++;
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportBean.getExtModel().getShareTitle(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportBean.getExtModel().getShareContent(), unlock);
        rowIndex++;
        for (String hashId : HashIdList) {
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, hashId, unlock);
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

    private void writeRecordToJmModeInfoMain(Workbook book, List<CmsBtTagModel> tags) {

        tags.forEach(cmsBtTagModel -> {
            List<CmsBtJmPromotionProductModel> cmsBtJmPromotionProductModels = cmsBtJmPromotionProductDaoExt.selectProductInfoByTagId(cmsBtTagModel.getId());
            if(cmsBtJmPromotionProductModels != null && cmsBtJmPromotionProductModels.size()>0){
                List<List<CmsBtJmPromotionProductModel>> products = CommonUtil.splitList(cmsBtJmPromotionProductModels,100);
                for(int i=0;i<products.size();i++){
                    if(i == 0){
                        writeRecordToJmModeInfo(book, cmsBtTagModel, products.get(i), cmsBtTagModel.getTagName());
                    }else{
                        writeRecordToJmModeInfo(book, cmsBtTagModel, products.get(i), cmsBtTagModel.getTagName()+(i+1));
                    }
                }

            }

        });

    }

    private void writeRecordToJmModeInfo(Workbook book, CmsBtTagModel tags, List<CmsBtJmPromotionProductModel> cmsBtJmPromotionProductModels, String modelName) {

        Sheet sheet = book.createSheet(String.format("页面内容-货架【%s】（专场特卖用）",modelName));
        try {
            ExcelUtils.copySheet((XSSFSheet)sheet,(XSSFSheet)book.getSheetAt(3),(XSSFWorkbook)book,(XSSFWorkbook)book,true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Row styleRow = FileUtils.row(sheet, 2);
        CellStyle unlock = styleRow.getRowStyle();
        int rowIndex = 3;

        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, tags.getTagName(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, tags.getTagExt().getHideFlag(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, tags.getTagExt().getShelfType(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, tags.getTagExt().getImageType(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, tags.getTagExt().getModuleTitle(), unlock);
        rowIndex++;
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, tags.getTagExt().getProductsSortBy(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, tags.getTagExt().getNoStockToLast(), unlock);
        for(CmsBtJmPromotionProductModel cmsBtJmPromotionProductModel:cmsBtJmPromotionProductModels){
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionProductModel.getJmHashId(), unlock);
        }
    }
}

