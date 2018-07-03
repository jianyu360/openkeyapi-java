package com.topnet.jyopenapi;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Described 剑鱼数据开放平台接口调用样例
 * @author rz
 * @date 2018-02-09
 * @version v1.0
 */
public class JyOpenApi  {
	
	/**
	 * 调用样例
	 * 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		String appid="jyo_ABpUW1NA11wMIAA5TKyE3";
		String secret="8341211B";
		String apiurl="https://api.jianyu360.com/openkey";

		Map<String, String> parameters = new HashMap<String, String>();
		// 请求参数
		parameters.put("action","getdata");
	    parameters.put("appid", appid);
	    parameters.put("pagenum", "1");
	    parameters.put("timestamp", new Date().getTime()/1000+"");
  
	    //参数签名
	    try {
	    	String signature=JyOpenUtils.Signature(parameters, secret);
	    	parameters.put("signature",signature);
	    	System.out.println(JyOpenUtils.post(apiurl,parameters));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
