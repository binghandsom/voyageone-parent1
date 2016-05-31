package com.voyageone.components.tmall;

import com.taobao.api.ApiException;
import com.taobao.api.FileItem;
import com.taobao.api.request.ItemImgDeleteRequest;
import com.taobao.api.request.ItemImgUploadRequest;
import com.taobao.api.response.ItemImgDeleteResponse;
import com.taobao.api.response.ItemImgUploadResponse;
import com.taobao.api.response.PictureCategoryGetResponse;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.tmall.bean.TbGetPicCategoryParam;
import com.voyageone.components.tmall.service.TbPictureService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * 淘宝图片接口单元测试
 * Created by Jonas on 11/12/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:test-context.xml")
public class TbPictureServiceTest {

    @Autowired
    private TbPictureService tbPictureService;

    @Test
    public void testMain() throws ApiException {
        ShopBean shopBean = new ShopBean(){{

        }};
        TbGetPicCategoryParam param = new TbGetPicCategoryParam() {{
            setPictureCategoryId(181141114316991400L);
        }};
        PictureCategoryGetResponse response = tbPictureService.getCategories(shopBean, param);
        System.out.println(response.getMsg());
    }

    @Test
    public void testUploadItemPicture() throws Exception {

        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("http://gw.api.taobao.com/router/rest");
        shopBean.setAppKey("21008948");
        shopBean.setAppSecret("0a16bd08019790b269322e000e52a19f");
        shopBean.setSessionKey("6200a23ce180124c66248fa2bd50420ZZf0df31db94bd5a907029661");

        ItemImgUploadRequest req = new ItemImgUploadRequest();
        req.setNumIid(527408428758L);

//        req.setImage(new FileItem("/Users/jonasvlag/Desktop/5.jpg"));
//        req.setPosition(3L);
//        req.setIsMajor(false);
//
//        ItemImgUploadResponse rsp = tbPictureService.uploadItemPicture(shopBean, req);
//
//        assert rsp != null;
//
//        System.out.println(rsp.getBody());

        for (int i = 0; i < 5; i++) {

            req.setImage(new FileItem("/Users/jonasvlag/Desktop/" + (i + 1) + ".jpg"));
            req.setPosition((long) i);
            req.setIsMajor(i == 0);

            ItemImgUploadResponse rsp = tbPictureService.uploadItemPicture(shopBean, req);

            assert rsp != null;

            System.out.println(rsp.getBody());

            Thread.sleep(1000);
        }
    }

    @Test
    public void testDeleteItemPicture() throws Exception {

        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("http://gw.api.taobao.com/router/rest");
        shopBean.setAppKey("21008948");
        shopBean.setAppSecret("0a16bd08019790b269322e000e52a19f");
        shopBean.setSessionKey("6200a23ce180124c66248fa2bd50420ZZf0df31db94bd5a907029661");

        ItemImgDeleteRequest req = new ItemImgDeleteRequest();
        req.setNumIid(527408428758L);

        List<Long> idList = new ArrayList<>();

        idList.add(200218203706L);
        idList.add(203581912317L);
        idList.add(203581000445L);

        for (Long id: idList) {

            req.setId(id);

            ItemImgDeleteResponse rsp = tbPictureService.deleteItemPicture(shopBean, req);

            assert rsp != null;

            System.out.println(rsp.getBody());

            Thread.sleep(1000);
        }
    }
}