package com.voyageone.bi.mapper;

import com.voyageone.bi.disbean.MonthlyReportDisBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Kylin on 2015/6/10.
 * vs_sales_product_iid
 */
@Repository
public interface MonthlyReportMapper {
    int select_count_monthly_report(Map<String, Object> mapCondition);

    List<MonthlyReportDisBean> select_list_monthly_report(Map<String, Object> mapParam);
}
