package com.voyageone.web2.admin.views.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.service.bean.com.TmCarrierChannelBean;
import com.voyageone.service.impl.com.channel.CarrierConfigService;
import com.voyageone.service.bean.com.PaginationResultBean;
import com.voyageone.service.model.com.TmCarrierChannelKey;
import com.voyageone.service.model.com.TmCarrierChannelModel;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.channel.CarrierConfigFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/9
 */
@RestController
@RequestMapping(value = AdminUrlConstants.Channel.Carrier.ROOT, method = RequestMethod.POST)
public class CarrierConfigController extends AdminController {
	
	@Autowired
	private CarrierConfigService carrierConfigService;
	
	@SuppressWarnings("serial")
	@RequestMapping(AdminUrlConstants.Channel.Carrier.GET_ALL_CARRIER)
	public AjaxResponse getAllCarrier() {
		List<Map<String, Object>> carriers = carrierConfigService.getAllCarrier();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		carriers.stream().forEach(item -> result.add(new HashMap<String, Object>() {{
			put("carrier", item.get("carrier"));
		}}));
		return success(result);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Carrier.SEARCH_CARRIER_CONFIG_BY_PAGE)
	public AjaxResponse searchCarrierConfigByPage(@RequestBody CarrierConfigFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索渠道快递信息
		PaginationResultBean<TmCarrierChannelBean> carrierConfigPage = carrierConfigService.searchCarrierConfigByPage(
				form.getOrderChannelId(), form.getCarrier(), form.getUsekd100Flg(), form.getActive(),
				form.getPageNum(), form.getPageSize());
		
		return success(carrierConfigPage);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Carrier.ADD_CARRIER_CONFIG)
	public AjaxResponse addCarrierConfig(@RequestBody CarrierConfigFormBean form) {
		return addOrUpdateCarrierConfig(form, true);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Carrier.UPDATE_CARRIER_CONFIG)
	public AjaxResponse updateCarrierConfig(@RequestBody CarrierConfigFormBean form) {
		return addOrUpdateCarrierConfig(form, false);
	}
	
	public AjaxResponse addOrUpdateCarrierConfig(CarrierConfigFormBean form, boolean append) {
		// 验证参数
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getOrderChannelId()));
		Preconditions.checkNotNull(StringUtils.isNotBlank(form.getCarrier()));

		// 保存渠道快递信息
		TmCarrierChannelModel model = new TmCarrierChannelModel();
		BeanUtils.copyProperties(form, model);
		carrierConfigService.addOrUpdateCarrierConfig(model, getUser().getUserName(), append);
		
		return success(true);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Carrier.DELETE_CARRIER_CONFIG)
	public AjaxResponse deleteCarrierConfig(@RequestBody CarrierConfigFormBean[] forms) {
		// 验证参数
		List<TmCarrierChannelKey> carrierKeys = new ArrayList<TmCarrierChannelKey>();
		for (CarrierConfigFormBean form : forms) {
			Preconditions.checkArgument(StringUtils.isNotBlank(form.getOrderChannelId()));
			Preconditions.checkNotNull(StringUtils.isNotBlank(form.getCarrier()));
			TmCarrierChannelKey carrierKey = new TmCarrierChannelKey();
			carrierKey.setOrderChannelId(form.getOrderChannelId());
			carrierKey.setCarrier(form.getCarrier());
		}
		// 删除渠道快递信息
		carrierConfigService.deleteCarrierConfig(carrierKeys, getUser().getUserName());
		
		return success(true);
	}

}
