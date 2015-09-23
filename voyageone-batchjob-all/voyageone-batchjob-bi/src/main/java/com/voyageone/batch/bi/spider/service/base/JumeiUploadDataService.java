package com.voyageone.batch.bi.spider.service.base;

import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiDealBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiProductBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiRecordBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiSkuBean;

import java.util.List;

/**
 * Created by Kylin on 2015/6/10.
 */
public interface JumeiUploadDataService {

    int getUploadJumeiProductCount(String task_id);

    List<JumeiProductBean> getUploadJumeiProductList(String task_id);

    int getUploadJumeiSkuCount(String strProductCode, String task_id);

    List<JumeiSkuBean> getUploadJumeiSkuList(String strProductCode, String task_id);

    int getUploadJumeiDealCount(String strProductCode, String task_id);

    List<JumeiDealBean> getUploadJumeiDealList(String strProductCode, String task_id);

    String getUploadJumeiID(String strCode, String strName);

    List<String> getUploadJumeiSku(String strProductCode, String task_id);

    void insertJumeiRecord(JumeiRecordBean jumeiRecord);

}
