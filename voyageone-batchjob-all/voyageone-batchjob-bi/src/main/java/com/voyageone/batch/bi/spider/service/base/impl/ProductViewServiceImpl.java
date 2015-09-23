package com.voyageone.batch.bi.spider.service.base.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jd.open.api.sdk.domain.ware.Ware;
import com.taobao.api.domain.Item;
import com.voyageone.batch.bi.mapper.ViewProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voyageone.batch.bi.bean.formbean.FormUser;
import com.voyageone.batch.bi.mapper.SalesProductMapper;
import com.voyageone.batch.bi.spider.service.base.ProductViewService;
import com.voyageone.batch.bi.util.UtilCheckData;

/**
 * Created by Kylin on 2015/6/5.
 */
@Service
public class ProductViewServiceImpl implements ProductViewService {

    @Autowired
    SalesProductMapper salesProductMapper;

    @Autowired
    ViewProductMapper viewProductMapper;

    public void selectProductInfo() {

    }

    public void updateProductInfo(List<String> listColumns, List<String> listValues) {

    }

    public void insertProductInfo(List<String> listColumns, List<String> listValues) {

    }

    public void replaceProductInfo(List<String> listColumns, List<String> listValues) {

    }

    @Transactional("transactionManagerMain")
    public int duplicateProductInfo(List<String> listColumns, String strProductIid, List<String> listValues) {

        if (listValues.size() > 0) {

            FormUser threadUser = UtilCheckData.getLocalUser();

            Map<String, Object> mapColumnValue = new HashMap<String, Object>();

            mapColumnValue.put("table_title_name", threadUser.getTable_title_name());
            mapColumnValue.put("column", listColumns);

            for (int i = 0; i < listValues.size(); i++) {
                String strValues = listValues.get(i);
                strValues = strValues.replace("(", "('" + threadUser.getShop_id() + "', '" + strProductIid + "', ");
                listValues.set(i, strValues);
            }

            mapColumnValue.put("value", listValues);

            return salesProductMapper.duplicate_vt_sales_product(mapColumnValue);
        } else {
            return 0;
        }

    }

    @Transactional("transactionManagerMain")
    @Override
    public int duplicateProductInfoJDTotal(List<String> listColumns, String strProductIid, List<String> listValues) {
        if (listValues.size() > 0) {

            FormUser threadUser = UtilCheckData.getLocalUser();

            Map<String, Object> mapColumnValue = new HashMap<String, Object>();

            mapColumnValue.put("table_title_name", threadUser.getTable_title_name());
            mapColumnValue.put("column", listColumns);

            for (int i = 0; i < listValues.size(); i++) {
                String strValues = listValues.get(i);
                strValues = strValues.replace("(", "('" + threadUser.getShop_id() + "', '" + strProductIid + "', ");
                listValues.set(i, strValues);
            }

            mapColumnValue.put("value", listValues);

            return salesProductMapper.duplicate_vt_sales_product_total(mapColumnValue);
        } else {
            return 0;
        }
    }

    @Transactional("transactionManagerMain")
    @Override
    public int duplicateProductInfoJDpc(List<String> listColumns, String strProductIid, List<String> listValues) {
        if (listValues.size() > 0) {

            FormUser threadUser = UtilCheckData.getLocalUser();

            Map<String, Object> mapColumnValue = new HashMap<String, Object>();

            mapColumnValue.put("table_title_name", threadUser.getTable_title_name());
            mapColumnValue.put("column", listColumns);

            for (int i = 0; i < listValues.size(); i++) {
                String strValues = listValues.get(i);
                strValues = strValues.replace("(", "('" + threadUser.getShop_id() + "', '" + strProductIid + "', ");
                listValues.set(i, strValues);
            }

            mapColumnValue.put("value", listValues);

            return salesProductMapper.duplicate_vt_sales_product_pc(mapColumnValue);
        } else {
            return 0;
        }
    }

    @Transactional("transactionManagerMain")
    @Override
    public int duplicateProductInfoJDmobile(List<String> listColumns, String strProductIid, List<String> listValues) {
        if (listValues.size() > 0) {

            FormUser threadUser = UtilCheckData.getLocalUser();

            Map<String, Object> mapColumnValue = new HashMap<String, Object>();

            mapColumnValue.put("table_title_name", threadUser.getTable_title_name());
            mapColumnValue.put("column", listColumns);

            for (int i = 0; i < listValues.size(); i++) {
                String strValues = listValues.get(i);
                strValues = strValues.replace("(", "('" + threadUser.getShop_id() + "', '" + strProductIid + "', ");
                listValues.set(i, strValues);
            }

            mapColumnValue.put("value", listValues);

            return salesProductMapper.duplicate_vt_sales_product_mobile(mapColumnValue);
        } else {
            return 0;
        }
    }

    @Transactional("transactionManagerMain")
    @Override
    public int duplicateViewProductLifeInfoTM(List<Item> listItems) {

        if (listItems.size() > 0) {
            FormUser threadUser = UtilCheckData.getLocalUser();
            Map<String, Object> mapColumnValue = new HashMap<String, Object>();
            mapColumnValue.put("db_name", threadUser.getDb_name());
            mapColumnValue.put("shop_id", threadUser.getShop_id());
            mapColumnValue.put("value", listItems);
            return viewProductMapper.duplicate_vs_view_sales_product_lift_cycle_tm(mapColumnValue);
        } else {
            return 0;
        }
    }

    @Transactional("transactionManagerMain")
    @Override
    public int duplicateViewProductLifeOnListInfoJD(List<Ware> listWares) {

        if (listWares.size() > 0) {
            FormUser threadUser = UtilCheckData.getLocalUser();
            Map<String, Object> mapColumnValue = new HashMap<String, Object>();
            mapColumnValue.put("db_name", threadUser.getDb_name());
            mapColumnValue.put("shop_id", threadUser.getShop_id());
            mapColumnValue.put("value", listWares);
            return viewProductMapper.duplicate_vs_view_sales_product_lift_cycle_jd_on(mapColumnValue);
        } else {
            return 0;
        }
    }

    @Transactional("transactionManagerMain")
    @Override
    public int duplicateViewProductLifeDeListInfoJD(List<Ware> listWares) {
        if (listWares.size() > 0) {
            FormUser threadUser = UtilCheckData.getLocalUser();
            Map<String, Object> mapColumnValue = new HashMap<String, Object>();
            mapColumnValue.put("db_name", threadUser.getDb_name());
            mapColumnValue.put("shop_id", threadUser.getShop_id());
            mapColumnValue.put("value", listWares);
            return viewProductMapper.duplicate_vs_view_sales_product_lift_cycle_jd_de(mapColumnValue);
        } else {
            return 0;
        }
    }
}
