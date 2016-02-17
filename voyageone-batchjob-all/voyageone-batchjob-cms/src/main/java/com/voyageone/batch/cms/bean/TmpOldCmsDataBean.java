package com.voyageone.batch.cms.bean;

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
    private String title_cn;
    private String description_cn;
    private String img1; // 商品图片
    private String img2; // 包装图片
    private String img3; // 带角度图片
    private String img4; // 自定义图片
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

    public String getTitle_cn() {
        return title_cn;
    }

    public void setTitle_cn(String title_cn) {
        this.title_cn = title_cn;
    }

    public String getDescription_cn() {
        return description_cn;
    }

    public void setDescription_cn(String description_cn) {
        this.description_cn = description_cn;
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
