package com.voyageone.batch.ims.modelbean;

/**
 * Created by zhujiaye on 15/6/28.
 */
public class CategoryMappingBean {
    // 主数据的类目id
    private int categoryId;
    // 第三方平台的渠道id
    private int platformCartId;
    // 第三方平台的类目id
    private String platformCid;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getPlatformCartId() {
        return platformCartId;
    }

    public void setPlatformCartId(int platformCartId) {
        this.platformCartId = platformCartId;
    }

    public String getPlatformCid() {
        return platformCid;
    }

    public void setPlatformCid(String platformCid) {
        this.platformCid = platformCid;
    }
}
