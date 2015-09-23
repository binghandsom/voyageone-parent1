package com.voyageone.batch.bi.mapper;

import com.voyageone.batch.bi.bean.modelbean.SalesProductBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Kylin on 2015/6/15.
 */
@Repository
public interface ViewProductMapper {

    int select_count_vs_view_sales_product_lift_cycle(Map<String, Object> mapCondition);

    SalesProductBean select_record_vs_view_sales_product_lift_cycle(Map<String, String> mapCondition);

    List<SalesProductBean> select_list_vs_view_sales_product_lift_cycle(Map<String, Object> mapCondition);

    List<String> select_list_iid_vs_view_sales_product_lift_cycle(Map<String, Object> mapCondition);

    int duplicate_vs_view_sales_product_lift_cycle_tm(Map<String, Object> mapColumnValue);

    int duplicate_vs_view_sales_product_lift_cycle_jd_on(Map<String, Object> mapColumnValue);

    int duplicate_vs_view_sales_product_lift_cycle_jd_de(Map<String, Object> mapColumnValue);

    int replace_vs_view_sales_product_lift_cycle(Map<String, Object> mapColumnValue);

    int update_vs_view_sales_product_lift_cycle(Map<String, String> mapValues);

    int insert_vs_view_sales_product_lift_cycle(Map<String, Object> mapColumnValue);

}
