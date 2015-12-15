package com.voyageone.bi.mapper;

import com.voyageone.bi.disbean.DetailReportDisBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Kylin on 2015/6/10.
 * vs_sales_product_iid
 */
@Repository
public interface DetailReportMapper {

    int select_count_detail_report(Map<String, Object> mapCondition);

    List<DetailReportDisBean> select_list_detail_report(Map<String, Object> mapParam);

}
