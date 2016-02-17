package com.voyageone.bi.task;

import java.util.List;

import com.voyageone.bi.ajax.bean.AjaxFinancialOfficialTaxReportBean;
import com.voyageone.bi.bean.OfficialTaxBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.bi.ajax.bean.AjaxFinancialCostReportBean;
import com.voyageone.bi.ajax.bean.AjaxFinancialMonthlyReportBean;
import com.voyageone.bi.ajax.bean.AjaxFinancialTaxReportBean;
import com.voyageone.bi.base.BiException;
import com.voyageone.bi.commonutils.Contants;
import com.voyageone.bi.disbean.DetailReportDisBean;
import com.voyageone.bi.disbean.MonthlyReportDisBean;
import com.voyageone.bi.disbean.TaxDetailReportDisBean;
import com.voyageone.bi.task.sup.FinancialExcelReportTask;
import com.voyageone.bi.tranbean.UserInfoBean;

/**
 * Created by Kylin on 2015/7/24.
 */
@Service
public class FinanceReportTask {
    private static Log logger = LogFactory.getLog(FinanceReportTask.class);

    @Autowired
    private FinancialExcelReportTask financialExcelReportTask;

    public void ajaxGetFinancialMonthlyReportData(AjaxFinancialMonthlyReportBean bean, UserInfoBean userInfoBean) throws BiException {

        AjaxFinancialMonthlyReportBean.Result result = bean.getResponseBean();
        try {
            List<MonthlyReportDisBean> listMonthlyReportDisBean = financialExcelReportTask.getFinancialMonthlyReportInfo(bean, userInfoBean);

            result.setMonthlyReportDisBean(listMonthlyReportDisBean);
            result.setReport_page(bean.getReport_page());
            result.setReport_record_count(bean.getReport_record_count());
            result.setReqResult(Contants.AJAX_RESULT_OK);
        } catch (Exception e) {
            logger.error("ajaxGetFinancialMonthlyReportData error:", e);
            result.setReqResult(Contants.AJAX_RESULT_FALSE);
            throw new BiException(e, "ajaxGetFinancialMonthlyReportData");
        }

    }

    public void ajaxGetFinancialCostReportData(AjaxFinancialCostReportBean bean, UserInfoBean userInfoBean) throws BiException {

        AjaxFinancialCostReportBean.Result result = bean.getResponseBean();
        try {
            List<DetailReportDisBean> listCostReportDisBean = financialExcelReportTask.getFinancialCostReportInfo(bean, userInfoBean);

            result.setDetailReportDisBean(listCostReportDisBean);
            result.setReport_page(bean.getReport_page());
            result.setReport_record_count(bean.getReport_record_count());
            result.setReqResult(Contants.AJAX_RESULT_OK);
        } catch (Exception e) {
            logger.error("ajaxGetFinancialCostReportData error:", e);
            result.setReqResult(Contants.AJAX_RESULT_FALSE);
            throw new BiException(e, "ajaxGetFinancialCostReportData");
        }

    }

    public void ajaxGetFinancialTaxReportData(AjaxFinancialTaxReportBean bean, UserInfoBean userInfoBean) throws BiException {

        AjaxFinancialTaxReportBean.Result result = bean.getResponseBean();
        try {
            List<TaxDetailReportDisBean> listTaxReportDisBean = financialExcelReportTask.getFinancialTaxReportInfo(bean, userInfoBean);

            result.setTaxDetailReportDisBean(listTaxReportDisBean);
            result.setReport_page(bean.getReport_page());
            result.setReport_record_count(bean.getReport_record_count());
            result.setReqResult(Contants.AJAX_RESULT_OK);
        } catch (Exception e) {
            logger.error("ajaxGetFinancialTaxReportData error:", e);
            result.setReqResult(Contants.AJAX_RESULT_FALSE);
            throw new BiException(e, "ajaxGetFinancialTaxReportData");
        }

    }

    public void ajaxGetFinancialOfficialTaxReportData(AjaxFinancialOfficialTaxReportBean bean, UserInfoBean userInfoBean) throws BiException {

        AjaxFinancialOfficialTaxReportBean.Result result = bean.getResponseBean();
        try {
            List<OfficialTaxBean> listOfficialTaxBean = financialExcelReportTask.getFinancialOfficialTaxReportInfo(bean, userInfoBean);

            result.setOfficialTaxBean(listOfficialTaxBean);
            result.setReport_page(bean.getReport_page());
            result.setReport_record_count(bean.getReport_record_count());
            result.setReqResult(Contants.AJAX_RESULT_OK);
        } catch (Exception e) {
            logger.error("ajaxGetFinancialOfficialTaxReportData error:", e);
            result.setReqResult(Contants.AJAX_RESULT_FALSE);
            throw new BiException(e, "ajaxGetFinancialOfficialTaxReportData");
        }

    }
}
