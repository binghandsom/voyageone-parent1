package com.voyageone.wms.service.impl.reportImpl;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.StringUtils;
import com.voyageone.wms.WmsConstants;
import com.voyageone.wms.WmsMsgConstants;
import com.voyageone.wms.formbean.FormReturn;
import com.voyageone.wms.formbean.FormReturnDownloadBean;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Service
public class WmsGoodsReturnReportService extends WmsReportBaseService {

    public byte[] createReportByte(List<FormReturnDownloadBean> returnDownloads, FormReturn formReturn) {
        byte[] bytes;
        try {
            // 报表模板名取得
            String templateFile = Properties.readValue(WmsConstants.ReportItems.GoodsReturn.TEMPLATE_PATH) + WmsConstants.ReportItems.GoodsReturn.TEMPLATE_NAME;
            // 报表模板名读入
            InputStream templateInput = new FileInputStream(templateFile);
            Workbook workbook = WorkbookFactory.create(templateInput);

            setGoodsReturnReportContent(workbook, returnDownloads, formReturn);

            // 输出内容
            ByteArrayOutputStream outData = new ByteArrayOutputStream();
            workbook.write(outData);
            bytes = outData.toByteArray();
            // 关闭
            templateInput.close();
            workbook.close();
            outData.close();
        } catch (Exception e) {
            logger.info("退货记录报表下载失败：" + e);
            throw new BusinessException(WmsMsgConstants.ReportMsg.INVDELRPT_DOWNLOAD_FAILED);
        }
        return bytes;
    }

    /**
     * 设置退货记录报表内容
     *
     * @param workbook        报表模板
     * @param returnDownloads 下载内容
     * @param formReturn      查询参数Bean
     */
    private void setGoodsReturnReportContent(Workbook workbook, List<FormReturnDownloadBean> returnDownloads, FormReturn formReturn) {

        // 模板Sheet
        int sheetNo = WmsConstants.ReportItems.GoodsReturn.TEMPLATE_SHEET_NO;

        // 初始行
        int intRow = WmsConstants.ReportItems.GoodsReturn.TEMPLATE_FIRSTROW_NO;

        // 按照模板克隆一个sheet
        Sheet sheet = workbook.cloneSheet(sheetNo);

        // 设置模板sheet页后的sheet名为报告sheet名
        workbook.setSheetName(sheetNo + 1, WmsConstants.ReportItems.GoodsReturn.RPT_SHEET_NAME);

        for (FormReturnDownloadBean formReturnDownloadBean : returnDownloads) {
            if (intRow != WmsConstants.ReportItems.GoodsReturn.TEMPLATE_FIRSTROW_NO) {
                Row newRow = sheet.createRow(intRow);
                //根据第2行（第一行是标题）格式设置每行的格式
                for (int col = 0; col < WmsConstants.ReportItems.GoodsReturn.COLNUM_MAX; col++) {
                    Cell newCell = newRow.createCell(col);
                    Cell oldCell = sheet.getRow(WmsConstants.ReportItems.GoodsReturn.TEMPLATE_FIRSTROW_NO).getCell(col);
                    newCell.setCellStyle(oldCell.getCellStyle());
                }
            }
            // 得到当前行
            Row currentRow = sheet.getRow(intRow);
            // no
            currentRow.getCell(WmsConstants.ReportItems.GoodsReturn.Col.COLNUM_NO).setCellValue(String.valueOf(intRow));
            // order_channel
            currentRow.getCell(WmsConstants.ReportItems.GoodsReturn.Col.COLNUM_ORDER_CHANNEL).setCellValue(StringUtils.null2Space(formReturnDownloadBean.getOrder_channel()));
            // store
            currentRow.getCell(WmsConstants.ReportItems.GoodsReturn.Col.COLNUM_STORE).setCellValue(StringUtils.null2Space(formReturnDownloadBean.getStore()));
            // return_time
            currentRow.getCell(WmsConstants.ReportItems.GoodsReturn.Col.COLNUM_RETURN_TIME).setCellValue(StringUtils.null2Space(formReturnDownloadBean.getReturn_time()));
            // order_time
            currentRow.getCell(WmsConstants.ReportItems.GoodsReturn.Col.COLNUM_ORDER_TIME).setCellValue(StringUtils.null2Space(formReturnDownloadBean.getOrder_time()));
            // source_order_id
            currentRow.getCell(WmsConstants.ReportItems.GoodsReturn.Col.COLNUM_SOURCE_ORDER_ID).setCellValue(StringUtils.null2Space(formReturnDownloadBean.getSource_order_id()));
            // client_order_id
            currentRow.getCell(WmsConstants.ReportItems.GoodsReturn.Col.COLNUM_CLIENT_ORDER_ID).setCellValue(StringUtils.null2Space(formReturnDownloadBean.getClient_order_id()));
            // order_num
            currentRow.getCell(WmsConstants.ReportItems.GoodsReturn.Col.COLNUM_ORDER_NUM).setCellValue(StringUtils.null2Space(formReturnDownloadBean.getOrder_num()));
            // res_id
            currentRow.getCell(WmsConstants.ReportItems.GoodsReturn.Col.COLNUM_RES_ID).setCellValue(StringUtils.null2Space(formReturnDownloadBean.getRes_id()));
            // sku
            currentRow.getCell(WmsConstants.ReportItems.GoodsReturn.Col.COLNUM_SKU).setCellValue(StringUtils.null2Space(formReturnDownloadBean.getSku()));
            // condition
            currentRow.getCell(WmsConstants.ReportItems.GoodsReturn.Col.COLNUM_CONDITION).setCellValue(StringUtils.null2Space(formReturnDownloadBean.getCondition()));
            // received_from
            currentRow.getCell(WmsConstants.ReportItems.GoodsReturn.Col.COLNUM_RECEIVED_FROM).setCellValue(StringUtils.null2Space(formReturnDownloadBean.getReceived_from()));
            // tracking_no
            currentRow.getCell(WmsConstants.ReportItems.GoodsReturn.Col.COLNUM_TRACKING_NO).setCellValue(StringUtils.null2Space(formReturnDownloadBean.getTracking_no()));
            // notes
            currentRow.getCell(WmsConstants.ReportItems.GoodsReturn.Col.COLNUM_NOTES).setCellValue(StringUtils.null2Space(formReturnDownloadBean.getNotes()));
            // session_status
            currentRow.getCell(WmsConstants.ReportItems.GoodsReturn.Col.COLNUM_SESSION_STATUS).setCellValue(StringUtils.null2Space(formReturnDownloadBean.getSession_status()));
            intRow = intRow + 1;
        }
        // 如果有记录的话，删除模板sheet
        if (returnDownloads.size() > 0) {
            workbook.removeSheetAt(sheetNo);
        }
    }

}
