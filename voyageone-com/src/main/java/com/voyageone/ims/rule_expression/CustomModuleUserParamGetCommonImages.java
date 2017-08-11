package com.voyageone.ims.rule_expression;

/**
 * cms_bt_image_group(mongoDB)取得url
 *
 * @htmlTemplate html模板，如果该值为空，那么parse直接返回图片的url, 如果不为空，那么图片套用这个html模板，如果有
 *               多张图，那么每个图片都套用这个html模板，并拼接好返回
 * @imageType  图片类型, 1:商品图 2:尺码图 3:品牌故事图 4:物流介绍图 5:店铺图 6:使用说明图 7:测量方式图
 * @viewType  1:PC端 2:APP端
 * @useOriUrl 1:使用原图 其它或者未设置，使用天猫平台图
 * @imageIndex 如果是null, 那么就获取全部, index从0开始, 指定但不存在的场合返回""
 *
 * @since 2016/06/02 morse.lu
 */
public class CustomModuleUserParamGetCommonImages extends CustomModuleUserParam {
    //user param
    private RuleExpression htmlTemplate;
    private RuleExpression imageType;
    private RuleExpression viewType;
    private RuleExpression useOriUrl;
    private RuleExpression imageIndex;

    public CustomModuleUserParamGetCommonImages() {}

    public CustomModuleUserParamGetCommonImages(RuleExpression htmlTemplate, RuleExpression imageType, RuleExpression viewType, RuleExpression useOriUrl, RuleExpression imageIndex) {
        this.htmlTemplate = htmlTemplate;
        this.imageType = imageType;
        this.viewType = viewType;
        this.useOriUrl = useOriUrl;
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

    public RuleExpression getViewType() {
        return viewType;
    }

    public void setViewType(RuleExpression viewType) {
        this.viewType = viewType;
    }

    public RuleExpression getUseOriUrl() {
        return useOriUrl;
    }

    public void setUseOriUrl(RuleExpression useOriUrl) {
        this.useOriUrl = useOriUrl;
    }

    public RuleExpression getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(RuleExpression imageIndex) {
        this.imageIndex = imageIndex;
    }
}
