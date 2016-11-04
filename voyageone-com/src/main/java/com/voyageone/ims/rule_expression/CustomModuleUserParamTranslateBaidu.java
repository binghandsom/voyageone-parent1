package com.voyageone.ims.rule_expression;

/**
 * cms_bt_image_group(mongoDB)取得url
 *
 * @transTarget 翻译对象
 * @transType  是否翻译
 *              0：不翻译
 *              1or空：直接百度翻译
 *              以外的场合：检索cms_mt_feed_custom_prop,cms_mt_feed_custom_option的key,找到对应的值就用，没找到还是去百度翻译
 *              整个transType的RuleExpression为null的话，认为都是1,即全部直接百度翻译
 * @separator  分隔符 翻译后的连接符号
 * @paddingExpression 翻译对象为空时的默认值
 *
 * exp:
 *      {"ruleWordList": [{"type": "CUSTOM","value": {"moduleName": "TranslateBaidu","userParam": {"transTarget": {"ruleWordList": [{"type": "COMMON","value": "brand"},{"type": "COMMON","value": "color"},{"type": "COMMON","value": "sizeType"},{"type": "COMMON","value": "materialEn"},{"type": "COMMON","value": "model"}]},"transType": {"ruleWordList": [{"type": "TEXT","value": "0"},{"type": "TEXT","value": "1"},{"type": "TEXT","value": "1"},{"type": "TEXT","value": "material"},{"type": "TEXT","value": "0"}]},"separator": {"ruleWordList": [{"type": "TEXT","value": "---"}]},"paddingExpression": null}}}]}
 *      数据值：{"brand": "Nick", "color": "blue", "sizeType": "mens", "materialEn": "cashmere", "model": "ML565BL_D"}
 *              cashmere在cms_mt_feed_custom_option里对应的是"针织羊绒" 如果这条记录删除(即找不到了)，那么会去百度翻译得到"羊绒"
 *      result: Nick---蓝色---男装---针织羊绒---ML565BL_D  or (Nick---蓝色---男装---羊绒---ML565BL_D)
 *
 * @since 2016/06/02 morse.lu
 */
public class CustomModuleUserParamTranslateBaidu extends CustomModuleUserParam {
    //user param
    private RuleExpression transTarget;
    private RuleExpression transType;
    private RuleExpression separator;
    private RuleExpression paddingExpression;

    public CustomModuleUserParamTranslateBaidu() {}

    public CustomModuleUserParamTranslateBaidu(RuleExpression transTarget, RuleExpression transType, RuleExpression separator, RuleExpression paddingExpression) {
        this.transTarget = transTarget;
        this.transType = transType;
        this.separator = separator;
        this.paddingExpression = paddingExpression;
    }

    public RuleExpression getTransTarget() {
        return transTarget;
    }

    public void setTransTarget(RuleExpression transTarget) {
        this.transTarget = transTarget;
    }

    public RuleExpression getTransType() {
        return transType;
    }

    public void setTransType(RuleExpression transType) {
        this.transType = transType;
    }

    public RuleExpression getSeparator() {
        return separator;
    }

    public void setSeparator(RuleExpression separator) {
        this.separator = separator;
    }

    public RuleExpression getPaddingExpression() {
        return paddingExpression;
    }

    public void setPaddingExpression(RuleExpression paddingExpression) {
        this.paddingExpression = paddingExpression;
    }
}
