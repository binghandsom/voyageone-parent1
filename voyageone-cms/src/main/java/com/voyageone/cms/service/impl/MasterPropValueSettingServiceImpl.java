package com.voyageone.cms.service.impl;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.voyageone.cms.service.CommonService;
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
import com.voyageone.cms.utils.RuleDecodeUtil;
import com.voyageone.common.bussiness.platformInfo.dao.PlatformInfoDao;
import com.voyageone.common.bussiness.platformInfo.model.PlatformInfoModel;
import com.voyageone.common.configs.ImsCategoryConfigs;
import com.voyageone.common.configs.beans.ImsCategoryBean;
import com.voyageone.core.modelbean.UserSessionBean;
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
	private PlatformInfoDao platformInfoDao;

	@Autowired
	CommonService commonService;
	
	private UserSessionBean userSession;
	
	/** type **/
	private final String channelIdKey = "channelId";
	private final String levelKey = "level";
	private final String levelValueKey = "levelValue";
	private final String hiddenInfoKey = "hiddenInfo";
	private final String errMsgKey = "errorMessage";
	private final String errMsgMapKey = "errMsgObj";

	public RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();

	/**
	 * 初始化处理.
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	@Override
	public Object init(MasterPropertyFormBean formData) throws NumberFormatException, IOException {

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
				
				return 0;
				
//				viewModelMap = this.buildResposeDataMap(Integer.valueOf(categoryId), masterProperties,
//						null);
			}
			if (viewModelMap != null) {
				responseMap.put("propModels", viewModelMap);
			}

			//设置当前类目名称
			ImsCategoryBean  mtCateBean = ImsCategoryConfigs.getMtCategoryBeanById(Integer.valueOf(categoryId));
			responseMap.put("currentCategoryName", mtCateBean.getCategoryPath());
			
			List<PlatformInfoModel> platformInfo = platformInfoDao.getPlatformInfo(Integer.valueOf(categoryId));
			responseMap.put("platformInfo", platformInfo);
		}

		//设定产品图片
		List<Map<String, Object>> images = this.getModelImageNames(level, levelValue, channelId);
		responseMap.put("images", images);
		
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
	 * 获取类目菜单列表.
	 */
	@Override
	public Object getCategoryNav(UserSessionBean userSession) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		//设置顶层所有类目
		List<ImsCategoryBean> topCategories = commonService.getChannelCategories(userSession.getSelChannel());
		responseMap.put("categories", topCategories);
		
		return responseMap;
	}

	/**
	 * 查询处理.
	 * @throws IOException 
	 */
	@Override
	public Object search(MasterPropertyFormBean formData) throws IOException {
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
			String errMsg="请选择主类目!";
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
			responseMap.put("propModels", viewModelMap);
			//设定产品图片
			List<Map<String, Object>> images = this.getModelImageNames(String.valueOf(level), levelValue, channelId);
			responseMap.put("images", images);
		}
		//设置当前类目名称
		ImsCategoryBean  mtCateBean = ImsCategoryConfigs.getMtCategoryBeanById(categoryId);
		responseMap.put("currentCategoryName", mtCateBean.getCategoryPath());
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
	public boolean submit(MasterPropertyFormBean formData,UserSessionBean userSession) {
		logger.info("提交处理开始...");
		this.userSession = userSession;
		Map<String, Object> modelMap = formData.getPropModels();
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
	 * 清空切换后类目的属性值.
	 * @param channelId
	 * @param level
	 * @param levelValue
	 */
	@Override
	@Transactional("transactionManager")
	public boolean switchCagetgory(String channelId, int level, String levelValue,UserSessionBean userSession) {
		
		//删除属性值
		int delCount = masterPropValueSettingDao.delete(channelId, level, levelValue);
		//更新dealFlag
		masterPropValueSettingDao.updateDealFlag(channelId, level, levelValue, userSession.getUserName());
		
		if (delCount>0) {
			
			return true;
		}
		
		return false;
	}

	/**
	 * 构建responseData.
	 * 
	 * @param categoryId
	 * @param updatePropertyValues
	 * @return
	 * @throws IOException 
	 */
	private Map<String, Object> buildResposeDataMap(int categoryId, List<MasterProperty> masterProperties,
			List<PropertyValue> updatePropertyValues) throws IOException {
		logger.debug("buildResposeDataMap() start:"+System.currentTimeMillis());
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (masterProperties != null && masterProperties.size() > 0) {

			// 获取属性选项列表.
			List<PropertyOption> propertyOptions = masterPropValueSettingDao.getPropertyOptions(categoryId);
			// 获取属性规则列表.
			List<PropertyRule> propertyRules = masterPropValueSettingDao.getPropertyRules(categoryId);
			//解码规则
			RuleDecodeUtil.decodeRule(propertyRules);
			// 装配属性选项列表.
			this.setPropOptionsAndRules(masterProperties, propertyOptions, propertyRules);

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

		}else {
			
		}

		return modelMap;

	}

	/**
	 * 把属性选项和属性输入规则装配到属性列表中.
	 * 
	 * @param masterProps
	 * @param propertyOptions
	 */
	private void setPropOptionsAndRules(List<MasterProperty> masterProps, List<PropertyOption> propertyOptions, List<PropertyRule> propertyRules) {

		for (MasterProperty prop : masterProps) {
			
			List<PropertyOption> resOptions = new ArrayList<PropertyOption>();
			
			List<PropertyRule> rules = new ArrayList<PropertyRule>();
			
			if (prop.getProp_type() == 2 || prop.getProp_type() == 3) {
				for (Iterator<PropertyOption> iter = propertyOptions.iterator();iter.hasNext();) {
					
					PropertyOption propertyOption = iter.next();
					
					if (propertyOption.getProp_id() == prop.getProp_id()) {
						resOptions.add(propertyOption);
						iter.remove();
					}
	
					prop.setPropertyOptions(resOptions);
				}

			}
			
			for (Iterator<PropertyRule> ruleIter = propertyRules.iterator();ruleIter.hasNext();) {
				PropertyRule propertyRule = ruleIter.next();
				if (propertyRule.getProp_id() == prop.getProp_id()) {
					rules.add(propertyRule);
					ruleIter.remove();
				}
			}
			prop.setRules(rules);

		}

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
				}else if (entry.getValue() instanceof List) {
					if ("items".equals(entry.getKey().toString())) {
						List<Map<String, Object>> subList = (List<Map<String,Object>>)valueModelMap.get(entry.getKey());
						List<Map<String, Object>> orgList = (List<Map<String,Object>>)entry.getValue();
						if (!subList.isEmpty()&&!orgList.isEmpty()) {
							Map<String, Object>  templateMap = orgList.get(0);
							for (Map<String, Object> map : subList) {
								synchronizeModelMap(templateMap,map);
							}
						}
						
					}
					
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
			MasterProperty prop = propIdPropMap.get(propertyValue.getProp_id());
			String key = null;
			if (prop.getIs_required()==1) {
				if (prop.getProp_type()==1) {
					key = "A_B_"+ encodeNgModelName(type, prop.getProp_id());
				}else {
					key = "A_"+ encodeNgModelName(type, prop.getProp_id());
				}
				 
			}else {
				 key = encodeNgModelName(type, prop.getProp_id());
			}
			if (prop.getProp_type()==2 && prop.getParent_prop_id()!=0) {
				if (key.startsWith("A_")) {
					key = key.replace("A_", "A_B_");
				}else {
					key = "B_"+ encodeNgModelName(type, prop.getProp_id());
				}
				 
			}
			List<PropertyOption> options = prop.getPropertyOptions();
			Map<String, Object> sunModel = new HashMap<>();
			sunModel.put("propType", type.getValue());
			sunModel.put("propName", prop.getProp_name());
			sunModel.put("propId", prop.getProp_id());
			sunModel.put("parentPropId", prop.getParent_prop_id());
			sunModel.put("require", prop.getIs_required());
			sunModel.put("rules", prop.getRules());
			switch (type) {
			case INPUT:
				sunModel.put("propValue", propertyValue.getProp_value());
				break;
			case SINGLECHECK:
				TextWord textWord = (TextWord) ruleJsonMapper.deserializeRuleExpression(propertyValue.getProp_value())
						.getRuleWordList().get(0);
				sunModel.put("propValue", textWord.getValue());
				sunModel.put("options", prop.getPropertyOptions());
				break;
			case MULTICHECK:
				
				// 设定默认值.
				List<PropertyValue> valueOptions = propertyValue.getValues();

				for (PropertyOption propertyOption : options) {
					for (PropertyValue selectedOption : valueOptions) {
						
						TextWord selectedValue = (TextWord) ruleJsonMapper
								.deserializeRuleExpression(selectedOption.getProp_value()).getRuleWordList().get(0);
						if (propertyOption.getProp_option_value().equals(selectedValue.getValue())) {
							propertyOption.setSelectedValue(selectedValue.getValue());
						}
						
					}
				}
				sunModel.put("options", prop.getPropertyOptions());
				break;
			case lABEL:
				sunModel.put("propValue", propertyValue.getProp_value());
				break;
			case COMPLEX:
				Map<String, Object> complexModelMap = new HashMap<String, Object>();
				buildUpdateModelMap(propertyValue.getValues(), complexModelMap, propIdPropMap);
				sunModel.put("children", complexModelMap);
				
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
				sunModel.put("items", complexModelMapList);
				break;

			default:
				break;
			}
			modelMap.put(key, sunModel);
		}

		return modelMap;
	}

	/**
	 * 创建model.
	 * 
	 * @param properties
	 * @param modelMap
	 * @return
	 * @throws IOException 
	 */
	private Map<String, Object> buildModelMap(List<MasterProperty> properties, Map<String, Object> modelMap,
			boolean isSetDefaultValue) {

		for (MasterProperty property : properties) {

			MasterPropTypeEnum type = MasterPropTypeEnum.valueOf(property.getProp_type());
			String key = null;
			if (property.getIs_required()==1) {
				if (property.getProp_type()==1) {
					key = "A_B_"+ encodeNgModelName(type, property.getProp_id());
				}else {
					key = "A_"+ encodeNgModelName(type, property.getProp_id());
				}
				 
			}else {
				 key = encodeNgModelName(type, property.getProp_id());
			}
			if (property.getProp_type()==2 && property.getParent_prop_id()!=0) {
				if (key.startsWith("A_")) {
					key = key.replace("A_", "A_B_");
				}else {
					key = "B_"+ encodeNgModelName(type, property.getProp_id());
				}
				 
			}
			Map<String, Object> sunModel = new HashMap<>();
			List<PropertyOption> options = property.getPropertyOptions();
			sunModel.put("propType", type.getValue());
			sunModel.put("propName", property.getProp_name());
			sunModel.put("propId", property.getProp_id());
			sunModel.put("parentPropId", property.getParent_prop_id());
			sunModel.put("require", property.getIs_required());
			sunModel.put("rules", property.getRules());
			switch (type) {
			case INPUT:
				if (isSetDefaultValue&&property.getProp_value_default()!=null&&!"".equals(property.getProp_value_default())) {
					RuleExpression ruleExpression = new RuleExpression();
					ruleExpression.addRuleWord(new TextWord(property.getProp_value_default()));
					String encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
					
					sunModel.put("propValue", encodePropValue);
					
				} else {
					sunModel.put("propValue", "");
				}
				break;
			case SINGLECHECK:
				if (isSetDefaultValue) {
					sunModel.put("propValue", property.getProp_value_default());
				} else {
					sunModel.put("propValue", "");
				}
				sunModel.put("options", options);
				break;
			case MULTICHECK:
				// 设定默认值.
				for (PropertyOption option : options) {
					if (isSetDefaultValue) {
						if (option.getProp_option_value().equals(property.getProp_value_default())) {
							option.setSelectedValue(option.getProp_option_value());
						}
					}
				}

				sunModel.put("options", options);
				break;
			case lABEL:
				if (property.getProp_value_default() != null){
					sunModel.put("propValue", property.getProp_value_default());
				}
				break;
			case COMPLEX:
				Map<String, Object> complexModelMap = new HashMap<String, Object>();
				buildModelMap(property.getProperties(), complexModelMap, isSetDefaultValue);
				sunModel.put("children", complexModelMap);
				break;
			case MULTICOMPLEX:
				List<Map<String, Object>> complexModelMapList = new ArrayList<Map<String, Object>>();
				Map<String, Object> complexMap = new HashMap<String, Object>();
				buildModelMap(property.getProperties(), complexMap, isSetDefaultValue);
				complexModelMapList.add(complexMap);
				sunModel.put("items", complexModelMapList);
				break;
			case MULTIINPUT:
				List<Map<String, Object>> inputModelMapList = new ArrayList<Map<String, Object>>();
				Map<String, Object> defaultInputMap = new HashMap<String, Object>();
				buildModelMap(property.getProperties(), defaultInputMap, isSetDefaultValue);
				inputModelMapList.add(defaultInputMap);
				sunModel.put("items", inputModelMapList);
				break;

			default:
				break;
			}
			modelMap.put(key, sunModel);
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
			if (entry.getValue() != null && entry.getValue() instanceof Map) {
				
				Map<String, Object> valueMap = (Map<String, Object>)entry.getValue();
				
				int propId = Integer.valueOf(valueMap.get("propId").toString());
				
				MasterPropTypeEnum type = MasterPropTypeEnum.valueOf(Integer.valueOf(valueMap.get("propType").toString()));
				
				switch (type) {
				case INPUT:
					String inputValue = (String) (valueMap.get("propValue"));
					if (inputValue != null && !"".equals(inputValue)) {
						propertyValues.add(constructInputProp(channelId, level, levelValue, propId, "", inputValue));
					}
					break;
				case SINGLECHECK:
					String singleCheckValue = (String) (valueMap.get("propValue"));
					if (singleCheckValue != null && !"".equals(singleCheckValue)) {
						propertyValues.add(constructSingleCheckBox(channelId, level, levelValue, propId, "",singleCheckValue));
					}
					break;
				case MULTICHECK:
					List<PropertyValue> multiCheckValues = constructMultiCheckBox(channelId, level, levelValue, propId,
							"", valueMap);
					if (multiCheckValues != null) {
						propertyValues.addAll(multiCheckValues);
					}

					break;
				case lABEL:
					String labelValue = (String) (valueMap.get("propValue"));
					if (labelValue != null && !"".equals(labelValue)) {
						propertyValues.add(constructInputProp(channelId, level, levelValue, propId, "", labelValue));
					}
					break;
				case COMPLEX:
					List<PropertyValue> complexValues = constructComplex(channelId, level, levelValue, propId,
							valueMap, "");
					if (complexValues != null) {
						propertyValues.addAll(complexValues);
					}

					break;
				case MULTICOMPLEX:
					List<PropertyValue> multiComplexValues = constructMultiComplex(channelId, level, levelValue, propId,
							(List<Map<String, Object>>) (valueMap.get("items")), "");
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
		singleCheckBoxProp.setCreater(this.userSession.getUserName());
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
		if (propValues.get("options") instanceof List) {
		
		List<Map<String, Object>> multiCheckMapList = (List<Map<String, Object>>)propValues.get("options");
		
		if (multiCheckMapList.size() > 0) {
			PropertyValue checkBoxParent = new PropertyValue();
			checkBoxParent.setChannel_id(channelId);
			checkBoxParent.setParent(parent);
			checkBoxParent.setProp_id(propId);
			checkBoxParent.setLevel(level);
			checkBoxParent.setLevel_value(levelValue);
			checkBoxParent.setCreater(this.userSession.getUserName());
			checkBoxParent.setUuid(UUID.randomUUID().toString().replace("-", ""));
			checkBoxParent.setProp_value(null);
			RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();

			for (Map<String, Object> map : multiCheckMapList) {
				if (map.get("selectedValue")!=null) {
					
					String propValue = map.get("selectedValue").toString();
				
					if (propValue != null && !"".equals(propValue)) {
						PropertyValue checkBox = new PropertyValue();
						checkBox.setChannel_id(channelId);
						checkBox.setParent(checkBoxParent.getUuid());
						checkBox.setProp_id(propId);
						checkBox.setLevel(level);
						checkBox.setLevel_value(levelValue);
						checkBox.setCreater(this.userSession.getUserName());
						RuleExpression ruleExpression = new RuleExpression();
						ruleExpression.addRuleWord(new TextWord(propValue));
						String encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
						checkBox.setProp_value(encodePropValue);
						checkBox.setUuid(UUID.randomUUID().toString().replace("-", ""));
						multiCheckBoxList.add(checkBox);
					}
				}
			}

			if (multiCheckBoxList.size() > 0) {
				multiCheckBoxList.add(checkBoxParent);
				return multiCheckBoxList;
			}

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
		parentComplexPropValue.setCreater(this.userSession.getUserName());
		parentComplexPropValue.setUuid(UUID.randomUUID().toString().replace("-", ""));
		parentComplexPropValue.setProp_value(null);
		
		if (propMap.get("children")==null || propMap.get("children") instanceof Map) {
			
			Map<String, Object> children=null;
			
			if (propMap.get("children")==null) {
				children = propMap;
			}else {
				children = (Map<String, Object>)propMap.get("children");
			}
		
			for (Map.Entry<String, Object> entry : children.entrySet()) {
				if (entry.getValue() != null && entry.getValue() instanceof Map) {
					
					Map<String, Object> valueMap = (Map<String, Object>)entry.getValue();
				
					MasterPropTypeEnum type = MasterPropTypeEnum.valueOf(Integer.valueOf(valueMap.get("propType").toString()));
					int subPropId =  Integer.valueOf(valueMap.get("propId").toString());
					switch (type) {
					case INPUT:
						String inputValue =  (String) (valueMap.get("propValue"));
						if (inputValue != null && !"".equals(inputValue)) {
							singleTypeList.add(constructInputProp(channelId, level, levelValue, subPropId,
									parentComplexPropValue.getUuid(), inputValue));
						}
		
						break;
					case SINGLECHECK:
						String singleCheckValue =  (String) (valueMap.get("propValue"));
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
								valueMap, parentComplexPropValue.getUuid());
						if (complexValues != null) {
							singleTypeList.addAll(complexValues);
						}
		
						break;
					case MULTICOMPLEX:
						List<PropertyValue> multiComplexValues = constructMultiComplex(channelId, level, levelValue, subPropId,
								(List<Map<String, Object>>) (valueMap.get("items")), parentComplexPropValue.getUuid());
						if (multiComplexValues != null) {
							singleTypeList.addAll(multiComplexValues);
						}
		
						break;
					}
				}
			}
			if (singleTypeList.size() > 0) {
				singleTypeList.add(parentComplexPropValue);
	
				return singleTypeList;
			}
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
		multiComplexPropValue.setCreater(this.userSession.getUserName());
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
	 * 创建层次关系.
	 * 
	 * @param masterProperties
	 */
	@Override
	public List<MasterProperty> buildPorpertyTrees(List<MasterProperty> masterProperties) {
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