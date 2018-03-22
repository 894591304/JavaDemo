package com.enation.app.ext.component.mobile.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

public class MsgSendClient {
    public static void main(String[] args) throws Exception{
    	String url = "https://api.netease.im/sms/verifycode.action";
    	List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("mobile", "15010755317"));
        nvps.add(new BasicNameValuePair("code", "275830"));
        new MsgSendClient().postAction(url,nvps);
    }
    
    public static boolean sendMsg(String mobile){
    	String url = "https://api.netease.im/sms/sendcode.action";
    	List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("mobile", mobile));
        nvps.add(new BasicNameValuePair("templateid", "3058611"));
        nvps.add(new BasicNameValuePair("codeLen", "6"));
        return postAction(url,nvps);
    }
    
    public static boolean checkMsg(String mobile,String code){
    	String url = "https://api.netease.im/sms/verifycode.action";
    	List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("mobile", mobile));
        nvps.add(new BasicNameValuePair("code", code));
        return postAction(url,nvps);
    }
    
    
    private static boolean postAction(String url,List<NameValuePair> nvps){
    	DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        String appKey = "1b94f4e31e4a5f181b6bb2143fb6fecf";
        String appSecret = "74c1ea2f39ea";
        String nonce =  "872628";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的参数
        try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			// 执行请求
			HttpResponse response = httpClient.execute(httpPost);
			// 打印执行结果
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, "utf-8");
			JSONObject json_result = JSONObject.fromObject(result);
			if(json_result.getString("code").equals("200")){
				return true;
			}else{
				System.out.println("请求网易云错误，错误返回值为："+result);
				return false;
			}
			
		}catch (Exception e) {
			System.out.println("请求网易云错误:"+e.getStackTrace());
			return false;
		}
    }
}
