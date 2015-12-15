package com.voyageone.bi.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.bi.bean.BillErrorExplanationBean;

/**
 * Created by Kylin on 2015/6/10.
 * vs_sales_product_iid
 */
@Repository
public interface BillErrorExplanationMapper {

    int select_count_vf_bill_error_explanation(Map<String, Object> mapCondition);

    List<BillErrorExplanationBean> select_list_vf_bill_error_explanation(Map<String, Object> mapParam);
}
