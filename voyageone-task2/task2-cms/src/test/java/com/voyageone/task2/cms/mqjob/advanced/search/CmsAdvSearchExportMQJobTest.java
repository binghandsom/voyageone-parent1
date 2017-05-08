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
        String json = "{\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"james\",\"channelId\":\"018\",\"cmsBtExportTaskId\":13203,\"searchValue\":{\"compareType\":null,\"brand\":null,\"tags\":[],\"priceChgFlg\":null,\"priceDiffFlg\":null,\"tagTypeSelectValue\":\"0\",\"promotionList\":[],\"catgoryList\":[],\"cidValue\":[],\"promotionTagType\":1,\"freeTagType\":1,\"supplierType\":1,\"brandSelType\":1,\"mCatPathType\":1,\"fCatPathType\":1,\"shopCatType\":1,\"pCatPathType\":1,\"productSelType\":\"1\",\"sizeSelType\":\"1\",\"salesType\":\"All\",\"custGroupType\":\"1\",\"cartId\":23,\"productStatus\":[],\"platformStatus\":[],\"pRealStatus\":[],\"hasErrorFlg\":null,\"promotionTags\":null,\"salesSortType\":null,\"salesStart\":null,\"salesEnd\":null,\"pCatId\":null,\"pCatPath\":null,\"pCatStatus\":0,\"shopCatStatus\":0,\"publishTimeStart\":null,\"publishTimeTo\":null,\"priceEnd\":null,\"priceStart\":null,\"priceType\":null,\"propertyStatus\":null,\"pCatPathList\":[],\"custAttrMap\":[{\"inputVal\":\"\",\"inputOpts\":\"\",\"inputOptsKey\":\"\"}],\"_selCodeList\":[],\"fileType\":2,\"_channleId\":\"018\",\"_userName\":\"james\",\"_language\":\"cn\",\"_taskId\":13203,\"_sessionBean\":{\"_adv_search_customProps\":[],\"_adv_search_selPlatformDataList\":[],\"_adv_search_commonProps\":[{\"valType\":\"string\",\"propId\":\"clientMsrpPrice\",\"propName\":\"客户建议零售价\"}],\"_adv_search_props_searchItems\":\"common.fields.clientMsrpPrice;\",\"_adv_search_selSalesType\":[{\"name\":\"7天销量\",\"value\":\"sales.codeSum7.cartId0\"},{\"name\":\"30天销量\",\"value\":\"sales.codeSum30.cartId0\"},{\"name\":\"YTD销量\",\"value\":\"sales.codeSumYear.cartId0\"},{\"name\":\"总销量\",\"value\":\"sales.codeSumAll.cartId0\"},{\"name\":\"天猫国际7天销量\",\"value\":\"sales.codeSum7.cartId23\"},{\"name\":\"天猫国际30天销量\",\"value\":\"sales.codeSum30.cartId23\"},{\"name\":\"天猫国际YTD销量\",\"value\":\"sales.codeSumYear.cartId23\"},{\"name\":\"天猫国际总销量\",\"value\":\"sales.codeSumAll.cartId23\"}],\"_adv_search_selBiDataList\":[{\"name\":\"天猫国际1浏览量\",\"value\":\"bi.sum1.pv.cartId23\"},{\"name\":\"天猫国际1访客数\",\"value\":\"bi.sum1.uv.cartId23\"},{\"name\":\"天猫国际1加购件数\",\"value\":\"bi.sum1.gwc.cartId23\"},{\"name\":\"天猫国际1收藏人数\",\"value\":\"bi.sum1.scs.cartId23\"},{\"name\":\"天猫国际7浏览量\",\"value\":\"bi.sum7.pv.cartId23\"},{\"name\":\"天猫国际7访客数\",\"value\":\"bi.sum7.uv.cartId23\"},{\"name\":\"天猫国际7加购件数\",\"value\":\"bi.sum7.gwc.cartId23\"},{\"name\":\"天猫国际7收藏人数\",\"value\":\"bi.sum7.scs.cartId23\"},{\"name\":\"天猫国际30浏览量\",\"value\":\"bi.sum30.pv.cartId23\"},{\"name\":\"天猫国际30访客数\",\"value\":\"bi.sum30.uv.cartId23\"},{\"name\":\"天猫国际30加购件数\",\"value\":\"bi.sum30.gwc.cartId23\"},{\"name\":\"天猫国际30收藏人数\",\"value\":\"bi.sum30.scs.cartId23\"},{\"name\":\"天猫国际商品发布时间\",\"value\":\"platforms.P23.pPublishTime\"},{\"name\":\"天猫国际中国建议售价\",\"value\":\"platforms.P23.pPriceMsrpEd\"},{\"name\":\"天猫国际中国指导售价\",\"value\":\"platforms.P23.pPriceRetailSt\"},{\"name\":\"天猫国际中国最终售价\",\"value\":\"platforms.P23.pPriceSaleEd\"}]}},\"channelIdMap\":{\"030\":\"Wella\",\"031\":\"WoodLand\",\"010\":\"Jewelry\",\"032\":\"Frye\",\"033\":\"KitBag\",\"012\":\"BCBG\",\"034\":\"Coty\",\"013\":\"Sears\",\"035\":\"LikingBuyer\",\"014\":\"WMF\",\"036\":\"Cinxus\",\"015\":\"GILT\",\"037\":\"SharonShoe\",\"016\":\"ShoeCity\",\"038\":\"FAMbrand\",\"017\":\"Lucky Vitamin\",\"039\":\"Ladolcevitae\",\"018\":\"Target\",\"019\":\"SummerGuru\",\"040\":\"AuthenticGlasses\",\"020\":\"EdcSkincare\",\"021\":\"BHFOX\",\"022\":\"DFO\",\"001\":\"Sneakerhead\",\"023\":\"ShoeZoo\",\"002\":\"PortAmerican\",\"024\":\"Overstock\",\"003\":\"Essuntial\",\"025\":\"FragranceNet\",\"004\":\"JuicyCouture\",\"026\":\"Lighthouse\",\"005\":\"Spalding\",\"027\":\"Yogademocracy\",\"006\":\"BHFO\",\"028\":\"ShoeMetro\",\"007\":\"Champion\",\"029\":\"Modotex\",\"008\":\"RealMadrid\",\"009\":\"SwissWatch\",\"928\":\"Liking\"}}";

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