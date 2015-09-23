package com.voyageone.batch.bi.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.voyageone.batch.configs.FileProperties;

public class FileUtils {
	private static final Log logger = LogFactory.getLog(FileUtils.class);
    private static String rootPath = null;


    public static void init() {
        rootPath = System.getProperty("user.dir");
    }

    public static void setRootPath(String rootPath1) {
        rootPath = rootPath1;
    }

    public static String getRootPath() {
        if (rootPath == null) {
            init();
        }
        return rootPath;
    }

    public static String getConfPath() {
        if (rootPath == null) {
            init();
        }
        return rootPath + "/conf/";
    }

    public static boolean downloadImageWithProxy(String urlString, String filename, String savePath) {
    	String proxyHost = FileProperties.readValue("bi.proxyHost");
    	int proxyPort = Integer.parseInt(FileProperties.readValue("bi.proxyPort"));
    	String proxyUser = FileProperties.readValue("bi.proxyUser");
    	String proxyPassword = FileProperties.readValue("bi.proxyPassword");
    	
    	return downloadImage(urlString, filename, savePath, proxyHost, proxyPort, proxyUser, proxyPassword); 
    }
    
    public static boolean downloadImage(String urlString, String filename, String savePath) {
    	return downloadImage(urlString, filename, savePath, null, 0, null, null); 
    }

    public static boolean downloadImage(String urlString, String filename, String savePath, String proxyIP, int proxyPort, String proxyUserName, String proxyPwd) {
    	boolean result = false; 
    	OutputStream os = null;
    	InputStream is = null;
        try {
            // 构造URL
            URL url = new URL(urlString);
            URLConnection con = null;
            
            if (proxyIP != null) {
            	// 设置代理 地址和端口
            	Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIP, proxyPort));  
                // 设置代理的密码验证
                if (proxyUserName != null) {
	            	Authenticator auth = new Authenticator() {
	                  private PasswordAuthentication pa = new PasswordAuthentication(proxyUserName, proxyPwd.toCharArray());
	                  @Override
	                  protected PasswordAuthentication getPasswordAuthentication() {
	                    return pa;
	                  }
	                };
	                Authenticator.setDefault(auth);
                }
                // 打开连接
                con = (HttpURLConnection)url.openConnection(proxy);
        	} else {
                // 打开连接
        		con = url.openConnection();
        	}
            //设置请求超时为5s
            con.setConnectTimeout(5 * 1000);
            //设置请求超时为5s
            con.setReadTimeout(10 * 1000);
            // 输入流
            is = con.getInputStream();

            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            File sf = new File(savePath);
            if (!sf.exists()) {
                sf.mkdirs();
            }
            File file = new File(sf.getPath() + "\\" + filename);
            if (file.exists()) {
            	file.delete();
            }
            os = new FileOutputStream(file);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            result = true;

        } catch (Exception e) {
        	logger.error("downloadImage", e);
        	logger.error("downloadImage url:="+urlString);
        } finally {
        	if (os != null) {
        		try {
					os.close();
				} catch (IOException e) {
				}
        	}
        	if (is != null) {
        		try {
        			is.close();
				} catch (IOException e) {
				}
        	}
        }
        return result;
    }

    public static void deleteFile(String savePath, String filename) {
    	String savePathTmp = savePath;
    	if (!(savePath.endsWith("/") || savePath.endsWith("\\"))) {
    		savePathTmp =  savePathTmp + "\\";
    	}
        File file = new File(savePathTmp + filename);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }
    
	public static void main(String[] args) throws InterruptedException {
		//String jumeiDownloadPath = FileProperties.readValue("jumei.download.filepath");
		String jumeiDownloadPath = "D:/";
		downloadImage("http://s7d5.scene7.com/is/image/sneakerhead/BHFO%5F2015%5Fx1000%5F1000x?$jc1000_1000$&$product=my8MqcnZbYjpR4rwvmkAJQQK1Rnw5qyN-25", 
				"my8MqcnZbYjpR4rwvmkAJQQK1Rnw5qyN-25.jpg", jumeiDownloadPath
				,"119.9.108.179"
				,7656
				,"vogservice"
				,"!@#abc123");
	}

}
