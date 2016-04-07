package com.voyageone.components.jumei.Bean;

/**
 * Created by dell on 2016/3/29.
 */
public class HtDealCopyDeal_DealInfo { //发货仓库
    int shipping_system_id;

    int user_purchase_limit;//            限购数量

    String product_long_name;//可选 产品长标题  参数范围: 注：130　用于详情页显示，商品名+功效特点描述，不能出现价格及促销信息

    String product_medium_name;// 可选String  产品中标题    参数范围: 注：35字　用于首页、列表页显示，填写商品名+功效的一句话描述，不能出现价格及促销信息
    String product_short_name;// 可选	String    产品短标题    参数范围: 注：15字　用于购物车、移动客户端等，填写产品名如果字数超过，需酌情删减部分信息

    String attribute;//可选	String 生产地区  参数范围: 注：150字以内

    String before_date;// 可选	String   保质期限   参数范围: 注：150字以内
    String suit_people;// 可选	String    适用人群    参数范围: 注：150字以内
    String special_explain;// 可选	String  特殊说明  参数范围: 注：150字以内

    String search_meta_text_custom;// 可选	String  自定义搜索词
    /*    参数范围: 注：富文本HTML代码,不能引用外链、不能用JS代码;
    1）所有图片不要使用中国模特，如有代言人必须是当年的，为避免后期旧品上架困难，也请尽量不要使用。图片宽660 - 790px，下同￿
            2）文字说明请放在本单详情底部，字体要求：黑色，16号，微软雅黑。
*/
    String description_properties;// 可选	String 本单详情
    /*           使用方法
//
//    参数范围: 注：富文本HTML代码,不能引用外链、不能用JS代码;
//    1）简要描述产品的使用方法、使用步骤、使用中可能出现的不良反应等，最好有示意图。￿
//            2）如果是套装需要逐条写上使用方法，每个产品名加粗。字体要求：黑色，12号，宋体。￿
//            3）护肤品必须在最后附上“温馨提示：护肤品成分各有不同，敏感性肌肤请先在耳后测试后再使用哦！”，否则审核不能通过。
*/
    String description_usage;// 可选	String
    /*
    //            商品实拍
    //
    //    参数范围: 注：富文本HTML代码,不能引用外链、不能用JS代码；
    //            1）每一张实拍图下最好有一行文字说明，字体要求：黑色，16号， 微软雅黑。 实拍图模板下载（带尺子）》￿
    //            2）带包装和不带包装的正面、侧面、背面图都要有，包装上的文字必须清晰可辨。至少一张带尺子的实拍，让用户直观了解到商品尺寸。￿
    //            3）多个SKU时可合在一起拍也可分开拍摄，彩妆除外观实拍外还需要有近距离效果试用图。
    //
    * */
    String description_images;// 可选
    ////    售卖SKU(全量覆盖)
//
//    参数范围: 注：全量覆盖; 多个sku_no以","隔开
//}
    String jumei_sku_no;// 可选	String
    public int getShipping_system_id() {
        return shipping_system_id;
    }

    public void setShipping_system_id(int shipping_system_id) {
        this.shipping_system_id = shipping_system_id;
    }

    public int getUser_purchase_limit() {
        return user_purchase_limit;
    }

    public void setUser_purchase_limit(int user_purchase_limit) {
        this.user_purchase_limit = user_purchase_limit;
    }

    public String getProduct_long_name() {
        return product_long_name;
    }

    public void setProduct_long_name(String product_long_name) {
        this.product_long_name = product_long_name;
    }

    public String getProduct_medium_name() {
        return product_medium_name;
    }

    public void setProduct_medium_name(String product_medium_name) {
        this.product_medium_name = product_medium_name;
    }

    public String getProduct_short_name() {
        return product_short_name;
    }

    public void setProduct_short_name(String product_short_name) {
        this.product_short_name = product_short_name;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getBefore_date() {
        return before_date;
    }

    public void setBefore_date(String before_date) {
        this.before_date = before_date;
    }

    public String getSuit_people() {
        return suit_people;
    }

    public void setSuit_people(String suit_people) {
        this.suit_people = suit_people;
    }

    public String getSpecial_explain() {
        return special_explain;
    }

    public void setSpecial_explain(String special_explain) {
        this.special_explain = special_explain;
    }

    public String getSearch_meta_text_custom() {
        return search_meta_text_custom;
    }

    public void setSearch_meta_text_custom(String search_meta_text_custom) {
        this.search_meta_text_custom = search_meta_text_custom;
    }

    public String getDescription_properties() {
        return description_properties;
    }

    public void setDescription_properties(String description_properties) {
        this.description_properties = description_properties;
    }

    public String getDescription_usage() {
        return description_usage;
    }

    public void setDescription_usage(String description_usage) {
        this.description_usage = description_usage;
    }

    public String getDescription_images() {
        return description_images;
    }

    public void setDescription_images(String description_images) {
        this.description_images = description_images;
    }

    public String getJumei_sku_no() {
        return jumei_sku_no;
    }

    public void setJumei_sku_no(String jumei_sku_no) {
        this.jumei_sku_no = jumei_sku_no;
    }
}
