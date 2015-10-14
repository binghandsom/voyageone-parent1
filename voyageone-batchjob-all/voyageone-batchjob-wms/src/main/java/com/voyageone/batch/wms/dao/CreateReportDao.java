package com.voyageone.batch.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.wms.modelbean.ClientInventoryBean;
import com.voyageone.batch.wms.modelbean.SpaldingPriceBean;
import com.voyageone.batch.wms.modelbean.ThirdReportBean;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fred on 2015/8/5.
 * 第三方日报用
 */
@Repository
public class CreateReportDao extends BaseDao {

    /**
     * 销售订单基本数据取得
     * @param cartID
     * @param order_channel_id
     * @param transfer_status
     * @param transfer_type
     * @param transfer_origin
     * @param start_created
     * @param end_created
     * @param task_name
     * @return List<ThirdReportBean>
     */
    public List<ThirdReportBean> getCreateReportData(String cartID,String order_channel_id,String transfer_status,String transfer_type,
                                                        String transfer_origin,String start_created,String end_created,String task_name) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("cart_id", cartID);
        paramMap.put("order_channel_id", order_channel_id);
        paramMap.put("transfer_status",transfer_status);
        paramMap.put("transfer_type",transfer_type);
        paramMap.put("transfer_origin",transfer_origin);
        paramMap.put("start_created",start_created);
        paramMap.put("end_created",end_created);
        paramMap.put("task_name",task_name);

        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_getCreateReportData", paramMap);
    }

    /**
     * 取得退货订单（退回TM仓库）的日报基本数据记录
     * @param cartID
     * @param order_channel_id
     * @param transfer_status
     * @param transfer_type
     * @param transfer_origin
     * @param start_created
     * @param end_created
     * @param task_name
     * @return List<ThirdReportBean>
     */
    public List<ThirdReportBean> getCreateReportDataByTM(String cartID,String order_channel_id,String transfer_status,String transfer_type,
                                                             String transfer_origin,String start_created,String end_created,String task_name) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("cart_id", cartID);
        paramMap.put("order_channel_id", order_channel_id);
        paramMap.put("transfer_status",transfer_status);
        paramMap.put("transfer_type",transfer_type);
        paramMap.put("transfer_origin",transfer_origin);
        paramMap.put("start_created",start_created);
        paramMap.put("end_created",end_created);
        paramMap.put("task_name",task_name);
        paramMap.put("transfer_type_out", CodeConstants.TransferType.WITHDRAWAL);
        paramMap.put("transfer_origin_out", CodeConstants.TransferOrigin.WITHDRAWAL);

        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_getCreateReportDataByTM", paramMap);
    }

    /**
     * 取得特殊物品销售订单的日报基本数据记录
     * @param cartID
     * @param order_channel_id
     * @param specialType
     * @param start_created
     * @param end_created
     * @param task_name
     * @return List<ThirdReportBean>
     */
    public List<ThirdReportBean> getCreateReportSpecialData(String cartID,String order_channel_id,String specialType,String start_created,String end_created,String task_name) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("cart_id", cartID);
        paramMap.put("order_channel_id", order_channel_id);
        paramMap.put("specialType",specialType);
        paramMap.put("start_created",start_created);
        paramMap.put("end_created",end_created);
        paramMap.put("task_name",task_name);

        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_getCreateReportSpecialData", paramMap);
    }


    /**
     * //销售订单基本价格数据取得
     * @param order_channel_id
     * @param order_number
     *
     * @return List<SpaldingPriceBean>
     */
    public List<SpaldingPriceBean> getPriceData(String order_number,String order_channel_id) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("order_channel_id", order_channel_id);
        paramMap.put("order_number",order_number);

        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_getPriceData", paramMap);
    }
}
