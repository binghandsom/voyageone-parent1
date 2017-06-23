package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.util.ListUtils;
import com.voyageone.ecerp.interfaces.third.koala.KoalaItemService;
import com.voyageone.ecerp.interfaces.third.koala.beans.ItemImgUploadResponse;
import com.voyageone.ecerp.interfaces.third.koala.beans.KoalaConfig;
import com.voyageone.ecerp.interfaces.third.koala.beans.response.SkuOuterIdResult;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by Charis on 2017/6/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPlatformProductUploadKlServiceTest {

    @Autowired
    private CmsBuildPlatformProductUploadKlService cmsBuildPlatformProductUploadKlService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private KoalaItemService koalaItemService;
    @Autowired
    private CmsPlatformProductImportKlFieldsService fieldsService;

    @Test
    public void testInsertCmsBtKlSku() {
        String channelId = "001";
        Long groupId = Long.parseLong("9717145");
        String numIId = "545783512152";

        SxData sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);
        // 上新对象code
        List<String> codes = null;
        if (ListUtils.notNull(sxData.getProductList())) {
            codes = sxData.getProductList().stream().map(p -> p.getCommonNotNull().getFieldsNotNull().getCode()).collect(Collectors.toList());
        }

        SkuOuterIdResult[] skuOuterIdResults = new SkuOuterIdResult[2];

        SkuOuterIdResult sku = new SkuOuterIdResult();
        sku.setSkuKey("ab7820-l-l");
        sku.setSkuOuterId("ab7820-l");
        skuOuterIdResults[0] = sku;
        sku = new SkuOuterIdResult();
        sku.setSkuKey("ab7820-m-m");
        sku.setSkuOuterId("ab7820-m");
        skuOuterIdResults[1] = sku;
        cmsBuildPlatformProductUploadKlService.saveCmsBtKlSku(channelId, sxData, codes, numIId, skuOuterIdResults);

    }

    @Test
    public void testUploadImage() {
        String channelId = "001";

        String url = "http://s7d5.scene7.com/is/image/sneakerhead/SN20161223_1200x1200?$1200X1200$&$img=new-balance-split-sport-style-tshirt-mt63514bk-1";
        KoalaConfig koalaConfig = Shops.getShopKoala(channelId, CartEnums.Cart.KL.getId());
        koalaConfig.setSessionkey("GwtM96");
        koalaConfig.setUseProxy(false);

        try {
//            String[] images = sxProductService.uploadImageByUrl_KL(url, koalaConfig, "sneakerRRR.jpg");
            for (int i = 1; i < 8; i++) {
                File file = new File("E:\\BBB\\" + i + ".jpg");

                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
                int n;
                while ((n = fis.read(b)) != -1)
                {
                    bos.write(b, 0, n);
                }
                fis.close();
                bos.close();

                ItemImgUploadResponse response = koalaItemService.imgUpload(koalaConfig, bos.toByteArray(), "code" + i + ".jpg");

                System.out.println(i + "\t" + response.getUrl());
            }


//            System.out.println(images[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUploadProduct() {
        // 清除缓存（cms.channel_config表）
        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_CmsChannelConfigs.toString());
        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_ShopConfigs.toString());
        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_ShopConfigConfigs.toString());


        String channelId = "001";
        int cartId = 34;
        Long groupId = Long.parseLong("11292497");
        // 保存渠道级别(channel)的共通配置项目(从cms_mt_channel_config表中取得的)
        Map<String, String> channelConfigValueMap = new ConcurrentHashMap<>();
        // 取得cms_mt_channel_config表中配置的渠道级别的配置项目值(如：颜色别名等)
        cmsBuildPlatformProductUploadKlService.doChannelConfigInit(channelId, cartId, channelConfigValueMap);

        // 从cms_mt_channel_condition_mapping_config表中取得当前渠道的取得产品主类目与天猫平台叶子类目(或者平台一级类目)，以及feed类目id和天猫平台类目之间的mapping关系数据
        Map<String, List<Map<String, String>>> categoryMappingListMap = cmsBuildPlatformProductUploadKlService.getCategoryMapping(channelId, cartId);

        CmsBtSxWorkloadModel cmsBtSxWorkloadModel = new CmsBtSxWorkloadModel();
        cmsBtSxWorkloadModel.setGroupId(groupId);
        cmsBtSxWorkloadModel.setChannelId(channelId);
        cmsBtSxWorkloadModel.setCartId(cartId);
        cmsBtSxWorkloadModel.setPublishStatus(0);
        cmsBtSxWorkloadModel.setCreater("test");


        ShopBean shopProp = Shops.getShop(channelId, cartId);

        cmsBuildPlatformProductUploadKlService.uploadProduct(cmsBtSxWorkloadModel, shopProp, channelConfigValueMap, categoryMappingListMap);
    }

    @Test
    public void testGetItemData() {
        String channelId = "001";
        String status = "5";
        String pid = null;
        String runType = null;
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("channelId", channelId);
        messageMap.put("platformStatus", status);
        messageMap.put("pid", pid);
        messageMap.put("runType", runType);

        try {
            fieldsService.onStartup(messageMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
