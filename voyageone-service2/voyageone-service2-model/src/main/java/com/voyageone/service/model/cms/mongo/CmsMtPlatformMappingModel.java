package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.CartPartitionModel;
import com.voyageone.service.bean.cms.MappingBean;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by james.li on 2015/12/7.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@Document
public class CmsMtPlatformMappingModel extends CartPartitionModel {

    private String channelId;

    // 主数据的类目id
    private String mainCategoryId;

    // 第三方平台的渠道id
    private int platformCartId;

    // 第三方平台的类目id
    private String platformCategoryId;

    //1 属性匹配完成  0 属性尚未匹配完成
    private int matchOver;

    private List<MappingBean> props;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getMainCategoryId() {
        return mainCategoryId;
    }

    public void setMainCategoryId(String mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

    public int getPlatformCartId() {
        return platformCartId;
    }

    public void setPlatformCartId(int platformCartId) {
        this.platformCartId = platformCartId;
    }

    public String getPlatformCategoryId() {
        return platformCategoryId;
    }

    public void setPlatformCategoryId(String platformCategoryId) {
        this.platformCategoryId = platformCategoryId;
    }

    public int getMatchOver() {
        return matchOver;
    }

    public void setMatchOver(int matchOver) {
        this.matchOver = matchOver;
    }

    public List<MappingBean> getProps() {
        return props;
    }

    public void setProps(List<MappingBean> props) {
        this.props = props;
    }

    public int getCartId() {
        return platformCartId;
    }
}
