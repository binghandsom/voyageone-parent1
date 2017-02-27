package com.voyageone.task2.cms.service;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.ItemSkuGetRequest;
import com.taobao.api.request.TmallItemUpdateSchemaGetRequest;
import com.taobao.api.response.ItemSkuGetResponse;
import com.taobao.api.response.TmallItemUpdateSchemaGetResponse;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author james.li on 2016/9/26.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class TargetDailyServiceTest {

    @Autowired
    TargetDailyService targetDailyService;

    @Autowired
    ProductService productService;
    @Test
    public void testOnStartup() throws Exception {
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "21008948", "0a16bd08019790b269322e000e52a19f");

        TmallItemUpdateSchemaGetRequest req3 = new TmallItemUpdateSchemaGetRequest();
        req3.setItemId(543549587470L);
        TmallItemUpdateSchemaGetResponse rsp3 = client.execute(req3, "620230429acceg4103a72932e22e4d53856b145a192140b2854639042");

        ItemSkuGetRequest req = new ItemSkuGetRequest();
        req.setFields("sku_id,iid,properties,quantity,price,outer_id,created,modified,status");
        req.setSkuId(3435122446256L);
        req.setNumIid(543549587470L);
        ItemSkuGetResponse rsp1 = client.execute(req, "620230429acceg4103a72932e22e4d53856b145a192140b2854639042");
        System.out.println(rsp1.getBody());

//        TmallItemCombineGetRequest req2 = new TmallItemCombineGetRequest();
//        req2.setItemId(543549587470L);
//        TmallItemCombineGetResponse rsp = client.execute(req2, "620230429acceg4103a72932e22e4d53856b145a192140b2854639042");
//        System.out.println(rsp.getBody());
        targetDailyService.onStartup(new ArrayList<>());
    }

    @Test
    public void test(){
        String cartId = "23";
        List<String> productCodes = Arrays.asList("RF1119MLPN-D");
        JongoQuery qryObj = new JongoQuery();
        qryObj.setQuery("{'common.fields.code':{$in:#},'platforms.P" + cartId + ".skus.0':{$exists:true},'platforms.P" + cartId + ".status':'Approved'}");
        qryObj.setParameters(productCodes);
        List<CmsBtProductModel> prodObjList = productService.getList("010", qryObj);
        System.out.println("a");
    }
}