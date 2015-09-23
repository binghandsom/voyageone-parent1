package com.voyageone.cms.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.voyageone.cms.dao.PopUpMtPropSetValueDao;
import com.voyageone.cms.formbean.PopUpMtPropSetValueBean.Word;
import com.voyageone.cms.service.PopUpMtPropSetValueService;
import com.voyageone.ims.enums.CmsFieldEnum.CmsModelEnum;
import com.voyageone.ims.rule_expression.CmsWord;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.DictWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.TextWord;

@Component("propertyPopUpService")
public class PopUpMtPropSetValueServiceImpl implements PopUpMtPropSetValueService {
	
	public RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
	
	@Autowired
	PopUpMtPropSetValueDao popUpMtPropSetValueDao;
	
	@Override
	public Object doSerialize(List<Word> words) {
		
		Map<String, Object> responseMap = new HashMap<String, Object>();
		
		RuleExpression ruleExpression = new RuleExpression();
		
		for (Word word : words) {
			if (word.getWordValue()!=null&&!"".equals(word.getWordValue())) {
				switch (word.getWordType()) {
				case 1:
					ruleExpression.addRuleWord(new TextWord(word.getWordValue()));
					break;
				case 2:
					ruleExpression.addRuleWord(new CmsWord(CmsModelEnum.valueOf(word.getWordValue())));
					break;
				case 3:
					ruleExpression.addRuleWord(new DictWord(word.getWordValue()));
					break;
				case 4:
					ruleExpression.addRuleWord(new TextWord(word.getWordValue()));
					break;
				case 5:
					ruleExpression.addRuleWord(new TextWord(word.getWordValue()));
					break;
					

				default:
					break;
				}
			}
			
		}
		
		if (!ruleExpression.getRuleWordList().isEmpty()) {
			
			String encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
			
			responseMap.put("encodeValue", encodePropValue);
		}
		
		
		return responseMap;
	}

	@Override
	public Object doDeserialize(String wordValue,String... channelId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<Word> resWords = new ArrayList<Word>();
		if (wordValue!=null&&!"".equals(wordValue)) {
			RuleExpression expression = ruleJsonMapper.deserializeRuleExpression(wordValue);
			List<RuleWord> words = expression.getRuleWordList();
			for (RuleWord ruleWord : words) {
				Word word = new Word();
				word.setWordType(ruleWord.getWordType().ordinal()+1);
				if (ruleWord instanceof TextWord) {
					word.setWordValue(((TextWord) ruleWord).getValue());
				}else if (ruleWord instanceof CmsWord) {
					word.setWordValue(((CmsWord) ruleWord).getValue().toString());
					word.setComplexValues(this.buildComplexValues(word.getWordType()));
				}else if (ruleWord instanceof DictWord) {
					word.setWordValue(((DictWord) ruleWord).getName());
					word.setComplexValues(this.buildComplexValues(word.getWordType(),channelId));
				}else if (ruleWord instanceof CustomWord) {
					
				}
				
				resWords.add(word);
			}
		}
		
		responseMap.put("decodeValues", resWords);
		
		return responseMap;
	}

	@Override
	public Object doGetComplexValues(int wordType,String... channelId) {
		
		Map<String, Object> responseMap = new HashMap<String, Object>();
		
		List<Map<String, String>> complexValues = buildComplexValues(wordType,channelId);
		responseMap.put("complexValues", complexValues);
		return responseMap;
	}

	/**
	 * 
	 * @param wordType
	 * @return
	 */
	private List<Map<String, String>> buildComplexValues(int wordType,String... channelId) {
		List<Map<String, String>> complexValues=new ArrayList<Map<String, String>>();
		
		switch (wordType) {
		
		case 2:
			for(int i=0;i<CmsModelEnum.values().length;i++){
				CmsModelEnum cmsModel = CmsModelEnum.values()[i];
				Map<String, String> valueMap = new HashMap<String, String>();
				valueMap.put("value", cmsModel.toString());
				complexValues.add(valueMap);
			}
			break;
		case 3:
			List<String> dictConts = popUpMtPropSetValueDao.getDictContents(channelId[0]);
			for (String dict : dictConts) {
				Map<String, String> valueMap = new HashMap<String, String>();
				valueMap.put("value", dict);
				complexValues.add(valueMap);
			}
			break;
		case 4:
			break;
		case 5:
			break;

		default:
			break;
		}
		return complexValues;
	}

	
	

}
