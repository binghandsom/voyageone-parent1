package com.voyageone.batch.bi.mapper;

import com.voyageone.batch.bi.bean.modelbean.ProductIIDBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Kylin on 2015/6/10.
 * vs_sales_product_iid
 */
@Repository
public interface ProductIidMapper {

    int select_count_vs_sales_product_iid(Map<String, Object> mapCondition);

    ProductIIDBean select_record_vs_sales_product_iid(Map<String, String> mapCondition);

    List<String> select_list_iid_vs_sales_product_iid(Map<String, Object> mapParam);

    String select_iid_vs_sales_product_iid(Map<String, Object> mapParam);

    String select_record_product_code_vs_sales_product_iid(Map<String, Object> mapCondition);

    List<ProductIIDBean> select_list_vs_sales_product_iid(Map<String, Object> mapParam);

    int replace_vs_sales_product_iid(Map<String, Object> mapColumnValue);

    int update_vs_sales_product_iid(Map<String, String> mapValues);

    int insert_vs_sales_product_iid(Map<String, Object> mapColumnValue);
}
