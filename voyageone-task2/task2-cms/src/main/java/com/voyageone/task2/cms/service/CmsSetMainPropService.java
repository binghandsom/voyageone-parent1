package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.dao.MainPropDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmsSetMainPropService extends BaseTaskService {

	@Autowired
    MainPropDao mainPropDao;

	@Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsSetMainPropJob";
    }

    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        // 这个代码是cms1.1的代码, 应该是删掉的, 为了以防万一头脑发昏删掉, 暂时先注释掉入口
        // 到了3月底没人放开注释就可以删掉了

//        // 允许运行的订单渠道取得
//        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);
//
//		// 线程
//        List<Runnable> threads = new ArrayList<>();
//
//        // 根据渠道运行
//        for (final String orderChannelID : orderChannelIdList) {
//
//            threads.add(new Runnable() {
//                @Override
//                public void run() {
//                    new setMainProp(orderChannelID).doRun();
//                }
//            });
//        }
//
//        runWithThreadPool(threads, taskControlList);
    }

//    /**
//     * 按渠道进行设置
//     */
//    public class setMainProp {
//        private OrderChannelBean channel;
//
//        public setMainProp(String orderChannelId) {
//            this.channel = ChannelConfigs.getChannel(orderChannelId);
//        }
//
//        public void doRun() {
//            logger.info(channel.getFull_name() + "产品导入主数据开始" );
//
//            String channel_id = this.channel.getOrder_channel_id();
//            String level_model = "2";
//
//            // 查找所有的商品
//            List<MainPropTodoListBean> mainPropTodoListBeanList;
//            mainPropTodoListBeanList = mainPropDao.getPlatformSubCatsWithoutShop(channel_id);
//
//            // 所有的类目匹配关系
//            Map<String, String> mainCategoryIdList = new HashMap<>();
//            // 已经完成属性匹配的主类目列表
//            List<String> categoryListSetted = new ArrayList<>();
//            // 如果检索到数据
//            if (mainPropTodoListBeanList.size() > 0) {
//                // 一次性获取所有的类目匹配关系（根据channel_id）
//                mainCategoryIdList = mainPropDao.selectMainCategoryIdList(channel_id);
//
//                // 查看已经完成属性匹配的主类目列表
//                categoryListSetted = mainPropDao.getMainCategoryListWhereAttrIsSetted(channel_id);
//
//            }
//
//            // 循环所有的商品
//            for (MainPropTodoListBean mainPropTodoListBean : mainPropTodoListBeanList) {
//                // 遍历每个商品
//                String category_id = mainPropTodoListBean.getCategory_id();
//                String model_id = mainPropTodoListBean.getModel_id();
//                String product_id = mainPropTodoListBean.getProduct_id();
//
//                // 获取主类目
//                String mainCategoryId;
//                // 有可能已经设置过主类目了，去获取一下
//                List<String> resultModel = mainPropDao.selectModelIdByModelName(channel_id, model_id);
//
//                if (resultModel != null && resultModel.size() > 0) {
//                    // 2015-10-14 tom modify 主数据类目的数据源从cms_bt_model_extend，改为cms_bt_cn_model_extend START
////                    mainCategoryId = resultModel.get(1);
//                    mainCategoryId = resultModel.get(2);
//                    // 2015-10-14 tom modify 主数据类目的数据源从cms_bt_model_extend，改为cms_bt_cn_model_extend END
//                } else {
//                    mainCategoryId = "";
//                }
//
//                // 如果没有设置过主类目的话，使用默认匹配的主类目
//                if (StringUtils.isEmpty(mainCategoryId)) {
//                    if (mainCategoryIdList.containsKey(category_id)) {
//                        mainCategoryId = mainCategoryIdList.get(category_id);
//
//                        // 将该商品的主类目，设为默认匹配好的值
//                        mainPropDao.doSetMainCategoryId(resultModel, channel_id, mainCategoryId, getTaskName());
//                    } else {
////                            // 如果没有设定过匹配用的主类目，那么认为出错了，并要求运营设置主类目
////                            blnError = true;
////                            break;
//
//                        // 如果没有设定过匹配用的主类目，那么认为无法继续了，跳过
//                        logger.warn("尚未指定主类目：category_id=" + category_id);
//                        logger.warn("尚未指定主类目：model_id=" + model_id);
//                        logger.warn("尚未指定主类目：product_id=" + product_id);
//                        continue;
//                    }
//                }
//
//                if (!categoryListSetted.contains(mainCategoryId)) {
//                    // 如果没有设定过匹配用的主类目，那么认为无法继续了，跳过
//                    logger.warn("主类目属性未设定完成：mainCategoryId=" + mainCategoryId);
//                    continue;
//                }
//
//                // 获取feed内容
//                List<MainPropTodoItemListBean> mainPropTodoItemListBeanList = mainPropDao.selectMainPropTodoItemListBean(channel_id, product_id);
//                Map<String, String> mapMainPropTodoItemListBeanList = new HashMap<>();
//                for (MainPropTodoItemListBean mainPropTodoItemListBean : mainPropTodoItemListBeanList) {
//                    mapMainPropTodoItemListBeanList.put(mainPropTodoItemListBean.getAttribute_name(), mainPropTodoItemListBean.getAttribute_value());
//                }
//
//                // 根据product id，获取sku列表
//                List<String> skuList = mainPropDao.getSkuListFromProductId(channel_id, product_id);
//
//                // 如果sku的数据不存在的话，那就没必要做这些处理了
//                if (skuList != null && skuList.size() > 0) {
//
//                    // 遍历SKU级别的数据
//                    // 获取ims_bt_sku_prop_mapping的数据
//                    List<FeedMappingSkuBean> feedMappingBeanSkuList = mainPropDao.selectFeedMappingSkuList(channel_id, mainCategoryId);
//                    // 获取ims_bt_prop_value_sku_template的数据
//                    List<PropValueSkuTemplateBean> propValueSkuTemplateBeanList = mainPropDao.selectPropValueSkuTemplateList();
//                    Map<String, PropValueSkuTemplateBean> propValueSkuTemplateBeanMap = new HashMap<>();
//                    for (PropValueSkuTemplateBean propValueSkuTemplateBean : propValueSkuTemplateBeanList) {
//                        propValueSkuTemplateBeanMap.put(propValueSkuTemplateBean.getProp_name(), propValueSkuTemplateBean);
//                    }
//
//                    // 设置sku的属性
//                    List<ImsPropValueSkuBean> mainPropSkuList = new ArrayList<>();
//
//                    // 遍历FeedMapping
//                    for (FeedMappingSkuBean feedMappingSkuBean : feedMappingBeanSkuList) {
//
//                        // 如果有条件的话，看看是否符合条件
//                        if (!StringUtils.isEmpty(feedMappingSkuBean.getConditions())) {
//                            // 条件
//                            Condition condition = JsonUtil.jsonToBean(feedMappingSkuBean.getConditions(), Condition.class);
//
//                            // 判断条件是否满足
//                            String strFeedValue = mapMainPropTodoItemListBeanList.get(condition.getProperty());
//                            switch (condition.getOperation()) {
//                                case IS_NULL:
//                                    if (!StringUtils.isEmpty(strFeedValue)) {
//                                        // 条件不满足，跳过
//                                        continue;
//                                    }
//                                    break;
//                                case IS_NOT_NULL:
//                                    if (StringUtils.isEmpty(strFeedValue)) {
//                                        // 条件不满足，跳过
//                                        continue;
//                                    }
//                                    break;
//                                case EQUALS:
//                                    // 禁止为空
//                                    if (strFeedValue == null) {
//                                        continue;
//                                    }
//                                    if (condition.getValue() == null) {
//                                        continue;
//                                    }
//
//                                    if (!strFeedValue.equals(condition.getValue())) {
//                                        // 条件不满足，跳过
//                                        continue;
//                                    }
//                                    break;
//                                case NOT_EQUALS:
//                                    // 禁止为空
//                                    if (strFeedValue == null) {
//                                        continue;
//                                    }
//                                    if (condition.getValue() == null) {
//                                        continue;
//                                    }
//
//                                    if (strFeedValue.equals(condition.getValue())) {
//                                        // 条件不满足，跳过
//                                        continue;
//                                    }
//                                    break;
//                            }
//
//                        }
//
//                        // 准备设定值
//                        String value = "";
//                        FeedPropMappingType feedPropMappingType;
//                        feedPropMappingType = FeedPropMappingType.valueOf(Integer.parseInt(feedMappingSkuBean.getType()));
//                        if (feedPropMappingType == FeedPropMappingType.FEED) {
//                            // 获取属性的值
//                            value = mapMainPropTodoItemListBeanList.get(feedMappingSkuBean.getValue());
//
//                            // 获取template，看看是否要进行运算
//                            PropValueSkuTemplateBean propValueSkuTemplateBean;
//                            if (propValueSkuTemplateBeanMap.containsKey(feedMappingSkuBean.getProp_name())) {
//                                propValueSkuTemplateBean = propValueSkuTemplateBeanMap.get(feedMappingSkuBean.getProp_name());
//
//                                value = doEditSkuTemplate(
//                                        value,
//                                        propValueSkuTemplateBean.getEdit(),
//                                        propValueSkuTemplateBean.getPrefix(),
//                                        propValueSkuTemplateBean.getSuffix()
//                                        );
//                            }
//
//                        } else if (feedPropMappingType == FeedPropMappingType.VALUE) {
//                            // 直接设定值
//                            value = feedMappingSkuBean.getValue();
//                        }
//
//                        // 循环sku列表
//                        for (String sku : skuList) {
//                            List<ImsPropValueSkuBean> imsPropValueSkuBeanList = mainPropDao.selectPropValueSku(
//                                    channel_id,
//                                    sku,
//                                    feedMappingSkuBean.getProp_name()
//                            );
//
//                            // 查看当前属性是否已经设定过了
//                            if (imsPropValueSkuBeanList != null && imsPropValueSkuBeanList.size() > 0) {
//                                // 如果这个属性已经设置过了，那么根据先来后到的优先度，之后的就不用设置了
//                                continue;
//                            }
//
//                            // 添加自己
//                            ImsPropValueSkuBean imsPropValueSkuBean = new ImsPropValueSkuBean();
//
//                            imsPropValueSkuBean.setSku(sku);
//                            imsPropValueSkuBean.setProp_name(feedMappingSkuBean.getProp_name());
//                            imsPropValueSkuBean.setProp_value(value);
//                            imsPropValueSkuBean.setOrder_channel_id(channel_id);
//
//                            mainPropSkuList.add(imsPropValueSkuBean);
//
//                        }
//
//                    }
//
//                    if (mainPropSkuList.size() > 0) {
//                        // 插入数据库
//                        mainPropDao.doInsertSkuValue(mainPropSkuList, getTaskName());
//                        logger.info("主数据属性值设定（SKU）：model_id=" + model_id + "; product_id=" + product_id + "; main_category_id=" + mainCategoryId);
//                    }
//                }
//
//                // 去value表检索一下，看看这个model是否已经设置过了
//                boolean blnMainValueExist = mainPropDao.selectMainValue(channel_id, level_model, model_id);
//                // 已经存在了，那就是已经设置过了
//                if (blnMainValueExist) {
//                    // 更新处理flag
//                    mainPropDao.doFinishProduct(channel_id, product_id);
//
//                    // 跳过这条数据吧
//                    continue;
//                }
//
//                // 获取该类目（主数据类目）的所有属性， 放入map
//                Map<String, ImsPropBean> imsPropMap = mainPropDao.selectImsPropByCategoryId(mainCategoryId);
//
//                // 获取mapping的内容，放入需要操作的对象
//                List<FeedMappingBean> feedMappingBeanList = mainPropDao.selectFeedMappingList(channel_id, mainCategoryId);
//
//                // 获取mapping default的内容，放入需要操作的对象
//                List<FeedMappingDefaultBean> feedMappingDefaultBeanList = mainPropDao.selectFeedMappingDefaultList(channel_id);
//                Map<String, List<String>> mapFeedMappingDefaultBeanList = new HashMap<>();
//                for (FeedMappingDefaultBean feedMappingDefaultBean : feedMappingDefaultBeanList) {
//                    List<String> lstProp = new ArrayList<>();
//                    lstProp.add(feedMappingDefaultBean.getProp_type());
//                    lstProp.add(feedMappingDefaultBean.getProp_value());
//
//                    mapFeedMappingDefaultBeanList.put(feedMappingDefaultBean.getProp_name(), lstProp);
//                }
//
//                // 设置主数据的属性
//                Map<String, ImsPropValueBean> mainPropList = new HashMap<>();
//
//                // 遍历FeedMapping
//                for (FeedMappingBean feedMappingBean : feedMappingBeanList) {
//                    if (mainPropList.containsKey(feedMappingBean.getProp_id())) {
//                        // 如果这个属性已经设置过了，那么根据先来后到的优先度，之后的就不用设置了
//                        continue;
//                    }
//
//                    // 如果有条件的话，看看是否符合条件
//                    if (!StringUtils.isEmpty(feedMappingBean.getConditions())) {
//                        // 条件
//                        Condition condition = JsonUtil.jsonToBean(feedMappingBean.getConditions(), Condition.class);
//
//                        // 判断条件是否满足
//                        String strFeedValue = mapMainPropTodoItemListBeanList.get(condition.getProperty());
//                        switch (condition.getOperation()) {
//                            case IS_NULL:
//                                if (!StringUtils.isEmpty(strFeedValue)) {
//                                    // 条件不满足，跳过
//                                    continue;
//                                }
//                                break;
//                            case IS_NOT_NULL:
//                                if (StringUtils.isEmpty(strFeedValue)) {
//                                    // 条件不满足，跳过
//                                    continue;
//                                }
//                                break;
//                            case EQUALS:
//                                // 禁止为空
//                                if (strFeedValue == null) {
//                                    continue;
//                                }
//                                if (condition.getValue() == null) {
//                                    continue;
//                                }
//
//                                if (!strFeedValue.equals(condition.getValue())) {
//                                    // 条件不满足，跳过
//                                    continue;
//                                }
//                                break;
//                            case NOT_EQUALS:
//                                // 禁止为空
//                                if (strFeedValue == null) {
//                                    continue;
//                                }
//                                if (condition.getValue() == null) {
//                                    continue;
//                                }
//
//                                if (strFeedValue.equals(condition.getValue())) {
//                                    // 条件不满足，跳过
//                                    continue;
//                                }
//                                break;
//                        }
//
//                    }
//
//                    // 准备设定值
//                    String value = "";
//                    FeedPropMappingType feedPropMappingType;
//                    feedPropMappingType = FeedPropMappingType.valueOf(Integer.parseInt(feedMappingBean.getType()));
//                    if (feedPropMappingType == FeedPropMappingType.FEED) {
//                        // 获取属性的值
//                        value = mapMainPropTodoItemListBeanList.get(feedMappingBean.getValue());
//                        value = "{\"ruleWordList\":[{\"type\":\"TEXT\", \"value\":\"" + value.replace("\"", "\\\"") + "\"}]}";
//                    } else if (feedPropMappingType == FeedPropMappingType.OPTIONS) {
//                        value = feedMappingBean.getValue();
//                        value = "{\"ruleWordList\":[{\"type\":\"TEXT\", \"value\":\"" + value.replace("\"", "\\\"") + "\"}]}";
//                    } else if (feedPropMappingType == FeedPropMappingType.CMS) {
//                        value = feedMappingBean.getValue();
//                        value = "{\"ruleWordList\":[{\"type\":\"CMS\", \"value\":[\"CmsModelEnum\",\"" + value + "\"]}]}";
//                    } else if (feedPropMappingType == FeedPropMappingType.VALUE) {
//                        if (feedMappingBean.getValue().startsWith("{")
//                                || feedMappingBean.getValue().startsWith("[{")
//                                ) {
//                            value = feedMappingBean.getValue();
//                        } else {
//                            value = feedMappingBean.getValue();
//                            value = "{\"ruleWordList\":[{\"type\":\"TEXT\", \"value\":\"" + value.replace("\"", "\\\"") + "\"}]}";
//                        }
//
//                    }
//
//                    // 设定值
//                    {
//                        // 添加一个父
//                        String strParentUuidMulti = "";
//                        String strParentUuid = "";
//
//                        // 看看自己的类型
//                        int intType = imsPropMap.get(feedMappingBean.getProp_id()).getPropType();
//                        if (intType == MasterPropTypeEnum.MULTICHECK.getValue()
//                                || intType == MasterPropTypeEnum.MULTICOMPLEX.getValue()
//                                ) {
//                            // 需要待会儿在最后插入数据的时候，添加一个父
//                            strParentUuidMulti = getUUID();
//                        }
//
//                        // 看看自己是否有父
//                        int intParentId = imsPropMap.get(feedMappingBean.getProp_id()).getParentPropId();
//                        if (intParentId != 0) {
//                            // 有父，添加父（包括所有父）
//                            List<ImsPropValueBean> imsParentPropValueBeanList =  doAddParentProp(channel_id, level_model, model_id, String.valueOf(intParentId), imsPropMap, mainPropList);
//
//                            for (ImsPropValueBean bean : imsParentPropValueBeanList) {
//                                mainPropList.put(bean.getPropId(), bean);
//                            }
//
//                            strParentUuid = imsParentPropValueBeanList.get(imsParentPropValueBeanList.size() - 1).getUuid();
//
//                        }
//
//                        // 添加自己
//                        ImsPropValueBean imsPropValueBean = new ImsPropValueBean();
//                        // 算一个uuid出来
//                        imsPropValueBean.setUuid(getUUID());
//                        // channel id
//                        imsPropValueBean.setChannelId(channel_id);
//                        // level
//                        imsPropValueBean.setLevel(level_model);
//                        // level value
//                        imsPropValueBean.setLevelValue(model_id);
//                        // prop id
//                        imsPropValueBean.setPropId(feedMappingBean.getProp_id());
//                        // prop value
//                        imsPropValueBean.setPropValue(value.replace("'", "''"));
//                        // parent
//                        imsPropValueBean.setParent(strParentUuid);
//                        // parent multi
//                        imsPropValueBean.setParentMulti(strParentUuidMulti);
//
//                        mainPropList.put(feedMappingBean.getProp_id(), imsPropValueBean);
//
//                    }
//                }
//
//                // 设置默认值
//                // 遍历主类目的所有属性
//                Iterator iter = imsPropMap.entrySet().iterator();
//                while (iter.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iter.next();
//
//                    String key = entry.getKey().toString();
//                    ImsPropBean val = (ImsPropBean) entry.getValue();
//
//                    // 遍历FeedMappingDefault
//                    if (mapFeedMappingDefaultBeanList.containsKey(val.getPropName())) {
//                        List<String> feedMappingDefaultBean = mapFeedMappingDefaultBeanList.get(val.getPropName());
//                        if (String.valueOf(val.getPropType()).equals(feedMappingDefaultBean.get(0))) {
//                            // 这个属性还没有被设置过的话，可以设置一下
//                            if (!mainPropList.containsKey(key)) {
//
//                                // 设定值
//                                {
//                                    // 添加一个父
//                                    String strParentUuidMulti = "";
//                                    String strParentUuid = "";
//
//                                    // 看看自己的类型
//                                    int intType = imsPropMap.get(key).getPropType();
//                                    if (intType == MasterPropTypeEnum.MULTICHECK.getValue()
//                                            || intType == MasterPropTypeEnum.MULTICOMPLEX.getValue()
//                                            ) {
//                                        // 需要待会儿在最后插入数据的时候，添加一个父
//                                        strParentUuidMulti = getUUID();
//                                    }
//
//                                    // 看看自己是否有父
//                                    int intParentId = imsPropMap.get(key).getParentPropId();
//                                    if (intParentId != 0) {
//                                        // 有父，添加父（包括所有父）
//                                        List<ImsPropValueBean> imsParentPropValueBeanList =  doAddParentProp(channel_id, level_model, model_id, String.valueOf(intParentId), imsPropMap, mainPropList);
//
//                                        for (ImsPropValueBean bean : imsParentPropValueBeanList) {
//                                            mainPropList.put(bean.getPropId(), bean);
//                                        }
//
//                                        strParentUuid = imsParentPropValueBeanList.get(imsParentPropValueBeanList.size() - 1).getUuid();
//
//                                    }
//
//                                    // 添加自己
//                                    ImsPropValueBean imsPropValueBean = new ImsPropValueBean();
//                                    // 算一个uuid出来
//                                    imsPropValueBean.setUuid(getUUID());
//                                    // channel id
//                                    imsPropValueBean.setChannelId(channel_id);
//                                    // level
//                                    imsPropValueBean.setLevel(level_model);
//                                    // level value
//                                    imsPropValueBean.setLevelValue(model_id);
//                                    // prop id
//                                    imsPropValueBean.setPropId(key);
//                                    // prop value
//                                    imsPropValueBean.setPropValue(feedMappingDefaultBean.get(1).replace("'", "''"));
//                                    // parent
//                                    imsPropValueBean.setParent(strParentUuid);
//                                    // parent multi
//                                    imsPropValueBean.setParentMulti(strParentUuidMulti);
//
//                                    mainPropList.put(key, imsPropValueBean);
//
//                                }
//
//
//                            }
//                        }
//                    }
//
//                }
//
//                // 插入到数据库里（主数据的值表）
//                List<ImsPropValueBean> imsPropValueBeanList = new ArrayList<>();
//                Set<String> set_MainPropList = mainPropList.keySet();
//                for (String propId : set_MainPropList) {
//
//                    // 操作对象
//                    ImsPropValueBean imsPropValueBean = mainPropList.get(propId);
//
//                    // 操作对象的类型
//                    int intType = imsPropMap.get(imsPropValueBean.getPropId()).getPropType();
//
//                    // 判断是否是多选（需要特殊处理）
//                    String uuidNew;
//                    if (intType == MasterPropTypeEnum.MULTICHECK.getValue()
//                            || intType == MasterPropTypeEnum.MULTICOMPLEX.getValue()
//                            ) {
//                        ImsPropValueBean tempBean = new ImsPropValueBean();
//
//                        // uuid
//                        uuidNew = getUUID();
//                        tempBean.setUuid(uuidNew);// imsPropValueBean.getParent()
//                        // channel id
//                        tempBean.setChannelId(channel_id);
//                        // level
//                        tempBean.setLevel(level_model);
//                        // level value
//                        tempBean.setLevelValue(model_id);
//                        // prop id
//                        tempBean.setPropId(imsPropValueBean.getPropId());
//                        // prop value
//                        tempBean.setPropValue("");
//                        // parent
//                        // 看看自己是否有父
//                        int intParentId = imsPropMap.get(imsPropValueBean.getPropId()).getParentPropId();
//                        if (intParentId != 0) {
//                            // 如果有父
//                            String strParentUuid = mainPropList.get(String.valueOf(intParentId)).getUuid();
//                            tempBean.setParent(strParentUuid);
//                        } else {
//                            // 没有
//                            tempBean.setParent("");
//                        }
//
//                        // 直接插入
//                        imsPropValueBeanList.add(tempBean);
//
//                        imsPropValueBean.setParent(uuidNew);
//
//                    }
//
//                    imsPropValueBeanList.add(imsPropValueBean);
//
//                }
//                if (imsPropValueBeanList.size() > 0) {
//                    mainPropDao.doInsertMainValue(imsPropValueBeanList, getTaskName());
//                    logger.info("主数据属性值设定（CODE）：model_id=" + model_id + "; product_id=" + product_id + "; main_category_id=" + mainCategoryId);
//                }
//
//                // 更新处理flag
//                mainPropDao.doFinishProduct(channel_id, product_id);
//
//            }
//
//            logger.info(channel.getFull_name() + "产品导入主数据结束");
//        }
//	}
//
//    private String doEditSkuTemplate(String inputValue, String edit, String prefix, String suffix) {
//        String value = inputValue;
//
//        // 根据edit进行变换
//        if (!StringUtils.isEmpty(edit)) {
//            if ("in2cm".equals(edit)) {
//                // 奇怪的数据转换
//                // 有时候别人提供的数字中会有类似于这样的数据：
//                // 33 3/4 意思是 33又四分之三 -> 33.75
//                // 33 1/2 意思是 33又二分之一 -> 33.5
//                // 33 1/4 意思是 33又四分之一 -> 33.25
//                // 33 5/8 意思是 33又八分之五 -> 33.625
//                // 直接这边代码处理掉避免人工干预
//                if (value.contains(" ")) {
//                    value = value.replaceAll(" +", " ");
//                    String[] strSplit = value.split(" ");
//
//                    // 修改：直接用除法来过滤掉所有这类问题 tom 20151014 START
////                    switch (strSplit[1]) {
////                        case "3/4":
////                            value = String.valueOf(Float.valueOf(strSplit[0]) + 0.75);
////                            break;
////                        case "1/2":
////                            value = String.valueOf(Float.valueOf(strSplit[0]) + 0.5);
////                            break;
////                        case "1/4":
////                            value = String.valueOf(Float.valueOf(strSplit[0]) + 0.25);
////                            break;
////                        case "5/8":
////                            value = String.valueOf(Float.valueOf(strSplit[0]) + 0.625);
////                            break;
////                    }
//                    String[] strSplitSub = strSplit[1].split("/");
//                    value = String.valueOf(
//                            Float.valueOf(strSplit[0]) +
//                                    (Float.valueOf(strSplitSub[0]) / Float.valueOf(strSplitSub[1]))
//                    );
//                    // 修改：直接用除法来过滤掉所有这类问题 tom 20151014 END
//
//                }
//
//                // 英寸转厘米
//                value = String.valueOf(Float.valueOf(value) * 2.54);
//
//                DecimalFormat df = new DecimalFormat("0.00");
//                value = df.format(Float.valueOf(value));
//
//            }
//        }
//
//        // 设置前缀
//        if (!StringUtils.isEmpty(prefix)) {
//            value = prefix + value;
//        }
//
//        // 设置后缀
//        if (!StringUtils.isEmpty(suffix)) {
//            value = value + suffix;
//        }
//
//        return value;
//
//    }
//
//    private String getUUID() {
//        // 创建 GUID 对象
//        UUID uuid = UUID.randomUUID();
//        // 得到对象产生的ID
//        String value = uuid.toString();
//        // 替换 -
//        value = value.replaceAll("-", "");
//
//        return value;
//    }
//
//    private List<ImsPropValueBean> doAddParentProp(String channel_id, String level, String modelId, String parentPropId, Map<String, ImsPropBean> imsPropMap, Map<String, ImsPropValueBean> mainPropList) {
//
//        List<ImsPropValueBean> result = new ArrayList<>();
//
//        String strParentUuid = "";
//
//        // 看看是否有父的父
//        int intParentId = imsPropMap.get(parentPropId).getParentPropId();
//        if (intParentId != 0) {
//            // 有父，添加父（包括所有父）
//            result.addAll(doAddParentProp(channel_id, level, modelId, String.valueOf(intParentId), imsPropMap, mainPropList));
//        }
//
//        if (result.size() > 0) {
//            strParentUuid = result.get(result.size() - 1).getUuid();
//        }
//
//        // 看看自己是否已经被添加过了
//        String uuid;
//        if (mainPropList.containsKey(parentPropId)) {
//            uuid  = mainPropList.get(parentPropId).getUuid();
//        } else {
//            uuid = getUUID();
//        }
//
//        // 看看自己的类型
//        String strParentUuidMulti = "";
//        int intType = imsPropMap.get(parentPropId).getPropType();
//        if (intType == MasterPropTypeEnum.MULTICHECK.getValue()
//                || intType == MasterPropTypeEnum.MULTICOMPLEX.getValue()
//                ) {
//            // 需要待会儿在最后插入数据的时候，添加一个父
//            strParentUuidMulti = getUUID();
//        }
//
//        // 添加自己
//        ImsPropValueBean imsPropValueBean = new ImsPropValueBean();
//        // 算一个uuid出来
//        imsPropValueBean.setUuid(uuid);
//        // channel id
//        imsPropValueBean.setChannelId(channel_id);
//        // level
//        imsPropValueBean.setLevel(level);
//        // level value
//        imsPropValueBean.setLevelValue(modelId);
//        // prop id
//        imsPropValueBean.setPropId(parentPropId);
//        // prop value
//        imsPropValueBean.setPropValue("");
//        // parent
//        imsPropValueBean.setParent(strParentUuid);
//        // parent multi
//        imsPropValueBean.setParentMulti(strParentUuidMulti);
//
//        result.add(imsPropValueBean);
//
//        return result;
//    }


}
