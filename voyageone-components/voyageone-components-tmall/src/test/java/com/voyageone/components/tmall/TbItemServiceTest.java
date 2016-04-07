package com.voyageone.components.tmall;

import com.taobao.api.ApiException;
import com.taobao.api.response.ItemSkusGetResponse;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.field.ComplexField;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.value.ComplexValue;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.tmall.exceptions.GetUpdateSchemaFailException;
import com.voyageone.components.tmall.service.TbItemSchema;
import com.voyageone.components.tmall.service.TbItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author jonasvlag, 16/2/25.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:test-context.xml")
public class TbItemServiceTest {

    @Autowired
    private TbItemService tbItemService;

    @Test
    public void testGetUpdateSchema() throws TopSchemaException, ApiException, GetUpdateSchemaFailException {

        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("http://gw.api.taobao.com/router/rest");
        shopBean.setAppKey("21008948");
        shopBean.setAppSecret("0a16bd08019790b269322e000e52a19f");
        shopBean.setSessionKey("6200a23ce180124c66248fa2bd50420ZZf0df31db94bd5a907029661");

        TbItemSchema itemSchema = tbItemService.getUpdateSchema(shopBean, 527408428758L);

        Field field = itemSchema.getFields().stream().filter(f -> f.getId().equals("item_images")).findFirst().orElse(null);

        ComplexField complexField = (ComplexField) field;

        ComplexValue complexValue = complexField.getComplexValue();

        assert complexValue != null;
    }

    @Test
    public void testGetSkuInfo() throws ApiException {

        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("http://gw.api.taobao.com/router/rest");
        shopBean.setAppKey("21008948");
        shopBean.setAppSecret("0a16bd08019790b269322e000e52a19f");
        shopBean.setSessionKey("6200a23ce180124c66248fa2bd50420ZZf0df31db94bd5a907029661");

        ItemSkusGetResponse rsp = tbItemService.getSkuInfo(shopBean, "527408428758", "properties");

        assert rsp != null;

        System.out.println(rsp.getBody());
    }
}