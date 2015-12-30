package com.voyageone.web2.sdk.api.service;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.CmsBtTagModel;
import com.voyageone.web2.sdk.api.request.ProductGetRequest;
import com.voyageone.web2.sdk.api.request.TagAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagAddClient {
    @Autowired
    protected VoApiDefaultClient voApiClient;

    /**
     * 获取商品 根据ID获
     */
    public CmsBtTagModel addTag(String channelId, String tagName, Integer tagType, Integer tagStatus, Integer sortOrder, Integer parentTagId, String Creater) {
        //设置参数
        TagAddRequest requestModel = new TagAddRequest(channelId);
        requestModel.setTagName(tagName);
        requestModel.setTagType(tagType);
        requestModel.setTagStatus(tagStatus);
        requestModel.setSortOrder(sortOrder);
        requestModel.setParentTagId(parentTagId);
        requestModel.setCreater(Creater);

        //SDK取得Product 数据
        return voApiClient.execute(requestModel).getTag();
    }

}
