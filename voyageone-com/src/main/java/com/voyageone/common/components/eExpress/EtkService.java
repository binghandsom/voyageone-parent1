package com.voyageone.common.components.eExpress;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import com.voyageone.common.components.eExpress.bean.*;
import com.voyageone.common.util.DateTimeUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;

import com.voyageone.common.configs.beans.PostResponse;
import com.voyageone.common.configs.beans.CarrierBean;
import com.voyageone.common.components.eExpress.base.EtkBase;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

@Component
public class EtkService extends EtkBase{
	
	public static final String ACTION_LOGIN = "http://tempuri.org/eExpress_Login";
	public static final String ACTION_IMPORT = "http://tempuri.org/eExpress_shipment_import";
	public static final String ACTION_CANCEL = "http://tempuri.org/eExpress_shipment_cancel";
	public static final String ACTION_TRACKING = "http://tempuri.org/eExpress_shipment_tracking";

	/*
	 * 客户身份校验(eExpress_login)
	 */
	private ExpressLoginRes eExpress_Login(CarrierBean carrierBean) throws Exception{
		
		ExpressLoginReq loginInfo = new ExpressLoginReq();
		loginInfo.setLoginID(carrierBean.getApi_user());
		loginInfo.setPwd(carrierBean.getApi_pwd());
		
		XStream xstream = new XStream(new DomDriver());
		xstream.autodetectAnnotations(true);  
		String postXml = xstream.toXML(loginInfo);
		//因为下划线是xstream的关键字，所以要转换下
		postXml=postXml.replace("__", "_");
		PostResponse result = reqETKFull(ACTION_LOGIN,postXml,carrierBean,"");
		
		//解析XML
		SAXReader saxReader = new SAXReader();  
        Document document = saxReader.read(new ByteArrayInputStream(result.getResult().getBytes("UTF-8")));
        Element root = document.getRootElement();
        
        ExpressLoginRes res = new ExpressLoginRes();
        Element table = root.element("Body").element("eExpress_LoginResponse").element("eExpress_LoginResult").element("diffgram").element("NewDataSet").element("Table");
        if (table!=null){
        	res.setResult(table.elementText("result"));
        	res.setMsg(table.elementText("msg"));
        	res.setSession(result.getSession());
        }else{
        	return null;
        }
		
		return res;
	}

	/*
	 * 提单提交(eExpress_shipment_import)
	 */
	public ExpressImportRes eExpressShipmentImport(ExpressShipmentImportReq order,CarrierBean carrierBean) throws Exception{
		
		ExpressLoginRes loginRes = eExpress_Login(carrierBean);
		if (loginRes != null && "T".equals(loginRes.getResult())){
			order.getAwb().setUserToken(loginRes.getMsg());
		}else{
			throw new Exception("调用E特快登录API eExpressShipmentImport错误：" + (loginRes==null?"null":loginRes.getMsg()));
		}		
		
		//Bean -->  XML
		XStream xstream = new XStream(new DomDriver());
		xstream.autodetectAnnotations(true);  
		String postXml = xstream.toXML(order);
		//因为下划线是xstream的关键字，所以要转换下
		postXml=postXml.replace("__", "_");
		PostResponse result = reqETKFull(ACTION_IMPORT,postXml,carrierBean,loginRes.getSession());
		
		//解析XML
		SAXReader saxReader = new SAXReader();  
        Document document = saxReader.read(new ByteArrayInputStream(result.getResult().getBytes("UTF-8")));
        Element root = document.getRootElement();
        
        ExpressImportRes res = new ExpressImportRes();
        Element table = root.element("Body").element("eExpress_shipment_importResponse").element("eExpress_shipment_importResult").element("diffgram").element("NewDataSet").element("Table");
        if (table!=null){
        	res.setResult(table.elementText("result"));
        	res.setMsg(table.elementText("msg"));
        	res.setShipment_number(table.elementText("shipment_number"));
        	res.setSid(table.elementText("sid"));
        	res.setArea_code(table.elementText("area_code"));
        }else{
        	throw new Exception("调用E特快登录API eExpressShipmentImport错误：" +  result.getResult());
        }
		
		return res;
	}
	
	/*
	 * 提单取消(eExpress_shipment_import)
	 */
	public ExpressCaneclRes eExpressShipmentCancel(ExpressShipmentCancelReq cancel,CarrierBean carrierBean) throws Exception{
		
		ExpressLoginRes loginRes = eExpress_Login(carrierBean);
		if (loginRes != null && "T".equals(loginRes.getResult())){
			cancel.setUserToken(loginRes.getMsg());
		}else{
			throw new Exception("调用E特快登录API错误：" +  (loginRes==null?"null":loginRes.getMsg()));
		}
		
		XStream xstream = new XStream(new DomDriver());
		xstream.autodetectAnnotations(true);  
		String postXml = xstream.toXML(cancel);
		//因为下划线是xstream的关键字，所以要转换下
		postXml=postXml.replace("__", "_");
		PostResponse result = reqETKFull(ACTION_CANCEL,postXml,carrierBean,loginRes.getSession());
		
		//解析XML
		SAXReader saxReader = new SAXReader();  
        Document document = saxReader.read(new ByteArrayInputStream(result.getResult().getBytes("UTF-8")));
        Element root = document.getRootElement();
        
        ExpressCaneclRes res = new ExpressCaneclRes();
        Element table = root.element("Body").element("eExpress_shipment_cancelResponse").element("eExpress_shipment_cancelResult").element("diffgram").element("NewDataSet").element("Table");
        if (table!=null){
        	res.setResult(table.elementText("resultType"));
        	res.setMsg(table.elementText("msg"));
        }else{
        	throw new Exception("调用E特快登录API eExpressShipmentCancel错误：" + result.getResult());
        }
		
		return res;
	}
	
	/*
	 * 提单状态追踪(eExpress_shipment_tracking)
	 */
	public ExpressTrackingRes eExpressShipmentTracking(ExpressShipmentTrackingReq tracking,CarrierBean carrierBean) throws Exception{
		
		ExpressLoginRes loginRes = eExpress_Login(carrierBean);
		if (loginRes != null && "T".equals(loginRes.getResult())){
			tracking.setUserToken(loginRes.getMsg());
		}else{
			throw new Exception("调用E特快登录API错误："  + (loginRes==null?"null":loginRes.getMsg()));
		}
		
		XStream xstream = new XStream(new DomDriver());
		xstream.autodetectAnnotations(true);  
		String postXml = xstream.toXML(tracking);
		//因为下划线是xstream的关键字，所以要转换下
		postXml=postXml.replace("__", "_");
		PostResponse result = reqETKFull(ACTION_TRACKING,postXml,carrierBean,loginRes.getSession());
		
		//解析XML
		SAXReader saxReader = new SAXReader();  
        Document document = saxReader.read(new ByteArrayInputStream(result.getResult().getBytes("UTF-8")));
        Element root = document.getRootElement();
        
        ExpressTrackingRes res = new ExpressTrackingRes();
        try {
	        Element table = root.element("Body").element("eExpress_shipment_trackingResponse").element("eExpress_shipment_trackingResult").element("diffgram").element("NewDataSet").element("Table");
			if (table!=null){

				res.setResult(table.elementText("Result"));

				List<ExpressTrackingDetail> trackingDetails = new ArrayList<>();

				// 物流信息有多条记录，需要全部取得
				for(int i=1;i<table.getParent().content().size();i++) {
					Element tableDetail = (Element) table.getParent().content().get(i);
					ExpressTrackingDetail trackingDetail = new ExpressTrackingDetail();
					trackingDetail.setShipment_number(tableDetail.elementText("shipment_number"));
					trackingDetail.setCustomer_number(tableDetail.elementText("customer_number"));
					trackingDetail.setStatus_code(tableDetail.elementText("status_code"));
					trackingDetail.setDescription(tableDetail.elementText("description"));
					trackingDetail.setEntry_datetime(tableDetail.elementText("entry_datetime"));

					trackingDetails.add(trackingDetail);
				}

				res.setTrackingDetails(trackingDetails);

	        }else{
	            throw new Exception("调用E特快登录API eExpressShipmentTracking错误：" + result.getResult());
	        }
        }catch (Exception e) {
        	Element errorTable = root.element("Body").element("eExpress_shipment_trackingResponse").element("eExpress_shipment_trackingResult").element("diffgram").element("DocumentElement").element("eExpress_shipment_tracking");
        	
            if (errorTable!=null){
            	res.setResult(errorTable.elementText("Result"));
            	res.setMsg(errorTable.elementText("Message"));
            }else{
            	throw new Exception("调用E特快登录API eExpressShipmentTracking错误：" + result.getResult());
            }       	
        }
		
		return res;
	}
}
