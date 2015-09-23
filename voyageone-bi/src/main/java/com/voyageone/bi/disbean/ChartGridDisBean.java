package com.voyageone.bi.disbean;

// 图标控件DB取得数据
public class ChartGridDisBean {
	
	// 标题栏
	private String title;
	// 标题栏
	private String sTitle;
	// 标题栏id
	private String id;
	// 标题栏对应值
	private String value;
	// 标题栏parent对应值
	private String parent;
	// type
	private String type;
	// 标题栏种类（例如：category, model, product）
	private String title_kind;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getsTitle() {
		return sTitle;
	}
	public void setsTitle(String sTitle) {
		this.sTitle = sTitle;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle_kind() {
		return title_kind;
	}
	public void setTitle_kind(String title_kind) {
		this.title_kind = title_kind;
	}
	private BaseKpiDisBean qty_kpi = new BaseKpiDisBean();
	private BaseKpiDisBean amt_kpi = new BaseKpiDisBean();
	private BaseKpiDisBean order_kpi = new BaseKpiDisBean();
	private BaseKpiDisBean atv_kpi = new BaseKpiDisBean();
	private BaseKpiDisBean pv_kpi = new BaseKpiDisBean();
	private BaseKpiDisBean uv_kpi = new BaseKpiDisBean();
	private BaseKpiDisBean tr_kpi = new BaseKpiDisBean();

	public BaseKpiDisBean getQty_kpi() {
		return qty_kpi;
	}
	public void setQty_kpi(BaseKpiDisBean qty_kpi) {
		this.qty_kpi = qty_kpi;
	}
	public BaseKpiDisBean getAmt_kpi() {
		return amt_kpi;
	}
	public void setAmt_kpi(BaseKpiDisBean amt_kpi) {
		this.amt_kpi = amt_kpi;
	}
	public BaseKpiDisBean getOrder_kpi() {
		return order_kpi;
	}
	public void setOrder_kpi(BaseKpiDisBean order_kpi) {
		this.order_kpi = order_kpi;
	}
	public BaseKpiDisBean getAtv_kpi() {
		return atv_kpi;
	}
	public void setAtv_kpi(BaseKpiDisBean atv_kpi) {
		this.atv_kpi = atv_kpi;
	}
	public BaseKpiDisBean getUv_kpi() {
		return uv_kpi;
	}
	public void setUv_kpi(BaseKpiDisBean uv_kpi) {
		this.uv_kpi = uv_kpi;
	}
	public BaseKpiDisBean getPv_kpi() {
		return pv_kpi;
	}
	public void setPv_kpi(BaseKpiDisBean pv_kpi) {
		this.pv_kpi = pv_kpi;
	}
	
	public BaseKpiDisBean getTr_kpi() {
		return tr_kpi;
	}
	public void setTr_kpi(BaseKpiDisBean tr_kpi) {
		this.tr_kpi = tr_kpi;
	}
}