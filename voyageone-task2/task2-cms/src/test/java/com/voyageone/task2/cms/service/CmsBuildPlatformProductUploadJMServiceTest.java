package com.voyageone.task2.cms.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jumei.JumeiHtMallService;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.CmsBtJmSkuDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.daoext.cms.CmsBtSxWorkloadDaoExt;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Ethan Shi on 2016/6/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:context-cms-test.xml")
public class CmsBuildPlatformProductUploadJMServiceTest {


    @Autowired
    CmsBuildPlatformProductUploadJMService cmsBuildPlatformProductUploadJMService;
    @Autowired
    private JumeiHtMallService jumeiHtMallService;


    @Autowired
    CmsBtSxWorkloadDaoExt cmsBtSxWorkloadDaoExt;

    @Autowired
    CmsBtProductGroupDao cmsBtProductGroupDao;

    @Autowired
    CmsBtJmSkuDao cmsBtJmSkuDao;

    @Autowired
    SxProductService sxProductService;


    @Test
    public void TestPrice() throws Exception {


    }



    @Test
    public void TestDate() throws Exception {



        Map<String, String> map = new HashMap<>();
        String value = map.get("1");

        long currentTime = System.currentTimeMillis();
        System.out.println(currentTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(currentTime);
        String dateStr = formatter.format(date);
        System.out.println(dateStr);

        Long time = getTime(dateStr);
        System.out.println(time);

        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.MINUTE, 30);
        System.out.println(rightNow.getTimeInMillis());
        Date date1 = new Date(rightNow.getTimeInMillis());
        date1.getTime();
        String date1Str = formatter.format(date1);
        System.out.println(date1Str);

        time = getTime(date1Str);
        System.out.println(time);


    }

    private static Long getTime(String user_time) throws Exception {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;


        d = sdf.parse(user_time);
        long l = d.getTime()/1000-8*3600;

        return l;
    }



    @Test
    public void testUpdateProduct() throws Exception {

//        List<CmsBtSxWorkloadModel> workloadList = cmsBtSxWorkloadDaoExt.selectSxWorkloadModelWithChannelIdCartIdGroupBy(1, "010", 27);
//
//        for (CmsBtSxWorkloadModel work : workloadList) {
////            work.setGroupId(27214L);
////            work.setGroupId(39342L);
//            work.setGroupId(30222L);
//
//
//            cmsBuildPlatformProductUploadJMService.updateProduct(work);
//        }

        CmsBtSxWorkloadModel work = new CmsBtSxWorkloadModel();
        work.setCartId(27);
        work.setChannelId("010");
        work.setGroupId(30222L);
        work.setPublishStatus(0);

        cmsBuildPlatformProductUploadJMService.updateProduct(work);

    }

    @Test
    public void testUpdateProduct2() throws Exception {

        CmsBtSxWorkloadModel workload = new CmsBtSxWorkloadModel();
        workload.setId(185);
        workload.setChannelId("017");
        workload.setCartId(27);
        workload.setGroupId(Long.parseLong("389898"));
        workload.setPublishStatus(0);

        cmsBuildPlatformProductUploadJMService.updateProduct(workload);

    }

    /**
     * 上新成功的数据，上传到聚美商城
     */
    @Test
    public void testUploadMallForAll() {
        String channelId = "012";
        int cartId = 27;
        ShopBean shop = Shops.getShop(channelId, cartId);

        String query = "{\"cartId\": " + cartId + "}";
        List<CmsBtProductGroupModel> listGroup = cmsBtProductGroupDao.select(query, channelId);

        List<Long> listSkipGroupId = new ArrayList<>(); // 跳过一些不上新的数据

        System.out.println("============ 上传聚美商城 start !!! ============");

        for (CmsBtProductGroupModel groupModel : listGroup) {
            if (!StringUtils.isEmpty(groupModel.getPlatformMallId())) {
                // 上传过，不再处理，注掉这段if的话，就支持更新了(但是注意uploadMall方法最后两个参数，null的话，不支持追加sku)
                continue;
            }
            if (StringUtils.isEmpty(groupModel.getPlatformPid()) || StringUtils.isEmpty(groupModel.getNumIId())) {
                // 没有成功上新过
                continue;
            }

            Long groupId = groupModel.getGroupId();
            SxData sxData;
            try {
                sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);
                if (sxData == null) {
                    throw new BusinessException("SxData取得失败!");
                }
                if (!StringUtils.isEmpty(sxData.getErrorMessage())) {
                    throw new BusinessException(sxData.getErrorMessage());
                }

            } catch (Exception e) {
                if (e instanceof BusinessException) {
                    String errorMsg = "GroupId [" + groupId + "]跳过:" + ((BusinessException) e).getMessage();
                    listSkipGroupId.add(groupId);
                } else {
                    System.out.println("GroupId [" + groupId + "]SxData取得失败!" + e.getMessage());
                }
                continue;
            }

            ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
            CmsBtProductModel product = sxData.getMainProduct();
            if (StringUtils.isEmpty(product.getPlatform(cartId).getpProductId()) || StringUtils.isEmpty(product.getPlatform(cartId).getpNumIId())) {
                System.out.println("GroupId [" + groupId + "] product表的产品id(pProductId)或商品id(pNumIId)为空!");
                continue;
            }

            try {
                cmsBuildPlatformProductUploadJMService.uploadMall(product, shop, expressionParser, null, null);
            } catch (Exception e) {
                System.out.println("GroupId [" + groupId + "] 上传聚美商城失败!" + e.getMessage());
            }
        }

        System.out.println("跳过的groupId:" + listSkipGroupId);
        System.out.println("============ 上传聚美商城 end !!! ============");
    }

}