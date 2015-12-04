package com.voyageone.wms.service.impl.reportImpl;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.StringUtils;
import com.voyageone.wms.WmsConstants;
import com.voyageone.wms.WmsMsgConstants;
import com.voyageone.wms.formbean.FormReportBean;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 *
 * Created by sky on 20150807.
 */
@Service
public class WmsInvDetReportService extends WmsReportBaseService {

    public byte[] createReportByte(List<FormReportBean> formReportBeans, FormReportBean formReportBean) {
        byte[] bytes;
        try{
            // 报表模板名取得
            String templateFile = Properties.readValue(WmsConstants.ReportItems.InvDelRpt.TEMPLATE_PATH) + WmsConstants.ReportItems.InvDelRpt.TEMPLATE_NAME;
            // 报表模板名读入
            InputStream templateInput = new FileInputStream(templateFile);
            Workbook workbook = WorkbookFactory.create(templateInput);
            // 设置内容（Champon时需要显示颜色和尺码）
            if (formReportBean.getOrder_channel_id().equals(ChannelConfigEnums.Channel.CHAMPION.getId())) {
                setInvDelReptContent2(workbook, formReportBeans, formReportBean);
            }
            else
            {
                setInvDelReptContent1(workbook, formReportBeans, formReportBean);
            }
            // 输出内容
            ByteArrayOutputStream outData = new ByteArrayOutputStream();
            workbook.write(outData);
            bytes = outData.toByteArray();
            // 关闭
            templateInput.close();
            workbook.close();
            outData.close();
        } catch (Exception e) {
            logger.info("库存详情报表下载失败：" + e);
            throw new BusinessException(WmsMsgConstants.ReportMsg.INVDELRPT_DOWNLOAD_FAILED);
        }
        return bytes;
    }

    /**
     * 设置库存详情报表内容
     * @param workbook 报表模板
     * @param formReportBeanList 库存内容按sku
     * @param formReportBeanParam 查询报表参数Bean
     */
    private  void setInvDelReptContent1(Workbook workbook, List<FormReportBean> formReportBeanList, FormReportBean formReportBeanParam) {

        // 模板Sheet
        int sheetNo = WmsConstants.ReportItems.InvDelRpt.RptType1.TEMPLATE_SHEET_NO;

        workbook.removeSheetAt(WmsConstants.ReportItems.InvDelRpt.RptType2.TEMPLATE_SHEET_NO);

        // 初始行
        int intRow = WmsConstants.ReportItems.InvDelRpt.RptType1.TEMPLATE_FIRSTROW_NO;

        // 按照模板克隆一个sheet
        Sheet sheet = workbook.cloneSheet(sheetNo);

        // 设置模板sheet页后的sheet名为报告sheet名
        workbook.setSheetName(sheetNo + 1, WmsConstants.ReportItems.InvDelRpt.RPT_SHEET_NAME);

        // 修改报表标题（报告开始日期报告，报告结束日期设置入列名）
        modifyColTile1(sheet, formReportBeanParam);

        for (FormReportBean formReportBean : formReportBeanList) {
            if(intRow != WmsConstants.ReportItems.InvDelRpt.RptType1.TEMPLATE_FIRSTROW_NO) {
                Row newRow = sheet.createRow(intRow);
                //根据第2行（第一行是标题）格式设置每行的格式,第一列不处理
                for (int col = WmsConstants.ReportItems.InvDelRpt.RptType1.START; col <= WmsConstants.ReportItems.InvDelRpt.RptType1.COLNUM; col++) {
                    Cell newCell = newRow.createCell(col);
                    Cell oldCell = sheet.getRow(WmsConstants.ReportItems.InvDelRpt.RptType1.TEMPLATE_FIRSTROW_NO).getCell(col);
                    newCell.setCellStyle(oldCell.getCellStyle());
                }
            }
            // 得到当前行
            Row currentRow = sheet.getRow(intRow);
            // sku
            currentRow.getCell(WmsConstants.ReportItems.InvDelRpt.RptType1.Col.COLNUM_SKU).setCellValue(formReportBean.getSku());
            // initInv
            currentRow.getCell(WmsConstants.ReportItems.InvDelRpt.RptType1.Col.COLNUM_INITINV).setCellValue(StringUtils.null2Space(formReportBean.getInitInv()).equals("0")? "" : StringUtils.null2Space(formReportBean.getInitInv()));
            // po
            currentRow.getCell(WmsConstants.ReportItems.InvDelRpt.RptType1.Col.COLNUM_PO).setCellValue(StringUtils.null2Space(formReportBean.getPo()).equals("0")? "" : StringUtils.null2Space(formReportBean.getPo()));
            // sell
            currentRow.getCell(WmsConstants.ReportItems.InvDelRpt.RptType1.Col.COLNUM_SELL).setCellValue(StringUtils.null2Space(formReportBean.getSell()).equals("0")? "" : StringUtils.null2Space(formReportBean.getSell()));
            // return
            currentRow.getCell(WmsConstants.ReportItems.InvDelRpt.RptType1.Col.COLNUM_RETURN).setCellValue(StringUtils.null2Space(formReportBean.getReturns()).equals("0")? "" : StringUtils.null2Space(formReportBean.getReturns()));
            // wit
            currentRow.getCell(WmsConstants.ReportItems.InvDelRpt.RptType1.Col.COLNUM_WIT).setCellValue(StringUtils.null2Space(formReportBean.getWit()).equals("0")? "" : StringUtils.null2Space(formReportBean.getWit()));
            // currInv
            currentRow.getCell(WmsConstants.ReportItems.InvDelRpt.RptType1.Col.COLNUM_CURRINV).setCellValue(StringUtils.null2Space(formReportBean.getCurrInv()).equals("0")? "" : StringUtils.null2Space(formReportBean.getCurrInv()));
            intRow = intRow + 1;
        }
        // 如果有记录的话，删除模板sheet
        if (formReportBeanList.size() > 0) {
            workbook.removeSheetAt(sheetNo);
        }
    }

    private void modifyColTile1(Sheet sheet, FormReportBean formReportBeanParam) {
        //标题行
        Row firstRow = sheet.getRow(WmsConstants.ReportItems.InvDelRpt.RptType1.TEMPLATE_FIRSTROW_NO - 1);
        firstRow.getCell(WmsConstants.ReportItems.InvDelRpt.RptType1.Col.COLNUM_INITINV).setCellValue(formReportBeanParam.getFromDate() + "的库存");
        firstRow.getCell(WmsConstants.ReportItems.InvDelRpt.RptType1.Col.COLNUM_CURRINV).setCellValue(formReportBeanParam.getToDate() + "的库存");
    }

    /**
     * 设置库存详情报表内容
     * @param workbook 报表模板
     * @param formReportBeanList 库存内容按sku
     * @param formReportBeanParam 查询报表参数Bean
     */
    private  void setInvDelReptContent2(Workbook workbook, List<FormReportBean> formReportBeanList, FormReportBean formReportBeanParam) {
        // 模板Sheet
        int sheetNo = WmsConstants.ReportItems.InvDelRpt.RptType2.TEMPLATE_SHEET_NO;
        // 初始行
        int intRow = WmsConstants.ReportItems.InvDelRpt.RptType2.TEMPLATE_FIRSTROW_NO;

        // 按照模板克隆一个sheet
        Sheet sheet = workbook.cloneSheet(sheetNo);

        // 设置模板sheet页后的sheet名为报告sheet名
        workbook.setSheetName(sheetNo + 1, WmsConstants.ReportItems.InvDelRpt.RPT_SHEET_NAME);

        // 修改报表标题（报告开始日期报告，报告结束日期设置入列名）
        modifyColTile2(sheet, formReportBeanParam);

        for (FormReportBean formReportBean : formReportBeanList) {
            if(intRow != WmsConstants.ReportItems.InvDelRpt.RptType2.TEMPLATE_FIRSTROW_NO) {
                Row newRow = sheet.createRow(intRow);
                //根据第2行（第一行是标题）格式设置每行的格式,第一列不处理
                for (int col =  WmsConstants.ReportItems.InvDelRpt.RptType2.START; col <= WmsConstants.ReportItems.InvDelRpt.RptType2.COLNUM; col++) {
                    Cell newCell = newRow.createCell(col);
                    Cell oldCell = sheet.getRow(WmsConstants.ReportItems.InvDelRpt.RptType2.TEMPLATE_FIRSTROW_NO).getCell(col);
                    newCell.setCellStyle(oldCell.getCellStyle());
                }
            }


            String[] product = formReportBean.getSku().split("-");

            String color = product[product.length -2];
            String size = product[product.length -1];
            String model =  formReportBean.getSku().replace("-" + color + "-" + size,"");

            // 得到当前行
            Row currentRow = sheet.getRow(intRow);
            // model
            currentRow.getCell(WmsConstants.ReportItems.InvDelRpt.RptType2.Col.COLNUM_MODEL).setCellValue(model);
            // color
            currentRow.getCell(WmsConstants.ReportItems.InvDelRpt.RptType2.Col.COLNUM_COLOR).setCellValue(color);
            // size
            currentRow.getCell(WmsConstants.ReportItems.InvDelRpt.RptType2.Col.COLNUM_SIZE).setCellValue(size);
            // initInv
            currentRow.getCell(WmsConstants.ReportItems.InvDelRpt.RptType2.Col.COLNUM_INITINV).setCellValue(StringUtils.null2Space(formReportBean.getInitInv()).equals("0")? "" : StringUtils.null2Space(formReportBean.getInitInv()));
            // po
            currentRow.getCell(WmsConstants.ReportItems.InvDelRpt.RptType2.Col.COLNUM_PO).setCellValue(StringUtils.null2Space(formReportBean.getPo()).equals("0")? "" : StringUtils.null2Space(formReportBean.getPo()));
            // sell
            currentRow.getCell(WmsConstants.ReportItems.InvDelRpt.RptType2.Col.COLNUM_SELL).setCellValue(StringUtils.null2Space(formReportBean.getSell()).equals("0")? "" : StringUtils.null2Space(formReportBean.getSell()));
            // return
            currentRow.getCell(WmsConstants.ReportItems.InvDelRpt.RptType2.Col.COLNUM_RETURN).setCellValue(StringUtils.null2Space(formReportBean.getReturns()).equals("0")? "" : StringUtils.null2Space(formReportBean.getReturns()));
            // wit
            currentRow.getCell(WmsConstants.ReportItems.InvDelRpt.RptType2.Col.COLNUM_WIT).setCellValue(StringUtils.null2Space(formReportBean.getWit()).equals("0")? "" : StringUtils.null2Space(formReportBean.getWit()));
            // currInv
            currentRow.getCell(WmsConstants.ReportItems.InvDelRpt.RptType2.Col.COLNUM_CURRINV).setCellValue(StringUtils.null2Space(formReportBean.getCurrInv()).equals("0")? "" : StringUtils.null2Space(formReportBean.getCurrInv()));
            intRow = intRow + 1;
        }
        // 如果有记录的话，删除模板sheet
        if (formReportBeanList.size() > 0) {
            workbook.removeSheetAt(sheetNo);
        }

        workbook.removeSheetAt(WmsConstants.ReportItems.InvDelRpt.RptType1.TEMPLATE_SHEET_NO);
    }

    private void modifyColTile2(Sheet sheet, FormReportBean formReportBeanParam) {
        //标题行
        Row firstRow = sheet.getRow(WmsConstants.ReportItems.InvDelRpt.RptType2.TEMPLATE_FIRSTROW_NO - 1);
        firstRow.getCell(WmsConstants.ReportItems.InvDelRpt.RptType2.Col.COLNUM_INITINV).setCellValue(formReportBeanParam.getFromDate() + "的库存");
        firstRow.getCell(WmsConstants.ReportItems.InvDelRpt.RptType2.Col.COLNUM_CURRINV).setCellValue(formReportBeanParam.getToDate() + "的库存");
    }

}
