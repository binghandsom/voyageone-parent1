package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.service.dao.cms.CmsBtTmScItemDao;
import com.voyageone.service.dao.cms.CmsBtTmTonggouFeedAttrDao;
import com.voyageone.service.dao.cms.CmsMtChannelConditionMappingConfigDao;
import com.voyageone.service.daoext.cms.CmsMtHsCodeUnitDaoExt;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.CmsBtUsWorkloadModel;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Charis on 2017/7/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPlatformProductUploadUsServiceTest {


    @Autowired
    SxProductService sxProductService;

    @Autowired
    PlatformProductUploadService platformProductUploadService;

    @Autowired
    CmsBuildPlatformProductUploadUsService cmsBuildPlatformProductUploadUsService;

    @Test
    public void testUpdateUsWorkload() throws Exception {
        List<CmsBtUsWorkloadModel> sxWorkloadModels = platformProductUploadService.getListUsWorkload(1, "001", 8, new Date());

        for (CmsBtUsWorkloadModel model : sxWorkloadModels) {
            sxProductService.updateUsWorkload(model, 1, "charisTest");

        }
    }


    @Test
    public void testUploadProduct() throws Exception {
        List<CmsBtUsWorkloadModel> cmsBtUsWorkloadModels = new ArrayList<>();
        CmsBtUsWorkloadModel model = new CmsBtUsWorkloadModel();
        model.setChannelId("001");
        model.setCode("bb0206");
        model.setCartId(8);
        model.setPublishStatus(0);
        model.setCreater("charis_test");
        model.setModified(new Date());
        cmsBtUsWorkloadModels.add(model);
        ShopBean shopProp = Shops.getShop("001", 8);

        cmsBuildPlatformProductUploadUsService.uploadProduct(cmsBtUsWorkloadModels, shopProp);

    }

}
