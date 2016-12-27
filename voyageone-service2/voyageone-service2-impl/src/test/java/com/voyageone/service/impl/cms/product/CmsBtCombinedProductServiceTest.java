package com.voyageone.service.impl.cms.product;

import com.voyageone.common.masterdate.schema.utils.JsonUtil;
import com.voyageone.common.util.JacksonUtil;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dell on 2016/12/26.
 */
public class CmsBtCombinedProductServiceTest {
    @Test
    public void getCombinedProductPlatformDetail() throws Exception {
//        String data = "{\"categoryId\":121478012,\"itemId\":539324303018,\"price\":179,\"quantity\":4971,\"skuList\":[{\"price\":\"179.00\",\"skuId\":3195038479787,\"skuTitle\":\"组合套餐：Aveeno/艾维诺 天然燕麦洗发沐浴二合一 532mL Aveeno/艾维诺 天然燕麦儿童润肤乳\",\"subSkuList\":[{\"outerId\":\"12082570\",\"price\":\"95.0\",\"subItemId\":\"536280580830\"},{\"outerId\":\"13682491\",\"price\":\"59.0\",\"subItemId\":\"536274096505\"}]}],\"subItemList\":[{\"outerId\":\"12082570\",\"ratio\":\"1\",\"subItemId\":\"536280580830\",\"title\":\"Aveeno/艾维诺 12082570 532ml\"},{\"outerId\":\"13682491\",\"ratio\":\"1\",\"subItemId\":\"536274096505\",\"title\":\"Aveeno/艾维诺 13682491 227g\"}],\"title\":\"美国Aveeno艾维诺进口婴幼儿沐浴露洗发水2合1润肤乳液组合Target\"}";


        String data = "{\"categoryId\":121478012,\"itemId\":539324303018,\"price\":179,\"quantity\":4992,\"skuList\":[{\"price\":\"179.00\",\"skuId\":3195038479787,\"skuTitle\":\"组合套餐：Aveeno/艾维诺 天然燕麦洗发沐浴二合一 532mL Aveeno/艾维诺 天然燕麦儿童润肤乳\",\"subSkuList\":[{\"outerId\":\"12082570\",\"price\":\"95.0\",\"subItemId\":\"536280580830\"},{\"outerId\":\"13682491\",\"price\":\"59.0\",\"subItemId\":\"536274096505\"}]}],\"subItemList\":[{\"outerId\":\"12082570\",\"ratio\":\"1\",\"subItemId\":\"536280580830\",\"title\":\"Aveeno/艾维诺 12082570 532ml\"},{\"outerId\":\"13682491\",\"ratio\":\"1\",\"subItemId\":\"536274096505\",\"title\":\"Aveeno/艾维诺 13682491 227g\"}],\"title\":\"美国Aveeno艾维诺进口婴幼儿沐浴露洗发水2合1润肤乳液组合Target\"}";
        System.out.println(com.voyageone.common.util.JsonUtil.jsonToBean(data, CmsBtCombinedProductService.TmallItemCombine.class));
    }

}