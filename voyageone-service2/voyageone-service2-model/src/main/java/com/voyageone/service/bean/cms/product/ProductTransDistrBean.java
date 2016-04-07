package com.voyageone.service.bean.cms.product;

/**
 * /product/translate/distribute Request Model
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since 2.0.0
 */
public class ProductTransDistrBean {

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

	/**
	 * projectionArr
	 */
	private String[] projectionArr;

	/**
	 * sortStr
	 */
	private String sortStr;

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

	public String[] getProjectionArr() {
		return projectionArr;
	}

	public void setProjectionArr(String[] projectionArr) {
		this.projectionArr = projectionArr;
	}

	public String getSortStr() {
		return sortStr;
	}

	public void setSortStr(String sortStr) {
		this.sortStr = sortStr;
	}
}