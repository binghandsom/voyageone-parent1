package com.voyageone.service.impl.cms;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CmsBtDataAmountService {
    @Autowired
    CmsBtFeedInfoDao daoCmsBtFeedInfo;
    @Autowired
    CmsBtProductDao daoCmsBtProduct;

    //1.FEED信息
    public void sumCmsBtFeedInfo() {
        String channelId = "010";
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

    // 2.主数据编辑信息
    public void sumMaster() {
        String channelId = "010";
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

    // 3.价格信息
    public void sumPrice() {
        String channelId = "015";
        String cardId = "27";
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
}
