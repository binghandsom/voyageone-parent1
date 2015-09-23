package com.voyageone.bi.task.sup;

import com.voyageone.bi.base.BiApplication;
import com.voyageone.bi.bean.BillBean;
import com.voyageone.bi.dao.ReportInfoDao;
import com.voyageone.bi.disbean.DetailReportDisBean;
import com.voyageone.bi.disbean.MonthlyReportDisBean;
import com.voyageone.bi.disbean.TaxDetailReportDisBean;
import jxl.Cell;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.Number;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class FinancialExportTask {

    private static Log logger = LogFactory.getLog(FinancialExportTask.class);

    @Autowired
    private ReportInfoDao reportInfoDao;

    /**
     * getFinanceCostWorkBook
     *
     * @param strWebRootPath
     * @param strCreateWorkBookPath
     * @return
     */
    public WritableWorkbook getFinanceCostWorkBook(String strWebRootPath, String strCreateWorkBookPath) {


        WritableWorkbook writableWorkbook = null;
        try {
            Workbook workbook = Workbook.getWorkbook(new File(strWebRootPath + BiApplication.readValue("finance_report_cost_path") + BiApplication.readValue("finance_report_file")));
            writableWorkbook = Workbook.createWorkbook(new File(strCreateWorkBookPath), workbook);
        } catch (IOException e) {
            logger.error("getFinanceCostWorkBook error", e);
            writableWorkbook = null;
        } catch (BiffException e) {
            logger.error("getFinanceCostWorkBook error", e);
            writableWorkbook = null;
        } finally {
        }
        return writableWorkbook;
    }

    /**
     * exportExcelCostReport
     *
     * @param workbook
     * @param sheetname
     * @param beanList
     */
    public void exportExcelCostReport(WritableWorkbook workbook, String sheetname, List<DetailReportDisBean> beanList) {

        // 生成一个表格
        WritableSheet writableSheet = workbook.getSheet(sheetname);

        if (writableSheet != null) {
            Cell cell = writableSheet.findCell(BiApplication.readValue("seq"));
            int iColumn = cell.getColumn();
            int iRow = cell.getRow() + 1;

            for (int indexRow = 0; indexRow < beanList.size(); indexRow++) {
                try {

                    int indexColumn = 0;

                    //SEQ
                    Label seq = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(indexRow + 1));
                    writableSheet.addCell(seq.copyTo(iColumn + indexColumn, iRow + indexRow));

                    indexColumn++;

                    //Year
                    Label year = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(beanList.get(indexRow).getYear_calc()));
                    writableSheet.addCell(year.copyTo(iColumn + indexColumn, iRow + indexRow));

                    indexColumn++;

                    //Month
                    Label month = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(beanList.get(indexRow).getMonth_calc()));
                    writableSheet.addCell(month.copyTo(iColumn + indexColumn, iRow + indexRow));

                    indexColumn++;

                    //Channel
                    if (beanList.get(indexRow).getOrder_channel_name() != null) {
                        Label channel = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(beanList.get(indexRow).getOrder_channel_name()));
                        writableSheet.addCell(channel.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Tracking Number
                    if (beanList.get(indexRow).getTracking_no() != null) {
                        Label tracking_no = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(beanList.get(indexRow).getTracking_no()));
                        writableSheet.addCell(tracking_no.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Client Order Number
                    if (beanList.get(indexRow).getClient_order_number() != null) {
                        Label client_order_num = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(beanList.get(indexRow).getClient_order_number()));
                        writableSheet.addCell(client_order_num.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //VO Order Number
                    if (beanList.get(indexRow).getOrder_number() != null) {
                        Label order_num = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(beanList.get(indexRow).getOrder_number()));
                        writableSheet.addCell(order_num.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Web Order Number
                    if (beanList.get(indexRow).getSource_order_id() != null) {
                        Label source_order_num = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(beanList.get(indexRow).getSource_order_id()));
                        writableSheet.addCell(source_order_num.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Weight LB(Order)
                    if (beanList.get(indexRow).getActual_shipped_weightLB() != null) {
                        Number weight_lb_order = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getActual_shipped_weightLB().doubleValue());
                        writableSheet.addCell(weight_lb_order.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Bill Weight(Order)
                    if (beanList.get(indexRow).getShipped_weightLB() != null) {
                        Number bill_weight_lb_order = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getShipped_weightLB().doubleValue());
                        writableSheet.addCell(bill_weight_lb_order.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Weight LB(Express)
                    if (beanList.get(indexRow).getGoods_weight_lb() != null) {
                        Number weight_lb_express = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getGoods_weight_lb().doubleValue());
                        writableSheet.addCell(weight_lb_express.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Bill Weight(Express)
                    if (beanList.get(indexRow).getExpress_weight_lb() != null) {
                        Number bill_weight_lb_express = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getExpress_weight_lb().doubleValue());
                        writableSheet.addCell(bill_weight_lb_express.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Ship Date
                    if (beanList.get(indexRow).getShip_date() != null) {
                        Label ship_date = new Label(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getShip_date());
                        writableSheet.addCell(ship_date.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Duties & Taxes RMB
                    if (beanList.get(indexRow).getTax_actual() != null) {
                        Number tax_actual = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getTax_actual().doubleValue());
                        writableSheet.addCell(tax_actual.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }


                    indexColumn++;

                    //Express Fee
                    if (beanList.get(indexRow).getTranspotation_amount() != null) {
                        Number transpotation_amount = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getTranspotation_amount().doubleValue());
                        writableSheet.addCell(transpotation_amount.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Mail Fee
                    if (beanList.get(indexRow).getMail_fee() != null) {
                        Number mail_fee = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getMail_fee().doubleValue());
                        writableSheet.addCell(mail_fee.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Ground Handling Fee
                    if (beanList.get(indexRow).getGround_handling_fee() != null) {
                        Number ground_handling_fee = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getGround_handling_fee().doubleValue());
                        writableSheet.addCell(ground_handling_fee.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Storage Charges
                    if (beanList.get(indexRow).getStorage_charges() != null) {
                        Number storage_charges = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getStorage_charges().doubleValue());
                        writableSheet.addCell(storage_charges.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                } catch (WriteException e) {
                    logger.error("exportExcelCostReport error", e);
                }
            }
        }


    }

    /**
     * getFinanceMonthlyWorkBook
     *
     * @param strWebRootPath
     * @param strCreateWorkBookPath
     * @return
     */
    public WritableWorkbook getFinanceMonthlyWorkBook(String strWebRootPath, String strCreateWorkBookPath) {

        WritableWorkbook writableWorkbook = null;
        try {
            Workbook workbook = Workbook.getWorkbook(new File(strWebRootPath + BiApplication.readValue("finance_report_monthly_path") + BiApplication.readValue("finance_report_file")));
            writableWorkbook = Workbook.createWorkbook(new File(strCreateWorkBookPath), workbook);
        } catch (IOException e) {
            logger.error("getFinanceMonthlyWorkBook error", e);
            writableWorkbook = null;
        } catch (BiffException e) {
            logger.error("getFinanceMonthlyWorkBook error", e);
            writableWorkbook = null;
        } finally {
        }
        return writableWorkbook;
    }

    /**
     * exportExcelMonthlyReport
     *
     * @param workbook
     * @param sheetname
     * @param beanList
     */
    public void exportExcelMonthlyReport(WritableWorkbook workbook, String sheetname, List<MonthlyReportDisBean> beanList) {

        // 获取既存Sheet
        WritableSheet writableSheet = workbook.getSheet(sheetname);

        if (writableSheet != null) {
            Cell cell = writableSheet.findCell(BiApplication.readValue("seq"));
            int iColumn = cell.getColumn();
            int iRow = cell.getRow() + 1;

            for (int indexRow = 0; indexRow < beanList.size(); indexRow++) {
                try {

                    int indexColumn = 0;

                    //SEQ
                    Label seq = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(indexRow + 1));
                    writableSheet.addCell(seq.copyTo(iColumn + indexColumn, iRow + indexRow));

                    indexColumn++;

                    //Year
                    Label year = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(beanList.get(indexRow).getYear_calc()));
                    writableSheet.addCell(year.copyTo(iColumn + indexColumn, iRow + indexRow));

                    indexColumn++;

                    //Month
                    Label month = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(beanList.get(indexRow).getMonth_calc()));
                    writableSheet.addCell(month.copyTo(iColumn + indexColumn, iRow + indexRow));

                    indexColumn++;

                    //Channel
                    if (beanList.get(indexRow).getOrder_channel_name() != null) {
                        Label channel = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(beanList.get(indexRow).getOrder_channel_name()));
                        writableSheet.addCell(channel.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Weight LB(Order)
                    if (beanList.get(indexRow).getActual_shipped_weightLB() != null) {
                        Number weight_lb_order = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getActual_shipped_weightLB().doubleValue());
                        writableSheet.addCell(weight_lb_order.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Bill Weight(Order)
                    if (beanList.get(indexRow).getShipped_weightLB() != null) {
                        Number bill_weight_lb_order = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getShipped_weightLB().doubleValue());
                        writableSheet.addCell(bill_weight_lb_order.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Weight LB(Express)
                    if (beanList.get(indexRow).getGoods_weight_lb() != null) {
                        Number weight_lb_express = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getGoods_weight_lb().doubleValue());
                        writableSheet.addCell(weight_lb_express.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Bill Weight(Express)
                    if (beanList.get(indexRow).getExpress_weight_lb() != null) {
                        Number bill_weight_lb_express = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getExpress_weight_lb().doubleValue());
                        writableSheet.addCell(bill_weight_lb_express.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Duties & Taxes RMB
                    if (beanList.get(indexRow).getTax_actual() != null) {
                        Number tax_actual = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getTax_actual().doubleValue());
                        writableSheet.addCell(tax_actual.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }


                    indexColumn++;

                    //Express Fee
                    if (beanList.get(indexRow).getTranspotation_amount() != null) {
                        Number transpotation_amount = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getTranspotation_amount().doubleValue());
                        writableSheet.addCell(transpotation_amount.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Mail Fee
                    if (beanList.get(indexRow).getMail_fee() != null) {
                        Number mail_fee = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getMail_fee().doubleValue());
                        writableSheet.addCell(mail_fee.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Ground Handling Fee
                    if (beanList.get(indexRow).getGround_handling_fee() != null) {
                        Number ground_handling_fee = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getGround_handling_fee().doubleValue());
                        writableSheet.addCell(ground_handling_fee.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Storage Charges
                    if (beanList.get(indexRow).getStorage_charges() != null) {
                        Number storage_charges = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getStorage_charges().doubleValue());
                        writableSheet.addCell(storage_charges.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                } catch (WriteException e) {
                    logger.error("exportExcelMonthlyReport error", e);
                }
            }
        }
    }

    /**
     * getFinanceTaxWorkBook
     *
     * @param strWebRootPath
     * @param strCreateWorkBookPath
     * @return
     */
    public WritableWorkbook getFinanceTaxWorkBook(String strWebRootPath, String strCreateWorkBookPath) {

        WritableWorkbook writableWorkbook = null;
        try {
            Workbook workbook = Workbook.getWorkbook(new File(strWebRootPath + BiApplication.readValue("finance_report_tax_path") + BiApplication.readValue("finance_report_file")));
            writableWorkbook = Workbook.createWorkbook(new File(strCreateWorkBookPath), workbook);
        } catch (IOException e) {
            logger.error("getFinanceTaxWorkBook error", e);
            writableWorkbook = null;
        } catch (BiffException e) {
            logger.error("getFinanceTaxWorkBook error", e);
            writableWorkbook = null;
        } finally {
        }
        return writableWorkbook;
    }

    /**
     * exportExcelTaxReport
     *
     * @param workbook
     * @param sheetname
     * @param beanList
     */
    public void exportExcelTaxReport(WritableWorkbook workbook, String sheetname, List<TaxDetailReportDisBean> beanList) {

        // 生成一个表格
        WritableSheet writableSheet = workbook.getSheet(sheetname);

        if (writableSheet != null) {
            Cell cell = writableSheet.findCell(BiApplication.readValue("seq"));
            int iColumn = cell.getColumn();
            int iRow = cell.getRow() + 1;

            for (int indexRow = 0; indexRow < beanList.size(); indexRow++) {
                try {

                    int indexColumn = 0;

                    //SEQ
                    Label seq = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(indexRow + 1));
                    writableSheet.addCell(seq.copyTo(iColumn + indexColumn, iRow + indexRow));

                    indexColumn++;

                    //Year
                    Label year = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(beanList.get(indexRow).getYear_calc()));
                    writableSheet.addCell(year.copyTo(iColumn + indexColumn, iRow + indexRow));

                    indexColumn++;

                    //Month
                    Label month = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(beanList.get(indexRow).getMonth_calc()));
                    writableSheet.addCell(month.copyTo(iColumn + indexColumn, iRow + indexRow));

                    indexColumn++;

                    //Channel
                    if (beanList.get(indexRow).getOrder_channel_name() != null) {
                        Label channel = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(beanList.get(indexRow).getOrder_channel_name()));
                        writableSheet.addCell(channel.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Tracking Number
                    if (beanList.get(indexRow).getTracking_no() != null) {
                        Label tracking_no = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(beanList.get(indexRow).getTracking_no()));
                        writableSheet.addCell(tracking_no.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;


                    //Client Order Number
                    if (beanList.get(indexRow).getClient_order_number() != null) {
                        Label client_order_num = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(beanList.get(indexRow).getClient_order_number()));
                        writableSheet.addCell(client_order_num.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //VO Order Number
                    if (beanList.get(indexRow).getOrder_number() != null) {
                        Label order_num = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(beanList.get(indexRow).getOrder_number()));
                        writableSheet.addCell(order_num.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Tax Bill ID
                    if (beanList.get(indexRow).getPay_in_warrant_num() != null) {
                        Label tax_bill_id = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(beanList.get(indexRow).getPay_in_warrant_num()));
                        writableSheet.addCell(tax_bill_id.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Weight LB(Order)
                    if (beanList.get(indexRow).getActual_shipped_weightLB() != null) {
                        Number weight_lb_order = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getActual_shipped_weightLB().doubleValue());
                        writableSheet.addCell(weight_lb_order.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Bill Weight(Order)
                    if (beanList.get(indexRow).getShipped_weightLB() != null) {
                        Number bill_weight_lb_order = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getShipped_weightLB().doubleValue());
                        writableSheet.addCell(bill_weight_lb_order.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Weight LB(Express)
                    if (beanList.get(indexRow).getGoods_weight_lb() != null) {
                        Number weight_lb_express = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getGoods_weight_lb().doubleValue());
                        writableSheet.addCell(weight_lb_express.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Bill Weight(Express)
                    if (beanList.get(indexRow).getExpress_weight_lb() != null) {
                        Number bill_weight_lb_express = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getExpress_weight_lb().doubleValue());
                        writableSheet.addCell(bill_weight_lb_express.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Ship Date
                    if (beanList.get(indexRow).getShip_date() != null) {
                        Label ship_date = new Label(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getShip_date());
                        writableSheet.addCell(ship_date.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Web Order Number
                    if (beanList.get(indexRow).getSource_order_id() != null) {
                        Label source_order_num = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(beanList.get(indexRow).getSource_order_id()));
                        writableSheet.addCell(source_order_num.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Duties & Taxes RMB
                    if (beanList.get(indexRow).getTax_actual() != null) {
                        Number tax_actual = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getTax_actual().doubleValue());
                        writableSheet.addCell(tax_actual.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }


                    indexColumn++;

                    //Duties & Taxes USD
                    if (beanList.get(indexRow).getTax_actual_usd() != null) {
                        Number tax_actual_usd = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getTax_actual_usd().doubleValue());
                        writableSheet.addCell(tax_actual_usd.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //Exchange Rate
                    if (beanList.get(indexRow).getExchange_rate() != null) {
                        Number exchange_rate = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getExchange_rate().doubleValue());
                        writableSheet.addCell(exchange_rate.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                } catch (WriteException e) {
                    logger.error("exportExcelTaxReport error", e);
                }
            }
        }
    }

    /**
     * getFinanceInvoiceWorkBook
     *
     * @param strWebRootPath
     * @param strCreateWorkBookPath
     * @return
     */
    public WritableWorkbook getFinanceInvoiceWorkBook(String strWebRootPath, String strCreateWorkBookPath) {

        WritableWorkbook writableWorkbook = null;
        try {
            Workbook workbook = Workbook.getWorkbook(new File(strWebRootPath + BiApplication.readValue("finance_report_invoice_path") + BiApplication.readValue("finance_report_file")));
            writableWorkbook = Workbook.createWorkbook(new File(strCreateWorkBookPath), workbook);
        } catch (IOException e) {
            logger.error("getFinanceInvoiceWorkBook error", e);
            writableWorkbook = null;
        } catch (BiffException e) {
            logger.error("getFinanceInvoiceWorkBook error", e);
            writableWorkbook = null;
        } finally {
        }
        return writableWorkbook;
    }

    /**
     * exportExcelInvoiceReport
     *
     * @param workbook
     * @param beanList
     */
    public void exportExcelInvoiceReport(WritableWorkbook workbook, List<BillBean> beanList) {

        // 生成一个表格
        WritableSheet writableSheet = workbook.getSheet(BiApplication.readValue("finance_report_sheet_invoice_list"));


        if (writableSheet != null) {
            Cell cell = writableSheet.findCell(BiApplication.readValue("invoice_no"));

            int iColumn = cell.getColumn();
            int iRow = cell.getRow() + 1;

            for (int indexRow = 0; indexRow < beanList.size(); indexRow++) {
                try {

                    int indexColumn = 0;

                    //Invoice No.
                    if (beanList.get(indexRow).getInvoice_num() != null) {
                        Label invoice_num = new Label(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getInvoice_num());
                        writableSheet.addCell(invoice_num.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //文件名
                    if (beanList.get(indexRow).getFile_name() != null) {
                        Label file_name = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(beanList.get(indexRow).getFile_name()));
                        writableSheet.addCell(file_name.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //国内邮费
                    if (beanList.get(indexRow).getTranspotation_fee() != null) {
                        Number transpotation_fee = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getTranspotation_fee().doubleValue());
                        writableSheet.addCell(transpotation_fee.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //地勤费
                    if (beanList.get(indexRow).getGround_handling_fee() != null) {
                        Number ground_handling_fee = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getGround_handling_fee().doubleValue());
                        writableSheet.addCell(ground_handling_fee.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //仓租费
                    if (beanList.get(indexRow).getStorage_charges() != null) {
                        Number storage_charges = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getStorage_charges().doubleValue());
                        writableSheet.addCell(storage_charges.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //邮件处理费（6元/件）
                    if (beanList.get(indexRow).getMail_fee() != null) {
                        Number mail_fee = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getMail_fee().doubleValue());
                        writableSheet.addCell(mail_fee.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //身份证验证费（3元/次）
                    if (beanList.get(indexRow).getIdentification_fee() != null) {
                        Number identification_fee = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getIdentification_fee().doubleValue());
                        writableSheet.addCell(identification_fee.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                    indexColumn++;

                    //入境税费
                    if (beanList.get(indexRow).getTax() != null) {
                        Number tax = new Number(iColumn + indexColumn, iRow + indexRow, beanList.get(indexRow).getTax().doubleValue());
                        writableSheet.addCell(tax.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }

                } catch (WriteException e) {
                    logger.error("exportExcelInvoiceReport error", e);
                }
            }
        }

    }

}
