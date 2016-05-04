package com.voyageone.web2.cms.views.channel;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsBtTagBean;
import com.voyageone.service.dao.cms.CmsBtTagDao;
import com.voyageone.service.daoext.cms.CmsBtTagDaoExt;
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
    private CmsBtTagDaoExt cmsBtTagDaoExt;
    @Autowired
    private SimpleTransaction simpleTransaction;

    /**
     * 根据channelId取得素有的标签并对其进行分类
     *
     * @param param
     * @return ret
     */
    public List<CmsBtTagBean> getTagInfoByChannelId(Map param) {
        //公司平台销售渠道
        String channelId = (String) param.get("channel_id");
        //标签类型
        String tagTypeSelectValue = (String) param.get("tagTypeSelectValue");
        //取得所有的标签类型
        List<CmsBtTagBean> categoryList = cmsBtTagDaoExt.selectListByChannelIdAndTagType(channelId, tagTypeSelectValue);
        //循环取得标签并对其进行分类
        List<CmsBtTagBean> ret = new ArrayList<>();
        for (CmsBtTagBean each : categoryList) {
            //取得一级标签
            if (each.getParentTagId() == 0) {
                ret.add(each);
            }
            for (CmsBtTagBean inner : categoryList) {
                //取得子标签
                if (each.getId() == inner.getParentTagId()) {
                    if (each.getChildren() == null) {
                        //添加一个子标签
                        each.setChildren(new ArrayList<CmsBtTagBean>());
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
                each.setChildren(new ArrayList<CmsBtTagBean>());
            } else {
                //非子标签
                each.setIsLeaf(false);
            }
        }
        //返回数据类型
        return ret;
    }

    /**
     * 查询指定标签类型下的所有标签(list形式)
     *
     * @param param
     * @return List<CmsBtTagModel>
     */
    public List<CmsBtTagModel> getTagInfoList(Map param) {
        // 取得所有的标签类型
        List<CmsBtTagBean> tagsList = getTagInfoByChannelId(param);
        //循环取得标签并对其进行分类
        List<CmsBtTagModel> ret = new ArrayList<>();
        tagsList.forEach(item -> {
            ret.addAll(treeToList(item));
        });
        //返回数据类型
        return ret;
    }

    /**
     * 一棵树转成list（拍平）
     * @param tagBean 一棵树
     * @return List<CmsBtTagModel>
     */
    private List<CmsBtTagModel> treeToList(CmsBtTagBean tagBean){
        List<CmsBtTagModel> result = new ArrayList<>();
        result.add(tagBean);
        if (tagBean.getChildren() != null) {
            tagBean.getChildren().forEach(item -> {
                result.addAll(treeToList(item));
            });
            tagBean.setChildren(null);
        }
        return result;
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
        //创建者/更新者用
        String userName = (String) param.get("userName");
        //插入数据的数值
        HashMap<String, Object> tagInfo = (HashMap<String, Object>) param.get("tagInfo");
        String tagTypeValue=String.valueOf(tagInfo.get("tagTypeSelectValue"));
        //父级标签ID
        String parentTagId="";
        //取得标签名称
        String tagPathNameValue="";
        //判断是否是一级标签
        boolean firstTag = (boolean) param.get("first");
        //取得所有的标签类型
        HashMap<String, Object> tagSelectObject = (HashMap<String, Object>) param.get("tagSelectObject");
        if (firstTag) {
            parentTagId="0";
            tagPathNameValue=tagPathName;
        }else{
            tagPathNameValue=String.valueOf(tagSelectObject.get("tagPathName")) + " > " + tagPathName;
            parentTagId=String.valueOf(tagSelectObject.get("id"));
        }
        //标签名称check
        if (StringUtils.isEmpty(tagPathName) || tagPathName.getBytes().length > 50) {
            //标签名称小于50字节
            throw new BusinessException("7000074");
        }
        //tag中如果同一级中添加一个名字一样的，提示不能添加
        List<CmsBtTagModel> categoryList = cmsBtTagDaoExt.selectCmsBtTagByTagInfo(channelId, parentTagId,tagTypeValue);
        //标签名称小于50字节
        for(int i=0;i<categoryList.size();i++){
            if(categoryList.get(i).getTagPathName().toString().equals(tagPathNameValue)){
                throw new BusinessException("7000080");
            }
        }
        CmsBtTagModel cmsBtTagModel = new CmsBtTagModel();
        //一级标签
        if (firstTag) {
            cmsBtTagModel.setChannelId(channelId);
            if (TAG_TYPE_SHOP_CLASSIFY.equals(tagTypeValue)) {
                cmsBtTagModel.setTagName(TAG_TYPE_SHOP_CLASSIFY_NAME);
            }
            if (TAG_TYPE_PROMOTION.equals(tagTypeValue)) {
                cmsBtTagModel.setTagName(TAG_TYPE_PROMOTION_NAME);
            }
            if (TAG_TYPE_GOODS.equals(tagTypeValue)) {
                cmsBtTagModel.setTagName(TAG_TYPE_GOODS_NAME);
            }
            if (TAG_TYPE_FREE.equals(tagTypeValue)) {
                cmsBtTagModel.setTagName(TAG_TYPE_FREE_NAME);
            }
            cmsBtTagModel.setTagPath("0");
            cmsBtTagModel.setTagPathName(tagPathName);
            cmsBtTagModel.setTagType(Integer.valueOf(String.valueOf(tagInfo.get("tagTypeSelectValue"))));
            cmsBtTagModel.setTagStatus(0);
            cmsBtTagModel.setSortOrder(0);
            cmsBtTagModel.setParentTagId(0);
            cmsBtTagModel.setActive(1);
            cmsBtTagModel.setCreater(userName);
            cmsBtTagModel.setModifier(userName);
            simpleTransaction.openTransaction();
            try {
                //将取得的数据插入到数据库
                cmsBtTagDaoExt.insertCmsBtTag(cmsBtTagModel);
                //取得标签名称
                String id = String.valueOf(cmsBtTagModel.getId());
                //对tagPath进行二次组装
                cmsBtTagModel.setTagPath("-" + id + "-");
                //更新数据cms_bt_tag
                cmsBtTagDaoExt.updateCmsBtTag(cmsBtTagModel);
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
            cmsBtTagModel.setActive(1);
            cmsBtTagModel.setParentTagId(Integer.valueOf(String.valueOf(tagSelectObject.get("id"))));
            cmsBtTagModel.setCreater(String.valueOf(tagSelectObject.get("creater")));
            cmsBtTagModel.setModifier(String.valueOf(tagSelectObject.get("modifier")));
            simpleTransaction.openTransaction();
            try {
                //将取得的数据插入到数据库
                cmsBtTagDao.insert(cmsBtTagModel);
                //取得标签名称
                String id = String.valueOf(cmsBtTagModel.getId());
                //对tagPath进行二次组装
                cmsBtTagModel.setTagPath(cmsBtTagModel.getTagPath() + id + "-");
                //更新数据cms_bt_tag
                cmsBtTagDao.update(cmsBtTagModel);
            } catch (Exception e) {
                simpleTransaction.rollback();
                throw e;
            }
            simpleTransaction.commit();

        }
        //记住插入的记录处理之后再返回画面
        HashMap<String, Object> tagTypeSelectValue = new HashMap<>();
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
        cmsBtTagModel.setId(Integer.valueOf(id));
        cmsBtTagModel.setActive(0);
        simpleTransaction.openTransaction();
        try {
            cmsBtTagDaoExt.updateCmsBtTag(cmsBtTagModel);
        } catch (Exception e) {
            simpleTransaction.rollback();
            throw e;
        }
        simpleTransaction.commit();
    }
}
