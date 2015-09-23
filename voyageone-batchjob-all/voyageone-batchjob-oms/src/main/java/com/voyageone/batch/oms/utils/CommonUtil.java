package com.voyageone.batch.oms.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;

public class CommonUtil {

	private static Log logger = LogFactory.getLog(CommonUtil.class);

	// 通过http post方式推送订单到美国
	public static String HttpPost(String content, String charset, String url) throws Exception {

		HttpURLConnection http = null;
		OutputStream output = null;
		BufferedReader in = null;
		String ret = null;

		try {
			logger.info("order post start:" + url);
			logger.info("order post content:" + content);
			URL u = new URL(url);
			http = (HttpURLConnection) u.openConnection();

			http.setDoOutput(true);
			http.setDoInput(true);
			// 设置连接主机超时（单位：毫秒）

			http.setConnectTimeout(10000);
			// 设置从主机读取数据超时（单位：毫秒）
			http.setReadTimeout(10000);
			// 设定请求的方法为"POST"，默认是GET
			http.setRequestMethod("POST");
			// 设定传送的内容类型
			http.setRequestProperty("Content-Type", "text/xml");
			http.connect();
			output = http.getOutputStream();
			if (charset != null) {
				output.write(content.getBytes(charset));
			} else {
				output.write(content.getBytes());
			}
			output.flush();
			output.close();

			StringBuilder sb = new StringBuilder();
			// 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端

			if (charset != null) {
				in = new BufferedReader(new InputStreamReader(http.getInputStream(), charset));
			} else {
				in = new BufferedReader(new InputStreamReader(http.getInputStream()));
			}

			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line).append("\n");
			}
			in.close();
			http.disconnect();
			ret = sb.toString();
			logger.info("response:" + ret);
			logger.info("order post end:" + url);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			if (output != null) {
				try {
					output.close();
				} catch (IOException ex) {
					e.printStackTrace();
					logger.error(ex.getMessage());
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException ex) {
					e.printStackTrace();
					logger.error(ex.getMessage());
				}
			}
			if (http != null) {
				http.disconnect();
			}
			// 异常发生
			throw new Exception("HttpPost fail "+e.getMessage());
		}
		return ret;
	}
	
	/**
	 * 
	 * @param requestXml
	 * @return
	 * @throws Exception
	 */
	 public static String SOAPHttp(String postUrl,String soap_action,String send_soap) throws Exception {
	        logger.info("Request 开始");
	        String ret="";
	        URL url=new URL(postUrl);
	        HttpURLConnection http= (HttpURLConnection) url.openConnection();
	        http.setConnectTimeout(30000);
			// 设置从主机读取数据超时（单位：毫秒）
	        http.setReadTimeout(30000);
	        http.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 5.0; Windows XP; DigExt)");
	        http.setRequestProperty( "Content-Length",String.valueOf( send_soap.length() ) );
	        http.setRequestProperty("Content-Type","text/xml; charset=UTF-8");
//	        _httpConn.setRequestProperty("soapActionString",_soap_action_str);
	        http.setRequestProperty("SOAPAction",soap_action);
	        http.setRequestMethod( "POST" );
	        http.setDoInput(true);
	        http.setDoOutput(true);
	        http.setUseCaches(false);
	        
	        http.connect();
	        
	        OutputStream out = http.getOutputStream();
	        out.write( send_soap.getBytes() );
	        out.flush();
	        out.close();
	        
	        int code=0;
	        code = http.getResponseCode();
	        if (code == HttpURLConnection.HTTP_OK) {
	        	BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
	        	StringBuffer sb=new StringBuffer();
				String line = "";
				while ((line = in.readLine()) != null) {
					sb.append(line).append("\n");
				}
				in.close();
				ret = sb.toString();
				
	            
	        }
	        else{
	        	throw new Exception("Request 异常  Response Code: "+String.valueOf(code));
	        }
	        logger.info("Request 结束");
	        return ret;
	        
	    }
}
