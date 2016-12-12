package com.voyageone.task2.cms.service.product;

import com.voyageone.common.util.JacksonUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by dell on 2016/12/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsAdvSearchExportFileServiceTest {
    @Autowired
    CmsAdvSearchExportFileService cmsAdvSearchExportFileService;

    @Test
    public void onStartup() throws Exception {

        String msg = "{\"compareType\":null,\"brand\":null,\"tags\":[],\"priceChgFlg\":\"\",\"priceDiffFlg\":\"\",\"tagTypeSelectValue\":\"0\",\"promotionList\":[],\"catgoryList\":[],\"cidValue\":[],\"promotionTagType\":1,\"freeTagType\":1,\"supplierType\":1,\"brandSelType\":1,\"productSelType\":\"1\",\"sizeSelType\":\"1\",\"salesType\":\"All\",\"custGroupType\":\"1\",\"custAttrMap\":[{\"inputVal\":\"\",\"inputOpts\":\"\",\"inputOptsKey\":\"\"}],\"_selCodeList\":[\"51A0HC13E1-00LCNB0\",\"SJ9020SZW\",\"01411YAA\",\"51A2GJ93W4-00LA600\",\"51A2GJ94W4-0DTA600\",\"BA9207L-SS\",\"ESH96163FFEI\",\"H36899\",\"SDI882DBE.07\",\"SSG016028SDM75\"],\"fileType\":3,\"productStatus\":[],\"platformStatus\":[],\"pRealStatus\":[],\"shopCatStatus\":0,\"pCatStatus\":0}";
        Map<String, Object> map = JacksonUtil.jsonToMap(msg);
        cmsAdvSearchExportFileService.onStartup(map);
    }

}