package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.response.PostProductSelectOneResponse;

/**
 * TOP API: product get request
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class PostProductSelectOneRequest extends VoApiRequest<PostProductSelectOneResponse> {

	/**
	 * 商品类目id.调用voapi.itemcats.get获取;必须是叶子类目id,如果没有传product_id,那么cid和props必须要传.
	 */
	private Long cid;

	/**
	 * 用户自定义关键属性,结构：pid1:value1;pid2:value2，如果有型号，系列等子属性用: 隔开 例如：“20000:优衣库:型号:001;632501:1234”，表示“品牌:优衣库:型号:001;货号:1234”
	 */
	private String customerProps;

	/**
	 * 需返回的字段列表.可选值:Product数据结构中的所有字段;多个字段之间用","分隔.
	 */
	private String fields;

	/**
	 * Product的id.两种方式来查看一个产品:1.传入product_id来查询 2.传入product_code来查询 3:传入cid和props来查询
	 */
	private Long productId;

	/**
	 * 比如:诺基亚N73这个产品的关键属性列表就是:品牌:诺基亚;型号:N73,对应的PV值就是10005:10027;10006:29729.
	 */
	private String props;

	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Long getCid() {
		return this.cid;
	}

	public void setCustomerProps(String customerProps) {
		this.customerProps = customerProps;
	}
	public String getCustomerProps() {
		return this.customerProps;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getFields() {
		return this.fields;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getProductId() {
		return this.productId;
	}

	public void setProps(String props) {
		this.props = props;
	}
	public String getProps() {
		return this.props;
	}

	public String getApiURLPath() {
		return "/puroduct/selectOne";
	}

	public Class<PostProductSelectOneResponse> getResponseClass() {
		return PostProductSelectOneResponse.class;
	}

//	public void check() throws ApiRuleException {
//		RequestCheckUtils.checkNotEmpty(fields, "fields");
//	}


}