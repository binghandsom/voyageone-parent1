package com.voyageone.service.bean.com;

import java.util.List;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/10
 */
public class PaginationBean<E> {

	private Integer count;
	
	private List<E> result;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<E> getResult() {
		return result;
	}

	public void setResult(List<E> result) {
		this.result = result;
	}
	
}
