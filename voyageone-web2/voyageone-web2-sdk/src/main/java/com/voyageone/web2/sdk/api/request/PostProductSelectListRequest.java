package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiListRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PostProductSelectListResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * /puroduct/selectList Request Model
 *
 * Product的id.两种方式来查看一个产品:1.传入productId List来查询 2.传入productCode list来查询 3:传入props来查询
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class PostProductSelectListRequest extends VoApiListRequest<PostProductSelectListResponse> {

	public String getApiURLPath() {
		return "/puroduct/selectList";
	}

	/**
	 * channelId
	 */
	private String channelId;

	/**
	 * productId
	 */
	private List<Long> productIdList = new ArrayList<>();

	/**
	 * Product的Code
	 */
	private List<String> productCodeList = new ArrayList<>();

	/**
	 * 比如:诺基亚N73这个产品的关键属性列表就是:品牌:诺基亚,型号:N73,对应的PV值就是10005:10027;10006:29729.
	 *
	 * 例如 获取model所属所有商品
	 *
	 */
	private String props;

	public PostProductSelectListRequest() {}

	public PostProductSelectListRequest(String channelId) {
		this.channelId = channelId;
	}

	public void check() throws ApiRuleException {
		RequestUtils.checkNotEmpty(channelId);
		RequestUtils.checkNotEmpty(" productIdList or productCodeList or props", productIdList, productCodeList, props);
		RequestUtils.checkNotEmpty(fields, "fields");
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public List<Long> getProductIdList() {
		return productIdList;
	}
	public void setProductIdList(List<Long> productIdList) {
		this.productIdList = productIdList;
	}

	public List<String> getProductCodeList() {
		return productCodeList;
	}
	public void setProductCodeList(List<String> productCodeList) {
		this.productCodeList = productCodeList;
	}

	public String getProps() {
		return props;
	}
	public void setProps(String props) {
		this.props = props;
	}

	public void addProp(String key, Object value) {
		this.props = RequestUtils.addProp(props, key, value);
	}
}