package com.voyageone.service.impl.cms;

import com.taobao.api.ApiException;
import com.taobao.api.domain.ScItem;
import com.taobao.api.domain.ScItemMap;
import com.taobao.api.domain.Shop;
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
        ShopBean shopBean = Shops.getShop("012", 23);
//        shopBean.setApp_url("http://gw.api.taobao.com/router/rest");
//        shopBean.setAppKey("");
//        shopBean.setAppSecret("");
//        shopBean.setSessionKey("");
//
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

	@Test
	public void do处理坏掉的货品() {
		ShopBean shopBean = getShopBean();

		do处理坏掉的货品_logic(shopBean, "ENW8C909-315-XS", 533956035747L);
		do处理坏掉的货品_logic(shopBean, "JVS3G299-001-XS", 538340180318L);
		do处理坏掉的货品_logic(shopBean, "JDKJF139-003-O/S", 523997051159L);
		do处理坏掉的货品_logic(shopBean, "ISQ64F09-616-L", 527409176348L);
		do处理坏掉的货品_logic(shopBean, "DIH1X148-E75-XS", 536069366284L);
		do处理坏掉的货品_logic(shopBean, "DIH1X148-P6S-XS", 536069366284L);
		do处理坏掉的货品_logic(shopBean, "EYK63C85-001-XXS", 534670479532L);
		do处理坏掉的货品_logic(shopBean, "DIH61J83-P6S-L", 536708647430L);
		do处理坏掉的货品_logic(shopBean, "BYM60J24-100-XS", 534775167150L);
		do处理坏掉的货品_logic(shopBean, "DIH61J83-V4T-XS", 536708647430L);
		do处理坏掉的货品_logic(shopBean, "KFQ1X034-15D-M", 535738330990L);
		do处理坏掉的货品_logic(shopBean, "KFQ1X034-I5C-L", 535738330990L);
		do处理坏掉的货品_logic(shopBean, "KFQ1X034-I5C-M", 535738330990L);
		do处理坏掉的货品_logic(shopBean, "BYM61J34-R4U-6", 536598877133L);
		do处理坏掉的货品_logic(shopBean, "JRR132HB-001-8.5", 525757202430L);
		do处理坏掉的货品_logic(shopBean, "BYQ1S869-1J4-XS", 536989131111L);
		do处理坏掉的货品_logic(shopBean, "BYQ1S869-410-S", 536989131111L);
		do处理坏掉的货品_logic(shopBean, "FFH69I17-P6S-L", 534763257070L);
		do处理坏掉的货品_logic(shopBean, "ELO65I94-003-6", 534091388665L);
		do处理坏掉的货品_logic(shopBean, "BGR67E64-U6H-2", 526970289563L);
		do处理坏掉的货品_logic(shopBean, "PUG2G160-001-L", 535775765180L);
		do处理坏掉的货品_logic(shopBean, "LMQ69I30-6S8-8", 534249871700L);
		do处理坏掉的货品_logic(shopBean, "PUG2G160-001-M", 535775765180L);
		do处理坏掉的货品_logic(shopBean, "KFQ61J96-I5C-S", 534733574200L);
		do处理坏掉的货品_logic(shopBean, "KYK64J20-P6J-S", 537522887054L);
		do处理坏掉的货品_logic(shopBean, "LHL1R858-001-S", 534739566939L);
		do处理坏掉的货品_logic(shopBean, "LMQ1X170-642-S", 536007875011L);
		do处理坏掉的货品_logic(shopBean, "LMQ1X170-642-XXS", 536007875011L);
		do处理坏掉的货品_logic(shopBean, "LMQ1X170-661-XXS", 536007875011L);
		do处理坏掉的货品_logic(shopBean, "RND3G156-001-8", 535812664284L);
		do处理坏掉的货品_logic(shopBean, "NPL1W997-001-L", 534940554606L);
		do处理坏掉的货品_logic(shopBean, "LHL1W115-001-XS", 527767281057L);
		do处理坏掉的货品_logic(shopBean, "QVV3G014-987-10", 525678947452L);
		do处理坏掉的货品_logic(shopBean, "LMQ63I29-4F1-M", 535740502496L);
		do处理坏掉的货品_logic(shopBean, "RXI1U807-3F7-S", 524045751658L);
		do处理坏掉的货品_logic(shopBean, "QVV4I807-987-XXS", 525720037495L);
		do处理坏掉的货品_logic(shopBean, "RXI1V223-510-XS", 539104765812L);
		do处理坏掉的货品_logic(shopBean, "NST61D04-001-S", 527355777882L);
		do处理坏掉的货品_logic(shopBean, "QVV66I48-128-0", 533038321871L);
		do处理坏掉的货品_logic(shopBean, "LMQ64K62-100-S", 535996875197L);
		do处理坏掉的货品_logic(shopBean, "QVV66I48-128-8", 533038321871L);
		do处理坏掉的货品_logic(shopBean, "LHL3C521-813-L", 526060324636L);
		do处理坏掉的货品_logic(shopBean, "RPY2F497-001-XXS", 524001001650L);
		do处理坏掉的货品_logic(shopBean, "QXX131FB-001-6", 525765765267L);
		do处理坏掉的货品_logic(shopBean, "PST588ST-1A8-N/S", 537641445838L);
		do处理坏掉的货品_logic(shopBean, "RPY3G239-P6F-L", 537262093425L);
		do处理坏掉的货品_logic(shopBean, "LHL64756-001-XS", 524062149329L);
		do处理坏掉的货品_logic(shopBean, "WUZ9B529-N2Y-S", 532234364564L);
		do处理坏掉的货品_logic(shopBean, "WUZ9D340-315-6", 536598117611L);
		do处理坏掉的货品_logic(shopBean, "WZM61I94-0A4-S", 529739669636L);
		do处理坏掉的货品_logic(shopBean, "XOV1R866-003-XS", 539144136332L);
		do处理坏掉的货品_logic(shopBean, "TDM1U606-003-XSS", 524064194275L);
		do处理坏掉的货品_logic(shopBean, "TFQ1U566-P6J-S", 524073088178L);
		do处理坏掉的货品_logic(shopBean, "TFQ1U566-P6J-XS", 524073088178L);
		do处理坏掉的货品_logic(shopBean, "WQR68E49-I3X-S", 529410239403L);
		do处理坏掉的货品_logic(shopBean, "SRV2E996-003-S", 538982811854L);
		do处理坏掉的货品_logic(shopBean, "WQR1U549-100-M/L", 531442871129L);
		do处理坏掉的货品_logic(shopBean, "UFE1W818-P2G-XXS", 529733726266L);
	}

	private void do处理坏掉的货品_logic(ShopBean shopBean, String sku, long numIId) {

		try {
			// 根据sku， 查询后端货品id
			long sc_item_id = tbScItemService.getScItemByOuterCode(shopBean, sku).getItemId();

			// 取消sku绑定
			tbScItemService.deleteScItemMap(shopBean, sc_item_id);

			// 删除货品
			// 没有API， 只能手动处理 （试下来发现， ，其实不处理也没关系）

			// 创建货品
			// 初始化库存
			// 创建关联
			taobaoScItemService.doSetScItem(shopBean, numIId);

			// 人工通知赵杰同步库存

		} catch (ApiException e) {
			System.out.println("ERROR: sku: " + sku + "; numIId:" + numIId);
			e.printStackTrace();
		}
	}

}