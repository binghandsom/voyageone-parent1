/**
 * 平台枚举
 */
define(function () {

    function JmImage(attrName, attrNameZn) {
        this.attrName = attrName;
        this.attrNameZn = attrNameZn;
    }

    var JmPromotionImg = {
        pcEntrance: new JmImage('pcEntrance', '1-PC端入口图-600×320'),
        appEntrance: new JmImage('appEntrance', "2-移动端入口图-和app首页5号图一致-2048x1024"),
        wxShare: new JmImage('wxShare', "3-微信分享图-300x300"),
        channelFrontCover: new JmImage('channelFrontCover', "4-频道页封面大图-1000×490"),
        channelMiddleImage: new JmImage('channelMiddleImage', "5-频道页中图-640×420"),
        channelComingSoon: new JmImage('channelComingSoon', "6-频道页预告图-310×200"),
        mobileFocus: new JmImage('mobileFocus', "7-手机焦点图-1520x622"),
        pcHeader: new JmImage('pcHeader', "PC端——专场头图-1920x840"),
        appHeader: new JmImage('appHeader', "移动端——专场头图-640x790"),
        appChannelEntrance: new JmImage('appChannelEntrance', "phone正式,app频道页专场入口图(目前使用在首页)-2048x847"),
        appArcCarousel: new JmImage('appArcCarousel', "1-app弧形轮播-2048x1142"),
        appCarousel: new JmImage('appCarousel', "1-app轮播-1520x622"),
        pcCarousel: new JmImage('pcCarousel', "2-pc轮播-1920x350"),
        pcRow1Cell2: new JmImage('pcRow1Cell2', "3-PC一排2个-472x170"),
        appRightBigCard: new JmImage('appRightBigCard', "4-app大卡片位右-2048x1042"),
        appLeftBigCard: new JmImage('appLeftBigCard', "4-app大卡片位左-2048x1042"),
        appSmallCard: new JmImage('appSmallCard', "4-app小卡片位-1024x1024"),
        pcIndexPreheat: new JmImage('pcIndexPreheat', "7-PC首页预热-535x212"),
        pcIndex: new JmImage('pcIndex', "7-PC首页正式-535x212"),
        padCarousel: new JmImage('padCarousel', "8-ipad轮播-2048x512"),
        padPreheat: new JmImage('padPreheat', "9-ipad卡片位模板预热-732x244"),
        padCard: new JmImage('padCard', "9-ipad卡片位模板正式-732x244")
    };

    function JmPromotionImageConfig() {

        this.JmPromotionImg = JmPromotionImg;

        this.getImageType = function (ImageNameZn) {
            var self = this;

            return _.find(self.JmPromotionImg, function (element) {
                return element.attrNameZn == ImageNameZn;
            });
        }
    }

    return new JmPromotionImageConfig();

});