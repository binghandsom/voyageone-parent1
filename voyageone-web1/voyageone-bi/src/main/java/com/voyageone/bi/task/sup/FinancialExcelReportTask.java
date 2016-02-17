package com.voyageone.bi.task.sup;

import com.voyageone.bi.ajax.bean.AjaxFinancialCostReportBean;
import com.voyageone.bi.ajax.bean.AjaxFinancialMonthlyReportBean;
import com.voyageone.bi.ajax.bean.AjaxFinancialOfficialTaxReportBean;
import com.voyageone.bi.ajax.bean.AjaxFinancialTaxReportBean;
import com.voyageone.bi.bean.OfficialTaxBean;
import com.voyageone.bi.dao.ReportInfoDao;
import com.voyageone.bi.disbean.DetailReportDisBean;
import com.voyageone.bi.disbean.MonthlyReportDisBean;
import com.voyageone.bi.disbean.TaxDetailReportDisBean;
import com.voyageone.bi.tranbean.UserInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialExcelReportTask {

    @Autowired
    private ReportInfoDao reportInfoDao;

    /**
     * getFinancialMonthlyReportInfo
     *
     * @param bean
     * @param userInfoBean
     * @return
     */
    public List<MonthlyReportDisBean> getFinancialMonthlyReportInfo(AjaxFinancialMonthlyReportBean bean, UserInfoBean userInfoBean) {

        int record_count = reportInfoDao.getMonthlyReportCount(bean);

        bean.setReport_record_count(record_count);

        List<MonthlyReportDisBean> result = reportInfoDao.getMonthlyReportList(bean);

        return result;
    }

    /**
     * getFinancialMonthlyReportInfo
     *
     * @param bean
     * @param userInfoBean
     * @return
     */
    public List<DetailReportDisBean> getFinancialCostReportInfo(AjaxFinancialCostReportBean bean, UserInfoBean userInfoBean) {

        int record_count = reportInfoDao.getDetailReportCount(bean);

        bean.setReport_record_count(record_count);

        List<DetailReportDisBean> result = reportInfoDao.getDetailReportList(bean);

        return result;
    }

    /**
     * getFinancialTranspotationBillInfo
     *
     * @param bean
     * @param userInfoBean
     * @return
     */
    public List<TaxDetailReportDisBean> getFinancialTaxReportInfo(AjaxFinancialTaxReportBean bean, UserInfoBean userInfoBean) {

        int record_count = reportInfoDao.getTaxDetailReportCount(bean);

        bean.setReport_record_count(record_count);

        List<TaxDetailReportDisBean> result = reportInfoDao.getTaxDetailReportList(bean);

        return result;
    }

    /**
     * getFinancialTranspotationBillInfo
     *
     * @param bean
     * @param userInfoBean
     * @return
     */
    public List<OfficialTaxBean> getFinancialOfficialTaxReportInfo(AjaxFinancialOfficialTaxReportBean bean, UserInfoBean userInfoBean) {

        int record_count = reportInfoDao.getOfficialTaxReportCount(bean);

        bean.setReport_record_count(record_count);

        List<OfficialTaxBean> result = reportInfoDao.getOfficialTaxReportList(bean);

        return result;
    }

}
