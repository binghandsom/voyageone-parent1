package com.voyageone.bi.controller;

import com.voyageone.bi.ajax.bean.AjaxFinancialBillErrorBean;
import com.voyageone.bi.base.AjaxResponseBean;
import com.voyageone.bi.base.BiException;
import com.voyageone.bi.commonutils.Contants;
import com.voyageone.bi.commonutils.SessionKey;
import com.voyageone.bi.task.FinanceUploadTask;
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
public class FinanceCheckController {

    private static Log logger = LogFactory.getLog(FinanceCheckController.class);

    @Autowired
    private FinanceUploadTask financeUploadTask;

    // 账单上传页面
    @RequestMapping(value = "/manage/goFinanceCheck")
    public String goFinanceCheck(HttpServletRequest request,
                               Map<String, Object> map) throws BiException {


        return "manage/financedatacheck/main";
    }

    /**
     * checkFinanceErrorDataParam
     *
     * @param response
     * @param request
     * @param bean
     * @throws BiException
     */
    @RequestMapping(value = "/manage/checkFinanceErrorDataParam")
    public void checkFinanceErrorDataParam(HttpServletResponse response,
                                      HttpServletRequest request,
                                      AjaxFinancialBillErrorBean bean) throws BiException {
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
     * getFinanceCheckUpload数据取得
     */
    @RequestMapping(value = "/manage/getFinanceCheckUpload")
    public void getFinanceCheckUpload(HttpServletResponse response,
                                 HttpServletRequest request,
                                 AjaxFinancialBillErrorBean bean) throws BiException {
        logger.info("getFinanceCheckUpload数据取得");
        if (!bean.checkInput()) {
            bean.WriteTo(response);
            return;
        }

        HttpSession session = request.getSession();
        UserInfoBean user = (UserInfoBean) session.getAttribute(SessionKey.LOGIN_INFO);
        financeUploadTask.ajaxGetFinancialErrorBillData(bean, user);
        bean.WriteTo(response);
    }

    /**
     * getFinanceCheckUpdateExplanation数据取得
     */
    @RequestMapping(value = "/manage/getFinanceCheckUpdateExplanation")
    public void getFinanceCheckUpdateExplanation(HttpServletResponse response,
                                   HttpServletRequest request,
                                   AjaxFinancialBillErrorBean bean) throws BiException {
        logger.info("getFinanceCheckUpdateExplanation数据取得");
        if (!bean.checkInput()) {
            bean.WriteTo(response);
            return;
        }

        HttpSession session = request.getSession();
        UserInfoBean user = (UserInfoBean) session.getAttribute(SessionKey.LOGIN_INFO);
        financeUploadTask.ajaxGetFinancialErrorExplanationBillData(bean, user);
        bean.WriteTo(response);
    }

    /**
     * getFinanceCheckUpdate数据取得
     */
    @RequestMapping(value = "/manage/getFinanceCheckUpdate")
    public void getFinanceCheckUpdate(HttpServletResponse response,
                                   HttpServletRequest request,
                                   AjaxFinancialBillErrorBean bean) throws BiException {
        logger.info("getFinanceCheckUpdate数据取得");
        if (!bean.checkInput()) {
            bean.WriteTo(response);
            return;
        }

        HttpSession session = request.getSession();
        UserInfoBean user = (UserInfoBean) session.getAttribute(SessionKey.LOGIN_INFO);
        financeUploadTask.ajaxUpdateFinancialBillData(bean, user);
        bean.WriteTo(response);
    }

}
