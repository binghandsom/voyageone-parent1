package com.voyageone.cms.formbean;

public class ModelCNBean {
	
	private ModelCNBaseBean cnBaseModelInfo = new ModelCNBaseBean();
	private ModelJDBean jdModelInfo = new ModelJDBean();
	private ModelTMBean tmModelInfo = new ModelTMBean();
	public ModelCNBaseBean getCnBaseModelInfo() {
		return cnBaseModelInfo;
	}
	public void setCnBaseModelInfo(ModelCNBaseBean cnBaseModelInfo) {
		this.cnBaseModelInfo = cnBaseModelInfo;
	}
	public ModelJDBean getJdModelInfo() {
		return jdModelInfo;
	}
	public void setJdModelInfo(ModelJDBean jdModelInfo) {
		this.jdModelInfo = jdModelInfo;
	}
	public ModelTMBean getTmModelInfo() {
		return tmModelInfo;
	}
	public void setTmModelInfo(ModelTMBean tmModelInfo) {
		this.tmModelInfo = tmModelInfo;
	}
	

	
	
}
