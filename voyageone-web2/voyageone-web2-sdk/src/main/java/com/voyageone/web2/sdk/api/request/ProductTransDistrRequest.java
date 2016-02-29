package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductTransDistrResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;


/**
 * /product/translate/distribute Request Model
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductTransDistrRequest extends VoApiRequest<ProductTransDistrResponse> {

	public String getApiURLPath() {
		return "/product/translate/distribute";
	}

	/**
	 * channelId
	 */
	private String channelId;

	/**
	 * translator
	 */
	private String translator;

	/**
	 * 任务分发策略，0:默认分发全部未未分配的商品，1:只对主商品进行分配.
	 */
	private int distributeRule = 0;

	/**
	 * translateTimeHDiff
	 */
	private int translateTimeHDiff = 4;

	/**
	 * limit
	 */
	private int limit = 10;


	public ProductTransDistrRequest() {}

	public ProductTransDistrRequest(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty(" channelId", channelId);
		RequestUtils.checkNotEmpty(" fields", fields);
		RequestUtils.checkNotEmpty(" translator", translator);
		RequestUtils.checkNotEmpty(" translateTimeHDiff", translateTimeHDiff);
		RequestUtils.checkNotEmpty(" limit", limit);
	}

	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getTranslator() {
		return translator;
	}

	public void setTranslator(String translator) {
		this.translator = translator;
	}

	public int getTranslateTimeHDiff() {
		return translateTimeHDiff;
	}

	public void setTranslateTimeHDiff(int translateTimeHDiff) {
		this.translateTimeHDiff = translateTimeHDiff;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getDistributeRule() {
		return distributeRule;
	}

	public void setDistributeRule(int distributeRule) {
		this.distributeRule = distributeRule;
	}
}