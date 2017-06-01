package com.voyageone.web2.cms.views.product;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.bean.cms.product.DelistingParameter;
import com.voyageone.service.bean.cms.product.GetChangeMastProductInfoParameter;
import com.voyageone.service.bean.cms.product.SetMastProductParameter;
import com.voyageone.web2.sdk.api.response.wms.GetStoreStockDetailResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.Map;
/**
 * @author james.li on 2016/6/14.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsProductDetailServiceTest {
    @Autowired
    CmsProductDetailService cmsProductDetailService;

    @Test
    public void testGetMastProductInfo() throws Exception {
        Map<String, Object> result = cmsProductDetailService.getMastProductInfo("010", 5938L, "cn");
        System.out.println(JacksonUtil.bean2Json(result));
    }

    @Test
    public void testGetChangeMastProductInfo() {
        GetChangeMastProductInfoParameter parameter = new GetChangeMastProductInfoParameter();
        parameter.setProductCode("CRBT0003SP-");
        parameter.setChannelId("010");
        parameter.setCartId(27);
        //{cartId:27,channelId:"010",productCode:"CRBT0003SP-"}
        Map<String, Object> result = cmsProductDetailService.getChangeMastProductInfo(parameter);
    }

    @Test
    public void testSetMastProduct() {
        SetMastProductParameter parameter = new SetMastProductParameter();
        parameter.setProductCode("DIBRHCRST/RHGAR7.5");
        parameter.setChannelId("010");
        parameter.setCartId(27);
        cmsProductDetailService.setMastProduct(parameter, "syste");
    }

    @Test
    public void testDelisting() throws Exception {
        DelistingParameter parameter = new DelistingParameter();
        parameter.setProductCode("DIBRHCRST/RHGAR8.5");
        parameter.setChannelId("010");
        parameter.setCartId(27);
        parameter.setComment("说明");
        System.out.println(JacksonUtil.bean2Json(parameter));
        cmsProductDetailService.delisting(parameter, "syste");

    }

    @Test
    public void testDelistinGroup() throws Exception {
        DelistingParameter parameter = new DelistingParameter();
        parameter.setProductCode("DIBRHCRST/RHGAR8.5");
        parameter.setChannelId("010");
        parameter.setCartId(23);
        parameter.setComment("说明");
        System.out.println(JacksonUtil.bean2Json(parameter));
        cmsProductDetailService.delistinGroup(parameter, "syste");
    }

}