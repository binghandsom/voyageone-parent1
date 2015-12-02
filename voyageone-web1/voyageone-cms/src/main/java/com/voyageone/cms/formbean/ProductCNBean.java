package com.voyageone.cms.formbean;

public class ProductCNBean extends ProductBaseBean{
   
	private ProductCNBaseProductInfo cnBaseProductInfo;
	
	private ProductCNTMProductInfo  tmProductInfo;
	
	private ProductCNJDProductInfo  jdProductInfo;

	public ProductCNBean(){
		this.cnBaseProductInfo = new ProductCNBaseProductInfo();
		this.tmProductInfo = new ProductCNTMProductInfo();
		this.jdProductInfo = new ProductCNJDProductInfo();
	}
	public ProductCNBaseProductInfo getCnBaseProductInfo() {
		return cnBaseProductInfo;
	}

	public void setCnBaseProductInfo(ProductCNBaseProductInfo cnBaseProductInfo) {
		this.cnBaseProductInfo = cnBaseProductInfo;
	}

	public ProductCNTMProductInfo getTmProductInfo() {
		return tmProductInfo;
	}

	public void setTmProductInfo(ProductCNTMProductInfo tmProductInfo) {
		this.tmProductInfo = tmProductInfo;
	}

	public ProductCNJDProductInfo getJdProductInfo() {
		return jdProductInfo;
	}

	public void setJdProductInfo(ProductCNJDProductInfo jdProductInfo) {
		this.jdProductInfo = jdProductInfo;
	}
	
	
}
