package com.enation.app.ext.zsy.utils;

public class ConfigUtil {
	/**
	 * 中顺易服务号相关信息
	 */
	public final static String APPID = "443666b807444eb7806488b65c2ff72c";//应用Id
	public final static String SOURCENO = "1000322031";//渠道号
	public final static String MERCHANTNO = "1000322031";//区分交易属于哪个商户的参数,暂与渠道号相同
	public final static String KEYSECRET = "559a3028592941798d402e64637b9f43";//签名的secret（appkey）
	
	/**
	 * 中顺易基础接口地址
	 */
	// 获取acces_token接口(POST)	
	public final static String TOKEN_URL = "https://uat-open.zsyjr.com/auth/service_access_token";
	// 批量充值登记接口（POST）		
	public final static String RECHARGE_REGISTER_URL = "https://uat-open.zsyjr.com/service/batch_recharge_register";
	// 批量代付接口（POST）
	public final static String PAYOUT_URL = "https://uat-open.zsyjr.com/service/general/ct/batchPayout";
	
}
