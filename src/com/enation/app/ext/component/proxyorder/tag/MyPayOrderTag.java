package com.enation.app.ext.component.proxyorder.tag;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.goods.model.TicketDetail;
import com.enation.app.b2b2c.component.goods.service.ITicketDetailManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.goodsagent.model.GoodsAgent;
import com.enation.app.ext.component.goodsagent.service.IGoodsAgentManager;
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
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class MyPayOrderTag extends BaseFreeMarkerTag{
	
	private INewOrderManager newOrderManager;
	private IProxyOrderManager proxyOrderManager;
	private INewOrderItemsManager newOrderItemsManager;
	private ITicketDetailManager ticketDetailManager;
	private IGoodsProxyManager goodsProxyManager;
	private IProxyCountManager proxyCountManager;
	
	
	private IProxyManager proxyManager;
	private IGoodsAgentManager goodsAgentManager;
	private IUserAccountManager userAccountManager;
	private ITicketGiveManager ticketGiveManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Map result = new HashMap();
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[MyPayOrderTag]");
		}
		List payList = new ArrayList();
		int paycount = 0;
		List usedList = new ArrayList();
		int usedcount = 0;
		List notPayOrderList = new ArrayList();
		int notpaycount = 0;
		List vipOrderList = new ArrayList();
		int vipcount = 0;
		//checkMyTicket(member.getMember_id());
		List<TicketGive> tList = this.ticketGiveManager.getByMemberId(member.getMember_id());
		int tc = tList.size();
		for(int t=0;t<tc;t++){
			Map tMap = new HashMap();
			TicketGive ticketGive2 = tList.get(t);
			ProxyOrder proxyOrder2 = this.proxyOrderManager.get(ticketGive2.getProxyorderId());
			TicketDetail ticketDetail2 = this.ticketDetailManager.get(proxyOrder2.getTicketId());
			OrderItem orderItem2 = this.newOrderItemsManager.get(proxyOrder2.getItemId());
			if(orderItem2.getGoods_id()!=499&&orderItem2.getGoods_id()!=498&&orderItem2.getGoods_id()!=497)
			{
				Goods goods2 = this.goodsProxyManager.getGoods(orderItem2.getGoods_id());
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
				long limittime2 = new Long(ticketDetail2.getEnddate());
				long begintime2 = new Long(ticketDetail2.getStartdate());
				Date ndate = new Date();
				long nowtime = ndate.getTime();
				Date ldate = new Date(limittime2);
				String lt = simpleDateFormat.format(ldate);
				if(nowtime>begintime2&&nowtime<limittime2){
					tMap.put("canuse",1);
				}else if(nowtime<begintime2){
					tMap.put("canuse",2);
				}else{
					tMap.put("canuse",0);
				}
				tMap.put("name",goods2.getName());
				tMap.put("limittime",lt);
				tMap.put("price",goods2.getPrice());
				tMap.put("proxyid",String.valueOf(orderItem2.getAddon()));
				tMap.put("proxyorderid",String.valueOf(ticketGive2.getProxyorderId()));
				if(proxyOrder2.getStatus()==4){	
					payList.add(tMap);
					paycount++;
				}else if(proxyOrder2.getStatus()==5){
					usedList.add(tMap);
					usedcount++;
				}
			}else{
				Goods goods2 = this.goodsProxyManager.getGoods(orderItem2.getGoods_id());
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
				long limittime2 = new Long(ticketDetail2.getEnddate());
				long begintime2 = new Long(ticketDetail2.getStartdate());
				Date ndate = new Date();
				long nowtime = ndate.getTime();
				Date ldate = new Date(limittime2);
				String lt = simpleDateFormat.format(ldate);
				if(nowtime>begintime2&&nowtime<limittime2){
					tMap.put("canuse",1);
				}else if(nowtime<begintime2){
					tMap.put("canuse",2);
				}else{
					tMap.put("canuse",0);
				}
				tMap.put("name",goods2.getName());
				tMap.put("limittime",lt);
				tMap.put("price",goods2.getPrice());
				tMap.put("proxyid",String.valueOf(orderItem2.getAddon()));
				tMap.put("proxyorderid",String.valueOf(ticketGive2.getProxyorderId()));
				if(proxyOrder2.getStatus()==4){	
					vipOrderList.add(tMap);
					vipcount++;
				}else if(proxyOrder2.getStatus()==5){
					usedList.add(tMap);
					usedcount++;
				}
			}
		}
		
		List<ProxyOrder> pList = this.proxyOrderManager.getAllByMemberid(member.getMember_id());
		int count = pList.size();
		for(int i=0;i<count;i++){
			Map payMap = new HashMap();
			TicketDetail ticketDetail = this.ticketDetailManager.get(pList.get(i).getTicketId());
			OrderItem orderItem = this.newOrderItemsManager.get(pList.get(i).getItemId());
			if(orderItem.getGoods_id()!=499&&orderItem.getGoods_id()!=498&&orderItem.getGoods_id()!=497)
			{
				Goods goods = this.goodsProxyManager.getGoods(orderItem.getGoods_id());
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
				long limittime = new Long(ticketDetail.getEnddate());
				long begintime = new Long(ticketDetail.getStartdate());
				Date ndate = new Date();
				long nowtime = ndate.getTime();
				Date ldate = new Date(limittime);
				String lt = simpleDateFormat.format(ldate);
				if(nowtime>begintime&&nowtime<limittime){
					payMap.put("canuse",1);
				}else if(nowtime<begintime){
					payMap.put("canuse",2);
				}else{
					payMap.put("canuse",0);
				}
				payMap.put("name",goods.getName());
				payMap.put("limittime",lt);
				payMap.put("price",goods.getPrice());
				payMap.put("proxyid",String.valueOf(orderItem.getAddon()));
				payMap.put("proxyorderid",String.valueOf(pList.get(i).getId()));
				if(pList.get(i).getStatus()==0){	
					payList.add(payMap);
					paycount++;
				}else if(pList.get(i).getStatus()==1||pList.get(i).getStatus()==3||pList.get(i).getStatus()==4||pList.get(i).getStatus()==5){
					usedList.add(payMap);
					usedcount++;
				}
			}else{
				Goods goods = this.goodsProxyManager.getGoods(orderItem.getGoods_id());
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
				long limittime = new Long(ticketDetail.getEnddate());
				long begintime = new Long(ticketDetail.getStartdate());
				Date ndate = new Date();
				long nowtime = ndate.getTime();
				Date ldate = new Date(limittime);
				String lt = simpleDateFormat.format(ldate);
				if(nowtime>begintime&&nowtime<limittime){
					payMap.put("canuse",1);
				}else if(nowtime<begintime){
					payMap.put("canuse",2);
				}else{
					payMap.put("canuse",0);
				}
				payMap.put("name",goods.getName());
				payMap.put("limittime",lt);
				payMap.put("price",goods.getPrice());
				payMap.put("proxyid",String.valueOf(orderItem.getAddon()));
				payMap.put("proxyorderid",String.valueOf(pList.get(i).getId()));
				if(pList.get(i).getStatus()==0||pList.get(i).getStatus()==1||pList.get(i).getStatus()==3){	
					vipOrderList.add(payMap);
					vipcount++;
				}
			}
		}
		List<Order> oList = this.newOrderManager.getNotPayOrder(member.getMember_id());
		int count2 = oList.size();
		for(int j=0;j<count2;j++){
			Map orderMap = new HashMap();
			Order o = oList.get(j);
			orderMap.put("money", o.getOrder_amount());
			orderMap.put("orderid",String.valueOf(o.getOrder_id()));
			orderMap.put("ordersn",String.valueOf(o.getSn()));
			notPayOrderList.add(orderMap);
			notpaycount++;
		}
		result.put("paylist",payList);
		result.put("paycount",paycount);
		result.put("notpayorderlist",notPayOrderList);
		result.put("notpaycount",notpaycount);
		result.put("usedlist",usedList);
		result.put("usedcount",usedcount);
		result.put("viporderlist",vipOrderList);
		result.put("vipcount",vipcount);
		return result;
	}
	public void checkMyTicket(int memberid){
		List<Order> oList = this.newOrderManager.getByMemberid(memberid);
		int count = oList.size();
		for(int i=0;i<count;i++){
			Order o = new Order();
			o = oList.get(i);
			if(o.getPay_status()==2||o.getStatus()==2){
				if(o.getShip_status()==1||o.getShip_status()==2){
					List<OrderItem> oiList = null;
					oiList = this.newOrderItemsManager.getByOrderId(o.getOrder_id());
					int count2 = oiList.size();
					for(int j=0;j<count2;j++){
						OrderItem oi = oiList.get(j);
						if(this.proxyOrderManager.getByItemId(oi.getItem_id()).size()!=oi.getNum()){
							addTicket(memberid,oi.getItem_id(),oi.getNum()-this.proxyOrderManager.getByItemId(oi.getItem_id()).size());
						}
					}
					o.setSigning_time(Integer.valueOf(String.valueOf(System.currentTimeMillis()/1000)));
					o.setThe_sign("系统已配发提货码");
					o.setShip_status(9);
					this.newOrderManager.update(o);
				}				
			}
		}		
	}
	
	public void addTicket(int memberid,int itemid,int num){
		OrderItem oi = new OrderItem();
		oi = this.newOrderItemsManager.get(itemid);
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
				System.out.println("兑换码不足！");
			}
		}
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
	public IProxyCountManager getProxyCountManager() {
		return proxyCountManager;
	}
	public void setProxyCountManager(IProxyCountManager proxyCountManager) {
		this.proxyCountManager = proxyCountManager;
	}
	public ITicketGiveManager getTicketGiveManager() {
		return ticketGiveManager;
	}
	public void setTicketGiveManager(ITicketGiveManager ticketGiveManager) {
		this.ticketGiveManager = ticketGiveManager;
	}

	
}
