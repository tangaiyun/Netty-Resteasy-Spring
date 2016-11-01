package com.tay.rest.antidefacement;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Coder {
	static String key;
	static SecretKey secretKey; 
	static Mac mac;
	static {
		Properties prop = new Properties();
		InputStream in = Coder.class.getResourceAsStream("/key.properties");
		try {
			prop.load(in);
			key = prop.getProperty("key").trim();
			secretKey = new SecretKeySpec(decryptBASE64(key), KeyGen.KEY_MAC);
			mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * BASE64解密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String input){
		if(input == null) {
			return null;
		}
		return Base64.decodeBase64(input);
	}

	/**
	 * BASE64加密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] input){
		if(input == null) {
			return null;
		}
		return Base64.encodeBase64String(input);
	}
	/**
	 * 为一个json输入生成签名字串
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	public static String genSignature(String jsonString){
		if(jsonString == null) {
			return null;
		}
		byte[] bytes = null;
		byte[] bytesSignature = null;
		String signature = null;
		try {
			bytes = jsonString.getBytes("utf-8");
			bytesSignature = mac.doFinal(bytes);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(bytesSignature != null) {
			signature = encryptBASE64(bytesSignature);
		}
		return signature;
	}

	public static void main(String[] args) throws Exception {
		String testJson = "{\"name\":\"NAME\",\"id\":2}";
		String signature = genSignature(testJson);
		System.out.println(signature);
	}
}
