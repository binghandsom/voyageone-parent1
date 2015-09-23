package com.voyageone.batch.bi.spider.service.base;

import java.util.List;
import java.util.Map;

import com.voyageone.batch.bi.bean.modelbean.ShopChannelEcommBean;

/**
 * Created by Kylin on 2015/6/10.
 */
public interface CommonSettingService {

    List<String> getProductColumnList(String strTable, List<String> listViewColumn);

    List<String> getShopColumnList(String strTable, List<String> listViewColumn);

    List<Map<String, String>> getColumnList(String strTable, String strWebType);

    List<String> getProductIidList(Integer init_index, Integer intSize);

    String getProductIid();

    String getProductCode(String strProductIid);

    int getCountProductIid();

    String getEcommCode(int iEcommID);

    String getChannelCode(int iChannelID);

    List<ShopChannelEcommBean> initShopConfigBeanList();

    int getCountViewProductIid();

    List<String> getViewProductIidList(Integer init_index, Integer intSize);

}
