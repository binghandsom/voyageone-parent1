package com.voyageone.service.impl.cms.usa;

import com.voyageone.common.util.BeanUtils;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.CommonPropService;
import com.voyageone.service.impl.cms.feed.FeedCustomPropService;
import com.voyageone.service.impl.cms.product.ProductTagService;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.impl.cms.vomqjobservice.CmsProductFreeTagsUpdateService;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * USA CMS2 高级检索
 *
 * @Author rex.wu
 * @Create 2017-07-17 13:20
 */
@Service
public class UsaAdvanceSearchService extends BaseService {

    @Autowired
    private ProductTagService productTagService;
    @Autowired
    CmsProductFreeTagsUpdateService cmsProductFreeTagsUpdateService;

    /**
     * 设置产品自由标签，同时添加该tag的所有上级tag，当在高级检索列表也单商品修改free tag时不走MQ方式了
     */
    public void setProdFreeTag(String channelId, Map<String, Object> params, String modifier) {
        List<String> tagPathList = (List<String>) params.get("tagPathList");
        if (tagPathList == null || tagPathList.isEmpty()) {
            $info("CmsAdvanceSearchService：setProdFreeTag 未选择标签,将清空所有自由标签");
        }

        List<String> orgDispTagList = null;
        if (params.get("orgDispTagList") != null) {
            orgDispTagList = (List<String>) params.get("orgDispTagList");
        }

        // 先获取singleProd参数，如果结果为1，则针对单商品进行自由标签编辑，那么直接操作，否则就是批量走MQ
        Integer singleProd = (Integer) params.get("singleProd");
        if (Objects.equals(singleProd, Integer.valueOf(1))) {
            List<String> prodCodeList = (List<String>) params.get("prodIdList");
            productTagService.setProdFreeTag(channelId, tagPathList, prodCodeList, orgDispTagList, EnumProductOperationType.SingleProdSetFreeTag, modifier);
        } else {
            Integer isSelAll = (Integer) params.get("isSelAll");
            if (isSelAll == null) {
                isSelAll = 0;
            }
            List<String> prodCodeList;
            if (isSelAll == 1) {
                CmsSearchInfoBean2 searchValue = new CmsSearchInfoBean2();
                BeanUtils.copyProperties((Map<String, Object>) params.get("searchInfo"), searchValue);
                cmsProductFreeTagsUpdateService.sendMessage(channelId, searchValue, tagPathList, orgDispTagList, modifier);
            } else {
                prodCodeList = (List<String>) params.get("prodIdList");
                cmsProductFreeTagsUpdateService.sendMessage(channelId, prodCodeList, tagPathList, orgDispTagList, modifier);
            }
        }

    }

}
