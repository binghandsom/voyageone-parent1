package com.voyageone.ims.rule_expression;

/**
 * 获取SxData里的product列表里的商品信息
 * @isMain 1或0：主商品， 非主商品
 * @codeIdx 主商品的场合忽略， 非主商品的场合， 是指第几个商品（主商品不算）
 * @dataType prop或image：common里的项目， 或者图片
 * @propName 当数据类型是text的场合， 这个项目就是common里的属性名称
 * @imageType 当数据类型是image的场合， 这个项目就是指图片类型（商品图之类的）
 * @imageIdx 当数据类型是image的场合， 第几张图片
 */
public class CustomModuleUserParamGetProductFieldInfo extends CustomModuleUserParam {
    //user param
    private RuleExpression isMain;
    private RuleExpression codeIdx;
    private RuleExpression dataType;
    private RuleExpression propName;
    private RuleExpression imageType;
    private RuleExpression imageIdx;

    public CustomModuleUserParamGetProductFieldInfo() {}

    public CustomModuleUserParamGetProductFieldInfo(RuleExpression isMain,
                                                    RuleExpression codeIdx,
                                                    RuleExpression dataType,
                                                    RuleExpression propName,
                                                    RuleExpression imageType,
                                                    RuleExpression imageIdx) {
        this.isMain = isMain;
        this.codeIdx = codeIdx;
        this.dataType = dataType;
        this.propName = propName;
        this.imageType = imageType;
        this.imageIdx = imageIdx;
    }

    public RuleExpression getIsMain() {
        return isMain;
    }

    public void setIsMain(RuleExpression isMain) {
        this.isMain = isMain;
    }

    public RuleExpression getCodeIdx() {
        return codeIdx;
    }

    public void setCodeIdx(RuleExpression codeIdx) {
        this.codeIdx = codeIdx;
    }

    public RuleExpression getDataType() {
        return dataType;
    }

    public void setDataType(RuleExpression dataType) {
        this.dataType = dataType;
    }

    public RuleExpression getPropName() {
        return propName;
    }

    public void setPropName(RuleExpression propName) {
        this.propName = propName;
    }

    public RuleExpression getImageType() {
        return imageType;
    }

    public void setImageType(RuleExpression imageType) {
        this.imageType = imageType;
    }

    public RuleExpression getImageIdx() {
        return imageIdx;
    }

    public void setImageIdx(RuleExpression imageIdx) {
        this.imageIdx = imageIdx;
    }
}
