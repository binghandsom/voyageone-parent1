package com.voyageone.common.util;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class ImgUtils {
 
	/**
	 * 图片流取得
	 * 
	 * @param request 
	 * @param response 
	 * @param imgPathPara 图片路径
	 * @return
	 */
    public static boolean getPicStream(HttpServletRequest request,
								    		HttpServletResponse response,
								    		String imgPathPara) throws Exception {
    	boolean ret = false;
		ServletOutputStream out = null;
		InputStream ips = null;
    	try {
    		
    		if (isLocalPath(imgPathPara)) {
    			// 本地文件的场合    			
        		ips = new FileInputStream(new File(imgPathPara));
    		} else {
    			// 网络文件的场合
    			URL url = null; 
    			url = new URL(imgPathPara);  
    			HttpURLConnection  httpUrl = (HttpURLConnection) url.openConnection();  
    			httpUrl.connect();
    			
    			ips = new BufferedInputStream(httpUrl.getInputStream());
    		} 

    		response.setContentType("multipart/form-data");
    		out = response.getOutputStream(); 
    		
    		//	读取文件流
    		int i = 0;
    		byte[] buffer = new	byte[4096];

    		while((i = ips.read(buffer)) != -1) {
    			out.write(buffer, 0, i); 
    		}
    		
    		out.flush(); 
    		ips.close();
    		
    		ret = true;
		} catch (Exception e) {
			throw e;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e){
					throw e;
				}
			}
			
			if (ips != null) {
				try {
					ips.close();
				} catch (Exception e){
					throw e;
				}
			}
		}
    	return ret;                                                             
    }
    
	/**
	 * 图片路径判定 
	 * 
	 * @param request
	 * @return
	 */
    public static boolean isLocalPath(String path) {
    	boolean ret = true;
    	
    	if (path.length() > 5) {
    		if ("http".equals(path.substring(0, 4))) {
    			ret = false;
    		}
    	}
    	
    	return ret;
    }
}
