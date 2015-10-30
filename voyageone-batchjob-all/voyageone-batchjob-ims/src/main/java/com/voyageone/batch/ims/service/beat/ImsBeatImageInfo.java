package com.voyageone.batch.ims.service.beat;

import com.voyageone.batch.ims.bean.BeatPicBean;
import com.voyageone.common.configs.beans.ShopBean;
import org.apache.commons.lang3.StringUtils;

/**
 * 价格披露使用的,商品的简要图片信息
 * Created by Jonas on 10/29/15.
 */
public class ImsBeatImageInfo {

    private String channel_id;

    private String code;

    private String url_key;

    private String image_name;

    private int image_id;

    private BeatPicBean beatInfo;

    private String categoryTid;

    private ShopBean shop;

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl_key() {
        return url_key;
    }

    public void setUrl_key(String url_key) {
        this.url_key = url_key;
    }

    public String getImage_name() {

        if (!StringUtils.isEmpty(image_name)) return image_name;

        return String.format("%s-%s", getUrl_key(), getImage_id());
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public void setBeatInfo(BeatPicBean beatInfo) {
        this.beatInfo = beatInfo;
    }

    public BeatPicBean getBeatInfo() {
        return beatInfo;
    }

    public void setCategoryTid(String categoryTid) {
        this.categoryTid = categoryTid;
    }

    public String getCategoryTid() {
        return categoryTid;
    }

    public void setShop(ShopBean shop) {
        this.shop = shop;
    }

    public ShopBean getShop() {
        return shop;
    }

    public String getTitle() {
        // 根据具体情况来生成 title
        // 拼接图片标题的规则，暂时这里固定写死，后续的功能会将此处处理移动到别处。
        // 对于规则，为了防止可配置方式导致后续查找图片的失败。这里规则内定于程序内。不轻易修改
        // !!! 请注意此处规则不要轻易修改，见上一行
        switch (getBeatInfo().getBeat_flg()) {
            case Waiting:
                //打标（beat_flg为1）
                return getImage_name() + ".beat.jpg";
            case Passed:
            case Cancel:
                return getImage_name() + ".jpg";
        }
        return null;
    }

    /**
     * 为价格披露进行重写,针对整形提供特殊处理
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Integer)
            return obj.equals(getImage_id());

        return super.equals(obj);
    }
}
