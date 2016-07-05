package com.voyageone.service.impl.cms;
import com.voyageone.service.bean.cms.CmsBtDataAmount.EnumFeedSum;
import com.voyageone.service.bean.cms.CmsBtDataAmount.EnumMasterSum;
import com.voyageone.service.bean.cms.CmsBtDataAmount.EnumPlatformPriceSum;
import com.voyageone.service.bean.cms.CmsBtDataAmount.IEnumDataAmountSum;
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

    public void sumCmsBtFeedInfo() {
        sumCmsBtFeedInfo("010");
    }
    //1.FEED信息
    public void sumCmsBtFeedInfo(String channelId) {
        List<EnumFeedSum> list = EnumFeedSum.getList();
        CmsBtDataAmountModel model = null;
        for (EnumFeedSum enumFeed : list) {
            long count = daoCmsBtFeedInfo.countByQuery(enumFeed.getStrQuery(), channelId);
            saveCmsBtDataAmount(channelId,0, enumFeed, count);
        }

        //        1.1 feed商品数
        //            CMS_FEED_ALL
        //        cms_bt_feed_info_cxxx的商品总数
        long channelFeedAllCount = daoCmsBtFeedInfo.countByQuery("", channelId);
        //        1.2 新品数
        //            CMS_FEED_STATUS_NEW
        //        cms_bt_feed_info_cxxx表中的upd_flg = 9的商品数  {'updFlg':9}
        long channelFeedNewCount = daoCmsBtFeedInfo.countByQuery("{'updFlg':9}", channelId);
        //        1.3 导入完成数
        //
        //            CMS_FEED_STATUS_IMPORT_FINISH
        //        cms_bt_feed_info_cxxx表中的upd_flg = 1的商品数   {'updFlg':1}
        long channelFeedImportFinishCount = daoCmsBtFeedInfo.countByQuery("{'updFlg':1}", channelId);
        //        1.4 不导入数
        //            CMS_FEED_STATUS_NOT_IMPORT
        //        cms_bt_feed_info_cxxx表中的upd_flg = 3的商品数   {'updFlg':3}
        long channelFeedNotImportCount = daoCmsBtFeedInfo.countByQuery("{'updFlg':3}", channelId);
        //        1.5 导入失败数
        //            CMS_FEED_STATUS_IMPORT_FAILD
        //        cms_bt_feed_info_cxxx表中的upd_flg = 2的商品数   {'updFlg':2}
        long channelFeedImportFaildCount = daoCmsBtFeedInfo.countByQuery("{'updFlg':2}", channelId);
        //        1.6 Feed数据错误
        //        cms_bt_feed_info_cxxx表中的upd_flg = 2的商品数   {'updFlg':8}
        long channelFeedDataErrorCount = daoCmsBtFeedInfo.countByQuery("{'updFlg':8}", channelId);
        //        1.7 等待导入数  ？ 界面不存在
        //            CMS_FEED_STATUS_WAITING_FOR_IMPORT
        //        cms_bt_feed_info_cxxx表中的upd_flg = 0的商品数	 {'updFlg':0}
        long channelFeedWaitingForImportCount = daoCmsBtFeedInfo.countByQuery("{'updFlg':0}", channelId);

    }

    public void sumMaster() {
        String channelId = "010";
    }
    // 2.主数据编辑信息
    public void sumMaster( String channelId) {
        List<EnumMasterSum> list = EnumMasterSum.getList();
        CmsBtDataAmountModel model = null;
        for (EnumMasterSum enumFeed : list) {
            long count = daoCmsBtProduct.countByQuery(enumFeed.getStrQuery(), channelId);
            saveCmsBtDataAmount(channelId,0, enumFeed, count);
        }
        //        2.1等待设置主类目数          {'common.fields.isMasterMain':1,'common.fields.categoryStatus':{$in:[null,0]}}
        //        CMS_MASTER_NO_CATEGORY
        //        cms_bt_product_cxxx表中的common.fields.isMasterMain = 1 and common.fields.categoryStatus不存在或者为null或者为0的商品数
        long masterNoCategoryCount = daoCmsBtProduct.countByQuery("{'common.fields.isMasterMain':1,'common.fields.categoryStatus':{$in:[null,0]}}", channelId);
        //        2.2等待设置税号数            {'common.fields.isMasterMain':1,'common.fields.hsCodeStatus':{$in:[null,0]}}
        //        CMS_MASTER_NO_HSCODE
        //        cms_bt_product_cxxx表中的common.fields.isMasterMain = 1 and common.fields.hsCodeStatus不存在或者为null或者为0的商品数
        long masterNoHsCodeCount = daoCmsBtProduct.countByQuery("{'common.fields.isMasterMain':1,'common.fields.hsCodeStatus':{$in:[null,0]}}", channelId);
        //        2.3等待翻译数                 {'common.fields.isMasterMain':1,'common.fields.translateStatus':{$in:[null,0]}}
        //        CMS_MASTER_UNTRANSLATED
        //        cms_bt_product_cxxx表中的common.fields.isMasterMain = 1 and common.fields.translateStatus不存在或者为null或者为0的商品数
        long masterUnTranslatedCount = daoCmsBtProduct.countByQuery("{'common.fields.isMasterMain':1,'common.fields.translateStatus':{$in:[null,0]}}", channelId);
    }
    public void sumPrice() {
        String channelId = "015";
        int cardId = 27;
        sumPrice(channelId, cardId);
    }
    // 3.价格信息
    public void sumPrice(String channelId,int cardId) {

        List<EnumPlatformPriceSum> list = EnumPlatformPriceSum.getList();
        CmsBtDataAmountModel model = null;
        for (EnumPlatformPriceSum enumFeed : list) {
            long count = daoCmsBtProduct.countByQuery(String.format(enumFeed.getStrQuery(), cardId), channelId);
            saveCmsBtDataAmount(channelId,cardId, enumFeed, count);
        }
        //        3.价格信息   各平台 http://localhost:9092/modules/cms/app.html#/search/advanceSearch   平台搜索条件   查询价格变动
        //    3.1比指导价低               {'platforms.P27.skus.priceChgFlg':{$regex:"^D"}}
        //        CMS_PLATFORM_PRICE_DOWN
        //        cms_bt_product_cxxx表中的platform.Pxx.skus.priceChgFlg是以D开头的商品数

        long priceDownCount = daoCmsBtProduct.countByQuery(String.format("{'platforms.P%s.skus.priceChgFlg':{$regex:\"^D\"}}", cardId), channelId);

        //        3.2比指导价高                {'platforms.P27.skus.priceChgFlg':{$regex:"^U"}}
        //        CMS_PLATFORM_PRICE_UP
        //        cms_bt_product_cxxx表中的platform.Pxx.skus.priceChgFlg是以U开头的商品数
        long priceUpCount = daoCmsBtProduct.countByQuery(String.format("{'platforms.P%s.skus.priceChgFlg':{$regex:\"^U\"}}", cardId), channelId);
        //        3.3击穿警告                   {'platforms.P27.skus.priceChgFlg':{$regex:"^X"}}
        //        CMS_PLATFORM_PRICE_BREAKDOWN
        //        cms_bt_product_cxxx表中的platform.Pxx.skus.priceChgFlg是以X开头的商品数
        long priceBreakDownCount = daoCmsBtProduct.countByQuery(String.format("{'platforms.P%s.skus.priceChgFlg':{$regex:\"^X\"}}", cardId), channelId);
    }

    // 4.各平台信息
    public void sumPlatInfo() {
        String channelId = "015";
        String cardId = "27";
        //        4.各平台信息   http://localhost:9092/modules/cms/app.html#/search/advanceSearch
        //        4.1商品数
        long productCount = daoCmsBtProduct.countByQuery(String.format("{platform.P%s:{$exists:true}}", cardId), channelId);
        //        4.2等待设置平台类目              对应 平台类目未设置             {'platform.P27.pCatStatus':{$in:[null,0]}}
        //        CMS_PLATFORM_NO_CATEGORY
        //        cms_bt_product_cxxx表中的platform.Pxx的pCatStatus不存在或者为null或者为0的商品数
        long noCategoryCount = daoCmsBtProduct.countByQuery(String.format("{'platform.P%s.pCatStatus':{$in:[null,0]}}", cardId), channelId);

        //        4.3等待设置属性数 对应界面条件待确认？                            {'platform.P27.pAttributeStatus':{$in:[null,0]}}
        //        CMS_PLATFORM_NO_ATTRIBUTE
        //        cms_bt_product_cxxx表中的platform.Pxx的pAttributeStatus不存在或者为null或者为0商品数
        long noAttributeCount = daoCmsBtProduct.countByQuery(String.format("{'platform.P%s.pAttributeStatus':{$in:[null,0]}}", cardId), channelId);
        //        4.4等待Approved数       Ready数量                                  {'platform.P27.status':'Ready'}
        //        CMS_PLATFORM_READY
        //        cms_bt_product_cxxx表中的platform.Pxx.status为Ready的商品数
        long readyCount = daoCmsBtProduct.countByQuery(String.format("{'platform.P%s.status':'Ready'}", cardId), channelId);
        //        4.5等待上新数               WaitingPublish                          {'platform.P27.pStatus':'WaitingPublish'}
        //        CMS_PLATFORM_WAITING_PUBLISh
        //        cms_bt_product_cxxx表中的platform.Pxx.pStatus为WaitingPublish的商品数
        long waitingPublishCount = daoCmsBtProduct.countByQuery(String.format("{'platform.P%s.pStatus':'WaitingPublish'}", cardId), channelId);
        //        4.6上新成功数   对应界面条件待确认？                                 {'platform.P27.pPublishError':{$in:[null,0]}}
        //        CMS_PLATFORM_PUBLISH_SUCCESS
        //        cms_bt_product_cxxx表中的platform.Pxx.pNumIId有值 and platform.Pxx.pPublishError不存在或者为null或者为0的商品数
        long waitingPublishSuccess = daoCmsBtProduct.countByQuery(String.format("{'platform.P%s.pPublishError':{$in:[null,0]}}", cardId), channelId);
        //        4.7上新失败数            错误                                        {'platform.P27.pPublishError':{$nin:[null,0]}}
        //        CMS_PLATFORM_PUBLISH_FAILD
        //        cms_bt_product_cxxx表中的platform.Pxx.pPublishError不为空的商品数
        long publishFaild = daoCmsBtProduct.countByQuery(String.format("{'platform.P%s.pPublishError':{$nin:[null,0]}}", cardId), channelId);
    }

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
    public  CmsBtDataAmountModel get(String channelId,int cardId,String amountName) {
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", channelId);
        map.put("amountName", amountName);
        map.put("cardId",cardId);
        return dao.selectOne(map);
    }
}
