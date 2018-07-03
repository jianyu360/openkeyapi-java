package com.topnet.jyopenapi;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * Hmac-SHA1 签名算法
 * @author rz
 * 
 */
public class HmacSHA1 {
	private static final String MAC_NAME = "HmacSHA1";
	public static final String ENCODING = "UTF-8";
	//签名方法
	public static String HmacSHA1Encrypt(String encryptText,String encryptKey ) throws Exception{
        byte[] data = encryptKey.getBytes( ENCODING );
        SecretKey secretKey = new SecretKeySpec( data, MAC_NAME );
        Mac mac = Mac.getInstance( MAC_NAME );
        mac.init( secretKey );
        byte[] text = encryptText.getBytes( ENCODING );
        byte[] digest = mac.doFinal( text );
        return new String(Base64.encodeBase64(digest));
	}
}
