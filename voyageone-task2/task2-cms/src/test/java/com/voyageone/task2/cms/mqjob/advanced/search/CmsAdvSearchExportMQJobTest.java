package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchExportMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
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

    @Autowired
    private ProductService productService;

    @Test
    public void onStartup() throws Exception {
        String json = "{\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"james\",\"channelId\":\"001\",\"cmsBtExportTaskId\":13211,\"searchValue\":{\"compareType\":null,\"brand\":null,\"tags\":[],\"priceChgFlg\":\"\",\"priceDiffFlg\":\"\",\"tagTypeSelectValue\":\"0\",\"promotionList\":[],\"catgoryList\":[],\"cidValue\":[],\"promotionTagType\":1,\"freeTagType\":1,\"supplierType\":1,\"brandSelType\":1,\"mCatPathType\":1,\"fCatPathType\":1,\"shopCatType\":1,\"pCatPathType\":1,\"productSelType\":\"1\",\"sizeSelType\":\"1\",\"salesType\":\"All\",\"custGroupType\":\"1\",\"custAttrMap\":[{\"inputVal\":\"\",\"inputOpts\":\"\",\"inputOptsKey\":\"\"}],\"_selCodeList\":[\"68220-gem\",\"014312\",\"68303-gem\",\"g86569\"],\"fileType\":3,\"productStatus\":[],\"platformStatus\":[],\"pRealStatus\":[],\"shopCatStatus\":0,\"pCatStatus\":0,\"_channleId\":\"001\",\"_userName\":\"james\",\"_language\":\"cn\",\"_taskId\":13211,\"_sessionBean\":{\"_adv_search_customProps\":[],\"_adv_search_selPlatformDataList\":[],\"_adv_search_commonProps\":[{\"valType\":\"string\",\"propId\":\"clientMsrpPrice\",\"propName\":\"客户建议零售价\"},{\"valType\":\"string\",\"propId\":\"clientNetPrice\",\"propName\":\"客户成本价\"},{\"valType\":\"string\",\"propId\":\"brand\",\"propName\":\"品牌\"},{\"valType\":\"string\",\"propId\":\"model\",\"propName\":\"款号\"},{\"valType\":\"string\",\"propId\":\"materialEn\",\"propName\":\"材质英语\"},{\"valType\":\"string\",\"propId\":\"materialCn\",\"propName\":\"材质中文\"},{\"valType\":\"DOUBLE\",\"propId\":\"priceMsrp\",\"propName\":\"中国建议售价\"},{\"valType\":\"DOUBLE\",\"propId\":\"priceRetail\",\"propName\":\"中国指导售价\"},{\"valType\":\"DOUBLE\",\"propId\":\"priceSale\",\"propName\":\"中国最终售价\"},{\"valType\":\"number\",\"propId\":\"skuCnt\",\"propName\":\"SKU数\"}],\"_adv_search_props_searchItems\":\"common.fields.clientMsrpPrice;common.fields.clientNetPrice;common.fields.brand;common.fields.model;common.fields.materialEn;common.fields.materialCn;common.fields.priceMsrp;common.fields.priceRetail;common.fields.priceSale;common.fields.skuCnt;\",\"_adv_search_selSalesType\":[],\"_adv_search_selBiDataList\":[]}},\"channelIdMap\":{\"030\":\"Wella\",\"031\":\"WoodLand\",\"010\":\"Jewelry\",\"032\":\"Frye\",\"033\":\"KitBag\",\"012\":\"BCBG\",\"034\":\"Coty\",\"013\":\"Sears\",\"035\":\"LikingBuyer\",\"014\":\"WMF\",\"036\":\"Cinxus\",\"015\":\"GILT\",\"037\":\"SharonShoe\",\"016\":\"ShoeCity\",\"038\":\"FAMbrand\",\"017\":\"Lucky Vitamin\",\"039\":\"Ladolcevitae\",\"018\":\"Target\",\"019\":\"SummerGuru\",\"040\":\"AuthenticGlasses\",\"020\":\"EdcSkincare\",\"021\":\"BHFOX\",\"022\":\"DFO\",\"001\":\"Sneakerhead\",\"023\":\"ShoeZoo\",\"002\":\"PortAmerican\",\"024\":\"Overstock\",\"003\":\"Essuntial\",\"025\":\"FragranceNet\",\"004\":\"JuicyCouture\",\"026\":\"Lighthouse\",\"005\":\"Spalding\",\"027\":\"Yogademocracy\",\"006\":\"BHFO\",\"028\":\"ShoeMetro\",\"007\":\"Champion\",\"029\":\"Modotex\",\"008\":\"RealMadrid\",\"009\":\"SwissWatch\",\"928\":\"Liking\"}}";

        AdvSearchExportMQMessageBody advSearchExportMQMessageBody = JacksonUtil.json2Bean(json, AdvSearchExportMQMessageBody.class);
        cmsAdvSearchExportMQJob.onStartup(advSearchExportMQMessageBody);

    }

    @Test
    public void test() {
        CmsBtProductModel cmsBtProductModel = productService.getProductByCode("007", "C5-P301-010");
        ExpressionParser parser = new SpelExpressionParser();

        Expression expression = parser.parseExpression("platforms[\"P23\"].fields[\"title\"]");

        StandardEvaluationContext context = new StandardEvaluationContext(cmsBtProductModel);

        Object price = expression.getValue(context);

        System.out.println(price);

    }

}