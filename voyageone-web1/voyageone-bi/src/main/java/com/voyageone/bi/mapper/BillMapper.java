package com.voyageone.bi.mapper;

import com.voyageone.bi.bean.BillBean;
import com.voyageone.bi.bean.BillErrorBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Kylin on 2015/6/10.
 * vs_sales_product_iid
 */
@Repository
public interface BillMapper {

    int select_count_vf_bill(Map<String, Object> mapCondition);

    BillBean select_record_vf_bill(Map<String, String> mapCondition);

    List<BillBean> select_list_vf_bill(Map<String, Object> mapParam);

    int insert_vf_bill(Map<String, String> mapValues);

    int delete_record_vf_bill(Map<String, Object> mapColumnValue);

    List<BillErrorBean> select_list_viw_fms_error(Map<String, Object> mapParam);

    int select_count_viw_fms_error(Map<String, Object> mapValues);
}
