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

        String msg1= "{\n" +
                "  \"consumerRetryTimes\": 0,\n" +
                "  \"mqId\": 0,\n" +
                "  \"delaySecond\": 0,\n" +
                "  \"sender\": \"VmsGiltAnalysisJob\",\n" +
                "  \"channelId\": \"028\",\n" +
                "  \"cmsBtFeedInfoModels\": [\n" +
                "    {\n" +
                "      \"_id\": null,\n" +
                "      \"created\": \"2017-05-12 17:59:06\",\n" +
                "      \"creater\": \"0\",\n" +
                "      \"modified\": \"2017-05-12 17:59:06.52\",\n" +
                "      \"modifier\": \"0\",\n" +
                "      \"channelId\": \"028\",\n" +
                "      \"catId\": null,\n" +
                "      \"category\": \"Apparel-Women apparel-Denim-Skirts\",\n" +
                "      \"mainCategoryEn\": null,\n" +
                "      \"mainCategoryCn\": null,\n" +
                "      \"code\": \"james-li\",\n" +
                "      \"name\": \"Cindy Button Front Denim Skirt\",\n" +
                "      \"model\": \"1150492584\",\n" +
                "      \"color\": \"denim blue\",\n" +
                "      \"origin\": \"CHN\",\n" +
                "      \"sizeType\": \"Women's Dresses & Skirts\",\n" +
                "      \"sizeChartType\": null,\n" +
                "      \"image\": [\n" +
                "        \"https://cdn5.giltcdn.com/images/share/uploads/0000/0005/0840/508403748/orig.jpg\",\n" +
                "        \"https://cdn5.giltcdn.com/images/share/uploads/0000/0005/0840/508403751/orig.jpg\",\n" +
                "        \"https://cdn5.giltcdn.com/images/share/uploads/0000/0005/0840/508403757/orig.jpg\"\n" +
                "      ],\n" +
                "      \"brand\": \"Rachel Antonoff\",\n" +
                "      \"weight\": \"\",\n" +
                "      \"shortDescription\": \"\",\n" +
                "      \"longDescription\": \"Woven cotton skirt<br>* A-line hem<br>* Tonal topstitching and panel seaming <br>* Front button closure<br><b><br>**Measurements:**</b><br>* 33\\\" from waist to hem; taken from a size 4<br>* Model's height is 5 feet 9 inches\",\n" +
                "      \"skus\": [\n" +
                "        {\n" +
                "          \"priceCurrent\": 565.0,\n" +
                "          \"priceMsrp\": 1499.0,\n" +
                "          \"priceNet\": 7011.45,\n" +
                "          \"priceClientRetail\": 215.0,\n" +
                "          \"priceClientMsrp\": 215.0,\n" +
                "          \"sku\": \"614707811\",\n" +
                "          \"size\": \"4 Women's US\",\n" +
                "          \"barcode\": \"1000061470780\",\n" +
                "          \"clientSku\": \"6147078\",\n" +
                "          \"image\": [\n" +
                "            \"https://cdn5.giltcdn.com/images/share/uploads/0000/0005/0840/508403748/orig.jpg\",\n" +
                "            \"https://cdn5.giltcdn.com/images/share/uploads/0000/0005/0840/508403751/orig.jpg\",\n" +
                "            \"https://cdn5.giltcdn.com/images/share/uploads/0000/0005/0840/508403757/orig.jpg\"\n" +
                "          ],\n" +
                "          \"qty\": 4,\n" +
                "          \"relationshipType\": null,\n" +
                "          \"variationTheme\": null,\n" +
                "          \"weightOrg\": \"4\",\n" +
                "          \"weightOrgUnit\": \"lb\",\n" +
                "          \"weightCalc\": null,\n" +
                "          \"isSale\": 1,\n" +
                "          \"mainVid\": \"\",\n" +
                "          \"attribute\": {\n" +
                "            \n" +
                "          },\n" +
                "          \"errInfo\": null\n" +
                "        },\n" +
                "        {\n" +
                "          \"priceCurrent\": 565.0,\n" +
                "          \"priceMsrp\": 1499.0,\n" +
                "          \"priceNet\": 60.45,\n" +
                "          \"priceClientRetail\": 215.0,\n" +
                "          \"priceClientMsrp\": 215.0,\n" +
                "          \"sku\": \"6147079\",\n" +
                "          \"size\": \"6 Women's US\",\n" +
                "          \"barcode\": \"1000061470797\",\n" +
                "          \"clientSku\": \"6147079\",\n" +
                "          \"image\": [\n" +
                "            \"https://cdn5.giltcdn.com/images/share/uploads/0000/0005/0840/508403748/orig.jpg\",\n" +
                "            \"https://cdn5.giltcdn.com/images/share/uploads/0000/0005/0840/508403751/orig.jpg\",\n" +
                "            \"https://cdn5.giltcdn.com/images/share/uploads/0000/0005/0840/508403757/orig.jpg\"\n" +
                "          ],\n" +
                "          \"qty\": 1,\n" +
                "          \"relationshipType\": null,\n" +
                "          \"variationTheme\": null,\n" +
                "          \"weightOrg\": \"4\",\n" +
                "          \"weightOrgUnit\": \"lb\",\n" +
                "          \"weightCalc\": null,\n" +
                "          \"isSale\": 1,\n" +
                "          \"mainVid\": \"\",\n" +
                "          \"attribute\": {\n" +
                "            \n" +
                "          },\n" +
                "          \"errInfo\": null\n" +
                "        },\n" +
                "        {\n" +
                "          \"priceCurrent\": 565.0,\n" +
                "          \"priceMsrp\": 1499.0,\n" +
                "          \"priceNet\": 160.45,\n" +
                "          \"priceClientRetail\": 215.0,\n" +
                "          \"priceClientMsrp\": 215.0,\n" +
                "          \"sku\": \"028-4442808\",\n" +
                "          \"size\": \"10 Women's US\",\n" +
                "          \"barcode\": \"1000061470810\",\n" +
                "          \"clientSku\": \"6147081\",\n" +
                "          \"image\": [\n" +
                "            \"https://cdn5.giltcdn.com/images/share/uploads/0000/0005/0840/508403748/orig.jpg\",\n" +
                "            \"https://cdn5.giltcdn.com/images/share/uploads/0000/0005/0840/508403751/orig.jpg\",\n" +
                "            \"https://cdn5.giltcdn.com/images/share/uploads/0000/0005/0840/508403757/orig.jpg\"\n" +
                "          ],\n" +
                "          \"qty\": 2,\n" +
                "          \"relationshipType\": null,\n" +
                "          \"variationTheme\": null,\n" +
                "          \"weightOrg\": \"4\",\n" +
                "          \"weightOrgUnit\": \"lb\",\n" +
                "          \"weightCalc\": null,\n" +
                "          \"isSale\": 1,\n" +
                "          \"mainVid\": \"\",\n" +
                "          \"attribute\": {\n" +
                "            \n" +
                "          },\n" +
                "          \"errInfo\": null\n" +
                "        },\n" +
                "        {\n" +
                "          \"priceCurrent\": 565.0,\n" +
                "          \"priceMsrp\": 1499.0,\n" +
                "          \"priceNet\": 60.45,\n" +
                "          \"priceClientRetail\": 215.0,\n" +
                "          \"priceClientMsrp\": 215.0,\n" +
                "          \"sku\": \"6147082\",\n" +
                "          \"size\": \"12 Women's US\",\n" +
                "          \"barcode\": \"1000061470827\",\n" +
                "          \"clientSku\": \"6147082\",\n" +
                "          \"image\": [\n" +
                "            \"https://cdn5.giltcdn.com/images/share/uploads/0000/0005/0840/508403748/orig.jpg\",\n" +
                "            \"https://cdn5.giltcdn.com/images/share/uploads/0000/0005/0840/508403751/orig.jpg\",\n" +
                "            \"https://cdn5.giltcdn.com/images/share/uploads/0000/0005/0840/508403757/orig.jpg\"\n" +
                "          ],\n" +
                "          \"qty\": 1,\n" +
                "          \"relationshipType\": null,\n" +
                "          \"variationTheme\": null,\n" +
                "          \"weightOrg\": \"4\",\n" +
                "          \"weightOrgUnit\": \"lb\",\n" +
                "          \"weightCalc\": null,\n" +
                "          \"isSale\": 1,\n" +
                "          \"mainVid\": \"\",\n" +
                "          \"attribute\": {\n" +
                "            \n" +
                "          },\n" +
                "          \"errInfo\": null\n" +
                "        }\n" +
                "      ],\n" +
                "      \"attributeList\": null,\n" +
                "      \"attribute\": {\n" +
                "        \"attributes_style_name\": [\n" +
                "          \"ra3165052.db\"\n" +
                "        ],\n" +
                "        \"attributes_size_type\": [\n" +
                "          \"Women's Dresses & Skirts\"\n" +
                "        ],\n" +
                "        \"categories_name\": [\n" +
                "          \"Skirts\"\n" +
                "        ],\n" +
                "        \"attributes_material_value\": [\n" +
                "          \"100% cotton\"\n" +
                "        ],\n" +
                "        \"attributes_size_size_chart_id\": [\n" +
                "          \"14691\"\n" +
                "        ],\n" +
                "        \"name\": [\n" +
                "          \"Cindy Button Front Denim Skirt\"\n" +
                "        ]\n" +
                "      },\n" +
                "      \"attributeVms\": {\n" +
                "        \n" +
                "      },\n" +
                "      \"fullAttribute\": {\n" +
                "        \n" +
                "      },\n" +
                "      \"updFlg\": 0,\n" +
                "      \"updMessage\": \"\",\n" +
                "      \"clientProductURL\": \"\",\n" +
                "      \"qty\": 0,\n" +
                "      \"usageEn\": \"\",\n" +
                "      \"isFeedReImport\": null,\n" +
                "      \"productType\": \"Skirts\",\n" +
                "      \"material\": \"\",\n" +
                "      \"lastReceivedOn\": \"\",\n" +
                "      \"mpn\": \"ra3165052.db\",\n" +
                "      \"catConf\": \"\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"subBeanName\": \"015\"\n" +
                "}";
        CmsFeedImportMQMessageBody messageBody = JacksonUtil.json2Bean(msg1, CmsFeedImportMQMessageBody.class);
        cmsFeedImportMQJob.onStartup(messageBody);
    }
}