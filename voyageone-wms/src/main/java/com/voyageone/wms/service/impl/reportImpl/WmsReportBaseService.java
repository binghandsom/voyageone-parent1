package com.voyageone.wms.service.impl.reportImpl;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.PermissionBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.wms.dao.ReportDao;
import com.voyageone.wms.formbean.FormReportBean;
import com.voyageone.wms.service.WmsReportService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**   
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.service.impl]  
 * @ClassName    [WmsReportServiceImpl]
 * @Description  [report服务类接口实现类]
 * @Author       [sky]   
 * @CreateDate   [20150720]
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]  
 * @Version      [v1.0] 
 */
@Service
public abstract class WmsReportBaseService {
	protected final static Log logger = LogFactory.getLog(WmsReportBaseService.class);
}
