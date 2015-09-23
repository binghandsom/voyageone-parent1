package com.voyageone.batch.bi.spider.service.base.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voyageone.batch.bi.bean.formbean.FormUser;
import com.voyageone.batch.bi.bean.modelbean.ShopChannelEcommBean;
import com.voyageone.batch.bi.mapper.ChannelMapper;
import com.voyageone.batch.bi.mapper.ColumnMapper;
import com.voyageone.batch.bi.mapper.EcommMapper;
import com.voyageone.batch.bi.mapper.ProductIidMapper;
import com.voyageone.batch.bi.mapper.ShopChannelEcommMapper;
import com.voyageone.batch.bi.mapper.ViewProductMapper;
import com.voyageone.batch.bi.spider.constants.Constants;
import com.voyageone.batch.bi.spider.service.base.CommonSettingService;
import com.voyageone.batch.bi.util.UtilCheckData;
import com.voyageone.common.util.DateTimeUtil;

/**
 * Created by Kylin on 2015/6/10.
 */
@Service
public class CommonSettingServiceImpl implements CommonSettingService {

    @Autowired
    private ProductIidMapper productIidMapper;
    @Autowired
    private ColumnMapper columnMapper;
    @Autowired
    private EcommMapper ecommMapper;
    @Autowired
    private ChannelMapper channelMapper;
    @Autowired
    private ShopChannelEcommMapper driverConfigMapper;
    @Autowired
    private ViewProductMapper viewProductMapper;

    /**
     * 根据单批数量限制，获得商品Iid一览
     *
     * @param init_index 获取IID的起始序号
     * @param intSize    单批数量限制
     * @return
     */
    @Transactional("transactionManagerSub")
    @Override
    public List<String> getProductIidList(Integer init_index, Integer intSize) {
        FormUser threadUser = UtilCheckData.getLocalUser();

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("db_name", threadUser.getDb_name());
        mapParameter.put("channel_id", threadUser.getChannel_id());
        mapParameter.put("ecomm_id", threadUser.getEcomm_id());
        mapParameter.put("shop_id", threadUser.getShop_id());
        mapParameter.put("init_index", init_index);
        mapParameter.put("get_size", intSize);

        return productIidMapper.select_list_iid_vs_sales_product_iid(mapParameter);
    }

    /**
     * 获取Web取得项目所对应的数据库Table项目List——商品级（天猫）
     *
     * @param strTable
     * @param listViewColumn
     * @return
     */
    @Transactional("transactionManagerSub")
    @Override
    public List<String> getProductColumnList(String strTable, List<String> listViewColumn) {

        List<String> listTableColumns = new ArrayList<String>();
        listTableColumns.add(0, "shop_id");
        listTableColumns.add(1, "num_iid");

        for (int i = 0; i < listViewColumn.size(); i++) {
            //获取进程下，user信息
            FormUser threadUser = UtilCheckData.getLocalUser();
            //设定检索条件
            Map<String, String> mapParameter = new HashMap<String, String>();
            mapParameter.put("ecomm_id", Integer.toString(threadUser.getEcomm_id()));
            mapParameter.put("column_web_name", listViewColumn.get(i));
            mapParameter.put("cor_table_name", strTable);

            listTableColumns.add(i + 2, columnMapper.select_record_vm_column(mapParameter));
        }
        return listTableColumns;
    }

    /**
     * 获取Web取得项目所对应的数据库Table项目List——店铺级（天猫）
     *
     * @param strTable
     * @param listViewColumn
     * @return
     */
    @Transactional("transactionManagerSub")
    @Override
    public List<String> getShopColumnList(String strTable, List<String> listViewColumn) {

        List<String> listTableColumns = new ArrayList<String>();

        listTableColumns.add(0, "shop_id");

        for (int i = 0; i < listViewColumn.size(); i++) {
            FormUser threadUser = UtilCheckData.getLocalUser();
            //设定检索条件
            Map<String, String> mapParameter = new HashMap<String, String>();
            mapParameter.put("ecomm_id", Integer.toString(threadUser.getEcomm_id()));
            mapParameter.put("column_web_name", listViewColumn.get(i));
            mapParameter.put("cor_table_name", strTable);
            //将项目检索结果添加入返回List
            listTableColumns.add(i + 1, columnMapper.select_record_vm_column(mapParameter));
        }
        return listTableColumns;
    }

    /**
     * 获取Web取得项目所对应的数据库Table项目List——（京东）单项成列
     *
     * @param strTable
     * @return
     */
    @Transactional("transactionManagerSub")
    @Override
    public List<Map<String, String>> getColumnList(String strTable, String strWebType) {
        FormUser threadUser = UtilCheckData.getLocalUser();

        Map<String, String> mapParameter = new HashMap<String, String>();
        mapParameter.put("ecomm_id", Integer.toString(threadUser.getEcomm_id()));
        mapParameter.put("cor_table_name", strTable);
        mapParameter.put("column_web_type", strWebType);
        return columnMapper.select_list_jd_vm_column(mapParameter);

    }

    /**
     * 获取该店铺数据库内商品总数。
     *
     * @return
     */
    @Transactional("transactionManagerSub")
    @Override
    public int getCountProductIid() {
        FormUser threadUser = UtilCheckData.getLocalUser();

        Map<String, Object> mapCondition = new HashMap<String, Object>();
        mapCondition.put("db_name", threadUser.getDb_name());
        mapCondition.put("channel_id", threadUser.getChannel_id());
        mapCondition.put("ecomm_id", threadUser.getEcomm_id());
        mapCondition.put("shop_id", threadUser.getShop_id());

        return productIidMapper.select_count_vs_sales_product_iid(mapCondition);
    }

    @Transactional("transactionManagerSub")
    @Override
    public String getProductCode(String strProductIid) {
        FormUser threadUser = UtilCheckData.getLocalUser();

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("db_name", threadUser.getDb_name());
        mapParameter.put("shop_id", threadUser.getShop_id());
        mapParameter.put("num_iid", strProductIid);

        return productIidMapper.select_record_product_code_vs_sales_product_iid(mapParameter);
    }

    @Transactional("transactionManagerSub")
    @Override
    public String getProductIid() {
        FormUser threadUser = UtilCheckData.getLocalUser();

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("db_name", threadUser.getDb_name());
        mapParameter.put("channel_id", threadUser.getChannel_id());
        mapParameter.put("ecomm_id", threadUser.getEcomm_id());
        mapParameter.put("shop_id", threadUser.getShop_id());

        return productIidMapper.select_iid_vs_sales_product_iid(mapParameter);
    }

    @Transactional("transactionManagerSub")
    @Override
    public String getEcommCode(int iEcommID) {
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("id", iEcommID);
        mapParameter.put("enable", Constants.ENABLE);

        return ecommMapper.select_code_vm_ecomm(mapParameter);
    }

    @Transactional("transactionManagerSub")
    @Override
    public String getChannelCode(int iChannelID) {
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("id", iChannelID);
        mapParameter.put("enable", Constants.ENABLE);

        return channelMapper.select_code_vm_channel(mapParameter);
    }

    @Transactional("transactionManagerSub")
    @Override
    public List<ShopChannelEcommBean> initShopConfigBeanList() {
        return driverConfigMapper.select_list_vm_shop_driver(Constants.ENABLE);
    }

    /**
     * 获取该店铺数据库内商品总数。
     *
     * @return
     */
    @Transactional("transactionManagerSub")
    @Override
    public int getCountViewProductIid() {
        FormUser threadUser = UtilCheckData.getLocalUser();

        Map<String, Object> mapCondition = new HashMap<String, Object>();
        mapCondition.put("db_name", threadUser.getDb_name());
        mapCondition.put("shop_id", threadUser.getShop_id());
        mapCondition.put("delist_time_from", DateTimeUtil.addDays(DateTimeUtil.getDate(), -7));

        return viewProductMapper.select_count_vs_view_sales_product_lift_cycle(mapCondition);
    }

    /**
     * 根据单批数量限制，获得商品Iid一览
     *
     * @param init_index 获取IID的起始序号
     * @param intSize    单批数量限制
     * @return
     */
    @Transactional("transactionManagerSub")
    @Override
    public List<String> getViewProductIidList(Integer init_index, Integer intSize) {
        FormUser threadUser = UtilCheckData.getLocalUser();

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("db_name", threadUser.getDb_name());
        mapParameter.put("shop_id", threadUser.getShop_id());
        mapParameter.put("delist_time_from", DateTimeUtil.addDays(DateTimeUtil.getDate(), -7));
        mapParameter.put("init_index", init_index);
        mapParameter.put("get_size", intSize);

        return viewProductMapper.select_list_iid_vs_view_sales_product_lift_cycle(mapParameter);
    }
}
