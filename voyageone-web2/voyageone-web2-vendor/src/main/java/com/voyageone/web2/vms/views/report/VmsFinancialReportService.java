package com.voyageone.web2.vms.views.report;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.common.configs.Types;
import com.voyageone.common.util.BeanUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.com.UserConfigBean;
import com.voyageone.service.bean.vms.report.FinancialReportBean;
import com.voyageone.service.impl.vms.report.FinancialReportService;
import com.voyageone.service.model.vms.VmsBtFinancialReportModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;


/**
 * VmsFeedImportResultService
 * Created on 2016/7/11.
 * @author jeff.duan
 * @version 1.0
 */
@Service
public class VmsFinancialReportService extends BaseAppService {

    @Autowired
    private FinancialReportService financialReportService;

    /**
     * 取得检索条件信息
     *
     * @return 检索条件信息
     */
    public Map<String, Object> init () {

        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> reportYearMonthList = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            // 取得系统日期 - i个月
            String date = DateTimeUtil.format(DateTimeUtil.addMonths(DateTimeUtil.getDate(), -i), "yyyy-MM");
            reportYearMonthList.add(new HashMap<String, Object>() {{ put("name", date);put("value", date.substring(0,4) + date.substring(5,7));}});
        }

        result.put("reportYearMonthList", reportYearMonthList);
        return result;
    }

    /**
     * 检索
     *
     * @param param 客户端参数
     * @param user UserSessionBean
     * @return 检索结果
     */
    public Map<String, Object> search(Map<String, Object> param, UserSessionBean user, String lang) {

        Map<String, Object> result = new HashMap<>();

        // 是否有权限承认财务报表
        boolean canConfirmReport = false;

        List<UserConfigBean>  confirmReportConfigList = user.getUserConfig().get("vms_confirm_report");
        if (confirmReportConfigList.size() > 0 ) {
            if (confirmReportConfigList.get(0).getCfg_val1().equals("1")) {
                canConfirmReport = true;
            }
        }

        Map<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("channelId", user.getSelChannelId());
        if (!StringUtils.isEmpty((String) param.get("reportYearMonth"))) {
            sqlParam.put("reportYearMonth", (String) param.get("reportYearMonth"));
        }

        Map<String, Object> newMap = MySqlPageHelper.build(sqlParam).addSort("report_year_month", Order.Direction.DESC).toMap();

        // 根据条件取得检索结果
        List<VmsBtFinancialReportModel> financialReportList = financialReportService.getFinancialReportList(newMap);

        result.put("financialReportList",  editFinancialReportList(financialReportList, lang));
        result.put("canConfirmReport",  canConfirmReport);

        return result;

    }

    /**
     * 检索结果编辑
     *
     * @param financialReportModels 检索结果
     * @param lang 语言
     */
    private List<FinancialReportBean> editFinancialReportList(List<VmsBtFinancialReportModel> financialReportModels, String lang) {
        List<FinancialReportBean>  financialReportList = new ArrayList<>();

        for (VmsBtFinancialReportModel financialReport : financialReportModels) {

            FinancialReportBean bean = new FinancialReportBean();
            BeanUtil.copy(financialReport, bean);

            // Status
            bean.setStatusName(Types.getTypeName(86, lang,  (String)financialReport.getStatus()));
            financialReportList.add(bean);
        }

        return financialReportList;
    }
}