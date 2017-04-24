package com.voyageone.task2.cms.mqjob.advanced.search;

import com.taobao.api.domain.Product;
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

import java.math.BigDecimal;

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
        String json = "{\n" +
                "\t\"cmsBtExportTaskId\": 268,\n" +
                "\t\"searchValue\": {\n" +
                "\t\t\"tags\": [],\n" +
                "\t\t\"priceChgFlg\": \"\",\n" +
                "\t\t\"priceDiffFlg\": \"\",\n" +
                "\t\t\"tagTypeSelectValue\": \"0\",\n" +
                "\t\t\"promotionList\": [],\n" +
                "\t\t\"catgoryList\": [],\n" +
                "\t\t\"cidValue\": [],\n" +
                "\t\t\"promotionTagType\": 1,\n" +
                "\t\t\"freeTagType\": 1,\n" +
                "\t\t\"supplierType\": 1,\n" +
                "\t\t\"brandSelType\": 1,\n" +
                "\t\t\"productSelType\": \"1\",\n" +
                "\t\t\"sizeSelType\": \"1\",\n" +
                "\t\t\"salesType\": \"All\",\n" +
                "\t\t\"custGroupType\": \"1\",\n" +
                "\t\t\"custAttrMap\": [{\n" +
                "\t\t\t\"inputVal\": \"\",\n" +
                "\t\t\t\"inputOpts\": \"\",\n" +
                "\t\t\t\"inputOptsKey\": \"\"\n" +
                "\t\t}],\n" +
                "\t\t\"_selCodeList\": [\"51A0HC13E1-00LCNB0\"],\n" +
                "\t\t\"fileType\": 1,\n" +
                "\t\t\"productStatus\": [],\n" +
                "\t\t\"platformStatus\": [],\n" +
                "\t\t\"pRealStatus\": [],\n" +
                "\t\t\"shopCatStatus\": 0,\n" +
                "\t\t\"pCatStatus\": 0,\n" +
                "\t\t\"_channleId\": \"010\",\n" +
                "\t\t\"_userName\": \"edward\",\n" +
                "\t\t\"_language\": \"cn\",\n" +
                "\t\t\"_taskId\": 268,\n" +
                "\t\t\"_sessionBean\": {\n" +
                "\t\t\t\"_adv_search_customProps\": [],\n" +
                "\t\t\t\"_adv_search_commonProps\": [],\n" +
                "\t\t\t\"_adv_search_props_searchItems\": \"\",\n" +
                "\t\t\t\"_adv_search_selSalesType\": []\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"channelIdMap\": {\n" +
                "\t\t\"030\": \"Wella\",\n" +
                "\t\t\"031\": \"WoodLand\",\n" +
                "\t\t\"010\": \"Jewelry\",\n" +
                "\t\t\"032\": \"Frye\",\n" +
                "\t\t\"033\": \"KitBag\",\n" +
                "\t\t\"012\": \"BCBG\",\n" +
                "\t\t\"034\": \"Coty\",\n" +
                "\t\t\"013\": \"Sears\",\n" +
                "\t\t\"035\": \"LikingBuyer\",\n" +
                "\t\t\"014\": \"WMF\",\n" +
                "\t\t\"015\": \"GILT\",\n" +
                "\t\t\"016\": \"ShoeCity\",\n" +
                "\t\t\"017\": \"Lucky Vitamin\",\n" +
                "\t\t\"018\": \"Target\",\n" +
                "\t\t\"019\": \"SummerGuru\",\n" +
                "\t\t\"020\": \"EdcSkincare\",\n" +
                "\t\t\"021\": \"BHFOX\",\n" +
                "\t\t\"022\": \"DFO\",\n" +
                "\t\t\"001\": \"Sneakerhead\",\n" +
                "\t\t\"023\": \"ShoeZoo\",\n" +
                "\t\t\"002\": \"PortAmerican\",\n" +
                "\t\t\"024\": \"Overstock\",\n" +
                "\t\t\"003\": \"Essuntial\",\n" +
                "\t\t\"025\": \"FragranceNet\",\n" +
                "\t\t\"004\": \"JuicyCouture\",\n" +
                "\t\t\"026\": \"Lighthouse\",\n" +
                "\t\t\"005\": \"Spalding\",\n" +
                "\t\t\"027\": \"Yogademocracy\",\n" +
                "\t\t\"006\": \"BHFO\",\n" +
                "\t\t\"028\": \"ShoeMetro\",\n" +
                "\t\t\"007\": \"Champion\",\n" +
                "\t\t\"029\": \"Modotex\",\n" +
                "\t\t\"008\": \"RealMadrid\",\n" +
                "\t\t\"009\": \"SwissWatch\",\n" +
                "\t\t\"928\": \"Liking\"\n" +
                "\t},\n" +
                "\t\"consumerRetryTimes\": 0,\n" +
                "\t\"mqId\": 0,\n" +
                "\t\"delaySecond\": 0,\n" +
                "\t\"sender\": \"edward\"\n" +
                "}";

        AdvSearchExportMQMessageBody advSearchExportMQMessageBody = JacksonUtil.json2Bean(json, AdvSearchExportMQMessageBody.class);
        cmsAdvSearchExportMQJob.onStartup(advSearchExportMQMessageBody);

    }

    @Test
    public void test(){
        CmsBtProductModel cmsBtProductModel = productService.getProductByCode("007","C5-P301-010");
        ExpressionParser parser = new SpelExpressionParser();

        Expression expression = parser.parseExpression("platforms[\"P23\"].fields[\"title\"]");

        StandardEvaluationContext context = new StandardEvaluationContext(cmsBtProductModel);

        Object price = expression.getValue(context);

        System.out.println(price);

    }

}