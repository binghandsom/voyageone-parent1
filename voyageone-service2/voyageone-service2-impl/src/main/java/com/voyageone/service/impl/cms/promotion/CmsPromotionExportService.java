package com.voyageone.service.impl.cms.promotion;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.bean.cms.promotion.CmsPromotionExportBean;
import com.voyageone.service.dao.cms.CmsBtPromotionExportTaskDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.CmsProperty;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsPromotionExportMQMessageBody;
import com.voyageone.service.model.cms.CmsBtPromotionExportTaskModel;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.CmsBtPromotionSkusModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 活动(非聚美)商品导出Service
 *
 * @Author rex.wu
 * @Create 2017-05-17 15:25
 */
@Service
public class CmsPromotionExportService extends BaseService {

    private static final String[] EXPORT_SKU_HEADERS = {
            "Cart ID", "Channel ID", "Platform Category(\">\"分割)", "NumberIID *",
            "GroupID", "Product Group *", "ProductId", "Product Code *", "Product Name *", "SKU *", "Discount Tag", "MSRP（USD）",
            "MSRP(RMB) *", "Retail Price *", "Sale Price *", "Promotion Price *", "Inventory", "Image Url 1 *", "Image Url 2",
            "Image Url 3", "Time", "p1", "p2", "p3", "p4", "size"};
    private static final String[] EXPORT_CODE_HEADERS = {"Cart ID", "Channel ID", "Platform Category(\">\"分割)",
            "NumberIID *", "GroupID", "Product Group *", "ProductId", "Product Code *", "Product Name *", "Discount Tag",
            "MSRP（USD）", "MSRP(RMB) *", "Retail Price *", "Sale Price *", "Promotion Price *", "Inventory", "Image Url 1 *",
            "Image Url 2", "Image Url 3", "Time", "p1", "p2", "p3", "p4"};
    private final static int PAGE_SIZE = 100;

    @Autowired
    private CmsBtPromotionExportTaskDao cmsBtPromotionExportTaskDao;
    @Autowired
    private PromotionCodeService promotionCodeService;
    @Autowired
    private PromotionSkuService promotionSkuService;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CmsMqSenderService cmsMqSenderService;

    /**
     * 创建活动(非聚美)导出任务
     *
     * @param channelId    渠道ID
     * @param promotionId  活动ID
     * @param templateType 模板类型
     * @param username     用户
     */
    public void createPromotionExportTask(String channelId, Integer promotionId, Integer templateType, String username) {
        CmsBtPromotionExportTaskModel queryModel = new CmsBtPromotionExportTaskModel();
        queryModel.setCmsBtPromotionId(promotionId);
        queryModel.setCreater(username);
        queryModel.setStatus(0);
        // 当前用户是否有等待导入的任务
        if (CollectionUtils.isNotEmpty(cmsBtPromotionExportTaskDao.selectList(queryModel))) {
            throw new BusinessException("尚有导出任务未执行完毕，请稍后。");
        }

        CmsBtPromotionExportTaskModel newTaskModel = new CmsBtPromotionExportTaskModel();
        newTaskModel.setCreater(username);
        newTaskModel.setCreated(new Date());
        newTaskModel.setCmsBtPromotionId(promotionId);
        newTaskModel.setTemplateType(templateType);
        newTaskModel.setStatus(0);
        cmsBtPromotionExportTaskDao.insert(newTaskModel);

        // MQ发送
        CmsPromotionExportMQMessageBody messageBody = new CmsPromotionExportMQMessageBody();
        messageBody.setChannelId(channelId);
        messageBody.setSender(username);
        messageBody.setCmsPromotionExportTaskId(newTaskModel.getCmsBtPromotionId());
        cmsMqSenderService.sendMessage(messageBody);
    }


    /**
     * 导出活动下载文件
     *
     * @param messageBody MQ消息体
     */
    public void export(CmsPromotionExportMQMessageBody messageBody) {
        Integer exportTaskId = messageBody.getCmsPromotionExportTaskId();
        CmsBtPromotionExportTaskModel exportTaskModel = cmsBtPromotionExportTaskDao.select(exportTaskId);
        if (exportTaskModel == null) {
            throw new BusinessException(String.format("cms_bt_promotion_export_task对应ID(%d)不存在", exportTaskId));
        }

        String username = messageBody.getSender();
        String channelId = messageBody.getChannelId(); // 渠道ID
        Integer promotionId = exportTaskModel.getCmsBtPromotionId(); // 活动ID
        Integer templateType = exportTaskModel.getTemplateType(); // 模板类型
        if (StringUtils.isBlank(channelId) || promotionId == null || templateType == null) {
            String errorMsg = String.format("活动(非聚美)商品导出错误，缺少参数，请参考(channelId=%s, promotionId=%d, templdateType=%d)", channelId, promotionId, templateType);
            CmsBtPromotionExportTaskModel targetExportTaskModel = new CmsBtPromotionExportTaskModel();
            targetExportTaskModel.setComment(errorMsg);
            targetExportTaskModel.setModifier("CmsAdvSearchExportMQJob");
            targetExportTaskModel.setModified(new Date());
            cmsBtPromotionExportTaskDao.update(targetExportTaskModel);
            throw new BusinessException(errorMsg);
        }

        String exportPath = "";
        if (Objects.equals(templateType, Integer.valueOf(0))) {
            exportPath = Properties.readValue(CmsProperty.Props.PROMOTION_EXPORT_PATH);
        } else if (Objects.equals(templateType, Integer.valueOf(1))) {
            exportPath = Properties.readValue(CmsProperty.Props.PROMOTION_JUHUASUAN_EXPORT_PATH);
        } else if (Objects.equals(templateType, Integer.valueOf(2))) {
            exportPath = Properties.readValue(CmsProperty.Props.PROMOTION_TMALL_EXPORT_PATH);
        }
        File pathFileObj = new File(exportPath);
        if (StringUtils.isBlank(exportPath) || !(pathFileObj = new File(exportPath)).exists()) {
            $info("活动(非聚美)商品导出文件目录不存在 " + exportPath);
            if (!pathFileObj.mkdirs()) {
                $error("活动(非聚美)商品导出文件目录不存在" + exportPath);
                throw new BusinessException("活动(非聚美)商品导出文件目录不存在" + exportPath);
            }
        }

        CmsBtPromotionModel cmsBtPromotionModel = promotionService.getByPromotionIdOrgChannelId(promotionId, channelId);
        Date beginTime = new Date();
        OutputStream outputStream = null;
        try {
            $info(String.format("开始导出活动(%s)商品", cmsBtPromotionModel.getPromotionName()));

            String filename = String.format("%s(%s).xlsx", cmsBtPromotionModel.getPromotionName(), DateTimeUtil.getLocalTime(0, "yyyyMMddHHmmss"));

            Workbook book = null;
            if (Objects.equals(templateType, Integer.valueOf(0))) {
                book = new SXSSFWorkbook(1000);
                createExportFile(book, cmsBtPromotionModel);
            } else if (Objects.equals(templateType, Integer.valueOf(1))) {
                // 模板是xls，后缀格式必须统一，否则打不开
                filename = filename.replace("xlsx", "xls");
                book = createJuhuasuanExportFile(cmsBtPromotionModel);
            } else if (Objects.equals(templateType, Integer.valueOf(2))) {
                // 模板是xls，后缀格式必须统一，否则打不开
                filename = filename.replace("xlsx", "xls");
                book = createTmallExportFile(cmsBtPromotionModel);
            }

            outputStream = new FileOutputStream(exportPath + filename);
            book.write(outputStream);
            $info(String.format("导出活动(%s)商品文件创建成功", cmsBtPromotionModel.getPromotionName()));

            CmsBtPromotionExportTaskModel targetExportTaskModel = new CmsBtPromotionExportTaskModel();
            targetExportTaskModel.setId(exportTaskModel.getId());
            targetExportTaskModel.setModifier("CmsAdvSearchExportMQJob");
            targetExportTaskModel.setModified(new Date());
            targetExportTaskModel.setStatus(1); // 导出任务成功
            targetExportTaskModel.setFileName(filename);
            targetExportTaskModel.setFilePath(exportPath);
            targetExportTaskModel.setBeginTime(beginTime);
            targetExportTaskModel.setEndTime(new Date());
            cmsBtPromotionExportTaskDao.update(targetExportTaskModel);
        } catch (Exception e) {
            e.printStackTrace();
            $error(String.format("活动(%s)商品导出失败", cmsBtPromotionModel.getPromotionName()));
            // 如果任务出现错误，记录失败
            CmsBtPromotionExportTaskModel targetExportTaskModel = new CmsBtPromotionExportTaskModel();
            targetExportTaskModel.setId(exportTaskModel.getId());
            targetExportTaskModel.setModifier("CmsAdvSearchExportMQJob");
            targetExportTaskModel.setModified(new Date());
            targetExportTaskModel.setStatus(2); // 导出任务失败
            targetExportTaskModel.setComment(e.getMessage());
            cmsBtPromotionExportTaskDao.update(targetExportTaskModel);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    $error(String.format("活动(%s)导出，关闭文件流出错了", cmsBtPromotionModel.getPromotionName()));
                }
            }
        }
    }


    // =========================================按上传模板导出================================================

    /**
     * 按上传模板导出活动商品
     *
     * @param book           工作簿
     * @param promotionModel 活动Model
     */
    private void createExportFile(Workbook book, CmsBtPromotionModel promotionModel) {
        List<CmsBtPromotionCodesBean> promotionCodes =
                promotionCodeService.getPromotionCodeListByIdOrgChannelId2(promotionModel.getId(), promotionModel.getChannelId());
        CellStyle style = book.createCellStyle();
        this.setHeadCellStyle(style, "en");
        // sku sheet header
        Sheet skuSheet = book.createSheet("sku");
        Row skuRow = FileUtils.row(skuSheet, 0); // 标题行
        int size = EXPORT_SKU_HEADERS.length;
        for (int i = 0; i < size; i++) {
            FileUtils.cell(skuRow, i, style).setCellValue(EXPORT_SKU_HEADERS[i]);
        }
        // code sheet header
        Sheet codeSheet = book.createSheet("code");
        Row codeRow = FileUtils.row(codeSheet, 0); // 标题行
        size = EXPORT_CODE_HEADERS.length;
        for (int i = 0; i < size; i++) {
            FileUtils.cell(codeRow, i, style).setCellValue(EXPORT_CODE_HEADERS[i]);
        }

        // 分页写入Sheet，对活动codes分页
        int codeCount = promotionCodes.size();
        int page = codeCount / PAGE_SIZE + (codeCount % PAGE_SIZE == 0 ? 0 : 1);
        int codeStartIndex = 0;
        int skuStartIndex = 1;
        for (int i = 1; i <= page; i++) {
            codeStartIndex = (i - 1) * PAGE_SIZE;
            skuStartIndex += this.writeRecordToWorkbook(book, promotionCodes, codeStartIndex, skuStartIndex);
        }
    }

    /**
     * 设置excel中英文title样式
     */
    private void setHeadCellStyle(CellStyle style, String lang) {
        if (style != null && StringUtils.isNotBlank(lang)) {
            if ("en".equalsIgnoreCase(lang)) {
                style.setBorderBottom(CellStyle.BORDER_THIN);
                style.setBorderTop(CellStyle.BORDER_THIN);
                style.setBorderLeft(CellStyle.BORDER_THIN);
                style.setBorderRight(CellStyle.BORDER_THIN);
                style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            } else if ("cn".equalsIgnoreCase(lang)) {
                style.setBorderBottom(CellStyle.BORDER_THIN);
                style.setBorderTop(CellStyle.BORDER_THIN);
                style.setBorderLeft(CellStyle.BORDER_THIN);
                style.setBorderRight(CellStyle.BORDER_THIN);
                style.setFillForegroundColor(IndexedColors.TURQUOISE.getIndex());
                style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            }
        }
    }

    /**
     * 从指定序号的活动code写一页数据分表到sku和code两个sheet中
     *
     * @param book           工作簿
     * @param promotionCodes 活动的所有code
     * @param codeStartIndex code本次code起始序号
     * @return int skuSheet本次实际操作的行数(因为code和sku一对多)，code sheet忽略此项
     */
    private int writeRecordToWorkbook(Workbook book, List<CmsBtPromotionCodesBean> promotionCodes, int codeStartIndex, int skuStartIndex) {

        // 写Sku Sheet
        Sheet skuSheet = book.getSheet("sku");
        // 写Code Sheet
        Sheet codeSheet = book.getSheet("code");

        CellStyle unlock = book.createCellStyle();
        this.setHeadCellStyle(unlock, "cn");

        int codeCount = promotionCodes.size();
        System.out.println("CODE个数+" + codeCount);
        int skuCount = 0;
        for (int i = 0; i < PAGE_SIZE; i++) {
            int index = codeStartIndex + i;
            if (index >= codeCount) {
                break;
            }
            CmsBtPromotionCodesBean promotionCodesBean = promotionCodes.get(index);
            index += 1; // 第一行是标题栏

            // 写入SKU sheet
            Map<String, Object> param = new HashMap<>();
            param.put("promotionId", promotionCodesBean.getPromotionId());
            param.put("productCode", promotionCodesBean.getProductCode());
            List<CmsBtPromotionSkusModel> cmsBtPromotionSkusList = promotionSkuService.getListByWhere(param);
            if (CollectionUtils.isNotEmpty(cmsBtPromotionSkusList)) {
                CmsBtPromotionSkusModel firstSku = cmsBtPromotionSkusList.get(0);
                promotionCodesBean.setSalePrice(cmsBtPromotionSkusList.get(0).getSalePrice().doubleValue());
                promotionCodesBean.setMsrpUS(cmsBtPromotionSkusList.get(0).getMsrpUsd().doubleValue());
                promotionCodesBean.setMsrp(cmsBtPromotionSkusList.get(0).getMsrpRmb().doubleValue());
                promotionCodesBean.setRetailPrice(cmsBtPromotionSkusList.get(0).getRetailPrice().doubleValue());
                promotionCodesBean.setSalePrice(cmsBtPromotionSkusList.get(0).getSalePrice().doubleValue());
                promotionCodesBean.setPromotionPrice(cmsBtPromotionSkusList.get(0).getPromotionPrice().doubleValue());
                for (CmsBtPromotionSkusModel sku : cmsBtPromotionSkusList) {
                    Row row = FileUtils.row(skuSheet, skuStartIndex);
                    FileUtils.cell(row, CmsConstants.CellNum.cartIdCellNum, unlock).setCellValue(promotionCodesBean.getCartId() == null ? "" : String.valueOf(promotionCodesBean.getCartId()));

                    FileUtils.cell(row, CmsConstants.CellNum.channelIdCellNum, unlock).setCellValue(StringUtils.trimToEmpty(promotionCodesBean.getOrgChannelId()));

                    FileUtils.cell(row, CmsConstants.CellNum.catPathCellNum, unlock).setCellValue(StringUtils.trimToEmpty(promotionCodesBean.getCatPath()));

                    FileUtils.cell(row, CmsConstants.CellNum.numberIdCellNum, unlock).setCellValue(StringUtils.trimToEmpty(promotionCodesBean.getNumIid()));

                    FileUtils.cell(row, CmsConstants.CellNum.groupIdCellNum, unlock).setCellValue(promotionCodesBean.getModelId() == null ? "" : String.valueOf(promotionCodesBean.getModelId()));

                    FileUtils.cell(row, CmsConstants.CellNum.groupNameCellNum, unlock).setCellValue(StringUtils.trimToEmpty(promotionCodesBean.getProductModel()));

                    FileUtils.cell(row, CmsConstants.CellNum.productIdCellNum, unlock).setCellValue(promotionCodesBean.getProductId() == null ? "" : String.valueOf(promotionCodesBean.getProductId()));

                    FileUtils.cell(row, CmsConstants.CellNum.productCodeCellNum, unlock).setCellValue(StringUtils.trimToEmpty(promotionCodesBean.getProductCode()));

                    FileUtils.cell(row, CmsConstants.CellNum.productNameCellNum, unlock).setCellValue(StringUtils.trimToEmpty(promotionCodesBean.getProductName()));

                    FileUtils.cell(row, CmsConstants.CellNum.skuCellNum, unlock).setCellValue(StringUtils.trimToEmpty(sku.getProductSku()));

                    FileUtils.cell(row, CmsConstants.CellNum.tagCellNum, unlock).setCellValue(StringUtils.trimToEmpty(promotionCodesBean.getTag()));

                    if (sku.getMsrpUsd() != null) {
                        FileUtils.cell(row, CmsConstants.CellNum.msrpUSCellNum, unlock).setCellValue(sku.getMsrpUsd().doubleValue());
                    }
                    if (sku.getMsrpRmb() != null) {
                        FileUtils.cell(row, CmsConstants.CellNum.msrpRMBCellNum, unlock).setCellValue(sku.getMsrpRmb().doubleValue());
                    }
                    if (sku.getRetailPrice() != null) {
                        FileUtils.cell(row, CmsConstants.CellNum.retailPriceCellNum, unlock).setCellValue(sku.getRetailPrice().doubleValue());
                    }
                    if (sku.getSalePrice() != null) {
                        FileUtils.cell(row, CmsConstants.CellNum.salePriceCellNum, unlock).setCellValue(sku.getSalePrice().doubleValue());
                    }
                    if (sku.getPromotionPrice() != null) {
                        FileUtils.cell(row, CmsConstants.CellNum.promotionPriceCellNum, unlock).setCellValue(sku.getPromotionPrice().doubleValue());
                    }

                    FileUtils.cell(row, CmsConstants.CellNum.inventoryCellNum, unlock).setCellValue(promotionCodesBean.getInventory() == null ? "0" : String.valueOf(promotionCodesBean.getInventory()));


                    FileUtils.cell(row, CmsConstants.CellNum.image1CellNum, unlock).setCellValue(StringUtils.trimToEmpty(promotionCodesBean.getImage_url_1()));

                    FileUtils.cell(row, CmsConstants.CellNum.image2CellNum, unlock).setCellValue(StringUtils.trimToEmpty(promotionCodesBean.getImage_url_2()));

                    FileUtils.cell(row, CmsConstants.CellNum.image3CellNum, unlock).setCellValue(StringUtils.trimToEmpty(promotionCodesBean.getImage_url_3()));

                    FileUtils.cell(row, CmsConstants.CellNum.timeCellNum, unlock).setCellValue(promotionCodesBean.getTime());

                    FileUtils.cell(row, CmsConstants.CellNum.property1CellNum, unlock).setCellValue(StringUtils.trimToEmpty(promotionCodesBean.getProperty1()));

                    FileUtils.cell(row, CmsConstants.CellNum.property2CellNum, unlock).setCellValue(StringUtils.trimToEmpty(promotionCodesBean.getProperty2()));

                    FileUtils.cell(row, CmsConstants.CellNum.property3CellNum, unlock).setCellValue(StringUtils.trimToEmpty(promotionCodesBean.getProperty3()));

                    FileUtils.cell(row, CmsConstants.CellNum.property4CellNum, unlock).setCellValue(StringUtils.trimToEmpty(promotionCodesBean.getProperty4()));

                    FileUtils.cell(row, CmsConstants.CellNum.size, unlock).setCellValue(StringUtils.trimToEmpty(sku.getSize()));

                    skuStartIndex++;
                    skuCount++;
                }
            }

            // 写入Code sheet
            Row row = FileUtils.row(codeSheet, index);


            FileUtils.cell(row, CmsConstants.CellNum.cartIdCellNum, unlock).setCellValue(promotionCodesBean.getCartId() == null ? "" : String.valueOf(promotionCodesBean.getCartId()));

            FileUtils.cell(row, CmsConstants.CellNum.channelIdCellNum, unlock).setCellValue(StringUtils.trimToEmpty(promotionCodesBean.getOrgChannelId()));

            FileUtils.cell(row, CmsConstants.CellNum.catPathCellNum, unlock).setCellValue(StringUtils.trimToEmpty(promotionCodesBean.getCatPath()));

            FileUtils.cell(row, CmsConstants.CellNum.numberIdCellNum, unlock).setCellValue(StringUtils.trimToEmpty(promotionCodesBean.getNumIid()));

            FileUtils.cell(row, CmsConstants.CellNum.groupIdCellNum, unlock).setCellValue(promotionCodesBean.getModelId());

            FileUtils.cell(row, CmsConstants.CellNum.groupNameCellNum, unlock).setCellValue(promotionCodesBean.getProductModel());

            FileUtils.cell(row, CmsConstants.CellNum.productIdCellNum, unlock).setCellValue(promotionCodesBean.getProductId());

            FileUtils.cell(row, CmsConstants.CellNum.productCodeCellNum, unlock).setCellValue(promotionCodesBean.getProductCode());

            FileUtils.cell(row, CmsConstants.CellNum.productNameCellNum, unlock).setCellValue(promotionCodesBean.getProductName());

            FileUtils.cell(row, CmsConstants.CellNum.tagCellNum - 1, unlock).setCellValue(promotionCodesBean.getTag());

            if (promotionCodesBean.getMsrpUS() != null) {
                FileUtils.cell(row, CmsConstants.CellNum.msrpUSCellNum - 1, unlock).setCellValue(promotionCodesBean.getMsrpUS());
            }
            if (promotionCodesBean.getMsrp() != null) {
                FileUtils.cell(row, CmsConstants.CellNum.msrpRMBCellNum - 1, unlock).setCellValue(promotionCodesBean.getMsrp());
            }
            if (promotionCodesBean.getRetailPrice() != null) {
                FileUtils.cell(row, CmsConstants.CellNum.retailPriceCellNum - 1, unlock).setCellValue(promotionCodesBean.getRetailPrice());
            }
            if (promotionCodesBean.getSalePrice() != null) {
                FileUtils.cell(row, CmsConstants.CellNum.salePriceCellNum - 1, unlock).setCellValue(promotionCodesBean.getSalePrice());
            }
            if (promotionCodesBean.getPromotionPrice() != null) {
                FileUtils.cell(row, CmsConstants.CellNum.promotionPriceCellNum - 1, unlock).setCellValue(promotionCodesBean.getPromotionPrice());
            }
            /*if (promotionCodesBean.getInventory() != null) {
                FileUtils.cell(row, CmsConstants.CellNum.inventoryCellNum - 1, unlock).setCellValue(promotionCodesBean.getInventory());
            }*/
            FileUtils.cell(row, CmsConstants.CellNum.inventoryCellNum, unlock).setCellValue(promotionCodesBean.getInventory() == null ? "0" : String.valueOf(promotionCodesBean.getInventory()));

            FileUtils.cell(row, CmsConstants.CellNum.image1CellNum - 1, unlock).setCellValue(promotionCodesBean.getImage_url_1());

            FileUtils.cell(row, CmsConstants.CellNum.image2CellNum - 1, unlock).setCellValue(promotionCodesBean.getImage_url_2());

            FileUtils.cell(row, CmsConstants.CellNum.image3CellNum - 1, unlock).setCellValue(promotionCodesBean.getImage_url_3());

            FileUtils.cell(row, CmsConstants.CellNum.timeCellNum - 1, unlock).setCellValue(promotionCodesBean.getTime());

            FileUtils.cell(row, CmsConstants.CellNum.property1CellNum - 1, unlock).setCellValue(promotionCodesBean.getProperty1());

            FileUtils.cell(row, CmsConstants.CellNum.property2CellNum - 1, unlock).setCellValue(promotionCodesBean.getProperty2());

            FileUtils.cell(row, CmsConstants.CellNum.property3CellNum - 1, unlock).setCellValue(promotionCodesBean.getProperty3());

            FileUtils.cell(row, CmsConstants.CellNum.property4CellNum - 1, unlock).setCellValue(promotionCodesBean.getProperty4());

        }

        return skuCount;

    }
    // =========================================按上传模板导出================================================


    // =========================================按聚划算模板导出================================================

    /**
     * 按聚划算模板导出活动
     *
     * @param promotionModel 活动Model
     */
    private Workbook createJuhuasuanExportFile(CmsBtPromotionModel promotionModel) throws Exception {
        String templatePath = Properties.readValue(CmsProperty.Props.CMS_PROMOTION_EXPORT_JUHUASUAN);

        InputStream inputStream = new FileInputStream(templatePath);
        Workbook book = WorkbookFactory.create(inputStream);

        Map<String, List<CmsBtPromotionCodesBean>> groups = getPromotionInfo(promotionModel.getPromotionId(), promotionModel.getChannelId());
        // 转成导出的格式
        List<CmsPromotionExportBean> cmsPromotionExportBeans = new ArrayList<>();
        groups.forEach((numiid, cmsBtPromotionCodesBeans) -> {
            cmsPromotionExportBeans.add(calculatePriceQty(cmsBtPromotionCodesBeans));
        });

        Sheet sheet = book.getSheetAt(0);
        Row styleRow = FileUtils.row(sheet, 2);
        CellStyle unlock = styleRow.getRowStyle();
        int rowIndex = 2;
        for (CmsPromotionExportBean item : cmsPromotionExportBeans) {
            Row row = FileUtils.row(sheet, rowIndex);

            FileUtils.cell(row, 0, unlock).setCellValue(item.getNumIid());
            FileUtils.cell(row, 1, unlock).setCellValue(item.getPromotionPrice());
            FileUtils.cell(row, 2, unlock).setCellValue(item.getQty());
            rowIndex++;
        }
        return book;
    }


    /**
     * 查询活动商品code信息，并按numiid进行分组
     *
     * @param promotionId  活动ID
     * @param selChannelId 渠道ID
     * @return 活动商品Code分组
     */
    private Map<String, List<CmsBtPromotionCodesBean>> getPromotionInfo(Integer promotionId, String selChannelId) {
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", selChannelId);
        param.put("promotionId", promotionId);
        Map<String, List<CmsBtPromotionCodesBean>> groups = new HashMap<>();
        List<CmsBtPromotionCodesBean> codes = promotionCodeService.getPromotionCodeList(param);
        // 把code按numiid进行分组
        for (CmsBtPromotionCodesBean codesBean : codes) {
            if (StringUtils.isBlank(codesBean.getNumIid()) || "0".equalsIgnoreCase(codesBean.getNumIid())) {
                continue;
            }
            if (groups.containsKey(codesBean.getNumIid())) {
                groups.get(codesBean.getNumIid()).add(codesBean);
            } else {
                List<CmsBtPromotionCodesBean> temp = new ArrayList<>();
                temp.add(codesBean);
                groups.put(codesBean.getNumIid(), temp);
            }
        }
        return groups;
    }


    private CmsPromotionExportBean calculatePriceQty(List<CmsBtPromotionCodesBean> codes) {
        Integer qty = 0;
        Integer promotionPrice = 0;
        Integer price = null;
        for (CmsBtPromotionCodesBean code : codes) {
            CmsBtProductModel cmsBtProductModel = productService.getProductByCode(code.getOrgChannelId(), code.getProductCode());
            if (cmsBtProductModel != null) {
                if (cmsBtProductModel.getCommon().getFields().getQuantity() != null) {
                    qty += cmsBtProductModel.getCommon().getFields().getQuantity();
                }
                price = cmsBtProductModel.getPlatform(23).getpPriceRetailEd().intValue();
            }
            if (code.getPromotionPrice() != null && promotionPrice < code.getPromotionPrice().intValue()) {
                promotionPrice = code.getPromotionPrice().intValue();
            }
        }
        CmsPromotionExportBean cmsPromotionExportBean = new CmsPromotionExportBean();
        cmsPromotionExportBean.setQty(qty);
        cmsPromotionExportBean.setPromotionPrice(promotionPrice);
        cmsPromotionExportBean.setMsrpPrice(price);
        cmsPromotionExportBean.setNumIid(codes.get(0).getNumIid());
        return cmsPromotionExportBean;
    }
    // =========================================按聚划算模板导出================================================

    // ======================================按官方活动(A类)模板导出=============================================

    /**
     * 按官方活动(A类模板)导出活动
     *
     * @param promotionModel 活动Model
     */
    private Workbook createTmallExportFile(CmsBtPromotionModel promotionModel) throws Exception {
        String templatePath = Properties.readValue(CmsProperty.Props.CMS_PROMOTION_EXPORT_TMALL);
        InputStream inputStream = new FileInputStream(templatePath);
        Workbook book = WorkbookFactory.create(inputStream);

        Map<String, List<CmsBtPromotionCodesBean>> groups = getPromotionInfo(promotionModel.getPromotionId(), promotionModel.getChannelId());
        Sheet sheet = book.getSheetAt(1);
        Row styleRow = FileUtils.row(sheet, 1);
        CellStyle unlock = styleRow.getRowStyle();
        int rowIndex = 1;
        for (String key : groups.keySet()) {
            Row row = FileUtils.row(sheet, rowIndex);

            FileUtils.cell(row, 0, unlock).setCellValue(key);
            FileUtils.cell(row, 1, unlock).setCellValue(groups.get(key).get(0).getMsrp());
            FileUtils.cell(row, 2, unlock).setCellValue(groups.get(key).get(0).getPromotionPrice());
            rowIndex++;
        }
        return book;
    }
    // =========================================按官方活动(A类)模板导出================================================
}
