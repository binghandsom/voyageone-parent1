package com.voyageone.web2.cms.views.channel;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsBtTagDao;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.web2.base.BaseAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/4/19.
 */
@Service
public class CmsChannelTagService extends BaseAppService {
    /**
     * 店铺分类标签
     */
    private static final String TAG_TYPE_SHOP_CLASSIFY = "1";
    private static final String TAG_TYPE_SHOP_CLASSIFY_NAME = "店铺内分类";
    /**
     * 活动标签
     */
    private static final String TAG_TYPE_PROMOTION = "2";
    private static final String TAG_TYPE_PROMOTION_NAME = "活动标签";
    /**
     * 货位标签
     */
    private static final String TAG_TYPE_GOODS = "3";
    private static final String TAG_TYPE_GOODS_NAME = "货位标签";
    /**
     * 自由标签
     */
    private static final String TAG_TYPE_FREE = "4";
    private static final String TAG_TYPE_FREE_NAME = "自由标签";
    @Autowired
    private CmsBtTagDao cmsBtTagDao;
    @Autowired
    private SimpleTransaction simpleTransaction;

    /**
     * 根据channelId取得素有的标签并对其进行分类
     *
     * @param param
     * @return ret
     */
    public List<CmsBtTagModel> getTagInfoByChannelId(Map param) {
        //公司平台销售渠道
        String channelId = (String) param.get("channel_id");
        //标签类型
        String tagTypeSelectValue = (String) param.get("tagTypeSelectValue");
        //取得所有的标签类型
        List<CmsBtTagModel> categoryList = cmsBtTagDao.selectListByChannelIdAndTagType(channelId, tagTypeSelectValue);
        //循环取得标签并对其进行分类
        List<CmsBtTagModel> ret = new ArrayList<>();
        for (CmsBtTagModel each : categoryList) {
            //取得一级标签
            if (each.getParentTagId() == 0) {
                ret.add(each);
            }
            for (CmsBtTagModel inner : categoryList) {
                //取得子标签
                if (each.getTagId().intValue() == inner.getParentTagId().intValue()) {
                    if (each.getChildren() == null) {
                        //添加一个子标签
                        each.setChildren(new ArrayList<CmsBtTagModel>());
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
                each.setChildren(new ArrayList<CmsBtTagModel>());
            } else {
                //非子标签
                each.setIsLeaf(false);
            }
        }
        //返回数据类型
        return ret;
    }

    /**
     * 保存标签名称到数据库
     *
     * @param param
     */
    public void saveTagInfo(Map param) {
        //标签类型
        String tagPathName = (String) param.get("tagPathName");
        //渠道id
        String channelId = (String) param.get("channelId");
        ////创建者/更新者用
        String userName = (String) param.get("userName");
        //插入数据的数值
        HashMap<String, Object> tagInfo = (HashMap<String, Object>) param.get("tagInfo");
        //标签名称check
        if (StringUtils.isEmpty(tagPathName) || tagPathName.getBytes().length > 50) {
            //标签名称小于50字节
            throw new BusinessException("7000074");
        }
        //判断是否是一级标签
        boolean firstTag = (boolean) param.get("first");
        HashMap<String, Object> tagSelectObject = (HashMap<String, Object>) param.get("tagSelectObject");
        CmsBtTagModel cmsBtTagModel = new CmsBtTagModel();
        //一级标签
        if (firstTag) {
            cmsBtTagModel.setChannelId(channelId);
            if (TAG_TYPE_SHOP_CLASSIFY.equals(String.valueOf(tagInfo.get("tagTypeSelectValue")))) {
                cmsBtTagModel.setTagName(TAG_TYPE_SHOP_CLASSIFY_NAME);
            }
            if (TAG_TYPE_PROMOTION.equals(String.valueOf(tagInfo.get("tagTypeSelectValue")))) {
                cmsBtTagModel.setTagName(TAG_TYPE_PROMOTION_NAME);
            }
            if (TAG_TYPE_GOODS.equals(String.valueOf(tagInfo.get("tagTypeSelectValue")))) {
                cmsBtTagModel.setTagName(TAG_TYPE_GOODS_NAME);
            }
            if (TAG_TYPE_FREE.equals(String.valueOf(tagInfo.get("tagTypeSelectValue")))) {
                cmsBtTagModel.setTagName(TAG_TYPE_FREE_NAME);
            }
            cmsBtTagModel.setTagPath("0");
            cmsBtTagModel.setTagPathName(tagPathName);
            cmsBtTagModel.setTagType(Integer.valueOf(String.valueOf(tagInfo.get("tagTypeSelectValue"))));
            cmsBtTagModel.setTagStatus(0);
            cmsBtTagModel.setSortOrder(0);
            cmsBtTagModel.setParentTagId(0);
            cmsBtTagModel.setIsActive(1);
            cmsBtTagModel.setCreater(userName);
            cmsBtTagModel.setModifier(userName);
            simpleTransaction.openTransaction();
            try {
                //将取得的数据插入到数据库
                cmsBtTagDao.insertCmsBtTag(cmsBtTagModel);
                //取得标签名称
                String id = String.valueOf(cmsBtTagModel.getTagId());
                //对tagPath进行二次组装
                cmsBtTagModel.setTagPath("-" + id + "-");
                //更新数据cms_bt_tag
                cmsBtTagDao.updateCmsBtTag(cmsBtTagModel);
            } catch (Exception e) {
                simpleTransaction.rollback();
                throw e;
            }
            simpleTransaction.commit();
        }
        if (!firstTag) {
            cmsBtTagModel.setChannelId(String.valueOf(tagSelectObject.get("channelId")));
            cmsBtTagModel.setTagName(String.valueOf(tagSelectObject.get("tagName")));
            cmsBtTagModel.setTagPath(String.valueOf(tagSelectObject.get("tagPath")));
            cmsBtTagModel.setTagPathName(String.valueOf(tagSelectObject.get("tagPathName")) + " > " + tagPathName);
            cmsBtTagModel.setTagType(Integer.valueOf(tagSelectObject.get("tagType").toString()));
            cmsBtTagModel.setTagStatus(Integer.valueOf(String.valueOf(tagSelectObject.get("tagStatus"))));
            cmsBtTagModel.setSortOrder(0);
            cmsBtTagModel.setIsActive(1);
            cmsBtTagModel.setParentTagId(Integer.valueOf(String.valueOf(tagSelectObject.get("id"))));
            cmsBtTagModel.setCreater(String.valueOf(tagSelectObject.get("creater")));
            cmsBtTagModel.setModifier(String.valueOf(tagSelectObject.get("modifier")));
            simpleTransaction.openTransaction();
            try {
                //将取得的数据插入到数据库
                cmsBtTagDao.insertCmsBtTag(cmsBtTagModel);
                //取得标签名称
                String id = String.valueOf(cmsBtTagModel.getTagId());
                //对tagPath进行二次组装
                cmsBtTagModel.setTagPath(cmsBtTagModel.getTagPath() + id + "-");
                //更新数据cms_bt_tag
                cmsBtTagDao.updateCmsBtTag(cmsBtTagModel);
            } catch (Exception e) {
                simpleTransaction.rollback();
                throw e;
            }
            simpleTransaction.commit();

        }
        //记住插入的记录处理之后再返回画面
        HashMap<String, Object> tagTypeSelectValue = new HashMap<>();
        tagTypeSelectValue.put("id", cmsBtTagModel.getTagId());
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
        tagTypeSelectValue.put("isActive", cmsBtTagModel.getIsActive());
        tagTypeSelectValue.put("tagChildrenName", (String) param.get("tagPathName"));
        tagTypeSelectValue.put("created", cmsBtTagModel.getCreated());
        tagTypeSelectValue.put("creater", cmsBtTagModel.getCreater());
        tagTypeSelectValue.put("modified", cmsBtTagModel.getModified());
        tagTypeSelectValue.put("modifier", cmsBtTagModel.getModified());
        editTagInfo(cmsBtTagModel, param, tagTypeSelectValue);
    }

    /**
     * 记住插入的记录处理之后再返回画面
     *
     * @param cmsBtTagModel
     * @param param
     */
    private void editTagInfo(CmsBtTagModel cmsBtTagModel, Map param, HashMap<String, Object> tagTypeSelectValue) {
        //取得插入数据库的parentTagId
        int parentTagId = cmsBtTagModel.getParentTagId();
        //取得前端的树的值
        Map tagInfo = (Map) param.get("tagInfo");
        //一级标签进行解析
        boolean isTagOne = false;
        List<HashMap<String, Object>> tagTree = (List<HashMap<String, Object>>) tagInfo.get("tagTree");
        //如果没有一级标签 添加一级标签
        if (cmsBtTagModel.getParentTagId() == 0 && !isTagOne) {
            tagTree.add(tagTypeSelectValue);
        }else{
            for (int i = 0; i < tagTree.size(); i++) {
                //如果一级标签有儿子,取得二级标签
                if (tagTree.get(i).get("children") != null) {
                    List<HashMap<String, Object>> children = (List<HashMap<String, Object>>) tagTree.get(i).get("children");
                    for (int j = 0; j < children.size(); j++) {
                        if (children.get(j).get("children") != null) {
                            List<HashMap<String, Object>> leaf = (List<HashMap<String, Object>>) children.get(j).get("children");
                            //如果二级标签有儿子,添加三级标签
                            if (parentTagId == (int) children.get(j).get("id") && !isTagOne) {
                                HashMap<String, Object> tagTwoValue = children.get(j);
                                tagTwoValue.put("isLeaf", false);
                                leaf.add(tagTypeSelectValue);
                                isTagOne = true;
                                break;
                            }
                        }
                        //如果二级标签没有儿子,添加三级标签
                        if (parentTagId == (Integer) children.get(j).get("id") && children.get(j).get("children") == null && !isTagOne) {
                            HashMap<String, Object> leaf = children.get(j);
                            leaf.put("children", tagTypeSelectValue);
                            leaf.put("isLeaf", false);
                            isTagOne = true;
                            break;
                        }
                    }
                    //如果一级标签有儿子,添加二级标签
                    if (parentTagId == (int) tagTree.get(i).get("id") && !isTagOne) {
                        HashMap<String, Object> tagOneValue = tagTree.get(i);
                        children.add(tagTypeSelectValue);
                        tagOneValue.put("isLeaf", false);
                        break;
                    }
                }
                //如果一级标签没有儿子,添加二级标签
                if (parentTagId == (Integer) tagTree.get(i).get("id") && tagTree.get(i).get("children") == null && !isTagOne) {
                    HashMap<String, Object> children = tagTree.get(i);
                    children.put("children", tagTypeSelectValue);
                    children.put("isLeaf", false);
                    break;
                }
            }
        }
    }

    /**
     * 更新isActive标志位
     *
     * @param param
     */
    public void DelTagInfo(Map param) {
        //子类标签
        int id = (Integer) param.get("id");
        //父类标签
        int parentTagId = (Integer) param.get("parentTagId");
        //子标签标志位
        boolean isTagOne = false;
        //取得标签树
        List<HashMap<String, Object>> tagTree = (List<HashMap<String, Object>>) param.get("tagTree");
        for (int i = 0; i < tagTree.size(); i++) {
            int tagOneTagId = (Integer) tagTree.get(i).get("id");
            if (id == tagOneTagId && !isTagOne) {
                tagTree.remove(i);
                param.put("tagTree", tagTree);
                break;
            }
            if (tagTree.get(i).get("children") != null) {
                List<HashMap<String, Object>> children = (List<HashMap<String, Object>>) tagTree.get(i).get("children");
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
                        List<HashMap<String, Object>> leaf = (List<HashMap<String, Object>>) children.get(j).get("children");
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
        cmsBtTagModel.setTagId(Integer.valueOf(id));
        cmsBtTagModel.setIsActive(0);
        simpleTransaction.openTransaction();
        try {
            cmsBtTagDao.updateCmsBtTag(cmsBtTagModel);
        } catch (Exception e) {
            simpleTransaction.rollback();
            throw e;
        }
        simpleTransaction.commit();
    }
}
