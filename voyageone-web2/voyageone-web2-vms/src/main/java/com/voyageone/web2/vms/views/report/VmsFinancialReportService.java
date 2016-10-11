package com.voyageone.web2.vms.views.report;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.common.configs.Types;
import com.voyageone.common.util.BeanUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.com.UserConfigBean;
import com.voyageone.service.bean.vms.report.FinancialReportBean;
import com.voyageone.service.impl.vms.report.FinancialReportService;
import com.voyageone.service.model.vms.VmsBtFinancialReportModel;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.vms.VmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * VmsFeedImportResultService
 * Created on 2016/7/11.
 * @author jeff.duan
 * @version 1.0
 */
@Service
public class VmsFinancialReportService extends BaseViewService {

    @Autowired
    private FinancialReportService financialReportService;

    /**
     * 取得检索条件信息     *
     *
     * @param user UserSessionBean
     * @return 检索条件信息
     */
    public Map<String, Object> init (UserSessionBean user) {

        Map<String, Object> result = new HashMap<>();

        // 是否有权限承认财务报表
        boolean canConfirmReport = false;

        List<UserConfigBean>  confirmReportConfigList = user.getUserConfig().get("vms_confirm_report");
        if (confirmReportConfigList != null && confirmReportConfigList.size() > 0 ) {
            if (confirmReportConfigList.get(0).getCfg_val1().equals("1")) {
                canConfirmReport = true;
            }
        }

        List<Map<String, Object>> reportYearMonthList = new ArrayList<>();


        Map<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("channelId", user.getSelChannelId());
        // 客户登录只能看到承认过的财务报表
        if (!canConfirmReport) {
            sqlParam.put("status", VmsConstants.FinancialReportStatus.CONFIRMED);
        }
        Map<String, Object> newMap = MySqlPageHelper.build(sqlParam).addSort("report_year_month", Order.Direction.DESC).toMap();

        // 根据条件取得检索结果
        List<VmsBtFinancialReportModel> financialReportList = financialReportService.getFinancialReportList(newMap);

        for (int i = 0; i <= 11; i++) {
            // 取得前12个年月
            if (i < financialReportList.size()) {
                String yearMonth = financialReportList.get(i).getReportYearMonth();
                reportYearMonthList.add(new HashMap<String, Object>() {{
                    put("name", yearMonth.substring(0, 4) + "-" + yearMonth.substring(4, 6));
                    put("value", yearMonth.substring(0, 4) + yearMonth.substring(4, 6));
                }});
            }
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
        if (confirmReportConfigList != null && confirmReportConfigList.size() > 0 ) {
            if (confirmReportConfigList.get(0).getCfg_val1().equals("1")) {
                canConfirmReport = true;
            }
        }

        Map<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("channelId", user.getSelChannelId());
        if (!StringUtils.isEmpty((String) param.get("reportYearMonth"))) {
            sqlParam.put("reportYearMonth", (String) param.get("reportYearMonth"));
        }
        // 客户登录只能看到承认过的财务报表
        if (!canConfirmReport) {
            sqlParam.put("status", VmsConstants.FinancialReportStatus.CONFIRMED);
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
            BeanUtils.copy(financialReport, bean);

            // Status
            bean.setStatusName(Types.getTypeName(86, lang,  (String)financialReport.getStatus()));
            financialReportList.add(bean);
        }

        return financialReportList;
    }

    /**
     * 检索
     *
     * @param param 客户端参数
     * @param user UserSessionBean
     * @return 检索结果
     */
    public void confirm(Map<String, Object> param, UserSessionBean user) {
        financialReportService.updateFinancialReportStatus(user.getSelChannelId(), (Integer) param.get("id"),
                VmsConstants.FinancialReportStatus.CONFIRMED, user.getUserName());
    }
}