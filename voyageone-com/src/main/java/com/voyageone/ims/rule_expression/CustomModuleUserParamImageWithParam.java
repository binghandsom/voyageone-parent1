package com.voyageone.ims.rule_expression;

import java.util.List;

/**
 * @htmlTemplate html模板，如果该值为空，那么parse直接返回图片的url, 如果不为空，那么图片套用这个html模板，如果有
 *               多张图，那么每个图片都套用这个html模板，并拼接好返回
 * @imageTemplate 图片模板，该值不能为空
 * @imageType     图片类型, 参见CmsCodeEnum， 该值不能为空
 */
public class CustomModuleUserParamImageWithParam extends CustomModuleUserParam {
    //user param
    private RuleExpression imageTemplate;
    private List<RuleExpression> imageParams;

    public CustomModuleUserParamImageWithParam() {}

    public CustomModuleUserParamImageWithParam(RuleExpression imageTemplate, List<RuleExpression> imageParams) {
        this.imageTemplate = imageTemplate;
        this.imageParams = imageParams;
    }

    public List<RuleExpression> getImageParams() {
        return imageParams;
    }

    public void setImageParams(List<RuleExpression> imageParams) {
        this.imageParams = imageParams;
    }

    public RuleExpression getImageTemplate() {
        return imageTemplate;
    }

    public void setImageTemplate(RuleExpression imageTemplate) {
        this.imageTemplate = imageTemplate;
    }
}
