package com.voyageone.batch.cms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.bean.*;
import com.voyageone.batch.cms.dao.MainPropDao;
import com.voyageone.batch.cms.dao.SuperFeedDao;
import com.voyageone.batch.cms.emum.FeedPropMappingType;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.cms.feed.Condition;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.enums.MasterPropTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CmsSetMainPropService extends BaseTaskService {

    @Autowired
    SuperFeedDao superfeeddao;

    @Autowired
    private TransactionRunner transactionRunner;

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

        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

		// 线程
        List<Runnable> threads = new ArrayList<>();

        // 根据渠道运行
        for (final String orderChannelID : orderChannelIdList) {

            threads.add(new Runnable() {
                @Override
                public void run() {
                    new setMainProp(orderChannelID).doRun();
                }
            });
        }

        runWithThreadPool(threads, taskControlList);
    }

    /**
     * 按渠道进行设置
     */
    public class setMainProp {
        private OrderChannelBean channel; 

        public setMainProp(String orderChannelId) {
            this.channel = ChannelConfigs.getChannel(orderChannelId); 
        }

        public void doRun() {
            logger.info(channel.getFull_name() + "产品导入开始" );

            boolean blnError = false;

            String channel_id = this.channel.getOrder_channel_id();
            String level_model = "2";

			while (true) {
				// 查找所有的商品
				List<MainPropTodoListBean> mainPropTodoListBeanList;
				mainPropTodoListBeanList = mainPropDao.getPlatformSubCatsWithoutShop(channel_id);

				// 如果没有检索到数据，那么就跳出循环
				if (mainPropTodoListBeanList.size() == 0) {
					break;
				}

				for (MainPropTodoListBean mainPropTodoListBean : mainPropTodoListBeanList) {
					// 遍历每个商品
					String category_id = mainPropTodoListBean.getCategory_id();
					String model_id = mainPropTodoListBean.getModel_id();
					String product_id = mainPropTodoListBean.getProduct_id();

					// 获取主类目
					String mainCategoryId = "36129"; // TODO:调用刘耀的函数，取得返回值。

					// 如果没有设定过主类目，那么认为出错了，并要求运营设置主类目
					if (StringUtils.isEmpty(mainCategoryId)) {
                        blnError = true;
						break;
					}

					// 去value表检索一下，看看这个model是否已经设置过了
                    // TODO:这个功能以后可能会被废除
                    // TODO:如果已经有了，那么“酌情”先删除再添加
                    boolean blnMainValueExist = mainPropDao.selectMainValue(channel_id, level_model, model_id);
                    if (blnMainValueExist) {
                        // 已经存在了，认为错误数据存在
                        blnError = true;
                        break;
                    }

					// 获取该类目（主数据类目）的所有属性， 放入map
                    Map<String, ImsPropBean> imsPropMap = mainPropDao.selectImsPropByCategoryId(mainCategoryId);

					// 获取feed内容
                    List<MainPropTodoItemListBean> mainPropTodoItemListBeanList = mainPropDao.selectMainPropTodoItemListBean(channel_id, product_id);
                    Map<String, String> mapMainPropTodoItemListBeanList = new HashMap<>();
                    for (MainPropTodoItemListBean mainPropTodoItemListBean : mainPropTodoItemListBeanList) {
                        mapMainPropTodoItemListBeanList.put(mainPropTodoItemListBean.getAttribute_name(), mainPropTodoItemListBean.getAttribute_value());
                    }

                    // 获取mapping的内容，放入需要操作的对象
                    List<FeedMappingBean> feedMappingBeanList = mainPropDao.selectFeedMappingList(channel_id, mainCategoryId);
                    Map<String, List<String>> mapFeedMappingBeanList = new HashMap<>();
                    for (FeedMappingBean feedMappingBean : feedMappingBeanList) {
                        List<String> lstProp = new ArrayList<>();
                        lstProp.add(feedMappingBean.getConditions());
                        lstProp.add(feedMappingBean.getType());
                        lstProp.add(feedMappingBean.getValue());

                        mapFeedMappingBeanList.put(feedMappingBean.getProp_id(), lstProp);
                    }

                    // 获取mapping default的内容，放入需要操作的对象
                    List<FeedMappingDefaultBean> feedMappingDefaultBeanList = mainPropDao.selectFeedMappingDefaultList(channel_id);
                    Map<String, List<String>> mapFeedMappingDefaultBeanList = new HashMap<>();
                    for (FeedMappingDefaultBean feedMappingDefaultBean : feedMappingDefaultBeanList) {
                        List<String> lstProp = new ArrayList<>();
                        lstProp.add(feedMappingDefaultBean.getProp_type());
                        lstProp.add(feedMappingDefaultBean.getProp_value());

                        mapFeedMappingDefaultBeanList.put(feedMappingDefaultBean.getProp_name(), lstProp);
                    }

					// 设置主数据的属性
                    Map<String, ImsPropValueBean> mainPropList = new HashMap<>();

					// 遍历FeedMapping
                    for (FeedMappingBean feedMappingBean : feedMappingBeanList) {
                        if (mainPropList.containsKey(feedMappingBean.getProp_id())) {
                            // 如果这个属性已经设置过了，那么根据先来后到的优先度，之后的就不用设置了
                            continue;
                        }

                        // 如果有条件的话，看看是否符合条件
                        if (!StringUtils.isEmpty(feedMappingBean.getConditions())) {
                            // 条件
                            Condition condition = JsonUtil.jsonToBean(feedMappingBean.getConditions(), Condition.class);

                            // 判断条件是否满足
                            String strFeedValue = mapMainPropTodoItemListBeanList.get(condition.getProperty());
                            switch (condition.getOperation()) {
                                case IS_NULL:
                                    if (!StringUtils.isEmpty(strFeedValue)) {
                                        // 条件不满足，跳过
                                        continue;
                                    }
                                    break;
                                case IS_NOT_NULL:
                                    if (StringUtils.isEmpty(strFeedValue)) {
                                        // 条件不满足，跳过
                                        continue;
                                    }
                                    break;
                                case EQUALS:
                                    // 禁止为空
                                    if (strFeedValue == null) {
                                        continue;
                                    }
                                    if (condition.getValue() == null) {
                                        continue;
                                    }

                                    if (!strFeedValue.equals(condition.getValue())) {
                                        // 条件不满足，跳过
                                        continue;
                                    }
                                    break;
                                case NOT_EQUALS:
                                    // 禁止为空
                                    if (strFeedValue == null) {
                                        continue;
                                    }
                                    if (condition.getValue() == null) {
                                        continue;
                                    }

                                    if (strFeedValue.equals(condition.getValue())) {
                                        // 条件不满足，跳过
                                        continue;
                                    }
                                    break;
                            }

                        }

                        // 准备设定值
                        String value = "";
                        FeedPropMappingType feedPropMappingType;
                        feedPropMappingType = FeedPropMappingType.valueOf(feedMappingBean.getType());
                        if (feedPropMappingType == FeedPropMappingType.FEED) {
                            // 获取属性的值
                            value = mapMainPropTodoItemListBeanList.get(feedMappingBean.getValue());
                            value = "{\"ruleWordList\":[{\"type\":\"TEXT\", \"value\":\"" + value + "\"}]}";
                        } else if (feedPropMappingType == FeedPropMappingType.OPTIONS) {
                            value = feedMappingBean.getValue();
                            value = "{\"ruleWordList\":[{\"type\":\"TEXT\", \"value\":\"" + value + "\"}]}";
                        } else if (feedPropMappingType == FeedPropMappingType.CMS) {
                            value = feedMappingBean.getValue();
                            value = "{\"ruleWordList\":[{\"type\":\"CMS\", \"value\":[\"CmsModelEnum\",\"" + value + "\"]}]}";
                        } else if (feedPropMappingType == FeedPropMappingType.VALUE) {
                            if (feedMappingBean.getValue().startsWith("{")
                                    || feedMappingBean.getValue().startsWith("[{")
                                    ) {
                                value = feedMappingBean.getValue();
                            } else {
                                value = feedMappingBean.getValue();
                                value = "{\"ruleWordList\":[{\"type\":\"TEXT\", \"value\":\"" + value + "\"}]}";
                            }

                        }

                        // 设定值
                        {
                            // 添加一个父
                            String strParentUuidMulti = "";
                            String strParentUuid = "";

                            // 看看自己的类型
                            int intType = imsPropMap.get(feedMappingBean.getProp_id()).getPropType();
                            if (intType == MasterPropTypeEnum.MULTICHECK.getValue()
                                    || intType == MasterPropTypeEnum.MULTICOMPLEX.getValue()
                                    ) {
                                // 需要待会儿在最后插入数据的时候，添加一个父
                                strParentUuidMulti = getUUID();
                            }

                            // 看看自己是否有父
                            int intParentId = imsPropMap.get(feedMappingBean.getProp_id()).getParentPropId();
                            if (intParentId != 0) {
                                // 有父，添加父（包括所有父）
                                List<ImsPropValueBean> imsParentPropValueBeanList =  doAddParentProp(channel_id, level_model, model_id, String.valueOf(intParentId), imsPropMap, mainPropList);

                                for (ImsPropValueBean bean : imsParentPropValueBeanList) {
                                    mainPropList.put(bean.getPropId(), bean);
                                }

                                strParentUuid = imsParentPropValueBeanList.get(imsParentPropValueBeanList.size() - 1).getUuid();

                            }

                            // 添加自己
                            ImsPropValueBean imsPropValueBean = new ImsPropValueBean();
                            // 算一个uuid出来
                            imsPropValueBean.setUuid(getUUID());
                            // channel id
                            imsPropValueBean.setChannelId(channel_id);
                            // level
                            imsPropValueBean.setLevel(level_model);
                            // level value
                            imsPropValueBean.setLevelValue(model_id);
                            // prop id
                            imsPropValueBean.setPropId(feedMappingBean.getProp_id());
                            // prop value
                            imsPropValueBean.setPropValue(value.replace("'", "''"));
                            // parent
                            imsPropValueBean.setParent(strParentUuid);
                            // parent multi
                            imsPropValueBean.setParentMulti(strParentUuidMulti);

                            mainPropList.put(feedMappingBean.getProp_id(), imsPropValueBean);

                        }
                    }

                    // 遍历FeedMappingDefault

					// 插入到数据库里（主数据的值表）
                    List<ImsPropValueBean> imsPropValueBeanList = new ArrayList<>();
                    Set<String> set_MainPropList = mainPropList.keySet();
                    for (String propId : set_MainPropList) {

                        // 操作对象
                        ImsPropValueBean imsPropValueBean = mainPropList.get(propId);

                        // 操作对象的类型
                        int intType = imsPropMap.get(imsPropValueBean.getPropId()).getPropType();

                        // 判断是否是多选（需要特殊处理）
                        String uuidNew;
                        if (intType == MasterPropTypeEnum.MULTICHECK.getValue()
                                || intType == MasterPropTypeEnum.MULTICOMPLEX.getValue()
                                ) {
                            ImsPropValueBean tempBean = new ImsPropValueBean();

                            // uuid
                            uuidNew = getUUID();
                            tempBean.setUuid(uuidNew);// imsPropValueBean.getParent()
                            // channel id
                            tempBean.setChannelId(channel_id);
                            // level
                            tempBean.setLevel(level_model);
                            // level value
                            tempBean.setLevelValue(model_id);
                            // prop id
                            tempBean.setPropId(imsPropValueBean.getPropId());
                            // prop value
                            tempBean.setPropValue("");
                            // parent
                            // 看看自己是否有父
                            int intParentId = imsPropMap.get(imsPropValueBean.getPropId()).getParentPropId();
                            if (intParentId != 0) {
                                // 如果有父
                                String strParentUuid = mainPropList.get(String.valueOf(intParentId)).getUuid();
                                tempBean.setParent(strParentUuid);
                            } else {
                                // 没有
                                tempBean.setParent("");
                            }

                            // 直接插入
                            imsPropValueBeanList.add(tempBean);

                            imsPropValueBean.setParent(uuidNew);

                        }

                        imsPropValueBeanList.add(imsPropValueBean);

                    }
                    mainPropDao.doInsertMainValue(imsPropValueBeanList, getTaskName());

                    // 更新处理flag
                    mainPropDao.doFinishProduct(channel_id, product_id);

				}

                // 认为出错了，就不用继续做下去了
                if (blnError) {
                    break;
                }
			}

            logger.info(channel.getFull_name() + "产品导入结束");
        }
	}


    private String getUUID() {
        // 创建 GUID 对象
        UUID uuid = UUID.randomUUID();
        // 得到对象产生的ID
        String value = uuid.toString();
        // 替换 -
        value = value.replaceAll("-", "");

        return value;
    }

    private List<ImsPropValueBean> doAddParentProp(String channel_id, String level, String modelId, String parentPropId, Map<String, ImsPropBean> imsPropMap, Map<String, ImsPropValueBean> mainPropList) {

        List<ImsPropValueBean> result = new ArrayList<>();

        String strParentUuid = "";

        // 看看是否有父的父
        int intParentId = imsPropMap.get(parentPropId).getParentPropId();
        if (intParentId != 0) {
            // 有父，添加父（包括所有父）
            result.addAll(doAddParentProp(channel_id, level, modelId, String.valueOf(intParentId), imsPropMap, mainPropList));
        }

        if (result.size() > 0) {
            strParentUuid = result.get(result.size() - 1).getUuid();
        }

        // 看看自己是否已经被添加过了
        String uuid;
        if (mainPropList.containsKey(parentPropId)) {
            uuid  = mainPropList.get(parentPropId).getUuid();
        } else {
            uuid = getUUID();
        }

        // 看看自己的类型
        String strParentUuidMulti = "";
        int intType = imsPropMap.get(parentPropId).getPropType();
        if (intType == MasterPropTypeEnum.MULTICHECK.getValue()
                || intType == MasterPropTypeEnum.MULTICOMPLEX.getValue()
                ) {
            // 需要待会儿在最后插入数据的时候，添加一个父
            strParentUuidMulti = getUUID();
        }

        // 添加自己
        ImsPropValueBean imsPropValueBean = new ImsPropValueBean();
        // 算一个uuid出来
        imsPropValueBean.setUuid(uuid);
        // channel id
        imsPropValueBean.setChannelId(channel_id);
        // level
        imsPropValueBean.setLevel(level);
        // level value
        imsPropValueBean.setLevelValue(modelId);
        // prop id
        imsPropValueBean.setPropId(parentPropId);
        // prop value
        imsPropValueBean.setPropValue("");
        // parent
        imsPropValueBean.setParent(strParentUuid);
        // parent multi
        imsPropValueBean.setParentMulti(strParentUuidMulti);

        result.add(imsPropValueBean);

        return result;
    }


}
