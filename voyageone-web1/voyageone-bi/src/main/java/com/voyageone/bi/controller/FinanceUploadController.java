package com.voyageone.bi.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.voyageone.bi.ajax.bean.AjaxFinancialBillBean;
import com.voyageone.bi.base.AjaxResponseBean;
import com.voyageone.bi.base.BiException;
import com.voyageone.bi.commonutils.Contants;
import com.voyageone.bi.commonutils.SessionKey;
import com.voyageone.bi.task.FinanceUploadTask;
import com.voyageone.bi.tranbean.UserInfoBean;


@Controller
public class FinanceUploadController {

    private static Log logger = LogFactory.getLog(FinanceUploadController.class);

    @Autowired
    private FinanceUploadTask financeUploadTask;

    // 账单上传页面
    @RequestMapping(value = "/manage/goFinanceUpload")
    public String goFinanceUpload(HttpServletRequest request,
                               Map<String, Object> map) throws BiException {


        return "manage/dataupload/main";
    }

    /**
     * checkSalesHomeParam
     *
     * @param response
     * @param request
     * @param bean
     * @throws BiException
     */
    @RequestMapping(value = "/manage/checkFinanceUploadDataParam")
    public void checkFinanceUploadDataParam(HttpServletResponse response,
                                      HttpServletRequest request,
                                      AjaxFinancialBillBean bean) throws BiException {
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
     * getDataUploadTaxData数据取得
     */
    @RequestMapping(value = "/manage/getDataUploadTaxData")
    public void getDataUploadTaxData(HttpServletResponse response,
                                     HttpServletRequest request,
                                     AjaxFinancialBillBean bean) throws BiException {
        logger.info("getDataUploadTaxData数据取得");
        if (!bean.checkInput()) {
            bean.WriteTo(response);
            return;
        }

        HttpSession session = request.getSession();
        UserInfoBean user = (UserInfoBean) session.getAttribute(SessionKey.LOGIN_INFO);
        financeUploadTask.ajaxGetFinancialTaxBillData(bean, user);
        bean.WriteTo(response);
    }


    /**
     * getDataUploadTranspotationData数据取得
     */
    @RequestMapping(value = "/manage/getDataUploadTranspotationData")
    public void getDataUploadTranspotationData(HttpServletResponse response,
                                               HttpServletRequest request,
                                               AjaxFinancialBillBean bean) throws BiException {
        logger.info("getDataUploadTranspotationData数据取得");
        if (!bean.checkInput()) {
            bean.WriteTo(response);
            return;
        }

        HttpSession session = request.getSession();
        UserInfoBean user = (UserInfoBean) session.getAttribute(SessionKey.LOGIN_INFO);
        financeUploadTask.ajaxGetFinancialTranspotationBillData(bean, user);
        bean.WriteTo(response);
    }

}
