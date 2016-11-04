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
 * ItemDetailsService
 *
 * @author jeff.duan 16/10/20
 * @version 2.0.0
 */
@Service
public class ItemDetailsService extends BaseService {
    @Autowired
    private WmsBtItemDetailsDao wmsBtItemDetailsDao;

    public void updateCodeForMove(String channelId, String itemCodeOld, List<String> skuList, String itemCodeNew, String modifier) {
        wmsBtItemDetailsDao.updateCodeForMove(channelId, itemCodeOld, skuList, itemCodeNew, modifier);
    }

}
