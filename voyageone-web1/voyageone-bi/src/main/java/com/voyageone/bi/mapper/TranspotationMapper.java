package com.voyageone.bi.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.bi.bean.TranspotationBean;

/**
 * Created by Kylin on 2015/6/10.
 * vs_sales_product_iid
 */
@Repository
public interface TranspotationMapper {
    int select_count_fms_cost_transpotation(Map<String, Object> mapCondition);

    TranspotationBean select_record_fms_cost_transpotation(Map<String, String> mapCondition);

    List<TranspotationBean> select_list_fms_cost_transpotation(Map<String, Object> mapParam);

    int insert_fms_cost_transpotation(Map<String, String> mapValues);

    int insert_list_fms_cost_transpotation(Map<String, Object> mapColumnValue);

    int delete_fms_cost_transpotation(Map<String, Object> mapColumnValue);

    int update_fms_cost_transpotation(Map<String, String> mapValue);
}
