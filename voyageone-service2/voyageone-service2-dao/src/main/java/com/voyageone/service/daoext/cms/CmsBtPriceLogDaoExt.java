package com.voyageone.service.daoext.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtPriceLogModel_Mysql;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/18
 * @version 2.0.0
 */
@Repository
public class CmsBtPriceLogDaoExt extends ServiceBaseDao {

    public List<CmsBtPriceLogModel_Mysql> selectPriceLogByCode(Map<String, Object> param) {
        return selectList("select_cms_bt_price_log_by_code", param);
    }

    public int selectPriceLogByCodeCnt(Map<String, Object> param) {
        return selectOne("select_cms_bt_price_log_by_code_cnt", param);
    }

    public int insertCmsBtPriceLogList(List<CmsBtPriceLogModel_Mysql> paramList) {
        Map<String, List<CmsBtPriceLogModel_Mysql>> insertDataMap = new HashMap<>();
        insertDataMap.put("list", paramList);
        return insert("insert_cms_bt_price_log_list", insertDataMap);
    }

    public CmsBtPriceLogModel_Mysql selectLastOneBySkuOnCart(String sku, Integer cartId, String channelId) {
        CmsBtPriceLogModel_Mysql model = selectOne("selectLastOneBySkuOnCart", parameters("sku", sku, "channel_id", channelId, "cart_id", cartId));
        if (model == null) {
            model = selectOne("selectLastOneBySkuOnCardFromHistory", parameters("sku", sku, "channel_id", channelId, "cart_id", cartId));
        }
        return model;
    }

    public List<CmsBtPriceLogModel_Mysql> selectListBySkuOnCart(String sku, String code, String cartId, String channelId) {
        return selectPageBySkuOnCart(sku, code, cartId, channelId, null, null);
    }

    public List<CmsBtPriceLogModel_Mysql> selectPageBySkuOnCart(String sku, String code, String cartId, String channelId, Integer offset, Integer limit) {

        if (StringUtils.isEmpty(sku))
            sku = null;

        if (StringUtils.isEmpty(cartId))
            cartId = null;

        return selectList("selectPageBySkuOnCart", parameters(
                "sku", sku,
                "code", code,
                "channel_id", channelId,
                "cart_id", cartId,
                "offset", offset,
                "limit", limit));
    }

    public int selectCountBySkuOnCart(String sku, String code, String cartId, String channelId) {

        if (StringUtils.isEmpty(sku))
            sku = null;

        if (StringUtils.isEmpty(cartId))
            cartId = null;

        return selectOne("selectCountBySkuOnCart", parameters(
                "sku", sku,
                "code", code,
                "channel_id", channelId,
                "cart_id", cartId));
    }

    public int updateCmsBtPriceLogForMove(String channelId, String itemCodeOld, List<String> skuList, String itemCodeNew, String modifier) {
        Map<String, Object> params = new HashMap<>();
        params.put("channelId", channelId);
        params.put("itemCodeOld", itemCodeOld);
        params.put("skuList", skuList);
        params.put("itemCodeNew", itemCodeNew);
        params.put("modifier", modifier);
        return update("updateCodeForMove", params);
    }
}
