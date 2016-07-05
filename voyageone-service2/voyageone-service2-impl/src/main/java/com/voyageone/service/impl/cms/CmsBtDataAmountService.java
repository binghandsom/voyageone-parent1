package com.voyageone.service.impl.cms;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsBtDataAmount.*;
import com.voyageone.service.dao.cms.CmsBtDataAmountDao;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.model.cms.CmsBtDataAmountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CmsBtDataAmountService {
    @Autowired
    CmsBtFeedInfoDao daoCmsBtFeedInfo;
    @Autowired
    CmsBtProductDao daoCmsBtProduct;
   @Autowired
    CmsBtDataAmountDao dao;

    public  void sumByChannelId( String channelId) {
        //1.汇总FEED信息
        sumCmsBtFeedInfo(channelId);
        //2.主数据编辑信息
        sumMaster(channelId);
        List<TypeChannelBean> listCarts = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_D, "cn");
        for (TypeChannelBean chanelBean : listCarts) {
            if (!StringUtils.isEmpty(chanelBean.getValue())) {
                int cartId =Integer.parseInt(chanelBean.getValue());
                if (cartId == 1 || cartId == 0) {
                    continue;
                }
                //3.汇总平台价格信息
                sumPrice(channelId, cartId);
                //4.平台信息
                sumPlatInfo(channelId, cartId);
            }
        }
    }
    //1.FEED信息
    private  void sumCmsBtFeedInfo(String channelId) {
        List<EnumFeedSum> list = EnumFeedSum.getList();
        CmsBtDataAmountModel model = null;
        for (EnumFeedSum enumFeed : list) {
            long count = daoCmsBtFeedInfo.countByQuery(enumFeed.getStrQuery(), channelId);
            saveCmsBtDataAmount(channelId,0, enumFeed, count);
        }
    }

    // 2.主数据编辑信息
    private void sumMaster( String channelId) {
        List<EnumMasterSum> list = EnumMasterSum.getList();
        CmsBtDataAmountModel model = null;
        for (EnumMasterSum enumFeed : list) {
            long count = daoCmsBtProduct.countByQuery(enumFeed.getStrQuery(), channelId);
            saveCmsBtDataAmount(channelId, 0, enumFeed, count);
        }
    }
    // 3.价格信息
    private  void sumPrice(String channelId,int cardId) {
        List<EnumPlatformPriceSum> list = EnumPlatformPriceSum.getList();
        CmsBtDataAmountModel model = null;
        for (EnumPlatformPriceSum enumFeed : list) {
            long count = daoCmsBtProduct.countByQuery(String.format(enumFeed.getStrQuery(), cardId), channelId);
            saveCmsBtDataAmount(channelId,cardId, enumFeed, count);
        }
    }

    // 4.各平台信息
    private  void sumPlatInfo(String channelId,int cardId) {
        List<EnumPlatformInfoSum> list = EnumPlatformInfoSum.getList();
        CmsBtDataAmountModel model = null;
        for (EnumPlatformInfoSum enumFeed : list) {
            long count = daoCmsBtProduct.countByQuery(String.format(enumFeed.getStrQuery(), cardId), channelId);
            saveCmsBtDataAmount(channelId,cardId, enumFeed, count);
        }
    }

    //保存
    private void saveCmsBtDataAmount(String channelId,int cardId, IEnumDataAmountSum enumFeed, long count) {
        CmsBtDataAmountModel model;
        model = get(channelId, cardId, enumFeed.getAmountName());
        if (model == null) {
            model = new CmsBtDataAmountModel();
            model.setId(0);
            model.setCreated(new Date());
            model.setCreater("system");
            model.setAmountName(enumFeed.getAmountName());
            model.setChannelId(channelId);
            model.setCartId(cardId);
            model.setComment(enumFeed.getComment());
            model.setLinkParameter(enumFeed.getLinkParameter());
            model.setLinkUrl(enumFeed.getLinkUrl());
            model.setDataAmountTypeId(enumFeed.getDataAmountTypeId());
        }
        model.setAmountVal(Long.toString(count));
        if (model.getId() == 0) {
            dao.insert(model);
        } else {
            dao.update(model);
        }
    }
    private  CmsBtDataAmountModel get(String channelId,int cardId,String amountName) {
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", channelId);
        map.put("amountName", amountName);
        map.put("cardId",cardId);
        return dao.selectOne(map);
    }
}
