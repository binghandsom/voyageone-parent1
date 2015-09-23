package com.voyageone.ims.rule_expression;

/**
 * @htmlTemplate html模板，如果该值为空，那么parse直接返回图片的url, 如果不为空，那么图片套用这个html模板，如果有
 *               多张图，那么每个图片都套用这个html模板，并拼接好返回
 * @imageTemplate 图片模板，该值不能为空
 * @imageType     图片类型, 参见CmsCodeEnum， 该值不能为空
 */
public class CustomModuleUserParamGetAllImages extends CustomModuleUserParam {
    //user param
    private RuleExpression imageTemplate;
    private RuleExpression htmlTemplate;
    private RuleExpression imageType;

    public CustomModuleUserParamGetAllImages() {}

    public CustomModuleUserParamGetAllImages(RuleExpression htmlTemplate, RuleExpression imageTemplate, RuleExpression imageType) {
        this.htmlTemplate = htmlTemplate;
        this.imageTemplate = imageTemplate;
        this.imageType = imageType;
    }

    public RuleExpression getHtmlTemplate() {
        return htmlTemplate;
    }

    public void setHtmlTemplate(RuleExpression htmlTemplate) {
        this.htmlTemplate = htmlTemplate;
    }

    public RuleExpression getImageType() {
        return imageType;
    }

    public void setImageType(RuleExpression imageType) {
        this.imageType = imageType;
    }

    public RuleExpression getImageTemplate() {
        return imageTemplate;
    }

    public void setImageTemplate(RuleExpression imageTemplate) {
        this.imageTemplate = imageTemplate;
    }
}
