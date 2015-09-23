package com.voyageone.cms.modelbean;

/**
 * 专用映射 ims_bt_feed_prop_mapping_selectCountsByCategory 返回的结果类型
 *
 * Created by Jonas on 9/18/15.
 */
public class CategoryPropCount {

    private long props;

    private long hasValue;

    public long getProps() {
        return props;
    }

    public void setProps(long props) {
        this.props = props;
    }

    public long getHasValue() {
        return hasValue;
    }

    public void setHasValue(long hasValue) {
        this.hasValue = hasValue;
    }
}
