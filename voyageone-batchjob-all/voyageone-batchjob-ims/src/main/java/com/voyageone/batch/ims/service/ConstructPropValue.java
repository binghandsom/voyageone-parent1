package com.voyageone.batch.ims.service;

import com.voyageone.batch.ims.ImsConstants;
import com.voyageone.batch.ims.bean.CustomMappingType;
import com.voyageone.batch.ims.dao.*;
import com.voyageone.batch.ims.modelbean.CustomPlatformPropMapping;
import com.voyageone.batch.ims.modelbean.PlatformSkuInfoBean;
import com.voyageone.batch.ims.modelbean.PropBean;
import com.voyageone.batch.ims.modelbean.PropValueBean;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.ims.enums.CmsFieldEnum;
import com.voyageone.ims.modelbean.DictWordBean;
import com.voyageone.ims.rule_expression.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.*;

/**
 * Created by Leo on 15-7-3.
 */
public class ConstructPropValue {
    public static final String ITEM_IMAGE_TPL_URL = "http://s7d5.scene7.com/is/image/sneakerhead/JEWELRY%5F20150803%5Fx1200%5F1200x?$1200x1200$&$1200x1200$&$product=%s";
    public static final String PRODUCT_IMAGE_TPL_URL = "http://s7d5.scene7.com/is/image/sneakerhead/BHFO%5F20150819%5Fx1200%5F1200x?$1200x1200$&$1200x1200$&$proudct=%s";
    public static final String DESCRIPTION_SINGLE_IMAGE_TPL_URL = "http://s7d5.scene7.com/is/image/sneakerhead/Jewelry%5F20150819%5Fx760%5F760x?$760x760$&$JEWERLY%2D760%2D760$&$proudct=%s";
    public static final String DESCRIPTION_WENZI_IMAGE_TPL_URL = "http://s7d5.scene7.com/is/image/sneakerhead/JEWELRY%5F20150901%5Fx380%5F251x%2D1?$380%2D251$&$text05=%s&$text04=%s&$text03=%s&$text02=%s&$text01=%s";
    public static void constructProductValue(RuleJsonMapper ruleJsonMapper, PropValueDao propValueDao, ConstructPropValue util) {
        String channelId = "002";
        int level = 1;
        String levelValue = "28425";

        RuleExpression ruleExpression = new RuleExpression();

        ruleExpression.addRuleWord(new TextWord("测试商品"));
        String encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
        PropValueBean huohaoProp = util.constructInputProp(channelId, level, levelValue, 1000017054, null, encodePropValue);
        propValueDao.insertPropValue(huohaoProp);

        PropValueBean rongLiangProp = util.constructSingleCheckBox(channelId, level, levelValue, 1000017052, null, "142754303");
        propValueDao.insertPropValue(rongLiangProp);

        PropValueBean shangshishijianProp = util.constructSingleCheckBox(channelId, level, levelValue, 1000017051, null, "379886796");
        propValueDao.insertPropValue(shangshishijianProp);

        PropValueBean caizhiProp = util.constructSingleCheckBox(channelId, level, levelValue, 1000017050, null, "112997");
        propValueDao.insertPropValue(caizhiProp);

        ruleExpression.clear();
        ruleExpression.addRuleWord(new TextWord("3000"));
        encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
        PropValueBean diaopaijiaProp = util.constructInputProp(channelId, level, levelValue, 1000017049, null, encodePropValue);
        propValueDao.insertPropValue(diaopaijiaProp);

        PropValueBean beifuxitongProp = util.constructSingleCheckBox(channelId, level, levelValue, 1000017048, null, "96618850");
        propValueDao.insertPropValue(beifuxitongProp);

        PropValueBean chandiProp = util.constructSingleCheckBox(channelId, level, levelValue, 1000017047, null, "27013");
        propValueDao.insertPropValue(chandiProp);

        PropValueBean shiyongxingbieProp = util.constructSingleCheckBox(channelId, level, levelValue, 1000017045, null, "20532");
        propValueDao.insertPropValue(shiyongxingbieProp);

        PropValueBean shiyongchangjingProp = util.constructSingleCheckBox(channelId, level, levelValue, 1000017044, null, "87410187");
        propValueDao.insertPropValue(shiyongchangjingProp);

        PropValueBean beifuxitongjiegouProp = util.constructSingleCheckBox(channelId, level, levelValue, 1000017042, null, "30187378");
        propValueDao.insertPropValue(beifuxitongjiegouProp);

        ruleExpression.clear();
        ruleExpression.addRuleWord(new TextWord("10米"));
        encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
        PropValueBean chicunProp = util.constructInputProp(channelId, level, levelValue, 1000017041, null, encodePropValue);
        propValueDao.insertPropValue(chicunProp);

        PropValueBean shifoupeibeiyuzhaoProp = util.constructSingleCheckBox(channelId, level, levelValue, 1000017040, null, "21958");
        propValueDao.insertPropValue(shifoupeibeiyuzhaoProp);

        PropValueBean shifoushuihuyuliucangProp = util.constructSingleCheckBox(channelId, level, levelValue, 1000017039, null, "21958");
        propValueDao.insertPropValue(shifoushuihuyuliucangProp);

        ruleExpression.clear();
        ruleExpression.addRuleWord(new TextWord("商品系列"));
        encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
        PropValueBean shangpinxilieProp = util.constructInputProp(channelId, level, levelValue, 1000017038, null, encodePropValue);
        propValueDao.insertPropValue(shangpinxilieProp);

        ruleExpression.clear();
        ruleExpression.addRuleWord(new TextWord("5000"));
        encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
        PropValueBean shichangjiageProp = util.constructInputProp(channelId, level, levelValue, 1000017036, null, encodePropValue);
        propValueDao.insertPropValue(shichangjiageProp);

        Map<PropBean, Object> chanpintupianMap = new HashMap<>();
        //产品图片模板
        RuleExpression productImageTplExpression = new RuleExpression();
        productImageTplExpression.addRuleWord(new TextWord("http://image.sneakerhead.com/is/image/sneakerhead/1200templates?$1200x1200$&$img="));
        DictWord productImageTpl = new DictWord("主产品图片模板", productImageTplExpression, false);
        System.out.println(ruleJsonMapper.serializeRuleWord(productImageTpl));

        /*
        //产品图片-1
        RuleExpression productImage1Expression1 = new RuleExpression();
        productImage1Expression1.addRuleWord(new DictWord("主产品图片模板"));
        RuleExpression imageKeyParam = new RuleExpression();
        imageKeyParam.addRuleWord(new TextWord("1"));
        CustomWord getImageByCodeCustomWord1 = new CustomWord(new CustomWordValueGetMainProductImageKey(imageKeyParam));
        productImage1Expression1.addRuleWord(getImageByCodeCustomWord1);
        DictWord productImage1 = new DictWord("主产品图片-1", productImage1Expression1, true);

        //产品图片-2
        RuleExpression productImage1Expression2 = new RuleExpression();
        imageKeyParam.clear();
        imageKeyParam.addRuleWord(new TextWord("2"));
        CustomWord getImageByCodeCustomWord2 = new CustomWord(new CustomWordValueGetMainProductImageKey(imageKeyParam));
        productImage1Expression2.addRuleWord(new DictWord("主产品图片模板"));
        productImage1Expression2.addRuleWord(getImageByCodeCustomWord2);
        DictWord productImage2 = new DictWord("主产品图片-2", productImage1Expression2, true);

        PropBean chanpinProp = new PropBean();
        chanpinProp.setPropType(ImsConstants.PlatformPropType.C_COMPLEX);
        chanpinProp.setPropId(1000017037);

        PropBean chanpinProp1 = new PropBean();
        chanpinProp1.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        chanpinProp1.setPropId(1000017055);
        PropBean chanpinProp2 = new PropBean();
        chanpinProp2.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        chanpinProp2.setPropId(1000017056);
        RuleExpression productImageExpression1 = new RuleExpression();
        productImageExpression1.addRuleWord(productImage1);
        chanpintupianMap.put(chanpinProp1, ruleJsonMapper.serializeRuleExpression(productImageExpression1));
        RuleExpression productImageExpression2 = new RuleExpression();
        productImageExpression2.addRuleWord(productImage2);
        chanpintupianMap.put(chanpinProp2, ruleJsonMapper.serializeRuleExpression(productImageExpression2));
        List<PropValueBean> propValueBeans = util.constructComplex(channelId, level, levelValue, chanpinProp, chanpintupianMap, null);

        for (PropValueBean propValue : propValueBeans) {
            propValueDao.insertPropValue(propValue);
        }*/
    }

    public static void constructItemValue(RuleJsonMapper ruleJsonMapper, PropValueDao propValueDao, ConstructPropValue util) {
        String channelId = "002";
        int level = 1;
        String levelValue = "28425";

        RuleExpression ruleExpression = new RuleExpression();

        ruleExpression.addRuleWord(new TextWord("测试商品-请勿购买"));
        String encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
        PropValueBean shangpinbiaotiProp = util.constructInputProp(channelId, level, levelValue, 1000017109, null, encodePropValue);
        propValueDao.insertPropValue(shangpinbiaotiProp);

        PropBean shangpinmaidianProp1 = new PropBean();
        shangpinmaidianProp1.setPropId(1000017151);
        shangpinmaidianProp1.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        PropBean shangpinmaidianProp2 = new PropBean();
        shangpinmaidianProp2.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        shangpinmaidianProp2.setPropId(1000017152);
        Map<PropBean, Object> shangpinmaidianMap = new HashMap<>();


        ruleExpression.clear();
        ruleExpression.addRuleWord(new TextWord("商品卖点1"));
        encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
        shangpinmaidianMap.put(shangpinmaidianProp1, encodePropValue);

        ruleExpression.clear();
        ruleExpression.addRuleWord(new TextWord("商品卖点2"));
        encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
        shangpinmaidianMap.put(shangpinmaidianProp2, encodePropValue);

        PropBean shangpinmaidianProp = new PropBean();
        shangpinmaidianProp.setPropId(1000017108);
        List<PropValueBean> shangpinmaidianPropValues = util.constructComplex(channelId, level, levelValue, shangpinmaidianProp, shangpinmaidianMap, null);
        for (PropValueBean shangpinmaidianPropValue : shangpinmaidianPropValues) {
            propValueDao.insertPropValue(shangpinmaidianPropValue);
        }

        PropValueBean shangpinzhuangtaiProp = util.constructSingleCheckBox(channelId, level, levelValue, 1000017107, null, "2");
        propValueDao.insertPropValue(shangpinzhuangtaiProp);

        ruleExpression.clear();
        ruleExpression.addRuleWord(new TextWord(new Date().toString()));
        encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
        PropValueBean kaishishijianProp = util.constructInputProp(channelId, level, levelValue, 1000017139, null, encodePropValue);
        propValueDao.insertPropValue(kaishishijianProp);

        ruleExpression.clear();
        ruleExpression.addRuleWord(new TextWord(new Date().toString()));
        encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
        PropValueBean jieshushijianProp = util.constructInputProp(channelId, level, levelValue, 1000017138, null, encodePropValue);
        propValueDao.insertPropValue(jieshushijianProp);

        PropValueBean fabuleixinProp = util.constructSingleCheckBox(channelId, level, levelValue, 1000017105, null, "b");
        propValueDao.insertPropValue(fabuleixinProp);

        PropValueBean baobeileixinProp = util.constructSingleCheckBox(channelId, level, levelValue, 1000017104, null, "5");
        propValueDao.insertPropValue(baobeileixinProp);

        List<String> shangpinmiaoshaOptions = new ArrayList<>();
        ruleExpression.clear();
        ruleExpression.addRuleWord(new TextWord("web"));
        encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
        shangpinmiaoshaOptions.add(encodePropValue);
        ruleExpression.clear();
        ruleExpression.addRuleWord(new TextWord("wap"));
        encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
        shangpinmiaoshaOptions.add(encodePropValue);
        List<PropValueBean> shangpinmiaoshaProps = util.constructMultiCheckBox(channelId, level, levelValue, 1000017102, null, shangpinmiaoshaOptions);
        for (PropValueBean shangpinmiaoshaProp : shangpinmiaoshaProps)
        {
            propValueDao.insertPropValue(shangpinmiaoshaProp);
        }

        ruleExpression.clear();
        ruleExpression.addRuleWord(new TextWord("0.8"));
        encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
        PropValueBean fandianbiliProp = util.constructInputProp(channelId, level, levelValue, 1000017101, null, encodePropValue);
        propValueDao.insertPropValue(fandianbiliProp);

        PropValueBean shangshishijianProp = util.constructSingleCheckBox(channelId, level, levelValue, 1000017100, null, "379874864");
        propValueDao.insertPropValue(shangshishijianProp);

        ruleExpression.clear();
        ruleExpression.addRuleWord(new TextWord("1"));
        encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
        PropValueBean shangpinshuliangProp = util.constructInputProp(channelId, level, levelValue, 1000017092, null, encodePropValue);
        propValueDao.insertPropValue(shangpinshuliangProp);

        ruleExpression.clear();
        ruleExpression.addRuleWord(new TextWord("900"));
        encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
        PropValueBean shangpinjiageProp = util.constructInputProp(channelId, level, levelValue, 1000017091, null, encodePropValue);
        propValueDao.insertPropValue(shangpinjiageProp);

        List<String> tiqufangshiOptions = new ArrayList<>();
        ruleExpression.clear();
        ruleExpression.addRuleWord(new TextWord("2"));
        encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
        tiqufangshiOptions.add(encodePropValue);
        List<PropValueBean> tiqufangshiProps = util.constructMultiCheckBox(channelId, level, levelValue, 1000017087, null, tiqufangshiOptions);
        for (PropValueBean tiqufangshiProp : tiqufangshiProps)
        {
            propValueDao.insertPropValue(tiqufangshiProp);
        }

        Map<PropBean, Object> suozaidiMap = new HashMap<>();
        PropBean suozaidiProp = new PropBean();
        suozaidiProp.setPropType(ImsConstants.PlatformPropType.C_COMPLEX);
        suozaidiProp.setPropId(1000017037);

        PropBean shengfenProp = new PropBean();
        shengfenProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        shengfenProp.setPropId(1000017137);
        PropBean chengshiProp = new PropBean();
        chengshiProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        chengshiProp.setPropId(1000017136);
        RuleExpression shenfenExpression = new RuleExpression();
        shenfenExpression.addRuleWord(new TextWord("上海"));
        suozaidiMap.put(shengfenProp, ruleJsonMapper.serializeRuleExpression(shenfenExpression));
        RuleExpression chengshiExpression = new RuleExpression();
        chengshiExpression.addRuleWord(new TextWord("上海"));
        suozaidiMap.put(chengshiProp, ruleJsonMapper.serializeRuleExpression(chengshiExpression));
        List<PropValueBean> suozaidiProps = util.constructComplex(channelId, level, levelValue, suozaidiProp, suozaidiMap, null);
        for (PropValueBean eachSuozaidiProp : suozaidiProps)
        {
            propValueDao.insertPropValue(eachSuozaidiProp);
        }

        PropValueBean yunfeichengdanfangshiProp = util.constructSingleCheckBox(channelId, level, levelValue, 1000017081, null, "1");
        propValueDao.insertPropValue(yunfeichengdanfangshiProp);

        PropValueBean maijiachengdanyunfeiProp = util.constructSingleCheckBox(channelId, level, levelValue, 1000017080, null, "postage");
        propValueDao.insertPropValue(maijiachengdanyunfeiProp);

        ruleExpression.clear();
        ruleExpression.addRuleWord(new TextWord("1364418490"));
        encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
        PropValueBean yunfeimobanIDProp = util.constructInputProp(channelId, level, levelValue, 1000017078, null, encodePropValue);
        propValueDao.insertPropValue(yunfeimobanIDProp);

        Map<PropBean, Object> yunfeiMap = new HashMap<>();
        PropBean yunfeiProp = new PropBean();
        yunfeiProp.setPropType(ImsConstants.PlatformPropType.C_COMPLEX);
        yunfeiProp.setPropId(1000017079);

        PropBean kuaidiProp = new PropBean();
        kuaidiProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        kuaidiProp.setPropId(1000017135);
        PropBean pingyouProp = new PropBean();
        pingyouProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        pingyouProp.setPropId(1000017134);
        PropBean EmsProp = new PropBean();
        EmsProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        EmsProp.setPropId(1000017133);

        RuleExpression kuaidiExpression = new RuleExpression();
        kuaidiExpression.addRuleWord(new TextWord("20"));
        yunfeiMap.put(kuaidiProp, ruleJsonMapper.serializeRuleExpression(kuaidiExpression));
        RuleExpression pingyouExpression = new RuleExpression();
        pingyouExpression.addRuleWord(new TextWord("30"));
        yunfeiMap.put(pingyouProp, ruleJsonMapper.serializeRuleExpression(pingyouExpression));
        RuleExpression EmsExpression = new RuleExpression();
        EmsExpression.addRuleWord(new TextWord("40"));
        yunfeiMap.put(EmsProp, ruleJsonMapper.serializeRuleExpression(EmsExpression));
        List<PropValueBean> yunfeiProps = util.constructComplex(channelId, level, levelValue, yunfeiProp, yunfeiMap, null);
        for (PropValueBean eachYunfeiProps : yunfeiProps)
        {
            propValueDao.insertPropValue(eachYunfeiProps);
        }


        PropValueBean baoxiuProp = util.constructSingleCheckBox(channelId, level, levelValue, 1000017075, null, "false");
        propValueDao.insertPropValue(baoxiuProp);

        /*
        PropValueBean fapiaoProp = util.constructSingleCheckBox(channelId, level, levelValue, 1000017074, null, "true");
        propValueDao.insertPropValue(fapiaoProp);
        */

        //======================商品描述====================================
        Map<PropBean, Object> shangpinmiaoshuMap = new HashMap<>();
        PropBean shangpinmiaoshuProp = new PropBean();
        shangpinmiaoshuProp.setPropType(ImsConstants.PlatformPropType.C_COMPLEX);
        shangpinmiaoshuProp.setPropId(1000017069);

        Map<PropBean, Object> shangpincanshuMap = new HashMap<>();
        PropBean shangpincanshuProp = new PropBean();
        shangpincanshuProp.setPropType(ImsConstants.PlatformPropType.C_COMPLEX);
        shangpincanshuProp.setPropId(1000017132);

        PropBean shangpincanshuneirongProp = new PropBean();
        shangpincanshuneirongProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        shangpincanshuneirongProp.setPropId(1000017182);
        RuleExpression shangpincanshuneirongExpression = new RuleExpression();
        shangpincanshuneirongExpression.addRuleWord(new TextWord("shangpincanshuneirong"));
        shangpincanshuMap.put(shangpincanshuneirongProp, ruleJsonMapper.serializeRuleExpression(shangpincanshuneirongExpression));
        PropBean shangpincanshupaixuzhiProp = new PropBean();
        shangpincanshupaixuzhiProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        shangpincanshupaixuzhiProp.setPropId(1000017181);
        RuleExpression shangpincanshupaixuzhiExpression = new RuleExpression();
        shangpincanshupaixuzhiExpression.addRuleWord(new TextWord("shangpincanshupaixuzhi"));
        shangpincanshuMap.put(shangpincanshupaixuzhiProp, ruleJsonMapper.serializeRuleExpression(shangpincanshupaixuzhiExpression));
        shangpinmiaoshuMap.put(shangpincanshuProp, shangpincanshuMap);

        Map<PropBean, Object> shipinzhanshiMap = new HashMap<>();
        PropBean shipinzhanshiProp = new PropBean();
        shipinzhanshiProp.setPropType(ImsConstants.PlatformPropType.C_COMPLEX);
        shipinzhanshiProp.setPropId(1000017131);

        PropBean shipinzhanshineirongProp = new PropBean();
        shipinzhanshineirongProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        shipinzhanshineirongProp.setPropId(1000017180);
        RuleExpression shipinzhanshineirongExpression = new RuleExpression();
        shipinzhanshineirongExpression.addRuleWord(new TextWord("shipinzhanshineirong"));
        shipinzhanshiMap.put(shipinzhanshineirongProp, ruleJsonMapper.serializeRuleExpression(shipinzhanshineirongExpression));
        PropBean shipinzhanshipaixuzhiProp = new PropBean();
        shipinzhanshipaixuzhiProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        shipinzhanshipaixuzhiProp.setPropId(1000017179);
        RuleExpression shipinzhanshipaixuzhiExpression = new RuleExpression();
        shipinzhanshipaixuzhiExpression.addRuleWord(new TextWord("shipinzhanshipaixuzhi"));
        shipinzhanshiMap.put(shipinzhanshipaixuzhiProp, ruleJsonMapper.serializeRuleExpression(shipinzhanshipaixuzhiExpression));
        shangpinmiaoshuMap.put(shipinzhanshiProp, shipinzhanshiMap);

        Map<PropBean, Object> motetuMap = new HashMap<>();
        PropBean motetuProp = new PropBean();
        motetuProp.setPropType(ImsConstants.PlatformPropType.C_COMPLEX);
        motetuProp.setPropId(1000017130);

        PropBean motetuneirongProp = new PropBean();
        motetuneirongProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        motetuneirongProp.setPropId(1000017178);
        RuleExpression motetuneirongExpression = new RuleExpression();
        motetuneirongExpression.addRuleWord(new TextWord("motetuneirong"));
        motetuMap.put(motetuneirongProp, ruleJsonMapper.serializeRuleExpression(motetuneirongExpression));
        PropBean motetupaixuzhiProp = new PropBean();
        motetupaixuzhiProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        motetupaixuzhiProp.setPropId(1000017177);
        RuleExpression motetupaixuzhiExpression = new RuleExpression();
        motetupaixuzhiExpression.addRuleWord(new TextWord("motetupaixuzhi"));
        motetuMap.put(motetupaixuzhiProp, ruleJsonMapper.serializeRuleExpression(motetupaixuzhiExpression));
        shangpinmiaoshuMap.put(motetuProp, motetuMap);

        Map<PropBean, Object> shangpinzhanshituMap = new HashMap<>();
        PropBean shangpinzhanshituProp = new PropBean();
        shangpinzhanshituProp.setPropType(ImsConstants.PlatformPropType.C_COMPLEX);
        shangpinzhanshituProp.setPropId(1000017131);

        PropBean shangpinzhanshituneirongProp = new PropBean();
        shangpinzhanshituneirongProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        shangpinzhanshituneirongProp.setPropId(1000017176);
        RuleExpression shangpinzhanshituneirongExpression = new RuleExpression();
        shangpinzhanshituneirongExpression.addRuleWord(new TextWord("shangpinzhanshituneirong"));
        shangpinzhanshituMap.put(shangpinzhanshituneirongProp, ruleJsonMapper.serializeRuleExpression(shangpinzhanshituneirongExpression));
        PropBean shangpinzhanshitupaixuzhiProp = new PropBean();
        shangpinzhanshitupaixuzhiProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        shangpinzhanshitupaixuzhiProp.setPropId(1000017175);
        RuleExpression shangpinzhanshitupaixuzhiExpression = new RuleExpression();
        shangpinzhanshitupaixuzhiExpression.addRuleWord(new TextWord("shangpinzhanshitupaixuzhi"));
        shangpinzhanshituMap.put(shangpinzhanshitupaixuzhiProp, ruleJsonMapper.serializeRuleExpression(shangpinzhanshitupaixuzhiExpression));
        shangpinmiaoshuMap.put(shangpinzhanshituProp, shangpinzhanshituMap);

        Map<PropBean, Object> gongnengzhanshiMap = new HashMap<>();
        PropBean gongnengzhanshiProp = new PropBean();
        gongnengzhanshiProp.setPropType(ImsConstants.PlatformPropType.C_COMPLEX);
        gongnengzhanshiProp.setPropId(1000017128);

        PropBean gongnengzhanshineirongProp = new PropBean();
        gongnengzhanshineirongProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        gongnengzhanshineirongProp.setPropId(1000017174);
        RuleExpression gongnengzhanshineirongExpression = new RuleExpression();
        gongnengzhanshineirongExpression.addRuleWord(new TextWord("gongnengzhanshineirong"));
        gongnengzhanshiMap.put(gongnengzhanshineirongProp, ruleJsonMapper.serializeRuleExpression(gongnengzhanshineirongExpression));
        PropBean gongnengzhanshipaixuzhiProp = new PropBean();
        gongnengzhanshipaixuzhiProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        gongnengzhanshipaixuzhiProp.setPropId(1000017173);
        RuleExpression gongnengzhanshipaixuzhiExpression = new RuleExpression();
        gongnengzhanshipaixuzhiExpression.addRuleWord(new TextWord("gongnengzhanshipaixuzhi"));
        gongnengzhanshiMap.put(gongnengzhanshipaixuzhiProp, ruleJsonMapper.serializeRuleExpression(gongnengzhanshipaixuzhiExpression));
        shangpinmiaoshuMap.put(gongnengzhanshiProp, gongnengzhanshiMap);

        Map<PropBean, Object> xijietuMap = new HashMap<>();
        PropBean xijietuProp = new PropBean();
        xijietuProp.setPropType(ImsConstants.PlatformPropType.C_COMPLEX);
        xijietuProp.setPropId(1000017127);

        PropBean xijietuneirongProp = new PropBean();
        xijietuneirongProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        xijietuneirongProp.setPropId(1000017172);
        RuleExpression xijietuneirongExpression = new RuleExpression();
        xijietuneirongExpression.addRuleWord(new TextWord("xijietuneirong"));
        xijietuMap.put(xijietuneirongProp, ruleJsonMapper.serializeRuleExpression(xijietuneirongExpression));
        PropBean xijietupaixuzhiProp = new PropBean();
        xijietupaixuzhiProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        xijietupaixuzhiProp.setPropId(1000017171);
        RuleExpression xijietupaixuzhiExpression = new RuleExpression();
        xijietupaixuzhiExpression.addRuleWord(new TextWord("xijietupaixuzhi"));
        xijietuMap.put(xijietupaixuzhiProp, ruleJsonMapper.serializeRuleExpression(xijietupaixuzhiExpression));
        shangpinmiaoshuMap.put(xijietuProp, xijietuMap);

        Map<PropBean, Object> chimashuomingMap = new HashMap<>();
        PropBean chimashuomingProp = new PropBean();
        chimashuomingProp.setPropType(ImsConstants.PlatformPropType.C_COMPLEX);
        chimashuomingProp.setPropId(1000017126);

        PropBean chimashuomingneirongProp = new PropBean();
        chimashuomingneirongProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        chimashuomingneirongProp.setPropId(1000017170);
        RuleExpression chimashuomingneirongExpression = new RuleExpression();
        chimashuomingneirongExpression.addRuleWord(new TextWord("chimashuomingneirong"));
        chimashuomingMap.put(chimashuomingneirongProp, ruleJsonMapper.serializeRuleExpression(chimashuomingneirongExpression));
        PropBean chimashuomingpaixuzhiProp = new PropBean();
        chimashuomingpaixuzhiProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        chimashuomingpaixuzhiProp.setPropId(1000017169);
        RuleExpression chimashuomingpaixuzhiExpression = new RuleExpression();
        chimashuomingpaixuzhiExpression.addRuleWord(new TextWord("chimashuomingpaixuzhi"));
        chimashuomingMap.put(chimashuomingpaixuzhiProp, ruleJsonMapper.serializeRuleExpression(chimashuomingpaixuzhiExpression));
        shangpinmiaoshuMap.put(chimashuomingProp, chimashuomingMap);

        Map<PropBean, Object> shiyongshuomingMap = new HashMap<>();
        PropBean shiyongshuomingProp = new PropBean();
        shiyongshuomingProp.setPropType(ImsConstants.PlatformPropType.C_COMPLEX);
        shiyongshuomingProp.setPropId(1000017125);

        PropBean shiyongshuomingneirongProp = new PropBean();
        shiyongshuomingneirongProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        shiyongshuomingneirongProp.setPropId(1000017168);
        RuleExpression shiyongshuomingneirongExpression = new RuleExpression();
        shiyongshuomingneirongExpression.addRuleWord(new TextWord("shiyongshuomingneirong"));
        shiyongshuomingMap.put(shiyongshuomingneirongProp, ruleJsonMapper.serializeRuleExpression(shiyongshuomingneirongExpression));
        PropBean shiyongshuomingpaixuzhiProp = new PropBean();
        shiyongshuomingpaixuzhiProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        shiyongshuomingpaixuzhiProp.setPropId(1000017167);
        RuleExpression shiyongshuomingpaixuzhiExpression = new RuleExpression();
        shiyongshuomingpaixuzhiExpression.addRuleWord(new TextWord("shiyongshuomingpaixuzhi"));
        shiyongshuomingMap.put(shiyongshuomingpaixuzhiProp, ruleJsonMapper.serializeRuleExpression(shiyongshuomingpaixuzhiExpression));
        shangpinmiaoshuMap.put(shiyongshuomingProp, shiyongshuomingMap);

        Map<PropBean, Object> jianyidapeiMap = new HashMap<>();
        PropBean jianyidapeiProp = new PropBean();
        jianyidapeiProp.setPropType(ImsConstants.PlatformPropType.C_COMPLEX);
        jianyidapeiProp.setPropId(1000017124);

        PropBean jianyidapeineirongProp = new PropBean();
        jianyidapeineirongProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        jianyidapeineirongProp.setPropId(1000017166);
        RuleExpression jianyidapeineirongExpression = new RuleExpression();
        jianyidapeineirongExpression.addRuleWord(new TextWord("jianyidapeineirongProp"));
        jianyidapeiMap.put(jianyidapeineirongProp, ruleJsonMapper.serializeRuleExpression(jianyidapeineirongExpression));
        PropBean jianyidapeipaixuzhiProp = new PropBean();
        jianyidapeipaixuzhiProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        jianyidapeipaixuzhiProp.setPropId(1000017165);
        RuleExpression jianyidapeipaixuzhiExpression = new RuleExpression();
        jianyidapeipaixuzhiExpression.addRuleWord(new TextWord("jianyidapeipaixuzhiProp"));
        jianyidapeiMap.put(jianyidapeipaixuzhiProp, ruleJsonMapper.serializeRuleExpression(jianyidapeipaixuzhiExpression));
        shangpinmiaoshuMap.put(jianyidapeiProp, jianyidapeiMap);

        Map<PropBean, Object> shouhouMap = new HashMap<>();
        PropBean shouhouProp = new PropBean();
        shouhouProp.setPropType(ImsConstants.PlatformPropType.C_COMPLEX);
        shouhouProp.setPropId(1000017123);

        PropBean shouhouneirongProp = new PropBean();
        shouhouneirongProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        shouhouneirongProp.setPropId(1000017164);
        RuleExpression shouhouneirongExpression = new RuleExpression();
        shouhouneirongExpression.addRuleWord(new TextWord("shouhouneirongProp"));
        shouhouMap.put(shouhouneirongProp, ruleJsonMapper.serializeRuleExpression(shouhouneirongExpression));
        PropBean shouhoupaixuzhiProp = new PropBean();
        shouhoupaixuzhiProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        shouhoupaixuzhiProp.setPropId(1000017163);
        RuleExpression shouhoupaixuzhiExpression = new RuleExpression();
        shouhoupaixuzhiExpression.addRuleWord(new TextWord("shouhoupaixuzhiProp"));
        shouhouMap.put(shouhoupaixuzhiProp, ruleJsonMapper.serializeRuleExpression(shouhoupaixuzhiExpression));
        shangpinmiaoshuMap.put(shouhouProp, shouhouMap);


        List<PropValueBean> shangpinmiaoshuProps = util.constructComplex(channelId, level, levelValue, shangpinmiaoshuProp, shangpinmiaoshuMap, null);
        for (PropValueBean eachShangpinmiaoshuProp : shangpinmiaoshuProps)
        {
            propValueDao.insertPropValue(eachShangpinmiaoshuProp);
        }
        //======================END 商品描述=================================

        //========================物流体积===================================
        Map<PropBean, Object> shangpinwuliuzhaongliangtijiMap = new HashMap<>();
        PropBean shangpinwuliuzhaongliangtijiProp = new PropBean();
        shangpinwuliuzhaongliangtijiProp.setPropType(ImsConstants.PlatformPropType.C_COMPLEX);
        shangpinwuliuzhaongliangtijiProp.setPropId(1000017066);

        PropBean shangpinwuliutijiProp = new PropBean();
        shangpinwuliutijiProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        shangpinwuliutijiProp.setPropId(1000017113);
        RuleExpression shangpinwuliutijiExpression = new RuleExpression();
        shangpinwuliutijiExpression.addRuleWord(new TextWord("1"));
        shangpinwuliuzhaongliangtijiMap.put(shangpinwuliutijiProp, ruleJsonMapper.serializeRuleExpression(shangpinwuliutijiExpression));
        PropBean shangpinzhongliangProp = new PropBean();
        shangpinzhongliangProp.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        shangpinzhongliangProp.setPropId(1000017112);
        RuleExpression shangpinzhongliangExpression = new RuleExpression();
        shangpinzhongliangExpression.addRuleWord(new TextWord("2"));
        shangpinwuliuzhaongliangtijiMap.put(shangpinzhongliangProp, ruleJsonMapper.serializeRuleExpression(shangpinzhongliangExpression));

        List<PropValueBean> shangpinwuliuzhongliangtijiProps = util.constructComplex(channelId, level, levelValue, shangpinwuliuzhaongliangtijiProp, shangpinwuliuzhaongliangtijiMap, null);
        for (PropValueBean eachShangpinwuliuzhongliangtijiProps : shangpinwuliuzhongliangtijiProps)
        {
            propValueDao.insertPropValue(eachShangpinwuliuzhongliangtijiProps);
        }
        //=======================END 物流体积================================

        PropValueBean youxiaoqiProp = util.constructSingleCheckBox(channelId, level, levelValue, 1000017065, null, "7");
        propValueDao.insertPropValue(youxiaoqiProp);

        ruleExpression.clear();
        ruleExpression.addRuleWord(new TextWord("11100"));
        encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
        PropValueBean tianmaoxitongfuwubanbenProp = util.constructInputProp(channelId, level, levelValue, 1000017060, null, encodePropValue);
        propValueDao.insertPropValue(tianmaoxitongfuwubanbenProp);

        //商品图片
        Map<PropBean, Object> shangpintupianMap = new HashMap<>();

        RuleExpression imageIndexParamExpression = new RuleExpression();

        //产品图片-1
        RuleExpression itemImage1Expression1 = new RuleExpression();
        itemImage1Expression1.addRuleWord(new DictWord("主产品图片模板"));
        imageIndexParamExpression.clear();
        imageIndexParamExpression.addRuleWord(new TextWord("1"));
        /*
        CustomWord getImageByCodeCustomWord1 = new CustomWord(new CustomWordValueGetMainProductImageKey(imageIndexParamExpression));
        itemImage1Expression1.addRuleWord(getImageByCodeCustomWord1);
        DictWord itemImage1 = new DictWord("主产品图片-1", itemImage1Expression1, true);

        //产品图片-2
        RuleExpression productImage1Expression2 = new RuleExpression();
        imageIndexParamExpression.clear();
        imageIndexParamExpression.addRuleWord(new TextWord("2"));
        CustomWord getImageByCodeCustomWord2 = new CustomWord(new CustomWordValueGetMainProductImageKey(imageIndexParamExpression));
        productImage1Expression2.addRuleWord(new DictWord("主产品图片模板"));
        productImage1Expression2.addRuleWord(getImageByCodeCustomWord2);
        DictWord itemImage2 = new DictWord("主产品图片-2", productImage1Expression2, true);

        PropBean shangpintupianProp = new PropBean();
        shangpintupianProp.setPropType(ImsConstants.PlatformPropType.C_COMPLEX);
        shangpintupianProp.setPropId(1000017068);

        PropBean shangpintupianProp1 = new PropBean();
        shangpintupianProp1.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        shangpintupianProp1.setPropId(1000017117);
        PropBean shangpintupianProp2 = new PropBean();
        shangpintupianProp2.setPropType(ImsConstants.PlatformPropType.C_INPUT);
        shangpintupianProp2.setPropId(1000017118);
        RuleExpression itemImageExpression1 = new RuleExpression();
        itemImageExpression1.addRuleWord(itemImage1);
        shangpintupianMap.put(shangpintupianProp1, ruleJsonMapper.serializeRuleExpression(itemImageExpression1));
        RuleExpression itemImageExpression2 = new RuleExpression();
        itemImageExpression2.addRuleWord(itemImage2);
        shangpintupianMap.put(shangpintupianProp2, ruleJsonMapper.serializeRuleExpression(itemImageExpression2));
        List<PropValueBean> propValueBeans = util.constructComplex(channelId, level, levelValue, shangpintupianProp, shangpintupianMap, null);

        for (PropValueBean propValue : propValueBeans) {
            propValueDao.insertPropValue(propValue);
        }
        */

    }

    public static void constructDictInfo(DictWordDao dictWordDao) {
        RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
        String order_channel_id = "010";

        //sku属性图片模板
        RuleExpression propImageTplExpression = new RuleExpression();
        propImageTplExpression.addRuleWord(new TextWord(convertUrlToPattern(PRODUCT_IMAGE_TPL_URL)));
        DictWord propImageTpl = new DictWord("属性图片模板", propImageTplExpression, false);
        System.out.println(ruleJsonMapper.serializeRuleWord(propImageTpl));
        DictWordBean dictWordPropImageTpl = new DictWordBean();
        dictWordPropImageTpl.setName("属性图片模板");
        dictWordPropImageTpl.setValue(ruleJsonMapper.serializeRuleWord(propImageTpl));
        dictWordPropImageTpl.setOrder_channel_id(order_channel_id);
        dictWordPropImageTpl.setCreater("Leo");
        dictWordPropImageTpl.setModifier("Leo");
        dictWordDao.addDictWord(dictWordPropImageTpl);

        //产品图片模板
        RuleExpression productImageTplExpression = new RuleExpression();
        productImageTplExpression.addRuleWord(new TextWord(convertUrlToPattern(PRODUCT_IMAGE_TPL_URL)));
        DictWord productImageTpl = new DictWord("产品图片模板", productImageTplExpression, false);
        System.out.println(ruleJsonMapper.serializeRuleWord(productImageTpl));

        TextWord productImageType = new TextWord(CmsFieldEnum.CmsCodeEnum.product_image.toString());
        RuleExpression productImageTypeExpression = new RuleExpression();
        productImageTypeExpression.addRuleWord(productImageType);

        int productImageCount = 5;
        RuleExpression imageIndexParamExpressioin = new RuleExpression();
        for (int i = 1; i <= productImageCount; i++) {
            RuleExpression productImage1Expression = new RuleExpression();
            imageIndexParamExpressioin.clear();
            imageIndexParamExpressioin.addRuleWord(new TextWord(String.valueOf(i)));
            CustomWord getImageByCodeCustomWord = new CustomWord(new CustomWordValueGetMainProductImages(null, productImageTplExpression, imageIndexParamExpressioin, productImageTypeExpression, null));
            productImage1Expression.addRuleWord(getImageByCodeCustomWord);
            DictWord productImage = new DictWord("产品图片-" + String.valueOf(i), productImage1Expression, true);
            System.out.println(ruleJsonMapper.serializeRuleWord(productImage));

            DictWordBean dictWordProductImage = new DictWordBean();
            dictWordProductImage.setName("产品图片-" + String.valueOf(i));
            dictWordProductImage.setValue(ruleJsonMapper.serializeRuleWord(productImage));
            dictWordProductImage.setOrder_channel_id(order_channel_id);
            dictWordProductImage.setCreater("Leo");
            dictWordProductImage.setModifier("Leo");
            dictWordDao.addDictWord(dictWordProductImage);
        }

        //商品图片模板
        RuleExpression itemImageTplExpression = new RuleExpression();
        itemImageTplExpression.addRuleWord(new TextWord(convertUrlToPattern(ITEM_IMAGE_TPL_URL)));
        System.out.println(ruleJsonMapper.serializeRuleWord(productImageTpl));

        int itemImageCount = 5;
        for (int i = 1; i <= itemImageCount; i++) {
            //商品图片-1
            RuleExpression itemImage1Expression = new RuleExpression();
            imageIndexParamExpressioin.clear();

            imageIndexParamExpressioin.addRuleWord(new TextWord(String.valueOf(i)));
            CustomWord getImageByCodeCustomWord = new CustomWord(new CustomWordValueGetMainProductImages(null, itemImageTplExpression, imageIndexParamExpressioin, productImageTypeExpression, null));
            itemImage1Expression.addRuleWord(getImageByCodeCustomWord);
            DictWord itemImage1 = new DictWord("商品图片-" + String.valueOf(i), itemImage1Expression, true);
            System.out.println(ruleJsonMapper.serializeRuleWord(itemImage1));

            DictWordBean dictWordItemImage = new DictWordBean();
            dictWordItemImage.setName("商品图片-" + String.valueOf(i));
            dictWordItemImage.setValue(ruleJsonMapper.serializeRuleWord(itemImage1));
            dictWordItemImage.setOrder_channel_id(order_channel_id);
            dictWordItemImage.setCreater("Leo");
            dictWordItemImage.setModifier("Leo");
            dictWordDao.addDictWord(dictWordItemImage);
        }
        //详情页单品图片模板
        RuleExpression descriptionImageTplExpression = new RuleExpression();
        descriptionImageTplExpression.addRuleWord(new TextWord(convertUrlToPattern(DESCRIPTION_SINGLE_IMAGE_TPL_URL)));

        //详情页所有图片
        TextWord handMadeImageType = new TextWord(CmsFieldEnum.CmsCodeEnum.handmade_image.toString());
        RuleExpression handMadeImageTypeExpression = new RuleExpression();
        handMadeImageTypeExpression.addRuleWord(handMadeImageType);

        String descriptionImageHtmlFormat = "<div style=\"margin:15px\" ><img src=\"%s\" style=\"display:block; width:760px;\" /></div>";
        RuleExpression descriptionImageHtmlFormatExpression = new RuleExpression();
        descriptionImageHtmlFormatExpression.addRuleWord(new TextWord(descriptionImageHtmlFormat));
        CustomWord allDescriptionImages  = new CustomWord(new CustomWordValueGetAllImages(descriptionImageHtmlFormatExpression, descriptionImageTplExpression, productImageTypeExpression));

        RuleExpression descriptionExpression = new RuleExpression();

        /* descriptionExpression.addRuleWord(new TextWord("<div style=\"margin-left:auto; margin-right:auto; width:790px;\">\n" +
                "            <div style=\"background-color:rgb(246,241,237); margin-bottom:15px\"name=\"精品描述\">\n" +
                "                <div><img style=\"width:760px\" src=\"https://img.alicdn.com/imgextra/i3/2183719539/TB2GLTheVXXXXc6XpXXXXXXXXXX_!!2183719539.jpg\"/></div>\n" +
                "                <div style=\"text-align:center;margin-left:52px; margin-right:52px; line-height:200%\">")); */
        descriptionExpression.addRuleWord(new TextWord("<div style=\"margin-left:auto; margin-right:auto; width:790px;\">\n" +
                "            <div style=\"background-color:rgb(246,241,237); margin-bottom:15px\"name=\"精品描述\">\n" +
                "                <div><img style=\"width:760px\" src=\"https://img.alicdn.com/imgextra/i4/2641101981/TB2NcXifXXXXXb3XXXXXXXXXXXX_!!2641101981.jpg\"/></div>\n" +
                "                <div style=\"text-align:center;margin-left:52px; margin-right:52px; line-height:200%\">"));
        descriptionExpression.addRuleWord(new TextWord("<div>"));
        descriptionExpression.addRuleWord(new MasterWord("FEED-详细说明-英"));
        descriptionExpression.addRuleWord(new TextWord("</div>"));
        descriptionExpression.addRuleWord(new TextWord("<div>"));
        descriptionExpression.addRuleWord(new CmsWord(CmsFieldEnum.CmsModelEnum.long_description_cn));
        descriptionExpression.addRuleWord(new TextWord("</div>"));
        descriptionExpression.addRuleWord(new TextWord("</div>"));
        descriptionExpression.addRuleWord(allDescriptionImages);


        descriptionExpression.addRuleWord(new TextWord("<div style=\"font-size:0; padding:0px 15px 15px 15px\">\n" +
                                                "<div style=\"display:inline-block; width:380px;border:solid 0px;\">\n"));
        RuleExpression wenzitupianTplExpression = new RuleExpression();
        wenzitupianTplExpression.addRuleWord(new TextWord(convertUrlToPattern(DESCRIPTION_WENZI_IMAGE_TPL_URL)));
        List<RuleExpression> imageParams = new ArrayList<>();
        RuleExpression imageParam1 = new RuleExpression();
        RuleExpression imageParam2 = new RuleExpression();
        RuleExpression imageParam3 = new RuleExpression();
        RuleExpression imageParam4 = new RuleExpression();
        RuleExpression imageParam5 = new RuleExpression();
        imageParam1.addRuleWord(new MasterWord("FEED-适合风格"));
        imageParams.add(imageParam1);
        imageParam2.addRuleWord(new MasterWord("FEED-适合人群"));
        imageParams.add(imageParam2);
        imageParam3.addRuleWord(new MasterWord("FEED-产品宝石"));
        imageParams.add(imageParam3);
        imageParam4.addRuleWord(new MasterWord("FEED-颜色"));
        imageParams.add(imageParam4);
        imageParam5.addRuleWord(new MasterWord("FEED-材质"));
        imageParams.add(imageParam5);

        descriptionExpression.addRuleWord(new TextWord("<img style=\"display:block; width:380px; height:251px\" src=\""));
        descriptionExpression.addRuleWord(new CustomWord(new CustomWordValueImageWithParam(wenzitupianTplExpression, imageParams)));
        descriptionExpression.addRuleWord(new TextWord("\" />"));

        RuleExpression handMadeHtmlTplExpression1 = new RuleExpression();
        RuleExpression handMadeTupianTplExpression2 = new RuleExpression();

        RuleExpression handMadeTupianIndexExpression1 = new RuleExpression();
        RuleExpression handMadeTupianIndexExpression2 = new RuleExpression();

        handMadeHtmlTplExpression1.addRuleWord(new TextWord("<img style=\"display:block; width:380px; height:509px\" src=\"%s\" />"));
        handMadeTupianTplExpression2.addRuleWord(new TextWord("<img style=\"display:block; width:380px; height:509px\" src=\"%s\" />"));
        handMadeTupianIndexExpression1.addRuleWord(new TextWord("1"));
        handMadeTupianIndexExpression2.addRuleWord(new TextWord("2"));

        RuleExpression paddingExpression = new RuleExpression();
        RuleExpression paddingPropNameExpression = new RuleExpression();
        RuleExpression paddingImageIndexExpression = new RuleExpression();
        paddingPropNameExpression.addRuleWord(new TextWord("Jewwery_handMade_image"));
        paddingImageIndexExpression.addRuleWord(new TextWord("1"));
        paddingExpression.addRuleWord(new CustomWord(new CustomWordValueGetPaddingImageKey(paddingPropNameExpression, paddingImageIndexExpression)));
        //TODO 修改handmade图片模板
        RuleExpression handMadeImageTplExpression = new RuleExpression();
        handMadeImageTplExpression.addRuleWord(new TextWord(convertUrlToPattern("http://s7d5.scene7.com/is/image/sneakerhead/Jewerly%5F20150907%5Fx380%5F509x?$308x509$&$Jewerly%5F380%2D509$&$product=%s")));
        descriptionExpression.addRuleWord(new CustomWord(new CustomWordValueGetMainProductImages(handMadeHtmlTplExpression1, handMadeImageTplExpression, handMadeTupianIndexExpression1, handMadeImageTypeExpression, paddingExpression)));
        descriptionExpression.addRuleWord(new TextWord("</div>"));
        descriptionExpression.addRuleWord(new TextWord("<div style=\"display:inline-block; width:380px;border:solid 0px;\">"));
        descriptionExpression.addRuleWord(new CustomWord(new CustomWordValueGetMainProductImages(handMadeHtmlTplExpression1, handMadeImageTplExpression, handMadeTupianIndexExpression2, handMadeImageTypeExpression, paddingExpression)));
        /*descriptionExpression.addRuleWord(new TextWord("<img style=\"display:block; width:380px; height:251px\" src=\"https://img.alicdn.com/imgextra/i1/2183719539/TB27l6teVXXXXXAXpXXXXXXXXXX_!!2183719539.jpg\" />"));*/
        descriptionExpression.addRuleWord(new TextWord("<img style=\"display:block; width:380px; height:251px\" src=\"https://img.alicdn.com/imgextra/i1/2641101981/TB26ItkfXXXXXb6XXXXXXXXXXXX_!!2641101981.jpg\" />"));
        descriptionExpression.addRuleWord(new TextWord("</div>"));
        descriptionExpression.addRuleWord(new TextWord("</div>"));
        descriptionExpression.addRuleWord(new TextWord("</div>"));
        //最后的about图片
        descriptionExpression.addRuleWord(new TextWord(
                "        <img src=\"https://img.alicdn.com/imgextra/i1/2641101981/TB2NcJufXXXXXXrXXXXXXXXXXXX_!!2641101981.jpg\" />\n"));
        /*
        descriptionExpression.addRuleWord(new TextWord(
        "        <img src=\"https://img.alicdn.com/imgextra/i4/2183719539/TB2V.DLeVXXXXXGXXXXXXXXXXXX_!!2183719539.jpg\" />\n"));*/
        descriptionExpression.addRuleWord(new TextWord("</div>"));

        String jewelryImageHtmlTemplate = "<div style=\"font-size:0; padding:0px 15px 15px 15px\">\n" +
                "                    <div style=\"display:inline-block; width:380px;border:solid 0px;\">\n" +
                "                        <img style=\"display:block; width:380px; height:251px\" src=\"%s\"/>\n" +
                "                        <img style=\"display:block; width:380px; height:509px\" src=\"%s\" />\n" +
                "                    </div>\n" +
                "                    <div style=\"display:inline-block; width:379px;border:solid 0px;\">\n" +
                "                        <img style=\"display:block; width:380px; height:509px\" src=\"%s\"/>\n" +
                "                        <img style=\"display:block; width:380px; height:251px\" src=\"%s\" />\n" +
                "                    </div>\n" +
                "                </div>";
        /*
        descriptionExpression.addRuleWord(new CustomWord(new CustomWordValueJewelryImageTextDesc(jewelryImageHtmlTemplate, "http://s7d5.scene7.com/is/image/sneakerhead/Jewelry%%5F20150819%%5Fx380%%5F251x?$380%%2D251$&$text05=%s&$text04=%s&$text03=%s&$text02=%s&$text01=%s", "http://s7d5.scene7.com/is/image/sneakerhead/BHFO%5F20150819%5Fx1200%5F1200x?$1200x1200$&$1200x1200$&$proudct=", "https://img.alicdn.com/bao/uploaded/i4/TB1bhdbHFXXXXbbaXXXXXXXXXXX_!!0-item_pic.jpg_130x130.jpg")));
        descriptionExpression.addRuleWord(new TextWord("</div></div>"));
*/
        DictWord dictWordDescription = new DictWord("详情页描述", descriptionExpression, false);
        DictWordBean dictWordBeanDescription = new DictWordBean();

        dictWordBeanDescription.setName("详情页描述");
        dictWordBeanDescription.setValue(ruleJsonMapper.serializeRuleWord(dictWordDescription));
        dictWordBeanDescription.setOrder_channel_id(order_channel_id);
        dictWordBeanDescription.setCreater("Leo");
        dictWordBeanDescription.setModifier("Leo");
        dictWordDao.addDictWord(dictWordBeanDescription);
    }

    public static void constructPlatformPropMappingCustom(PlatformPropCustomMappingDao platformPropCustomMappingDao) { CustomPlatformPropMapping customPlatformPropMapping = new CustomPlatformPropMapping();
        customPlatformPropMapping.setCartId(23);

        customPlatformPropMapping.setCustomMappingType(CustomMappingType.BRAND_INFO);
        customPlatformPropMapping.setPlatformPropId("prop_20000");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setCustomMappingType(CustomMappingType.TMALL_STYLE_CODE);
        customPlatformPropMapping.setPlatformPropId("prop_13021751");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setCustomMappingType(CustomMappingType.SKU_INFO);
        customPlatformPropMapping.setPlatformPropId("sku");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);
        customPlatformPropMapping.setPlatformPropId("prop_1627207");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);
        customPlatformPropMapping.setPlatformPropId("prop_10537981");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);
        customPlatformPropMapping.setPlatformPropId("sku_price");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);
        customPlatformPropMapping.setPlatformPropId("sku_quantity");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);
        customPlatformPropMapping.setPlatformPropId("sku_outerId");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);
        customPlatformPropMapping.setPlatformPropId("sku_barcode");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);
        customPlatformPropMapping.setPlatformPropId("prop_extend_1627207");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);
        customPlatformPropMapping.setPlatformPropId("alias_name");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);
        customPlatformPropMapping.setPlatformPropId("prop_image");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);
        customPlatformPropMapping.setPlatformPropId("prop_extend_10537981");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);
        customPlatformPropMapping.setPlatformPropId("in_prop_151018199");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("in_prop_150988152");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);
        customPlatformPropMapping.setPlatformPropId("prop_9066257");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);
        customPlatformPropMapping.setPlatformPropId("prop_extend_9066257");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("in_prop_150778146");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("sku_MarketTime");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("in_prop_148242406");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("prop_20509");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("prop_extend_20509");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("prop_14067173");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("std_size_prop_20509_-1");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("std_size_prop_20518_-1");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("custom_prop_1");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("basecolor");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("in_prop_1627207");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("std_size_extends_20509");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("std_size_extends_20518");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_tip");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_shengao");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_shengao_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_tizhong");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_tizhong_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_jiankuan");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_jiankuan_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_xiongwei");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_xiongwei_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_yaowei");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_yaowei_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_xiuchang");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_xiuchang_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_yichang");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_yichang_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_baiwei");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_baiwei_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_xiabaiwei");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_xiabaiwei_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_xiukou");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_xiukou_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_xiufei");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_xiufei_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_zhongyao");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_zhongyao_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_lingshen");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_lingshen_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_linggao");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_linggao_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_lingkuan");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_lingkuan_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_lingwei");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_lingwei_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_yuanbaihou");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_yuanbaihou_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_pingbai");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_pingbai_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_yuanbai");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setPlatformPropId("size_mapping_yuanbai_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);
        //=================================SKU_INFO END==============================

        customPlatformPropMapping.setCustomMappingType(CustomMappingType.TMALL_ITEM_QUANTITY);
        customPlatformPropMapping.setPlatformPropId("quantity");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setCustomMappingType(CustomMappingType.TMALL_ITEM_PRICE);
        customPlatformPropMapping.setPlatformPropId("price");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setCustomMappingType(CustomMappingType.TMALL_XINGHAO);
        customPlatformPropMapping.setPlatformPropId("prop_1626510");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);
        customPlatformPropMapping.setPlatformPropId("in_prop_1626510");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setCustomMappingType(CustomMappingType.TMALL_SERVICE_VERSION);
        customPlatformPropMapping.setPlatformPropId("service_version");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setCustomMappingType(CustomMappingType.TMALL_OUT_ID);
        customPlatformPropMapping.setPlatformPropId("outer_id");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setCustomMappingType(CustomMappingType.TMALL_SHOP_CATEGORY);
        customPlatformPropMapping.setPlatformPropId("seller_cids");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setCustomMappingType(CustomMappingType.PRICE_SECTION);
        customPlatformPropMapping.setPlatformPropId("prop_13618191");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);

        customPlatformPropMapping.setCustomMappingType(CustomMappingType.PRICE_SECTION);
        customPlatformPropMapping.setPlatformPropId("prop_21548");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMapping);
    }

    public static void constructTmallSkuInfo(PlatformSkuInfoDao platformSkuInfoDao) {
        PlatformSkuInfoBean tmallSkuInfo = new PlatformSkuInfoBean();
        tmallSkuInfo.setCart_id(Integer.valueOf(CartEnums.Cart.TG.getId()));

        //============================================== 模板0 BEGIN =================================================================================
        tmallSkuInfo.setProp_id("sku");
        long skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.SKU);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //颜色
        tmallSkuInfo.setProp_id("prop_1627207");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.SKU_COLOR,
                SkuTemplateSchema.SkuTemplate_0_Schema.EXTENDCOLOR_COLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_price");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.SKU_PRICE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_quantity");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.SKU_QUANTITY);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_outerId");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.SKU_OUTERID);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_barcode");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.SKU_BARCODE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_MarketTime");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.SKU_MARKET_TIME);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //颜色扩展
        tmallSkuInfo.setProp_id("prop_extend_1627207");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.EXTENDCOLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("alias_name");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.EXTENDCOLOR_ALIASNAME);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("prop_image");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.EXTENDCOLOR_IMAGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("basecolor");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.EXTENDCOLOR_BASECOLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("in_prop_1627207");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.SKU_COLOR,
                SkuTemplateSchema.SkuTemplate_0_Schema.EXTENDCOLOR_COLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);
        //============================================== 模板0 END ===================================================================================

        //============================================== 模板1 BEGIN =================================================================================
        //SKU
        tmallSkuInfo.setProp_id("sku");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_1_Schema.SKU);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_price");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_PRICE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_quantity");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_QUANTITY);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_outerId");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_OUTERID);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_barcode");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_BARCODE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("prop_9066257");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_SIZE
                , SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDSKU_SIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //戒指手寸扩展 规格
        tmallSkuInfo.setProp_id("prop_extend_9066257");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDSIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("alias_name");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDSKU_ALIAS_NAME);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //项链长度
        tmallSkuInfo.setProp_id("in_prop_150988152");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_CUSTOM_SIZE1);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //手链长度
        tmallSkuInfo.setProp_id("in_prop_151018199");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_SIZE
                , SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDSKU_SIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);
        //============================================== 模板1 END ===================================================================================


        //============================================== 模板2 BEGIN ===================================================================================
        tmallSkuInfo.setProp_id("sku");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //sku
        tmallSkuInfo.setProp_id("prop_1627207");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_COLOR,
                SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDCOLOR_COLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_price");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_PRICE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_quantity");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_QUANTITY);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_outerId");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_OUTERID);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_barcode");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_BARCODE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //颜色扩展
        tmallSkuInfo.setProp_id("prop_extend_1627207");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDCOLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("alias_name");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDCOLOR_ALIASNAME, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_ALIASNAME);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("prop_image");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDCOLOR_IMAGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("prop_10537981");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_SIZE, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_SIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("basecolor");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDCOLOR_BASECOLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);


        //尺码扩展
        tmallSkuInfo.setProp_id("prop_extend_10537981");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //尺寸
        tmallSkuInfo.setProp_id("prop_20509");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_SIZE, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_SIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //尺寸扩展
        tmallSkuInfo.setProp_id("prop_extend_20509");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //手链长度
        tmallSkuInfo.setProp_id("in_prop_151018199");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_SIZE
                , SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_SIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //项链长度
        tmallSkuInfo.setProp_id("in_prop_148242406");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_SIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //项链长度
        tmallSkuInfo.setProp_id("in_prop_150988152");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_CUSTOM_SIZE1);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);


        //项链长度
        tmallSkuInfo.setProp_id("prop_14067173");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_CUSTOM_SIZE1);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //珍珠直径
        tmallSkuInfo.setProp_id("in_prop_150778146");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_CUSTOM_SIZE2);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //============================================== 模板2 END ======================================================================================

        //============================================== 模板3 BEGIN ===================================================================================
        tmallSkuInfo.setProp_id("sku");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.SKU);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //颜色
        tmallSkuInfo.setProp_id("prop_1627207");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_COLOR,
                SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDCOLOR_COLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //颜色
        tmallSkuInfo.setProp_id("in_prop_1627207");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_COLOR,
                SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDCOLOR_COLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("std_size_prop_20509_-1");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_SIZE, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_SIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("std_size_prop_20518_-1");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_SIZE, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_SIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_price");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_PRICE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_quantity");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_QUANTITY);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_outerId");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_OUTERID);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_barcode");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_BARCODE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_MarketTime");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_MARKET_TIME);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //颜色扩展
        tmallSkuInfo.setProp_id("prop_extend_1627207");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDCOLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("prop_image");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDCOLOR_IMAGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("basecolor");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDCOLOR_BASECOLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("alias_name");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDCOLOR_ALIASNAME);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //尺码扩展
        tmallSkuInfo.setProp_id("std_size_extends_20509");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("std_size_extends_20518");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_tip");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_TIP);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_shengao");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_SHENGAO);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_shengao_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_SHENGAO_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_tizhong");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_TIZHONG);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_tizhong_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_TIZHONG_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_jiankuan");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_JIANKUAN);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_jiankuan_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_JIANKUAN_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_xiongwei");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIONGWEI);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_xiongwei_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIONGWEI_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_yaowei");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_YAOWEI);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_yaowei_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_YAOWEI_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_xiuchang");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIUCHANG);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_xiuchang_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIUCHANG_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_yichang");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_YICHANG);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_yichang_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_YICHANG_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_beikuan");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_BEIKUAN);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_beikuan_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_BEIKUAN_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_qianchang");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_QIANCHANG);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_qianchang_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_QIANCHANG_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_baiwei");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_BAIWEI);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_baiwei_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_BAIWEI_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_xiabaiwei");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIABAIWEI);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_xiabaiwei_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIABAIWEI_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_xiukou");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIUKOU);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_xiukou_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIUKOU_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_xiufei");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIUFEI);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_xiufei_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIUFEI_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_zhongyao");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_ZHONGYAO);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_zhongyao_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_ZHONGYAO_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_lingshen");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_LINGSHEN);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_lingshen_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_LINGSHEN_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_linggao");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_LINGGAO);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_linggao_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_LINGGAO_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_lingkuan");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_LINGKUAN);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_lingkuan_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_LINGKUAN_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_lingwei");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_LINGWEI);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_lingwei_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_LINGWEI_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_yuanbaihou");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_YUANBAIHOU);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_yuanbaihou_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_YUANBAIHOU_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_pingbai");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_PINGBAI);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_pingbai_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_PINGBAI_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_yuanbai");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_YUANBAI);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_yuanbai_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_YUANBAI_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("custom_prop_1");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_CUSTOM_SIZE_1);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);
        //============================================== 模板3 END ======================================================================================
    }

    public static String convertUrlToPattern(String url) {
        url = url.replace("%s", "$@_*");
        url = url.replace("%", "%%");
        url = url.replace("$@_*", "%s");
        return url;
    }

    public static void main(String args[]) {
        ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
        PropValueDao propValueDao = ctx.getBean(PropValueDao.class);
        PlatformSkuInfoDao platformSkuInfoDao = ctx.getBean(PlatformSkuInfoDao.class);
        PlatformPropCustomMappingDao platformPropCustomMappingDao = ctx.getBean(PlatformPropCustomMappingDao.class);
        DictWordDao dictWordDao = ctx.getBean(DictWordDao.class);

        RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
        ConstructPropValue util = new ConstructPropValue();

        //constructPlatformPropMappingCustom(platformPropCustomMappingDao);
        constructTmallSkuInfo(platformSkuInfoDao);
//        constructTmallSkuInfo(platformSkuInfoDao);
        /*
        constructProductValue(ruleJsonMapper, propValueDao, util);
        constructItemValue(ruleJsonMapper, propValueDao, util);
        constructPlatformPropMappingCustom(platformPropCustomMappingDao);
        constructTmallSkuInfo(platformSkuInfoDao);
        constructDictInfo(dictWordDao);
        */
        /*
        constructProductValue(ruleJsonMapper, propValueDao, util);
        constructItemValue(ruleJsonMapper, propValueDao, util);
        */
        //constructPlatformPropMappingCustom(platformPropCustomMappingDao);
//       constructTmallSkuInfo(platformSkuInfoDao);
        //constructDictInfo(dictWordDao);
    }

    private PropValueBean constructSingleCheckBox(String channelId, int level, String levelValue, int propId, String parent,
                                                  String propValue) {
        PropValueBean singleCheckBoxProp = new PropValueBean();
        singleCheckBoxProp.setChannel_id(channelId);
        singleCheckBoxProp.setProp_id(propId);
        singleCheckBoxProp.setLevel(level);
        singleCheckBoxProp.setLevel_value(levelValue);
        singleCheckBoxProp.setParent(parent);
        singleCheckBoxProp.setUuid(UUID.randomUUID().toString());

        RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
        RuleExpression ruleExpression = new RuleExpression();
        ruleExpression.addRuleWord(new TextWord(propValue));
        String encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
        singleCheckBoxProp.setProp_value(encodePropValue);

        return singleCheckBoxProp;
    }

    private PropValueBean constructInputProp(String channelId, int level, String levelValue, int propId, String parent,
                                             String propValue) {
        PropValueBean inputProp = new PropValueBean();
        inputProp.setChannel_id(channelId);
        inputProp.setProp_id(propId);
        inputProp.setLevel(level);
        inputProp.setLevel_value(levelValue);
        inputProp.setParent(parent);
        inputProp.setUuid(UUID.randomUUID().toString());

        inputProp.setProp_value(propValue);

        return inputProp;
    }

    private List<PropValueBean> constructMultiCheckBox(String channelId, int level, String levelValue, int propId, String parent,
                                                       List<String> propValues) {
        PropValueBean multiCheckBox = new PropValueBean();
        List<PropValueBean> singleCheckBoxList = new ArrayList<>();

        multiCheckBox.setChannel_id(channelId);
        multiCheckBox.setParent(parent);
        multiCheckBox.setProp_id(propId);
        multiCheckBox.setLevel(level);
        multiCheckBox.setLevel_value(levelValue);
        multiCheckBox.setUuid(UUID.randomUUID().toString());
        multiCheckBox.setProp_value(null);

        for (String propValue : propValues)
        {
            PropValueBean singleCheckBox = new PropValueBean();
            singleCheckBox.setChannel_id(channelId);
            singleCheckBox.setParent(multiCheckBox.getUuid());
            singleCheckBox.setProp_id(propId);
            singleCheckBox.setLevel(level);
            singleCheckBox.setLevel_value(levelValue);
            singleCheckBox.setProp_value(propValue);
            singleCheckBox.setUuid(UUID.randomUUID().toString());
            singleCheckBoxList.add(singleCheckBox);
        }
        singleCheckBoxList.add(multiCheckBox);

        return singleCheckBoxList;
    }

    private List<PropValueBean> constructComplex(String channelId, int level, String levelValue, PropBean prop,
                                                 Map<PropBean, Object> propMap, String parent) {
        PropValueBean complexPropValue = new PropValueBean();
        List<PropValueBean> singleTypeList = new ArrayList<>();

        complexPropValue.setChannel_id(channelId);
        complexPropValue.setParent(parent);
        complexPropValue.setProp_id(prop.getPropId());
        complexPropValue.setLevel(level);
        complexPropValue.setLevel_value(levelValue);
        complexPropValue.setUuid(UUID.randomUUID().toString());
        complexPropValue.setProp_value(null);

        for (Map.Entry<PropBean, Object> entry : propMap.entrySet())
        {
            switch (entry.getKey().getPropType())
            {
                case ImsConstants.PlatformPropType.C_INPUT:
                    singleTypeList.add(constructInputProp(channelId, level, levelValue, entry.getKey().getPropId(), complexPropValue.getUuid(), (String) (entry.getValue())));
                    break;
                case ImsConstants.PlatformPropType.C_SINGLE_CHECK:
                    singleTypeList.add(constructSingleCheckBox(channelId, level, levelValue, entry.getKey().getPropId(), complexPropValue.getUuid(), (String) (entry.getValue())));
                    break;
                case ImsConstants.PlatformPropType.C_MULTI_CHECK:
                    singleTypeList.addAll(constructMultiCheckBox(channelId, level, levelValue, entry.getKey().getPropId(), complexPropValue.getUuid(), (ArrayList) (entry.getValue())));
                    break;
                case ImsConstants.PlatformPropType.C_COMPLEX:
                    singleTypeList.addAll(constructComplex(channelId, level, levelValue, entry.getKey(), (HashMap) (entry.getValue()), complexPropValue.getUuid()));
                    break;
                case ImsConstants.PlatformPropType.C_MULTI_COMPLEX:
                    singleTypeList.addAll(constructMultiComplex(channelId, level, levelValue, entry.getKey(), (List <Map<PropBean, Object>>)(entry.getValue()), complexPropValue.getUuid()));
                    break;
            }
        }
        singleTypeList.add(complexPropValue);
        return singleTypeList;
    }

    private List<PropValueBean> constructMultiComplex(String channelId, int level, String levelValue, PropBean prop,
                                                      List<Map<PropBean, Object>> multiComplexMapList, String parent) {
        PropValueBean multiComplexPropValue = new PropValueBean();
        List<PropValueBean> singleTypeList = new ArrayList<>();

        multiComplexPropValue.setChannel_id(channelId);
        multiComplexPropValue.setParent(parent);
        multiComplexPropValue.setProp_id(prop.getPropId());
        multiComplexPropValue.setLevel(level);
        multiComplexPropValue.setLevel_value(levelValue);
        multiComplexPropValue.setUuid(UUID.randomUUID().toString());
        multiComplexPropValue.setProp_value(null);

        for (Map<PropBean, Object> complexPropMap : multiComplexMapList) {
            singleTypeList.addAll(constructComplex(channelId, level, levelValue, prop, complexPropMap, multiComplexPropValue.getUuid()));
        }
        singleTypeList.add(multiComplexPropValue);
        return singleTypeList;
    }
}
