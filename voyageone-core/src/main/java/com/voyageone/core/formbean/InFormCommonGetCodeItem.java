package com.voyageone.core.formbean;

/**
 * 画面传入Code取得bean
 * 
 * @author jerry
 *
 */
public class InFormCommonGetCodeItem {

	//	Code id
	private int id;
	//	空白显示
	private boolean showBlank;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isShowBlank() {
		return showBlank;
	}
	public void setShowBlank(boolean showBlank) {
		this.showBlank = showBlank;
	}
}
