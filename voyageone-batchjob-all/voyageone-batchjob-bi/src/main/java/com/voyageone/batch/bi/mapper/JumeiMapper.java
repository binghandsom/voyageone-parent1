package com.voyageone.batch.bi.mapper;

import com.voyageone.batch.bi.bean.modelbean.jumei.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Kylin on 2015/7/15.
 */
@Repository
public interface JumeiMapper {

    List<JumeiProductBean> select_list_ims_jumei_vl_product(Map<String, Object> mapParameter);

    List<JumeiSkuBean> select_list_ims_jumei_vl_sku(Map<String, Object> mapParameter);

    List<JumeiDealBean> select_list_ims_jumei_vl_deal(Map<String, Object> mapParameter);

    int ims_jumei_vl_product_count(Map<String, Object> mapParameter);

    int ims_jumei_vl_sku_count(Map<String, Object> mapParameter);

    int ims_jumei_vl_deal_count(Map<String, Object> mapParameter);

    void insert_ims_jumei_product_added(Map<String, List<JumeiProductAddedBean>> mapParameter);

    String select_jumeiid_ims_jumei_product_vl_added(Map<String, String> mapParameter);

    List<String> select_sku_ims_jumei_vl_sku(Map<String, Object> mapParameter);

    void insert_ims_jumei_vl_record(JumeiRecordBean jumeiRecord);
}
