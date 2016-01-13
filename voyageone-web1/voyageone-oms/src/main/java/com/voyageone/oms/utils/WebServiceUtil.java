/**
 * 
 */
package com.voyageone.oms.utils;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

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
		client.setReadTimeout(20000);
		
		URI u = new URI(url);
		WebResource resource = client.resource(u);
		String response = resource.type(MediaType.APPLICATION_JSON_TYPE).post(String.class, jsonData);
		return response;
	}
}
