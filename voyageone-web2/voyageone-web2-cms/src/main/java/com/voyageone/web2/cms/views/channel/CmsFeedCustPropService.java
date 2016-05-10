package com.voyageone.web2.cms.views.channel;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.service.impl.cms.feed.FeedCategoryAttributeService;
import com.voyageone.service.impl.cms.feed.FeedCategoryTreeService;
import com.voyageone.service.impl.cms.feed.FeedCustomPropService;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedAttributesModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryTreeModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jiang, 2016/2/26
 * @version 2.0.0从
 */
@Service
public class CmsFeedCustPropService extends BaseAppService {

    @Autowired
    private FeedCategoryTreeService feedCategoryTreeService;
    @Autowired
    private FeedCustomPropService feedCustomPropService;

    @Autowired
    private FeedCategoryAttributeService feedCategoryAttributeService;

//    private Map<String, List<String>> attrMap = null;

    public Map<String, Object> getFeedCustProp(Map<String, String> params, UserSessionBean userInfo) {
        Map<String, Object> result = new HashMap<>();
        String catPath = StringUtils.trimToNull(params.get("cat_path"));
        int splitFlg = NumberUtils.toInt(params.get("unsplitFlg"), 0);
        if (splitFlg == 1) {
            // 合并为一个list输出
            List<Map<String, Object>> list = feedCustomPropService.getAllAttr(userInfo.getSelChannelId(), catPath);
            List<Map<String, Object>> valList = convertList(list, true, "0".equals(catPath));

            result.put("valList", valList);
        }else {
            // 分为两个list输出
            List<Map<String, Object>> list1 = feedCustomPropService.getOrigProp(userInfo.getSelChannelId(), "0");
            List<Map<String, Object>> list2 = feedCustomPropService.getTransProp(userInfo.getSelChannelId(), "0");
            List<Map<String, Object>> valList = convertList(list2, true, true);
            List<Map<String, Object>> unvalList = convertList(list1, false, true);

            // 判断是否全店铺共通属性
            String commFlg = CmsChannelConfigs.getConfigBeanNoCode(userInfo.getSelChannelId()
                    , CmsConstants.ChannelConfig.SAME_ATTR).getConfigValue1();
            if (!"1".equals(commFlg)) {
                if (!"0".equals(catPath)) {
                    List<Map<String, Object>> initAttrList = null;
                    // 查询指定类目(从mongo来的原始数据)
//                    CmsMtFeedCategoryTreeModelx catgAttrList = this.selectCatAttr(userInfo.getSelChannelId(), catPath);
//                    if (catgAttrList != null && catgAttrList.getCategoryTree().size() > 0) {
//                        List<CmsMtFeedCategoryModel> childList = catgAttrList.getCategoryTree();
//                        if (childList != null && childList.size() > 0) {
//                            attrMap = null;
//                            getSubCatTree(childList, catPath);
//                            if (attrMap != null) {
//                                initAttrList = new ArrayList<>(attrMap.size());
//                                for (String o : attrMap.keySet()) {
//                                    Map<String, Object> objMap = new HashMap<>();
//                                    objMap.put("prop_id", "");
//                                    objMap.put("prop_original", o);
//                                    objMap.put("cat_path", catPath);
//                                    initAttrList.add(objMap);
//                                }
//                            }
//                        }
//                    }

                    CmsMtFeedAttributesModel attr = feedCategoryAttributeService.getCategoryAttributeByCategory(userInfo.getSelChannelId(), catPath);
                    if(attr != null){
                        initAttrList = new ArrayList<>();
                        for (String o : attr.getAttribute().keySet()) {
                            Map<String, Object> objMap = new HashMap<>();
                            objMap.put("prop_id", "");
                            objMap.put("prop_original", o);
                            objMap.put("cat_path", catPath);
                            initAttrList.add(objMap);
                        }
                    }
                    // 过滤已翻译的属性
                    List<Map<String, Object>> custArr1 = feedCustomPropService.getOrigProp(userInfo.getSelChannelId(), catPath);
                    List<Map<String, Object>> custArr2 = feedCustomPropService.getTransProp(userInfo.getSelChannelId(), catPath);
                    if (custArr1 == null) {
                        custArr1 = new ArrayList<>();
                    }
                    if (custArr2 == null) {
                        custArr2 = new ArrayList<>();
                    }
                    List<Map<String, Object>> valList2 = convertList(custArr2, true, false);
                    List<Map<String, Object>> unvalList2 = convertList(custArr1, false, false);

                    // 合并过滤后的未翻译自定义属性
                    filterList(valList2, initAttrList);
                    filterList(unvalList2, initAttrList);
                    if (initAttrList != null && initAttrList.size() > 0) {
                        unvalList2.addAll(initAttrList);
                    }

                    // 最后合并共通属性和自定义属性
                    filterList(valList2, valList);
                    filterList(unvalList2, unvalList);
                    valList.addAll(valList2);
                    unvalList.addAll(unvalList2);
                }
            }
            result.put("sameAttr", commFlg);
            result.put("valList", valList);
            result.put("unvalList", unvalList);
        }
        return result;
    }

    public Map<String, Object> saveFeedCustProp(Map<String, Object> params, UserSessionBean userInfo) {
        Map<String, Object> result = new HashMap<>();
        String catPath = StringUtils.trimToNull((String) params.get("cat_path"));
        List<Map<String, Object>> valList = (List<Map<String, Object>>) params.get("valList");
        List<Map<String, Object>> unvalList = (List<Map<String, Object>>) params.get("unvalList");
        List<Map<String, Object>> addList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> updList = new ArrayList<Map<String, Object>>();
        String propId = null;
        String cat_path = null;

        if (valList != null) {
            for (Map<String, Object> item : valList) {
                cat_path = StringUtils.trimToNull((String) item.get("cat_path"));
                Object propIdObj = item.get("prop_id");
                propId = null;
                if (propIdObj != null) {
                    propId = StringUtils.trimToNull(propIdObj.toString());
                }
                if (propId == null) {
                    // 新规插入由前端去控制传进来的值为0
//                    if ("0".equals(catPath) && "0".equals(cat_path)) {
                        // 新增属性,只能新增共通属性
                        if (feedCustomPropService.isAttrExist(item, catPath, userInfo.getSelChannelId())) {
                            $warn("该属性亦存在 " + item.toString());
                        } else {
                            addList.add(item);
                        }
//                    }
                } else {
                    // 修改属性
                    if ("0".equals(catPath) && "0".equals(cat_path)) {
                        updList.add(item);
                    } else if (!"0".equals(catPath) && !"0".equals(cat_path) && catPath.equals(cat_path)) {
                        updList.add(item);
                    }
                }
            }
        }
        if (unvalList != null) {
            for (Map<String, Object> item : unvalList) {
                item.put("prop_translation", "");
                cat_path = StringUtils.trimToNull((String) item.get("cat_path"));
                Object propIdObj = item.get("prop_id");
                propId = null;
                if (propIdObj != null) {
                    propId = StringUtils.trimToNull(propIdObj.toString());
                }
                if (propId == null) {
                    if ("0".equals(catPath) && "0".equals(cat_path)) {
                        // 新增属性,只能新增共通属性
                        if (feedCustomPropService.isAttrExist(item, catPath, userInfo.getSelChannelId())) {
                            $warn("该属性亦存在 " + item.toString());
                        } else {
                            addList.add(item);
                        }
                    }
                } else {
                    // 修改属性
                    if ("0".equals(catPath) && "0".equals(cat_path)) {
                        updList.add(item);
                    } else if (!"0".equals(catPath) && !"0".equals(cat_path) && catPath.equals(cat_path)) {
                        updList.add(item);
                    }
                }
            }
        }
        feedCustomPropService.saveAttr(addList, updList, catPath, userInfo.getSelChannelId(), userInfo.getUserName());
        return result;
    }

//    private CmsMtFeedCategoryModel findChildCategoryByCid (List<CmsMtFeedCategoryModel> childList, String cid) {
//        CmsMtFeedCategoryModel result = new CmsMtFeedCategoryModel();
//        for(CmsMtFeedCategoryModel childCategory : childList) {
//            if (childCategory.getIsChild() == 0) {
//                result = findChildCategoryByCid(childCategory.getChild(), cid);
//            } else if (childCategory.getIsChild() == 1 && cid.equals(childCategory.getCid())) {
//                result = childCategory;
//            } else {
//                continue;
//            }
//        }
//        return result;
//    }

    // 取得类目路径数据
    public List<CmsMtCategoryTreeModel> getTopCategories(UserSessionBean user) {
//        CmsMtFeedCategoryTreeModelx treeModelx = cmsMtFeedCategoryTreeDao.selectFeedCategoryx(user.getSelChannelId());
        List<CmsMtFeedCategoryTreeModel> feedBeanList = feedCategoryTreeService.getFeedAllCategoryTree(user.getSelChannelId());
        return feedBeanList.stream().map(this::buildFeedCategoryBean).collect(Collectors.toList());
    }

    public List<CmsMtFeedCategoryTreeModel> getCategoryList (UserSessionBean userInfo) {
        //HashMap dataMap = new HashMap(1);
        List<CmsMtFeedCategoryTreeModel> topTree = feedCategoryTreeService.getFeedAllCategoryList(userInfo.getSelChannelId());
        List<CmsMtFeedCategoryTreeModel> rsltList = new ArrayList<>();
        CmsMtFeedCategoryTreeModel comMdl = new CmsMtFeedCategoryTreeModel();
        comMdl.setCatPath("共通属性");
        comMdl.setCatName("共通属性");
        comMdl.setCatId("共通属性");
        comMdl.setIsParent(0);
        rsltList.add(comMdl);
        rsltList.addAll(topTree);
//        getSubCatTree2List(topTree, rsltList);
//        for (CmsMtFeedCategoryModel catItem : rsltList) {
//            catItem.setChild(null);
//            catItem.setAttribute(null);
//        }

        return rsltList;
    }

    public Map<String, Object> getFeedCustPropValueList(Map<String, String> params, UserSessionBean userInfo) {
        String catPath = StringUtils.trimToNull(params.get("cat_path"));
        int tSts = org.apache.commons.lang3.math.NumberUtils.toInt(params.get("sts"), 2);
        String propName = StringUtils.trimToNull(params.get("propName"));
        String propValue = StringUtils.trimToNull(params.get("propValue"));
        if (tSts == 0) {
            // 查询未翻译的属性值时，不需要输入属性值
            propValue = null;
        }
        int skip = org.apache.commons.lang3.math.NumberUtils.toInt(params.get("skip"));
        int limit = org.apache.commons.lang3.math.NumberUtils.toInt(params.get("limit"));

        // 查询共通属性及类目属性
        List<Map<String, Object>> rslt1 = feedCustomPropService.getPropValue(catPath, tSts, propName, propValue, userInfo.getSelChannelId());
        if (rslt1 == null) {
            rslt1 = new ArrayList<>(0);
        }

        Map<String, Object> dataMap = new HashMap<>();
        int listCnt = rslt1.size();
        dataMap.put("total", listCnt);
        if (listCnt == 0 || skip == 0 || limit == 0) {
            dataMap.put("resultData", rslt1);
        } else {
            int staIdx = (skip - 1) * limit;
            int endIdx = staIdx + limit;
            if (listCnt < endIdx) {
                endIdx = listCnt;
            }
            dataMap.put("resultData", rslt1.subList(staIdx, endIdx));
        }

        return dataMap;
    }

    public Map<String, Object> addFeedCustPropValue(Map<String, String> params, UserSessionBean userInfo) {
        int propId = org.apache.commons.lang3.math.NumberUtils.toInt(params.get("prop_id"));
        String origValue = StringUtils.trimToNull(params.get("value_original"));
        String transValue = StringUtils.trimToEmpty(params.get("value_translation"));

        // 先判断该属性值是否已存在
        if (feedCustomPropService.isPropValueExist(propId, userInfo.getSelChannelId(), origValue)) {
            throw new BusinessException("重复翻译的属性值");
        }

        int rslt = feedCustomPropService.addPropValue(propId, userInfo.getSelChannelId(), origValue, transValue, userInfo.getUserName());
        if (rslt == 0) {
            throw new BusinessException("新增翻译后的属性值不成功");
        }
        return new HashMap<>();
    }

    public Map<String, Object> saveFeedCustPropValue (Map<String, String> params, UserSessionBean userInfo) {
        int valueId = org.apache.commons.lang3.math.NumberUtils.toInt(params.get("value_id"));
        String transValue = StringUtils.trimToEmpty(params.get("value_translation"));

        // 先判断该属性值是否已存在
        if (!feedCustomPropService.isPropValueExist(valueId)) {
            throw new BusinessException("该属性值不存在");
        }

        int rslt = feedCustomPropService.savePropValue(valueId, transValue, userInfo.getUserName());
        if (rslt == 0) {
            throw new BusinessException("更新翻译后的属性值不成功");
        }

        return new HashMap<>();
    }

//    private void getSubCatTree2List(List<CmsMtFeedCategoryModel> childList, List<CmsMtFeedCategoryModel> rsltList) {
//        for (CmsMtFeedCategoryModel catItem : childList) {
//            rsltList.add(catItem);
//            if (catItem.getIsChild() == 0) {
//                getSubCatTree2List(catItem.getChild(), rsltList);
//            }
//        }
//    }

    private List<Map<String, Object>> convertList(List<Map<String, Object>> inputList, boolean hasValue, boolean isComm) {
        List<Map<String, Object>> rslt = new ArrayList<>();
        if (inputList == null || inputList.size() == 0) {
            return rslt;
        }
        for (Map<String, Object> item : inputList) {
            Map<String, Object> objMap = new HashMap<>();
            objMap.put("prop_id", item.get("prop_id"));
            objMap.put("prop_original", item.get("feed_prop_original"));
            if (hasValue) {
                objMap.put("prop_translation", item.get("feed_prop_translation"));
            }
            if (isComm) {
                objMap.put("cat_path", "0");
            } else {
                objMap.put("cat_path", item.get("feed_cat_path"));
            }
            rslt.add(objMap);
        }
        return rslt;
    }

//    // 根据类目路径查询自定义未翻译属性信息(不包含共通属性)
//    private CmsMtFeedCategoryTreeModelx selectCatAttr(String channelId, String categoryId) {
////        Query query = new Query();
////        Criteria criteria = Criteria.where("channelId").is(channelId);
////        query.addCriteria(criteria);
//
//        return feedCategoryTreeService.getFeedCategory(channelId);
////        return mongoTemplate.find(query, Object.class, "cms_mt_feed_category_tree");
//        //TODO-- 这里只能使用Object对象来影射，不能使用Map.class，可能是spring mongoTemplate的问题
//    }

//    private void getSubCatTree(List<CmsMtFeedCategoryModel> childList, String catPath) {
//        for (CmsMtFeedCategoryModel catItem : childList) {
//            if (catItem.getIsChild() == 1) {
//                if (catPath.equals(catItem.getCid())) {
//                    attrMap = catItem.getAttribute();
//                    break;
//                }
//            } else {
//                getSubCatTree(catItem.getChild(), catPath);
//            }
//        }
//    }

    private void filterList(List<Map<String, Object>> list1, List<Map<String, Object>> list2) {
        if (list1 == null || list1.size() == 0) {
            return;
        }
        for (Map<String, Object> item : list1) {
            String propName = (String) item.get("prop_original");
            if (list2 != null && list2.size() > 0) {
                int i = 0;
                boolean hasValue = false;
                for (Map<String, Object> item2 : list2) {
                    String propName2 = (String) item2.get("prop_original");
                    if (propName.equals(propName2)) {
                        hasValue = true;
                        break;
                    }
                    i ++;
                }
                if (hasValue) {
                    list2.remove(i);
                }
            }
        }
    }



    /**
     * 递归重新给Feed类目赋值 并转换成CmsMtCategoryTreeModel.
     */
    private CmsMtCategoryTreeModel buildFeedCategoryBean(CmsMtFeedCategoryTreeModel feedCategoryModel) {

        CmsMtCategoryTreeModel cmsMtCategoryTreeModel = new CmsMtCategoryTreeModel();

        cmsMtCategoryTreeModel.setCatId(feedCategoryModel.getCatId());
        cmsMtCategoryTreeModel.setCatName(feedCategoryModel.getCatName());
        cmsMtCategoryTreeModel.setCatPath(feedCategoryModel.getCatPath());
        cmsMtCategoryTreeModel.setIsParent(feedCategoryModel.getIsParent());

        // 先取出暂时保存
        List<CmsMtFeedCategoryTreeModel> children = feedCategoryModel.getChildren();
        List<CmsMtCategoryTreeModel> newChild = new ArrayList<>();

        if (children != null && !children.isEmpty())
            for (CmsMtFeedCategoryTreeModel child : children) {
                newChild.add(buildFeedCategoryBean(child));
            }
        cmsMtCategoryTreeModel.setChildren(newChild);

        return cmsMtCategoryTreeModel;
    }
}
