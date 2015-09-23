package com.voyageone.oms.formbean;

/**
 * 画面传入Code取得bean
 * 
 * @author jerry
 *
 */
public class InFormCommonGetCodeItem {

	//	Code id
	private String id;
	//	空白显示
	private boolean showBlank;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isShowBlank() {
		return showBlank;
	}
	public void setShowBlank(boolean showBlank) {
		this.showBlank = showBlank;
	}
}
