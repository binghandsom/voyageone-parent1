package com.voyageone.batch.oms.formbean;

import java.util.List;

public class OutFormSendOrderMail {
	    //付款时间
		private String paytime;
		//sourceId
		 private String sourceId;
		 //买家名称
		 private String name;
		 /*private String sku;
		 private String product;
		 private String price;*/
		 //付款总金额
		 private String grandTotal;
		 //渠道
		 private String OrderChannelId;
		 //渠道
		 private String cartId;
		 
		 //orderDetail列表
		 private List<OutFormSendOrderDetailMail> orderDetailList;
	  
		public String getPaytime() {
			return paytime;
		}
		public void setPaytime(String paytime) {
			this.paytime = paytime;
		}
		public String getSourceId() {
			return sourceId;
		}
		public void setSourceId(String sourceId) {
			this.sourceId = sourceId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		/*public String getSku() {
			return sku;
		}
		public void setSku(String sku) {
			this.sku = sku;
		}
		public String getProduct() {
			return product;
		}
		public void setProduct(String product) {
			this.product = product;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}*/
		public String getGrandTotal() {
			return grandTotal;
		}
		public void setGrandTotal(String grandTotal) {
			this.grandTotal = grandTotal;
		}
		public List<OutFormSendOrderDetailMail> getOrderDetailList() {
			return orderDetailList;
		}
		public void setOrderDetailList(List<OutFormSendOrderDetailMail> orderDetailList) {
			this.orderDetailList = orderDetailList;
		}
		public String getOrderChannelId() {
			return OrderChannelId;
		}
		public void setOrderChannelId(String orderChannelId) {
			OrderChannelId = orderChannelId;
		}
		public String getCartId() {
			return cartId;
		}
		public void setCartId(String cartId) {
			this.cartId = cartId;
		}
		
}
