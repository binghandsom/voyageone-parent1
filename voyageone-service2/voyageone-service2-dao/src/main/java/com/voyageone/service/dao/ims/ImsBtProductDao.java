package com.voyageone.service.dao.ims;

import com.voyageone.common.Constants;
import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.ims.ImsBtProductModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author morse.lu 16/04/20
 */
@Repository
public class ImsBtProductDao extends ServiceBaseDao {

    public ImsBtProductModel selectImsBtProductByChannelCartCode(String channelId, int cartId, String code) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("channelId", channelId);
        dataMap.put("cartId", cartId);
        dataMap.put("code", code);
        return selectOne("ims_bt_product_selectByChannelCartCode", dataMap);
    }

    public int insertImsBtProduct(ImsBtProductModel productModel, String creater) {
        productModel.setCreater(creater);
        productModel.setModifier(creater);
        return insert("ims_bt_product_insert", productModel);
    }

    public int updateImsBtProductBySeq(ImsBtProductModel productModel, String modifier) {
        productModel.setModifier(modifier);
        return update("ims_bt_product_updateBySeq", productModel);
    }
}
