package com.voyageone.web2.cms.wsdl.control;

import com.voyageone.web2.cms.wsdl.BaseController;
import com.voyageone.web2.cms.wsdl.service.FeedMappingService;
import com.voyageone.web2.sdk.api.request.FeedMappingsGetRequest;
import com.voyageone.web2.sdk.api.response.FeedMappingsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * FeedMapping Controller
 *
 * @author chuanyu.liang 16/01/14
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping(value = "/rest/feedMapping", method = RequestMethod.POST)
public class FeedMappingController extends BaseController {

	@Autowired
	private FeedMappingService feedMappingService;

	/**
	 * 根据条件查询
	 * 
	 * @param request Request
	 * @return FeedMappingsGetResponse
	 */
	@RequestMapping("get")
	public FeedMappingsGetResponse get(@RequestBody FeedMappingsGetRequest request) {
		return feedMappingService.getList(request);
	}

}
