package com.voyageone.oms.service.impl;

import com.google.gson.GsonBuilder;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.MD5;
import com.voyageone.oms.OmsCodeConstants;
import com.voyageone.oms.OmsConstants;
import com.voyageone.oms.OmsConstants.PropKey;
import com.voyageone.oms.dao.OrderDao;
import com.voyageone.oms.formbean.InFormServiceSearchSKU;
import com.voyageone.oms.formbean.OutFormServiceSearchSKU;
import com.voyageone.oms.service.OmsCommonService;
import com.voyageone.oms.utils.WebServiceUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class OmsCommonServiceImpl implements OmsCommonService {
	private static Log logger = LogFactory.getLog(OmsCommonServiceImpl.class);
	
	@Autowired
	private OrderDao orderDao;
	
	/**
	 * 获得SKU信息
	 * 
	 * @return
	 */
	public List<OutFormServiceSearchSKU> getSKUList(InFormServiceSearchSKU inFormServiceSearchSKU,String type) {
		List<OutFormServiceSearchSKU> ret = new ArrayList<OutFormServiceSearchSKU>();

		String param = JsonUtil.getJsonString(inFormServiceSearchSKU);
		String result=null;

		//WebService 调用
		// 斯伯丁，CHAMPION，皇马 的场合（CMS 在国内）
		if(OmsCodeConstants.OrderChannelId.SP.equals(inFormServiceSearchSKU.getChannelId()) ||
				OmsCodeConstants.OrderChannelId.CHAMPION.equals(inFormServiceSearchSKU.getChannelId()) ||
				OmsCodeConstants.OrderChannelId.RM.equals(inFormServiceSearchSKU.getChannelId())) {
			if (type.equals(OmsConstants.SKU_TYPE_ADDNEWORDER)) {
				String searchskuPath = Properties.readValue(PropKey.SEARCHSKUCN_PATH);
				result = HttpUtils.post(searchskuPath, param);
			}
			//OrderDetail调用
			if (type.equals(OmsConstants.SKU_TYPE_ORDERDETAIL)) {
				String searchskuPath = Properties.readValue(PropKey.SEARCHSKUListCN_PATH);
				result = HttpUtils.post(searchskuPath, param);
			}
		// 珠宝的场合
		} else if (OmsCodeConstants.OrderChannelId.JW.equals(inFormServiceSearchSKU.getChannelId())) {
			String searchskuPath = Properties.readValue(PropKey.SEARCHSKUINFO_PATH);
			result = getSKUInfoByWebService(searchskuPath, inFormServiceSearchSKU);
		// 其他的场合（CMS 在美国）
		} else {
			if(type.equals(OmsConstants.SKU_TYPE_ADDNEWORDER)){
				String searchskuPath = Properties.readValue(PropKey.SEARCHSKU_PATH);
				result = HttpUtils.post(searchskuPath, param);
			}
			//OrderDetail调用
			if(type.equals(OmsConstants.SKU_TYPE_ORDERDETAIL)){
				String searchskuPath = Properties.readValue(PropKey.SEARCHSKUList_PATH);
				result = HttpUtils.post(searchskuPath, param);
			}
		}

		ret = JsonUtil.jsonToBeanList(result, OutFormServiceSearchSKU.class);
//		for (OutFormServiceSearchSKU outFormServiceSearchSKU : ret) {
//			// TODO 转换什么字符？
//			if (!StringUtils.isEmpty(outFormServiceSearchSKU.getDescription())) {
//				outFormServiceSearchSKU.setDescription(outFormServiceSearchSKU.getDescription());
//			}
//			if (!StringUtils.isEmpty(outFormServiceSearchSKU.getProduct())) {
//				outFormServiceSearchSKU.setProduct(outFormServiceSearchSKU.getProduct());
//			}
//		}
		
		
		return ret;
	}

	/**
	 * 获得SKU信息（VoyageOne WebService）
	 *
	 * @return
	 */
	private String getSKUInfoByWebService(String url, InFormServiceSearchSKU inFormServiceSearchSKU) {
		String result = "";

		try {
			Map param = new HashMap();
			String timeStamp = DateTimeUtil.getNow();
			param.put("timeStamp", timeStamp);

			String prefixStr = "VoyageOne";
			param.put("signature", MD5.getMD5(prefixStr + timeStamp));
			param.put("dataBody", inFormServiceSearchSKU);

			String jsonParam = new GsonBuilder().serializeNulls().create().toJson(param);
			String resultFromWebService =  WebServiceUtil.postByJsonStr(url, jsonParam);
			result = JsonUtil.getJsonString(JsonUtil.jsonToMap(resultFromWebService).get("resultInfo"));
		} catch (Exception e) {
			logger.error("getSKUInfoByWebService", e);

			result = null;
		}

		return result;
	}
	// TODO 共通方法为什么不放到Common
//	public static String convert(String utfString) {
//		String[] buf = utfString.split("//u");
//		StringBuilder sb = new StringBuilder();
//		for (String string : buf) {
//			if (string.length() > 0) {
//				sb.append((char) Integer.parseInt(string, 16));
//			}
//		}
//		return sb.toString();
//	}
	//加入到共同方法
//	public static String post(String url, String param) {
//        PrintWriter out = null;
//        BufferedReader in = null;
//        String result = "";
//        try {
//            URL realUrl = new URL(url);
//            // 打开和URL之间的连接
//            URLConnection conn = realUrl.openConnection();
//            // 设置通用的请求属性
//            conn.setRequestProperty("accept", "*/*");
//            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            // 发送POST请求必须设置如下两行
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            // 获取URLConnection对象对应的输出流
//            out = new PrintWriter(conn.getOutputStream());
//            // 发送请求参数
//            out.print(param);
//            // flush输出流的缓冲
//            out.flush();
//            // 定义BufferedReader输入流来读取URL的响应
//            in = new BufferedReader(
//                    new InputStreamReader(conn.getInputStream()));
//            String line;
//            String a;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
//            result= new String(result.getBytes(), "UTF-8");
//        } catch (Exception e) {
//            System.out.println("发送 POST 请求出现异常！"+e);
//            e.printStackTrace();
//        }
//        //使用finally块来关闭输出流、输入流
//        finally{
//            try{
//                if(out!=null){
//                    out.close();
//                }
//                if(in!=null){
//                    in.close();
//                }
//            }
//            catch(IOException ex){
//                ex.printStackTrace();
//            }
//        }
//        return result;
//    }   
//	  public static String get(String url, String param) {
//	        String result = "";
//	        BufferedReader in = null;
//	        try {
//	            String urlNameString = url + "?" + param;
//	            URL realUrl = new URL(urlNameString);
//	            // 打开和URL之间的连接
//	            URLConnection connection = realUrl.openConnection();
//	            // 设置通用的请求属性
//	            connection.setRequestProperty("accept", "*/*");
//	            connection.setRequestProperty("connection", "Keep-Alive");
//	            connection.setRequestProperty("user-agent",
//	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//	            // 建立实际的连接
//	            connection.connect();
//	            // 获取所有响应头字段
//	            Map<String, List<String>> map = connection.getHeaderFields();
//	            // 遍历所有的响应头字段
//	            for (String key : map.keySet()) {
//	                System.out.println(key + "--->" + map.get(key));
//	            }
//	            // 定义 BufferedReader输入流来读取URL的响应
//	            in = new BufferedReader(new InputStreamReader(
//	                    connection.getInputStream()));
//	            String line;
//	            while ((line = in.readLine()) != null) {
//	                result += line;
//	            }
//	        } catch (Exception e) {
//	            System.out.println("发送GET请求出现异常！" + e);
//	            e.printStackTrace();
//	        }
//	        // 使用finally块来关闭输入流
//	        finally {
//	            try {
//	                if (in != null) {
//	                    in.close();
//	                }
//	            } catch (Exception e2) {
//	                e2.printStackTrace();
//	            }
//	        }
//	        return result;
//	    }
}
