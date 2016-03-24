package com.voyageone.service.impl.cms;

import com.voyageone.service.bean.cms.CmsTagInfoBean;
import com.voyageone.service.dao.cms.CmsBtTagDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtTagModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Tag Service
 *
 * @author jerry 15/12/30
 * @version 2.0.1
 */
@Service
public class TagService extends BaseService {

    @Autowired
    // mysql
    private CmsBtTagDao cmsBtTagDao;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

//    @Autowired
//    private DataSourceTransactionManager transactionManager;
//    DefaultTransactionDefinition def =new DefaultTransactionDefinition();

    /**
     * Tag追加
     * @param request TagAddRequest
     * @return TagAddResponse
     */
    public int addTag(CmsTagInfoBean request) {
        // 返回值Tag设定
        CmsBtTagModel tag = null;
        // 执行结果
        boolean ret = true;

        // 输入参数检查
        checkAddTagParam(request);

        // tag 新追加
        // 新追加TagId
        int tagId = insertTag(request);
        if (tagId == 0) {
            ret = false;
        }

        // tagPath 更新
        if (ret) {
            updateTagPathName(request.getParentTagId(), tagId);
        }

        return tagId;
    }

    /**
     * Tag追加，输入参数检查
     * @param request 请求参数
     */
    private void checkAddTagParam(CmsTagInfoBean request) {
        // 父Tag追加的场合
        if (request.getParentTagId() == 0) {
            // TagName 对应Tag存在检查
            List<CmsBtTagModel> cmsBtTagModelList = cmsBtTagDao.selectListByParentTagId(request.getChannelId(), request.getParentTagId(), request.getTagName());
            if (cmsBtTagModelList.size() > 0) {
                throw new RuntimeException("tag name is exist");
            }
            // 子Tag追加的场合
        } else {
            // 父TagId存在检查
            CmsBtTagModel cmsBtTagModel = cmsBtTagDao.selectCmsBtTagByParentTagId(request.getParentTagId());
            if (cmsBtTagModel == null) {
                throw new RuntimeException("parent tag not found");
            }

            // TagName 对应Tag存在检查
            List<CmsBtTagModel> cmsBtTagModelList = cmsBtTagDao.selectListByParentTagId(request.getChannelId(), request.getParentTagId(), request.getTagName());
            if (cmsBtTagModelList.size() > 0) {
                throw new RuntimeException("tag name is exist");
            }
        }
    }

    /**
     * Tag追加
     * @param request TagAddRequest
     * @return 新追加TagID
     */
    public int insertTag(CmsTagInfoBean request) {
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
        cmsBtTagModel.setCreater(request.getModifier());
        cmsBtTagModel.setModifier(request.getModifier());

        int recordCount = cmsBtTagDao.insertCmsBtTag(cmsBtTagModel);

        if (recordCount > 0) {
            tagId = cmsBtTagModel.getTagId();
        }

        return tagId;
    }

    /**
     * 更新TagPath
     * @param parentTagId 父TagId
     * @param tagId 子TagId
     * @return 更新结果
     */
    private boolean updateTagPathName(Integer parentTagId, Integer tagId) {
        boolean ret = false;

        String tagPath = "";

        // 父Tag的场合
        String tagPathSeparator = "-";
        if (parentTagId == 0) {
            tagPath = tagPathSeparator + tagId + tagPathSeparator;
        } else {
            tagPath = tagPathSeparator + parentTagId + tagPathSeparator + tagId + tagPathSeparator;
        }

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
     * 追加TagPathName取得
     * @param parentTagId 父TagId
     * @param tagName 新追加Tag名
     * @return 追加TagPathName
     */
    private String getTagPathName(Integer parentTagId, String tagName) {
        String ret = "";

        // 父Tag的场合
        if (parentTagId == 0) {
            ret = tagName;
        } else {
            CmsBtTagModel cmsBtTagModel = cmsBtTagDao.selectCmsBtTagByTagId(parentTagId);
            ret = cmsBtTagModel.getTagName() + ">" + tagName;
        }

        return ret;
    }

//    /**
//     * Tag移除
//     * @param request TagRemoveRequest
//     * @return TagRemoveResponse
//     */
//    public TagRemoveResponse removeTag(TagRemoveRequest request) {
//        boolean ret = true;
//        // 返回结果
//        TagRemoveResponse result = new TagRemoveResponse();
//
//        // 输入参数检查
//        ret = checkRemoveTagParam(request, result);
//
//        if (ret) {
//            // tag 删除
//            ret = deleteTag(request);
//        }
//
//        // 返回值设定
//        result.setRemoveResult(ret);
//        return result;
//    }
//
//    /**
//     * Tag删除
//     * @param request TagAddRequest
//     * @return 新追加TagID
//     */
//    private boolean deleteTag(TagRemoveRequest request) {
//        boolean ret = false;
//
//        // 当前Tag取得
//        CmsBtTagModel cmsBtTagModel = cmsBtTagDao.selectCmsBtTagByTagId(request.getTagId());
//
//        TransactionStatus status=transactionManager.getTransaction(def);
//
//        // 当前Tag删除
//        CmsBtTagModel cmsBtTagModelPara = new CmsBtTagModel();
//        cmsBtTagModelPara.setTagId(request.getTagId());
//        int recordCount = cmsBtTagDao.deleteCmsBtTagByTagId(cmsBtTagModelPara);
//        if (recordCount > 0) {
//            ret = true;
//        }
//
//        // 父Tag删除的场合
//        if (cmsBtTagModel.getParentTagId() == 0) {
//            cmsBtTagModelPara = new CmsBtTagModel();
//            cmsBtTagModelPara.setParentTagId(request.getTagId());
//            cmsBtTagDao.deleteCmsBtTagByParentTagId(cmsBtTagModelPara);
//        }
//
//        if (ret) {
//            transactionManager.commit(status);
//        } else {
//            transactionManager.rollback(status);
//        }
//
//        return ret;
//    }
//
//    /**
//     * Tag删除，输入参数检查
//     * @param request 请求参数
//     * @param result 返回结果
//     * @return 检查结果
//     */
//    private boolean checkRemoveTagParam(TagRemoveRequest request, TagRemoveResponse result) {
//        boolean ret = true;
//
//        // Tag 使用检查
//        long productCountByTagId = cmsBtProductDao.selectProductCountByTagId(request.getChannelId(), request.getTagId());
//        if (productCountByTagId > 0) {
//            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70010;
//            result.setCode(codeEnum.getErrorCode());
//            result.setMessage(codeEnum.getErrorMsg());
//
//            ret = false;
//        }
//
//        return ret;
//    }

    /**
     * ParentTagId检索Tags
     */
    public List<CmsBtTagModel> getListByParentTagId(int parentTagId) {
        return cmsBtTagDao.selectListByParentTagId(parentTagId);
    }

    /**
     * 根据ChannelId检索Tags
     */
    public List<CmsBtTagModel> getListByChannelId(String channelId) {
        return cmsBtTagDao.selectListByChannelId(channelId);
    }

//    /**
//     * 根据ChannelId检索Tags
//     * @param request TagsGetByChannelIdRequest
//     * @return TagsGetByChannelIdResponse
//     */
//    public TagsGetByChannelIdResponse selectListByChannelId(TagsGetByChannelIdRequest request) {
//        // 返回结果
//        TagsGetByChannelIdResponse result = new TagsGetByChannelIdResponse();
//
//        List<CmsBtTagModel> tagModelList = cmsBtTagDao.selectListByChannelId(request.getChannelId());
//
//        // 返回值设定
//        result.setTags(tagModelList);
//        return result;
//    }
}
