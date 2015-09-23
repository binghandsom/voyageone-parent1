package com.voyageone.cms.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.cms.dao.FeedDefaultPropSettingDao;
import com.voyageone.cms.formbean.FeedDefaultPropBean;
import com.voyageone.cms.formbean.FeedDefaultPropOptionBean;
import com.voyageone.cms.modelbean.FeedDefaultPropModel;
import com.voyageone.cms.modelbean.FeedDefaultPropOptionModel;
import com.voyageone.cms.service.FeedDefaultPropSettingService;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.ims.rule_expression.TextWord;

@Service
public class FeedDefaultPropSettingServiceImpl implements FeedDefaultPropSettingService {
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(FeedDefaultPropSettingService.class);

	@Autowired
	FeedDefaultPropSettingDao feedDefaultPropSettingDao;

	private boolean isUpdate;

	private RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
	
	/**
	 * 获取当前渠道的默认属性及值.
	 * 
	 * @param channelId
	 * @return
	 */
	@Override
	public List<FeedDefaultPropBean> getFeedProps(String channelId) {

		List<FeedDefaultPropModel> props = feedDefaultPropSettingDao.getFeedDefaultProps(channelId);
		List<FeedDefaultPropOptionModel> options = feedDefaultPropSettingDao.getFeedDefaultPropOptions();

		isUpdate = feedDefaultPropSettingDao.isExist(channelId);

		return buildFeedPropBeans(props, options);
	}

	/**
	 * 保存当前渠道的默认属性及默认值.
	 * 
	 * @param isUpdate
	 * @param channelId
	 * @param feedDefaultPropBeans
	 * @return
	 */
	@Override
	public int submit(List<FeedDefaultPropBean> feedDefaultPropBeans,UserSessionBean userSession) {

		List<FeedDefaultPropModel> models = new ArrayList<>();

		for (FeedDefaultPropBean propBean : feedDefaultPropBeans) {
			if (propBean.getPropValue()!=null && !"".equals(propBean.getPropValue().trim())) {
				FeedDefaultPropModel model = new FeedDefaultPropModel();
				RuleExpression ruleExpression = new RuleExpression();
				BeanUtils.copyProperties(propBean, model);
				switch (model.getPropType()) {
				case 2:
				case 3:
					ruleExpression.addRuleWord(new TextWord(model.getPropValue()));
					String encodePropValue = ruleJsonMapper.serializeRuleExpression(ruleExpression);
					model.setPropValue(encodePropValue);
					
					break;

				default:
					break;
				}
				model.setChannelId(userSession.getSelChannel());
				model.setCreater(userSession.getUserName());
				model.setModifier(userSession.getUserName());
				models.add(model);
			}
		}
		// 删除已有数据.
		feedDefaultPropSettingDao.delete(userSession.getSelChannel());
		// 保存数据
		return feedDefaultPropSettingDao.save(models);
	}

	private List<FeedDefaultPropBean> buildFeedPropBeans(List<FeedDefaultPropModel> props,
			List<FeedDefaultPropOptionModel> options) {

		List<FeedDefaultPropBean> propBeans = new ArrayList<>();
		
		for (Iterator propIter = props.iterator(); propIter.hasNext();) {

			FeedDefaultPropBean propBean = new FeedDefaultPropBean();

			List<FeedDefaultPropOptionBean> optionBeans = new ArrayList<>();

			FeedDefaultPropModel propModel = (FeedDefaultPropModel) propIter.next();

			BeanUtils.copyProperties(propModel, propBean);

			switch (propBean.getPropType()) {
			case 2:
			case 3:
				if (propBean.getPropValue() != null && !"".equals(propBean.getPropValue().trim())) {
					TextWord textWord = (TextWord) ruleJsonMapper.deserializeRuleExpression(propBean.getPropValue())
							.getRuleWordList().get(0);
					propBean.setPropValue(textWord.getValue());
				}
				break;

			default:
				break;

			}

			for (Iterator optionIter = options.iterator(); optionIter.hasNext();) {
				FeedDefaultPropOptionModel optionModel = (FeedDefaultPropOptionModel) optionIter.next();

				if (propModel.getPropName().equals(optionModel.getPropName())) {
					FeedDefaultPropOptionBean optionBean = new FeedDefaultPropOptionBean();
					BeanUtils.copyProperties(optionModel, optionBean);
					optionBeans.add(optionBean);
					optionIter.remove();
				}

			}
			propBean.setOptions(optionBeans);
			propBeans.add(propBean);
		}
		return propBeans;
	}

	@Override
	public boolean isUpdate() {
		return isUpdate;
	}

}
