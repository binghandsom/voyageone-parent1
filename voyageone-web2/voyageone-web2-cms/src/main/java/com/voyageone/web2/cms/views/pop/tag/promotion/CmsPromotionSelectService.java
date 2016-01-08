package com.voyageone.web2.cms.views.pop.tag.promotion;

import com.voyageone.common.util.CommonUtil;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.CmsBtTagModel;
import com.voyageone.web2.sdk.api.request.TagsGetRequest;
import com.voyageone.web2.sdk.api.service.ProductTagClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/14
 * @version 2.0.0
 */
@Service
public class CmsPromotionSelectService extends BaseAppService {

    @Autowired
    private VoApiDefaultClient voApiClient;

    @Autowired
    private ProductTagClient productTagClient;

    public List<CmsBtTagModel> getPromotionTags(Map<String, Object> params) {
        int tag_id = (int) params.get("refTagId");
        return this.selectListByParentTagId(tag_id);
    }

    /**
     * 获取二级Tag
     */
    public List<CmsBtTagModel> selectListByParentTagId(int parentTagId) {
        //设置参数
        TagsGetRequest requestModel = new TagsGetRequest();
        requestModel.setParentTagId(parentTagId);
        return voApiClient.execute(requestModel).getTags();
    }

    /**
     * addToPromotion
     */
    public Map<String, Object> addToPromotion(Map<String, Object> params, String channelId, String modifier) {
        String tag_path = params.get("tagPath").toString();
        List<Long> productIds = CommonUtil.changeListType((ArrayList<Integer>) params.get("productIds"));
        // TODO 2016-01-08 目前处理逻辑只往product表中添加tag,不会将对应的产品添加到promotion中
        return productTagClient.addTagProducts(channelId, tag_path, productIds, modifier);
    }
}
