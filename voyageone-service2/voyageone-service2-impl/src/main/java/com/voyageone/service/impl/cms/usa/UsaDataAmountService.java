package com.voyageone.service.impl.cms.usa;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.cms.CmsBtDataAmount.EnumDataAmountType;
import com.voyageone.service.bean.cms.CmsBtDataAmount.EnumPlatformInfoSum;
import com.voyageone.service.dao.cms.CmsBtDataAmountDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtDataAmountModel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 美国CMS2 cms_bt_data_amount相关service
 *
 * @Author rex.wu
 * @Create 2017-07-11 16:05
 */
@Service
public class UsaDataAmountService extends BaseService {

    @Autowired
    private CmsBtDataAmountDao cmsBtDataAmountDao;

    public Map<String, Object> getUsaHomeData(String channelId) {
        Map<String, Object> homeDataMap = new HashMap<>();

        CmsBtDataAmountModel queryModel = new CmsBtDataAmountModel();
        queryModel.setChannelId(channelId);
        queryModel.setDataAmountTypeId(EnumDataAmountType.UsaFeedSum.getId());


        homeDataMap.put("feedInfo", cmsBtDataAmountDao.selectList(queryModel));

        // Product 各平台统计数：<cartId,<amountName, amountModel>>
        Map<Integer, Map<String, CmsBtDataAmountModel>> platformMap = new HashMap<>();

        queryModel.setDataAmountTypeId(EnumDataAmountType.UsaPlatformSum.getId());
        List<CmsBtDataAmountModel> platformDataAmountList = cmsBtDataAmountDao.selectList(queryModel);
        if (CollectionUtils.isNotEmpty(platformDataAmountList)) {
            for (CmsBtDataAmountModel dataAmount : platformDataAmountList) {
                if (platformMap.containsKey(dataAmount.getCartId())) {
                    platformMap.get(dataAmount.getCartId()).put(dataAmount.getAmountName(), dataAmount);
                } else {
                    Map<String, CmsBtDataAmountModel> valMap = new HashMap<>();
                    valMap.put(dataAmount.getAmountName(), dataAmount);
                    platformMap.put(dataAmount.getCartId(), valMap);
                }
            }
        }

        // 为每个cart 增加一个All Items, 懒得在js中写
        for (Map.Entry<Integer, Map<String, CmsBtDataAmountModel>> entry : platformMap.entrySet()) {
            Integer cartId = entry.getKey();
            Map<String, CmsBtDataAmountModel> valueMap = entry.getValue();
            CmsBtDataAmountModel allDataAmount = new CmsBtDataAmountModel();
            allDataAmount.setCartId(cartId);
            allDataAmount.setAmountName("All Items");
            int total = 0;
            for (CmsBtDataAmountModel item : valueMap.values()) {
                total += Integer.valueOf(StringUtils.defaultString(item.getAmountVal(), "0"));
                allDataAmount.setLinkUrl(item.getLinkUrl());
            }
            allDataAmount.setAmountVal(String.valueOf(total));
            allDataAmount.setLinkParameter(String.format(EnumPlatformInfoSum.USA_CMS_PLATFORMS_AMOUNT.getLinkParameter(), String.valueOf(cartId), "all"));
            valueMap.put("All Items", allDataAmount);

        }

        homeDataMap.put("platformInfo", platformMap);
        return homeDataMap;

    }

}
