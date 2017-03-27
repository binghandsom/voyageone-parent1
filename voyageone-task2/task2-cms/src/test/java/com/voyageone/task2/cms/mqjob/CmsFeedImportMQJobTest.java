package com.voyageone.task2.cms.mqjob;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsFeedImportMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by gjl on 2017/3/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsFeedImportMQJobTest {
    @Autowired
    private CmsFeedImportMQJob cmsFeedImportMQJob;
    @Test
    public void testOnStartup() throws Exception {

        String msg1= "{\"channelId\":\"010\",\"cmsBtFeedInfoModels\":[{\"category\":\"Earrings-Drops-Cubic Zirconia\",\"code\":\"00395\",\"name\":\"Drop Earrings with Cubic Zirconia in Sterling Silver\",\"model\":\"00395\",\"color\":\"White / White /\",\"origin\":\"US\",\"sizeType\":\"Women\\u0027s\",\"image\":[\"http://max1.jewelrystatic.com/media/catalog/product/0/0/00395_main.jpg\",\"http://max1.jewelrystatic.com/media/catalog/product/0/0/00395_a1.jpg\"],\"brand\":\"Jewelry.com\",\"weight\":\"0.00\",\"shortDescription\":\"Drop Earrings with Cubic Zirconia\",\"longDescription\":\"Day or night, these glittering drop earrings are sure to add an extra dose of sparkle to your look. Pear-cut cubic zirconia drop from round-cut stones for an effortlessly chic set of earrings. Pieces measure 3/4 by 5/16 inches.\",\"skus\":[{\"priceCurrent\":240.0,\"priceMsrp\":619.0,\"priceNet\":240.0,\"priceClientRetail\":240.0,\"priceClientMsrp\":619.0,\"sku\":\"00395\",\"size\":\"OneSize\",\"barcode\":\"0763397003954\",\"clientSku\":\"00395\",\"image\":[\"http://max1.jewelrystatic.com/media/catalog/product/0/0/00395_main.jpg\",\"http://max1.jewelrystatic.com/media/catalog/product/0/0/00395_a1.jpg\"],\"qty\":154,\"weightOrg\":\"0\",\"weightOrgUnit\":\"lb\",\"attribute\":{}}],\"attribute\":{\"MSRP\":[\"619.00\"],\"StoneColor\":[\"White\"],\"Manufacturermodel\":[\"00395\"],\"CountryOfOrigin\":[\"US\"],\"Gender\":[\"Women\\u0027s\"],\"Stone Length\":[\"0\"],\"Lab Created\":[\"Y\"],\"Stone\":[\"Cubic Zirconia\"],\"Stone Width\":[\"0\"],\"isClearance\":[\"FALSE\"],\"Launch Product\":[\"FALSE\"],\"SettingTypeDesc\":[\"BLANK\"],\"Age Group\":[\"Adult\"],\"Style\":[\"Drops\"],\"NominalScaleWeight\":[\"0\"],\"ItemCreationDate\":[\"2015-01-05\"],\"TotalMetalWeight\":[\"0.0000\"],\"MetalType\":[\"Sterling Silver\"],\"Gem Creation Method\":[\"Created\"],\"ItemClassification\":[\"Earrings\"],\"MerchantCategory\":[\"Earrings - Drops - Cubic Zirconia\"],\"Popularity\":[\"30\"],\"ShapeCut\":[\"Pear\"],\"VisibilityName\":[\"Catalog, Search\"],\"MagicPrice1\":[\"0.00\"],\"MagicPrice0\":[\"0.00\"],\"Active\":[\"TRUE\"],\"MetalStamp\":[\"925\"],\"Bullet Point 2\":[\"Crafted in Sterling Silver\"],\"MagicPrice3\":[\"0.00\"],\"Bullet Point 3\":[\"Pieces Measure 3/4 x 5/16 Inches\"],\"ITEMTHUMBURL\":[\"http://www.jewelry.com/channeladvisor_products.php/image.php?sku\\u003d00395\\u0026w\\u003d200\\u0026h\\u003d200\"],\"MagicPrice2\":[\"0.00\"],\"PieceID\":[\"14970\"],\"Bullet Point 1\":[\"Adorned with Cubic Zirconia\"],\"MetalColor\":[\"White\"],\"MagicPrice9\":[\"0.00\"],\"MagicPrice8\":[\"0.00\"],\"ActionURL\":[\"http://www.jewelry.com/drop-earrings-with-cubic-zirconia-in-sterling-silver-sku-00395\"],\"Visibility\":[\"4\"],\"MagicPrice5\":[\"0.00\"],\"ChainLength\":[\"0.0000\"],\"MagicPrice4\":[\"0.00\"],\"BackFinding\":[\"Fish Hook\"],\"MagicPrice7\":[\"0.00\"],\"ManufacturingPolicy\":[\"Make-to-Stock\"],\"MagicPrice6\":[\"0.00\"]},\"attributeVms\":{},\"fullAttribute\":{},\"updFlg\":0,\"updMessage\":\"\",\"clientProductURL\":\"http://www.jewelry.com/drop-earrings-with-cubic-zirconia-in-sterling-silver-sku-00395\",\"qty\":0,\"usageEn\":\"\",\"productType\":\"Earrings\",\"material\":\"MetalType 1 :Sterling Silver；Stone 2:0\",\"lastReceivedOn\":\"\",\"mpn\":\"00395\",\"channelId\":\"010\",\"created\":\"2017-03-27 14:20:44\",\"creater\":\"0\",\"modified\":\"2017-03-27 14:20:44.547\",\"modifier\":\"0\"}],\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"VmsJEAnalysisJob\"}";
        CmsFeedImportMQMessageBody messageBody = JacksonUtil.json2Bean(msg1, CmsFeedImportMQMessageBody.class);
        cmsFeedImportMQJob.onStartup(messageBody);
    }
}