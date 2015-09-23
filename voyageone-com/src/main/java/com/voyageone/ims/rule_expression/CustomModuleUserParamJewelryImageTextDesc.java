package com.voyageone.ims.rule_expression;

/**
 * Created by Leo on 15-6-18.
 */
public class CustomModuleUserParamJewelryImageTextDesc extends CustomModuleUserParam {
    //user param
    private RuleExpression htmlTemplate;
    private RuleExpression imageTextTemplate;
    private RuleExpression handMadeImageTemplate;
    private RuleExpression tipImageUrl;

    public CustomModuleUserParamJewelryImageTextDesc() { }

    public CustomModuleUserParamJewelryImageTextDesc(RuleExpression htmlTemplate, RuleExpression imageTextTemplate, RuleExpression handMadeImageTemplate, RuleExpression tipImageUrl) {
        this.htmlTemplate = htmlTemplate;
        this.imageTextTemplate = imageTextTemplate;
        this.handMadeImageTemplate = handMadeImageTemplate;
        this.tipImageUrl = tipImageUrl;
    }

    public RuleExpression getHtmlTemplate() {
        return htmlTemplate;
    }

    public void setHtmlTemplate(RuleExpression htmlTemplate) {
        this.htmlTemplate = htmlTemplate;
    }

    public RuleExpression getImageTextTemplate() {
        return imageTextTemplate;
    }

    public void setImageTextTemplate(RuleExpression imageTextTemplate) {
        this.imageTextTemplate = imageTextTemplate;
    }

    public RuleExpression getHandMadeImageTemplate() {
        return handMadeImageTemplate;
    }

    public void setHandMadeImageTemplate(RuleExpression handMadeImageTemplate) {
        this.handMadeImageTemplate = handMadeImageTemplate;
    }

    public RuleExpression getTipImageUrl() {
        return tipImageUrl;
    }

    public void setTipImageUrl(RuleExpression tipImageUrl) {
        this.tipImageUrl = tipImageUrl;
    }
}
