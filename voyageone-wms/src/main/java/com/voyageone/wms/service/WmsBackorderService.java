package com.voyageone.wms.service;

import com.voyageone.core.modelbean.UserSessionBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Simple to Introduction
 * @Package      [com.voyageone.wms.service]
 * @ClassName    [WmsReturnService]
 * @Description  [Backorder服务类接口]
 * @Author       [sky]
 * @CreateDate   [20150528]
 * @UpdateUser   [${user}]
 * @UpdateDate   [${date} ${time}]
 * @UpdateRemark [说明本次修改内容]
 * @Version      [v1.0]
 */
public interface WmsBackorderService {
    /**
     * @description 【backorderList页面】获取backorderList
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @param object  FormBackorder对象
     * @param user 用户
     */
    void getBackorderInfoList(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user);

    /**
     * @description 【backorderList页面】插入backorderInfo
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @param object  FormBackorder对象
     * @param user 用户
     */
    void addBackorderInfo(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user);

    /**
     * @description 【backorderList页面】删除backorderInfo
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @param object  FormBackorder对象
     * @param user 用户信息
     */
    void delBackorderInfo(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user);

    void popInit(HttpServletRequest request, HttpServletResponse response, UserSessionBean user);

    void listInit(HttpServletRequest request, HttpServletResponse response, UserSessionBean user);
}
