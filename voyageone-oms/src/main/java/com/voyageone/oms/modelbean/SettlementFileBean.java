package com.voyageone.oms.modelbean;


public class SettlementFileBean {

	//	订单渠道ID
	private String orderChannelId;
	private String cartId;
	private String fileType;
	//	账务方式
	private String payType;

	//	上传日期
	private String uploadTime;
	//	上传文件名
	private String settlementFileId;
	//	账号
	private String account_no;
	//	起始日期
	private String begin_time;
	//	终止日期
	private String end_time;
	//	导出时间
	private String export_time;
	// 总收入
	private String total_income;
	// 总支出
	private String total_expense;

	private String creater;
	private String modifier;

	public String getOrderChannelId() {
		return orderChannelId;
	}

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public void setOrderChannelId(String orderChannelId) {
		this.orderChannelId = orderChannelId;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getSettlementFileId() {
		return settlementFileId;
	}

	public void setSettlementFileId(String settlementFileId) {
		this.settlementFileId = settlementFileId;
	}

	public String getAccount_no() {
		return account_no;
	}

	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}

	public String getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getExport_time() {
		return export_time;
	}

	public void setExport_time(String export_time) {
		this.export_time = export_time;
	}

	public String getTotal_income() {
		return total_income;
	}

	public void setTotal_income(String total_income) {
		this.total_income = total_income;
	}

	public String getTotal_expense() {
		return total_expense;
	}

	public void setTotal_expense(String total_expense) {
		this.total_expense = total_expense;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
}
