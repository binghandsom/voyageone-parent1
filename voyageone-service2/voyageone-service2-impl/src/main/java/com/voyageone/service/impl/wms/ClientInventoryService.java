package com.voyageone.service.impl.wms;

import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.common.configs.Stores;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.StoreBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.service.bean.wms.ItemDetailsBean;
import com.voyageone.service.dao.wms.WmsBtClientInventoryDao;
import com.voyageone.service.dao.wms.WmsBtItemDetailsDao;
import com.voyageone.service.impl.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClientInventoryService
 *
 * @author chuanyu.liang 16/09/07
 * @version 2.0.0
 */
@Service
public class ClientInventoryService extends BaseService {
    @Autowired
    private WmsBtClientInventoryDao wmsBtClientInventoryDao;
    @Autowired
    private WmsBtItemDetailsDao wmsBtItemDetailsDao;

    public void insertClientInventory(String channelId, String clientSku, Integer qty) {
        // 判断在itemDetail表中是否存在
        List<ItemDetailsBean> items = wmsBtItemDetailsDao.selectByClientSku(channelId, clientSku);

        // 如果存在那么就更新库存
        if (items != null && items.size() > 0) {
            Map<String, Object> param = new HashMap<>();
            param.put("channelId", channelId);
            param.put("clientSku", clientSku);
            param.put("qty", qty);
            long storeID = 0;
            ThirdPartyConfigBean bean = ThirdPartyConfigs.getThirdPartyConfig(channelId, "inventory_full_update_config");
            if (bean != null) {
                storeID = getStore(channelId, bean.getProp_val6());
            }
            param.put("storeId", storeID);
            param.put("itemCode", items.get(0).getItemcode());
            param.put("sku", items.get(0).getSku());
            param.put("sizeColumn", items.get(0).getSize());
            param.put("barcode", items.get(0).getBarcode());
            param.put("synFlg", "0");
            param.put("simFlg", "0");
            param.put("active", 1);
            param.put("creater", "ClientInventoryService");
            wmsBtClientInventoryDao.insertClientInventory(param);
        }
    }

    public void updateClientInventorySynFlag(String channelId) {
        wmsBtClientInventoryDao.updateClientInventorySynFlag(channelId);
    }

    /**
     * @description 获取渠道对应的仓库
     * @param order_channel_id 渠道
     * @return storeId
     */
    private Long getStore(String order_channel_id, String store_area) {
        long storeID = 0;
        List<StoreBean> storeBeans = Stores.getChannelStoreList(order_channel_id);
        assert storeBeans != null;
        for (StoreBean storeBean : storeBeans) {
            // 库存不由我们管理；是真实仓库；仓库所属区域与配置相同
            if (storeBean.getInventory_manager().equals(StoreConfigEnums.Manager.NO.getId())
                    && storeBean.getStore_kind().equals(StoreConfigEnums.Kind.REAL.getId())
                    && storeBean.getStore_area().equals(store_area)) {
                storeID = storeBean.getStore_id();
                break;
            }
        }

        return storeID;
    }

}
