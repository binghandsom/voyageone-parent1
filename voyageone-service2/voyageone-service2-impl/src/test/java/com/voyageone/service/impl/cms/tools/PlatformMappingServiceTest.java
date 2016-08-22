package com.voyageone.service.impl.cms.tools;

import com.voyageone.service.dao.cms.mongo.CmsBtPlatformMappingDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.CmsBtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 对商品属性计算进行单元测试
 * <p>
 * Created by jonas on 8/13/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class PlatformMappingServiceTest {

    @Autowired
    private PlatformMappingService platformMappingService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CmsBtPlatformMappingDao fieldMapsDao;

    private CmsBtPlatformMappingModel getTestModel() {

        CmsBtProductModel productModel = productService.getProductById("010", 9440);

        int cartId = 23;

        CmsBtProductModel_Platform_Cart cart = productModel.getPlatform(cartId);

        return new CmsBtPlatformMappingModel() {
            {
                setChannelId(productModel.getChannelId());
                setCategoryPath(cart.getpCatPath());
                setCartId(cartId);
                setCategoryType(2);
                setMappings(new HashMap<String, FieldMapping>() {
                    {
                        put("market_price", new FieldMapping() {
                            {
                                setFieldId("market_price");
                                setExpressions(new ArrayList<FieldMappingExpression>() {
                                    {
                                        add(new FieldMappingExpression() {
                                            {
                                                setValue("价格是");
                                                setType("FIXED");
                                                setAppend("<br />");
                                            }
                                        });
                                        add(new FieldMappingExpression() {
                                            {
                                                setValue("MetalStamp");
                                                setType("FEED_ORG");
                                                setAppend(" ");
                                            }
                                        });
                                        add(new FieldMappingExpression() {
                                            {
                                                setValue("hsCodePrivate");
                                                setType("MASTER");
                                                setAppend("");
                                            }
                                        });
                                    }
                                });
                            }
                        });
                        put("clientProductUrl", new FieldMapping() {
                            {
                                setFieldId("clientProductUrl");
                                setValue("1");
                            }
                        });
                    }
                });
            }
        };
    }

    @Test
    public void testSave() {

        CmsBtPlatformMappingModel fieldMapsModel = getTestModel();

        platformMappingService.saveMap(fieldMapsModel);

        fieldMapsModel = fieldMapsDao.selectOne(fieldMapsModel.getCartId(), fieldMapsModel.getCategoryType(), fieldMapsModel.getCategoryPath(), fieldMapsModel.getChannelId());

        fieldMapsDao.delete(fieldMapsModel);
    }

    @Test
    public void testFill() {

        CmsBtPlatformMappingModel fieldMapsModel = getTestModel();

//        platformMappingService.saveMap(fieldMapsModel);

        fieldMapsModel = fieldMapsDao.selectOne(fieldMapsModel.getCartId(), fieldMapsModel.getCategoryType(), fieldMapsModel.getCategoryPath(), fieldMapsModel.getChannelId());

//        Map<String, Object> valueMap = platformMappingService.getValueMap(fieldMapsModel.getChannelId(), 9440L, fieldMapsModel.getCartId(), fieldMapsModel.getCategoryPath());
//
//        assert valueMap != null;

        fieldMapsDao.delete(fieldMapsModel);
    }
}