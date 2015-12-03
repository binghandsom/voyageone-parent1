package com.voyageone.bi.controller;

import com.voyageone.bi.ajax.bean.AjaxFinancialBillErrorBean;
import com.voyageone.bi.base.AjaxResponseBean;
import com.voyageone.bi.base.BiException;
import com.voyageone.bi.commonutils.Contants;
import com.voyageone.bi.commonutils.SessionKey;
import com.voyageone.bi.task.DataUploadTask;
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
public class DataCheckController {

    private static Log logger = LogFactory.getLog(DataCheckController.class);

    @Autowired
    private DataUploadTask dataUploadTask;

    // 账单上传页面
    @RequestMapping(value = "/manage/goDataCheck")
    public String goDataUpload(HttpServletRequest request,
                               Map<String, Object> map) throws BiException {


        return "manage/financedatacheck/main";
    }

    /**
     * checkSalesHomeParam
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
     * getDataCheckUpload数据取得
     */
    @RequestMapping(value = "/manage/getDataCheckUpload")
    public void getDataCheckUpload(HttpServletResponse response,
                                 HttpServletRequest request,
                                 AjaxFinancialBillErrorBean bean) throws BiException {
        logger.info("getDataCheckData数据取得");
        if (!bean.checkInput()) {
            bean.WriteTo(response);
            return;
        }

        HttpSession session = request.getSession();
        UserInfoBean user = (UserInfoBean) session.getAttribute(SessionKey.LOGIN_INFO);
        dataUploadTask.ajaxGetFinancialErrorBillData(bean, user);
        bean.WriteTo(response);
    }

    /**
     * getDataCheckUpload数据取得
     */
    @RequestMapping(value = "/manage/getDataCheckUploadExplanation")
    public void getDataCheckUploadExplanation(HttpServletResponse response,
                                   HttpServletRequest request,
                                   AjaxFinancialBillErrorBean bean) throws BiException {
        logger.info("getDataCheckUploadExplanation数据取得");
        if (!bean.checkInput()) {
            bean.WriteTo(response);
            return;
        }

        HttpSession session = request.getSession();
        UserInfoBean user = (UserInfoBean) session.getAttribute(SessionKey.LOGIN_INFO);
        dataUploadTask.ajaxGetFinancialErrorExplanationBillData(bean, user);
        bean.WriteTo(response);
    }

    /**
     * getDataCheckUpload数据取得
     */
    @RequestMapping(value = "/manage/getDataCheckUpdate")
    public void getDataCheckUpdate(HttpServletResponse response,
                                   HttpServletRequest request,
                                   AjaxFinancialBillErrorBean bean) throws BiException {
        logger.info("getDataCheckUpdate数据取得");
        if (!bean.checkInput()) {
            bean.WriteTo(response);
            return;
        }

        HttpSession session = request.getSession();
        UserInfoBean user = (UserInfoBean) session.getAttribute(SessionKey.LOGIN_INFO);
        dataUploadTask.ajaxUpdateFinancialBillData(bean, user);
        bean.WriteTo(response);
    }

}
