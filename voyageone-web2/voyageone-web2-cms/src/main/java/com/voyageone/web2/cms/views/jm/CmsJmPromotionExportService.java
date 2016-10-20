package com.voyageone.web2.cms.views.jm;


import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ExcelUtils;
import com.voyageone.common.util.FileUtils;
import com.voyageone.service.bean.cms.CmsBtTagJmModuleExtensionBean;
import com.voyageone.service.bean.cms.jumei.CmsBtJmPromotionSaveBean;
import com.voyageone.service.dao.cms.CmsBtTagJmModuleExtensionDao;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.impl.cms.CmsBtJmBayWindowService;
import com.voyageone.service.impl.cms.jumei.CmsBtJmImageTemplateService;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.impl.cms.jumei.CmsMtJmConfigService;
import com.voyageone.service.model.cms.*;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsBtJmBayWindowModel;
import com.voyageone.web2.base.BaseViewService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
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

    public static final Integer HASHID = 0;

    public static final Integer MALLID = 1;
    @Autowired
    private CmsBtJmPromotionService cmsBtJmPromotionService;

    @Autowired
    private CmsMtJmConfigService cmsMtJmConfigService;

    @Autowired
    private CmsBtJmPromotionProductDaoExt cmsBtJmPromotionProductDaoExt;

    @Autowired
    private CmsBtTagJmModuleExtensionDao cmsBtTagJmModuleExtensionDao;
    
    @Autowired
    private CmsBtJmBayWindowService cmsBtJmBayWindowService;

    @Autowired
    private CmsBtJmImageTemplateService cmsBtJmImageTemplateService;

    public byte[] doExportJmPromotionFile(Integer jmPromotionId, Integer type) {

        String templatePath = "/usr/web/contents/cms/file_template/JMPromotion-template.xlsx";
        CmsBtJmPromotionSaveBean cmsBtJmPromotionSaveBean = cmsBtJmPromotionService.getEditModel(jmPromotionId, true);

        $info("准备打开文档 [ %s ]", templatePath);

        try (InputStream inputStream = new FileInputStream(templatePath)) {
            Workbook book = WorkbookFactory.create(inputStream);
            writeRecordToJmPromotionInfo(book, cmsBtJmPromotionSaveBean, Arrays.asList("aaaa", "vvv"));
            writeRecordToJmModeInfoMain(book,cmsBtJmPromotionSaveBean.getTagList(), type);
            writeRecordToJBayWindow(book,jmPromotionId);
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

    private void writeRecordToJBayWindow(Workbook book, Integer jmPromotionId) {
        CmsBtJmBayWindowModel cmsBtJmBayWindowModel = cmsBtJmBayWindowService.getBayWindowByJmPromotionId(jmPromotionId);
        if(cmsBtJmBayWindowModel != null) {
            Sheet sheet = book.getSheetAt(2);
            XSSFCellStyle unlock = (XSSFCellStyle) book.createCellStyle();
            unlock.setBorderBottom(CellStyle.BORDER_THIN);
            unlock.setBorderTop(CellStyle.BORDER_THIN);
            unlock.setBorderLeft(CellStyle.BORDER_THIN);
            unlock.setBorderRight(CellStyle.BORDER_THIN);
            int rowIndex = 2;
            cmsBtJmBayWindowModel.getBayWindows().sort((o1, o2) -> o1.getOrder() > o2.getOrder() ? 1 : -1);
            int i = 1;
            for(CmsBtJmBayWindowModel.BayWindow bayWindow : cmsBtJmBayWindowModel.getBayWindows()){
                ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex), 0, i++ , unlock);
                ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex), 1, "定位飘窗", unlock);
                ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex), 2, bayWindow.getName(), unlock);
                ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex), 3, bayWindow.getUrl(), unlock);
                ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 4, bayWindow.getLink(), unlock);
            }

            String[] a= {"a","b"};
            String.format("aa%s",a);

        }
    }

    private void writeRecordToJmPromotionInfo(Workbook book, CmsBtJmPromotionSaveBean cmsBtJmPromotionSaveBean, List<String> HashIdList) throws Exception {
        CmsBtJmPromotionExportContext cmsBtJmPromotionExportContext = new CmsBtJmPromotionExportContext(cmsBtJmPromotionSaveBean, cmsMtJmConfigService);
        Sheet sheet = book.getSheetAt(0);
        Row styleRow = FileUtils.row(sheet, 2);
        CellStyle unlock = styleRow.getRowStyle();
        int rowIndex = 1;

        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportContext.getDisplayPlatform(), unlock);
        rowIndex++;
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportContext.getBrandString(), unlock);

        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportContext.getModel().getActivityPcId(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportContext.getModel().getActivityAppId(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportContext.getExtModel().getSessionType(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportContext.getSessionCategory(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportContext.getPreDisplayChannel(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportContext.getExtModel().getMainTitle(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportContext.getExtModel().getMarketingTitle(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportContext.getExtModel().getMarketingCopywriter(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportContext.getExtModel().getPromotionalCopy(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportContext.getExtModel().getPcPageId(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportContext.getExtModel().getAppPageId(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, DateTimeUtil.format(cmsBtJmPromotionExportContext.getModel().getPrePeriodStart(), null), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, DateTimeUtil.format(cmsBtJmPromotionExportContext.getModel().getActivityStart(), null), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, DateTimeUtil.format(cmsBtJmPromotionExportContext.getModel().getActivityEnd(), null), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportContext.getSyncMobile(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportContext.getShowHiddenDeal(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportContext.getShowSoldOutDeal(), unlock);
        rowIndex++;
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportContext.getExtModel().getShareTitle(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportContext.getExtModel().getShareContent(), unlock);
        rowIndex++;

    }

    private void writeRecordToJmModeInfoMain(Workbook book, List<CmsBtTagModel> tags, Integer type) {

        tags.forEach(cmsBtTagModel -> {
            CmsBtTagJmModuleExtensionModel cmsBtTagJmModuleExtensionModel = cmsBtTagJmModuleExtensionDao.select(cmsBtTagModel.getId());
            List<CmsBtJmPromotionProductExtModel> cmsBtJmPromotionProductModels = cmsBtJmPromotionProductDaoExt.selectProductInfoByTagId(cmsBtTagModel.getId());
            if(cmsBtJmPromotionProductModels != null && cmsBtJmPromotionProductModels.size()>0){
                List<List<CmsBtJmPromotionProductExtModel>> products = CommonUtil.splitList(cmsBtJmPromotionProductModels,100);
                for(int i=0;i<products.size();i++){
                    if(cmsBtTagJmModuleExtensionModel.getFeatured()){
                        writeRecordToFeaturedHashId(book,products.get(i), type);
                    }else{
                        if(i == 0){
                            writeRecordToJmModeInfo(book, cmsBtTagModel, cmsBtTagJmModuleExtensionModel, products.get(i), cmsBtTagModel.getTagName(), type);
                        }else{
                            writeRecordToJmModeInfo(book, cmsBtTagModel, cmsBtTagJmModuleExtensionModel, products.get(i), cmsBtTagModel.getTagName()+(i+1), type);
                        }
                    }
                }
            }
        });
        book.removeSheetAt(3);

    }

    private void writeRecordToJmModeInfo(Workbook book, CmsBtTagModel tags, CmsBtTagJmModuleExtensionModel cmsBtTagJmModuleExtensionModel, List<CmsBtJmPromotionProductExtModel> cmsBtJmPromotionProductModels, String modelName, Integer type) {

        CmsBtTagJmModuleExtensionBean tagExt = new CmsBtTagJmModuleExtensionBean(cmsBtTagJmModuleExtensionModel);

        Sheet sheet = book.createSheet(String.format("页面内容-货架【%s】（专场特卖用）",modelName));
        try {
            ExcelUtils.copySheet((XSSFSheet)sheet,(XSSFSheet)book.getSheetAt(3),(XSSFWorkbook)book,(XSSFWorkbook)book,true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Row styleRow = FileUtils.row(sheet, 2);
        CellStyle unlock = styleRow.getRowStyle();
        int rowIndex = 3;

        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, modelName, unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, tagExt.getHideFlag(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, tagExt.getShelfType(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, tagExt.getImageType(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, tagExt.getCmsBtTagJmModuleExtensionModel().getModuleTitle(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmImageTemplateService.getSeparatorBar(modelName), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, tagExt.getProductsSortBy(), unlock);
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, tagExt.getNoStockToLast(), unlock);
        for(CmsBtJmPromotionProductExtModel cmsBtJmPromotionProductModel:cmsBtJmPromotionProductModels){
            if(type == CmsJmPromotionExportService.HASHID){
                ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionProductModel.getJmHashId(), unlock);
            }else{
                ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionProductModel.getJumeiMallId(), unlock);
            }
        }
    }

    private void writeRecordToFeaturedHashId(Workbook book, List<CmsBtJmPromotionProductExtModel> cmsBtJmPromotionProductModels, Integer type){
        Sheet sheet = book.getSheetAt(0);
        Row styleRow = FileUtils.row(sheet, 2);
        CellStyle unlock = styleRow.getRowStyle();
        int rowIndex = 25;
        for (CmsBtJmPromotionProductExtModel cmsBtJmPromotionProductModel : cmsBtJmPromotionProductModels) {
            if(type == CmsJmPromotionExportService.HASHID) {
                ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionProductModel.getJmHashId(), unlock);
            }else{
                ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionProductModel.getJumeiMallId(), unlock);
            }
        }
    }
}

