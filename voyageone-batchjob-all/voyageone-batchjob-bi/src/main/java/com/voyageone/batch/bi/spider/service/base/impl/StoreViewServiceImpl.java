package com.voyageone.batch.bi.spider.service.base.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voyageone.batch.bi.bean.formbean.FormUser;
import com.voyageone.batch.bi.mapper.SalesShopMapper;
import com.voyageone.batch.bi.spider.service.base.StoreViewService;
import com.voyageone.batch.bi.util.UtilCheckData;

/**
 * Created by Kylin on 2015/6/5.
 */
@Service
public class StoreViewServiceImpl implements StoreViewService {

    @Autowired
    SalesShopMapper salesShopMapper;

    public void selectStoreInfo() {

    }

    public void updateStoreInfo(List<String> listColumns, List<String> listValues) {

    }

    public void insertStoreInfo(List<String> listColumns, List<String> listValues) {

    }

    public void replaceStoreInfo(List<String> listColumns, List<String> listValues) {

    }

    @Transactional("transactionManagerMain")
    public int duplicateStoreInfo(List<String> listColumns, List<String> listValues) {

        if (listValues.size() > 0) {
        	FormUser threadUser = UtilCheckData.getLocalUser();
            Map<String, Object> mapColumnValue = new HashMap<String, Object>();
            mapColumnValue.put("table_title_name", threadUser.getTable_title_name());
            mapColumnValue.put("column", listColumns);

            for (int i = 0; i < listValues.size(); i++) {
                String strValues = listValues.get(i);
                strValues = strValues.replace("(", "('"  + threadUser.getShop_id() + "', ");
                listValues.set(i, strValues);
            }

            mapColumnValue.put("value", listValues);
            return salesShopMapper.duplicate_vt_sales_shop(mapColumnValue);
        }else {
            return 0;
        }

    }
}
