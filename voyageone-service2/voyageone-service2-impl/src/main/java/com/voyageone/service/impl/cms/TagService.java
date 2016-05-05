package com.voyageone.service.impl.cms;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.cms.CmsBtTagBean;
import com.voyageone.service.bean.cms.CmsTagInfoBean;
import com.voyageone.service.dao.cms.CmsBtTagDao;
import com.voyageone.service.daoext.cms.CmsBtTagDaoExt;
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
    private CmsBtTagDaoExt cmsBtTagDaoExt;
    @Autowired
    private CmsBtTagDao cmsBtTagDao;

    /**
     * Tag追加
     * @param request TagAddRequest
     * @return TagAddResponse
     */
    @VOTransactional
    public int addTag(CmsTagInfoBean request) {
        // 返回值Tag设定
//        CmsBtTagModel tag = null;
        // 执行结果
        boolean ret = true;

        // 输入参数检查
        checkAddTagParam(request);

        // tag 新追加
        // 新追加TagId
        CmsBtTagModel tag = insertTag(request);
        if (tag == null) {
            ret = false;
        }

        // tagPath 更新
        if (ret) {
            updateTagPathName(tag);
        }

        return tag.getId();
    }

    /**
     * Tag追加，输入参数检查
     * @param request 请求参数
     */
    private void checkAddTagParam(CmsTagInfoBean request) {
        // 父Tag追加的场合
        if (request.getParentTagId() == 0) {
            // TagName 对应Tag存在检查
            List<CmsBtTagModel> cmsBtTagModelList = cmsBtTagDaoExt.selectListByParentTagId(request.getChannelId(), request.getParentTagId(), request.getTagName());
            if (cmsBtTagModelList.size() > 0) {
                throw new RuntimeException("tag name is exist");
            }
            // 子Tag追加的场合
        } else {
            // 父TagId存在检查
            CmsBtTagModel cmsBtTagModel = cmsBtTagDaoExt.selectCmsBtTagByParentTagId(request.getParentTagId());
            if (cmsBtTagModel == null) {
                throw new RuntimeException("parent tag not found");
            }

            // TagName 对应Tag存在检查
            List<CmsBtTagModel> cmsBtTagModelList = cmsBtTagDaoExt.selectListByParentTagId(request.getChannelId(), request.getParentTagId(), request.getTagName());
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
    private CmsBtTagModel insertTag(CmsTagInfoBean request) {
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

        int recordCount = cmsBtTagDao.insert(cmsBtTagModel);

        if (recordCount > 0) {
            return cmsBtTagModel;
        }

        return null;
    }

    /**
     * 更新TagPath
     * @param cmsBtTagModel CmsBtTagModel
     * @return 更新结果
     */
    private boolean updateTagPathName(CmsBtTagModel cmsBtTagModel) {
        boolean ret = false;

        String tagPath = "";

        // 父Tag的场合
        String tagPathSeparator = "-";
        if (cmsBtTagModel.getParentTagId() == 0) {
            tagPath = tagPathSeparator + cmsBtTagModel.getId() + tagPathSeparator;
        } else {
            tagPath = tagPathSeparator + cmsBtTagModel.getParentTagId() + tagPathSeparator + cmsBtTagModel.getId() + tagPathSeparator;
        }

//        CmsBtTagModel cmsBtTagModel = new CmsBtTagModel();
//        cmsBtTagModel.setId(tagId);
        cmsBtTagModel.setTagPath(tagPath);

        int updateRecCount = cmsBtTagDao.update(cmsBtTagModel);

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
            CmsBtTagModel cmsBtTagModel = cmsBtTagDaoExt.selectCmsBtTagByTagId(parentTagId);
            ret = cmsBtTagModel.getTagName() + ">" + tagName;
        }

        return ret;
    }

    /**
     * ParentTagId检索Tags
     */
    public List<CmsBtTagModel> getListByParentTagId(int parentTagId) {
        return cmsBtTagDaoExt.selectListByParentTagId(parentTagId);
    }

    /**
     * 根据ChannelId检索Tags
     */
    public List<CmsBtTagModel> getListByChannelId(String channelId) {
        return cmsBtTagDaoExt.selectListByChannelId(channelId);
    }

    /**
     * 根据ChannelId 和 tagType 检索Tags
     */
    public List<CmsBtTagBean>  getListByChannelIdAndTagType(String channelId,String tagType) {
        return cmsBtTagDaoExt.selectListByChannelIdAndTagType(channelId, tagType);
    }
    public List<CmsBtTagModel>  getListByChannelIdAndparentTagIdAndTypeValue(String channelId, String parentTagId, String tagTypeValue) {
        return cmsBtTagDaoExt.selectCmsBtTagByTagInfo(channelId, parentTagId, tagTypeValue);
    }


    @VOTransactional
    public int updateTagModel(CmsBtTagModel cmsBtTagModel) {
        return cmsBtTagDaoExt.updateCmsBtTag(cmsBtTagModel);
    }

    @VOTransactional
    public void insertCmsBtTagAndUpdateTagePath(CmsBtTagModel cmsBtTagModel, boolean firstTag) {
        //将取得的数据插入到数据库
        cmsBtTagDaoExt.insertCmsBtTag(cmsBtTagModel);

        //对tagPath进行二次组装
        //一级标签
        if (firstTag) {
            cmsBtTagModel.setTagPath("-" + cmsBtTagModel.getId() + "-");
        } else {
            //对tagPath进行二次组装
            cmsBtTagModel.setTagPath(cmsBtTagModel.getTagPath() + cmsBtTagModel.getId() + "-");

        }
        //更新数据cms_bt_tag
        cmsBtTagDaoExt.updateCmsBtTag(cmsBtTagModel);
    }
}
