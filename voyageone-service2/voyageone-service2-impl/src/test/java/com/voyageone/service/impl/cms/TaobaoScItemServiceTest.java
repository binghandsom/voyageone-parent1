package com.voyageone.service.impl.cms;

import com.taobao.api.ApiException;
import com.taobao.api.domain.ScItem;
import com.taobao.api.domain.ScItemMap;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.tmall.service.TbScItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tom.zhu on 2016/10/08.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class TaobaoScItemServiceTest {

    @Autowired
    TaobaoScItemService taobaoScItemService;

    @Autowired
    TbScItemService tbScItemService;

    private ShopBean getShopBean() {
        ShopBean shopBean = Shops.getShop("018", 23);
        shopBean.setApp_url("http://gw.api.taobao.com/router/rest");
        shopBean.setAppKey("");
        shopBean.setAppSecret("");
        shopBean.setSessionKey("");

        return shopBean;
    }

    /**
     * 主逻辑测试
     * @throws Exception
     */
    @Test
    public void testTaobaoScItem() throws Exception {
		Long[] numIIdList = {
//				539958820481L, 539959418957L
		};

		for (int i = 0; i < numIIdList.length; i++) {
			long numIId = numIIdList[i];
			System.out.println("NOW:" + i + "/" + numIIdList.length + ", numIID:" + numIId);
			taobaoScItemService.doSetScItem(getShopBean(), numIId);
		}
//      taobaoScItemService.doSetScItem(getShopBean(), 524011540895L);
//		taobaoScItemService.doSetScItem(getShopBean(), 524038603921L);
    }

    /**
     * 获取关联信息
     * @throws Exception
     */
    @Test
    public void testGetScItemMap() throws Exception {

        try {
            List<ScItemMap> mapList = tbScItemService.getScItemMap(getShopBean(), 532900631201L, null);
            System.out.println(mapList.size());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

	/**
	 * 库存初始化
	 * @throws Exception
	 */
	@Test
	public void testInitialInventory() throws Exception {

//		try {
//			tbScItemService.doInitialInventory(getShopBean(), "BABCMAXAZRIA001", "BBE1U688-1O6-XXS", "0");
//			tbScItemService.doInitialInventory(getShopBean(), "BABCMAXAZRIA001", "BBE1U688-1O6-XS", "0");
//			tbScItemService.doInitialInventory(getShopBean(), "BABCMAXAZRIA001", "BBE1U688-1O6-S", "0");
//			tbScItemService.doInitialInventory(getShopBean(), "BABCMAXAZRIA001", "BBE1U688-1O6-M", "0");
//			tbScItemService.doInitialInventory(getShopBean(), "BABCMAXAZRIA001", "BBE1U688-1O6-L", "0");
//			tbScItemService.doInitialInventory(getShopBean(), "BABCMAXAZRIA001", "BBE1U688-Z5I-XXS", "0");
//			tbScItemService.doInitialInventory(getShopBean(), "BABCMAXAZRIA001", "BBE1U688-Z5I-XS", "0");
//			tbScItemService.doInitialInventory(getShopBean(), "BABCMAXAZRIA001", "BBE1U688-Z5I-S", "0");
//			tbScItemService.doInitialInventory(getShopBean(), "BABCMAXAZRIA001", "BBE1U688-Z5I-M", "0");
//			tbScItemService.doInitialInventory(getShopBean(), "BABCMAXAZRIA001", "BBE1U688-Z5I-L", "0");
//		} catch (ApiException e) {
//			e.printStackTrace();
//		}
	}

	@Test
	public void test随便测测() {
		try {
//			ScItem scItem = tbScItemService.getScItemByOuterCode(getShopBean(), "UZA60B29-001-XS");
			long id = 539813019324L;
			tbScItemService.getInventoryByScItemId(getShopBean(), id);

		} catch (ApiException e) {
			e.printStackTrace();
		}

//		try {
//			List<ScItemMap> xxx = tbScItemService.getScItemMap(getShopBean(), 537536315047L, null);
////			ScItem scItem = tbScItemService.getScitemByNumIId(getShopBean(), 537536315047L);
//			long id = xxx.size();
//		} catch (ApiException e) {
//			e.printStackTrace();
//		}
	}

}