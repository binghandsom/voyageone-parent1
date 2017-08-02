package com.voyageone.web2.cms.views.channel;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.util.BeanUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsBtTagBean;
import com.voyageone.service.fields.cms.CmsBtTagModelTagType;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gjl
 * @version 2.0.0, 2016/4/19.
 */
@Service
public class CmsChannelTagService extends BaseViewService {

    @Autowired
    private TagService tagService;
    @Autowired
    private CmsAdvanceSearchService advanceSearchService;
    @Autowired
    private ProductService productService;

    /**
     * 取得标签管理初始化数据（注意：高级检索画面(查询条件)使用时，查询的是最近90天的所有活动，包括已结束的）
     *
     * @return result
     */
    public Map<String, Object> getInitTagInfo(Map param, String lang) {
        Map<String, Object> result = new HashMap<>();
        List<CmsBtTagBean> tagsList = getTagInfoByChannelId(param);
        List<CmsBtTagBean> categoryList = convertToTree(tagsList);

        //取得所有的标签类型
        result.put("tagTree", categoryList);

        List<TypeBean> types = Types.getTypeList(TypeConfigEnums.MastType.tagType.getId(), lang);
        if (types != null) {
            //标签类型
            result.put("tagTypeList", types.stream().filter(w -> w.getValue().equals("4")).collect(Collectors.toList()));
        }

        Integer orgFlg = (Integer) param.get("orgFlg");
        if (orgFlg != null && orgFlg == 2) {
            // 高级检索，设置自由标签的场合，需要检索一遍所选择商品的自由标签设值，返回到前端
            Integer isSelAll = (Integer) param.get("isSelAll");
            if (isSelAll == null) {
                isSelAll = 0;
            }
            List<String> prodCodes = null;
            if (isSelAll == 1) {
                // 从高级检索重新取得查询结果（根据session中保存的查询条件）
                prodCodes = advanceSearchService.getProductCodeList((String) param.get("channelId"), (Map<String, Object>) param.get("searchInfo"));
            } else {
                prodCodes = (List<String>) param.get("productIds");
            }
            if (prodCodes == null || prodCodes.isEmpty()) {
                $warn("没有code条件 params=" + param.toString());
            } else {
                // 检索商品的自由标签设值
                JongoQuery queryObj = new JongoQuery();
                queryObj.setQuery("{'common.fields.code':{$in:#}}");
                queryObj.setParameters(prodCodes);
                queryObj.setProjectionExt("prodId", "common.fields.code", "freeTags");
                List<CmsBtProductModel> prodList = productService.getList((String) param.get("channelId"), queryObj);
                if (prodList == null || prodList.isEmpty()) {
                    $warn("根据条件查不到商品 params=" + param.toString());
                } else {
                    // 用于标识是否已勾选
                    Map<String, Boolean> orgChkStsMap = new HashMap<>();
                    // 用于标识是否半选（即不是所有商品都设置了该标签）
                    Map<String, Boolean> orgDispMap = new HashMap<>();

                    // TODO 此段先注释掉，即勾选子节点的话，再显示弹出画面时父节点也显示被勾选
//                    for (CmsBtProductModel prodObj : prodList) {
//                        List<String> tags = prodObj.getFreeTags();
//                        if (tags == null || tags.isEmpty()) {
//                            continue;
//                        }
//                        // 先过滤一遍父节点
//                        for (int i = 0; i < tags.size(); i ++) {
//                            String tagPath = tags.get(i);
//                            for (String tagPath2 : tags) {
//                                if (tagPath != null && tagPath2 != null && tagPath2.length() > tagPath.length() && tagPath2.startsWith(tagPath)) {
//                                    tags.set(i, null);
//                                }
//                            }
//                        }
//                        tags = tags.stream().filter(tagPath -> tagPath != null).collect(Collectors.toList());
//                        prodObj.setFreeTags(tags);
//                    }

                    for (CmsBtTagBean tagBean : tagsList) {
                        // 遍历商品列表，查看是否勾选(这里的tagsList是列表,不是树型结构)
                        int selCnt = 0;
                        for (CmsBtProductModel prodObj : prodList) {
                            List<String> tags = prodObj.getFreeTags();
                            if (tags == null || tags.isEmpty()) {
                                continue;
                            }
                            if (tags.indexOf(tagBean.getTagPath()) >= 0) {
                                // 有勾选
                                selCnt++;
                            }
                        }
                        if (selCnt == prodList.size()) {
                            orgChkStsMap.put(tagBean.getTagPath(), true);
                        } else if (0 < selCnt && selCnt < prodList.size()) {
                            orgDispMap.put(tagBean.getTagPath(), true);
                        }
                    }
                    result.put("orgChkStsMap", orgChkStsMap);
                    result.put("orgDispMap", orgDispMap);
                }
            }
        }
        //返回数据类型
        return result;
    }

    /**
     * 根据channelId取得所有的标签并对其进行分类
     *
     * @param params Map
     * @return ret
     */
    public List<CmsBtTagBean> getTagInfoByChannelId(Map params) {
        //取得所有的标签类型
        List<CmsBtTagBean> categoryList = tagService.getListByChannelIdAndTagType(params);
        return categoryList;
    }

    /**
     * 将数据转换为树型结构
     *
     * @return List<CmsBtTagBean>
     */
    public List<CmsBtTagBean> convertToTree(List<CmsBtTagBean> valueList) {
        //循环取得标签并对其进行分类
        List<CmsBtTagBean> ret = new ArrayList<>();
        for (CmsBtTagBean each : valueList) {
            //取得一级标签
            if (each.getParentTagId() == 0) {
                ret.add(each);
            }
            for (CmsBtTagBean inner : valueList) {
                //取得子标签
                if (each.getId().intValue() == inner.getParentTagId().intValue()) {
                    if (each.getChildren() == null) {
                        //添加一个子标签
                        each.setChildren(new ArrayList<>());
                    }
                    each.getChildren().add(inner);
                    //取得子标签的名称
                    if (inner.getTagChildrenName().contains(">")) {
                        String PathName[] = inner.getTagChildrenName().split(">");
                        inner.setTagChildrenName(PathName[PathName.length - 1]);
                    }
                }
            }
            //判断是否子标签
            if (each.getChildren() == null) {
                //子标签
                each.setIsLeaf(true);
                each.setChildren(new ArrayList<>());
            } else {
                //非子标签
                each.setIsLeaf(false);
            }
        }
        //返回数据类型
        return ret;
    }

    /**
     * 将数据转换为list结构
     *
     * @return List<CmsBtTagModel>
     */
    public List<CmsBtTagModel> convertToList(List<CmsBtTagBean> tagsList) {
        //循环取得标签并对其进行分类
        List<CmsBtTagModel> ret = new ArrayList<>();
        tagsList.forEach(item -> ret.addAll(treeToList(item)));
        //返回数据类型
        return ret;
    }

    /**
     * 一棵树转成list（拍平）
     *
     * @param tagBean 一棵树
     * @return List<CmsBtTagModel>
     */
    private List<CmsBtTagModel> treeToList(CmsBtTagBean tagBean) {
        List<CmsBtTagModel> result = new ArrayList<>();
        result.add(tagBean);
        if (tagBean.getChildren() != null) {
            tagBean.getChildren().forEach(item -> result.addAll(treeToList(item)));
            tagBean.setChildren(null);
        }
        return result;
    }

    /**
     * 保存标签名称到数据库
     *
     * @param param Map
     */
    public void saveTagInfo(Map<String, Object> param) {
        //标签类型
        String tagName = (String) param.get("tagName");
        //渠道id
        String channelId = (String) param.get("channelId");
        //创建者/更新者用
        String userName = (String) param.get("userName");
        //插入数据的数值
        Map<String, Object> tagInfo = (Map<String, Object>) param.get("tagInfo");
        String tagTypeValue = String.valueOf(tagInfo.get("tagTypeSelectValue"));
        //父级标签ID
        String parentTagId;
        //取得标签名称
        String tagPathNameValue;
        //判断是否是一级标签
        boolean firstTag = (boolean) param.get("first");
        //取得所有的标签类型
        Map<String, Object> tagSelectObject = (Map<String, Object>) param.get("tagSelectObject");
        if (firstTag) {
            parentTagId = "0";
            tagPathNameValue = tagName;
        } else {
            tagPathNameValue = String.valueOf(tagSelectObject.get("tagPathName")) + " > " + tagName;
            parentTagId = String.valueOf(tagSelectObject.get("id"));
        }
        //标签名称check
        if (StringUtils.isEmpty(tagName) || tagName.getBytes().length > 50) {
            //标签名称小于50字节
            throw new BusinessException("7000074");
        }
        //tag中如果同一级中添加一个名字一样的，提示不能添加
        List<CmsBtTagModel> categoryList = tagService.getListByChannelIdAndParentTagIdAndTypeValue(channelId, parentTagId, tagTypeValue);
        //标签名称小于50字节
        for (CmsBtTagModel aCategoryList : categoryList) {
            if (aCategoryList.getTagPathName().equals(tagPathNameValue)) {
                throw new BusinessException("7000081");
            }
        }
        CmsBtTagModel cmsBtTagModel = new CmsBtTagModel();
        //一级标签
        if (firstTag) {
            cmsBtTagModel.setChannelId(channelId);
            cmsBtTagModel.setTagName(tagName);
            cmsBtTagModel.setTagType(Integer.valueOf(tagTypeValue));
            cmsBtTagModel.setTagPath("0");
            cmsBtTagModel.setTagPathName(tagName);
            cmsBtTagModel.setTagStatus(0);
            cmsBtTagModel.setSortOrder(0);
            cmsBtTagModel.setParentTagId(0);
            cmsBtTagModel.setActive(1);
            cmsBtTagModel.setCreater(userName);
        } else {
            cmsBtTagModel.setChannelId(String.valueOf(tagSelectObject.get("channelId")));
            cmsBtTagModel.setTagName(tagName);
            cmsBtTagModel.setTagPath(String.valueOf(tagSelectObject.get("tagPath")));
            cmsBtTagModel.setTagPathName(String.valueOf(tagSelectObject.get("tagPathName")) + " > " + tagName);
            cmsBtTagModel.setTagType(Integer.valueOf(tagSelectObject.get("tagType").toString()));
            cmsBtTagModel.setTagStatus(Integer.valueOf(String.valueOf(tagSelectObject.get("tagStatus"))));
            cmsBtTagModel.setSortOrder(0);
            cmsBtTagModel.setActive(1);
            cmsBtTagModel.setParentTagId(Integer.valueOf(String.valueOf(tagSelectObject.get("id"))));
            cmsBtTagModel.setCreater(userName);
            cmsBtTagModel.setModifier(userName);
        }
        // save to db
        tagService.insertCmsBtTagAndUpdateTagPath(cmsBtTagModel, firstTag);

        //记住插入的记录处理之后再返回画面
        Map<String, Object> tagTypeSelectValue = new HashMap<>();
        tagTypeSelectValue.put("id", cmsBtTagModel.getId());
        tagTypeSelectValue.put("channelId", cmsBtTagModel.getChannelId());
        tagTypeSelectValue.put("tagName", cmsBtTagModel.getTagName());
        tagTypeSelectValue.put("tagPath", cmsBtTagModel.getTagPath());
        tagTypeSelectValue.put("tagPathName", cmsBtTagModel.getTagPathName());
        tagTypeSelectValue.put("tagType", cmsBtTagModel.getTagType());
        tagTypeSelectValue.put("tagStatus", cmsBtTagModel.getTagStatus());
        tagTypeSelectValue.put("sortOrder", cmsBtTagModel.getSortOrder());
        tagTypeSelectValue.put("parentTagId", cmsBtTagModel.getParentTagId());
        tagTypeSelectValue.put("children", new ArrayList<CmsBtTagModel>());
        tagTypeSelectValue.put("isLeaf", true);
        tagTypeSelectValue.put("isActive", cmsBtTagModel.getActive());
        tagTypeSelectValue.put("tagChildrenName", param.get("tagPathName"));
        tagTypeSelectValue.put("created", cmsBtTagModel.getCreated());
        tagTypeSelectValue.put("creater", cmsBtTagModel.getCreater());
        tagTypeSelectValue.put("modified", cmsBtTagModel.getModified());
        tagTypeSelectValue.put("modifier", cmsBtTagModel.getModified());
        editTagInfo(cmsBtTagModel, param, tagTypeSelectValue);
    }

    /**
     * 记住插入的记录处理之后再返回画面
     */
    private void editTagInfo(CmsBtTagModel cmsBtTagModel, Map<String, Object> param, Map<String, Object> tagTypeSelectValue) {
        //取得插入数据库的parentTagId
        int parentTagId = cmsBtTagModel.getParentTagId();
        //取得前端的树的值
        Map tagInfo = (Map) param.get("tagInfo");
        //一级标签进行解析
        boolean isTagOne = false;
        List<Map<String, Object>> tagTree = (List<Map<String, Object>>) tagInfo.get("tagTree");
        //如果没有一级标签 添加一级标签
        if (cmsBtTagModel.getParentTagId() == 0) {
            tagTree.add(tagTypeSelectValue);
        } else {
            for (Map<String, Object> aTagTree : tagTree) {
                //如果一级标签有儿子,取得二级标签
                if (aTagTree.get("children") != null) {
                    List<Map<String, Object>> children = (List<Map<String, Object>>) aTagTree.get("children");
                    for (Map<String, Object> aChildren : children) {
                        if (aChildren.get("children") != null) {
                            List<Map<String, Object>> leaf = (List<Map<String, Object>>) aChildren.get("children");
                            //如果二级标签有儿子,添加三级标签
                            if (parentTagId == (int) aChildren.get("id") && !isTagOne) {
                                aChildren.put("isLeaf", false);
                                leaf.add(tagTypeSelectValue);
                                isTagOne = true;
                                break;
                            }
                        }
                        //如果二级标签没有儿子,添加三级标签
                        if (parentTagId == (Integer) aChildren.get("id") && aChildren.get("children") == null && !isTagOne) {
                            aChildren.put("children", tagTypeSelectValue);
                            aChildren.put("isLeaf", false);
                            isTagOne = true;
                            break;
                        }
                    }
                    //如果一级标签有儿子,添加二级标签
                    if (parentTagId == (int) aTagTree.get("id") && !isTagOne) {
                        children.add(tagTypeSelectValue);
                        aTagTree.put("isLeaf", false);
                        break;
                    }
                }
                //如果一级标签没有儿子,添加二级标签
                if (parentTagId == (Integer) aTagTree.get("id") && aTagTree.get("children") == null && !isTagOne) {
                    aTagTree.put("children", tagTypeSelectValue);
                    aTagTree.put("isLeaf", false);
                    break;
                }
            }
        }
    }

    /**
     * 更新isActive标志位
     */
    public void DelTagInfo(Map<String, Object> param) {
        //子类标签
        int id = (Integer) param.get("id");
        CmsBtTagModel tag = tagService.getTagByTagId(id);
        if (tag == null) return;
        //父类标签
        int parentTagId = (Integer) param.get("parentTagId");
        //子标签标志位
        boolean isTagOne = false;
        //取得标签树
        List<Map<String, Object>> tagTree = (List<Map<String, Object>>) param.get("tagTree");
        for (int i = 0; i < tagTree.size(); i++) {
            int tagOneTagId = (Integer) tagTree.get(i).get("id");
            if (id == tagOneTagId && !isTagOne) {
                tagTree.remove(i);
                param.put("tagTree", tagTree);
                break;
            }
            if (tagTree.get(i).get("children") != null) {
                List<Map<String, Object>> children = (List<Map<String, Object>>) tagTree.get(i).get("children");
                //如果只有一个子节点将父节点的删除按钮显示
                if (children.size() == 1 && parentTagId == (int) tagTree.get(i).get("id")) {
                    tagTree.get(i).put("isLeaf", true);
                }
                for (int j = 0; j < children.size(); j++) {
                    int childrenTagId = (Integer) children.get(j).get("id");
                    if ((id == childrenTagId) && !isTagOne) {
                        children.remove(j);
                        param.put("tagTree", tagTree);
                        isTagOne = true;
                        break;
                    }
                    if (children.get(j).get("children") != null) {
                        List<Map<String, Object>> leaf = (List<Map<String, Object>>) children.get(j).get("children");
                        //如果只有一个子节点将父节点的删除按钮显示
                        if (leaf.size() == 1 && parentTagId == (int) children.get(j).get("id")) {
                            children.get(j).put("isLeaf", true);
                        }
                        for (int k = 0; k < leaf.size(); k++) {
                            int leafTagId = (Integer) leaf.get(k).get("id");
                            if (id == leafTagId && !isTagOne) {
                                leaf.remove(k);
                                param.put("tagTree", tagTree);
                                isTagOne = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        //插入数据的数值
        CmsBtTagModel cmsBtTagModel = new CmsBtTagModel();
        cmsBtTagModel.setId(id);
        cmsBtTagModel.setActive(0);
        int affected = tagService.updateTagModel(cmsBtTagModel);
        if (affected > 0 && (tag.getTagType() == CmsBtTagModelTagType.shelves || tag.getTagType() == CmsBtTagModelTagType.usa_free_tags)) {
            JongoUpdate updObj = new JongoUpdate();
            if (tag.getTagType() == CmsBtTagModelTagType.usa_free_tags) {
                updObj.setQuery("{\"usFreeTags\":{$regex:\"-" + id + "-\"}}");
                updObj.setUpdate("{$pull:{\"usFreeTags\":{$regex:\"-" + id + "-\"}}}");
            } else {
                updObj.setQuery("{\"freeTags\":{$regex:\"-" + id + "-\"}}");
                updObj.setUpdate("{$pull:{\"freeTags\":{$regex:\"-" + id + "-\"}}}");
            }
            WriteResult writeResult = productService.updateMulti(updObj, tag.getChannelId());
            $info(String.format("删除Tag, 同步更新商品自由标签结果: %s", JacksonUtil.bean2Json(writeResult)));
        }
    }


    public CmsBtTagModel getTagByTagId(int tagId) {
        return tagService.getTagByTagId(tagId);
    }

    /**
     * 编辑Tag->tagPathName
     *
     * @param tagId    Tag -> ID
     * @param tagName  Tag -> tagName
     * @param modifier 修改人
     */
    public CmsBtTagModel editTagPathName(Integer tagId, String tagName, String modifier) {
        CmsBtTagModel tag = null;
        if (tagId != null && org.apache.commons.lang3.StringUtils.isNotBlank(tagName)) {
            tag = tagService.getTagByTagId(tagId);
            if (tag != null && !tagName.equalsIgnoreCase(tag.getTagName())) {
                Integer parentTagId = tag.getParentTagId() == null ? 0 : tag.getParentTagId();
                CmsBtTagModel sameTagPathNameOne = tagService.getTagByParentIdAndName(tag.getTagType(), parentTagId, tagName);
                if (sameTagPathNameOne != null) {
                    throw new BusinessException("A sibling tag with the same name exists.");
                }

                // 记录原来tagPathName, tagPathName%s 去replace子节点tagPathName
                String oldTagPathName = tag.getTagPathName();

                // 更新Tag name 和 pathName
                CmsBtTagModel updateModel = new CmsBtTagModel();
                updateModel.setId(tag.getId());
                updateModel.setTagName(tagName);
                if (parentTagId == 0) {
                    updateModel.setTagPathName(tagName);
                } else {
                    CmsBtTagModel parentTag = tagService.getTagByTagId(parentTagId);
                    updateModel.setTagPathName(parentTag.getTagPathName() + " > " + tagName);
                }

                updateModel.setModified(new Date());
                updateModel.setModifier(modifier);
                int affected = tagService.updateTagModel(updateModel);
                if (affected > 0) {
                    // replace 子节点tagPathName
                    tagService.replaceChildrenPathName(tag.getTagType(), oldTagPathName, updateModel.getTagPathName());
                    BeanUtils.copy(updateModel, tag);
                    return tag;
                }
            }
        }
        return null;
    }

}
