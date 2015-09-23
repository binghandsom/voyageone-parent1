package com.voyageone.ims.rule_expression;

/**
 * @htmlTemplate html模板，如果该值为空，那么parse直接返回图片的url, 如果不为空，那么图片套用这个html模板，如果有
 *               多张图，那么每个图片都套用这个html模板，并拼接好返回
 * @imageTemplate 图片模板，该值不能为空
 * @imageType     图片类型, 参见CmsCodeEnum， 该值不能为空
 */
public class CustomModuleUserParamImageFormat extends CustomModuleUserParam {
    //user param
    private RuleExpression htmlTemplate;
    private RuleExpression imageUrl;

    public CustomModuleUserParamImageFormat() {}

    public CustomModuleUserParamImageFormat(RuleExpression htmlTemplate, RuleExpression imageUrl) {
        this.htmlTemplate = htmlTemplate;
        this.imageUrl = imageUrl;
    }

    public RuleExpression getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(RuleExpression imageUrl) {
        this.imageUrl = imageUrl;
    }

    public RuleExpression getHtmlTemplate() {
        return htmlTemplate;
    }

    public void setHtmlTemplate(RuleExpression htmlTemplate) {
        this.htmlTemplate = htmlTemplate;
    }
}
