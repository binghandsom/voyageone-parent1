package com.voyageone.service.model.cms.mongo.jm.promotion;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

import java.util.List;

/**
 * 按渠道拆分的，聚美专场活动使用的，飘窗数据模型
 *
 * Created by jonas on 2016/10/14.
 *
 * @author jonas
 * @version 2.8.0
 * @since 2.8.0
 */
public class CmsBtJmBayWindowModel extends ChannelPartitionModel {

    private Boolean fixed;

    private Integer cartId;

    private Integer promotionId;

    private Integer jmPromotionId;

    private List<BayWindow> bayWindows;

    public Boolean getFixed() {
        return fixed;
    }

    public void setFixed(Boolean fixed) {
        this.fixed = fixed;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public Integer getJmPromotionId() {
        return jmPromotionId;
    }

    public void setJmPromotionId(Integer jmPromotionId) {
        this.jmPromotionId = jmPromotionId;
    }

    public List<BayWindow> getBayWindows() {
        return bayWindows;
    }

    public void setBayWindows(List<BayWindow> bayWindows) {
        this.bayWindows = bayWindows;
    }

    public static class BayWindow {

        private String name;

        private String link;

        private Integer order;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }
    }
}
