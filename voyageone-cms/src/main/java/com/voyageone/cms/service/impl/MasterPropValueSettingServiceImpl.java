package com.voyageone.cms.service.impl;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.voyageone.cms.dao.MasterPropValueSettingDao;
import com.voyageone.cms.dao.ModelDao;
import com.voyageone.cms.dao.ProductDao;
import com.voyageone.cms.formbean.MasterPropertyFormBean;
import com.voyageone.cms.formbean.ModelProductCNBean;
import com.voyageone.cms.formbean.ProductImage;
import com.voyageone.cms.modelbean.MasterProperty;
import com.voyageone.cms.modelbean.PropertyOption;
import com.voyageone.cms.modelbean.PropertyRule;
import com.voyageone.cms.modelbean.PropertyValue;
import com.voyageone.cms.service.MasterPropValueSettingService;
import com.voyageone.common.bussiness.platformInfo.dao.PlatformInfoDao;
import com.voyageone.common.bussiness.platformInfo.model.PlatformInfoModel;
import com.voyageone.common.configs.ImsCategoryConfigs;
import com.voyageone.common.configs.beans.ImsCategoryBean;
import com.voyageone.ims.enums.MasterPropTypeEnum;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.ims.rule_expression.TextWord;

@Component("propertyService")
public class MasterPropValueSettingServiceImpl implements MasterPropValueSettingService {
	
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(MasterPropValueSettingServiceImpl.class);

	@Autowired
	private MasterPropValueSettingDao masterPropValueSettingDao;

	@Autowired
	private ModelDao modelDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	PlatformInfoDao platformInfoDao;
	
	/** type **/
	private final String ENTRY = "ENTRY";
	private final String modelKey = "propModel";
	private final String viewKey = "htmlView";
	private final String channelIdKey = "channelId";
	private final String levelKey = "level";
	private final String levelValueKey = "levelValue";
	private final String hiddenInfoKey = "hiddenInfo";
	private final String viewModelKey = "viewModel";
	private final String errMsgKey = "errorMessage";
	private final String errMsgMapKey = "errMsgObj";

	public RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();

	/**
	 * 初始化处理.
	 */
	@Override
	public Object init(MasterPropertyFormBean formData) {

		logger.info("初始化处理开始...");
		Map<String, Object> responseMap = new HashMap<String, Object>();

		String channelId = null;
		String level = null;
		String levelValue = null;
		String categoryId = null;
		String parentLevel = null;
		String parentLevelValue = null;
		
		logger.info("channel id：" + formData.getChannelId());
		logger.info("level：" + formData.getLevel());
		logger.info("level value：" + formData.getLevelValue());
		logger.info("parent level：" + formData.getParentLevel());
		logger.info("parent level value：" + formData.getParentLevelValue());
		logger.info("master category id：" + formData.getCategoryId());
		// 属性值列表.
		List<PropertyValue> updatePropertyValues = null;
		
		// 获取channel_id
		if (formData.getChannelId() != null && !"".equals(formData.getChannelId())) {
			channelId = formData.getChannelId();
		}

		// 获取level
		if (formData.getLevel() != null && !"".equals(formData.getLevel())) {
			level = formData.getLevel();
		}

		// 获取level_value
		if (formData.getLevelValue() != null && !"".equals(formData.getLevelValue())) {
			levelValue = formData.getLevelValue();
		}

		// 获取parent_level
		if (formData.getParentLevel() != null && !"".equals(formData.getParentLevel())) {
			parentLevel = formData.getParentLevel();
		}

		// 获取parent_level_value
		if (formData.getParentLevelValue() != null && !"".equals(formData.getParentLevelValue())) {
			parentLevelValue = formData.getParentLevelValue();
		}

		// 获取主数据category_id
		if (formData.getCategoryId() != null && !"".equals(formData.getCategoryId())) {
			categoryId = formData.getCategoryId();
		}
		
		if (parentLevel != null && parentLevelValue != null && !"".equals(parentLevel)
				&& !"".equals(parentLevelValue)) {

			updatePropertyValues = masterPropValueSettingDao.getUpdatePropertyValues(channelId,
					Integer.valueOf(parentLevel), parentLevelValue);
			logger.info("父类目属性值数量："+updatePropertyValues.size());
		} else {
			updatePropertyValues = masterPropValueSettingDao.getUpdatePropertyValues(channelId, Integer.valueOf(level),
					levelValue);
			logger.info("类目属性值数量："+updatePropertyValues.size());
		}
		if (categoryId != null) {
			
			// 获取属性列表.
			List<MasterProperty> masterProperties = masterPropValueSettingDao
					.getMasterProperties(Integer.valueOf(categoryId));
			if (masterProperties.size()==0) {
				Map<String, Object> errMessageMap = new HashMap<String, Object>();
				String errMsg="该主类目("+categoryId+")的属性不存在！";
				errMessageMap.put(this.errMsgKey, errMsg);
				responseMap.put(this.errMsgMapKey, errMessageMap);
				logger.info("初始化处理结束...");
				return responseMap;
			}
			logger.info("主类目属性数量："+masterProperties.size());
			
			Map<String, Object> viewModelMap =null;
			
			if (updatePropertyValues != null && updatePropertyValues.size() > 0) {
				
				 viewModelMap = this.buildResposeDataMap(Integer.valueOf(categoryId), masterProperties,
						updatePropertyValues);
				
			}else {
				viewModelMap = this.buildResposeDataMap(Integer.valueOf(categoryId), masterProperties,
						null);
			}
			if (viewModelMap != null) {
				responseMap.put(this.viewModelKey, viewModelMap);
				
			}
			//设定产品图片
			List<Map<String, Object>> images = this.getModelImageNames(level, levelValue, channelId);
			responseMap.put("images", images);
			
			//设置当前类目名称
			ImsCategoryBean  mtCateBean = ImsCategoryConfigs.getMtCategoryBeanById(Integer.valueOf(categoryId));
			responseMap.put("currentCategoryName", mtCateBean.getCategoryName());
			
			List<PlatformInfoModel> platformInfo = platformInfoDao.getPlatformInfo(Integer.valueOf(categoryId));
			responseMap.put("platformInfo", platformInfo);
		}
		
		//设置顶层所有类目
		List<ImsCategoryBean> topCategories = ImsCategoryConfigs.getMtCategoryBeanById(0).getSubCategories();
		responseMap.put("categories", topCategories);	
		
		// 设定hidden值.
		Map<String, Object> hiddenMap = new HashMap<String, Object>();
		hiddenMap.put(this.channelIdKey, channelId);
		hiddenMap.put(this.levelKey, level);
		hiddenMap.put(this.levelValueKey, levelValue);
		responseMap.put(this.hiddenInfoKey, hiddenMap);
		logger.info("初始化处理结束...");
		return responseMap;

	}

	/**
	 * 查询处理.
	 */
	@Override
	public Object search(MasterPropertyFormBean formData) {
		logger.info("查询处理开始...");
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> viewModelMap = null;
		List<PropertyValue> updatePropertyValues = null;
		int categoryId=0;
		Map<String, Object> hiddenMap = formData.getHiddenInfo();
		// 获取channel_id
		String channelId = hiddenMap.get(this.channelIdKey).toString();
		// 获取level
		int level = Integer.valueOf(hiddenMap.get(this.levelKey).toString());
		// 获取level_value
		String levelValue = hiddenMap.get(this.levelValueKey).toString();

		// 获取主数据category_id
		if (formData.getCategoryId() != null && !"".equals(formData.getCategoryId())) {
			try {
				categoryId = Integer.valueOf(formData.getCategoryId());
			} catch (NumberFormatException e) {
				Map<String, Object> errMessageMap = new HashMap<String, Object>();
				String errMsg="输入的主类目ID类型错误，请重新输入！";
				errMessageMap.put(this.errMsgKey, errMsg);
				responseMap.put(this.errMsgMapKey, errMessageMap);
				logger.error(errMsg);
				return responseMap;
			}
			
		}else{
			Map<String, Object> errMessageMap = new HashMap<String, Object>();
			String errMsg="请输入主类目ID!";
			errMessageMap.put(this.errMsgKey, errMsg);
			responseMap.put(this.errMsgMapKey, errMessageMap);
			logger.error(errMsg);
			return responseMap;
		}
		// 获取属性列表.
		List<MasterProperty> masterProperties = masterPropValueSettingDao
				.getMasterProperties(categoryId);

		if (masterProperties == null || masterProperties.size() < 1) {
			String errMsg="您查询的类目不存在，请查证后再查询！";
			Map<String, Object> errMessageMap = new HashMap<String, Object>();
			errMessageMap.put(this.errMsgKey, errMsg);
			responseMap.put(this.errMsgMapKey, errMessageMap);
			logger.error(errMsg);
			return responseMap;
		}
		// 属性值列表.
		updatePropertyValues = masterPropValueSettingDao.getUpdatePropertyValues(channelId, Integer.valueOf(level),
				levelValue);
		if (categoryId != 0) {
			if (updatePropertyValues != null && updatePropertyValues.size() > 0) {
				viewModelMap = this.buildResposeDataMap(categoryId, masterProperties,
						updatePropertyValues);
			} else {
				viewModelMap = this.buildResposeDataMap(categoryId, masterProperties, null);
			}
		}

		if (viewModelMap != null) {
			responseMap.put(this.viewModelKey, viewModelMap);
			//设定产品图片
			List<Map<String, Object>> images = this.getModelImageNames(String.valueOf(level), levelValue, channelId);
			responseMap.put("images", images);
		}
		//设置顶层所有类目
		List<ImsCategoryBean> topCategories = ImsCategoryConfigs.getMtCategoryBeanById(0).getSubCategories();
		responseMap.put("categories", topCategories);
		//设置当前类目名称
		ImsCategoryBean  mtCateBean = ImsCategoryConfigs.getMtCategoryBeanById(categoryId);
		responseMap.put("currentCategoryName", mtCateBean.getCategoryName());
		//设置平台信息.
		List<PlatformInfoModel> platformInfo = platformInfoDao.getPlatformInfo(categoryId);
		responseMap.put("platformInfo", platformInfo);
		logger.info("查询处理结束...");
		return responseMap;
	}

	/**
	 * 提交处理.
	 */
	@Override
	@Transactional("transactionManager")
	public boolean submit(MasterPropertyFormBean formData) {
		logger.info("提交处理开始...");
		Map<String, Object> modelMap = formData.getPropModel();
		Map<String, Object> hiddenMap = formData.getHiddenInfo();
		// 获取channel_id
		String channelId = hiddenMap.get(this.channelIdKey).toString();
		// 获取level
		int level = Integer.valueOf(hiddenMap.get(this.levelKey).toString());
		// 获取level_value
		String levelValue = hiddenMap.get(this.levelValueKey).toString();

		// 查询属性值列表.
		int valuesCount = masterPropValueSettingDao.getPropValueCount(channelId, level, levelValue);

		if (valuesCount > 0) {
			
			masterPropValueSettingDao.delete(channelId, level, levelValue);
		}
		
		List<PropertyValue> propertyValues = contructPropertyValues(channelId, level, levelValue, modelMap);

		if (propertyValues != null && propertyValues.size() > 0) {
			// 保存属性值对象列表.
			int count = this.masterPropValueSettingDao.save(propertyValues);
			
			if (count > 0) {
				return true;
			}
			
		}
		logger.info("提交处理结束...");
		return false;
	}

	/**
	 * 构建responseData.
	 * 
	 * @param categoryId
	 * @param updatePropertyValues
	 * @return
	 */
	private Map<String, Object> buildResposeDataMap(int categoryId, List<MasterProperty> masterProperties,
			List<PropertyValue> updatePropertyValues) {
		logger.debug("buildResposeDataMap() start:"+System.currentTimeMillis());
		Map<String, Object> viewModelMap = new HashMap<String, Object>();

		if (masterProperties != null && masterProperties.size() > 0) {

			// 获取属性选项列表.
			List<PropertyOption> propertyOptions = masterPropValueSettingDao.getPropertyOptions(categoryId);
			// 获取属性规则列表.
			List<PropertyRule> propertyRules = masterPropValueSettingDao.getPropertyRules(categoryId);

			Map<String, Object> modelMap = new HashMap<String, Object>();

			// 装配属性选项列表.
			setPropertyOptions(masterProperties, propertyOptions);

			// 装配属性规则.
			setPropertyRules(masterProperties, propertyRules);

			Map<Integer, MasterProperty> propIdPropMap = new HashMap<Integer, MasterProperty>();
			for (MasterProperty masterProperty : masterProperties) {
				propIdPropMap.put(masterProperty.getProp_id(), masterProperty);
			}

			List<MasterProperty> resultProperties = buildPorpertyTrees(masterProperties);

			if (updatePropertyValues == null || propIdPropMap.get(updatePropertyValues.get(0).getProp_id()) == null) { // 新建
				modelMap = buildModelMap(resultProperties, new HashMap<String, Object>(), true);

			} else { // 编辑.
				Map<String, Object> orgModelMap = buildModelMap(resultProperties, new HashMap<String, Object>(), false);
				List<PropertyValue> valuesTrees = buildValueTrees(updatePropertyValues, propIdPropMap);
				Map<String, Object> valueModelMap = buildUpdateModelMap(valuesTrees, new HashMap<String, Object>(),
						propIdPropMap);
				synchronizeModelMap(orgModelMap, valueModelMap);
				modelMap = valueModelMap;

			}

			String htmlView = buildDivView(resultProperties).toString();

			// 设定view
			viewModelMap.put(this.viewKey, htmlView);
			// 设定页面默认值
			viewModelMap.put(this.modelKey, modelMap);
		}else {
			
		}

		return viewModelMap;

	}

	/**
	 * 同步modelMap.
	 * 
	 * @param orgModelMap
	 * @param valueModelMap
	 */
	private void synchronizeModelMap(Map<String, Object> orgModelMap, Map<String, Object> valueModelMap) {
		for (Map.Entry<String, Object> entry : orgModelMap.entrySet()) {
			if (valueModelMap.get(entry.getKey()) == null) {
				valueModelMap.put(entry.getKey(), entry.getValue());
			} else {
				if (entry.getValue() instanceof Map) {
					synchronizeModelMap((Map<String, Object>) entry.getValue(),
							(Map<String, Object>) valueModelMap.get(entry.getKey()));
				}
			}
		}
	}

	/**
	 * 创建更新用modelMap.
	 * 
	 * @param valuesTrees
	 * @param modelMap
	 * @param propIdPropMap
	 * @return
	 */
	private Map<String, Object> buildUpdateModelMap(List<PropertyValue> valuesTrees, Map<String, Object> modelMap,
			Map<Integer, MasterProperty> propIdPropMap) {

		for (PropertyValue propertyValue : valuesTrees) {

			MasterPropTypeEnum type = propertyValue.getType();
			String key = encodeNgModelName(type, propertyValue.getProp_id());
			switch (type) {
			case INPUT:
				modelMap.put(key, propertyValue.getProp_value());
				break;
			case SINGLECHECK:
				TextWord textWord = (TextWord) ruleJsonMapper.deserializeRuleExpression(propertyValue.getProp_value())
						.getRuleWordList().get(0);
				modelMap.put(key, textWord.getValue());
				break;
			case MULTICHECK:
				Map<String, Object> multiCheckMap = new HashMap<String, Object>();
				List<PropertyOption> options = propIdPropMap.get(propertyValue.getProp_id()).getPropertyOptions();
				// 设定默认值.
				List<PropertyValue> valueOptions = propertyValue.getValues();

				for (PropertyOption propertyOption : options) {
					for (PropertyValue selectedOption : valueOptions) {
						TextWord selectedValue = (TextWord) ruleJsonMapper
								.deserializeRuleExpression(selectedOption.getProp_value()).getRuleWordList().get(0);
						String optionModelName = "OPTION" + propertyOption.getProp_option_id();
						if (propertyOption.getProp_option_value().equals(selectedValue.getValue())) {
							multiCheckMap.put(optionModelName, propertyOption.getProp_option_value());
						}
					}
				}

				modelMap.put(key, multiCheckMap);
				break;
			case lABEL:
				modelMap.put(key, propertyValue.getProp_value());
				break;
			case COMPLEX:
				Map<String, Object> complexModelMap = new HashMap<String, Object>();
				buildUpdateModelMap(propertyValue.getValues(), complexModelMap, propIdPropMap);
				modelMap.put(key, complexModelMap);
				break;
			case MULTICOMPLEX:
				List<Map<String, Object>> complexModelMapList = new ArrayList<Map<String, Object>>();

				List<PropertyValue> subPropertyValues = propertyValue.getValues();

				if (subPropertyValues == null || subPropertyValues.size() == 0) {
					Map<String, Object> complexMap = new HashMap<String, Object>();
					subPropertyValues = new ArrayList<PropertyValue>();
					PropertyValue defaultValue = new PropertyValue();
					subPropertyValues.add(defaultValue);
					buildUpdateModelMap(subPropertyValues, complexMap, propIdPropMap);
					complexModelMapList.add(complexMap);
				}

				for (PropertyValue subValue : subPropertyValues) {
					Map<String, Object> complexMap = new HashMap<String, Object>();
					buildUpdateModelMap(subValue.getValues(), complexMap, propIdPropMap);
					complexModelMapList.add(complexMap);
				}

				modelMap.put(key, complexModelMapList);
				break;

			default:
				break;
			}
		}

		return modelMap;
	}

	/**
	 * 创建model.
	 * 
	 * @param properties
	 * @param modelMap
	 * @return
	 */
	private Map<String, Object> buildModelMap(List<MasterProperty> properties, Map<String, Object> modelMap,
			boolean isSetDefaultValue) {

		for (MasterProperty masterProperty : properties) {

			MasterPropTypeEnum type = MasterPropTypeEnum.valueOf(masterProperty.getProp_type());
			String key = encodeNgModelName(type, masterProperty.getProp_id());
			switch (type) {
			case INPUT:
				if (isSetDefaultValue&&masterProperty.getProp_value_default()!=null&&!"".equals(masterProperty.getProp_value_default())) {
					RuleExpression ruleExpression = new RuleExpression();
					ruleExpression.addRuleWord(new TextWord(masterProperty.getProp_value_default()));
					String encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
					modelMap.put(key, encodePropValue);
				} else {
					modelMap.put(key, "");
				}
				break;
			case SINGLECHECK:
				if (isSetDefaultValue) {
					modelMap.put(key, masterProperty.getProp_value_default());
				} else {
					modelMap.put(key, "");
				}
				break;
			case MULTICHECK:
				Map<String, Object> multiCheckMap = new HashMap<String, Object>();
				// 设定默认值.
				List<PropertyOption> options = masterProperty.getPropertyOptions();
				if (masterProperty.getProp_value_default() != null
						&& !"".equals(masterProperty.getProp_value_default())) {
					for (PropertyOption propertyOption : options) {
						String optionModelName = "OPTION" + propertyOption.getProp_option_id();
						if (isSetDefaultValue) {
							if (propertyOption.getProp_option_value().equals(masterProperty.getProp_value_default())) {
								multiCheckMap.put(optionModelName, propertyOption.getProp_option_value());
							}
						}

					}

				}
				modelMap.put(key, multiCheckMap);
				break;
			case lABEL:
				if (masterProperty.getProp_value_default() != null){
					modelMap.put(key, masterProperty.getProp_value_default());
				}
				break;
			case COMPLEX:
				Map<String, Object> complexModelMap = new HashMap<String, Object>();
				buildModelMap(masterProperty.getProperties(), complexModelMap, isSetDefaultValue);
				modelMap.put(key, complexModelMap);
				break;
			case MULTICOMPLEX:
				List<Map<String, Object>> complexModelMapList = new ArrayList<Map<String, Object>>();
				Map<String, Object> complexMap = new HashMap<String, Object>();
				buildModelMap(masterProperty.getProperties(), complexMap, isSetDefaultValue);
				complexModelMapList.add(complexMap);
				modelMap.put(key, complexModelMapList);
				break;
			case MULTIINPUT:
				List<Map<String, Object>> inputModelMapList = new ArrayList<Map<String, Object>>();
				Map<String, Object> defaultInputMap = new HashMap<String, Object>();
				buildModelMap(masterProperty.getProperties(), defaultInputMap, isSetDefaultValue);
				inputModelMapList.add(defaultInputMap);
				modelMap.put(key, inputModelMapList);
				break;

			default:
				break;
			}
		}

		return modelMap;
	}

	/**
	 * 创建属性值层次关系.
	 * 
	 * @param propValues
	 */
	private List<PropertyValue> buildValueTrees(List<PropertyValue> propValues,
			Map<Integer, MasterProperty> propIdPropMap) {
		// 设置属性层次关系.
		List<PropertyValue> assistPropValues = new ArrayList<PropertyValue>(propValues);

		List<PropertyValue> removePropValues = new ArrayList<PropertyValue>();

		for (int i = 0; i < propValues.size(); i++) {
			PropertyValue value = propValues.get(i);
			int type = propIdPropMap.get(value.getProp_id()).getProp_type();
			value.setType(MasterPropTypeEnum.valueOf(type));

			List<PropertyValue> subTree = new ArrayList<PropertyValue>();
			for (Iterator assIterator = assistPropValues.iterator(); assIterator.hasNext();) {

				PropertyValue subValue = (PropertyValue) assIterator.next();
				if (subValue.getParent() != null && value.getUuid() != null) {
					if (subValue.getParent().equals(value.getUuid())) {
						subTree.add(subValue);
						assIterator.remove();
					}
				}

			}
			value.setValues(subTree);
			if (value.getParent() != null && !"".equals(value.getParent())) {
				removePropValues.add(value);
			}

		}
		propValues.removeAll(removePropValues);

		return propValues;
	}

	/**
	 * 构建属性值.
	 * 
	 * @param channelId
	 * @param level
	 * @param levelValue
	 * @param formDataMap
	 * @return
	 */
	private List<PropertyValue> contructPropertyValues(String channelId, int level, String levelValue,
			Map<String, Object> formDataMap) {
		List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();
		for (Map.Entry<String, Object> entry : formDataMap.entrySet()) {
			if (entry.getValue() != null) {
				String encodeValue = entry.getKey();
				Map.Entry<MasterPropTypeEnum, String> type_propIdEntry = decodeNgModelName(encodeValue);
				int propId = Integer.valueOf(type_propIdEntry.getValue());
				MasterPropTypeEnum type = type_propIdEntry.getKey();
				switch (type) {
				case INPUT:
					String inputValue = (String) (entry.getValue());
					if (inputValue != null && !"".equals(inputValue)) {
						propertyValues.add(constructInputProp(channelId, level, levelValue, propId, "", inputValue));
					}
					break;
				case SINGLECHECK:
					String singleCheckValue = (String) (entry.getValue());
					if (singleCheckValue != null && !"".equals(singleCheckValue)) {
						propertyValues.add(constructSingleCheckBox(channelId, level, levelValue, propId, "",
								(String) (entry.getValue())));
					}
					break;
				case MULTICHECK:
					List<PropertyValue> multiCheckValues = constructMultiCheckBox(channelId, level, levelValue, propId,
							"", (Map<String, Object>) entry.getValue());
					if (multiCheckValues != null) {
						propertyValues.addAll(multiCheckValues);
					}

					break;
				case lABEL:
					String labelValue = (String) (entry.getValue());
					if (labelValue != null && !"".equals(labelValue)) {
						propertyValues.add(constructInputProp(channelId, level, levelValue, propId, "", labelValue));
					}
					break;
				case COMPLEX:
					List<PropertyValue> complexValues = constructComplex(channelId, level, levelValue, propId,
							(HashMap) (entry.getValue()), "");
					if (complexValues != null) {
						propertyValues.addAll(complexValues);
					}

					break;
				case MULTICOMPLEX:
					List<PropertyValue> multiComplexValues = constructMultiComplex(channelId, level, levelValue, propId,
							(List<Map<String, Object>>) (entry.getValue()), "");
					if (multiComplexValues != null) {
						propertyValues.addAll(multiComplexValues);
					}

					break;
				}
			}
		}

		return propertyValues;
	}

	/**
	 * 构建SingleCheck属性值.
	 * 
	 * @param channelId
	 * @param level
	 * @param levelValue
	 * @param propId
	 * @param parent
	 * @param propValue
	 * @return
	 */
	private PropertyValue constructSingleCheckBox(String channelId, int level, String levelValue, int propId,
			String parent, String propValue) {
		PropertyValue singleCheckBoxProp = new PropertyValue();
		singleCheckBoxProp.setChannel_id(channelId);
		singleCheckBoxProp.setProp_id(propId);
		singleCheckBoxProp.setLevel(level);
		singleCheckBoxProp.setLevel_value(levelValue);
		singleCheckBoxProp.setParent(parent);
		singleCheckBoxProp.setCreater("lewis");
		singleCheckBoxProp.setUuid(UUID.randomUUID().toString().replace("-", "").replace("-", ""));

		RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
		RuleExpression ruleExpression = new RuleExpression();
		ruleExpression.addRuleWord(new TextWord(propValue));
		String encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
		singleCheckBoxProp.setProp_value(encodePropValue);

		return singleCheckBoxProp;

	}

	/**
	 * 构建input属性值.
	 * 
	 * @param channelId
	 * @param level
	 * @param levelValue
	 * @param propId
	 * @param parent
	 * @param propValue
	 * @return
	 */
	private PropertyValue constructInputProp(String channelId, int level, String levelValue, int propId, String parent,
			String propValue) {
		PropertyValue inputProp = new PropertyValue();
		inputProp.setChannel_id(channelId);
		inputProp.setProp_id(propId);
		inputProp.setLevel(level);
		inputProp.setLevel_value(levelValue);
		inputProp.setParent(parent);
		inputProp.setCreater("lewis");
		inputProp.setUuid(UUID.randomUUID().toString().replace("-", ""));

		inputProp.setProp_value(propValue);

		return inputProp;
	}

	/**
	 * 构建MultiCheck属性值.
	 * 
	 * @param channelId
	 * @param level
	 * @param levelValue
	 * @param propId
	 * @param parent
	 * @param propValues
	 * @return
	 */
	private List<PropertyValue> constructMultiCheckBox(String channelId, int level, String levelValue, int propId,
			String parent, Map<String, Object> propValues) {
		List<PropertyValue> multiCheckBoxList = new ArrayList<>();
		if (propValues.size() > 0) {
			PropertyValue checkBoxParent = new PropertyValue();
			checkBoxParent.setChannel_id(channelId);
			checkBoxParent.setParent(parent);
			checkBoxParent.setProp_id(propId);
			checkBoxParent.setLevel(level);
			checkBoxParent.setLevel_value(levelValue);
			checkBoxParent.setCreater("lewis");
			checkBoxParent.setUuid(UUID.randomUUID().toString().replace("-", ""));
			checkBoxParent.setProp_value(null);
			RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();

			for (String propValue : (List<String>) new ArrayList(propValues.values())) {
				if (propValue != null && !"".equals(propValue)) {
					PropertyValue checkBox = new PropertyValue();
					checkBox.setChannel_id(channelId);
					checkBox.setParent(checkBoxParent.getUuid());
					checkBox.setProp_id(propId);
					checkBox.setLevel(level);
					checkBox.setLevel_value(levelValue);
					checkBox.setCreater("lewis");
					RuleExpression ruleExpression = new RuleExpression();
					ruleExpression.addRuleWord(new TextWord(propValue));
					String encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
					checkBox.setProp_value(encodePropValue);
					checkBox.setUuid(UUID.randomUUID().toString().replace("-", ""));
					multiCheckBoxList.add(checkBox);
				}
			}

			if (multiCheckBoxList.size() > 0) {
				multiCheckBoxList.add(checkBoxParent);
				return multiCheckBoxList;
			}

		}
		return null;
	}

	/**
	 * 构建Complex属性值.
	 * 
	 * @param channelId
	 * @param level
	 * @param levelValue
	 * @param propId
	 * @param propMap
	 * @param parent
	 * @return
	 */
	private List<PropertyValue> constructComplex(String channelId, int level, String levelValue, int propId,
			Map<String, Object> propMap, String parent) {
		PropertyValue parentComplexPropValue = new PropertyValue();
		List<PropertyValue> singleTypeList = new ArrayList<>();

		parentComplexPropValue.setChannel_id(channelId);
		parentComplexPropValue.setParent(parent);
		parentComplexPropValue.setProp_id(propId);
		parentComplexPropValue.setLevel(level);
		parentComplexPropValue.setLevel_value(levelValue);
		parentComplexPropValue.setCreater("lewis");
		parentComplexPropValue.setUuid(UUID.randomUUID().toString().replace("-", ""));
		parentComplexPropValue.setProp_value(null);

		for (Map.Entry<String, Object> entry : propMap.entrySet()) {
			String encodeValue = entry.getKey();
			Map.Entry<MasterPropTypeEnum, String> type_propEntry = decodeNgModelName(encodeValue);
			MasterPropTypeEnum type = type_propEntry.getKey();
			int subPropId = Integer.valueOf(type_propEntry.getValue());
			switch (type) {
			case INPUT:
				String inputValue = (String) (entry.getValue());
				if (inputValue != null && !"".equals(inputValue)) {
					singleTypeList.add(constructInputProp(channelId, level, levelValue, subPropId,
							parentComplexPropValue.getUuid(), inputValue));
				}

				break;
			case SINGLECHECK:
				String singleCheckValue = (String) (entry.getValue());
				if (singleCheckValue != null && !"".equals(singleCheckValue)) {
					singleTypeList.add(constructSingleCheckBox(channelId, level, levelValue, subPropId,
							parentComplexPropValue.getUuid(), singleCheckValue));
				}

				break;
			case MULTICHECK:
				List<PropertyValue> checkValues = constructMultiCheckBox(channelId, level, levelValue, subPropId,
						parentComplexPropValue.getUuid(), (Map<String, Object>) entry.getValue());
				if (checkValues != null) {
					singleTypeList.addAll(checkValues);
				}

				break;
			case COMPLEX:
				List<PropertyValue> complexValues = constructComplex(channelId, level, levelValue, subPropId,
						(HashMap) (entry.getValue()), parentComplexPropValue.getUuid());
				if (complexValues != null) {
					singleTypeList.addAll(complexValues);
				}

				break;
			case MULTICOMPLEX:
				List<PropertyValue> multiComplexValues = constructMultiComplex(channelId, level, levelValue, subPropId,
						(List<Map<String, Object>>) (entry.getValue()), parentComplexPropValue.getUuid());
				if (multiComplexValues != null) {
					singleTypeList.addAll(multiComplexValues);
				}

				break;
			}
		}
		if (singleTypeList.size() > 0) {
			singleTypeList.add(parentComplexPropValue);

			return singleTypeList;
		}
		return null;
	}

	/**
	 * 构建MultiComplex属性值.
	 * 
	 * @param channelId
	 * @param level
	 * @param levelValue
	 * @param propId
	 * @param multiComplexMapList
	 * @param parent
	 * @return
	 */
	private List<PropertyValue> constructMultiComplex(String channelId, int level, String levelValue, int propId,
			List<Map<String, Object>> multiComplexMapList, String parent) {
		PropertyValue multiComplexPropValue = new PropertyValue();
		List<PropertyValue> singleTypeList = new ArrayList<>();

		multiComplexPropValue.setChannel_id(channelId);
		multiComplexPropValue.setParent(parent);
		multiComplexPropValue.setProp_id(propId);
		multiComplexPropValue.setLevel(level);
		multiComplexPropValue.setLevel_value(levelValue);
		multiComplexPropValue.setCreater("lewis");
		multiComplexPropValue.setUuid(UUID.randomUUID().toString().replace("-", ""));
		multiComplexPropValue.setProp_value(null);

		for (Map<String, Object> complexPropMap : multiComplexMapList) {
			List<PropertyValue> complexValues = constructComplex(channelId, level, levelValue, propId, complexPropMap,
					multiComplexPropValue.getUuid());
			if (complexValues != null) {
				singleTypeList.addAll(complexValues);
			}

		}
		if (singleTypeList.size() > 0) {
			singleTypeList.add(multiComplexPropValue);
			return singleTypeList;
		}

		return null;
	}

	/**
	 * 创建div视图.
	 * 
	 * @param masterProperties
	 * @return
	 */
	private StringBuilder buildDivView(List<MasterProperty> masterProperties) {
		StringBuilder htmlStringBuilder = new StringBuilder();
		htmlStringBuilder.append("<div class=\"panel-body\">");
		htmlStringBuilder = buildHtmlView(masterProperties, htmlStringBuilder, false, modelKey);
		htmlStringBuilder.append("</div>");
		return htmlStringBuilder;
	}

	/**
	 * 创建层次关系.
	 * 
	 * @param masterProperties
	 */
	private List<MasterProperty> buildPorpertyTrees(List<MasterProperty> masterProperties) {
		// 设置属性层次关系.
		List<MasterProperty> assistProperties = new ArrayList<MasterProperty>(masterProperties);

		List<MasterProperty> removeProperties = new ArrayList<MasterProperty>();

		for (int i = 0; i < masterProperties.size(); i++) {
			MasterProperty property = masterProperties.get(i);
			List<MasterProperty> resProperties = new ArrayList<MasterProperty>();
			for (Iterator assIterator = assistProperties.iterator(); assIterator.hasNext();) {

				MasterProperty subpProperty = (MasterProperty) assIterator.next();
				if (subpProperty.getParent_prop_id() == property.getProp_id()) {
					resProperties.add(subpProperty);
					assIterator.remove();
				}

			}
			property.setProperties(resProperties);
			if (property.getIs_top_prop() != 1) {
				removeProperties.add(property);
			}

		}
		masterProperties.removeAll(removeProperties);
		//排序
		Collections.sort(masterProperties);
		Collections.reverse(masterProperties);
		return masterProperties;
	}

	/**
	 * 创建属性视图.
	 * 
	 * @param masterProperties
	 * @param htmlStringBuilder
	 * @return
	 */
	private StringBuilder buildHtmlView(List<MasterProperty> masterProperties, StringBuilder htmlStringBuilder,
			boolean isMulti, String path) {

		for (MasterProperty masterProperty : masterProperties) {
			MasterPropTypeEnum type = MasterPropTypeEnum.valueOf(masterProperty.getProp_type());
			switch (type) {
			case INPUT:
				String inputView = buildInputView(masterProperty, isMulti, path);
				htmlStringBuilder.append(inputView);
				break;
			case SINGLECHECK:
				String singleCheckView = buildSingleCheckView(masterProperty, isMulti, path);
				htmlStringBuilder.append(singleCheckView);
				break;
			case MULTICHECK:
				String multiCheckView = buildMultiCheckView(masterProperty, isMulti, path);
				htmlStringBuilder.append(multiCheckView);
				break;
			case lABEL:
				String lableView = buildLabelView(masterProperty,path);
				htmlStringBuilder.append(lableView);
				break;
			case COMPLEX:
				htmlStringBuilder = buildComplexView(htmlStringBuilder, masterProperty, isMulti, path);
				break;
			case MULTICOMPLEX:
				htmlStringBuilder = buildMultiComplexView(htmlStringBuilder, masterProperty, true, path);
				break;
			case MULTIINPUT:
				htmlStringBuilder = buildMultiInputView(htmlStringBuilder, masterProperty, true, path);
				break;
			default:
				break;
			}

		}
		return htmlStringBuilder;
	}

	/**
	 * 创建Complex视图.
	 * 
	 * @param htmlStringBuilder
	 * @param masterProperty
	 * @return
	 */
	private StringBuilder buildComplexView(StringBuilder htmlStringBuilder, MasterProperty masterProperty,
			boolean isMulti, String parentPath) {

		StringBuilder complexBuilder = new StringBuilder();

		String ngModelName = encodeNgModelName(MasterPropTypeEnum.valueOf(masterProperty.getProp_type()),
				masterProperty.getProp_id());

		String path = parentPath + "." + ngModelName;
		
		if (!isMulti) {
			
			buildRulesView(masterProperty, complexBuilder);

			complexBuilder = buildHtmlView(masterProperty.getProperties(), complexBuilder, isMulti, path);

			bulidFieldSet(complexBuilder, masterProperty);
		}else {
			
			complexBuilder.append("<td>");
			
			buildRulesView(masterProperty, complexBuilder);

			complexBuilder = buildHtmlView(masterProperty.getProperties(), complexBuilder, false, path);

			bulidFieldSet(complexBuilder, masterProperty);
			
			complexBuilder.append("</td>");
		}

		
		

		return htmlStringBuilder.append(complexBuilder);
	}

	/**
	 * 创建 MultiComplex 视图.
	 * 
	 * @param htmlStringBuilder
	 * @param masterProperty
	 * @param isMulti
	 * @param parentPath
	 * @return
	 */
	private StringBuilder buildMultiComplexView(StringBuilder htmlStringBuilder, MasterProperty masterProperty,
			boolean isMulti, String parentPath) {
		StringBuilder multiComplexBuilder = new StringBuilder();
		String ngModelName = encodeNgModelName(MasterPropTypeEnum.valueOf(masterProperty.getProp_type()),
				masterProperty.getProp_id());
		String path = parentPath + "." + ngModelName;

		buildRulesView(masterProperty, multiComplexBuilder);
		multiComplexBuilder.append("<div>");
		multiComplexBuilder.append("<table class=\"table table-bordered table-hover bg-white\">");
		multiComplexBuilder.append("<tr>");
		for (int i = 0; i < masterProperty.getProperties().size(); i++) {
			MasterProperty property = masterProperty.getProperties().get(i);
			multiComplexBuilder.append("<th>");
			multiComplexBuilder.append(property.getProp_name());
			multiComplexBuilder.append("</th>");
		}
		multiComplexBuilder.append("<th>");
		multiComplexBuilder.append("删除");
		multiComplexBuilder.append("</th>");
		multiComplexBuilder.append("</tr>");
		String valueModelList = "list" + masterProperty.getProp_id();
		multiComplexBuilder.append("<tr ng-repeat=\"" + ENTRY + " in " + path + " as " + valueModelList + "\">");
		multiComplexBuilder = buildHtmlView(masterProperty.getProperties(), multiComplexBuilder, isMulti, ENTRY);
		multiComplexBuilder.append("<td style=\"white-space: nowrap\">");
		multiComplexBuilder.append("<div class=\"buttons\">");
		multiComplexBuilder
				.append("<button ng-click=\""+valueModelList+ ".splice($index, 1)\" title=\"Delete\" class=\"btn btn-sm btn-danger\">");
		multiComplexBuilder.append("<em class=\"fa fa-trash\"></em>");
		multiComplexBuilder.append("</button>");
		multiComplexBuilder.append("</td>");
		multiComplexBuilder.append(" </div>");
		multiComplexBuilder.append("</tr>");
		multiComplexBuilder.append("</table>");
		multiComplexBuilder
		.append("<button type=\"button\" class=\"btn btn-default\" ng-click=\"" + valueModelList + ".push({})\" >添加</button>");
		multiComplexBuilder.append("</div>");
		bulidFieldSet(multiComplexBuilder, masterProperty);
		return htmlStringBuilder.append(multiComplexBuilder);
	}

	/**
	 *  创建MultiInput视图.
	 * @param htmlStringBuilder
	 * @param masterProperty
	 * @param isMulti
	 * @param path
	 * @return
	 */
	private StringBuilder buildMultiInputView(StringBuilder htmlStringBuilder, MasterProperty masterProperty,
			boolean isMulti, String path) {
		StringBuilder multiInputBuilder = new StringBuilder();
		buildRulesView(masterProperty, multiInputBuilder);
		multiInputBuilder = buildHtmlView(masterProperty.getProperties(), multiInputBuilder, isMulti, path);
		bulidFieldSet(multiInputBuilder, masterProperty);
		return htmlStringBuilder.append(multiInputBuilder);
	}

	/**
	 * 创建label视图.
	 * @param masterProperty
	 * @param path
	 * @return
	 */
	private String buildLabelView(MasterProperty masterProperty, String path) {
		StringBuilder htmlStringBuilder = new StringBuilder();
		String labelModelName = encodeNgModelName(MasterPropTypeEnum.valueOf(masterProperty.getProp_type()),
				masterProperty.getProp_id());
		String ng_model = path + "." + labelModelName;
		buildRulesView(masterProperty, htmlStringBuilder);
		htmlStringBuilder.append("<label>");
		htmlStringBuilder.append("{{"+ng_model+"}}");
		htmlStringBuilder.append("</label>");
		bulidFieldSet(htmlStringBuilder, masterProperty);
		return htmlStringBuilder.toString();
	}

	/**
	 * 创建multiCheck视图.
	 * @param masterProperty
	 * @param isMulti
	 * @param path
	 * @return
	 */
	private String buildMultiCheckView(MasterProperty masterProperty, boolean isMulti, String path) {
		StringBuilder htmlStringBuilder = new StringBuilder();
		List<PropertyOption> options = masterProperty.getPropertyOptions();
		String multiCheckModelName = encodeNgModelName(MasterPropTypeEnum.valueOf(masterProperty.getProp_type()),
				masterProperty.getProp_id());
		if (!isMulti) {

			buildRulesView(masterProperty, htmlStringBuilder);

			for (PropertyOption propertyOption : options) {
				String optionModelName = "OPTION" + propertyOption.getProp_option_id();
				String ng_model = path + "." + multiCheckModelName + "." + optionModelName;
				htmlStringBuilder.append("<label>");
				htmlStringBuilder.append("<input type=\"checkbox\" ");
				htmlStringBuilder.append("ng-model=\"" + ng_model + "\" ");
				htmlStringBuilder.append("ng-true-value=\"'" + propertyOption.getProp_option_value() + "'\" ");
				htmlStringBuilder.append("ng-false-value=\"''\"");
				htmlStringBuilder.append("/>");
				htmlStringBuilder.append(propertyOption.getProp_option_name());
				htmlStringBuilder.append("</label>");
			}
			bulidFieldSet(htmlStringBuilder, masterProperty);
		} else {
			htmlStringBuilder.append("<td>");
			htmlStringBuilder.append("<fieldset>");
			buildRulesView(masterProperty, htmlStringBuilder);
			Map<String, Object> defaultCheckMap = new HashMap<String, Object>();
			for (PropertyOption propertyOption : options) {
				String optionModelName = "OPTION" + propertyOption.getProp_option_id();
				String ng_model = path + "." + multiCheckModelName + "." + optionModelName;
				htmlStringBuilder.append("<label>");
				htmlStringBuilder.append("<input type=\"checkbox\" ");
				htmlStringBuilder.append("ng-model=\"" + ng_model + "\" ");
				htmlStringBuilder.append("ng-true-value=\"'" + propertyOption.getProp_option_value() + "'\" ");
				htmlStringBuilder.append("ng-false-value=\"''\"");
				htmlStringBuilder.append("/>");
				htmlStringBuilder.append(propertyOption.getProp_option_name());
				htmlStringBuilder.append("</label>");
				if (masterProperty.getProp_value_default() != null && masterProperty.getProp_value_default() != ""
						&& propertyOption.getProp_option_value().equals(masterProperty.getProp_value_default())) {
					// 绑定默认值
					defaultCheckMap.put(optionModelName, masterProperty.getProp_value_default());
				} else {
					defaultCheckMap.put(optionModelName, "");
				}
			}
			htmlStringBuilder.append("</fieldset>");
			htmlStringBuilder.append("</td>");

		}
		isMulti = false;
		return htmlStringBuilder.toString();
	}

	/**
	 * 创建singleCheck视图.
	 * @param masterProperty
	 * @param isMulti
	 * @param path
	 * @return
	 */
	private String buildSingleCheckView(MasterProperty masterProperty, boolean isMulti, String path) {
		StringBuilder htmlStringBuilder = new StringBuilder();
		String singleCheckModelName = encodeNgModelName(MasterPropTypeEnum.valueOf(masterProperty.getProp_type()),
				masterProperty.getProp_id());
		List<PropertyOption> options = masterProperty.getPropertyOptions();
		if (!isMulti) {
			
			buildRulesView(masterProperty, htmlStringBuilder);
			for (PropertyOption propertyOption : options) {

				String ng_model = path + "." + singleCheckModelName;
				htmlStringBuilder.append("<label>");
				htmlStringBuilder.append("<input type=\"radio\" ");
				htmlStringBuilder.append("ng-model=\"" + ng_model + "\" ");
				htmlStringBuilder.append("name=\"" + singleCheckModelName + "\"  ");
				htmlStringBuilder.append("value=\"" + propertyOption.getProp_option_value() + "\" ");
				htmlStringBuilder.append("/>");
				htmlStringBuilder.append(propertyOption.getProp_option_name() + "&nbsp;&nbsp;");
				htmlStringBuilder.append("</label>");
			}

			bulidFieldSet(htmlStringBuilder, masterProperty);

		} else {

			String ng_model = path + "." + singleCheckModelName;
			StringBuilder temBuilder = new StringBuilder();
			for (PropertyOption propertyOption : options) {
				temBuilder.append("<option value=\"" + propertyOption.getProp_option_value() + "\">");
				temBuilder.append(propertyOption.getProp_option_name());
				temBuilder.append("</option>");
			}

			htmlStringBuilder.append("<td>");
			htmlStringBuilder.append("<select ng-model=\"" + ng_model + "\">");
			htmlStringBuilder.append(temBuilder.toString());
			htmlStringBuilder.append("</select>");
			htmlStringBuilder.append("</td>");
			isMulti = false;
		}

		return htmlStringBuilder.toString();

	}

	/**
	 * 创建textInput视图.
	 * @param masterProperty
	 * @param isMulti
	 * @param path
	 * @return
	 */
	private String buildInputView(MasterProperty masterProperty, boolean isMulti, String path) {
		StringBuilder htmlStringBuilder = new StringBuilder();
		String inputModelName = encodeNgModelName(MasterPropTypeEnum.valueOf(masterProperty.getProp_type()),
				masterProperty.getProp_id());

		if (!isMulti) {
			String ng_model = path + "." + inputModelName;
			htmlStringBuilder.append("<fieldset id=\"" + masterProperty.getProp_id() + "\">");
			if (masterProperty.getIs_top_prop()==1) {
				String fieldSetTitle = "<legend>" + masterProperty.getProp_name() + "</legend>";
				htmlStringBuilder.append(fieldSetTitle);
			}
			buildRulesView(masterProperty, htmlStringBuilder);
			htmlStringBuilder.append("<label>");
			htmlStringBuilder.append(masterProperty.getProp_name() + ":&nbsp;");
			htmlStringBuilder.append("</label>");
			htmlStringBuilder.append("<input type=\"text\" ");
			htmlStringBuilder.append("ng-model=\"" + ng_model + "\" ");
			htmlStringBuilder.append("ng-click=\"setValuePopup(this,'"+ng_model+"')\"");
			htmlStringBuilder.append("ng-readonly=\"true\"");
			htmlStringBuilder.append("/>");
			htmlStringBuilder.append("</fieldset>");

		} else {
			String ng_model = path + "." + inputModelName;
			htmlStringBuilder.append("<td>");
			htmlStringBuilder.append("<input type=\"text\" ");
			htmlStringBuilder.append("ng-model=\"" + ng_model + "\" ");
			htmlStringBuilder.append("ng-click=\"setValuePopup(this,'"+ng_model+"')\"");
			htmlStringBuilder.append("ng-readonly=\"true\"");
			htmlStringBuilder.append("/>");
			htmlStringBuilder.append("</td>");

		}
		isMulti = false;
		return htmlStringBuilder.toString();
	}

	/**
	 * 构造FieldSet.
	 * 
	 * @param htmlStringBuilder
	 * @param masterProperty
	 */
	private void bulidFieldSet(StringBuilder htmlStringBuilder, MasterProperty masterProperty) {

		String startFieldSet = "<fieldset id=\"" + masterProperty.getProp_id() + "\">";
		String fieldSetTitle = "<legend>" + masterProperty.getProp_name() + "</legend>";
		htmlStringBuilder.insert(0, startFieldSet + fieldSetTitle);
		htmlStringBuilder.append("</fieldset>");
	}

	/**
	 * 组装规则标签.
	 * 
	 * @param masterProperty
	 * @param htmlStringBuilder
	 */
	private void buildRulesView(MasterProperty masterProperty, StringBuilder htmlStringBuilder) {
		
		StringBuilder ruleView = new StringBuilder();

		List<PropertyRule> rules = masterProperty.getRules();

		if (rules != null && rules.size() > 0) {
			ruleView.append("<div>");
			ruleView.append("<h5>填写规则:</h5>");
			ruleView.append("<ul>");
			for (int i = 0; i < rules.size(); i++) {
				
				PropertyRule rule = rules.get(i);
				
				ruleView.append("<li>");
				if (rule.getProp_rule_name().contains("minValueRule")) {
					ruleView.append(rule.getProp_rule_name() + "(" + rule.getProp_rule_value() + ")");
				}else if (rule.getProp_rule_name().contains("maxValueRule")) {
					ruleView.append(rule.getProp_rule_name() + "(" + rule.getProp_rule_value() + ")");
				}else if (rule.getProp_rule_name().contains("disableRule")) {
					ruleView.append(rule.getProp_rule_name() + "(" + rule.getProp_rule_value() + "), "+rule.getProp_rule_relationship()+": "+rule.getProp_rule_relationship_operator());
				}else if (rule.getProp_rule_name().contains("minInputNumRule")) {
					ruleView.append(rule.getProp_rule_name() + "(" + rule.getProp_rule_value() + ")");
				}else if (rule.getProp_rule_name().contains("maxInputNumRule")) {
					ruleView.append(rule.getProp_rule_name() + "(" + rule.getProp_rule_value() + ")");
				}else if (rule.getProp_rule_name().contains("regexRule")) {
					ruleView.append(rule.getProp_rule_name() + "(" + rule.getProp_rule_value() + ")");
				}else if (rule.getProp_rule_name().contains("tipRule")) {
					ruleView.append(rule.getProp_rule_value());
				}else if (rule.getProp_rule_name().contains("devTipRule")) {
					ruleView.append(rule.getProp_rule_value());
				}else if (rule.getProp_rule_name().contains("minLengthRule")) {
					ruleView.append(rule.getProp_rule_name() + "(" + rule.getProp_rule_value() + ")");
				}else if (rule.getProp_rule_name().contains("maxLengthRule")) {
					ruleView.append(rule.getProp_rule_name() + "(" + rule.getProp_rule_value() + ")");
				}else if (rule.getProp_rule_name().contains("maxTargetSizeRule")) {
					ruleView.append(rule.getProp_rule_name() + "(" + rule.getProp_rule_value() + ")");
				}else if (rule.getProp_rule_name().contains("minImageSizeRule")) {
					ruleView.append(rule.getProp_rule_name() + "(" + rule.getProp_rule_value() + ")");
				}else if (rule.getProp_rule_name().contains("maxImageSizeRule")) {
					ruleView.append(rule.getProp_rule_name() + "(" + rule.getProp_rule_value() + ")");
				}else if (rule.getProp_rule_name().contains("readOnlyRule")) {
					ruleView.append(rule.getProp_rule_name() + "(" + rule.getProp_rule_value() + ")");
				}else if (rule.getProp_rule_name().contains("requiredRule")) {
					if ("true".equals(rule.getProp_rule_value())) {
						ruleView.append("必须填写");
					}
				}else if (rule.getProp_rule_name().contains("valueTypeRule")) {
					ruleView.append(rule.getProp_rule_name() + "(" + rule.getProp_rule_value() + ")");
				}else if (rule.getProp_rule_name().matches("^\\d+.*")) {
					ruleView.append(rule.getProp_rule_value());
				}
				
				ruleView.append("</li>");
				
			}
			ruleView.append("</ul>");
			ruleView.append("</div>");
			htmlStringBuilder.append(ruleView);
			
		}

	}

	/**
	 * 把属性规则装配到属性中.
	 * 
	 * @param masterProperties
	 * @param propertyOptions
	 */
	private void setPropertyOptions(List<MasterProperty> masterProperties, List<PropertyOption> propertyOptions) {

		for (MasterProperty masterProperty : masterProperties) {
			List<PropertyOption> resOptions = new ArrayList<PropertyOption>();
			if (masterProperty.getProp_type() == 2 || masterProperty.getProp_type() == 3) {
				for (int i = 0; i < propertyOptions.size(); i++) {
					PropertyOption propertyOption = propertyOptions.get(i);
					if (propertyOption.getProp_id() == masterProperty.getProp_id()) {
						resOptions.add(propertyOption);
					}
				}

				masterProperty.setPropertyOptions(resOptions);

				propertyOptions.removeAll(resOptions);
			}

		}

	}

	/**
	 * 把属性规则装配到属性中.
	 * @param masterProperties
	 * @param propertyRules
	 * @return
	 */
	private List<MasterProperty> setPropertyRules(List<MasterProperty> masterProperties,
			List<PropertyRule> propertyRules) {

		for (MasterProperty masterProperty : masterProperties) {
			List<PropertyRule> rules = new ArrayList<PropertyRule>();
			for (int i = 0; i < propertyRules.size(); i++) {
				PropertyRule propertyRule = propertyRules.get(i);
				if (propertyRule.getProp_id() == masterProperty.getProp_id()) {
					rules.add(propertyRule);
				}
			}
			masterProperty.setRules(rules);
		}

		propertyRules.removeAll(propertyRules);

		return masterProperties;

	}

	private String encodeNgModelName(MasterPropTypeEnum propType, int propId) {
		String seperator = "_";
		return propType.toString() + seperator + propId;
	}

	private Map.Entry<MasterPropTypeEnum, String> decodeNgModelName(String encodeStr) {
		String seperator = "_";
		String[] strs = encodeStr.split(seperator);
		Map.Entry<MasterPropTypeEnum, String> typeMap = new AbstractMap.SimpleEntry(
				MasterPropTypeEnum.valueOf(strs[0]), strs[1]);
		return typeMap;
	}
	
	/**
	 * 获取产品图片列表.
	 * @param level
	 * @param levelValue
	 * @param channelId
	 * @return
	 */
	private List<Map<String, Object>>getModelImageNames(String level,String levelValue, String channelId) {
		List<Map<String, Object>> imageMapList = new ArrayList<Map<String, Object>>();
		if ("2".equals(level)) {
			HashMap<String, Object> paramData = new HashMap<String, Object>();
			paramData.put("modelId", levelValue);
			paramData.put("channelId", channelId);
			List<ModelProductCNBean> modelProductCNBeans = modelDao.doGetCNProductList(paramData);
			
			for (ModelProductCNBean modelProductCNBean : modelProductCNBeans) {
				Map<String, Object> imagemap = new HashMap<>();
				imagemap.put("imageName", modelProductCNBean.getProductImgUrl());
				imageMapList.add(imagemap);
			}
			
		} else if ("3".equals(level)) {
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("productId", levelValue);
			data.put("channelId", channelId);
			List<ProductImage> productImages = productDao.doGetProductImage(data);
			if (productImages!=null&&productImages.size()>0) {
				Map<String, Object> imagemap = new HashMap<>();
				imagemap.put("imageName", productImages.get(0).getImageName());
				imageMapList.add(imagemap);
			}
		}
		
		
		return imageMapList;
	}
	
}
