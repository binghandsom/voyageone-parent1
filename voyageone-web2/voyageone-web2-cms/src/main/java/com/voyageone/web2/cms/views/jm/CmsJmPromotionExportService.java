package com.voyageone.web2.cms.views.jm;


import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.*;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 聚美活动新增商品
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsJmPromotionExportService extends BaseViewService {

    public static final Integer HASHID = 1;

    public static final Integer MALLID = 2;
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
            writeRecordToJmPromotionInfo(book, cmsBtJmPromotionSaveBean, type);
            writeRecordToJmModeInfoMain(book, cmsBtJmPromotionSaveBean.getTagList(), type);
            writeRecordToJBayWindow(book, jmPromotionId);
            $info("文档写入完成");
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                book.write(outputStream);

                $info("已写入输出流");

                byte[] bytes = outputStream.toByteArray();
                book.close();

                return bytes;
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
        if (cmsBtJmBayWindowModel != null && !ListUtils.isNull(cmsBtJmBayWindowModel.getBayWindows())) {
            Sheet sheet = book.getSheetAt(2);
            XSSFCellStyle unlock = (XSSFCellStyle) book.createCellStyle();
            unlock.setBorderBottom(CellStyle.BORDER_THIN);
            unlock.setBorderTop(CellStyle.BORDER_THIN);
            unlock.setBorderLeft(CellStyle.BORDER_THIN);
            unlock.setBorderRight(CellStyle.BORDER_THIN);
            int rowIndex = 2;
            cmsBtJmBayWindowModel.getBayWindows().sort((o1, o2) -> o1.getOrder() > o2.getOrder() ? 1 : -1);
            int i = 1;
            for (CmsBtJmBayWindowModel.BayWindow bayWindow : cmsBtJmBayWindowModel.getBayWindows()) {
                ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex), 0, i++, unlock);
                if (cmsBtJmBayWindowModel.getFixed()) {
                    ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex), 1, "定位飘窗", unlock);
                } else {
                    ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex), 1, "链接飘窗", unlock);
                }
                ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex), 2, bayWindow.getName(), unlock);
                ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex), 3, bayWindow.getUrl(), unlock);
                ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 4, bayWindow.getLink(), unlock);
            }

            String[] a = {"a", "b"};
            String.format("aa%s", a);

        }
    }

    private void writeRecordToJmPromotionInfo(Workbook book, CmsBtJmPromotionSaveBean cmsBtJmPromotionSaveBean, Integer type) throws Exception {
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
        ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, cmsBtJmPromotionExportContext.getSessionType(), unlock);
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

        if (type == CmsJmPromotionExportService.HASHID) {
            ExcelUtils.setCellValue(FileUtils.row(sheet, 25), 1, "商品（HashID）", null);
        } else {
            ExcelUtils.setCellValue(FileUtils.row(sheet, 25), 1, "商品（MallID）", null);
        }


    }

    private void writeRecordToJmModeInfoMain(Workbook book, List<CmsBtJmPromotionSaveBean.Tag> tags, Integer type) {

        tags.forEach(tag -> {
            CmsBtTagModel cmsBtTagModel = tag.getModel();
            CmsBtTagJmModuleExtensionModel cmsBtTagJmModuleExtensionModel = cmsBtTagJmModuleExtensionDao.select(cmsBtTagModel.getId());
            if (cmsBtTagJmModuleExtensionModel != null) {
                List<CmsBtJmPromotionProductExtModel> cmsBtJmPromotionProductModels = cmsBtJmPromotionProductDaoExt.selectProductInfoByTagId(cmsBtTagModel.getId());
                List<String> ids = null;
                if (cmsBtJmPromotionProductModels != null) {
                    if (type == CmsJmPromotionExportService.HASHID) {
                        ids = cmsBtJmPromotionProductModels.stream().filter(item -> !StringUtil.isEmpty(item.getJmHashId()) && item.getQuantity() > 0).map(item -> item.getJmHashId()).collect(Collectors.toList());
                    } else {
                        ids = cmsBtJmPromotionProductModels.stream().filter(item -> !StringUtil.isEmpty(item.getJumeiMallId()) && item.getQuantity() > 0).map(item -> item.getJumeiMallId()).collect(Collectors.toList());
                    }
                }

                if (ids == null) ids = new ArrayList<String>();
                List<List<String>> idList = CommonUtil.splitList(ids, 100);
                for (int i = 0; i < idList.size(); i++) {
                    if (cmsBtTagJmModuleExtensionModel.getFeatured()) {
                        writeRecordToFeaturedHashId(book, idList.get(i));
                    } else {
                        if (i == 0) {
                            writeRecordToJmModeInfo(book, cmsBtTagModel, cmsBtTagJmModuleExtensionModel, idList.get(i), cmsBtTagModel.getTagName(), type);
                        } else {
                            writeRecordToJmModeInfo(book, cmsBtTagModel, cmsBtTagJmModuleExtensionModel, idList.get(i), cmsBtTagModel.getTagName() + (i + 1), type);
                        }
                    }
                }
            }
        });
        book.removeSheetAt(3);

    }

    private void writeRecordToJmModeInfo(Workbook book, CmsBtTagModel tags, CmsBtTagJmModuleExtensionModel cmsBtTagJmModuleExtensionModel, List<String> ids, String modelName, Integer type) {

        CmsBtTagJmModuleExtensionBean tagExt = new CmsBtTagJmModuleExtensionBean(cmsBtTagJmModuleExtensionModel);

        Sheet sheet = book.createSheet(String.format("页面内容-货架【%s】（专场特卖用）", modelName));
        try {
            ExcelUtils.copySheet((XSSFSheet) sheet, (XSSFSheet) book.getSheetAt(3), (XSSFWorkbook) book, (XSSFWorkbook) book, true);
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
        if (type == CmsJmPromotionExportService.HASHID) {
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex), 1, "商品（HashID）", null);
        } else {
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex), 1, "商品（MallID）", null);
        }
        for (String id : ids) {
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, id, unlock);
        }
    }

    private void writeRecordToFeaturedHashId(Workbook book, List<String> ids) {
        Sheet sheet = book.getSheetAt(0);
        Row styleRow = FileUtils.row(sheet, 2);
        CellStyle unlock = styleRow.getRowStyle();
        int rowIndex = 25;
        for (String id : ids) {
            ExcelUtils.setCellValue(FileUtils.row(sheet, rowIndex++), 2, id, unlock);
        }
    }
}

