package com.voyageone.ims.rule_expression;

/**
 * Created by Tom on 16-8-26.
 * 将指定的文字内容, 制作成为一张固定宽度的图片, 高度自动计算
 * 文字来源是指定字段名 (默认从platforms.Pxx.fields下面获取, 如果没有, 则从common.fields下面获取)
 * 文字如果为空, 那么就不做任何处理跳出
 * 文字如果希望有回车的话, 请用\n
 *
 */
public class CustomModuleUserParamGetDescImage extends CustomModuleUserParam {
    //user param
    private RuleExpression field;       // 数据来源字段名称
    private RuleExpression width;       // 固定宽度
    private RuleExpression startX;      // 起始位置x
    private RuleExpression startY;      // 起始位置y
    private RuleExpression sectionSize; // 行间距
    private RuleExpression fontSize;    // 文字大小
    private RuleExpression oneLineBit;  // 一行的英文单词数

    public CustomModuleUserParamGetDescImage() { }

    public CustomModuleUserParamGetDescImage(RuleExpression field, RuleExpression width, RuleExpression startX, RuleExpression startY, RuleExpression sectionSize, RuleExpression fontSize, RuleExpression oneLineBit) {
        this.field = field;             // 数据来源字段名称
        this.width = width;             // 固定宽度
        this.startX = startX;           // 起始位置x
        this.startY = startY;           // 起始位置y
        this.sectionSize = sectionSize; // 行间距
        this.fontSize = fontSize;       // 文字大小
        this.oneLineBit = oneLineBit;   // 一行的英文单词数
    }

    public RuleExpression getField() {
        return field;
    }

    public void setField(RuleExpression field) {
        this.field = field;
    }

    public RuleExpression getWidth() {
        return width;
    }

    public void setWidth(RuleExpression width) {
        this.width = width;
    }

    public RuleExpression getStartX() {
        return startX;
    }

    public void setStartX(RuleExpression startX) {
        this.startX = startX;
    }

    public RuleExpression getStartY() {
        return startY;
    }

    public void setStartY(RuleExpression startY) {
        this.startY = startY;
    }

    public RuleExpression getSectionSize() {
        return sectionSize;
    }

    public void setSectionSize(RuleExpression sectionSize) {
        this.sectionSize = sectionSize;
    }

    public RuleExpression getFontSize() {
        return fontSize;
    }

    public void setFontSize(RuleExpression fontSize) {
        this.fontSize = fontSize;
    }

    public RuleExpression getOneLineBit() {
        return oneLineBit;
    }

    public void setOneLineBit(RuleExpression oneLineBit) {
        this.oneLineBit = oneLineBit;
    }
}
