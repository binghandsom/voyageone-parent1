package com.voyageone.wms.service;

import com.voyageone.core.modelbean.UserSessionBean;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Simple to Introduction
 * @Package      [com.voyageone.wms.service]
 * @ClassName    [WmsReportService]
 * @Description  [Report服务类接口]
 * @Author       [sky]
 * @CreateDate   [20150720]
 * @UpdateUser   [${user}]
 * @UpdateDate   [${date} ${time}]
 * @UpdateRemark [说明本次修改内容]
 * @Version      [v1.0]
 */
@Service
public interface WmsReportService {
    void init(HttpServletRequest request, HttpServletResponse response, UserSessionBean user);

    byte[] downloadInvDelReport(String param, UserSessionBean user);

}
