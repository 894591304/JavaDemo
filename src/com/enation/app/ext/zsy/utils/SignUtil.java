package com.enation.app.ext.zsy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class SignUtil {
	
	public static String getAccessToken(String date){
		String APPID = ConfigUtil.APPID;
		String KEYSECRET = ConfigUtil.KEYSECRET;
		String SOURCENO = ConfigUtil.SOURCENO;
		PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
		Map<String, String> params1 = new HashMap<String, String>();
		Map<String, String> params2 = new HashMap<String, String>();
		params1.put("appId",APPID);
		params1.put("scope","ACCESSTOKEN");
		params1.put("sourceNo", SOURCENO);	
		//将时间戳与请求参数组装到一起,保证每次生成的签名不一样
		params1.put("timestamp", date);		
		//调用签名算法
		params2.put("appId",APPID);
		params2.put("scope", "ACCESSTOKEN");
		params2.put("sourceNo", SOURCENO);		
		params2.put("timestamp", date);
		String signature = getSignature(params2, KEYSECRET);
		params1.put("signature",signature);
		String data = "appId="+APPID+"&scope=ACCESSTOKEN"+"&sourceNo="+SOURCENO+"&timestamp="+date+"&signature="+signature;
		//System.out.println(params1);
		try{
			URL realUrl = new URL(ConfigUtil.TOKEN_URL);
			HttpURLConnection conn=(HttpURLConnection)realUrl.openConnection();
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            out = new PrintWriter(conn.getOutputStream());
            out.print(data);
            out.flush();
            in = new BufferedReader( new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}catch(IOException ex){
				ex.printStackTrace();
			}
	     }
		//System.out.println("中顺易接口获取accesstoken接口测试:"+result);
		JSONObject reJson = JSONObject.parseObject(result);
		JSONObject atdata = reJson.getJSONObject("data");
		String accessToken = atdata.getString("access_token");
		System.out.println("获取accesstoken成功："+accessToken);
		return accessToken;
	}
	
	public static String sendBatchPayout(){
		Date d = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = String.valueOf(d.getTime());
		String url = ConfigUtil.PAYOUT_URL;
		String ACCESSTOKEN = getAccessToken(date);				//accesstoken
		String KEYSECRET = ConfigUtil.KEYSECRET;
		String APPID = ConfigUtil.APPID;					//各商家接入api应用的AppId
		String BIZTYPE = "WHYW";							//业务类型
		//String CURRENCY = "CNY";							//币种:CNY
		String MERCHANTNO = ConfigUtil.MERCHANTNO;			//商家号
		//String NOTIFYURL = "";							//结果通知URL(暂时没有)
		String OUTBATCHNO = df.format(d);					//外部订单号(精确到毫秒的时间)
		String SOURCENO = ConfigUtil.SOURCENO;				//渠道号		
		Map<String, String> params = new HashMap<String, String>();
		params.put("accessToken",ACCESSTOKEN);
		params.put("appId",APPID);
		params.put("bizType",BIZTYPE);
		params.put("merchantNo",MERCHANTNO);
		params.put("outBatchNo",OUTBATCHNO);
		params.put("sourceNo",SOURCENO);
		String TIMESTAMP = date;							//当前时间的long型的毫秒数
		String TOTALAMT = "";							//总金额
		JSONArray payerList = new JSONArray();
		int tamount = 0;
		for(int i=0;i<1;i++){
			JSONObject payerJson = new JSONObject();
			String ACCOUNTNO = "";									//信帐宝账户ID（可为空）
			int AMOUNT = 1;											//付款金额单位为：分
			String OUTCUSTOMERID = "";								//付款方外部会员号
			String OUTDETAILNO = "201712081216123";					//外部明细交易号（订单号）
			//String OUTTRADENO1 = "";								//外部明细单号（可不传）
			String PAYEEBANKACCOUNTTYPE = "PERSON";					//收款方银行账户类型,PERSON-个人银行卡, ENTERPRISE-企业银行卡
			String PAYEEBANKCARD = "6210300000000000";				//收款方银行卡号码
			String PAYEEBANKCODE = "ABC";					//收款方银行编码
			String PAYEEBANKNAME = "北京银行";						//收款方银行名称
			String PAYEECARDHOLDER = "张三";							//收款方银行账户名
			String PAYEEOPENBANKNAME = "北京银行望京支行";			//收款方银行卡开户行支行名称
			String PAYEEOPENBANKPROVINCE = "北京市";				//收款方银行卡开户行所在省市
			String PAYEEOPENBANKCITY = "北京市";        
			String SETTLT_PERIOD = "7";								//结算周期
			payerJson.put("accountNo",ACCOUNTNO);
			payerJson.put("amt",AMOUNT);
//			payerJson.put("outCustomerId",OUTCUSTOMERID);
			payerJson.put("outDetailNo",OUTDETAILNO);
			//payerJson.put("outTradeNo",OUTTRADENO1);
			payerJson.put("payeeBankAccountType",PAYEEBANKACCOUNTTYPE);
			payerJson.put("payeeBankCardNo",PAYEEBANKCARD);
			payerJson.put("payeeBankCode",PAYEEBANKCODE);
			payerJson.put("payeeBankName",PAYEEBANKNAME);
			payerJson.put("payeeBankCardHolder",PAYEECARDHOLDER);
			payerJson.put("payeeBankBranchName",PAYEEOPENBANKNAME);
			payerJson.put("payeeBankProvinceName",PAYEEOPENBANKPROVINCE);
			payerJson.put("payeeBankCityName",PAYEEOPENBANKCITY);
//			payerJson.put("settlt_period",SETTLT_PERIOD);
			payerList.add(payerJson);
			tamount = tamount+AMOUNT;
		}
		TOTALAMT = String.valueOf(tamount);
		params.put("totalAmt",TOTALAMT);
		params.put("timestamp",TIMESTAMP);
		params.put("payerList", payerList.toJSONString());
		String SIGNATURE = getSignature(params, KEYSECRET);	//签名
		String data = "accessToken="+ACCESSTOKEN+
					  "&appId="+APPID+
					  "&bizType="+BIZTYPE+
					  "&merchantNo="+MERCHANTNO+
					  "&outBatchNo="+OUTBATCHNO+
					  "&sourceNo="+SOURCENO+
					  "&totalAmt="+TOTALAMT+
					  "&timestamp="+TIMESTAMP+
					  "&payerList="+params.get("payerList")+
					  "&signature="+SIGNATURE;
		System.out.println("发送参数："+data);		
		String result = post(data,url);
		System.out.println("返回参数："+result);
		return result;
	}
	
	public static String getSignature(Map params,String keySecret){
		String signature = null;
		 Map<String, String> sortedParams = new TreeMap<String, String>(params);
	      //先将这些请求参数以其参数名的字典序升序进行排序
	      Set<Entry<String, String>> entrys = sortedParams.entrySet();
	      // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
	        StringBuilder basestring = new StringBuilder();
	        for (Entry<String, String> param : entrys) {
	            String key = param.getKey();
	            String value = param.getValue();

	            basestring.append(key).append("=").append(value);
	        }
	        basestring.append(keySecret);
	        // 使用MD5对待签名串求签
	        byte[] bytes = null;
	        try {
	            MessageDigest md5 = MessageDigest.getInstance("MD5");
	            try {
					bytes = md5.digest(basestring.toString().getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
	        } catch (GeneralSecurityException ex) {
	            
	        } 
	        // 将MD5输出的二进制结果转换为小写的十六进制
	        StringBuilder sign = new StringBuilder();
	        for (int i = 0; i < bytes.length; i++) {
	            String hex = Integer.toHexString(bytes[i] & 0xFF);
	            if (hex.length() == 1) {
	                sign.append("0");
	            }
	            sign.append(hex);
	        }
	        //System.out.println("中顺易接口signature:"+sign.toString());
	        return sign.toString();
	}
	
	public static String post(String data,String URL) { 
//		PrintWriter out = null;
	    OutputStream out1=null;
        BufferedReader in = null;
        String result = "";
        try{
			URL realUrl = new URL(URL);
			HttpURLConnection conn=(HttpURLConnection)realUrl.openConnection();
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            out1=conn.getOutputStream();
//            out = new PrintWriter(conn.getOutputStream());
//            out.write(new String(data.getBytes("UTF-8")));
            out1.write(data.getBytes("UTF-8"));
            out1.flush();
            in = new BufferedReader( new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		finally{
			try{
				if(out1!=null){
				    out1.close();
				}
				if(in!=null){
					in.close();
				}
			}catch(IOException ex){
				ex.printStackTrace();
			}
	     }
        return result;
	}
}
