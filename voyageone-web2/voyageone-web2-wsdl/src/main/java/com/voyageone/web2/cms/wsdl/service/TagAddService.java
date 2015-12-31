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
import com.voyageone.web2.sdk.api.request.*;
import com.voyageone.web2.sdk.api.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.text.Collator;
import java.util.*;

/**
 * Tag Service
 *
 * @author jerry 15/12/30
 * @version 2.0.1
 * @since. 2.0.0
 */
@Service
public class TagAddService extends BaseService {

    @Autowired
    // mysql
    private CmsBtTagDao cmsBtTagDao;

    @Autowired
    // mongodb
    private CmsBtProductDao cmsBtProductDao;

    // TagPath 分割符
    private String tagPathSeparator = "-";

    @Autowired
    private DataSourceTransactionManager transactionManager;
    DefaultTransactionDefinition def =new DefaultTransactionDefinition();

    /**
     * Tag追加
     * @param request TagAddRequest
     * @return TagAddResponse
     */
    public TagAddResponse addTag(TagAddRequest request) {
        // 返回结果
        TagAddResponse result = new TagAddResponse();
        // 返回值Tag设定
        CmsBtTagModel tag = null;
        // 执行结果
        boolean ret = true;
        // 新追加TagId
        int tagId = 0;

        TransactionStatus status=transactionManager.getTransaction(def);

        // 输入参数检查
        ret = checkAddTagParam(request, result);

        if (ret) {
            // tag 新追加
            tagId = insertTag(request);
            if (tagId == 0) {
                ret = false;
            }
        }

        // tagPath 更新
        if (ret) {
            ret = updateTagPathName(request.getParentTagId(), tagId);
        }

        if (ret) {
            transactionManager.commit(status);
        } else {
            transactionManager.rollback(status);
        }

        // 返回值设定
        tag = new CmsBtTagModel();
        tag.setTagId(tagId);
        result.setTag(tag);

        return result;
    }

    /**
     * Tag追加
     * @param request TagAddRequest
     * @return 新追加TagID
     */
    private int insertTag(TagAddRequest request) {
        int tagId = 0;

        CmsBtTagModel cmsBtTagModel = new CmsBtTagModel();
        cmsBtTagModel.setChannelId(request.getChannelId());
        cmsBtTagModel.setTagName(request.getTagName());
        // 初期值插入
        cmsBtTagModel.setTagPath("");
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

    /**
     * 追加TagPathName取得
     * @param parentTagId 父TagId
     * @param tagName 新追加Tag名
     * @return 追加TagPathName
     */
    private String getTagPathName(Integer parentTagId, String tagName) {
        String ret = "";

        CmsBtTagModel cmsBtTagModel = cmsBtTagDao.getCmsBtTagByTagId(parentTagId);
        ret = cmsBtTagModel.getTagName() + ">" + tagName;

        return ret;
    }

    /**
     * 更新TagPath
     * @param parentTagId 父TagId
     * @param tagId 子TagId
     * @return 更新结果
     */
    private boolean updateTagPathName(Integer parentTagId, Integer tagId) {
        boolean ret = false;

        String tagPath = tagPathSeparator + parentTagId + tagPathSeparator + tagId + tagPathSeparator;

        CmsBtTagModel cmsBtTagModel = new CmsBtTagModel();
        cmsBtTagModel.setTagId(tagId);
        cmsBtTagModel.setTagPath(tagPath);

        int updateRecCount = cmsBtTagDao.updateCmsBtTag(cmsBtTagModel);

        if (updateRecCount > 0) {
            ret = true;
        }

        return ret;
    }

    /**
     * Tag追加，输入参数检查
     * @param request 请求参数
     * @param result 返回结果
     * @return 检查结果
     */
    private boolean checkAddTagParam(TagAddRequest request, TagAddResponse result) {
        boolean ret = true;

        // 父TagId存在检查
        CmsBtTagModel cmsBtTagModel = cmsBtTagDao.getCmsBtTagByTagId(request.getParentTagId());
        if (cmsBtTagModel == null) {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70008;
            result.setCode(codeEnum.getErrorCode());
            result.setMessage(codeEnum.getErrorMsg());

            ret = false;
        }

        if (ret) {
            List<CmsBtTagModel> cmsBtTagModelList = cmsBtTagDao.selectListByParentTagId(request.getChannelId(), request.getParentTagId(), request.getTagName());
            if (cmsBtTagModelList.size() > 0) {
                VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70009;
                result.setCode(codeEnum.getErrorCode());
                result.setMessage(codeEnum.getErrorMsg());

                ret = false;
            }
        }

        return ret;
    }

    /**
     * Tag移除
     * @param request TagRemoveRequest
     * @return TagRemoveResponse
     */
    public TagRemoveResponse removeTag(TagRemoveRequest request) {
        boolean ret = true;
        // 返回结果
        TagRemoveResponse result = new TagRemoveResponse();

        // 输入参数检查
        ret = checkRemoveTagParam(request, result);

        if (ret) {
            // tag 删除
            ret = deleteTag(request);
        }

        // 返回值设定
        result.setRemoveResult(ret);
        return result;
    }

    /**
     * Tag删除
     * @param request TagAddRequest
     * @return 新追加TagID
     */
    private boolean deleteTag(TagRemoveRequest request) {
        boolean ret = false;

        CmsBtTagModel cmsBtTagModel = new CmsBtTagModel();
        cmsBtTagModel.setChannelId(request.getChannelId());
        cmsBtTagModel.setTagId(request.getTagId());

        int recordCount = cmsBtTagDao.deleteCmsBtTag(cmsBtTagModel);

        if (recordCount > 0) {
            ret = true;
        }

        return ret;
    }

    /**
     * Tag删除，输入参数检查
     * @param request 请求参数
     * @param result 返回结果
     * @return 检查结果
     */
    private boolean checkRemoveTagParam(TagRemoveRequest request, TagRemoveResponse result) {
        boolean ret = true;

        // Tag 使用检查
        List<CmsBtProductModel> productModelList = cmsBtProductDao.selectProductByTagId(request.getChannelId(), request.getTagId());
        if (productModelList.size() > 0) {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70010;
            result.setCode(codeEnum.getErrorCode());
            result.setMessage(codeEnum.getErrorMsg());

            ret = false;
        }

        return ret;
    }

    /**
     * 根据ParentTagId检索Tags
     * @param request TagsGetByParentTagIdRequest
     * @return TagsGetByParentTagIdResponse
     */
    public TagsGetByParentTagIdResponse selectListByParentTagId(TagsGetByParentTagIdRequest request) {
        // 返回结果
        TagsGetByParentTagIdResponse result = new TagsGetByParentTagIdResponse();

        List<CmsBtTagModel> tagModelList = cmsBtTagDao.selectListByParentTagId(request.getParentTagId());

        // 返回值设定
        result.setTags(tagModelList);
        return result;
    }

    /**
     * 根据ChannelId检索Tags
     * @param request TagsGetByChannelIdRequest
     * @return TagsGetByChannelIdResponse
     */
    public TagsGetByChannelIdResponse selectListByChannelId(TagsGetByChannelIdRequest request) {
        // 返回结果
        TagsGetByChannelIdResponse result = new TagsGetByChannelIdResponse();

        List<CmsBtTagModel> tagModelList = cmsBtTagDao.selectListByChannelId(request.getChannelId());

        // 返回值设定
        result.setTags(tagModelList);
        return result;
    }
}
