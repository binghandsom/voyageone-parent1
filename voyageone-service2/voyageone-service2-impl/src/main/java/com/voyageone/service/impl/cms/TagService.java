package com.voyageone.service.impl.cms;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.cms.CmsBtTagBean;
import com.voyageone.service.bean.cms.CmsTagInfoBean;
import com.voyageone.service.bean.cms.businessmodel.CmsBtTag.TagCodeCountInfo;
import com.voyageone.service.dao.cms.CmsBtTagDao;
import com.voyageone.service.dao.cms.CmsBtTagJmModuleExtensionDao;
import com.voyageone.service.daoext.cms.CmsBtTagDaoExt;
import com.voyageone.service.daoext.cms.CmsBtTagDaoExtCamel;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtTagJmModuleExtensionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.util.MapModel;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Tag Service
 *
 * @author jerry 15/12/30
 * @version 2.0.1
 */
@Service
public class TagService extends BaseService {
    private final CmsBtTagDaoExt cmsBtTagDaoExt;
    private final CmsBtTagDao cmsBtTagDao;
    private final CmsBtTagJmModuleExtensionDao tagJmModuleExtensionDao;

    @Autowired
    CmsBtTagDaoExtCamel cmsBtTagDaoExtCamel;

    @Autowired
    public TagService(CmsBtTagDaoExt cmsBtTagDaoExt, CmsBtTagDao cmsBtTagDao, CmsBtTagJmModuleExtensionDao tagJmModuleExtensionDao) {
        this.cmsBtTagDaoExt = cmsBtTagDaoExt;
        this.cmsBtTagDao = cmsBtTagDao;
        this.tagJmModuleExtensionDao = tagJmModuleExtensionDao;
    }

    /**
     * Tag追加
     *
     * @param request TagAddRequest
     * @return TagAddResponse
     */
    @VOTransactional
    public int addTag(CmsTagInfoBean request) {
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

    public void addJmModule(CmsBtTagJmModuleExtensionModel tagJmModuleExtensionModel) {
        tagJmModuleExtensionDao.insert(tagJmModuleExtensionModel);
    }

    /**
     * 通过原始（核心）tag 获取聚美模块的扩展配置数据模型
     *
     * @param tagModel 原始 tag
     * @return CmsBtTagJmModuleExtensionModel
     */
    public CmsBtTagJmModuleExtensionModel getJmModule(CmsBtTagModel tagModel) {
        return getJmModule(tagModel.getId());
    }

    public CmsBtTagJmModuleExtensionModel getJmModule(Integer tagId) {
        return tagJmModuleExtensionDao.select(tagId);
    }

    /**
     * 是否有主推模块
     */
    public boolean hasFeaturedJmModuleByTopTagId(int promotionTopTagId) {
        return getListByParentTagId(promotionTopTagId)
                .stream()
                .anyMatch(tagModel -> {
                    CmsBtTagJmModuleExtensionModel tagJmModuleExtensionModel = getJmModule(tagModel);
                    return tagJmModuleExtensionModel != null && tagJmModuleExtensionModel.getFeatured();
                });
    }

    /**
     * ParentTagId检索Tags
     */
    public List<CmsBtTagModel> getListByParentTagId(int parentTagId) {
        return cmsBtTagDaoExt.selectListByParentTagId(parentTagId);
    }

    /**
     * 根据ChannelId 和 tagType 检索Tags
     */
    public List<CmsBtTagBean> getListByChannelIdAndTagType(Map params) {
        //标签类型
        String tagType = (String) params.get("tagTypeSelectValue");
        if ("4".equals(tagType)) {
            // 查询自由标签
            return cmsBtTagDaoExt.selectListByChannelIdAndTagType(params);
        } else if ("2".equals(tagType)) {
            // 查询Promotion标签
            List<CmsBtTagBean> categoryList = null;
            Integer orgFlg = (Integer) params.get("orgFlg");
            if (orgFlg != null && orgFlg == 1) {
                // 从高级检索画面(查询条件)而来
                categoryList = cmsBtTagDaoExt.selectListByChannelId4AdvSearch(params);
            } else {
                categoryList = cmsBtTagDaoExt.selectListByChannelIdAndTagType2(params);
            }
            if (categoryList == null || categoryList.isEmpty()) {
                return categoryList;
            }
            // 再查询一遍，检查是否有子节点
            params.put("tagList", categoryList.stream().map(tagBean -> tagBean.getId()).collect(Collectors.toList()));
            List<CmsBtTagBean> categoryList2 = cmsBtTagDaoExt.selectListByChannelIdAndParentTag(params);
            if (categoryList2 != null && !categoryList2.isEmpty()) {
                for (CmsBtTagBean tagBean : categoryList2) {
                    if (categoryList.indexOf(tagBean) >= 0) {
                        continue;
                    }
                    categoryList.add(tagBean);
                }
            }
            // 按时间倒序排序
            Comparator<CmsBtTagBean> comparator = (cl1, cl2) -> cl1.getModified().compareTo(cl2.getModified());
            categoryList.sort(comparator.reversed());
            return categoryList;
        }
        return new ArrayList<>(0);
    }

    public List<CmsBtTagModel> getListByChannelIdAndParentTagIdAndTypeValue(String channelId, String parentTagId, String tagTypeValue) {
        return cmsBtTagDaoExt.selectCmsBtTagByTagInfo(channelId, parentTagId, tagTypeValue);
    }

    @VOTransactional
    public int updateTagModel(CmsBtTagModel cmsBtTagModel) {
        return cmsBtTagDao.update(cmsBtTagModel);
    }

    public void updateTagModel(CmsBtTagJmModuleExtensionModel tagJmModuleExtensionModel) {
        tagJmModuleExtensionDao.update(tagJmModuleExtensionModel);
    }

    @VOTransactional
    public void insertCmsBtTagAndUpdateTagPath(CmsBtTagModel cmsBtTagModel, final boolean firstTag) {
        insertCmsBtTagAndUpdateTagPath(cmsBtTagModel, modelConsumer -> {
            if (firstTag) {
                modelConsumer.setTagPath("-" + modelConsumer.getId() + "-");
            } else {
                //对tagPath进行二次组装
                modelConsumer.setTagPath(modelConsumer.getTagPath() + modelConsumer.getId() + "-");
            }
        });
    }

    public void insertCmsBtTagAndUpdateTagPath(CmsBtTagModel cmsBtTagModel, Consumer<CmsBtTagModel> tagPathSetter) {
        //将取得的数据插入到数据库
        cmsBtTagDao.insert(cmsBtTagModel);

        tagPathSetter.accept(cmsBtTagModel);

        updateTagModel(cmsBtTagModel);
    }

    public List<CmsBtTagBean> getTagPathNameByTagPath(String channelId, List<String> tagPathList) {
        return cmsBtTagDaoExt.selectTagPathNameByTagPath(channelId, tagPathList);
    }

    public CmsBtTagModel getTagByTagId(int tagId) {
        return cmsBtTagDaoExt.selectCmsBtTagByTagId(tagId);
    }

    /**
     * 查询同级别的tag信息
     */
    public List<CmsBtTagModel> getListBySameLevel(String channelId, int parentTagId, int tagId) {
        return cmsBtTagDaoExt.selectListBySameLevel(channelId, parentTagId, tagId);
    }

    public CmsBtTagJmModuleExtensionModel createJmModuleExtension(CmsBtTagModel tagModel) {
        CmsBtTagJmModuleExtensionModel tagJmModuleExtensionModel = new CmsBtTagJmModuleExtensionModel();

        tagJmModuleExtensionModel.setTagId(tagModel.getId());
        tagJmModuleExtensionModel.setModuleTitle(tagModel.getTagName()); // 创建时，默认使用标签名称
        tagJmModuleExtensionModel.setFeatured(false);
        tagJmModuleExtensionModel.setHideFlag(1);
        tagJmModuleExtensionModel.setDisplayStartTime(null);
        tagJmModuleExtensionModel.setDisplayEndTime(null);
        tagJmModuleExtensionModel.setShelfType(1);
        tagJmModuleExtensionModel.setImageType(1);
        tagJmModuleExtensionModel.setProductsSortBy(2);
        tagJmModuleExtensionModel.setNoStockToLast(true);

        return tagJmModuleExtensionModel;
    }

    /**
     * Tag追加，输入参数检查
     *
     * @param request 请求参数
     */
    private void checkAddTagParam(CmsTagInfoBean request) {
        // 父Tag追加的场合
        if (request.getParentTagId() == 0) {
            // TagName 对应Tag存在检查
            List<CmsBtTagModel> cmsBtTagModelList = cmsBtTagDaoExt.selectListByParentTagId(request.getChannelId(), request.getParentTagId(), request.getTagName());
            if (!cmsBtTagModelList.isEmpty()) {
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
            if (!cmsBtTagModelList.isEmpty()) {
                throw new RuntimeException("tag name is exist");
            }
        }
    }

    /**
     * Tag追加
     *
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
     *
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
     *
     * @param parentTagId 父TagId
     * @param tagName     新追加Tag名
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

    public List<TagCodeCountInfo> getListTagCodeCount(int promotionId, int parentTagId, List<String> codeList) {
        return cmsBtTagDaoExtCamel.selectListTagCodeCount(promotionId, parentTagId, codeList);
    }

    /**
     * 根据Tag-parentTagId 和 tagPathName 查询Tag
     *
     * @param tagType     Tag类型
     * @param parentTagId Tag父节点ID
     * @param tagName     Tag->tagName
     * @return Tag
     */
    public CmsBtTagModel getTagByParentIdAndName(Integer tagType, Integer parentTagId, String tagName) {
        CmsBtTagModel queryModel = new CmsBtTagModel();
        queryModel.setTagType(tagType);
        queryModel.setParentTagId(parentTagId);
        queryModel.setTagName(tagName);
        queryModel.setActive(1);
        return cmsBtTagDao.selectOne(queryModel);
    }
}
