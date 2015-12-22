package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.response.PostProductSelectListResponse;
import com.voyageone.web2.sdk.api.response.PostProductSelectOneResponse;

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
public class PostProductSelectListRequest extends VoApiRequest<PostProductSelectListResponse> {

	public String getApiURLPath() {
		return "/puroduct/selectList";
	}

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


	/**
	 * 页码.传入值为1代表第一页,传入值为2代表第二页,依此类推.默认返回的数据是从第一页开始.
	 */
	private Long pageNo;

	/**
	 * 每页条数.每页返回最多返回100条,默认值为40
	 */
	private Long pageSize;


	/**
	 * 需返回的字段列表.可选值:Product数据结构中的所有字段;多个字段之间用","分隔.
	 */
	private String fields;

	public PostProductSelectListRequest() {

	}

	public PostProductSelectListRequest(String channelId) {
		this.channelId = channelId;
	}

//	public void check() throws ApiRuleException {
//		RequestCheckUtils.checkNotEmpty(fields, "fields");
//	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}



	public void addProp(String key, Object value) {
		String temp = null;
		if (value instanceof String) {
			temp = "\"%s\" : \"%s\"";
		} else if (value instanceof Integer
				|| value instanceof Long
				|| value instanceof Double) {
			temp = "\"%s\" : %s";
		} else {
			VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70004;
			throw new ApiException(codeEnum.getErrorCode(), codeEnum.getErrorMsg());
		}

		String propValue = String.format(temp, key, value.toString());

		if (props == null) {
			props = propValue;
		} else {
			props = props + " ; " + propValue;
		}
	}
}