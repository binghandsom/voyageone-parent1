/**
 * 
 */
package com.voyageone.batch.cms.utils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.voyageone.common.components.channelAdvisor.bean.orders.ObjectFactory;
import com.voyageone.common.util.JsonUtil;

import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jacky
 *
 */
public class WebServiceUtil {
	/**
	 * 
	 * @param url
	 * @param jsonData
	 * @return
	 * @throws URISyntaxException
	 */
	public static String postByJsonStr(String url, String jsonData) throws Exception{
		Client client = Client.create();
		client.setConnectTimeout(15000);
		client.setReadTimeout(120000);
		
		URI u = new URI(url);
		WebResource resource = client.resource(u);
		String response = resource.type(MediaType.APPLICATION_JSON_TYPE).post(String.class, jsonData);
		return response;
	}

	public static String getByUrl(String url) throws Exception{
		Client client = Client.create();
		client.setConnectTimeout(15000);
		client.setReadTimeout(120000);

		URI u = new URI(url);
		WebResource resource = client.resource(u);
		String response = resource.get(String.class);
		return response;
	}

	public static void main(String[] args) throws Exception {
		String q = "my Panda";
		String url = "http://openapi.baidu.com/public/2.0/bmt/translate?client_id=Tzadu9fNzPlQ4YsD3gmHbWDm&q=";
		q = URLEncoder.encode(q, "utf-8");
		url += q + "&from=auto&to=auto";

//		url = "http://openapi.baidu.com/public/2.0/bmt/translate?client_id=SdcVpwzIwiIcQaLmlBkhpAez&q=my Panda&from=auto&to=auto";
//		String kong = URLEncoder.encode("", "utf-8");

//		url = "http://openapi.baidu.com/public/2.0/bmt/translate?client_id=SdcVpwzIwiIcQaLmlBkhpAez&q=white%0ARound%0ADiamond%0A1%0Aprongs%0A1%0AWhite%0ABangles%0A7 1/2 inches in circumference&from=auto&to=auto";
//
//		Map<String, String> dataMap = new HashMap<String, String>();
//		dataMap.put("client_id", "h6VoPbgID9RYTZVGoOFBFd5B");
//		dataMap.put("q", "today\\nwhite cat");
//		dataMap.put("from", "auto");
//		dataMap.put("to", "auto");
		String after = getByUrl(url);
		Map<String, Object> jsonToMap = JsonUtil.jsonToMap(after);
		Object trans_result = jsonToMap.get("trans_result");
		List<Map> mapList = (List<Map>) trans_result;
		if (mapList != null && mapList.size() > 0) {
			for (int i = 0; i < mapList.size();i++) {
				Map<String, String> map = mapList.get(i);
				String afterDst = map.get("dst");
				System.out.print(afterDst);
			}
		}



	}
}
