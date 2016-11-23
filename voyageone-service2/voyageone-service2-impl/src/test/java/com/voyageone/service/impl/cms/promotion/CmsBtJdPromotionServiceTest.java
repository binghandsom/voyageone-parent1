package com.voyageone.service.impl.cms.promotion;

import com.jd.open.api.sdk.domain.ware.Sku;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.bean.JdPromotionSkuBean;
import com.voyageone.components.jd.service.JdSkuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by desmond on 2016/11/17.
 *
 * @version 2.9.0
 * @since 2.9.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsBtJdPromotionServiceTest {

    @Autowired
    private CmsBtJdPromotionService cmsBtJdPromotionService;
    @Autowired
    private JdSkuService jdSkuService;

    @Test
    public void testAddSkuIdToPromotionBatch() {
        Integer totoalCnt = 101;
        Integer size = 10;
        int retry = (totoalCnt%size == 0) ? (totoalCnt/size) : (totoalCnt/size+1);
        System.out.println("");
        System.out.println("retry = " + retry);

        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("https://api.jd.com/routerjson");
        shopBean.setAppKey("");
        shopBean.setAppSecret("");
        shopBean.setSessionKey(""); // 京东国际匠心界全球购专营店(SessionKey)
        shopBean.setOrder_channel_id("928");
        shopBean.setCart_id("28");
        shopBean.setShop_name("京东国际匠心界全球购专营店");
        shopBean.setPlatform_id("2");

        // test用促销01
//        Long promoId = 1045629728L;
//        Long promoId = 1045913792L;
//        Long promoId = 1046183527L;
//        Long promoId = 1046206009L;
        Long promoId = 1046178147L;

        String testWareId = "1956814662";  // 匠心界店内追加的一个专门用来测试的商品
        List<Sku> skus;
        StringBuilder failCause = new StringBuilder();
        try {
            // 根据京东商品id取得京东平台上的sku信息列表(即使出错也不报出来，算上新成功，只是回写出错，以后再回写也可以)
            skus = jdSkuService.getSkusByWareId(shopBean, testWareId, failCause);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        if (ListUtils.isNull(skus)) {
            System.out.println(String.format("测试用wareId(%s)取得的skus为空!", testWareId));
            return;
        }

        List<JdPromotionSkuBean> jdPromoSkuList = new ArrayList<>();
        int i = 1;
        for (Sku jdPromoSku : skus) {
//            if (i <= 8) {i++;continue;}
            JdPromotionSkuBean sku = new JdPromotionSkuBean();
            sku.setSkuCode(jdPromoSku.getOuterId());
            // 奇数件设置jdSkuId,偶数件不设置，测试自动去京东平台取得skuId逻辑
            if (i % 2 == 1) sku.setJdSkuId(StringUtils.toString(jdPromoSku.getSkuId()));
            sku.setJdPrice(jdPromoSku.getJdPrice());
            sku.setJdPromoPrice("39"+String.format("%03d", i)+".60");
            jdPromoSkuList.add(sku);
            i++;
//            if (i > 11) break;
        }

        try {
            cmsBtJdPromotionService.addSkuIdToPromotionBatch(shopBean, promoId, jdPromoSkuList, "测试");
            System.out.println("批量添加sku到京东促销成功!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void testUpdateJdSkuIds() {
        String channelId = "928";
        String cartId = "28";

        List<JdPromotionSkuBean> jdPromoSkuList = new ArrayList<>();
        JdPromotionSkuBean sku1 = new JdPromotionSkuBean();
        sku1.setSkuCode("022-EA3060501754");
        sku1.setJdSkuId("1970000001");
        jdPromoSkuList.add(sku1);
        JdPromotionSkuBean sku2 = new JdPromotionSkuBean();
        sku2.setSkuCode("022-EA3060538652");
        sku2.setJdSkuId("1970000002");
        jdPromoSkuList.add(sku2);
        JdPromotionSkuBean sku3 = new JdPromotionSkuBean();
        sku3.setSkuCode("022-EA3060538852");
        sku3.setJdSkuId("1970000003");
        jdPromoSkuList.add(sku3);

        try {
            cmsBtJdPromotionService.updateJdSkuIds(channelId, cartId, jdPromoSkuList, "测试");
            System.out.println("批量回写jdSkuId成功!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
