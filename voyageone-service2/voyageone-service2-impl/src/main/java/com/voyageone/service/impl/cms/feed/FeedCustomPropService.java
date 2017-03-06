package com.voyageone.service.impl.cms.feed;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.feed.FeedCustomPropWithValueBean;
import com.voyageone.service.daoext.cms.CmsMtFeedCustomOptionDaoExt;
import com.voyageone.service.daoext.cms.CmsMtFeedCustomPropDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.CmsBtCustomPropService;
import com.voyageone.service.model.cms.CmsMtFeedCustomOptionModel;
import com.voyageone.service.model.cms.CmsMtFeedCustomPropModel;
import com.voyageone.service.model.cms.mongo.CmsBtCustomPropModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 获取自定义属性的列表(含翻译), 同时获取属性相应的属性值的列表(含翻译)
 * <p>
 * Created by zhujiaye on 16/2/26.
 */
@Service
public class FeedCustomPropService extends BaseService {

    @Autowired
    private CmsMtFeedCustomPropDaoExt cmsMtFeedCustomPropDaoExt;
    @Autowired
    private CmsMtFeedCustomOptionDaoExt cmsMtFeedCustomOptionDaoExt;

    @Autowired
    private CmsBtCustomPropService cmsBtCustomPropService;

    private class FeedCustomPropData {
        // 自定义属性
        private List<FeedCustomPropWithValueBean> customPropList;
        private Map<String, Map<String, Map<String, List<String>>>> customPropMap; // customPropList的简化版 (全小写) (<类目名称 <属性名称, <属性值, 属性值翻译列表>>>)
        // 翻译: 全店共通
        private Map<String, String> propCommonPublic;

        public List<FeedCustomPropWithValueBean> getCustomPropList() {
            return customPropList;
        }

        public void setCustomPropList(List<FeedCustomPropWithValueBean> customPropList) {
            this.customPropList = customPropList;
        }

        public Map<String, Map<String, Map<String, List<String>>>> getCustomPropMap() {
            return customPropMap;
        }

        public void setCustomPropMap(Map<String, Map<String, Map<String, List<String>>>> customPropMap) {
            this.customPropMap = customPropMap;
        }

        public Map<String, String> getPropCommonPublic() {
            return propCommonPublic;
        }

        public void setPropCommonPublic(Map<String, String> propCommonPublic) {
            this.propCommonPublic = propCommonPublic;
        }
    }

    /**
     * 初始化一下 ( 获取最新的数据, 整理好的数据放在: customPropList )
     * 每次大量处理之前最好都做一遍, 防止别人修改了什么内容没有及时反映
     *
     * @param channel_id channel id
     */
    private FeedCustomPropData doInit(String channel_id) {
        FeedCustomPropData result = new FeedCustomPropData();
        // 自定义属性
        List<FeedCustomPropWithValueBean> customPropList;
        Map<String, Map<String, Map<String, List<String>>>> customPropMap; // customPropList的简化版 (全小写) (<类目名称 <属性名称, <属性值, 属性值翻译列表>>>)
        // 翻译: 全店共通
        Map<String, String> propCommonPublic;

        // 翻译: 全店共通
        propCommonPublic = new HashMap<>();
        // 翻译: 无视category级别的共通
        Map<Integer, Map<String, String>> propCommon = new HashMap<>(); // ( <属性id <属性值, 属性值的翻译> > )
        Map<String, Map<String, String>> propCommonN = new HashMap<>(); // ( <属性名 <属性值, 属性值的翻译> > )

        // 获取表里的数据 (当前渠道的所有数据)
        customPropList = cmsMtFeedCustomOptionDaoExt.selectPropList(channel_id);
        for (FeedCustomPropWithValueBean customProp : customPropList) {
            customProp.setMapPropValue(new HashMap<>());
        }
        List<CmsMtFeedCustomOptionModel> propValueList = cmsMtFeedCustomOptionDaoExt.selectPropValue(channel_id);

        // 数据整理 - 将 <属性值得翻译> 的内容整合到 <属性列表> 中去
        // 注意: 不属于任何prop (就是prop_id为0) 的数据, 将会被扔到 customtranslationList
        for (CmsMtFeedCustomOptionModel propValue : propValueList) {
            if (propValue.getPropId() == 0) {
                // 全店共通的翻译
                propCommonPublic.put(propValue.getFeedValueOriginal(), propValue.getFeedValueTranslation());
            } else {
                // 找找看, 找得到就挂上去, 找不到也不管, 直接扔掉
                for (FeedCustomPropWithValueBean customProp : customPropList) {
                    if (customProp.getId() == propValue.getPropId()) {
                        // 找到了
                        List<String> lst = new ArrayList<>();
                        lst.add(propValue.getFeedValueTranslation());
                        customProp.getMapPropValue().put(propValue.getFeedValueOriginal(), lst);

                        break;
                    }
                }
            }
        }

        // 获取无视category级别的共通
        for (FeedCustomPropWithValueBean customProp : customPropList) {
            if (StringUtils.isEmpty(customProp.getFeed_cat_path()) || "0".equals(customProp.getFeed_cat_path())) {
                // 设置属性名称
                propCommon.put(customProp.getId(), new HashMap<>());
            }
        }
        for (CmsMtFeedCustomOptionModel customPropValue : propValueList) {
            if (propCommon.containsKey(customPropValue.getPropId())) {
                propCommon.get(customPropValue.getPropId()).put(customPropValue.getFeedValueOriginal(), customPropValue.getFeedValueTranslation());
            }
        }
        for (Map.Entry<Integer, Map<String, String>> entry : propCommon.entrySet()) {
            Integer key = entry.getKey(); // 属性id
            Map<String, String> val = entry.getValue();    // 属性值的翻译列表

            for (FeedCustomPropWithValueBean customProp : customPropList) {
                if (customProp.getId() == key) {
                    propCommonN.put(customProp.getFeed_prop_original(), val);

                    break;
                }
            }
        }

        // 在category级的结构体中补充一些无视category级别备选的翻译数据
        for (FeedCustomPropWithValueBean propModel : customPropList) {
            // 遍历所有属性值
            for (Map.Entry<String, List<String>> entry : propModel.getMapPropValue().entrySet()) {
                String key = entry.getKey();        // 属性值
                List<String> val = entry.getValue();    // 属性值的翻译列表

                // 自己不是共通的话, 才会走进来. 如果自己就是共通的话, 就没有必要进来了
                if (!StringUtils.isEmpty(propModel.getFeed_cat_path()) && !"0".equals(propModel.getFeed_cat_path())) {
                    // 看看这个key是否有 <无视category级的翻译> 翻译
                    if (propCommonN.containsKey(propModel.getFeed_prop_original())) { // 找到属性了
                        if (propCommonN.get(propModel.getFeed_prop_original()).containsKey(key)) {
                            String tmpTrans = propCommonN.get(propModel.getFeed_prop_original()).get(key);
                            if (!StringUtils.isEmpty(tmpTrans)) {
                                // 设置一下
                                val.add(tmpTrans);
                            }
                        }
                    }
                }

                // 看看这个key是否有 <全店铺共通> 翻译
                if (propCommonPublic.containsKey(key)) {
                    val.add(propCommonPublic.get(key));
                }
            }
        }

        // 设置简化版 (<类目名称 <属性名称, <属性值, 属性值翻译>>>)
        customPropMap = new HashMap<>();
        for (FeedCustomPropWithValueBean propModel : customPropList) {
            // 属性值英文全部变成小写
            Map<String, List<String>> propValueLower = new HashMap<>();
            {
                for (Map.Entry<String, List<String>> entry : propModel.getMapPropValue().entrySet()) {
                    String key = entry.getKey().toLowerCase();        // 属性值
                    List<String> val = entry.getValue();    // 属性值的翻译列表
                    propValueLower.put(key, val);
                }
            }

            // 看看类目是否存在
            if (customPropMap.containsKey(propModel.getFeed_cat_path().toLowerCase())) {
                // 存在类目的
                // 同一个类目里是不可以有相同属性名称的, 所以无需判断是否重复, 如果有就直接覆盖
                customPropMap.get(propModel.getFeed_cat_path().toLowerCase()).put(propModel.getFeed_prop_original().toLowerCase(), propValueLower);

            } else {
                // 添加这个类目
                Map<String, Map<String, List<String>>> mapCat = new HashMap<>();

                // <属性名称 <属性值名, 属性值翻译列表>>
                mapCat.put(propModel.getFeed_prop_original(), propValueLower);

                customPropMap.put(propModel.getFeed_cat_path().toLowerCase(), mapCat);
            }
        }

        // 自定义属性
        result.setCustomPropList(customPropList);
        result.setCustomPropMap(customPropMap); // customPropList的简化版 (全小写) (<类目名称 <属性名称, <属性值, 属性值翻译列表>>>)
        // 翻译: 全店共通
        result.setPropCommonPublic(propCommonPublic);

        return result;
    }

    /**
     * 获取自定义属性列表
     * 注意点:
     * 1. 如果feed_cat_path为0或空, 那么只显示公共属性
     * 2. 显示优先顺为: 公共属性 -> 类目属性 (数据库里有display_order, 之前init里的sql已经处理掉了)
     * 3. 类目属性名称如果与公共属性名称一致, 那么翻译以类目属性的翻译为准
     * 4. 如果类目属性名称没有翻译, 那么就认为该类目无需显示
     *
     * @param channel_id    channel id
     * @param feed_cat_path feed cat path
     * @return 自定义属性列表 (返回的内容里, 其实主要就是feed_prop_original 和 feed_prop_translation比较有用)
     */
    public List<FeedCustomPropWithValueBean> getPropList(
            String channel_id,
            String feed_cat_path
    ) {
        FeedCustomPropData feedCustomPropData = doInit(channel_id);

        // 自定义属性
        List<FeedCustomPropWithValueBean> customPropList = feedCustomPropData.getCustomPropList();
        Map<String, Map<String, Map<String, List<String>>>> customPropMap = feedCustomPropData.getCustomPropMap(); // customPropList的简化版 (全小写) (<类目名称 <属性名称, <属性值, 属性值翻译列表>>>)
        // 翻译: 全店共通
        Map<String, String> propCommonPublic = feedCustomPropData.getPropCommonPublic();

        List<FeedCustomPropWithValueBean> result = new ArrayList<>();

        // 抽出公共字段
        for (FeedCustomPropWithValueBean propModel : customPropList) {
            if (StringUtils.isEmpty(propModel.getFeed_cat_path()) || "0".equals(propModel.getFeed_cat_path())) {
                result.add(propModel);
            }
        }

        // 设定类目字段
        if (!StringUtils.isEmpty(feed_cat_path) && !"0".equals(feed_cat_path)) {
            // 遍历一下
            for (FeedCustomPropWithValueBean propModel : customPropList) {
                // 如果类目名称一致
                if (propModel.getFeed_cat_path().equals(feed_cat_path)) {
                    String fanyi = propModel.getFeed_prop_translation();

                    // 看看公共属性里是否有存在
                    boolean blnFound = false;
                    for (FeedCustomPropWithValueBean prop : result) {
                        // 找到了的场合
                        if (prop.getFeed_prop_original().equals(propModel.getFeed_prop_original())) {
                            // 看看是否有翻译
                            if (!StringUtils.isEmpty(fanyi)) {
                                // 有翻译的话, 那就替换掉翻译
                                prop.setFeed_prop_translation(propModel.getFeed_prop_translation());
                            } else {
                                // 如果没有翻译, 那就删掉这个公共属性
                                result.remove(prop);
                            }

                            blnFound = true;
                            break;
                        }
                    }
                    // 如果没找到
                    if (!blnFound) {
                        // 如果有翻译, 那就加上, 如果没有翻译, 那就忽略这个字段
                        if (!StringUtils.isEmpty(fanyi)) {
                            result.add(propModel);
                        }
                    }

                }
            }
        }

        return result;
    }

    public List<FeedCustomPropWithValueBean> getPropListForEdit() {
        // TODO: 这里是给前台编辑用的, 用来抽出基本的信息

        return null;
    }

    /**
     * 求翻译 (忽略待翻译的内容的大小写, 包括其所在的类目和属性名,都忽略大小写)
     *
     * @param channel_id    channel id
     * @param feed_cat_path 要翻译的内容, 所处的feed category path, 如果不知道就设0
     * @param prop_name     要翻译的内容, 是哪个属性里的, 如果不知道就设0
     * @param value         要翻译的内容
     * @return 翻译好的内容
     */
    public String getPropTrans(String channel_id, String feed_cat_path, String prop_name, String value) {
        FeedCustomPropData feedCustomPropData = doInit(channel_id);

        // 自定义属性
        List<FeedCustomPropWithValueBean> customPropList = feedCustomPropData.getCustomPropList();
        Map<String, Map<String, Map<String, List<String>>>> customPropMap = feedCustomPropData.getCustomPropMap(); // customPropList的简化版 (全小写) (<类目名称 <属性名称, <属性值, 属性值翻译列表>>>)
        // 翻译: 全店共通
        Map<String, String> propCommonPublic = feedCustomPropData.getPropCommonPublic();

        String result = "";

        // 从customPropList的简化版中获取数据
        Map<String, Map<String, List<String>>> propMap = null;
        if (customPropMap.containsKey(feed_cat_path.toLowerCase())) {
            // 指定类目
            propMap = customPropMap.get(feed_cat_path.toLowerCase());
        } else if (customPropMap.containsKey("0")) {
            // 类目级共通
            propMap = customPropMap.get("0");
        }

        if (propMap != null) {
            if (propMap.containsKey(prop_name.toLowerCase())) {
                Map<String, List<String>> valueMap = propMap.get(prop_name.toLowerCase());
                if (valueMap.containsKey(value.toLowerCase())) {
                    List<String> transList = valueMap.get(value.toLowerCase());

                    for (String trans : transList) {
                        if (!StringUtils.isEmpty(trans)) {
                            result = trans;
                            break;
                        }
                    }
                }
            }
        }

        // 如果没有内容, 那么就去看看全局共通里面是否有相同的
        if (StringUtils.isEmpty(result)) {
            if (propCommonPublic.containsKey(value.toLowerCase())) {
                result = propCommonPublic.get(value.toLowerCase());
            }
        }

        return result;
    }

    /**
     * 获取所有的翻译列表
     *
     * @param channel_id    channel id
     * @param feed_cat_path feed category path
     * @return Map map
     */
    public Map<String, String> getTransList(String channel_id, String feed_cat_path) {
        FeedCustomPropData feedCustomPropData = doInit(channel_id);

        // 自定义属性
        List<FeedCustomPropWithValueBean> customPropList = feedCustomPropData.getCustomPropList();
        Map<String, Map<String, Map<String, List<String>>>> customPropMap = feedCustomPropData.getCustomPropMap(); // customPropList的简化版 (全小写) (<类目名称 <属性名称, <属性值, 属性值翻译列表>>>)
        // 翻译: 全店共通
        Map<String, String> propCommonPublic = feedCustomPropData.getPropCommonPublic();

        Map<String, String> result = new HashMap<>();

        // 全局共通
        result.putAll(propCommonPublic);

        // 从customPropList的简化版中获取数据
        Map<String, Map<String, List<String>>> propMap = null;
        if (customPropMap.containsKey(feed_cat_path.toLowerCase())) {
            // 指定类目
            propMap = customPropMap.get(feed_cat_path.toLowerCase());
        } else if (customPropMap.containsKey("0")) {
            // 类目级共通
            propMap = customPropMap.get("0");
        }

        if (propMap != null) {
            propMap.forEach((kProp, vProp) ->
                            vProp.forEach((k, v) -> {
                                String value = "";
                                for (String trans : v) {
                                    if (!StringUtils.isEmpty(trans)) {
                                        value = trans;
                                        break;
                                    }
                                }
                                result.put(k, value);

                            })
            );
        }

        return result;
    }

    // 根据类目路径查询属性信息
    public List<Map<String, Object>> getAllAttr(String channelId, String catPath) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("channelId", channelId);
        params.put("feedCatPath", catPath);
        return cmsMtFeedCustomPropDaoExt.selectAllAttr(params);
    }

    // 查询属性值
    public List<Map<String, Object>> getPropValue(String catPath, int tSts, String propName, String propValue, String chaId) {
        Map<String, Object> sqlPara = new HashMap<>();
        sqlPara.put("feedCatPath", catPath);
        sqlPara.put("propName", propName);
        sqlPara.put("propValue", propValue);
        sqlPara.put("tSts", tSts);
        sqlPara.put("channelId", chaId);
        return cmsMtFeedCustomPropDaoExt.selectPropValue(sqlPara);
    }

    // 根据类目路径查询自定义未翻译属性信息
    public List<Map<String, Object>> getOrigProp(String channelId, String catPath) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("channelId", channelId);
        params.put("feedCatPath", catPath);
        return cmsMtFeedCustomPropDaoExt.selectOrigProp(params);
    }

    // 根据类目路径查询自定义已翻译属性信息
    public List<Map<String, Object>> getTransProp(String channelId, String catPath) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("channelId", channelId);
        params.put("feedCatPath", catPath);
        return cmsMtFeedCustomPropDaoExt.selectTransProp(params);
    }

    // 取得全店铺共通配置属性
//	public String getSameAttr(String channelId) {
//		Map<String, Object> params = new HashMap<>(2);
//		params.put("channelId", channelId);
//		return cmsMtFeedCustomPropDaoExt.selectSameAttr(params);
//	}

    // 查询指定属性值是否存在
    public boolean isPropValueExist(int propId, String chnId, String origValue) {
        Map<String, Object> sqlPara = new HashMap<>();
        sqlPara.put("propId", propId);
        sqlPara.put("channelId", chnId);
        sqlPara.put("origValue", origValue);
        return cmsMtFeedCustomPropDaoExt.isPropValueExist(sqlPara);
    }

    // 查询指定属性值是否存在
    public boolean isPropValueExist(int valueId) {
        Map<String, Object> sqlPara = new HashMap<>();
        sqlPara.put("valueId", valueId);
        return cmsMtFeedCustomPropDaoExt.isPropValueExistById(sqlPara);
    }

    // 查询指定类目属性是否存在
    public boolean isAttrExist(Map<String, Object> params, String catPath, String chnId) {
        Map<String, Object> sqlPara = new HashMap<>();
        sqlPara.putAll(params);
        sqlPara.put("cat_path", catPath);
        sqlPara.put("channelId", chnId);
        return cmsMtFeedCustomPropDaoExt.isAttrExist(sqlPara);
    }

    // 添加属性值
    @VOTransactional
    public int addPropValue(int propId, String chnId, String origValue, String transValue, String userName) {
        Map<String, Object> sqlPara = new HashMap<>();
        sqlPara.put("propId", propId);
        sqlPara.put("channelId", chnId);
        sqlPara.put("origValue", origValue);
        sqlPara.put("transValue", transValue);
        sqlPara.put("userName", userName);
        return cmsMtFeedCustomPropDaoExt.insertPropValue(sqlPara);
    }

    // 修改属性值
    @VOTransactional
    public int savePropValue(int valueId, String transValue, String userName) {
        Map<String, Object> sqlPara = new HashMap<>();
        sqlPara.put("valueId", valueId);
        sqlPara.put("transValue", transValue);
        sqlPara.put("userName", userName);
        return cmsMtFeedCustomPropDaoExt.updatePropValue(sqlPara);
    }

    // 保存属性
    @VOTransactional
    public void saveAttr(List<Map<String, Object>> addList, List<Map<String, Object>> updList, String catPath, String channelId, String userName) {
        if (!addList.isEmpty()) {
            Map<String, Object> params = new HashMap<>(4);
            params.put("channelId", channelId);
            params.put("cat_path", catPath);
            params.put("userName", userName);
            params.put("list", addList);
            int tslt = cmsMtFeedCustomPropDaoExt.insertAttr(params);
            if (tslt != addList.size()) {
                $error("添加属性结果与期望不符：添加条数=" + addList.size() + " 实际更新件数=" + tslt);
            } else {
                $debug("添加属性成功 实际更新件数=" + tslt);
            }
        }
        if (!updList.isEmpty()) {
            for (Map<String, Object> item : updList) {
                item.put("userName", userName);
                int tslt = cmsMtFeedCustomPropDaoExt.updateAttr(item);
                if (tslt != 1) {
                    $error("修改属性结果失败，params=" + item.toString());
                }
            }
        }
    }

    public List<Map<String, Object>> getFeedCustomPropAttrs(String channelId, String catPath) {
        CmsBtCustomPropModel cmsBtCustomPropModel = cmsBtCustomPropService.getCustomPropByCatChannelExtend(channelId,channelId,catPath);
        List<Map<String, Object>> customProps = new ArrayList<>();

        customProps = cmsBtCustomPropModel.getEntitys().stream().map(entity -> {
            Map<String, Object> item = new HashMap();
            item.put("feed_prop_original", entity.getNameEn());
            item.put("feed_prop_translation", entity.getNameCn());
            return item;
        }).collect(Collectors.toList());
        return customProps;

//        Map<String, Object> params = new HashMap<>(2);
//        params.put("channelId", channelId);
//        params.put("feedCatPath", catPath);
//        return cmsMtFeedCustomPropDaoExt.selectAttrs(params);
    }

    public List<CmsMtFeedCustomPropModel> getFeedCustomPropWithCategory(String channelId, String feedCategory) {
        Map<String, Object> params = new HashMap<>();
        params.put("channelId", channelId);
        params.put("feedCatPath", feedCategory);
        return cmsMtFeedCustomPropDaoExt.selectWithCategory(params);
    }
}
