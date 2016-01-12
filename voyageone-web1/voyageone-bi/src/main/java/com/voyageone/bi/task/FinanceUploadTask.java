package com.voyageone.bi.task;

import com.voyageone.bi.ajax.bean.AjaxFinancialBillBean;
import com.voyageone.bi.ajax.bean.AjaxFinancialBillErrorBean;
import com.voyageone.bi.base.BiException;
import com.voyageone.bi.bean.BillBean;
import com.voyageone.bi.bean.BillErrorBean;
import com.voyageone.bi.bean.BillErrorExplanationBean;
import com.voyageone.bi.commonutils.Contants;
import com.voyageone.bi.task.sup.FinancialBillTask;
import com.voyageone.bi.tranbean.UserInfoBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Kylin on 2015/7/24.
 */
@Service
public class FinanceUploadTask {
    private static Log logger = LogFactory.getLog(FinanceUploadTask.class);

    @Autowired
    private FinancialBillTask financialBillTask;
    @Autowired
    private FinanceUpdateTask financeUpdateTask;

    public void ajaxGetFinancialTaxBillData(AjaxFinancialBillBean bean, UserInfoBean userInfoBean) throws BiException {

        AjaxFinancialBillBean.Result result = bean.getResponseBean();
        try {
            List<BillBean> listTaxBillBean = financialBillTask.getFinancialTaxBillInfo(bean, userInfoBean);

            result.setTaxBillBean(listTaxBillBean);
            result.setTax_page(bean.getTax_page());
            result.setTax_record_count(bean.getTax_record_count());
            result.setReqResult(Contants.AJAX_RESULT_OK);
        } catch (Exception e) {
            logger.error("ajaxGetFinancialTaxBillData error:", e);
            result.setReqResult(Contants.AJAX_RESULT_FALSE);
            throw new BiException(e, "ajaxGetFinancialTranspotationBillData");
        }

    }

    public void ajaxGetFinancialTranspotationBillData(AjaxFinancialBillBean bean, UserInfoBean userInfoBean) throws BiException {

        AjaxFinancialBillBean.Result result = bean.getResponseBean();
        try {
            List<BillBean> listTranspotationBillBean = financialBillTask.getFinancialTranspotationBillInfo(bean, userInfoBean);

            result.setTranspotationBillBean(listTranspotationBillBean);
            result.setTranspotation_page(bean.getTranspotation_page());
            result.setTranspotation_record_count(bean.getTranspotation_record_count());
            result.setReqResult(Contants.AJAX_RESULT_OK);
        } catch (Exception e) {
            result.setReqResult(Contants.AJAX_RESULT_FALSE);
            throw new BiException(e, "ajaxGetFinancialTranspotationBillData");
        }

    }

    public void ajaxGetFinancialErrorBillData(AjaxFinancialBillErrorBean bean, UserInfoBean userInfoBean) throws BiException {

        AjaxFinancialBillErrorBean.Result result = bean.getResponseBean();
        try {
            List<BillErrorBean> billErrorBeanList = financialBillTask.getFinancialErrorBillInfo(bean, userInfoBean);

            result.setBillErrorBean(billErrorBeanList);
            result.setError_page(bean.getError_page());
            result.setError_record_count(bean.getError_record_count());
            result.setReqResult(Contants.AJAX_RESULT_OK);
        } catch (Exception e) {
            logger.error("ajaxGetFinancialErrorBillData error:", e);
            result.setReqResult(Contants.AJAX_RESULT_FALSE);
            throw new BiException(e, "ajaxGetFinancialErrorBillData");
        }

    }

    public void ajaxGetFinancialErrorExplanationBillData(AjaxFinancialBillErrorBean bean, UserInfoBean userInfoBean) throws BiException {

        AjaxFinancialBillErrorBean.Result result = bean.getResponseBean();
        try {
            List<BillErrorExplanationBean> billErrorExplanationBeanList = financialBillTask.getFinancialErrorExplanationBillInfo(bean, userInfoBean);

            result.setBillErrorExplanationBean(billErrorExplanationBeanList);
            result.setError_page(bean.getExplanation_page());
            result.setError_record_count(bean.getExplanation_record_count());
            result.setReqResult(Contants.AJAX_RESULT_OK);
        } catch (Exception e) {
            logger.error("ajaxGetFinancialErrorBillData error:", e);
            result.setReqResult(Contants.AJAX_RESULT_FALSE);
            throw new BiException(e, "ajaxGetFinancialErrorBillData");
        }

    }

    public void ajaxUpdateFinancialBillData(AjaxFinancialBillErrorBean bean, UserInfoBean userInfoBean) throws BiException {

        AjaxFinancialBillErrorBean.Result result = bean.getResponseBean();
        try {
            List<BillErrorBean> billErrorBeanList = financialBillTask.getFinancialErrorBillInfo(bean, userInfoBean);

            if (bean.getError_record_count() <= 0){
                financeUpdateTask.run();
            }

            result.setBillErrorBean(billErrorBeanList);
            result.setError_page(bean.getError_page());
            result.setError_record_count(bean.getError_record_count());

            List<BillErrorExplanationBean> billErrorExplanationBeanList = financialBillTask.getFinancialErrorExplanationBillInfo(bean, userInfoBean);
            result.setBillErrorExplanationBean(billErrorExplanationBeanList);
            result.setError_page(bean.getExplanation_page());
            result.setError_record_count(bean.getExplanation_record_count());
            result.setReqResult(Contants.AJAX_RESULT_OK);
        } catch (Exception e) {
            logger.error("ajaxGetFinancialErrorBillData error:", e);
            result.setReqResult(Contants.AJAX_RESULT_FALSE);
            throw new BiException(e, "ajaxGetFinancialErrorBillData");
        }

    }
}
