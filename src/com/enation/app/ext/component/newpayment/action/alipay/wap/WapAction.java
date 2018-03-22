package com.enation.app.ext.component.newpayment.action.alipay.wap;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderItemsManager;
import com.enation.app.ext.component.zsydetail.model.ZsyDetail;
import com.enation.app.ext.component.zsydetail.service.IZsyDetailManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.app.shop.core.plugin.payment.AbstractPaymentPlugin;


public class WapAction extends WWAction{

	private static final long serialVersionUID = 776607342221347685L;
	private INewOrderManager newOrderManager;
	private INewOrderItemsManager newOrderItemsManager;
	private IZsyDetailManager zsyDetailManager;
	private IProxyManager proxyManager;
	
	public String callBack(){
		System.out.println("调用了wap的callback");
		Map<String,String> params = new HashMap<String,String>();
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		Map requestParams = request.getParameterMap();
		System.out.println(requestParams);
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
					: valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		System.out.println(params);
		String ALIPAY_PUBLIC_KEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
		try {
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
			System.out.println(out_trade_no);
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
			System.out.println(trade_no);
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
			System.out.println(trade_status);
			Order order = this.newOrderManager.getBySn(out_trade_no);
			boolean verify_result;
			try {
				verify_result = AlipaySignature.rsaCheckV1(params,ALIPAY_PUBLIC_KEY,"UTF-8", "RSA");
				System.out.println(verify_result);
				if(verify_result){
					if(trade_status.equals("TRADE_FINISHED")){
						System.out.println("TRADE_FINISHED");
					} else if (trade_status.equals("TRADE_SUCCESS")){
						System.out.println("TRADE_SUCCESS");
					}
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
					return "success";
				}else{
					System.out.println("支付失败！");
					String sWord = AlipaySignature.getSignCheckContentV1(params);
					System.out.println(sWord);
					return "fail";
				}
			} catch (AlipayApiException e) {
				String sWord = AlipaySignature.getSignCheckContentV1(params);
				System.out.println(sWord);
				e.printStackTrace();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "fail";
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
