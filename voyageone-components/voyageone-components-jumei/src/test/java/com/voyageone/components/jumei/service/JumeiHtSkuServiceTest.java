package com.voyageone.components.jumei.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jumei.JumeiHtSkuService;
import com.voyageone.components.jumei.reponse.HtSkuAddResponse;
import com.voyageone.components.jumei.reponse.HtSkuUpdateResponse;
import com.voyageone.components.jumei.request.HtSkuAddRequest;
import com.voyageone.components.jumei.request.HtSkuUpdateRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class JumeiHtSkuServiceTest {

    @Autowired
    private JumeiHtSkuService service;

    @Test
    public void testAddSku() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
        shopBean.setApp_url("http://openapi.ext.jmrd.com:8823");

        HtSkuAddRequest request = new HtSkuAddRequest();
        // 聚美Spu_No(必须)
        request.setJumei_spu_no("112670");
        // 聚美生成的deal 唯一值(必须)
        request.setJumei_hash_id("ht1463986241p222551454");
        // 海关备案商品编码(可选)
        // 注:(发货仓库为保税区仓库时，此处必填) 获取仓库接口　增加返回bonded_area_id字段 大于０　表示　保税
        request.setCustoms_product_number("1009");
        // 商家商品编码(必须) 注:确保唯一
        request.setBusinessman_num("2000101");
        // 库存(必须) 注：填写可供售卖的真实库存，无货超卖可能引起投诉与退款。无库存填写0
        request.setStocks("30");
        // 团购价(必须)  注：至少大于15元
        request.setDeal_price("27");
        // 市场价(必须)  注：必须大于等于团购价
        request.setMarket_price("32");
        // 是否在本次团购售卖，1是，0否(可选)    默认值:1
        request.setSale_on_this_deal("1");

        HtSkuAddResponse response = service.add(shopBean, request);
        if (response != null && response.is_Success()) {
            // 聚美添加Sku信息成功
            String reponseBody = response.getBody();
            System.out.println("聚美添加Sku信息成功！ body=" + reponseBody);
        } else {
            // 聚美添加Sku信息失败
            System.out.println("聚美添加Sku信息失败！");
        }
    }

    @Test
    public void testUpdateSku() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
        shopBean.setApp_url("http://openapi.ext.jmrd.com:8823");

        HtSkuUpdateRequest request = new HtSkuUpdateRequest();
        // 聚美Spu_No(必须)
        request.setJumei_sku_no("701506634");
        // 聚美生成的deal 唯一值(必须)
        request.setJumei_hash_id("ht1463986241p222551454");
        // 海关备案商品编码(可选)
        // 注:(发货仓库为保税区仓库时，此处必填) 获取仓库接口　增加返回bonded_area_id字段 大于０　表示　保税
        request.setCustoms_product_number("2009");
        // 商家商品编码(必须) 注:确保唯一
        request.setBusinessman_num("2000301");

        HtSkuUpdateResponse response = service.update(shopBean, request);
        if (response != null && response.is_Success()) {
            // 聚美修改Sku信息成功
            String reponseBody = response.getBody();
            System.out.println("聚美修改Sku信息成功！ body=" + reponseBody);
        } else {
            // 聚美修改Sku信息失败
            System.out.println("聚美修改Sku信息失败！");
        }
    }
}
