package com.voyageone.ims.rule_expression;

/**
 * Created by Leo on 15-6-18.
 * @htmlTemplate html模板，如果该值为空，那么parse直接返回图片的url, 如果不为空，那么图片套用这个html模板，如果有
 *               多张图，那么每个图片都套用这个html模板，并拼接好返回
 * @imageTemplate 图片模板，该值不能为空
 * @imageIndex    第几张图片，该值如果为空或者值为负数，那么返回所有的图片
 * @imageType     图片类型, 参见FieldImageType， 该值不能为空
 * @paddingExpression 图片取不到时的默认值
 * @paddingIsUrl paddingExpression里设定的是否是完整url. false或空：imageTemplate的%s用paddingExpression取到的值替换  true：直接使用paddingExpression取到的值
 * @useOriUrl 是否用原图
 */
public class CustomModuleUserParamGetMainPrductImages extends CustomModuleUserParam {
    //user param
    private RuleExpression htmlTemplate;
    private RuleExpression imageTemplate;
    private RuleExpression imageIndex;
    private RuleExpression imageType;
    private RuleExpression paddingExpression;
    private RuleExpression paddingIsUrl;
    private RuleExpression useOriUrl;

    public CustomModuleUserParamGetMainPrductImages() { }

    public CustomModuleUserParamGetMainPrductImages(RuleExpression htmlTemplate, RuleExpression imageTemplate, RuleExpression imageIndex, RuleExpression imageType, RuleExpression paddingExpression, RuleExpression paddingIsUrl, RuleExpression useOriUrl) {
        this.htmlTemplate = htmlTemplate;
        this.imageTemplate = imageTemplate;
        this.imageIndex = imageIndex;
        this.imageType = imageType;
        this.paddingExpression = paddingExpression;
        this.paddingIsUrl = paddingIsUrl;
        this.useOriUrl = useOriUrl;
    }

    public RuleExpression getPaddingExpression() {
        return paddingExpression;
    }

    public void setPaddingExpression(RuleExpression paddingExpression) {
        this.paddingExpression = paddingExpression;
    }

    public RuleExpression getHtmlTemplate() {
        return htmlTemplate;
    }

    public void setHtmlTemplate(RuleExpression htmlTemplate) {
        this.htmlTemplate = htmlTemplate;
    }

    public RuleExpression getImageTemplate() {
        return imageTemplate;
    }

    public void setImageTemplate(RuleExpression imageTemplate) {
        this.imageTemplate = imageTemplate;
    }

    public RuleExpression getImageType() {
        return imageType;
    }

    public void setImageType(RuleExpression imageType) {
        this.imageType = imageType;
    }

    public RuleExpression getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(RuleExpression imageIndex) {
        this.imageIndex = imageIndex;
    }

    public RuleExpression getUseOriUrl() {
        return useOriUrl;
    }

    public void setUseOriUrl(RuleExpression useOriUrl) {
        this.useOriUrl = useOriUrl;
    }

    public RuleExpression getPaddingIsUrl() {
        return paddingIsUrl;
    }

    public void setPaddingIsUrl(RuleExpression paddingIsUrl) {
        this.paddingIsUrl = paddingIsUrl;
    }
}
