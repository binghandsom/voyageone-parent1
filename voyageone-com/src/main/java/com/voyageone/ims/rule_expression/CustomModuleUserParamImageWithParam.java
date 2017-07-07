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
    // addedby morse.lu 2016/07/13 start
    // 如果有值并且为true，则图片url不使用imageTemplate，而使用图片模板cms_bt_image_template表取出的url
    private RuleExpression useCmsBtImageTemplate;
    // addedby morse.lu 2016/07/13 end
    // added by morse.lu 2016/09/19 start
    private RuleExpression viewType;
    // added by morse.lu 2016/09/19 end
    // added by charis.li 2017/07/05 STA
    private RuleExpression htmlTemplate;
    // added by charis.li 2017/07/05 END

    public CustomModuleUserParamImageWithParam() {}

    public CustomModuleUserParamImageWithParam(RuleExpression imageTemplate, List<RuleExpression> imageParams, RuleExpression useCmsBtImageTemplate, RuleExpression viewType, RuleExpression htmlTemplate) {
        this.imageTemplate = imageTemplate;
        this.imageParams = imageParams;
        this.useCmsBtImageTemplate = useCmsBtImageTemplate;
        this.viewType = viewType;
        this.htmlTemplate = htmlTemplate;
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

    public RuleExpression getUseCmsBtImageTemplate() {
        return useCmsBtImageTemplate;
    }

    public void setUseCmsBtImageTemplate(RuleExpression useCmsBtImageTemplate) {
        this.useCmsBtImageTemplate = useCmsBtImageTemplate;
    }

    public RuleExpression getViewType() {
        return viewType;
    }

    public void setViewType(RuleExpression viewType) {
        this.viewType = viewType;
    }

    public RuleExpression getHtmlTemplate() {
        return htmlTemplate;
    }

    public void setHtmlTemplate(RuleExpression htmlTemplate) {
        this.htmlTemplate = htmlTemplate;
    }
}
