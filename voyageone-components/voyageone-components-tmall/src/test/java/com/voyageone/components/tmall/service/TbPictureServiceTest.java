package com.voyageone.components.tmall.service;

import com.taobao.api.ApiException;
import com.taobao.api.FileItem;
import com.taobao.api.request.ItemImgDeleteRequest;
import com.taobao.api.request.ItemImgUploadRequest;
import com.taobao.api.response.ItemImgDeleteResponse;
import com.taobao.api.response.ItemImgUploadResponse;
import com.taobao.api.response.PictureCategoryGetResponse;
import com.taobao.api.response.PictureGetResponse;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.tmall.bean.TbGetPicCategoryParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNull;

/**
 * 淘宝图片服务单元测试
 * Created by jonas on 2016/11/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class TbPictureServiceTest {
    @Autowired
    private TbPictureService tbPictureService;

    @Autowired
    private TbItemService tbItemService;

    private ShopBean shopBean;

    public TbPictureServiceTest() {
        shopBean = new ShopBean();
        shopBean.setApp_url("http://gw.api.taobao.com/router/rest");
        shopBean.setAppKey("21008948");
        shopBean.setAppSecret("0a16bd08019790b269322e000e52a19f");
        shopBean.setSessionKey("62013001ZZ9c83cb4fe59499440ef154430b7679c3866f21792368114");
    }

    /**
     * @since 2.9.0
     */
    @Test
    public void replacePicture() throws Exception {

    }

    /**
     * @since 2.9.0
     */
    @Test
    public void getPictures() throws Exception {
        PictureGetResponse result2 = tbPictureService.getPictures(shopBean, "http://img.alicdn.com/imgextra/i1/1792368114/TB2h0qAbbmI.eBjy1zjXXaq5VXa_!!1792368114.jpg_430x430q90.jpg");

        assertNull(result2.getErrorCode());

        TbItemSchema itemSchema = tbItemService.getUpdateSchema(shopBean, 541009678524L);

        PictureGetResponse result1 = tbPictureService.getPictures(shopBean, "http://img.alicdn.com/" + itemSchema.getMainImages()[0]);

        assertNull(result1.getErrorCode());
    }

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