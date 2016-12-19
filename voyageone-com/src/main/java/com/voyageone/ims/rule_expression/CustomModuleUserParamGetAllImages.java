package com.voyageone.ims.rule_expression;

/**
 * @htmlTemplate html模板，如果该值为空，那么parse直接返回图片的url, 如果不为空，那么图片套用这个html模板，如果有
 *               多张图，那么每个图片都套用这个html模板，并拼接好返回
 * @imageTemplate 图片模板，该值不能为空
 * @imageType     图片类型, 参见CmsCodeEnum， 该值不能为空
 * @useOriUrl 1:使用原图 其它或者未设置，不使用原图
 * @useCmsBtImageTemplate 是否使用图片模板，true为使用，默认不使用
 * @viewType 1:PC 2:App 默认PC (使用图片模板时才用的到)
 * @codeIndex 没有的话表示全code，有的话用指定的codeIndex(index从0开始，上新的productList里依次计数，会跳过主商品，即主商品不计数)
 *              可指定多个index，示例："codeIndex": {"ruleWordList": [{"type": "TEXT","value": "0"},{"type": "TEXT","value": "2"},{"type": "TEXT","value": "3"}]}
 * @imageIndex 没有的话表示指定的imageType下的所有图都要，有的话用指定的index(index从0开始) 示例与codeIndex一致
 */
public class CustomModuleUserParamGetAllImages extends CustomModuleUserParam {
    //user param
    private RuleExpression imageTemplate;
    private RuleExpression htmlTemplate;
    private RuleExpression imageType;
    private RuleExpression useOriUrl;
    // addedby morse.lu 2016/07/18 start
    // 如果有值并且为true，则图片url不使用imageTemplate，而使用图片模板cms_bt_image_template表取出的url
    private RuleExpression useCmsBtImageTemplate;
    // addedby morse.lu 2016/07/18 end
    // added by morse.lu 2016/09/19 start
    private RuleExpression viewType;
    // added by morse.lu 2016/09/19 end
    // added by morse.lu 2016/12/05 start
    private RuleExpression codeIndex;
    private RuleExpression imageIndex;
    // added by morse.lu 2016/12/05 end

    public CustomModuleUserParamGetAllImages() {}

    public CustomModuleUserParamGetAllImages(RuleExpression htmlTemplate, RuleExpression imageTemplate, RuleExpression imageType, RuleExpression useOriUrl, RuleExpression useCmsBtImageTemplate, RuleExpression viewType, RuleExpression codeIndex, RuleExpression imageIndex) {
        this.htmlTemplate = htmlTemplate;
        this.imageTemplate = imageTemplate;
        this.imageType = imageType;
        this.useOriUrl = useOriUrl;
        this.useCmsBtImageTemplate = useCmsBtImageTemplate;
        this.viewType = viewType;
        this.codeIndex = codeIndex;
        this.imageIndex = imageIndex;
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

    public RuleExpression getUseOriUrl() {
        return useOriUrl;
    }

    public void setUseOriUrl(RuleExpression useOriUrl) {
        this.useOriUrl = useOriUrl;
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

    public RuleExpression getCodeIndex() {
        return codeIndex;
    }

    public void setCodeIndex(RuleExpression codeIndex) {
        this.codeIndex = codeIndex;
    }

    public RuleExpression getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(RuleExpression imageIndex) {
        this.imageIndex = imageIndex;
    }
}
