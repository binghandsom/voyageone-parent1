package com.voyageone.bi.task.sup;

import com.voyageone.bi.ajax.bean.AjaxFinancialBillBean;
import com.voyageone.bi.ajax.bean.AjaxFinancialBillErrorBean;
import com.voyageone.bi.bean.BillBean;
import com.voyageone.bi.bean.BillErrorBean;
import com.voyageone.bi.bean.BillErrorExplanationBean;
import com.voyageone.bi.dao.BillInfoDao;
import com.voyageone.bi.tranbean.UserInfoBean;
import com.voyageone.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialBillTask {

    @Autowired
    private BillInfoDao billInfoDao;

    /**
     * getFinancialTranspotationBillInfo
     *
     * @param bean
     * @param userInfoBean
     * @return
     */
    public List<BillBean> getFinancialTranspotationBillInfo(AjaxFinancialBillBean bean, UserInfoBean userInfoBean) {

        int record_count = billInfoDao.getTranspotationBillCount(bean);

        bean.setTranspotation_record_count(record_count);

        setBean(bean);

        List<BillBean> result = billInfoDao.getTranspotationBillList(bean);

        return result;
    }

    /**
     * getFinancialTaxBillInfo
     *
     * @param bean
     * @param userInfoBean
     * @return
     */
    public List<BillBean> getFinancialTaxBillInfo(AjaxFinancialBillBean bean, UserInfoBean userInfoBean) {

        int record_count = billInfoDao.getTaxBillCount(bean);

        bean.setTax_record_count(record_count);

        setBean(bean);

        List<BillBean> result = billInfoDao.getTaxBillList(bean);

        return result;
    }

    /**
     * getFinancialErrorBillInfo
     *
     * @param bean
     * @param userInfoBean
     * @return
     */
    public List<BillErrorBean> getFinancialErrorBillInfo(AjaxFinancialBillErrorBean bean, UserInfoBean userInfoBean) {

        int record_count = billInfoDao.getBillErrorCount(bean);

        bean.setError_record_count(record_count);

        List<BillErrorBean> result = billInfoDao.getBillErrorList(bean);

        return result;
    }

    /**
     * getFinancialErrorBillInfo
     *
     * @param bean
     * @param userInfoBean
     * @return
     */
    public List<BillErrorExplanationBean> getFinancialErrorExplanationBillInfo(AjaxFinancialBillErrorBean bean, UserInfoBean userInfoBean) {

        int record_count = billInfoDao.getBillErrorExplanationCount(bean);

        bean.setExplanation_record_count(record_count);

        List<BillErrorExplanationBean> result = billInfoDao.getBillErrorExplanationList(bean);

        return result;
    }

    private void setBean(AjaxFinancialBillBean bean) {

        if ("".equals(bean.getTime_start())) {
            bean.setTime_start(DateTimeUtil.format(DateTimeUtil.getDate(), DateTimeUtil.DEFAULT_DATE_FORMAT));
        }

        if ("".equals(bean.getTime_start())) {
            bean.setTime_start(DateTimeUtil.format(DateTimeUtil.getDate(), DateTimeUtil.DEFAULT_DATE_FORMAT));
        }

        if (!"".equals(bean.getFile_name())) {
            bean.setFile_name(bean.getFile_name().trim());
        }

        if (!"".equals(bean.getInvoice_num())) {
            bean.setInvoice_num(bean.getInvoice_num().trim());
        }
    }

}
