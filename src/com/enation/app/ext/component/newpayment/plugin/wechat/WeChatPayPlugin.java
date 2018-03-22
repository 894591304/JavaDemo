package com.enation.app.ext.component.newpayment.plugin.wechat;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Component;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderItemsManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderManager;
import com.enation.app.ext.component.zsydetail.model.ZsyDetail;
import com.enation.app.ext.component.zsydetail.service.IZsyDetailManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.app.shop.core.model.PayCfg;
import com.enation.app.shop.core.model.PayEnable;
import com.enation.app.shop.core.plugin.payment.AbstractPaymentPlugin;
import com.enation.app.shop.core.plugin.payment.IPaymentEvent;
import com.enation.framework.context.webcontext.ThreadContextHolder;

@Component
public class WeChatPayPlugin extends AbstractPaymentPlugin implements IPaymentEvent {
	private INewOrderManager newOrderManager;
	private INewOrderItemsManager newOrderItemsManager;
	private IZsyDetailManager zsyDetailManager;
	private IProxyManager proxyManager;

	public String onCallBack(String ordertype) {
		System.out.println("调用微信h5支付的onCallBack");
		Map<String, String> cfgparams = this.paymentManager.getConfigParams(getId());
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String API_key = (String) cfgparams.get("API_key");

		String notifyXml = null;
		try {
			notifyXml = inputStream2String(request.getInputStream());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (notifyXml != null) {
			try {
				Map<String, String> notifyMap = doXMLParse(notifyXml);
				String return_code = notifyMap.get("return_code");
				String return_msg = notifyMap.get("return_msg");
				if (return_code.equals("SUCCESS")) {
					Map<String, String> validateMap = new HashMap<String, String>();
					for (Map.Entry<String, String> entry : notifyMap.entrySet()) {
						if(entry.getKey().equals("sign"))continue;
						System.out.println(entry.getKey()+":"+entry.getValue());
						validateMap.put(entry.getKey(), entry.getValue());
					}
					// 签名
					String _sign = "";
					try {
						_sign = getSign(validateMap, API_key);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					
					String sign = notifyMap.get("sign");
					String out_trade_no = notifyMap.get("out_trade_no");
					String total_fee = notifyMap.get("total_fee");
					if(sign.equals(_sign)){
						Order order = this.newOrderManager.getBySn(out_trade_no);
						int n_total_fee = (int) (order.getNeedPayMoney() * 100);
						if(String.valueOf(n_total_fee).equals(total_fee)){
							if(order.getPay_status()==2){
								return "success";
							}
							order.setPay_status(2);
							order.setStatus(2);
							order.setShip_status(1);
							order.setPaymoney(order.getNeed_pay_money());
							this.newOrderManager.update(order);
							List<OrderItem> oList = this.newOrderItemsManager.getByOrderId(order.getOrder_id());
							int os = oList.size();
							double money = 0;
							double money1 = 0;
							double money2 = 0;
							double money3 = 0;
							int id1 = 0;
							int id2 = 0;
							int id3 = 0;
							for(int o=0;o<os;o++){
								OrderItem oItem = oList.get(o);
								if(oItem.getGoods_id()==99999999){
									money1 = money1+oItem.getNum()*oItem.getPrice();
									id1 = Integer.valueOf(oItem.getAddon());
									Proxy proxy = this.proxyManager.get(id1);
									ZsyDetail zsyDetail = new ZsyDetail();
									zsyDetail.setProxyMemberId(proxy.getMemberId());
									zsyDetail.setOutCustomerId(order.getSn()+"9");
									zsyDetail.setPayDate(String.valueOf(System.currentTimeMillis()/1000));
									zsyDetail.setPayTime(7);
									float amount = (float)money1;
									zsyDetail.setAmount(amount);
									zsyDetail.setStatus(0);
									this.zsyDetailManager.add(zsyDetail);						
								}
								if(oItem.getGoods_id()==99999998){
									money2 = money2+oItem.getNum()*oItem.getPrice();
									id2 = Integer.valueOf(oItem.getAddon());
									Proxy proxy = this.proxyManager.get(id2);
									ZsyDetail zsyDetail = new ZsyDetail();
									zsyDetail.setProxyMemberId(proxy.getMemberId());
									zsyDetail.setOutCustomerId(order.getSn()+"8");
									zsyDetail.setPayDate(String.valueOf(System.currentTimeMillis()/1000));
									zsyDetail.setPayTime(7);
									float amount = (float)money2;
									zsyDetail.setAmount(amount);
									zsyDetail.setStatus(0);
									this.zsyDetailManager.add(zsyDetail);
								}
								if(oItem.getGoods_id()==99999997){
									money3 = money3+oItem.getNum()*oItem.getPrice();
									id3 = Integer.valueOf(oItem.getAddon());
									Proxy proxy = this.proxyManager.get(id3);
									ZsyDetail zsyDetail = new ZsyDetail();
									zsyDetail.setProxyMemberId(proxy.getMemberId());
									zsyDetail.setOutCustomerId(order.getSn()+"7");
									zsyDetail.setPayDate(String.valueOf(System.currentTimeMillis()/1000));
									zsyDetail.setPayTime(7);
									float amount = (float)money3;
									zsyDetail.setAmount(amount);
									zsyDetail.setStatus(0);
									this.zsyDetailManager.add(zsyDetail);
								}
							}
							String payUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
							System.out.println("付款成功，更新订单");
							HttpServletResponse response = ThreadContextHolder.getHttpResponse();
							Map<String,String> resultMap = new HashMap<String,String>();
							resultMap.put("return_code", "SUCCESS");
							resultMap.put("return_msg", "OK");
							response.getWriter().write(ArrayToXml(resultMap));
							response.getWriter().flush();
							return "success";
						}else{
							System.out.println("付款金额错误");
							return "fail";
						}
					}else{
						System.out.println("签名错误");
						return "fail";
					}
				}else{
					System.out.println(return_msg);
				}
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return "fail";
	}

	public String onPay(PayCfg payCfg, PayEnable order) {
		System.out.println("调用onPay");
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		Map<String, String> cfgparams = this.paymentManager.getConfigParams(getId());
		Map<String, String> params = new HashMap<String, String>();
		// 公众账号ID
		String appid = (String) cfgparams.get("appid");
		params.put("appid", appid);
		// 商户号
		String mch_id = (String) cfgparams.get("mch_id");
		params.put("mch_id", mch_id);
		// key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
		String API_key = (String) cfgparams.get("API_key");
		// 随机字符串
		String nonce_str = create_nonce_str();
		params.put("nonce_str", nonce_str);
		// 商户订单号
		String out_trade_no = order.getSn();
		params.put("out_trade_no", out_trade_no);
		// 商品描述
		String body = "the order is:" + order.getSn() + "for hjyx";
		params.put("body", body);
		// 标价金额
		int total_fee = (int) (order.getNeedPayMoney() * 100);
		params.put("total_fee", String.valueOf(total_fee));
		// 终端ip
		String spbill_create_ip = getAddrIp(request);
		params.put("spbill_create_ip", spbill_create_ip);
		// 通知地址
		String notify_url = getCallBackUrl(payCfg, order);
		params.put("notify_url", notify_url);
		// 交易类型
		String trade_type = "MWEB";
		params.put("trade_type", trade_type);
		// 附加数据
		String attach = "hjyx2017";
		params.put("attach", attach);
		// 签名
		String sign = "";
		try {
			sign = getSign(params, API_key);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		params.put("sign", sign);
		// 封装数据
		String postXml = ArrayToXml(params);
		// 统一下单地址
		String payUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

		String res = httpsRequest(payUrl, "POST", postXml);
		Map<String, String> xml = new HashMap<String, String>();
		try {
			xml = doXMLParse(res);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// for (Map.Entry<String, String> entry : xml.entrySet()) {
		// System.out.println("Key = " + entry.getKey() + ", Value = " +
		// entry.getValue());
		// }

		String mweb_url = xml.get("mweb_url");
		String return_url = URLDecoder.decode("http://m.huijuyouxuan.com/javamall/payment_result.html?out_trade_no="+out_trade_no);
		
		String html = "<script>window.location.href='" + mweb_url + "&redirect_url="+return_url+"'</script>";

		return html;
	}

	public String onReturn(String ordertype) {
		System.out.println("调用onReturn方法");
		Map<String, String> cfgparams = this.paymentManager.getConfigParams(getId());
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String appid = (String) cfgparams.get("appid");
		String mch_id = (String) cfgparams.get("mch_id");
		String API_key = (String) cfgparams.get("API_key");

		return "";
	}

	// 发起微信支付请求
	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			// 当outputStr不为null时向输出流写数据
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			return buffer.toString();
		} catch (ConnectException ce) {
			System.out.println("连接超时：{}" + ce);
		} catch (Exception e) {
			System.out.println("https请求异常：{}" + e);
		}
		return null;
	}

	/***
	 * map转成xml**
	 * 
	 * @param arr
	 * @return
	 */
	public String ArrayToXml(Map<String, String> arr) {
		String xml = "<xml>";

		Iterator<Entry<String, String>> iter = arr.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			String key = entry.getKey();
			String val = entry.getValue();
			xml += "<" + key + ">" + val + "</" + key + ">";
		}

		xml += "</xml>";
		return xml;
	}

	private String create_nonce_str() {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String res = "";
		for (int i = 0; i < 16; i++) {
			Random rd = new Random();
			res += chars.charAt(rd.nextInt(chars.length() - 1));
		}
		return res;
	}

	private String getAddrIp(HttpServletRequest request) {
		return request.getRemoteAddr();
	}

	private String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

	private String getSign(Map<String, String> params, String API_key) throws UnsupportedEncodingException {
		String string1 = createSign(params, false);
		String stringSignTemp = string1 + "&key=" + API_key;
		System.out.println(stringSignTemp);
		String signValue = DigestUtils.md5Hex(stringSignTemp).toUpperCase();
		return signValue;
	}

	/**
	 * 构造签名
	 * 
	 * @param params
	 * @param encode
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String createSign(Map<String, String> params, boolean encode) throws UnsupportedEncodingException {
		Set<String> keysSet = params.keySet();
		Object[] keys = keysSet.toArray();
		Arrays.sort(keys);
		StringBuffer temp = new StringBuffer();
		boolean first = true;
		for (Object key : keys) {
			if (first) {
				first = false;
			} else {
				temp.append("&");
			}
			temp.append(key).append("=");
			Object value = params.get(key);
			String valueString = "";
			if (null != value) {
				valueString = value.toString();
			}
			if (encode) {
				temp.append(URLEncoder.encode(valueString, "UTF-8"));
			} else {
				temp.append(valueString);
			}
		}
		return temp.toString();
	}

	private Map<String, String> doXMLParse(String xml) throws XmlPullParserException, IOException {

		InputStream inputStream = new ByteArrayInputStream(xml.getBytes());

		Map<String, String> map = null;

		XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();

		pullParser.setInput(inputStream, "UTF-8"); // 为xml设置要解析的xml数据

		int eventType = pullParser.getEventType();

		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				map = new HashMap<String, String>();
				break;

			case XmlPullParser.START_TAG:
				String key = pullParser.getName();
				if (key.equals("xml"))
					break;

				String value = pullParser.nextText();
				map.put(key, value);

				break;

			case XmlPullParser.END_TAG:
				break;

			}

			eventType = pullParser.next();

		}

		return map;
	}

	private String inputStream2String(InputStream in) throws UnsupportedEncodingException, IOException {
		if (in == null)
			return "";

		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n, "UTF-8"));
		}
		return out.toString();
	}

	public String getId() {
		return "weChatPayPlugin";
	}

	public String getName() {
		return "微信h5支付接口";
	}

	public INewOrderManager getNewOrderManager() {
		return newOrderManager;
	}

	public void setNewOrderManager(INewOrderManager newOrderManager) {
		this.newOrderManager = newOrderManager;
	}

	public INewOrderItemsManager getNewOrderItemsManager() {
		return newOrderItemsManager;
	}

	public void setNewOrderItemsManager(INewOrderItemsManager newOrderItemsManager) {
		this.newOrderItemsManager = newOrderItemsManager;
	}

	public IZsyDetailManager getZsyDetailManager() {
		return zsyDetailManager;
	}

	public void setZsyDetailManager(IZsyDetailManager zsyDetailManager) {
		this.zsyDetailManager = zsyDetailManager;
	}

	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}
}
