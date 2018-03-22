package com.enation.app.ext.component.ticketgive.action;

import java.util.Random;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.goodsdiscountticketdetail.model.GoodsDiscountTicketDetail;
import com.enation.app.ext.component.goodsdiscountticketdetail.service.IGoodsDiscountTicketDetailManager;
import com.enation.app.ext.component.proxycart.service.IProxyCartManager;
import com.enation.app.ext.component.proxyorder.model.ProxyOrder;
import com.enation.app.ext.component.proxyorder.service.IProxyOrderManager;
import com.enation.app.ext.component.ticketgive.model.TicketGive;
import com.enation.app.ext.component.ticketgive.service.ITicketGiveManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.JsonMessageUtil;

public class TicketGiveAction  extends WWAction{

	private static final long serialVersionUID = -3236428968981321753L;
	
	private ITicketGiveManager ticketGiveManager;
	private IProxyOrderManager proxyOrderManager;
	private IGoodsDiscountTicketDetailManager goodsDiscountTicketDetailManager;
	
	private int proxyorderid;
	private String giveKey;
	
	public String giveTicket(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			showErrorJson("登录失效，请重新登录！");
			return "json_message";
		}
		ProxyOrder proxyOrder = this.proxyOrderManager.get(proxyorderid);
		if(proxyOrder!=null&&proxyOrder.getMemberId()==member.getMember_id()){
			TicketGive ticketGive = this.ticketGiveManager.getByProxyOrderId(proxyorderid);
			if(ticketGive!=null){
				this.json=JsonMessageUtil.getStringJson("result", "2");
				return "json_message";
			}else{
				TicketGive ticketGive2 = new TicketGive();
				String key = getRandomString(8);
				ticketGive2.setProxyorderId(proxyorderid);
				ticketGive2.setGiveKey(key);
				ticketGive2.setStatus(0);
				this.ticketGiveManager.add(ticketGive2);
				proxyOrder.setStatus(3);
				this.proxyOrderManager.update(proxyOrder);
				this.json=JsonMessageUtil.getStringJson("result", "1");
				return "json_message";
			}			
		}else{
			this.json=JsonMessageUtil.getStringJson("result", "0");
			return "json_message";
		}					
	}
	
	public String exchangeTicket(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			showErrorJson("请登录后再兑换！");
			return "json_message";
		}
		boolean check = giveKey.matches("[a-z0-9]{8}");
		if(check==false){
			this.json=JsonMessageUtil.getStringJson("result", "0");
			return "json_message";
		}
		TicketGive ticketGive = this.ticketGiveManager.getByGiveKey(giveKey);
		GoodsDiscountTicketDetail goodsDiscountTicketDetail = this.goodsDiscountTicketDetailManager.getBySendKey(giveKey);
		if(ticketGive==null&&goodsDiscountTicketDetail==null){
			this.json=JsonMessageUtil.getStringJson("result", "0");
			return "json_message";
		}
		if(ticketGive!=null){
			if(ticketGive.getStatus()==1){
				this.json=JsonMessageUtil.getStringJson("result", "2");
				return "json_message";
			}else{
				ProxyOrder proxyOrder = this.proxyOrderManager.get(ticketGive.getProxyorderId());
				if(proxyOrder.getMemberId()==member.getMember_id()){
					this.json=JsonMessageUtil.getStringJson("result", "3");
					return "json_message";
				}
				ticketGive.setMemberId(member.getMember_id());
				ticketGive.setStatus(1);
				this.ticketGiveManager.edit(ticketGive);
				proxyOrder.setStatus(4);
				this.proxyOrderManager.update(proxyOrder);
				this.json=JsonMessageUtil.getStringJson("poid",String.valueOf(ticketGive.getProxyorderId()));
				return "json_message";
			}			
		}else{
			if(goodsDiscountTicketDetail.getSendStatus()!=1){
				this.json=JsonMessageUtil.getStringJson("result", "2");
				return "json_message";
			}else{
				if(goodsDiscountTicketDetail.getBelongMemberId()==member.getMember_id()){
					this.json=JsonMessageUtil.getStringJson("result", "3");
					return "json_message";
				}
				goodsDiscountTicketDetail.setSendStatus(2);
				goodsDiscountTicketDetail.setBelongMemberId(member.getMember_id());
				this.goodsDiscountTicketDetailManager.update(goodsDiscountTicketDetail);
				this.json=JsonMessageUtil.getStringJson("tid",String.valueOf(goodsDiscountTicketDetail.getId()));
				return "json_message";
			}
		}
	}
	
	public String getRandomString(int length) { 
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 }   
	
	public ITicketGiveManager getTicketGiveManager() {
		return ticketGiveManager;
	}
	public void setTicketGiveManager(ITicketGiveManager ticketGiveManager) {
		this.ticketGiveManager = ticketGiveManager;
	}

	public int getProxyorderid() {
		return proxyorderid;
	}

	public void setProxyorderid(int proxyorderid) {
		this.proxyorderid = proxyorderid;
	}

	public String getGiveKey() {
		return giveKey;
	}

	public void setGiveKey(String giveKey) {
		this.giveKey = giveKey;
	}

	public IProxyOrderManager getProxyOrderManager() {
		return proxyOrderManager;
	}

	public void setProxyOrderManager(IProxyOrderManager proxyOrderManager) {
		this.proxyOrderManager = proxyOrderManager;
	}

	public IGoodsDiscountTicketDetailManager getGoodsDiscountTicketDetailManager() {
		return goodsDiscountTicketDetailManager;
	}

	public void setGoodsDiscountTicketDetailManager(
			IGoodsDiscountTicketDetailManager goodsDiscountTicketDetailManager) {
		this.goodsDiscountTicketDetailManager = goodsDiscountTicketDetailManager;
	}

}
