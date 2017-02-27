package com.voyageone.service.impl.cms;

import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.CmsBtCustomPropModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by james on 2017/2/21.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsBtCustomPropServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    CommonSchemaService commonSchemaService;
    @Test
    public void getProductCustomProp() throws Exception {

        List<Field> cmsMtCommonFields = commonSchemaService.getComSchemaModel().getFields();
        List<Map<String,Object>> Master = new ArrayList<>();
        cmsMtCommonFields.forEach(field -> {
            Map<String,Object> item = new HashMap();
            item.put("id",field.getId());
            item.put("name",field.getName());
            Master.add(item);
        });

        CmsBtProductModel cmsBtProductModel = productService.getProductByCode("010","RF1119MLPN-D");
        cmsBtCustomPropService.setProductCustomProp(cmsBtProductModel);
        System.out.println(JacksonUtil.bean2Json(cmsBtProductModel.getFeed()));

    }

    @Test
    public void getFeedAttributeName() throws Exception {
//        CmsBtCustomPropModel aa = cmsBtCustomPropService.getFeedAttributeName("015");
//        System.out.println(JacksonUtil.bean2Json(aa));
    }

    @Test
    public void rearrange() throws Exception {
        CmsBtCustomPropModel aa = cmsBtCustomPropService.getCustomPropByCatChannel("010","010","aaa>bbb>ccc");
        aa.getSort().add("Brand");
        aa.getSort().add("Metal Stamp");
//        cmsBtCustomPropService.rearrange(aa);
        System.out.println(JacksonUtil.bean2Json(aa));


    }

    @Autowired
    CmsBtCustomPropService cmsBtCustomPropService;
    @Test
    public void getCustomPropByCatChannelExtend() throws Exception {

        CmsBtCustomPropModel aa = cmsBtCustomPropService.getCustomPropByCatChannelExtend("010","010","");
        System.out.println(JacksonUtil.bean2Json(aa));
    }

    @Test
    public void update() throws Exception {
        CmsBtCustomPropModel cmsBtCustomPropModel = new CmsBtCustomPropModel();
        cmsBtCustomPropModel.setCat("aaa>bbb");
        cmsBtCustomPropModel.setChannelId("010");
        cmsBtCustomPropModel.setOrgChannelId("010");
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
        entity.setNameEn("Stone");
        entity.setNameCn("产品宝石");
        entity.setChecked(false);
        entity.setType(CmsBtCustomPropModel.CustomPropType.Common.getValue());
        entities.add(entity);
        entity = new CmsBtCustomPropModel.Entity();
        entity.setNameEn("Description Measure");
        entity.setNameCn("产品尺寸");
        entity.setChecked(false);
        entity.setType(CmsBtCustomPropModel.CustomPropType.Common.getValue());
        entities.add(entity);
        cmsBtCustomPropModel.setEntitys(entities);
        cmsBtCustomPropService.update(cmsBtCustomPropModel);
        cmsBtCustomPropModel.set_id(null);
        cmsBtCustomPropModel.setCat("aaa>bbb>ccc");
        cmsBtCustomPropService.update(cmsBtCustomPropModel);
        cmsBtCustomPropModel.set_id(null);
        cmsBtCustomPropModel.setCat("aaa");
        cmsBtCustomPropService.update(cmsBtCustomPropModel);


    }

}