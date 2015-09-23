package com.voyageone.bi.disbean;

import java.util.List;

import org.olap4j.Cell;

// 图标控件DB取得数据
public class BaseKpiDisBean {
	
	// KPI
	//	销售量
	private String value = "0";
	//	环比销售量
	private String value_r = "0";
	//  环比（销售量）
	private String value_r_rate = "0";
	//  环比增幅（销售量）
	private String value_r_rate_up = "0";
	//	同比销售量
	private String value_y = "0";
	//  同比（销售量）
	private String value_y_rate = "0";
	//  同比增幅（销售量）
	private String value_y_rate_up = "0";

	public BaseKpiDisBean() {
	}
	
	public BaseKpiDisBean(List<Cell> cellList) {
		value = cellList.get(0).getFormattedValue();
		value_r = cellList.get(1).getFormattedValue();
		value_r_rate = cellList.get(2).getFormattedValue();
		value_r_rate_up = cellList.get(3).getFormattedValue();
		value_y = cellList.get(4).getFormattedValue();
		value_y_rate = cellList.get(5).getFormattedValue();
		value_y_rate_up = cellList.get(6).getFormattedValue();
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue_r() {
		return value_r;
	}
	public void setValue_r(String value_r) {
		this.value_r = value_r;
	}
	public String getValue_r_rate() {
		return value_r_rate;
	}
	public void setValue_r_rate(String value_r_rate) {
		this.value_r_rate = value_r_rate;
	}
	public String getValue_r_rate_up() {
		return value_r_rate_up;
	}
	public void setValue_r_rate_up(String value_r_rate_up) {
		this.value_r_rate_up = value_r_rate_up;
	}
	public String getValue_y() {
		return value_y;
	}
	public void setValue_y(String value_y) {
		this.value_y = value_y;
	}
	public String getValue_y_rate() {
		return value_y_rate;
	}
	public void setValue_y_rate(String value_y_rate) {
		this.value_y_rate = value_y_rate;
	}
	public String getValue_y_rate_up() {
		return value_y_rate_up;
	}
	public void setValue_y_rate_up(String value_y_rate_up) {
		this.value_y_rate_up = value_y_rate_up;
	}
}