package com.voyageone.bi.controller;

import com.voyageone.bi.ajax.bean.AjaxFinancialTaxReportBean;
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
public class FinanceTaxReportController {

    private static Log logger = LogFactory.getLog(FinanceTaxReportController.class);

    // 页面初期化
    @Autowired
    private DataReportTask dataReportTask;

    // 月度报表页面
    @RequestMapping(value = "/manage/goTaxReport")
    public String goTaxReport(HttpServletRequest request,
                                  Map<String, Object> map) throws BiException {


        return "manage/financereporttax/main";
    }
    /**
     * checkSalesHomeParam
     *
     * @param response
     * @param request
     * @param bean
     * @throws BiException
     */
    @RequestMapping(value = "/manage/checkFinanceTaxReportDataParam")
    public void doCheckSalesHomeParam(HttpServletResponse response,
                                      HttpServletRequest request,
                                      AjaxFinancialTaxReportBean bean) throws BiException {
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
     * getDataReportTax数据取得
     */
    @RequestMapping(value = "/manage/getDataReportTax")
    public void getDataReportTax(HttpServletResponse response,
                                 HttpServletRequest request,
                                 AjaxFinancialTaxReportBean bean) throws BiException {
        logger.info("getDataReportTax");
        if (!bean.checkInput()) {
            bean.WriteTo(response);
            return;
        }

        HttpSession session = request.getSession();
        UserInfoBean user = (UserInfoBean) session.getAttribute(SessionKey.LOGIN_INFO);
        dataReportTask.ajaxGetFinancialTaxReportData(bean, user);
        bean.WriteTo(response);
    }

}
