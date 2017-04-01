package com.voyageone.service.dao.ims;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.ims.ImsBtProductExceptModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tom.zhu 16/10/20
 */
@Repository
public class ImsBtProductExceptDao extends ServiceBaseDao {

    public int selectImsBtProductExceptByChannelCartSku(String orderChannelId, List<String> skuList) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("order_channel_id", orderChannelId);
        dataMap.put("except_sku", skuList);
        List<ImsBtProductExceptModel> lst = selectList("ims_bt_product_except_selectByChannelCartSku", dataMap);

        if (lst == null) {
            return 0;
        } else {
            return lst.size();
        }
    }
}
