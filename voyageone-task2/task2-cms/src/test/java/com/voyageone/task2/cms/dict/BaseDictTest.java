package com.voyageone.task2.cms.dict;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.ims.rule_expression.*;

/**
 * Created by dell on 2016/9/18.
 */
public class BaseDictTest {
    protected String C_TEXT_BR = "<br />";

    protected String C_TEMPLATE_IMG = "<img src=\"%s\">";
    protected String C_TEMPLATE_IMG_790 = "<img width=790px src=\"%s\">";
    protected String C_TEMPLATE_IMG_990 = "<img width=990px src=\"%s\">";
    protected String C_TEMPLATE_IMG_XIANGSHUI = "<a href=\"%s\"><img src=\"%s\"></a>";
    protected String C_商品图片 = "PRODUCT_IMAGE";
    protected String C_包装图片 = "PACKAGE_IMAGE";
    protected String C_带角度图片 = "ANGLE_IMAGE";
    protected String C_自定义图片 = "CUSTOM_IMAGE";
    protected String C_移动端自定义图片 = "MOBILE_CUSTOM_IMAGE";

    protected String C_PC端自拍商品图 = "CUSTOM_PRODUCT_IMAGE";
    protected String C_APP端自拍商品图 = "M_CUSTOM_PRODUCT_IMAGE";
    protected String C_吊牌图 = "HANG_TAG_IMAGE";
    protected String C_耐久标签 = "DURABILITY_TAG_IMAGE";

    /**
     * 取得子类Task名称
     *
     * @param cls    子类
     */
    protected String getTaskName(Class cls) {
        return cls.getName();
    }

    /**
     * 生成json
     *
     * @param title    字典名字
     * @param isUrl    生成出来的内容是否是url(一般是图片的话就是true, 其他有文字的都是false)
     * @param ruleRoot rule
     */
    protected void doCreateJson(String title, boolean isUrl, RuleExpression ruleRoot) {

        DictWord dictRoot = new DictWord();
        dictRoot.setName(title);
        dictRoot.setExpression(ruleRoot);
        dictRoot.setIsUrl(isUrl);

//        RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
//        String json = ruleJsonMapper.serializeRuleWord(dictRoot);
        String json = JacksonUtil.bean2JsonNotNull(dictRoot);

        System.out.println("=====================================");
        System.out.println("字典: " + title);
        System.out.println(json);

    }

    protected void do处理无线端20张图片(int idx, RuleExpression ruleRoot, RuleWord ruleWord) {
        String imageStr = ",\"image_hot_area_" + idx + "\":{\"item_picture_image\":\"";
        TextWord imageWord = new TextWord(imageStr);
        ruleRoot.addRuleWord(imageWord);

        ruleRoot.addRuleWord(ruleWord);

        imageStr = "\"}";
        imageWord = new TextWord(imageStr);
        ruleRoot.addRuleWord(imageWord);
    }

    protected void do处理天猫同购无线端20张图片(int idx, RuleExpression ruleRoot, RuleWord ruleWord) {
        String imageStr = ",{\"img\": \"";
        if (idx == 0) imageStr = "{\"img\": \"";
        TextWord imageWord = new TextWord(imageStr);
        ruleRoot.addRuleWord(imageWord);

        ruleRoot.addRuleWord(ruleWord);

        imageStr = "\"}";
        imageWord = new TextWord(imageStr);
        ruleRoot.addRuleWord(imageWord);
    }

}
