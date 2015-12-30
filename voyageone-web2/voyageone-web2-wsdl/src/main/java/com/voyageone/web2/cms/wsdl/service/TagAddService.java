package com.voyageone.web2.cms.wsdl.service;

import com.google.common.base.Joiner;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.cms.wsdl.dao.CmsBtTagDao;
import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.domain.CmsBtTagModel;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.request.ProductGetRequest;
import com.voyageone.web2.sdk.api.request.ProductsGetRequest;
import com.voyageone.web2.sdk.api.request.TagAddRequest;
import com.voyageone.web2.sdk.api.response.ProductGetResponse;
import com.voyageone.web2.sdk.api.response.ProductsGetResponse;
import com.voyageone.web2.sdk.api.response.TagAddResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.*;

/**
 * product Service
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 * @since. 2.0.0
 */
@Service
public class TagAddService extends BaseService {

    @Autowired
    private CmsBtTagDao cmsBtTagDao;

    public TagAddResponse addTag(TagAddRequest request) {
        TagAddResponse result = new TagAddResponse();
        CmsBtTagModel tag = null;

        // tag 新追加
        int tagId = insertTag(request);

        // tagPath 更新

        tag.setTagId(tagId);

        result.setTag(tag);
        return result;
    }

    private int insertTag(TagAddRequest request) {
        int tagId = 0;

        CmsBtTagModel cmsBtTagModel = new CmsBtTagModel();
        cmsBtTagModel.setChannelId(request.getChannelId());
        cmsBtTagModel.setTagName(request.getTagName());
        cmsBtTagModel.setTagPathName(getTagPathName(request.getParentTagId(), request.getTagName()));
        cmsBtTagModel.setTagType(request.getTagType());
        cmsBtTagModel.setTagStatus(request.getTagStatus());
        cmsBtTagModel.setSortOrder(request.getSortOrder());
        cmsBtTagModel.setParentTagId(request.getParentTagId());
        cmsBtTagModel.setCreater(request.getCreater());
        cmsBtTagModel.setModifier(request.getCreater());

        int recordCount = cmsBtTagDao.insertCmsBtTag(cmsBtTagModel);

        if (recordCount > 0) {
            tagId = cmsBtTagModel.getTagId();
        }

        return tagId;
    }

    private String getTagPathName(Integer parentTagId, String tagName) {
        String ret = "";

        CmsBtTagModel cmsBtTagModel = cmsBtTagDao.getCmsBtTagByTagId(parentTagId);
        ret = cmsBtTagModel.getTagName() + ">" + tagName;

        return ret;
    }
}
