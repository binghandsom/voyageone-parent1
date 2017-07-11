package com.voyageone.service.impl.cms.usa;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.cms.CmsBtDataAmount.EnumDataAmountType;
import com.voyageone.service.dao.cms.CmsBtDataAmountDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtDataAmountModel;

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


        return homeDataMap;


    }

}
