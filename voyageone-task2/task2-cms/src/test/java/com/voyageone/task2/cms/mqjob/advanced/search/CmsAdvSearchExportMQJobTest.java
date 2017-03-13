package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.common.configs.MQConfigInitTestUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchExportMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by rex on 2017/1/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsAdvSearchExportMQJobTest {

    @Autowired
    private CmsAdvSearchExportMQJob cmsAdvSearchExportMQJob;

    @Test
    public void onStartup() throws Exception {
        String json = "{\"cmsBtExportTaskId\":10878,\"searchValue\":{\"tags\":[],\"priceChgFlg\":\"\",\"priceDiffFlg\":\"\",\"tagTypeSelectValue\":\"0\",\"promotionList\":[],\"catgoryList\":[],\"cidValue\":[],\"promotionTagType\":1,\"freeTagType\":1,\"supplierType\":1,\"brandSelType\":1,\"productSelType\":\"1\",\"sizeSelType\":\"1\",\"salesType\":\"All\",\"custGroupType\":\"1\",\"freeTags\":[\"-11768-\"],\"custAttrMap\":[{\"inputVal\":\"\",\"inputOpts\":\"\",\"inputOptsKey\":\"\"}],\"_selCodeList\":[],\"fileType\":1,\"productStatus\":[],\"platformStatus\":[],\"pRealStatus\":[],\"shopCatStatus\":0,\"pCatStatus\":0,\"_channleId\":\"928\",\"_userName\":\"james\",\"_language\":\"cn\",\"_taskId\":10878,\"_sessionBean\":{\"_adv_search_customProps\":[],\"_adv_search_commonProps\":[{\"valType\":\"string\",\"propId\":\"codeDiff\",\"propName\":\"原始 颜色/口味/香型等\"},{\"valType\":\"string\",\"propId\":\"color\",\"propName\":\"颜色/口味/香型等\"},{\"valType\":\"list-2\",\"propId\":\"productType\",\"propName\":\"产品分类\"},{\"valType\":\"list-2\",\"propId\":\"sizeType\",\"propName\":\"适用人群\"},{\"valType\":\"list-2\",\"propId\":\"productTypeCn\",\"propName\":\"产品分类(中文)\"},{\"valType\":\"list-2\",\"propId\":\"sizeTypeCn\",\"propName\":\"适用人群(中文)\"},{\"valType\":\"list-2\",\"propId\":\"hsCodeCrop\",\"propName\":\"税号集货\"},{\"valType\":\"list-2\",\"propId\":\"hsCodePrivate\",\"propName\":\"税号个人\"},{\"valType\":\"string\",\"propId\":\"origin\",\"propName\":\"产地\"},{\"valType\":\"string\",\"propId\":\"translator\",\"propName\":\"翻译者\"},{\"valType\":\"string\",\"propId\":\"weightLB\",\"propName\":\"重量(lb)\"},{\"valType\":\"string\",\"propId\":\"weightG\",\"propName\":\"重量(克)\"},{\"valType\":\"string\",\"propId\":\"weightKG\",\"propName\":\"重量(千克)\"}],\"_adv_search_props_searchItems\":\"common.fields.codeDiff;common.fields.color;common.fields.productType;common.fields.sizeType;common.fields.productTypeCn;common.fields.sizeTypeCn;common.fields.hsCodeCrop;common.fields.hsCodePrivate;common.fields.origin;common.fields.translator;common.fields.weightLB;common.fields.weightG;common.fields.weightKG;\",\"_adv_search_selSalesType\":[]}},\"channelIdMap\":{\"092\":\"channel092\",\"093\":\"channel093\",\"030\":\"Wella\",\"031\":\"WoodLand\",\"032\":\"Frye\",\"010\":\"Jewelry\",\"033\":\"KitBag\",\"034\":\"Coty\",\"012\":\"BCBG\",\"013\":\"Sears\",\"035\":\"LikingBuyer\",\"014\":\"WMF\",\"015\":\"GILT\",\"016\":\"ShoeCity\",\"996\":\"Test\",\"017\":\"Lucky Vitamin\",\"997\":\"VO\",\"018\":\"Target\",\"998\":\"TestUsJoi\",\"019\":\"SummerGuru\",\"020\":\"EdcSkincare\",\"021\":\"BHFOX\",\"022\":\"DFO\",\"000\":\"all Channel\",\"001\":\"Sneakerhead\",\"023\":\"ShoeZoo\",\"024\":\"Overstock\",\"002\":\"PortAmerican\",\"003\":\"Essuntial\",\"025\":\"FragranceNet\",\"004\":\"JuicyCouture\",\"026\":\"Lighthouse\",\"027\":\"Yogademocracy\",\"005\":\"Spalding\",\"006\":\"BHFO\",\"028\":\"ShoeMetro\",\"007\":\"Champion\",\"029\":\"Modotex\",\"008\":\"RealMadrid\",\"009\":\"SwissWatch\",\"928\":\"Liking\"},\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"james\"}";
        AdvSearchExportMQMessageBody advSearchExportMQMessageBody = JacksonUtil.json2Bean(json, AdvSearchExportMQMessageBody.class);
        cmsAdvSearchExportMQJob.onStartup(advSearchExportMQMessageBody);
//        MQConfigInitTestUtil.startMQ(cmsAdvSearchExportMQJob);

    }

}