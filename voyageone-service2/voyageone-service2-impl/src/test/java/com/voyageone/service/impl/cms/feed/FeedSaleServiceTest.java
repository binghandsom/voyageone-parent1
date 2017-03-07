package com.voyageone.service.impl.cms.feed;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.dao.wms.WmsBtItemDetailsDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class FeedSaleServiceTest {

    @Autowired
    FeedSaleService saleService;

    @Autowired
    ProductService productService;

    @Autowired
    WmsBtItemDetailsDao wmsBtItemDetailsDao;

    @Test
    public void notSale( ) {

        String channelId="010";
         String clientSku="ESH96163FFEI";
        saleService.notSale(channelId, clientSku);
    }
    @Test
    public void sale( ) {

        String channelId="010";
        String clientSku="ESH96163FFEI";
        saleService.sale(channelId, clientSku,10);
    }

    @Test
    public void test(){
        JongoQuery jongoQuery = new JongoQuery();
        jongoQuery.setProjection("{'common.fields.code':1}");
        List<CmsBtProductModel> a =  productService.getList("021", jongoQuery);
        a.forEach(item->{
            if(ListUtils.isNull(wmsBtItemDetailsDao.selectByCode("021",item.getCommon().getFields().getCode()))){
                System.out.println(item.getCommon().getFields().getCode());
            }
        });

    }
}
