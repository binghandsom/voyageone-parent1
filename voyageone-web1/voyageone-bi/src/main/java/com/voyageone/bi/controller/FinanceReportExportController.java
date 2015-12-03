package com.voyageone.bi.controller;

import com.voyageone.bi.ajax.bean.AjaxFinancialBillBean;
import com.voyageone.bi.ajax.bean.AjaxFinancialCostReportBean;
import com.voyageone.bi.ajax.bean.AjaxFinancialMonthlyReportBean;
import com.voyageone.bi.ajax.bean.AjaxFinancialTaxReportBean;
import com.voyageone.bi.base.BiApplication;
import com.voyageone.bi.base.BiException;
import com.voyageone.bi.commonutils.DateTimeUtil;
import com.voyageone.bi.commonutils.BiFileUtils;
import com.voyageone.bi.commonutils.SessionKey;
import com.voyageone.bi.task.DataExportTask;
import com.voyageone.bi.tranbean.UserInfoBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

@Controller
public class FinanceReportExportController {

    private static Log logger = LogFactory.getLog(FinanceReportExportController.class);

    // 页面初期化
    @Autowired
    private DataExportTask dataExportTask;

    /**
     * getReportCost
     */
    @RequestMapping(value = "/manage/getReportCost")
    public void getReportCost(HttpServletResponse response,
                              HttpServletRequest request,
                              AjaxFinancialCostReportBean bean) throws BiException {
        logger.info("getReportCost");
        if (!bean.checkInput()) {
            bean.WriteTo(response);
            return;
        }
        try {
            String webRootPath = BiFileUtils.getAbsolutRootPath(request);
            String file_name_sub = (DateTimeUtil.getDateTime()).replace(":", "_").replace("-", "_").replace(" ", "_") + BiApplication.readValue("finance_report_file");
            String file_name = BiApplication.readValue("finance_report_cost") + file_name_sub;
            String file_path = webRootPath + BiApplication.readValue("finance_report_path") + file_name;
            bean.setWeb_root_path(webRootPath);
            bean.setReport_file_name(file_name);
            bean.setReport_file_path(file_path);
            String webFilePath = request.getContextPath();
            bean.setWeb_file_path(webFilePath + BiApplication.readValue("finance_sub_path") + file_name);
            HttpSession session = request.getSession();
            UserInfoBean user = (UserInfoBean) session.getAttribute(SessionKey.LOGIN_INFO);
            dataExportTask.ajaxExportFinancialCostReport(bean, user);
            bean.WriteTo(response);
        } catch (Exception ex) {
            logger.error("getReportCost", ex);
        }

    }

    /**
     * getReportTax
     */
    @RequestMapping(value = "/manage/getReportTax")
    public void getReportTax(HttpServletResponse response,
                             HttpServletRequest request,
                             AjaxFinancialTaxReportBean bean) throws BiException {
        logger.info("getReportTax");
        if (!bean.checkInput()) {
            bean.WriteTo(response);
            return;
        }
        try {
            String webRootPath = BiFileUtils.getAbsolutRootPath(request);
            String file_name_sub = (DateTimeUtil.getDateTime()).replace(":", "_").replace("-", "_").replace(" ", "_") + BiApplication.readValue("finance_report_file");
            String file_name = BiApplication.readValue("finance_report_tax") + file_name_sub;
            String file_path = webRootPath + BiApplication.readValue("finance_report_path") + file_name;
            bean.setWeb_root_path(webRootPath);
            bean.setReport_file_name(file_name);
            bean.setReport_file_path(file_path);
            String webFilePath = request.getContextPath();
            bean.setWeb_file_path(webFilePath + BiApplication.readValue("finance_sub_path") + file_name);
            HttpSession session = request.getSession();
            UserInfoBean user = (UserInfoBean) session.getAttribute(SessionKey.LOGIN_INFO);
            dataExportTask.ajaxExportFinancialTaxReport(bean, user);
            bean.WriteTo(response);
        } catch (Exception ex) {
            logger.error("getReportTax", ex);
        }

    }

    /**
     * getReportMonthly
     */
    @RequestMapping(value = "/manage/getReportMonthly")
    public void getReportMonthly(HttpServletResponse response,
                                 HttpServletRequest request,
                                 AjaxFinancialMonthlyReportBean bean) throws BiException {
        logger.info("getDataReportMonthly");
        if (!bean.checkInput()) {
            bean.WriteTo(response);
            return;
        }
        try {
            String webRootPath = BiFileUtils.getAbsolutRootPath(request);
            String file_name_sub = (DateTimeUtil.getDateTime()).replace(":", "_").replace("-", "_").replace(" ", "_") + BiApplication.readValue("finance_report_file");
            String file_name = BiApplication.readValue("finance_report_monthly") + file_name_sub;
            String file_path = webRootPath + BiApplication.readValue("finance_report_path") + file_name;
            bean.setWeb_root_path(webRootPath);
            bean.setReport_file_name(file_name);
            bean.setReport_file_path(file_path);
            String webFilePath = request.getContextPath();
            bean.setWeb_file_path(webFilePath + BiApplication.readValue("finance_sub_path") + file_name);
            HttpSession session = request.getSession();
            UserInfoBean user = (UserInfoBean) session.getAttribute(SessionKey.LOGIN_INFO);
            dataExportTask.ajaxExportFinancialMonthlyReport(bean, user);
            bean.WriteTo(response);
        } catch (Exception ex) {
            logger.error("getReportMonthly", ex);
        }

    }

    /**
     * getReportInvoice
     */
    @RequestMapping(value = "/manage/getReportInvoice")
    public void getReportInvoice(HttpServletResponse response,
                                 HttpServletRequest request,
                                 AjaxFinancialBillBean bean) throws BiException {
        logger.info("getReportInvoice");
        if (!bean.checkInput()) {
            bean.WriteTo(response);
            return;
        }
        try {
            String webRootPath = BiFileUtils.getAbsolutRootPath(request);
            String file_name_sub = (DateTimeUtil.getDateTime()).replace(":", "_").replace("-", "_").replace(" ", "_") + BiApplication.readValue("finance_report_file");
            String file_name = BiApplication.readValue("finance_report_invoice") + file_name_sub;
            String file_path = webRootPath + BiApplication.readValue("finance_report_path") + file_name;
            bean.setWeb_root_path(webRootPath);
            bean.setReport_file_name(file_name);
            bean.setReport_file_path(file_path);
            String webFilePath = request.getContextPath();
            bean.setWeb_file_path(webFilePath + BiApplication.readValue("finance_sub_path") + file_name);
            HttpSession session = request.getSession();
            UserInfoBean user = (UserInfoBean) session.getAttribute(SessionKey.LOGIN_INFO);
            dataExportTask.ajaxExportFinancialBillReport(bean, user);
            bean.WriteTo(response);
        } catch (Exception ex) {
            logger.error("getReportInvoice", ex);
        }

    }

    /**
     * downloadReportInvoice
     */
    @RequestMapping(value = "/manage/downloadReportInvoice")
    private void downloadReportInvoice(HttpServletResponse response,
                                       HttpServletRequest request,
                                       AjaxFinancialBillBean bean) throws BiException {
        try {
            String webRootPath = BiFileUtils.getAbsolutRootPath(request);
            String strAbsoluteFilePath = webRootPath + BiApplication.readValue("finance_report_path") + bean.getReport_file_name();
            // path是指欲下载的文件的路径。
            File file = new File(strAbsoluteFilePath);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(strAbsoluteFilePath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(
                    response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            logger.error("downloadReportInvoice", ex);
        } catch (Exception ex) {
            logger.error("downloadReportInvoice", ex);
        }

    }

    /**
     * downloadReportCost
     */
    @RequestMapping(value = "/manage/downloadReportCost")
    private void downloadReportCost (HttpServletResponse response,
                                     HttpServletRequest request,
                                     AjaxFinancialCostReportBean bean)throws BiException {
        try {
            String webRootPath = BiFileUtils.getAbsolutRootPath(request);
            String strAbsoluteFilePath = webRootPath + BiApplication.readValue("finance_report_path") + bean.getReport_file_name();
            // path是指欲下载的文件的路径。
            File file = new File(strAbsoluteFilePath);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(strAbsoluteFilePath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(
                    response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            logger.error("downloadReportCost", ex);
        } catch (Exception ex) {
            logger.error("downloadReportCost", ex);
        }
    }


    /**
     * downloadReportTax
     */
    @RequestMapping(value = "/manage/downloadReportTax")
    private void downloadReportTax (HttpServletResponse response,
                                     HttpServletRequest request,
                                    AjaxFinancialTaxReportBean bean)throws BiException {
        try {
            String webRootPath = BiFileUtils.getAbsolutRootPath(request);
            String strAbsoluteFilePath = webRootPath + BiApplication.readValue("finance_report_path") + bean.getReport_file_name();
            // path是指欲下载的文件的路径。
            File file = new File(strAbsoluteFilePath);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(strAbsoluteFilePath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(
                    response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            logger.error("downloadReportTax", ex);
        } catch (Exception ex) {
            logger.error("downloadReportTax", ex);
        }
    }

    /**
     * downloadReportCost
     */
    @RequestMapping(value = "/manage/downloadReportMonthly")
    private void downloadReportMonthly (HttpServletResponse response,
                                    HttpServletRequest request,
                                    AjaxFinancialMonthlyReportBean bean)throws BiException {
        try {
            String webRootPath = BiFileUtils.getAbsolutRootPath(request);
            String strAbsoluteFilePath = webRootPath + BiApplication.readValue("finance_report_path") + bean.getReport_file_name();
            // path是指欲下载的文件的路径。
            File file = new File(strAbsoluteFilePath);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(strAbsoluteFilePath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(
                    response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            logger.error("downloadReportMonthly", ex);
        } catch (Exception ex) {
            logger.error("downloadReportMonthly", ex);
        }
    }
}
