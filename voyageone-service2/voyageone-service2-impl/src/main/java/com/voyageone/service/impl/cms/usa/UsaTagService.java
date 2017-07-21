package com.voyageone.service.impl.cms.usa;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.CmsBtTagBean;
import com.voyageone.service.bean.cms.search.product.CmsProductCodeListBean;
import com.voyageone.service.daoext.cms.CmsBtTagDaoExt;
import com.voyageone.service.fields.cms.CmsBtTagModelTagType;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.impl.cms.search.product.CmsProductSearchQueryService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * USA CMS Tag management
 *
 * @Author rex.wu
 * @Create 2017-07-19 12:52
 */
@Service
public class UsaTagService extends BaseService {

    @Autowired
    private CmsBtTagDaoExt cmsBtTagDaoExt;
    @Autowired
    private CmsProductSearchQueryService cmsProductSearchQueryService;
    @Autowired
    private ProductService productService;

    /**
     * 获取USA CMS Free tags
     */
    public List<CmsBtTagBean> getUsaFreeTags(String channelId) {
        return cmsBtTagDaoExt.selectListByType(channelId, CmsBtTagModelTagType.usa_free_tags);
    }


    /**
     * 取得标签管理初始化数据（注意：高级检索画面(查询条件)使用时，查询的是最近90天的所有活动，包括已结束的）
     *
     * @return result
     */
    public Map<String, Object> getInitTagInfo(String channelId, Map<String, Object> params, String lang) {
        String orgFlg = (String) params.get("orgFlg"); //
        Integer selAllFlg = (Integer) params.get("selAllFlg");                              // 是否全选: 1用searchInfo检索全选;其他非全选,具体Code以selCodeList为准
        String selTagType = (String) params.get("selTagType");                              // 标签类型: USA 自由标签6
        List<String> selCodeList = (List<String>) params.get("selCodeList");                // 勾选的具体Code集合
        Map<String, Object> searchInfoMap = (Map<String, Object>) params.get("searchInfo"); // 全选时检索条件
        CmsSearchInfoBean2 searchInfo = JacksonUtil.json2Bean(JacksonUtil.bean2Json(searchInfoMap), CmsSearchInfoBean2.class);

        // 返回结果
        Map<String, Object> resultMap = new HashMap<>();

        // 先查询 美国自由标签(type=6), 然后将其Convert to Tree
        List<CmsBtTagBean> tagsList = this.getUsaFreeTags(channelId);
        List<CmsBtTagBean> tagTree = convertToTree(tagsList);
        resultMap.put("tagTree", tagTree);

        // 标签类型
        List<TypeBean> types = Types.getTypeList(TypeConfigEnums.MastType.tagType.getId(), lang);
        if (types != null) {
            resultMap.put("tagTypeList", types.stream().filter(type -> type.getValue().equals(String.valueOf(CmsBtTagModelTagType.usa_free_tags))).collect(Collectors.toList()));
        }

        orgFlg = "2";
        if (Objects.equals(orgFlg, "2")) {
            // 用于标识是否已勾选
            Map<String, Boolean> orgChkStsMap = new HashMap<>();
            // 用于标识是否半选（即不是所有商品都设置了该标签）
            Map<String, Boolean> orgDispMap = new HashMap<>();

            // 高级检索，设置自由标签的场合，需要检索一遍所选择商品的自由标签设值，返回到前端
            List<String> codeList = null;
            if (Objects.equals(selAllFlg, Integer.valueOf(1))) {
                CmsProductCodeListBean productCodeListBean = cmsProductSearchQueryService.getProductCodeList(searchInfo, channelId);
                long total = productCodeListBean.getTotalCount();
                if (total > 0) {
                    int pageSize = 1000;
                    long pageNum = (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
                    for (int i = 1; i <= pageNum; i++) {
                        searchInfo.setProductPageSize(pageSize);
                        searchInfo.setProductPageNum(i);
                        CmsProductCodeListBean subProductCodeListBean = cmsProductSearchQueryService.getProductCodeList(searchInfo, channelId);
                        if (subProductCodeListBean.getTotalCount() > 0l) {
                            this.pickFreeTags(channelId, subProductCodeListBean.getProductCodeList(), tagsList, orgChkStsMap, orgDispMap);
                        }
                    }
                }
            } else {
                codeList = selCodeList;
                if (CollectionUtils.isNotEmpty(codeList)) {
                    this.pickFreeTags(channelId, codeList, tagsList, orgChkStsMap, orgChkStsMap);
                }
            }
            resultMap.put("orgChkStsMap", orgChkStsMap);
            resultMap.put("orgDispMap", orgDispMap);
        }
        //返回数据类型
        return resultMap;
    }

    /**
     * 处理Free tags的全选和半选
     *
     * @param channelId    渠道ID
     * @param codeList     产品Code
     * @param tagsList     标签
     * @param orgChkStsMap 全选
     * @param orgDispMap   半选
     */
    private void pickFreeTags(String channelId, List<String> codeList, List<CmsBtTagBean> tagsList, Map<String, Boolean> orgChkStsMap, Map<String, Boolean> orgDispMap) {
        if (StringUtils.isBlank(channelId) || CollectionUtils.isEmpty(codeList)) {
            return;
        }
        if (orgChkStsMap == null) {
            orgChkStsMap = new HashMap<>();
        }
        if (orgDispMap == null) {
            orgDispMap = new HashMap<>();
        }
        // 检索商品的自由标签设值
        JongoQuery queryObj = new JongoQuery();
        queryObj.setQuery("{'common.fields.code':{$in:#}}");
        queryObj.setParameters(codeList);
        queryObj.setProjectionExt("prodId", "common.fields.code", "usFreeTags");
        List<CmsBtProductModel> prodList = productService.getList(channelId, queryObj);
        if (CollectionUtils.isNotEmpty(prodList)) {

            // TODO 此段先注释掉，即勾选子节点的话，再显示弹出画面时父节点也显示被勾选
            /*for (CmsBtProductModel prodObj : prodList) {
                List<String> tags = prodObj.getFreeTags();
                if (tags == null || tags.isEmpty()) {
                    continue;
                }
                // 先过滤一遍父节点
                for (int i = 0; i < tags.size(); i ++) {
                    String tagPath = tags.get(i);
                    for (String tagPath2 : tags) {
                        if (tagPath != null && tagPath2 != null && tagPath2.length() > tagPath.length() && tagPath2.startsWith(tagPath)) {
                            tags.set(i, null);
                        }
                    }
                }
                tags = tags.stream().filter(tagPath -> tagPath != null).collect(Collectors.toList());
                prodObj.setFreeTags(tags);
            }*/

            for (CmsBtTagBean tagBean : tagsList) {
                // 遍历商品列表，查看是否勾选(这里的tagsList是列表,不是树型结构)
                int selCnt = 0;
                for (CmsBtProductModel prodObj : prodList) {
                    List<String> tags = prodObj.getUsFreeTags();
                    if (tags == null || tags.isEmpty()) {
                        continue;
                    }
                    if (tags.indexOf(tagBean.getTagPath()) >= 0) {
                        // 有勾选
                        selCnt++;
                    }
                }
                if (selCnt == prodList.size()) {
                    if (orgDispMap.containsKey(tagBean.getTagPath())) {
                        // 如果在半选中直接跳过了
                        continue;
                    }
                    // 本页数据全选
                    orgChkStsMap.put(tagBean.getTagPath(), true);
                } else if (0 < selCnt && selCnt < prodList.size()) {
                    // 如果在全选中则移除
                    if (orgChkStsMap.containsKey(tagBean.getTagPath())) {
                        orgChkStsMap.remove(tagBean.getTagPath());
                    }
                    orgDispMap.put(tagBean.getTagPath(), true);
                }
            }
        }
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

}
