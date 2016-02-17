package com.voyageone.bi.mapper;

import com.voyageone.bi.bean.OfficialTaxBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Kylin on 2015/6/10.
 * vs_sales_product_iid
 */
@Repository
public interface OfficialTaxMapper {
    int select_count_official_tax_detail_report(Map<String, Object> mapCondition);

    List<OfficialTaxBean> select_list_official_tax_detail_report(Map<String, Object> mapParam);
}
