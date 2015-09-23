package com.voyageone.bi.task;

import java.util.List;

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
import com.voyageone.bi.task.sup.FinancialReportTask;
import com.voyageone.bi.tranbean.UserInfoBean;

/**
 * Created by Kylin on 2015/7/24.
 */
@Service
public class DataReportTask {
    private static Log logger = LogFactory.getLog(DataReportTask.class);

    @Autowired
    private FinancialReportTask financialReportTask;

    public void ajaxGetFinancialMonthlyReportData(AjaxFinancialMonthlyReportBean bean, UserInfoBean userInfoBean) throws BiException {

        AjaxFinancialMonthlyReportBean.Result result = bean.getResponseBean();
        try {
            List<MonthlyReportDisBean> listMonthlyReportDisBean = financialReportTask.getFinancialMonthlyReportInfo(bean, userInfoBean);

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
            List<DetailReportDisBean> listCostReportDisBean = financialReportTask.getFinancialCostReportInfo(bean, userInfoBean);

            result.setDetailReportDisBean(listCostReportDisBean);
            result.setReport_page(bean.getReport_page());
            result.setReport_record_count(bean.getReport_record_count());
            result.setReqResult(Contants.AJAX_RESULT_OK);
        } catch (Exception e) {
            logger.error("ajaxGetFinancialMonthlyReportData error:", e);
            result.setReqResult(Contants.AJAX_RESULT_FALSE);
            throw new BiException(e, "ajaxGetFinancialMonthlyReportData");
        }

    }

    public void ajaxGetFinancialTaxReportData(AjaxFinancialTaxReportBean bean, UserInfoBean userInfoBean) throws BiException {

        AjaxFinancialTaxReportBean.Result result = bean.getResponseBean();
        try {
            List<TaxDetailReportDisBean> listTaxReportDisBean = financialReportTask.getFinancialTaxReportInfo(bean, userInfoBean);

            result.setTaxDetailReportDisBean(listTaxReportDisBean);
            result.setReport_page(bean.getReport_page());
            result.setReport_record_count(bean.getReport_record_count());
            result.setReqResult(Contants.AJAX_RESULT_OK);
        } catch (Exception e) {
            logger.error("ajaxGetFinancialMonthlyReportData error:", e);
            result.setReqResult(Contants.AJAX_RESULT_FALSE);
            throw new BiException(e, "ajaxGetFinancialMonthlyReportData");
        }

    }
}
