package com.voyageone.bi.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.bi.disbean.TaxDetailReportDisBean;

/**
 * Created by Kylin on 2015/6/10.
 * vs_sales_product_iid
 */
@Repository
public interface TaxDetailReportMapper {
    int select_count_tax_detail_report(Map<String, Object> mapCondition);

    List<TaxDetailReportDisBean> select_list_tax_detail_report(Map<String, Object> mapParam);
}
