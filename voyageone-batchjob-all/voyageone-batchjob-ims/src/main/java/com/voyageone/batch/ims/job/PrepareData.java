package com.voyageone.batch.ims.job;

import com.voyageone.batch.ims.bean.CustomMappingType;
import com.voyageone.batch.ims.dao.PlatformPropCustomMappingDao;
import com.voyageone.batch.ims.modelbean.CustomPlatformPropMapping;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * Created by Leo on 15-7-1.
 */
public class PrepareData {
    private PlatformPropCustomMappingDao platformPropCustomMappingDao;

    public PrepareData() {
        ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
        platformPropCustomMappingDao = ctx.getBean(PlatformPropCustomMappingDao.class);
    }

    public void preparePropMappingCustom()
    {
        CustomPlatformPropMapping customPlatformPropMapping = new CustomPlatformPropMapping();
        customPlatformPropMapping.setCustomMappingType(CustomMappingType.BRAND_INFO);
        customPlatformPropMapping.setCartId(1);
        customPlatformPropMapping.setPlatformPropId("prop_20000");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setCustomMappingType(CustomMappingType.SKU_INFO);
        customPlatformPropMapping.setCartId(1);
        customPlatformPropMapping.setPlatformPropId("sku");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setCustomMappingType(CustomMappingType.SKU_INFO);
        customPlatformPropMapping.setCartId(1);
        customPlatformPropMapping.setPlatformPropId("prop_extend_1627207");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setCustomMappingType(CustomMappingType.SKU_INFO);
        customPlatformPropMapping.setCartId(1);
        customPlatformPropMapping.setPlatformPropId("prop_extend_10537981");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

    }
    public static void main(String[] args)
    {
        /*
        RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();

        //产品图片模板
        RuleExpression productImageTplExpression = new RuleExpression();
        productImageTplExpression.addRuleWord(new TextWord("http://image.sneakerhead.com/is/image/sneakerhead/1200templates?$1200x1200$&$img="));
        DictWord productImageTpl = new DictWord("产品图片模板", productImageTplExpression, false);
        System.out.println(ruleJsonMapper.serializeRuleWord(productImageTpl));

        //产品图片-1
        RuleExpression productImage1Expression = new RuleExpression();
        productImage1Expression.addRuleWord(new DictWord("产品图片模板"));
        CustomWord getImageByCodeCustomWord = new CustomWord(new CustomWordValueGetMainProductImageKey("1"));
        productImage1Expression.addRuleWord(getImageByCodeCustomWord);
        DictWord productImage1 = new DictWord("产品图片-1", productImage1Expression, true);
        System.out.println(ruleJsonMapper.serializeRuleWord(productImage1));
        */
        PrepareData prepareData = new PrepareData();
        prepareData.preparePropMappingCustom();
    }
}
