package com.voyageone.web2.admin.views.channel;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.service.bean.com.ComMtValueChannelBean;
import com.voyageone.service.impl.com.channel.ChannelAttributeService;
import com.voyageone.service.model.com.ComMtValueChannelModel;
import com.voyageone.service.bean.com.PaginationResultBean;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.channel.ChannelAttributeFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/9
 */
@RestController
@RequestMapping(value = AdminUrlConstants.Channel.Attribute.ROOT, method = RequestMethod.POST)
public class ChannelAttributeController extends AdminController {
	
	@Autowired
	private ChannelAttributeService channelAttributeService;
	
	@RequestMapping(AdminUrlConstants.Channel.Attribute.SEARCH_CHANNEL_ATTRIBUTE_BY_PAGE)
	public AjaxResponse searchChannelAttributeByPage(@RequestBody ChannelAttributeFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索渠道属性信息
		PaginationResultBean<ComMtValueChannelBean> channelAttrPage = channelAttributeService.searchChannelAttributeByPage(
				form.getChannelId(), form.getTypeId(), form.getLangId(), form.getName(), form.getValue(),
				form.getActive(), form.getPageNum(), form.getPageSize());
		
		return success(channelAttrPage);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Attribute.ADD_CHANNEL_ATTRIBUTE)
	public AjaxResponse addChannelAttribute(@RequestBody ChannelAttributeFormBean form) {
		return addOrUpdateChannelAttribute(form, true);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Attribute.UPDATE_CHANNEL_ATTRIBUTE)
	public AjaxResponse updateChannelAttribute(@RequestBody ChannelAttributeFormBean form) {
		Preconditions.checkNotNull(form.getId());
		return addOrUpdateChannelAttribute(form, false);
	}
	
	public AjaxResponse addOrUpdateChannelAttribute(ChannelAttributeFormBean form, boolean append) {
		// 验证参数
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getChannelId()));
		Preconditions.checkNotNull(form.getTypeId());
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getName()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getValue()));

		// 保存渠道属性信息
		ComMtValueChannelModel model = new ComMtValueChannelModel();
		BeanUtils.copyProperties(form, model);
		channelAttributeService.addOrUpdateChannelAttribute(model, getUser().getUserName(), append);
		
		return success(true);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Attribute.DELETE_CHANNEL_ATTRIBUTE)
	public AjaxResponse deleteChannelAttribute(@RequestBody Integer[] channelAttrIds) {
		// 验证参数
		Preconditions.checkArgument(ArrayUtils.isNotEmpty(channelAttrIds));
		// 删除渠道属性信息
		channelAttributeService.deleteChannelAttribute(Arrays.asList(channelAttrIds), getUser().getUserName());
		
		return success(true);
	}

}
