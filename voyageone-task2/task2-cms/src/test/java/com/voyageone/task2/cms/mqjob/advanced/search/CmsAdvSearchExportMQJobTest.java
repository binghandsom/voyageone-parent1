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
                "    \"cmsBtExportTaskId\": 276,\n" +
                "    \"searchValue\": {\n" +
                "        \"tags\": [],\n" +
                "        \"priceChgFlg\": \"\",\n" +
                "        \"priceDiffFlg\": \"\",\n" +
                "        \"tagTypeSelectValue\": \"0\",\n" +
                "        \"promotionList\": [],\n" +
                "        \"catgoryList\": [],\n" +
                "        \"cidValue\": [],\n" +
                "        \"promotionTagType\": 1,\n" +
                "        \"freeTagType\": 1,\n" +
                "        \"supplierType\": 1,\n" +
                "        \"brandSelType\": 1,\n" +
                "        \"productSelType\": \"1\",\n" +
                "        \"sizeSelType\": \"1\",\n" +
                "        \"salesType\": \"All\",\n" +
                "        \"custGroupType\": \"1\",\n" +
                "        \"custAttrMap\": [{\n" +
                "            \"inputVal\": \"\",\n" +
                "            \"inputOpts\": \"\",\n" +
                "            \"inputOptsKey\": \"\"\n" +
                "        }],\n" +
                "        \"_selCodeList\": [\"1117446288\"],\n" +
                "        \"fileType\": 3,\n" +
                "        \"productStatus\": [],\n" +
                "        \"platformStatus\": [],\n" +
                "        \"pRealStatus\": [],\n" +
                "        \"shopCatStatus\": 0,\n" +
                "        \"pCatStatus\": 0,\n" +
                "        \"_channleId\": \"928\",\n" +
                "        \"_userName\": \"edward\",\n" +
                "        \"_language\": \"cn\",\n" +
                "        \"_taskId\": 276,\n" +
                "        \"_sessionBean\": {\n" +
                "            \"_adv_search_customProps\": [{\n" +
                "                \"feed_prop_translation\": \"品牌\",\n" +
                "                \"feed_prop_original\": \"brand\"\n" +
                "            }],\n" +
                "            \"_adv_search_selPlatformDataList\": [{\n" +
                "                \"name\": \"匠心界URL\",\n" +
                "                \"value\": \"platforms.P28.URL\"\n" +
                "            }, {\n" +
                "                \"name\": \"匠心界Numiid\",\n" +
                "                \"value\": \"platforms.P28.pNumIId\"\n" +
                "            }, {\n" +
                "                \"name\": \"匠心界商品名称\",\n" +
                "                \"value\": \"platforms.P28.fields.title\"\n" +
                "            }, {\n" +
                "                \"name\": \"匠心界类目\",\n" +
                "                \"value\": \"platforms.P28.fields.fields.pCatPath\"\n" +
                "            }],\n" +
                "            \"_adv_search_commonProps\": [{\n" +
                "                \"valType\": \"string\",\n" +
                "                \"propId\": \"clientMsrpPrice\",\n" +
                "                \"propName\": \"客户建议零售价\"\n" +
                "            }, {\n" +
                "                \"valType\": \"string\",\n" +
                "                \"propId\": \"clientNetPrice\",\n" +
                "                \"propName\": \"客户成本价\"\n" +
                "            }],\n" +
                "            \"_adv_search_props_searchItems\": \"feed.cnAtts.brand;feed.orgAtts.brand;common.fields.clientMsrpPrice;common.fields.clientNetPrice;\",\n" +
                "            \"_adv_search_selSalesType\": [],\n" +
                "            \"_adv_search_selBiDataList\": [{\n" +
                "                \"name\": \"匠心界1浏览量\",\n" +
                "                \"value\": \"bi.sum1.pv.cartId28\"\n" +
                "            }, {\n" +
                "                \"name\": \"匠心界1访客数\",\n" +
                "                \"value\": \"bi.sum1.uv.cartId28\"\n" +
                "            }, {\n" +
                "                \"name\": \"匠心界1加购件数\",\n" +
                "                \"value\": \"bi.sum1.gwc.cartId28\"\n" +
                "            }, {\n" +
                "                \"name\": \"匠心界1收藏人数\",\n" +
                "                \"value\": \"bi.sum1.scs.cartId28\"\n" +
                "            }, {\n" +
                "                \"name\": \"匠心界7浏览量\",\n" +
                "                \"value\": \"bi.sum7.pv.cartId28\"\n" +
                "            }, {\n" +
                "                \"name\": \"匠心界7访客数\",\n" +
                "                \"value\": \"bi.sum7.uv.cartId28\"\n" +
                "            }, {\n" +
                "                \"name\": \"匠心界7加购件数\",\n" +
                "                \"value\": \"bi.sum7.gwc.cartId28\"\n" +
                "            }, {\n" +
                "                \"name\": \"匠心界7收藏人数\",\n" +
                "                \"value\": \"bi.sum7.scs.cartId28\"\n" +
                "            }, {\n" +
                "                \"name\": \"匠心界30浏览量\",\n" +
                "                \"value\": \"bi.sum30.pv.cartId28\"\n" +
                "            }, {\n" +
                "                \"name\": \"匠心界30访客数\",\n" +
                "                \"value\": \"bi.sum30.uv.cartId28\"\n" +
                "            }, {\n" +
                "                \"name\": \"匠心界30加购件数\",\n" +
                "                \"value\": \"bi.sum30.gwc.cartId28\"\n" +
                "            }, {\n" +
                "                \"name\": \"匠心界30收藏人数\",\n" +
                "                \"value\": \"bi.sum30.scs.cartId28\"\n" +
                "            }, {\n" +
                "                \"name\": \"匠心界商品发布时间\",\n" +
                "                \"value\": \"platforms.P28.pPublishTime\"\n" +
                "            }, {\n" +
                "                \"name\": \"匠心界中国建议售价\",\n" +
                "                \"value\": \"platforms.P28.pPriceMsrpEd\"\n" +
                "            }, {\n" +
                "                \"name\": \"匠心界中国指导售价\",\n" +
                "                \"value\": \"platforms.P28.pPriceRetailSt\"\n" +
                "            }, {\n" +
                "                \"name\": \"匠心界中国最终售价\",\n" +
                "                \"value\": \"platforms.P28.pPriceSaleEd\"\n" +
                "            }, {\n" +
                "                \"name\": \"官网同购1浏览量\",\n" +
                "                \"value\": \"bi.sum1.pv.cartId31\"\n" +
                "            }, {\n" +
                "                \"name\": \"官网同购1访客数\",\n" +
                "                \"value\": \"bi.sum1.uv.cartId31\"\n" +
                "            }, {\n" +
                "                \"name\": \"官网同购1加购件数\",\n" +
                "                \"value\": \"bi.sum1.gwc.cartId31\"\n" +
                "            }, {\n" +
                "                \"name\": \"官网同购1收藏人数\",\n" +
                "                \"value\": \"bi.sum1.scs.cartId31\"\n" +
                "            }, {\n" +
                "                \"name\": \"官网同购7浏览量\",\n" +
                "                \"value\": \"bi.sum7.pv.cartId31\"\n" +
                "            }, {\n" +
                "                \"name\": \"官网同购7访客数\",\n" +
                "                \"value\": \"bi.sum7.uv.cartId31\"\n" +
                "            }, {\n" +
                "                \"name\": \"官网同购7加购件数\",\n" +
                "                \"value\": \"bi.sum7.gwc.cartId31\"\n" +
                "            }, {\n" +
                "                \"name\": \"官网同购7收藏人数\",\n" +
                "                \"value\": \"bi.sum7.scs.cartId31\"\n" +
                "            }, {\n" +
                "                \"name\": \"官网同购30浏览量\",\n" +
                "                \"value\": \"bi.sum30.pv.cartId31\"\n" +
                "            }, {\n" +
                "                \"name\": \"官网同购30访客数\",\n" +
                "                \"value\": \"bi.sum30.uv.cartId31\"\n" +
                "            }, {\n" +
                "                \"name\": \"官网同购30加购件数\",\n" +
                "                \"value\": \"bi.sum30.gwc.cartId31\"\n" +
                "            }, {\n" +
                "                \"name\": \"官网同购30收藏人数\",\n" +
                "                \"value\": \"bi.sum30.scs.cartId31\"\n" +
                "            }, {\n" +
                "                \"name\": \"官网同购商品发布时间\",\n" +
                "                \"value\": \"platforms.P31.pPublishTime\"\n" +
                "            }, {\n" +
                "                \"name\": \"官网同购中国建议售价\",\n" +
                "                \"value\": \"platforms.P31.pPriceMsrpEd\"\n" +
                "            }, {\n" +
                "                \"name\": \"官网同购中国指导售价\",\n" +
                "                \"value\": \"platforms.P31.pPriceRetailSt\"\n" +
                "            }, {\n" +
                "                \"name\": \"官网同购中国最终售价\",\n" +
                "                \"value\": \"platforms.P31.pPriceSaleEd\"\n" +
                "            }, {\n" +
                "                \"name\": \"悦境1浏览量\",\n" +
                "                \"value\": \"bi.sum1.pv.cartId29\"\n" +
                "            }, {\n" +
                "                \"name\": \"悦境1访客数\",\n" +
                "                \"value\": \"bi.sum1.uv.cartId29\"\n" +
                "            }, {\n" +
                "                \"name\": \"悦境1加购件数\",\n" +
                "                \"value\": \"bi.sum1.gwc.cartId29\"\n" +
                "            }, {\n" +
                "                \"name\": \"悦境1收藏人数\",\n" +
                "                \"value\": \"bi.sum1.scs.cartId29\"\n" +
                "            }, {\n" +
                "                \"name\": \"悦境7浏览量\",\n" +
                "                \"value\": \"bi.sum7.pv.cartId29\"\n" +
                "            }, {\n" +
                "                \"name\": \"悦境7访客数\",\n" +
                "                \"value\": \"bi.sum7.uv.cartId29\"\n" +
                "            }, {\n" +
                "                \"name\": \"悦境7加购件数\",\n" +
                "                \"value\": \"bi.sum7.gwc.cartId29\"\n" +
                "            }, {\n" +
                "                \"name\": \"悦境7收藏人数\",\n" +
                "                \"value\": \"bi.sum7.scs.cartId29\"\n" +
                "            }, {\n" +
                "                \"name\": \"悦境30浏览量\",\n" +
                "                \"value\": \"bi.sum30.pv.cartId29\"\n" +
                "            }, {\n" +
                "                \"name\": \"悦境30访客数\",\n" +
                "                \"value\": \"bi.sum30.uv.cartId29\"\n" +
                "            }, {\n" +
                "                \"name\": \"悦境30加购件数\",\n" +
                "                \"value\": \"bi.sum30.gwc.cartId29\"\n" +
                "            }, {\n" +
                "                \"name\": \"悦境30收藏人数\",\n" +
                "                \"value\": \"bi.sum30.scs.cartId29\"\n" +
                "            }, {\n" +
                "                \"name\": \"悦境商品发布时间\",\n" +
                "                \"value\": \"platforms.P29.pPublishTime\"\n" +
                "            }, {\n" +
                "                \"name\": \"悦境中国建议售价\",\n" +
                "                \"value\": \"platforms.P29.pPriceMsrpEd\"\n" +
                "            }, {\n" +
                "                \"name\": \"悦境中国指导售价\",\n" +
                "                \"value\": \"platforms.P29.pPriceRetailSt\"\n" +
                "            }, {\n" +
                "                \"name\": \"悦境中国最终售价\",\n" +
                "                \"value\": \"platforms.P29.pPriceSaleEd\"\n" +
                "            }]\n" +
                "        }\n" +
                "    },\n" +
                "    \"channelIdMap\": {\n" +
                "        \"030\": \"Wella\",\n" +
                "        \"031\": \"WoodLand\",\n" +
                "        \"010\": \"Jewelry\",\n" +
                "        \"032\": \"Frye\",\n" +
                "        \"033\": \"KitBag\",\n" +
                "        \"012\": \"BCBG\",\n" +
                "        \"034\": \"Coty\",\n" +
                "        \"013\": \"Sears\",\n" +
                "        \"035\": \"LikingBuyer\",\n" +
                "        \"014\": \"WMF\",\n" +
                "        \"036\": \"Cinxus\",\n" +
                "        \"015\": \"GILT\",\n" +
                "        \"037\": \"SharonShoe\",\n" +
                "        \"016\": \"ShoeCity\",\n" +
                "        \"038\": \"FAMbrand\",\n" +
                "        \"017\": \"Lucky Vitamin\",\n" +
                "        \"039\": \"Ladolcevitae\",\n" +
                "        \"018\": \"Target\",\n" +
                "        \"019\": \"SummerGuru\",\n" +
                "        \"020\": \"EdcSkincare\",\n" +
                "        \"021\": \"BHFOX\",\n" +
                "        \"022\": \"DFO\",\n" +
                "        \"001\": \"Sneakerhead\",\n" +
                "        \"023\": \"ShoeZoo\",\n" +
                "        \"002\": \"PortAmerican\",\n" +
                "        \"024\": \"Overstock\",\n" +
                "        \"003\": \"Essuntial\",\n" +
                "        \"025\": \"FragranceNet\",\n" +
                "        \"004\": \"JuicyCouture\",\n" +
                "        \"026\": \"Lighthouse\",\n" +
                "        \"005\": \"Spalding\",\n" +
                "        \"027\": \"Yogademocracy\",\n" +
                "        \"006\": \"BHFO\",\n" +
                "        \"028\": \"ShoeMetro\",\n" +
                "        \"007\": \"Champion\",\n" +
                "        \"029\": \"Modotex\",\n" +
                "        \"008\": \"RealMadrid\",\n" +
                "        \"009\": \"SwissWatch\",\n" +
                "        \"928\": \"Liking\"\n" +
                "    },\n" +
                "    \"consumerRetryTimes\": 0,\n" +
                "    \"mqId\": 0,\n" +
                "    \"delaySecond\": 0,\n" +
                "    \"sender\": \"edward\",\n" +
                "    \"channelId\": \"928\"\n" +
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