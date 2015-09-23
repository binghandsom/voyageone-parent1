package com.voyageone.batch.wms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.wms.modelbean.TakeStockBean;
import com.voyageone.common.Constants;

@Repository
public class TakeStockDao extends BaseDao {

    /**
     * 取得需要进行盘点比较的盘点记录（盘点状态为Stock的记录抽出）
     * @param OrderChannelID 订单渠道
     * @param row_count 抽出件数
     * @return List<TakeStockBean>
     */
    public List<TakeStockBean> getTakeStockInfo(String OrderChannelID,int row_count) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", OrderChannelID);
        params.put("limit", row_count);

        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_selectTakeStockInfo", params);
    }

    /**
     * 进行盘点比较
     * @param takeStock 盘点详细
     */
    public void compareStock(TakeStockBean takeStock) {
        updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_compareStock", takeStock);
    }

}
