package com.voyageone.web2.vms.views.report;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.common.configs.Types;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.vms.feed.FeedFileService;
import com.voyageone.service.impl.vms.report.FinancialReportService;
import com.voyageone.service.model.vms.VmsBtFinancialReportModel;
import com.voyageone.web2.base.BaseAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        List<Map<String, Object>> yearMonthList = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            // 取得系统日期 - i个月
            String date = DateTimeUtil.format(DateTimeUtil.addMonths(DateTimeUtil.getDate(), -i), "yyyy-MM");
            yearMonthList.add(new HashMap<String, Object>() {{ put("name", date);put("value", date.substring(0,4) + date.substring(5,7));}});
        }

        result.put("yearMonthList", yearMonthList);
        return result;
    }

    /**
     * 检索
     *
     * @param param 客户端参数
     * @param channelId 渠道id
     * @return 检索结果
     */
    public Map<String, Object> search(Map<String, Object> param, String channelId) {

        Map<String, Object> result = new HashMap<>();

        Map<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("channelId", channelId);
        if (!StringUtils.isEmpty((String) param.get("reportYearMonth"))) {
            sqlParam.put("reportYearMonth", (String) param.get("reportYearMonth"));
        }

        Map<String, Object> newMap = MySqlPageHelper.build(sqlParam).addSort("report_year_month", Order.Direction.DESC).toMap();

        // 根据条件取得检索结果
        List<VmsBtFinancialReportModel> financialReportList = financialReportService.getFinancialReportList(newMap);

        result.put("financialReportList",  financialReportList);

        return result;

    }

    /**
     * 检索结果编辑
     *
     * @param feedImportResultList 检索结果
     * @param lang 语言
     */
    private void editFeedImportResultList(List<Map<String, Object>> feedImportResultList, String lang) {

        for (Map<String, Object> feedImportResult : feedImportResultList) {
            // Status
            feedImportResult.put("statusName", Types.getTypeName(83, lang,  (String)feedImportResult.get("status")));
        }
    }
}