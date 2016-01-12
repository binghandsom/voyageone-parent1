package com.voyageone.batch.bi.spider.service.base.impl;

import com.voyageone.batch.bi.bean.formbean.FormUser;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiDealBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiProductBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiRecordBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiSkuBean;
import com.voyageone.batch.bi.mapper.JumeiMapper;
import com.voyageone.batch.bi.spider.service.base.JumeiUploadDataService;
import com.voyageone.batch.bi.util.UtilCheckData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kylin on 2015/6/10.
 */
@Service
public class JumeiUploadDataServiceImpl implements JumeiUploadDataService {

    @Autowired
    private JumeiMapper jumeiMapper;

    /**
     * 根据单批数量限制，获得商品Iid一览
     *
     * @param task_id
     * @return
     */
    @Transactional("transactionManagerSub")
    @Override
    public int getUploadJumeiProductCount(String task_id) {
        FormUser threadUser = UtilCheckData.getLocalUser();

        Map<String, Object> mapCondition = new HashMap<>();
        mapCondition.put("channel_id", threadUser.getChannel_code());
        mapCondition.put("task_id", task_id);
        mapCondition.put("status", "0");

        return jumeiMapper.ims_jumei_vl_product_count(mapCondition);
    }

    @Override
    public List<JumeiProductBean> getUploadJumeiProductList(String task_id) {
        FormUser threadUser = UtilCheckData.getLocalUser();

        Map<String, Object> mapCondition = new HashMap<>();
        mapCondition.put("channel_id", threadUser.getChannel_code());
        mapCondition.put("task_id", task_id);
        mapCondition.put("status", "0");

        return jumeiMapper.select_list_ims_jumei_vl_product(mapCondition);
    }

    @Override
    public int getUploadJumeiSkuCount(String strProductCode, String task_id) {
        FormUser threadUser = UtilCheckData.getLocalUser();

        Map<String, Object> mapCondition = new HashMap<>();
        mapCondition.put("channel_id", threadUser.getChannel_code());
        mapCondition.put("task_id", task_id);
        mapCondition.put("product_code", strProductCode);
        mapCondition.put("status", "0");

        return jumeiMapper.ims_jumei_vl_sku_count(mapCondition);
    }

    @Override
    public List<JumeiSkuBean> getUploadJumeiSkuList(String strProductCode, String task_id) {
        FormUser threadUser = UtilCheckData.getLocalUser();

        Map<String, Object> mapCondition = new HashMap<>();
        mapCondition.put("channel_id", threadUser.getChannel_code());
        mapCondition.put("task_id", task_id);
        mapCondition.put("product_code", strProductCode);
        mapCondition.put("status", "0");

        return jumeiMapper.select_list_ims_jumei_vl_sku(mapCondition);
    }

    @Override
    public int getUploadJumeiDealCount(String strProductCode, String task_id) {
        FormUser threadUser = UtilCheckData.getLocalUser();

        Map<String, Object> mapCondition = new HashMap<>();
        mapCondition.put("channel_id", threadUser.getChannel_code());
        mapCondition.put("task_id", task_id);
        mapCondition.put("product_code", strProductCode);
        mapCondition.put("status", "0");

        return jumeiMapper.ims_jumei_vl_deal_count(mapCondition);
    }

    @Override
    public List<JumeiDealBean> getUploadJumeiDealList(String strProductCode, String task_id) {
        FormUser threadUser = UtilCheckData.getLocalUser();

        Map<String, Object> mapCondition = new HashMap<>();
        mapCondition.put("channel_id", threadUser.getChannel_code());
        mapCondition.put("task_id", task_id);
        mapCondition.put("product_code", strProductCode);
        mapCondition.put("status", "0");

        return jumeiMapper.select_list_ims_jumei_vl_deal(mapCondition);
    }

    @Override
    public String getUploadJumeiID(String strCode, String strName) {
        Map<String, String> mapCondition = new HashMap<>();
        mapCondition.put("code", strCode);
        mapCondition.put("name", strName);
        return jumeiMapper.select_jumeiid_ims_jumei_product_vl_added(mapCondition);
    }

    @Override
    public List<String> getUploadJumeiSku(String strProductCode, String task_id) {
        FormUser threadUser = UtilCheckData.getLocalUser();

        Map<String, Object> mapCondition = new HashMap<>();
        mapCondition.put("channel_id", threadUser.getChannel_code());
        mapCondition.put("task_id", task_id);
        mapCondition.put("product_code", strProductCode);
        mapCondition.put("status", "0");

        return jumeiMapper.select_sku_ims_jumei_vl_sku(mapCondition);
    }

    @Override
    public void insertJumeiRecord(JumeiRecordBean jumeiRecord) {
        jumeiMapper.insert_ims_jumei_vl_record(jumeiRecord);
    }

    public void synchronizeJumeiProductRecord(){
        jumeiMapper.update_ims_jumei_vl_product_status();
        jumeiMapper.update_ims_jumei_vl_product_status_0();
    }

    public void synchronizeJumeiDealRecord(){
        jumeiMapper.update_ims_jumei_vl_deal_status();
        jumeiMapper.update_ims_jumei_vl_deal_status_0();
    }

    public void updateJumeiRecord(){
        jumeiMapper.update_ims_jumei_vl_record();
    }
}
