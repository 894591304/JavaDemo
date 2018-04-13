package com.enation.app.ext.component.memberproxyvip.action;

import com.enation.app.b2b2c.component.goods.model.TicketDetail;
import com.enation.app.b2b2c.component.goods.service.ITicketDetailManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.memberproxyvip.model.MemberProxyVip;
import com.enation.app.ext.component.memberproxyvip.service.IMemberProxyVipManager;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.component.proxyorder.model.ProxyOrder;
import com.enation.app.ext.component.proxyorder.service.INewOrderItemsManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderManager;
import com.enation.app.ext.component.proxyorder.service.IProxyOrderManager;
import com.enation.app.ext.component.ticketgive.service.ITicketGiveManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.app.ext.component.ticketgive.model.TicketGive;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.JsonMessageUtil;

public class ProxyVipAction extends WWAction{

	private static final long serialVersionUID = -5316856903726335418L;
	
	private int cardid;
	private IMemberProxyVipManager memberProxyVipManager;
	private INewOrderItemsManager newOrderItemsManager;
	private INewOrderManager newOrderManager;
	private IProxyOrderManager proxyOrderManager;
	private ITicketGiveManager ticketGiveManager;
	private ITicketDetailManager ticketDetailManager;
	private IProxyManager proxyManager;

	public String useVipCard(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			showErrorJson("请先登录！");
			return "json_message";
		}
		ProxyOrder proxyOrder = this.proxyOrderManager.get(cardid);
		if(proxyOrder==null){
			this.json=JsonMessageUtil.getStringJson("result", "0");//无卡券
			return "json_message";
		}
		TicketGive ticketGive = this.ticketGiveManager.getByProxyOrderId(proxyOrder.getId());
		if(proxyOrder.getMemberId()!=member.getMember_id()){
			if(ticketGive==null){
				this.json=JsonMessageUtil.getStringJson("result", "0");//不属于
				return "json_message";
			}else{				
				if(ticketGive.getMemberId()!=member.getMember_id()){
					this.json=JsonMessageUtil.getStringJson("result", "0");//不属于
					return "json_message";
				}
			}
		}else if(ticketGive!=null){
			this.json=JsonMessageUtil.getStringJson("result", "2");
			return "json_message";//已赠送
		}
		OrderItem orderItem = this.newOrderItemsManager.get(proxyOrder.getItemId());
		Order order = this.newOrderManager.getById(orderItem.getOrder_id());
		TicketDetail ticketDetail = this.ticketDetailManager.get(proxyOrder.getTicketId());
		if(ticketDetail.getStatus()==2){
			this.json=JsonMessageUtil.getStringJson("result", "3");
			return "json_message";//已使用
		}
		int proxyId = Integer.valueOf(orderItem.getAddon());
		Proxy proxy = this.proxyManager.get(proxyId);
		int memberId = member.getMember_id();
		MemberProxyVip memberProxyVip = this.memberProxyVipManager.getByMemberIdAndProxyMemberId(memberId, proxyId);
		if(memberProxyVip==null){
			memberProxyVip = new MemberProxyVip();
			memberProxyVip.setMemberId(memberId);
			memberProxyVip.setProxyMemberId(proxy.getMemberId());
			long now = System.currentTimeMillis();
			String ntime = String.valueOf(now/1000);
			memberProxyVip.setVipBT(ntime);
			if(orderItem.getGoods_id()==499){
				memberProxyVip.setV1LT(String.valueOf(360*24*60*60));
				memberProxyVip.setVipEX("360");
			}else if(orderItem.getGoods_id()==498){
				memberProxyVip.setV1LT(String.valueOf(90*24*60*60));
				memberProxyVip.setVipEX("90");
			}else if(orderItem.getGoods_id()==497){
				memberProxyVip.setV1LT(String.valueOf(30*24*60*60));
				memberProxyVip.setVipEX("30");
			}else{
				this.json=JsonMessageUtil.getStringJson("result", "4");
				return "json_message";//已参数错误
			}
			memberProxyVip.setV2LT("0");
			memberProxyVip.setV3LT("0");
			this.memberProxyVipManager.add(memberProxyVip);
			ticketDetail.setStatus(2);
			this.ticketDetailManager.update(ticketDetail);
			proxyOrder.setStatus(6);
			this.proxyOrderManager.update(proxyOrder);
			this.json=JsonMessageUtil.getStringJson("result", "1");
			return "json_message";//成功
		}else if(memberProxyVip.getV1LT()=="0"&&memberProxyVip.getV2LT()=="0"&&memberProxyVip.getV3LT()=="0"){
			long now = System.currentTimeMillis();
			String ntime = String.valueOf(now/1000);
			memberProxyVip.setVipBT(ntime);
			if(orderItem.getGoods_id()==499){
				memberProxyVip.setV1LT(String.valueOf(360*24*60*60));
				memberProxyVip.setVipEX(String.valueOf(Integer.valueOf(memberProxyVip.getVipEX())+360));
			}else if(orderItem.getGoods_id()==498){
				memberProxyVip.setV1LT(String.valueOf(90*24*60*60));
				memberProxyVip.setVipEX(String.valueOf(Integer.valueOf(memberProxyVip.getVipEX())+90));
			}else if(orderItem.getGoods_id()==497){
				memberProxyVip.setV1LT(String.valueOf(30*24*60*60));
				memberProxyVip.setVipEX(String.valueOf(Integer.valueOf(memberProxyVip.getVipEX())+30));
			}else{
				this.json=JsonMessageUtil.getStringJson("result", "4");
				return "json_message";//已参数错误
			}
			this.memberProxyVipManager.update(memberProxyVip);
			ticketDetail.setStatus(2);
			this.ticketDetailManager.update(ticketDetail);
			proxyOrder.setStatus(6);
			this.proxyOrderManager.update(proxyOrder);
			this.json=JsonMessageUtil.getStringJson("result", "1");
			return "json_message";//成功
		}else{
			int time = 0;
			long timestamp = 0;
			if(orderItem.getGoods_id()==499){
				time=360;
				timestamp = 360*24*60*60;
			}else if(orderItem.getGoods_id()==498){
				time=90;
				timestamp = 90*24*60*60;
			}else if(orderItem.getGoods_id()==497){
				time=30;
				timestamp = 30*24*60*60;
			}else{
				this.json=JsonMessageUtil.getStringJson("result", "4");
				return "json_message";//已参数错误
			}
			long v1 = Long.valueOf(memberProxyVip.getV1LT());
			long v2 = Long.valueOf(memberProxyVip.getV2LT());
			long v3 = Long.valueOf(memberProxyVip.getV3LT());
			if(v3!=0){					
				memberProxyVip.setV1LT(String.valueOf(v1+timestamp));
				memberProxyVip.setV2LT(String.valueOf(v2+timestamp));
				memberProxyVip.setV3LT(String.valueOf(v3+timestamp));
			}else if(v2!=0){					
				if(v2>timestamp){
					memberProxyVip.setV1LT(String.valueOf(v1+timestamp));
					memberProxyVip.setV2LT(String.valueOf(v2+timestamp));
					memberProxyVip.setV3LT(String.valueOf(timestamp));
				}else{
					memberProxyVip.setV1LT(String.valueOf(v1+timestamp));
					memberProxyVip.setV2LT(String.valueOf(v2+timestamp));
					memberProxyVip.setV3LT(String.valueOf(v2));
				}
			}else{
				if(v1>timestamp){
					memberProxyVip.setV1LT(String.valueOf(v1+timestamp));
					memberProxyVip.setV2LT(String.valueOf(timestamp));
				}else{
					memberProxyVip.setV1LT(String.valueOf(v1+timestamp));
					memberProxyVip.setV2LT(String.valueOf(v1));
				}
			}
			memberProxyVip.setVipEX(String.valueOf(Integer.valueOf(memberProxyVip.getVipEX())+time));
			this.memberProxyVipManager.update(memberProxyVip);
			ticketDetail.setStatus(2);
			this.ticketDetailManager.update(ticketDetail);
			proxyOrder.setStatus(6);
			this.proxyOrderManager.update(proxyOrder);
			this.json=JsonMessageUtil.getStringJson("result", "1");
			return "json_message";//成功
		}
	}

	public int getCardid() {
		return cardid;
	}

	public void setCardid(int cardid) {
		this.cardid = cardid;
	}

	public IMemberProxyVipManager getMemberProxyVipManager() {
		return memberProxyVipManager;
	}

	public void setMemberProxyVipManager(
			IMemberProxyVipManager memberProxyVipManager) {
		this.memberProxyVipManager = memberProxyVipManager;
	}

	public INewOrderItemsManager getNewOrderItemsManager() {
		return newOrderItemsManager;
	}

	public void setNewOrderItemsManager(INewOrderItemsManager newOrderItemsManager) {
		this.newOrderItemsManager = newOrderItemsManager;
	}

	public INewOrderManager getNewOrderManager() {
		return newOrderManager;
	}

	public void setNewOrderManager(INewOrderManager newOrderManager) {
		this.newOrderManager = newOrderManager;
	}

	public IProxyOrderManager getProxyOrderManager() {
		return proxyOrderManager;
	}

	public void setProxyOrderManager(IProxyOrderManager proxyOrderManager) {
		this.proxyOrderManager = proxyOrderManager;
	}

	public ITicketGiveManager getTicketGiveManager() {
		return ticketGiveManager;
	}

	public void setTicketGiveManager(ITicketGiveManager ticketGiveManager) {
		this.ticketGiveManager = ticketGiveManager;
	}

	public ITicketDetailManager getTicketDetailManager() {
		return ticketDetailManager;
	}

	public void setTicketDetailManager(ITicketDetailManager ticketDetailManager) {
		this.ticketDetailManager = ticketDetailManager;
	}

	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}
}
