package com.topnet.jyopenapi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import com.alibaba.fastjson.JSON;

/**
 * 
 * @author rz
 *
 */

public class JyOpenUtils {
	private static final String ENCODING = "UTF-8";
	
	//替换特殊字符
    private static String percentEncode(String value) throws UnsupportedEncodingException {
        return value != null ? URLEncoder.encode(value, ENCODING).replace("+", "%20").replace("*", "%2A").replace("%7E", "~") : null;
    }

    //签名方法
    public static String Signature(Map<String, String> parameters,String secret) throws UnsupportedEncodingException{
    	String[] sortedKeys = parameters.keySet().toArray(new String[]{});
	    Arrays.sort(sortedKeys);
	    //参数签名
	    StringBuilder queryString = new StringBuilder();
	    for(String key : sortedKeys) {
	        // 这里对key和value进行编码
	    	queryString.append("&").append(key).append("=").append(parameters.get(key));
	    }
	    try {
	    	String signature=HmacSHA1.HmacSHA1Encrypt(percentEncode(queryString.toString().substring(1)),secret+"&");
	    	return signature;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
    }
    
    //post请求方法
    public static Map post(String url,Map<String, String> hm){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url); 
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		Iterator<String> it=hm.keySet().iterator();
		while(it.hasNext()){
			String key=it.next();
			formparams.add(new BasicNameValuePair(key, hm.get(key)));
		}
	    try {  
	    	    if (formparams.size()>0){	    	    	
		    	    UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
		    	    System.out.println(formparams.toString());
		            httppost.setEntity(uefEntity); 
	    	    }	    	
	            CloseableHttpResponse response = httpclient.execute(httppost);  
	            try {  
	            	 HttpEntity entity = response.getEntity();  
	                 if (entity != null) {
	                	 byte []b=EntityUtils.toByteArray(entity);
	                	 Map obj=(Map)JSON.parse(b);
	                	 return obj;
	                 }  
	            } finally {  
	                response.close();  
	            }  
	        } catch (ClientProtocolException e) {  
	            e.printStackTrace();  
	        } catch (UnsupportedEncodingException e1) {  
	            e1.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        } finally {
	            try {  
	                httpclient.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }
	    return null;
	}
}
