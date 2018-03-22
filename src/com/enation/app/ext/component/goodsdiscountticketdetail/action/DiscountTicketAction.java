package com.enation.app.ext.component.goodsdiscountticketdetail.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.goodsagent.model.GoodsAgent;
import com.enation.app.ext.component.goodsagent.service.IGoodsAgentManager;
import com.enation.app.ext.component.goodsdiscountticketdetail.model.GoodsDiscountTicketDetail;
import com.enation.app.ext.component.goodsdiscountticketdetail.service.IGoodsDiscountTicketDetailManager;
import com.enation.app.ext.component.memberproxyvip.model.MemberProxyVip;
import com.enation.app.ext.component.memberproxyvip.service.IMemberProxyVipManager;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderItemsManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonMessageUtil;

public class DiscountTicketAction extends WWAction{

	private static final long serialVersionUID = -4901058544250965117L;

	private int num;
	private int proxyid;
	private float ticketvalue;
	private int memberid;
	private int ticketid;
	private int vold;
	private int v1;
	private int v2;
	private int v3;
	private int sendnum;
	private int option;
	private String orderid;
	
	private IProxyManager proxyManager;
	private IGoodsAgentManager goodsAgentManager;
	private IGoodsDiscountTicketDetailManager goodsDiscountTicketDetailManager;
	private IMemberProxyVipManager memberProxyVipManager;
	private INewOrderManager newOrderManager;
	private INewOrderItemsManager newOrderItemsManager;
	
	public String createTicket(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			this.json=JsonMessageUtil.getStringJson("result", "2");//未登录
			return "json_message";
		}
		Proxy proxy = this.proxyManager.get(proxyid);
		if(proxy==null){
			this.json=JsonMessageUtil.getStringJson("result", "0");//代理商品不存在
			return "json_message";
		}
		if(proxy.getMemberId()!=member.getMember_id()){
			this.json=JsonMessageUtil.getStringJson("result", "0");//代理商品用户不正确
			return "json_message";
		}
		int c = this.goodsDiscountTicketDetailManager.getTicketNum(proxyid);
		if(proxy.getGoodsAmount()-c<num){
			this.json=JsonMessageUtil.getStringJson("result", "3");//生成现金券数量超过代理商品剩余数量
			return "json_message";
		}
		GoodsAgent goodsAgent = this.goodsAgentManager.get(proxy.getGoodsId());
		String ticketOption = goodsAgent.getTicketOption();
		if(ticketOption==null){
			this.json=JsonMessageUtil.getStringJson("result", "4");//此商品无法生成商品券
			return "json_message";
		}
		String[] tOption = ticketOption.split("/");
		int tlength =tOption.length;
		int s = 0;
		for(int i=0;i<tlength;i++){
			float t = Float.valueOf(tOption[i]);
			if(t==ticketvalue){
				s=i+1;
			}
		}
		if(s==0){
			this.json=JsonMessageUtil.getStringJson("result", "5");//现金券价格不正确
			return "json_message";
		}
		for(int j=0;j<num;j++){
			GoodsDiscountTicketDetail goodsDiscountTicketDetail = new GoodsDiscountTicketDetail();
			goodsDiscountTicketDetail.setGoodsId(proxy.getGoodsId());
			goodsDiscountTicketDetail.setProxyId(proxy.getId());
			goodsDiscountTicketDetail.setProxyMemberId(proxy.getMemberId());
			goodsDiscountTicketDetail.setTicketValue(ticketvalue);
			Long f = (long)(Math.random()*10000000);
			String ticketid = "HJYXYS"+s+f;
			goodsDiscountTicketDetail.setDiscountTicketId(ticketid);
			goodsDiscountTicketDetail.setGiveStatus(0);
			goodsDiscountTicketDetail.setSendStatus(0);
			goodsDiscountTicketDetail.setUseStatus(0);
			goodsDiscountTicketDetail.setBelongMemberId(0);
			this.goodsDiscountTicketDetailManager.add(goodsDiscountTicketDetail);			
		}
		proxy.setTicketFrozenCredit(proxy.getTicketFrozenCredit()+num*ticketvalue);
		this.proxyManager.edit(proxy);
		this.json=JsonMessageUtil.getStringJson("result", "1");
		return "json_message";
	}
	
	public String oneSend(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			this.json=JsonMessageUtil.getStringJson("result", "2");//未登录
			return "json_message";
		}
		GoodsDiscountTicketDetail goodsDiscountTicketDetail = this.goodsDiscountTicketDetailManager.get(ticketid);
		if(goodsDiscountTicketDetail==null){
			this.json=JsonMessageUtil.getStringJson("result", "0");//代理商品不存在
			return "json_message";
		}
		if(goodsDiscountTicketDetail.getProxyMemberId()!=member.getMember_id()){
			this.json=JsonMessageUtil.getStringJson("result", "0");//代理商品用户不正确
			return "json_message";
		}
		if(goodsDiscountTicketDetail.getGiveStatus()!=0){
			this.json=JsonMessageUtil.getStringJson("result", "3");//代理商品已赠送
			return "json_message";
		}
		goodsDiscountTicketDetail.setBelongMemberId(memberid);
		goodsDiscountTicketDetail.setGiveStatus(1);
		this.goodsDiscountTicketDetailManager.update(goodsDiscountTicketDetail);
		this.json=JsonMessageUtil.getStringJson("result", "1");
		return "json_message";
	}
	
	public String allSend(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			this.json=JsonMessageUtil.getStringJson("result", "2");//未登录
			return "json_message";
		}
		Proxy proxy = this.proxyManager.get(proxyid);
		if(proxy==null){
			this.json=JsonMessageUtil.getStringJson("result", "0");//代理商品不存在
			return "json_message";
		}
		if(proxy.getMemberId()!=member.getMember_id()){
			this.json=JsonMessageUtil.getStringJson("result", "0");//代理商品用户不正确
			return "json_message";
		}
		GoodsAgent goodsAgent = this.goodsAgentManager.get(proxy.getGoodsId());
		String ticketOption = goodsAgent.getTicketOption();
		String[] tOption = ticketOption.split("/");
		List<GoodsDiscountTicketDetail> gList = new ArrayList<GoodsDiscountTicketDetail>();
		if(option==1){
			gList = this.goodsDiscountTicketDetailManager.getByProxyIdAndTicketValue(proxyid,Float.valueOf(tOption[0]));
		}else if(option==2){
			gList = this.goodsDiscountTicketDetailManager.getByProxyIdAndTicketValue(proxyid,Float.valueOf(tOption[1]));
		}else if(option==3){
			gList = this.goodsDiscountTicketDetailManager.getByProxyIdAndTicketValue(proxyid,Float.valueOf(tOption[2]));
		}else{
			gList = this.goodsDiscountTicketDetailManager.getNotGiveByProxyId(proxy.getId());
		}
		if(gList.size()==0){
			this.json=JsonMessageUtil.getStringJson("result", "3");//没有可以赠送的现金券
			return "json_message";
		}
		int ticketnum = gList.size();
		List<MemberProxyVip> mList = new ArrayList<MemberProxyVip>();
		List<MemberProxyVip> m1List = new ArrayList<MemberProxyVip>();
		List<MemberProxyVip> mv1list = new ArrayList<MemberProxyVip>();
		List<MemberProxyVip> mv2list = new ArrayList<MemberProxyVip>();
		List<MemberProxyVip> mv3list = new ArrayList<MemberProxyVip>();
		List<MemberProxyVip> mvoldlist = new ArrayList<MemberProxyVip>();
		if(v1==1){
			mv1list =  this.memberProxyVipManager.getV1ByProxyMemberId(proxy.getMemberId());
		}
		if(v2==1){
			mv2list =  this.memberProxyVipManager.getV2ByProxyMemberId(proxy.getMemberId());
		}
		if(v3==1){
			mv3list =  this.memberProxyVipManager.getV3ByProxyMemberId(proxy.getMemberId());
		}
		if(vold==1){
			mvoldlist =  this.memberProxyVipManager.getVOldByProxyMemberId(proxy.getMemberId());
		}
		mList.addAll(mv1list);
		mList.addAll(mv2list);
		mList.addAll(mv3list);
		mList.addAll(mvoldlist);
		int membernum = mList.size();
		if(membernum==0){
			this.json=JsonMessageUtil.getStringJson("result", "5");
			return "json_message";
		}
		for(int i=0;i<sendnum;i++){
			if(m1List.size()==0){
				m1List.addAll(mList);
			}
			for(int j=0;j<membernum;j++){
				if(gList.size()==0){
					this.json=JsonMessageUtil.getStringJson("result", "4");
					return "json_message";
				}
				Random random = new Random();
				int r = random.nextInt(m1List.size());
				GoodsDiscountTicketDetail gDetail = gList.get(0);
				MemberProxyVip m = m1List.get(r);
				gDetail.setBelongMemberId(m.getMemberId());
				gDetail.setGiveStatus(1);
				this.goodsDiscountTicketDetailManager.update(gDetail);
				m1List.remove(r);
				gList.remove(0);
			}			
		}
		this.json=JsonMessageUtil.getStringJson("result", "1");
		return "json_message";
	}
	
	public String send(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			this.json=JsonMessageUtil.getStringJson("result", "2");//未登录
			return "json_message";
		}
		GoodsDiscountTicketDetail goodsDiscountTicketDetail = this.goodsDiscountTicketDetailManager.get(ticketid);
		if(goodsDiscountTicketDetail==null){
			this.json=JsonMessageUtil.getStringJson("result", "0");//代理商品不存在
			return "json_message";
		}
		if(goodsDiscountTicketDetail.getBelongMemberId()!=member.getMember_id()){
			this.json=JsonMessageUtil.getStringJson("result", "3");//现金券不属于
			return "json_message";
		}
		if(goodsDiscountTicketDetail.getUseStatus()!=0){
			this.json=JsonMessageUtil.getStringJson("result", "4");//现金券已使用
			return "json_message";
		}
		if(goodsDiscountTicketDetail.getSendStatus()!=0){
			this.json=JsonMessageUtil.getStringJson("result", "5");//现金券已赠送或赠送中
			return "json_message";
		}
		goodsDiscountTicketDetail.setSendStatus(1);
		String key = getRandomString(8);
		goodsDiscountTicketDetail.setSendKey(key);
		this.goodsDiscountTicketDetailManager.update(goodsDiscountTicketDetail);		
		this.json=JsonMessageUtil.getStringJson("result", "1");
		return "json_message";
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
	
	public String useTicket(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			this.json=JsonMessageUtil.getStringJson("result", "2");//未登录
			return "json_message";
		}
		if(orderid==null||orderid==""){
			this.json=JsonMessageUtil.getStringJson("result", "3");//参数错误
			return "json_message";
		}
		int oid = Integer.valueOf(orderid);
		Order order = this.newOrderManager.getById(oid);
		if(order==null){
			this.json=JsonMessageUtil.getStringJson("result", "3");//参数错误
			return "json_message";
		}
		int m1 = order.getMember_id();
		int m2 = member.getMember_id();
		if(m1!=m2){
			this.json=JsonMessageUtil.getStringJson("result", "3");//参数错误
			return "json_message";
		}
		List<OrderItem> oList = this.newOrderItemsManager.getByOrderId(oid);
		int count = oList.size();
		for(int i=0;i<count;i++){
			OrderItem orderItem = oList.get(i);
			int pid = Integer.valueOf(orderItem.getAddon());
			GoodsDiscountTicketDetail goodsDiscountTicketDetail = this.goodsDiscountTicketDetailManager.getHignValueByProxyid(pid, member.getMember_id());
			float ticketvalue = 0;
			if(goodsDiscountTicketDetail!=null){
				ticketvalue = goodsDiscountTicketDetail.getTicketValue();
				goodsDiscountTicketDetail.setUseStatus(1);
				goodsDiscountTicketDetail.setUseOrderItemId(orderItem.getItem_id());
				this.goodsDiscountTicketDetailManager.update(goodsDiscountTicketDetail);
			}
			order.setNeed_pay_money(order.getNeed_pay_money()-ticketvalue);			
		}
		this.newOrderManager.update(order);
		this.json=JsonMessageUtil.getStringJson("result", "1");
		return "json_message";
	}
	
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getProxyid() {
		return proxyid;
	}

	public void setProxyid(int proxyid) {
		this.proxyid = proxyid;
	}

	public float getTicketvalue() {
		return ticketvalue;
	}

	public void setTicketvalue(float ticketvalue) {
		this.ticketvalue = ticketvalue;
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

	public IGoodsDiscountTicketDetailManager getGoodsDiscountTicketDetailManager() {
		return goodsDiscountTicketDetailManager;
	}

	public void setGoodsDiscountTicketDetailManager(
			IGoodsDiscountTicketDetailManager goodsDiscountTicketDetailManager) {
		this.goodsDiscountTicketDetailManager = goodsDiscountTicketDetailManager;
	}

	public int getMemberid() {
		return memberid;
	}

	public void setMemberid(int memberid) {
		this.memberid = memberid;
	}

	public int getTicketid() {
		return ticketid;
	}

	public void setTicketid(int ticketid) {
		this.ticketid = ticketid;
	}

	public int getVold() {
		return vold;
	}

	public void setVold(int vold) {
		this.vold = vold;
	}

	public int getV1() {
		return v1;
	}

	public void setV1(int v1) {
		this.v1 = v1;
	}

	public int getV2() {
		return v2;
	}

	public void setV2(int v2) {
		this.v2 = v2;
	}

	public int getV3() {
		return v3;
	}

	public void setV3(int v3) {
		this.v3 = v3;
	}

	public int getSendnum() {
		return sendnum;
	}

	public void setSendnum(int sendnum) {
		this.sendnum = sendnum;
	}

	public IMemberProxyVipManager getMemberProxyVipManager() {
		return memberProxyVipManager;
	}

	public void setMemberProxyVipManager(
			IMemberProxyVipManager memberProxyVipManager) {
		this.memberProxyVipManager = memberProxyVipManager;
	}

	public int getOption() {
		return option;
	}

	public void setOption(int option) {
		this.option = option;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
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
}
