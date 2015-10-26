package com.voyageone.cms.service.impl;

import com.voyageone.cms.dao.MasterCatComPropBatchUpdateDao;
import com.voyageone.cms.formbean.MasterCatPropBatchUpdateBean;
import com.voyageone.cms.formbean.MasterCategoryPropBean;
import com.voyageone.cms.modelbean.PropertyValue;
import com.voyageone.cms.service.MasterCatComPropBatchUpdateService;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.ims.enums.MasterPropTypeEnum;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.ims.rule_expression.TextWord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MasterCatComPropBatchUpdateServiceImpl implements MasterCatComPropBatchUpdateService {
	
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(MasterCatComPropBatchUpdateServiceImpl.class);

	@Autowired
	MasterCatComPropBatchUpdateDao masterCatComPropBatchUpdateDao;

	/**
	 * 获取需要批量更新的主属性的选项值.
	 * @param PropName
	 * @return
	 */
	@Override
	public List<Map<String,String>> getPropOptions(String PropName) {
		return masterCatComPropBatchUpdateDao.getComPropOptions(PropName);
	}

	/**
	 * 批量更新属性值.
	 * @param formData
	 * @param userSession
	 * @return
	 */
	@Override
	@Transactional
	public boolean batchUpdate(MasterCatPropBatchUpdateBean formData, UserSessionBean userSession) {

		boolean isSuccess =false;

		RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();

		//需要更新的主属性.
		MasterCategoryPropBean masterProperty = formData.getMasterProperty();

		//需要更新的model列表.
		List<String> modelIdList = formData.getTarModelList();

		//需要更新的从属性列表.
		List<MasterCategoryPropBean> propBeans = formData.getProperties();

		//需要删除的从属性列表.
		List<MasterCategoryPropBean> delPropBeans = formData.getDelProperties();

		//格式化主属性属性值.
		RuleExpression ruleExpression = new RuleExpression();
		ruleExpression.addRuleWord(new TextWord(masterProperty.getPropValue()));
		String masterEncodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);


		//更新主属性列表.
		masterCatComPropBatchUpdateDao.batchUpdate(modelIdList, masterProperty.getPropName(), userSession.getSelChannel(), 2, masterEncodePropValue, userSession.getUserName());
		//验证主属性的值是否已经存在.
		List<Map<String,String>> verifyPropList = masterCatComPropBatchUpdateDao.verifyPropValueExist(masterProperty.getPropName(), modelIdList, null, userSession.getSelChannel(), 2, null);
		//如果不存在就插入.
		for (Map<String,String> propMap:verifyPropList){
			if(propMap.get("valPropId")==null){
				PropertyValue value = new PropertyValue();

				value.setChannel_id(userSession.getSelChannel());
				value.setLevel(2);
				value.setLevel_value(String.valueOf(propMap.get("modelId")));
				value.setParent(null);
				value.setProp_id(Integer.valueOf(String.valueOf(propMap.get("propId"))));
				value.setUuid(UUID.randomUUID().toString().replace("-", ""));
				value.setProp_value(masterEncodePropValue);
				value.setCreater(userSession.getUserName());
				value.setModifier(userSession.getUserName());
				masterCatComPropBatchUpdateDao.addPropValue(value);
			}
		}

		//删除需要删除的从属性的属性值.
		if(delPropBeans.size()>0){
			for (MasterCategoryPropBean delBean:delPropBeans){
				for (Map<String,String> propMap:verifyPropList){
					String propName = delBean.getPropName();
					String modelId = String.valueOf(propMap.get("modelId"));
					String channelId = userSession.getSelChannel();
					String propId = String.valueOf(String.valueOf(propMap.get("propId")));
					List<Map<String,String>> delPropList = masterCatComPropBatchUpdateDao.verifyPropValueExist(propName,null,modelId,channelId,2,propId);
					if(delPropList.size()>0){
						for (Map<String,String> delMap:delPropList){
							if(delMap.get("valPropId")!=null)
								masterCatComPropBatchUpdateDao.removePorpValue(String.valueOf(delMap.get("valPropId")),userSession.getSelChannel(),2,String.valueOf(delMap.get("modelId")));
						}
					}
				}
			}
		}


		//更新从属性列表.
		for (MasterCategoryPropBean propBean:propBeans){
			RuleExpression updRuleExpression = new RuleExpression();
			updRuleExpression.addRuleWord(new TextWord(propBean.getPropValue()));
			String updEncodePropValue = ruleJsonMapper.serializeRuleExpression(updRuleExpression);
			for (Map<String,String> propMap:verifyPropList){
				List<Map<String,String>> updatePropList = masterCatComPropBatchUpdateDao.verifyPropValueExist(propBean.getPropName(),null,String.valueOf(propMap.get("modelId")),userSession.getSelChannel(),2,String.valueOf(propMap.get("propId")));
				if(updatePropList.size()>0){
					for (Map<String,String> updMap:updatePropList){

						if(updMap.get("valPropId")==null){

							PropertyValue subValue = new PropertyValue();

							subValue.setChannel_id(userSession.getSelChannel());
							subValue.setLevel(2);
							subValue.setLevel_value(String.valueOf(updMap.get("modelId")));
							subValue.setParent(null);
							subValue.setProp_id(Integer.valueOf(String.valueOf(updMap.get("propId"))));
							subValue.setUuid(UUID.randomUUID().toString().replace("-", ""));
							subValue.setProp_value(updEncodePropValue);
							subValue.setCreater(userSession.getUserName());
							subValue.setModifier(userSession.getUserName());

							//插入.
							masterCatComPropBatchUpdateDao.addPropValue(subValue);

							isSuccess=true;
						}else {
							//更新.
							masterCatComPropBatchUpdateDao.updateSubPropValue(updEncodePropValue,userSession.getUserName(),String.valueOf(updMap.get("valPropId")),userSession.getSelChannel(),2,String.valueOf(updMap.get("modelId")));

							isSuccess=true;
						}

					}
				}
			}
		}

		//更新产品状态.
		if(modelIdList.size()>0)
			masterCatComPropBatchUpdateDao.updateProductPublishStatus(modelIdList,userSession.getSelChannel());

		return isSuccess;
	}
}
