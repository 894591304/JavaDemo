package com.enation.app.ext.component.newpayment.plugin.alipay.wap;

import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.*;
import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayNotify;
import com.alipay.util.AlipaySubmit;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.domain.AlipayTradeWapPayModel;

import com.enation.app.shop.core.model.PayCfg;
import com.enation.app.shop.core.model.PayEnable;
import com.enation.app.shop.core.plugin.payment.AbstractPaymentPlugin;
import com.enation.app.shop.core.plugin.payment.IPaymentEvent;
import com.enation.app.shop.core.service.IPaymentManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class AlipayWapPlugin extends AbstractPaymentPlugin implements IPaymentEvent
{
	public String onCallBack(String ordertype)
	{
		System.out.println("调用onCallBack");
		Map<String, String> cfgparams = this.paymentManager.getConfigParams(getId());
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String key = (String)cfgparams.get("key");
		System.out.println(key);
		String partner = (String)cfgparams.get("partner");
		System.out.println(partner);
		String encoding = (String)cfgparams.get("callback_encoding");    
		System.out.println(encoding);
		Map<String, String> params = new HashMap();
		Map requestParams = request.getParameterMap();
		System.out.println(requestParams);
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String)iter.next();
			String[] values = (String[])requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = valueStr + values[i] + ",";
			}
			if (!StringUtil.isEmpty(encoding)) {
				valueStr = StringUtil.to(valueStr, encoding);
			}
			params.put(name, valueStr);
		}
		System.out.println(params);
		String trade_no = request.getParameter("trade_no");
		System.out.println(trade_no);
		String order_no = request.getParameter("out_trade_no");
		System.out.println(order_no);
		String total_fee = request.getParameter("total_fee");
		System.out.println(total_fee);
		String subject = request.getParameter("subject");
		System.out.println(subject);
		if (!StringUtil.isEmpty(encoding)) {
			subject = StringUtil.to(subject, encoding);
		}
		System.out.println(subject);
		String body = "";
		if (request.getParameter("body") != null) {
			body = request.getParameter("body");
			if (!StringUtil.isEmpty(encoding)) {
				body = StringUtil.to(body, encoding);
			}
		}
		System.out.println(body);
		String buyer_email = request.getParameter("buyer_email");
		System.out.println(buyer_email);
		String trade_status = request.getParameter("trade_status");
		System.out.println(trade_status);
		if (AlipayNotify.callbackverify(params, key, partner))
		{
			paySuccess(order_no, trade_no, ordertype);
			System.out.println("paysuccess!");
			if ((trade_status.equals("TRADE_FINISHED")) || (trade_status.equals("TRADE_SUCCESS")))
			{
				System.out.println("成功1");
				this.logger.info("异步校验订单[" + order_no + "]成功");
				return "success";
			}
			System.out.println("成功2");
			this.logger.info("异步校验订单[" + order_no + "]成功");
			return "success";
		}
		System.out.println("失败1");
		this.logger.info("异步校验订单[" + order_no + "]失败");
		return "fail";
	}
  
  
  
  
  public String on1Pay(PayCfg payCfg, PayEnable order)
  {	
     Map<String, String> params = this.paymentManager.getConfigParams(getId());
     String seller_email = (String)params.get("seller_email");
     String partner = (String)params.get("partner");
     String key = (String)params.get("key");
     String show_url = getShowUrl(order);
     String notify_url = getCallBackUrl(payCfg, order);
     notify_url = "http://m.huijuyouxuan.com/javamall/shop/ext/wap!callBack.do";
     String return_url = getReturnUrl(payCfg, order);
     this.logger.info("notify_url is [" + notify_url + "]");
     String out_trade_no = order.getSn();  
     String subject = "订单:" + order.getSn();   
     String body = "网店订单";   
     String total_fee = String.valueOf(order.getNeedPayMoney());
     String paymethod = "";
     HttpServletRequest request = ThreadContextHolder.getHttpRequest();
     String defaultbank = request.getParameter("bank");
     defaultbank = defaultbank == null ? "" : defaultbank;
     Pattern pattern = Pattern.compile("[0-9]*");
     if (pattern.matcher(defaultbank).matches()) {
       defaultbank = "";
    }
     String anti_phishing_key = "";
     String exter_invoke_ip = "";
     String extra_common_param = "";
     String buyer_email = "";
     String royalty_type = "";
     String royalty_parameters = "";
     Map<String, String> sParaTemp = new HashMap();
     sParaTemp.put("payment_type", "1");
     sParaTemp.put("out_trade_no", out_trade_no);
     sParaTemp.put("subject", subject);
     sParaTemp.put("body", body);
     sParaTemp.put("total_fee", total_fee);
     sParaTemp.put("show_url", show_url);
     sParaTemp.put("paymethod", paymethod);
     sParaTemp.put("defaultbank", defaultbank);
     sParaTemp.put("anti_phishing_key", anti_phishing_key);
     sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
     sParaTemp.put("extra_common_param", extra_common_param);
     sParaTemp.put("buyer_email", buyer_email);
     sParaTemp.put("royalty_type", royalty_type);
     sParaTemp.put("royalty_parameters", royalty_parameters);
     sParaTemp.put("service", "alipay.wap.create.direct.pay.by.user");
     sParaTemp.put("partner", partner);
     sParaTemp.put("return_url", return_url);
     sParaTemp.put("notify_url", notify_url);
     sParaTemp.put("seller_id", partner);
     sParaTemp.put("_input_charset", AlipayConfig.input_charset);
     return AlipaySubmit.buildForm(sParaTemp, "https://mapi.alipay.com/gateway.do?", "get", key);
  }
  public String onPay(PayCfg payCfg, PayEnable order)
  {
	  	Map<String, String> params = this.paymentManager.getConfigParams(getId());
	  	String RSA_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANPaDrGQ5G+IabRLm0MPXILTLWEdJFfDO+43uiDX2vQRlA3ZKI8zodfT+eKMAbjMVymN64H+cXjglzz567P7j3WaVKYa+2sUY1QoVWJ4BfxaBbCKN0BWxVrC/N3L/J6vgcEg2R7K+9ecDYXWW467asbs9fv1uW66QNl/i4Lcy/ojAgMBAAECgYBWgnMBm5NM4D2Z/E3Yoos6eRXM1GOk2vq8GlGNW9fzVsJIrWbco07xnYQrONTzK65kL6n+GXMo0z2vDrdrdcxskW7mKmFLVQ4+91BmYOsr08iyqhxs56wczPsDFtYx1atxo5XF14xjWxBm+cOH0h77pH3KYrhUBP7sCv3vbWxz4QJBAO2Ljq9Tfw376asiGS5h8qv1S5/XXkMD8F9pZDceeFkDm+V0v6xTb2EdEkj/uYjsPggkcSFe06qR2rPZ0kGL6HkCQQDkT36uQhs4JnHgyVG7MO9YgKKRpp8/4eWCb+iaiZ6yMotAQdNLGS3nOL/UC4+BD2Hjtt1NDGN9jCCo0UDE64h7AkEA7EmTxubBTYG1r5sdfTvFDjPT481xYCv84IuPOhy/DZw/aIE3YA0mj21BkQd+4KpOEf9d4RnYZqTq7VfQg47N0QJAQWgAu71WfOyOnT4vevgW6XzYle9tGekGTirvMS6R1y0htYPmG1KYUvBwDDhPoUKd4ZWmqocNDY+SX6qq6n8rdwJBAO1NGLmiZf4v3fsMDdEZCSkCxVVJtf+aTGgojeeHmolpji0oVUwaTm1ltXU7klhoGWBBO12FQ6yu1Oodm4wtYYw=";
	  	String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
	  	String notify_url = "http://m.huijuyouxuan.com/javamall/shop/ext/wap!callBack.do";
	  	String return_url = getReturnUrl(payCfg, order);
	  	// 商户订单号，商户网站订单系统中唯一订单号，必填
	    String out_trade_no = order.getSn();
		// 订单名称，必填
	    String subject = "订单:" + order.getSn(); 
		System.out.println(subject);
	    // 付款金额，必填
	    String total_amount=String.valueOf(order.getNeedPayMoney());
	    // 商品描述，可空
	    String body = "网店订单";
	    // 销售产品码 必填
	    String product_code="QUICK_WAP_PAY";
	    /**********************/
	    // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签     
	    //调用RSA签名方式
	    AlipayClient client = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do","2017022705929713",RSA_PRIVATE_KEY,"json","UTF-8",ALIPAY_PUBLIC_KEY,"RSA");
	    AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();
	    
	    // 封装请求支付信息
	    AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
	    model.setOutTradeNo(out_trade_no);
	    model.setSubject(subject);
	    model.setTotalAmount(total_amount);
	    model.setBody(body);
	    model.setProductCode(product_code);
	    alipay_request.setBizModel(model);
	    // 设置异步通知地址
	    alipay_request.setNotifyUrl(notify_url);
	    // 设置同步地址
	    alipay_request.setReturnUrl(return_url);   
	    
	    // form表单生产
	    String form = "";
	    try {
			form = client.pageExecute(alipay_request).getBody();
			System.out.println(form);
			return form;
		} catch (AlipayApiException e) {
			e.printStackTrace();
			System.out.println("生产失败！");
			return null;
		} 
  }
  





  public String onReturn(String ordertype)
  {
     Map<String, String> cfgparams = this.paymentManager.getConfigParams(getId());
     HttpServletRequest request = ThreadContextHolder.getHttpRequest();
     String key = (String)cfgparams.get("key");
     String partner = (String)cfgparams.get("partner");
     String encoding = (String)cfgparams.get("return_encoding");
    

     Map<String, String> params = new HashMap();
     Map requestParams = request.getParameterMap();
     for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
       String name = (String)iter.next();
       String[] values = (String[])requestParams.get(name);
       String valueStr = "";
       for (int i = 0; i < values.length; i++) {
         valueStr = valueStr + values[i] + ",";
      }
      

       if (!StringUtil.isEmpty(encoding)) {
         valueStr = StringUtil.to(valueStr, encoding);
      }
       params.put(name, valueStr);
    }
    



     String trade_no = request.getParameter("trade_no");
     String order_no = request.getParameter("out_trade_no");
     String total_fee = request.getParameter("total_fee");
     String subject = request.getParameter("subject");
    
     if (!StringUtil.isEmpty(encoding)) {
       subject = StringUtil.to(subject, encoding);
    }
    
     String body = "";
     if (request.getParameter("body") != null) {
       body = request.getParameter("body");
       if (!StringUtil.isEmpty(encoding)) {
         body = StringUtil.to(body, encoding);
      }
    }
    
     String buyer_email = request.getParameter("buyer_email");
     String trade_status = request.getParameter("trade_status");
    



     boolean verify_result = AlipayNotify.returnverify(params, key, partner);
    
     if (verify_result)
    {
       paySuccess(order_no, trade_no, ordertype);
       return order_no;
    }
    
     throw new RuntimeException("验证失败");
  }
  
  public String getId()
  {
     return "alipayWapPlugin";
  }
  

  public String getName()
  {
     return "支付宝Wap支付接口";
  }
}

