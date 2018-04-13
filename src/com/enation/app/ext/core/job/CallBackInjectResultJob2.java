package com.enation.app.ext.core.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.enation.app.b2b2c.component.goods.model.TicketDetail;
import com.enation.app.b2b2c.component.goods.service.ITicketDetailManager;
import com.enation.app.ext.component.goodsagent.model.GoodsAgent;
import com.enation.app.ext.component.goodsagent.service.IGoodsAgentManager;
import com.enation.app.ext.component.goodsdiscountticketdetail.model.GoodsDiscountTicketDetail;
import com.enation.app.ext.component.goodsdiscountticketdetail.service.IGoodsDiscountTicketDetailManager;
import com.enation.app.ext.component.memberproxyvip.model.MemberProxyVip;
import com.enation.app.ext.component.memberproxyvip.service.IMemberProxyVipManager;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.component.proxycount.service.IProxyCountManager;
import com.enation.app.ext.component.proxyorder.model.ProxyOrder;
import com.enation.app.ext.component.proxyorder.service.INewOrderItemsManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderManager;
import com.enation.app.ext.component.proxyorder.service.IProxyOrderManager;
import com.enation.app.ext.component.ticketgive.model.TicketGive;
import com.enation.app.ext.component.ticketgive.service.ITicketGiveManager;
import com.enation.app.ext.component.useraccount.model.UserAccount;
import com.enation.app.ext.component.useraccount.service.IUserAccountManager;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.framework.util.JsonMessageUtil;

/*
 * 此定时任务每分钟自动执行一次
 */

public class CallBackInjectResultJob2 {

	private INewOrderManager newOrderManager;
	private IProxyOrderManager proxyOrderManager;
	private INewOrderItemsManager newOrderItemsManager;
	private ITicketDetailManager ticketDetailManager;
	private IGoodsProxyManager goodsProxyManager;
	private IProxyCountManager proxyCountManager;
	private IGoodsDiscountTicketDetailManager goodsDiscountTicketDetailManager;
	
	private IProxyManager proxyManager;
	private IGoodsAgentManager goodsAgentManager;
	private IUserAccountManager userAccountManager;
	private ITicketGiveManager ticketGiveManager;
	private IMemberProxyVipManager memberProxyVipManager;
	
	
    public void execute() {
    	checkOrderTime();   //过期订单
    	checkAllTicket();	//全局发货
    	vipCardAutoUse();	//VIP卡自动使用
    	Date date = new Date();
    	SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日hh时mm分ss秒");
        System.out.println(df.format(date)+"执行了order相关定时任务！");
    }
    
    public void checkOrderTime(){
    	List<Order> oList = this.newOrderManager.checkOrderTime();
		int total1 = oList.size();
		for(int i=0;i<total1;i++){
			Order order = oList.get(i);
			List<OrderItem> oItemsList = this.newOrderItemsManager.getByOrderId(order.getOrder_id());
			int total2 = oItemsList.size();
			for(int t=0;t<total2;t++){
				OrderItem orderItem = oItemsList.get(t);
				GoodsDiscountTicketDetail goodsDiscountTicketDetail = this.goodsDiscountTicketDetailManager.getByItemId(orderItem.getItem_id());
				if(goodsDiscountTicketDetail!=null){
					goodsDiscountTicketDetail.setUseStatus(0);
					goodsDiscountTicketDetail.setUseOrderItemId(0);
					this.goodsDiscountTicketDetailManager.update(goodsDiscountTicketDetail);
				}
				Proxy proxy = this.proxyManager.get(Integer.valueOf(orderItem.getAddon()));
				proxy.setSale(proxy.getSale()-orderItem.getNum());
				this.proxyManager.edit(proxy);
			}
			order.setStatus(8);
			order.setCancel_reason("长时间未付款，自动取消！");
			this.newOrderManager.update(order);
		}
    }
    
    public void checkAllTicket(){
    	List<Order> oList = this.newOrderManager.getAll();
		int count = oList.size();
		for(int i=0;i<count;i++){
			Order o = new Order();
			o = oList.get(i);
			int memberid = o.getMember_id();
			if(o.getPay_status()==2||o.getStatus()==2){
				if(o.getShip_status()==1||o.getShip_status()==2){
					List<OrderItem> oiList = null;
					oiList = this.newOrderItemsManager.getByOrderId(o.getOrder_id());
					int count2 = oiList.size();
					boolean b = true;
					for(int j=0;j<count2;j++){
						OrderItem oi = oiList.get(j);
						boolean a = true;
						if(this.proxyOrderManager.getByItemId(oi.getItem_id()).size()!=oi.getNum()){
							a = addTicket(memberid,oi.getItem_id(),oi.getNum()-this.proxyOrderManager.getByItemId(oi.getItem_id()).size());
						}
						if(a==false){
							b=false;
						}
					}
					if(b==true){
						o.setSigning_time(Integer.valueOf(String.valueOf(System.currentTimeMillis()/1000)));
						o.setThe_sign("系统已配发提货码");
						o.setShip_status(9);
						this.newOrderManager.update(o);
					}else{
						System.out.println("部分提货券已配发，但提货码不足，未全部配发。");
					}
				}				
			}
		}		
	}
    
    public boolean addTicket(int memberid,int itemid,int num){
		OrderItem oi = new OrderItem();
		oi = this.newOrderItemsManager.get(itemid);
		boolean b = true;
		for(int i=0;i<num;i++){
			int ticketid = this.ticketDetailManager.getNewKey(oi.getGoods_id());
			Proxy proxy = this.proxyManager.get(Integer.valueOf(oi.getAddon()));
			Order order = this.newOrderManager.getById(oi.getOrder_id());
			GoodsAgent goodsAgent = this.goodsAgentManager.get(proxy.getGoodsId());
			Goods goods = this.goodsProxyManager.getGoods(proxy.getGoodsId());
			int level = this.userAccountManager.getLevel(proxy.getMemberId());
			if(ticketid!=0){
				float proxyPrice = 0;
				if(level==1){
					proxyPrice = goodsAgent.getGoldPrice();
				}else if(level==2){
					proxyPrice = goodsAgent.getPlatinumPrice();
				}else{
					proxyPrice = goodsAgent.getBlackPrice();
				}
				ProxyOrder po = new ProxyOrder();
				po.setMemberId(memberid);
				po.setItemId(itemid);
				po.setTicketId(ticketid);
				po.setStatus(0);
				po.setProxyMemberId(proxy.getMemberId());
				po.setSoldTime(String.valueOf(order.getCreate_time()));
				po.setGoodsId(proxy.getGoodsId());
				po.setProxyPrice(proxyPrice);
				po.setPrice(Float.valueOf(String.valueOf(goods.getPrice())));
				this.proxyOrderManager.add(po);
				proxyCheckOut(Integer.valueOf(oi.getAddon()));
				this.proxyCountManager.addSell(oi.getGoods_id(),1);
			}else{
				System.out.println("兑换码不足！商品ID为："+goods.getGoods_id());
				b=false;
			}
		}
		return b;
	}
	
	public void proxyCheckOut(int proxyid)
	{
		Proxy proxy = this.proxyManager.get(proxyid);
		GoodsAgent goodsAgent = this.goodsAgentManager.get(proxy.getGoodsId());
		UserAccount userAccount = this.userAccountManager.getByMemberId(proxy.getMemberId());
		float dlPrice = 0;
		if(userAccount.getCreditLevel()==1){
			dlPrice = goodsAgent.getGoldPrice();
		}else if(userAccount.getCreditLevel()==2){
			dlPrice = goodsAgent.getPlatinumPrice();
		}else{
			dlPrice = goodsAgent.getBlackPrice();
		}		
		//proxy.setSale(proxy.getSale()+1);
		//System.out.println("销量已增加！");
		proxy.setFrozenEarn(proxy.getFrozenEarn()+goodsAgent.getPrice()-dlPrice);
		System.out.println("锁定收益已更新！");
		this.proxyManager.edit(proxy);
		System.out.println("销售成功！代理信息已更新！");
	}
	
	public void vipCardAutoUse(){
		List<ProxyOrder> pList = this.proxyOrderManager.getVipCardOutOfTime();
		int count = 0;
		for(int i=0;i<count;i++){
			ProxyOrder proxyOrder = pList.get(0);
			TicketDetail ticketDetail = this.ticketDetailManager.get(proxyOrder.getTicketId());
			if(ticketDetail==null){
				System.out.println(proxyOrder.getGoodsId()+"号vip卡缺货！");
			}else if(ticketDetail.getStatus()==0){
				TicketGive ticketGive = this.ticketGiveManager.getByProxyOrderId(proxyOrder.getId());
				if(ticketGive==null){
					useVipCard(proxyOrder, proxyOrder.getMemberId());
				}else{
					if(ticketGive.getMemberId()==0){
						ticketGive.setStatus(1);
						ticketGive.setMemberId(proxyOrder.getMemberId());
						this.ticketGiveManager.edit(ticketGive);
						useVipCard(proxyOrder, proxyOrder.getMemberId());
					}else{
						useVipCard(proxyOrder,ticketGive.getMemberId());
					}
				}
				ticketDetail.setStatus(2);
				this.ticketDetailManager.update(ticketDetail);
			}
		}

	}
	
	public void useVipCard(ProxyOrder proxyOrder,int memberId){
		System.out.println(memberId+"号用户已自动使用"+proxyOrder.getGoodsId()+"号VIP卡！");
		OrderItem orderItem = this.newOrderItemsManager.get(proxyOrder.getItemId());
		int proxyId = Integer.valueOf(orderItem.getAddon());
		Proxy proxy = this.proxyManager.get(proxyId);
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
			}
			memberProxyVip.setV2LT("0");
			memberProxyVip.setV3LT("0");
			this.memberProxyVipManager.add(memberProxyVip);
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
			}
			this.memberProxyVipManager.update(memberProxyVip);
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
			TicketDetail ticketDetail = this.ticketDetailManager.get(proxyOrder.getTicketId());
			ticketDetail.setStatus(2);
			this.ticketDetailManager.update(ticketDetail);
			proxyOrder.setStatus(6);
			this.proxyOrderManager.update(proxyOrder);
		}
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

	public INewOrderItemsManager getNewOrderItemsManager() {
		return newOrderItemsManager;
	}

	public void setNewOrderItemsManager(INewOrderItemsManager newOrderItemsManager) {
		this.newOrderItemsManager = newOrderItemsManager;
	}

	public ITicketDetailManager getTicketDetailManager() {
		return ticketDetailManager;
	}

	public void setTicketDetailManager(ITicketDetailManager ticketDetailManager) {
		this.ticketDetailManager = ticketDetailManager;
	}

	public IGoodsProxyManager getGoodsProxyManager() {
		return goodsProxyManager;
	}

	public void setGoodsProxyManager(IGoodsProxyManager goodsProxyManager) {
		this.goodsProxyManager = goodsProxyManager;
	}

	public IProxyCountManager getProxyCountManager() {
		return proxyCountManager;
	}

	public void setProxyCountManager(IProxyCountManager proxyCountManager) {
		this.proxyCountManager = proxyCountManager;
	}

	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

	public IGoodsAgentManager getGoodsAgentManager() {
		return goodsAgentManager;
	}

	public void setGoodsAgentManager(IGoodsAgentManager goodsAgentManager) {
		this.goodsAgentManager = goodsAgentManager;
	}

	public IUserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(IUserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

	public IGoodsDiscountTicketDetailManager getGoodsDiscountTicketDetailManager() {
		return goodsDiscountTicketDetailManager;
	}

	public void setGoodsDiscountTicketDetailManager(
			IGoodsDiscountTicketDetailManager goodsDiscountTicketDetailManager) {
		this.goodsDiscountTicketDetailManager = goodsDiscountTicketDetailManager;
	}

	public ITicketGiveManager getTicketGiveManager() {
		return ticketGiveManager;
	}

	public void setTicketGiveManager(ITicketGiveManager ticketGiveManager) {
		this.ticketGiveManager = ticketGiveManager;
	}

	public IMemberProxyVipManager getMemberProxyVipManager() {
		return memberProxyVipManager;
	}

	public void setMemberProxyVipManager(
			IMemberProxyVipManager memberProxyVipManager) {
		this.memberProxyVipManager = memberProxyVipManager;
	}
}
