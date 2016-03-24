package com.voyageone.task2.cms.bean;

import com.voyageone.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujiaye on 16/1/29.
 */
public class TmpOldCmsDataBean {
    private String channel_id;
    private String cart_id;
    private String code;
    private String model;
    private String category_path;
    private String product_id;
    private String num_iid;
    private String title_en;
    private String title_cn;
    private String description_en;
    private String description_cn;
    private String description_cn_short;
    private String img1; // 商品图片
    private String img2; // 包装图片
    private String img3; // 带角度图片
    private String img4; // 自定义图片
    private String color_en;
    private String hs_code_pu; // 个人行邮税号
    private int translate_status; // 是否已翻译完毕(0:未翻译 1:已翻译)
    private Double price_sale; // 真实售卖的价格
    private int finish_flg; // 已经处理过了的商品为1, 等待处理的为0

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCategory_path() {
        return category_path;
    }

    public void setCategory_path(String category_path) {
        this.category_path = category_path;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(String num_iid) {
        this.num_iid = num_iid;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getTitle_cn() {
        return title_cn;
    }

    public void setTitle_cn(String title_cn) {
        this.title_cn = title_cn;
    }

    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    public String getDescription_cn() {
        return description_cn;
    }

    public void setDescription_cn(String description_cn) {
        this.description_cn = description_cn;
    }

    public String getDescription_cn_short() {
        return description_cn_short;
    }

    public void setDescription_cn_short(String description_cn_short) {
        this.description_cn_short = description_cn_short;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }

    public String getColor_en() {
        return color_en;
    }

    public void setColor_en(String color_en) {
        this.color_en = color_en;
    }

    public String getHs_code_pu() {
        return hs_code_pu;
    }

    public void setHs_code_pu(String hs_code_pu) {
        this.hs_code_pu = hs_code_pu;
    }

    public int getTranslate_status() {
        return translate_status;
    }

    public void setTranslate_status(int translate_status) {
        this.translate_status = translate_status;
    }

    public Double getPrice_sale() {
        return price_sale;
    }

    public void setPrice_sale(Double price_sale) {
        this.price_sale = price_sale;
    }

    public int getFinish_flg() {
        return finish_flg;
    }

    public void setFinish_flg(int finish_flg) {
        this.finish_flg = finish_flg;
    }

    /**
     * 把以逗号分隔的字符串, 比如这样的格式[xxx, yyy, zzz, aaa, bbb, ccc], 转换为字符串列表
     * @param imgListString 比如这样的格式[xxx, yyy, zzz, aaa, bbb, ccc]
     * @return 字符串列表
     */
    public List<String> getImageList(String imgListString) {
        List<String> result = new ArrayList<>();

        if (StringUtils.isEmpty(imgListString.trim())) {
            return result;
        }

        String[] imgSplit = imgListString.split(",");

        for (String img : imgSplit) {
            result.add(img.trim());
        }

        return result;

    }
}
