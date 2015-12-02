package com.voyageone.bi.mapper;

import com.voyageone.bi.bean.TaxDetailBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Kylin on 2015/6/10.
 * vs_sales_product_iid
 */
@Repository
public interface TaxDetailMapper {
    int select_count_fms_cost_tax_detail(Map<String, Object> mapCondition);

    TaxDetailBean select_record_fms_cost_tax_detail(Map<String, String> mapCondition);

    List<TaxDetailBean> select_list_fms_cost_tax_detail(Map<String, Object> mapParam);

    int insert_fms_cost_tax_detail(Map<String, String> mapValues);

    int insert_list_fms_cost_tax_detail(Map<String, Object> mapColumnValue);

    int delete_fms_cost_tax_detail(Map<String, Object> mapColumnValue);

    int update_fms_cost_tax_detail(Map<String, String> mapValue);
}
