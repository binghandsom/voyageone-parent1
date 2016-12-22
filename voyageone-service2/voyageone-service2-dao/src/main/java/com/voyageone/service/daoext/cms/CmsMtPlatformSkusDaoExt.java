package com.voyageone.service.daoext.cms;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author desmond, 2016/12/14.
 * @version 2.10.0
 * @since 2.10.0
 */
@Repository
public interface CmsMtPlatformSkusDaoExt {

    /**
     * 取得cms_mt_platform_skus表中指定渠道id,平台id下所有叶子类目id对应的颜色和尺码的属性值件数(件数存放在idx字段返回)
     * 根据channelId,cartId,platformCategoryId,attrType分组group by,并取得属性值记录的总件数放入idx返回
     */
    public List getPlatformSkusSaleAttrCount(Map<String, Object> param);

    /**
     * 删除cms_mt_platform_skus表中指定渠道id，平台id和类目id的所有颜色和尺码记录
     */
    public void deletePlatformSkusSaleAttr(Map<String, Object> param);

}
