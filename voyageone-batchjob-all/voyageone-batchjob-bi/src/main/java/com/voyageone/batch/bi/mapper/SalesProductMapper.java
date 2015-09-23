package com.voyageone.batch.bi.mapper;

import com.voyageone.batch.bi.bean.modelbean.SalesProductBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Kylin on 2015/6/15.
 */
@Repository
public interface SalesProductMapper {

    int select_count_vt_sales_product(Map<String, String> mapCondition);

    SalesProductBean select_record_vt_sales_product(Map<String, String> mapCondition);

    List<SalesProductBean> select_list_vt_sales_product(Map<String, String> mapCondition);

    int duplicate_vt_sales_product(Map<String, Object> mapColumnValue);

    int duplicate_vt_sales_product_total(Map<String, Object> mapColumnValue);

    int duplicate_vt_sales_product_pc(Map<String, Object> mapColumnValue);

    int duplicate_vt_sales_product_mobile(Map<String, Object> mapColumnValue);

    int replace_vt_sales_product(Map<String, Object> mapColumnValue);

    int update_vt_sales_product(Map<String, String> mapValues);

    int insert_vt_sales_product(Map<String, Object> mapColumnValue);

}
