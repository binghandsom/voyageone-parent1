package com.voyageone.service.impl.cms;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.model.cms.mongo.CmsBtCustomPropModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/2/21.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsBtCustomPropServiceTest {

    @Autowired
    CmsBtCustomPropService cmsBtCustomPropService;
    @Test
    public void getCustomPropByCatChannelExtend() throws Exception {

        CmsBtCustomPropModel aa = cmsBtCustomPropService.getCustomPropByCatChannelExtend("010","aaa>bbb>ccc");
        System.out.println(JacksonUtil.bean2Json(aa));
    }

    @Test
    public void update() throws Exception {
        CmsBtCustomPropModel cmsBtCustomPropModel = new CmsBtCustomPropModel();
        cmsBtCustomPropModel.setCat("aaa>bbb");
        cmsBtCustomPropModel.setChannelId("010");
        List<CmsBtCustomPropModel.Entity> entities = new ArrayList<>();
        CmsBtCustomPropModel.Entity entity = new CmsBtCustomPropModel.Entity();
        entity.setNameCn("产品材质");
        entity.setNameEn("Metal Stamp");
        entity.setChecked(true);
        entity.setType(CmsBtCustomPropModel.CustomPropType.Common.getValue());
        entities.add(entity);
        entity = new CmsBtCustomPropModel.Entity();
        entity.setNameCn("产品品牌");
        entity.setNameEn("Brand");
        entity.setChecked(true);
        entity.setType(CmsBtCustomPropModel.CustomPropType.Common.getValue());
        entities.add(entity);
        entity = new CmsBtCustomPropModel.Entity();
        entity.setNameCn("Stone");
        entity.setNameEn("产品宝石");
        entity.setChecked(false);
        entity.setType(CmsBtCustomPropModel.CustomPropType.Common.getValue());
        entities.add(entity);
        entity = new CmsBtCustomPropModel.Entity();
        entity.setNameCn("Description Measure");
        entity.setNameEn("产品尺寸");
        entity.setChecked(false);
        entity.setType(CmsBtCustomPropModel.CustomPropType.Common.getValue());
        entities.add(entity);
        cmsBtCustomPropModel.setEntitys(entities);
        cmsBtCustomPropService.update(cmsBtCustomPropModel);
    }

}