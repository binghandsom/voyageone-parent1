package com.voyageone.web2.admin.views.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.service.bean.com.ComMtValueChannelBean;
import com.voyageone.service.impl.com.channel.ChannelAttributeService;
import com.voyageone.service.model.com.PageModel;
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
		PageModel<ComMtValueChannelBean> channelAttrPage = channelAttributeService.searchChannelAttributeByPage(
				form.getChannelId(), form.getTypeId(), form.getLangId(), form.getName(), form.getValue(),
				form.getPageNum(), form.getPageSize());
		
		return success(channelAttrPage);
	}

}
