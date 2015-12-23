package com.voyageone.wsdl.core.secrity;

import java.util.Map;

public class Test {

	private static String publicKey;  
    private static String privateKey;  
    
	public static void main(String[] args) throws Exception {
		Map<String, Object> keyMap = RSACoder.initKey();  
		  
        publicKey = RSACoder.getPublicKey(keyMap);  
        privateKey = RSACoder.getPrivateKey(keyMap);  
        System.err.println("公钥: \n\r" + publicKey);  
        System.err.println("私钥： \n\r" + privateKey);
        
//        System.err.println("公钥加密——私钥解密");  
//        String inputStr = "abc黄帝";  
//        byte[] data = inputStr.getBytes("UTF-8");  
//  
//        byte[] encodedData = RSACoder.encryptByPublicKey(data, publicKey);  
//  
//        byte[] decodedData = RSACoder.decryptByPrivateKey(encodedData,  
//                privateKey);  
//  
//        String outputStr = new String(decodedData,"UTF-8");  
//        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);  
        
        
//        System.err.println("私钥加密——公钥解密");  
//        String inputStr = "sign检查";
//        byte[] data = inputStr.getBytes();
//  
//        byte[] encodedData = RSACoder.encryptByPrivateKey(data, privateKey);  
//  
//        byte[] decodedData = RSACoder
//                .decryptByPublicKey(encodedData, publicKey);  
//  
//        String outputStr = new String(decodedData);
//        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);  
//  
//        System.err.println("私钥签名——公钥验证签名");  
//        // 产生签名  
//        String sign = RSACoder.sign(encodedData, privateKey);  
//        System.err.println("签名:\r" + sign);
//  
//        // 验证签名  
//        boolean status = RSACoder.verify(encodedData, publicKey, sign);  
//        System.err.println("状态:\r" + status);
        test();
	}
	
	public static void test() throws Exception {  
        String inputStr = "DES朱啸";  
        String key = DESCoder.initKey();  
        System.err.println("原文:\t" + inputStr);  
  
        System.err.println("密钥:\t" + key);  
  
        byte[] inputData = inputStr.getBytes();  
        inputData = DESCoder.encrypt(inputData, key);  
  
        System.err.println("加密后:\t" + DESCoder.encryptBASE64(inputData));  
  
        byte[] outputData = DESCoder.decrypt(inputData, key);  
        String outputStr = new String(outputData);  
  
        System.err.println("解密后:\t" + outputStr);  
  
    }
}
