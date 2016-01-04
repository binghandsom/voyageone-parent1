package com.voyageone.web2.sdk.api.service;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.CmsBtTagModel;
import com.voyageone.web2.sdk.api.request.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagAddClient {
    @Autowired
    protected VoApiDefaultClient voApiClient;

    /**
     * 追加Tag
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

    /**
     * 移除Tag
     */
    public boolean removeTag(String channelId, Integer tagId) {
        //设置参数
        TagRemoveRequest requestModel = new TagRemoveRequest(channelId);
        requestModel.setChannelId(channelId);
        requestModel.setTagId(tagId);

        return voApiClient.execute(requestModel).isRemoveResult();
    }

    /**
     * 根据ParentTagId检索Tags
     */
    public List<CmsBtTagModel> getTagsByParentTagId(String channelId, Integer parentTagId) {
        //设置参数
        TagsGetByParentTagIdRequest requestModel = new TagsGetByParentTagIdRequest(channelId);
        requestModel.setChannelId(channelId);
        requestModel.setParentTagId(parentTagId);

        return voApiClient.execute(requestModel).getTags();
    }

    /**
     * 根据ChanelId检索Tags
     */
    public List<CmsBtTagModel> getParentTagsByChannelId(String channelId) {
        //设置参数
        TagsGetByChannelIdRequest requestModel = new TagsGetByChannelIdRequest(channelId);
        requestModel.setChannelId(channelId);

        return voApiClient.execute(requestModel).getTags();
    }
}
