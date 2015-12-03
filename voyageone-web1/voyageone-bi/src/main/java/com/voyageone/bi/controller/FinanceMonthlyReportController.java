package com.voyageone.bi.controller;

import com.voyageone.bi.ajax.bean.AjaxFinancialMonthlyReportBean;
import com.voyageone.bi.base.AjaxResponseBean;
import com.voyageone.bi.base.BiException;
import com.voyageone.bi.commonutils.Contants;
import com.voyageone.bi.commonutils.SessionKey;
import com.voyageone.bi.task.DataReportTask;
import com.voyageone.bi.tranbean.UserInfoBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class FinanceMonthlyReportController {

    private static Log logger = LogFactory.getLog(FinanceMonthlyReportController.class);

    // 页面初期化
    @Autowired
    private DataReportTask dataReportTask;

    // 月度报表页面
    @RequestMapping(value = "/manage/goMonthlyReport")
    public String goMonthlyReport(HttpServletRequest request,
                                  Map<String, Object> map) throws BiException {


        return "manage/financereportmonthly/main";
    }

    /**
     * checkSalesHomeParam
     *
     * @param response
     * @param request
     * @param bean
     * @throws BiException
     */
    @RequestMapping(value = "/manage/checkFinanceMonthlyReportDataParam")
    public void doCheckSalesHomeParam(HttpServletResponse response,
                                      HttpServletRequest request,
                                      AjaxFinancialMonthlyReportBean bean) throws BiException {
        if (!bean.checkInput()) {
            bean.WriteTo(response);
        } else {
            AjaxResponseBean result = bean.getResponseBean();
            result.setReqResult(Contants.AJAX_RESULT_OK);
            bean.WriteTo(response);
        }
        return;
    }

    /**
     * getDataReportMonthly数据取得
     */
    @RequestMapping(value = "/manage/getDataReportMonthly")
    public void getDataReportMonthly(HttpServletResponse response,
                                     HttpServletRequest request,
                                     AjaxFinancialMonthlyReportBean bean) throws BiException {
        logger.info("getDataReportMonthly");
        if (!bean.checkInput()) {
            bean.WriteTo(response);
            return;
        }

        HttpSession session = request.getSession();
        UserInfoBean user = (UserInfoBean) session.getAttribute(SessionKey.LOGIN_INFO);
        dataReportTask.ajaxGetFinancialMonthlyReportData(bean, user);
        bean.WriteTo(response);
    }

}