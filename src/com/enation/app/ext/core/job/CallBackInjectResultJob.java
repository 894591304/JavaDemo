package com.enation.app.ext.core.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.goodsdiscountticketdetail.model.GoodsDiscountTicketDetail;
import com.enation.app.ext.component.goodsdiscountticketdetail.service.IGoodsDiscountTicketDetailManager;
import com.enation.app.ext.component.memberproxyvip.model.MemberProxyVip;
import com.enation.app.ext.component.memberproxyvip.service.IMemberProxyVipManager;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderItemsManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderManager;
import com.enation.app.ext.component.useraccount.model.UserAccount;
import com.enation.app.ext.component.useraccount.service.IUserAccountManager;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.app.shop.core.service.IMemberManager;

/*
 * 此定时任务每小时自动执行一次
 */

public class CallBackInjectResultJob {

	private IMemberProxyVipManager memberProxyVipManager;
	private IProxyManager proxyManager;
	private IGoodsProxyManager goodsProxyManager;
	private IUserAccountManager userAccountManager;
	private IMemberManager memberManager;
	private IGoodsDiscountTicketDetailManager goodsDiscountTicketDetailManager;
	private INewOrderItemsManager newOrderItemsManager;
	private INewOrderManager newOrderManager;
		
    public void execute() {
    	checkVIPTime();			//vip时间检测任务
    	checkProxyStatus();		//检测代理商品是否过期
    	checkUseraccount();     //检测用户账户并结算
    	Date date = new Date();
    	SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日hh时mm分ss秒");
        System.out.println(df.format(date)+"执行了VIP及proxy相关任务！");
    }

    public void checkUseraccount(){
    	List<UserAccount> ulist = this.userAccountManager.getAll();
    	int uc = ulist.size();
    	for(int u=0;u<uc;u++){
    		UserAccount userAccount = ulist.get(u);
    		Member member = this.memberManager.get(userAccount.getMemberId());
		List<Proxy> pList = this.proxyManager.getAllByMemberid(member.getMember_id());
		int total = pList.size();
		for(int i=0;i<total;i++){
			userAccount = this.userAccountManager.getByMemberId(member.getMember_id());
			Proxy proxy = pList.get(i);
			if(proxy.getStatus()==5){
				float ticketvalue = 0;
				if(proxy.getTicketFrozenCredit()!=0){
					List<GoodsDiscountTicketDetail> gList = this.goodsDiscountTicketDetailManager.getSaleProxy(proxy.getId());
					int count = gList.size();
					for(int c=0;c<count;c++){
						GoodsDiscountTicketDetail goodsDiscountTicketDetail = gList.get(c);
						ticketvalue = goodsDiscountTicketDetail.getTicketValue();
					}
				}
				if(proxy.getProxyTestTime()!=proxy.getProxyEndTime()){
					proxy.setStatus(0);
					if(ticketvalue>userAccount.getWaitCash()+proxy.getFrozenEarn()){
						userAccount.setRemainCredit(userAccount.getRemainCredit()+proxy.getFrozenCredit());
						userAccount.setWaitCash(0);
						userAccount.setRepay(userAccount.getRepay()+ticketvalue-userAccount.getWaitCash()-proxy.getFrozenEarn());
						Long nTime = Long.valueOf(String.valueOf(System.currentTimeMillis()))/1000;
						Long eTime = nTime+60*60*24*60;
						if(userAccount.getRepayTime()!=null){
							if(Long.valueOf(userAccount.getRepayTime())>=eTime){
								userAccount.setRepayTime(String.valueOf(eTime));
							}
						}else{
							userAccount.setRepayTime(String.valueOf(eTime));
						}
					}else{
						userAccount.setRemainCredit(userAccount.getRemainCredit()+proxy.getFrozenCredit());
						userAccount.setWaitCash(userAccount.getWaitCash()+proxy.getFrozenEarn()-ticketvalue);
					}
					this.userAccountManager.update(userAccount);
					proxy.setFrozenDeposit(0);
					proxy.setFrozenCredit(0);
					proxy.setTicketFrozenCredit(0);
					this.proxyManager.edit(proxy);
				}else{
					proxy.setStatus(0);
					Long sale = Long.valueOf(proxy.getSale());
					Long goodsAmount =Long.valueOf(proxy.getGoodsAmount());
					if(sale>=goodsAmount*0.5){
						if(ticketvalue>userAccount.getWaitCash()+proxy.getFrozenEarn()){
							userAccount.setRemainCredit(userAccount.getRemainCredit()+proxy.getFrozenCredit());
							userAccount.setWaitCash(0);
							userAccount.setRepay(userAccount.getRepay()+ticketvalue-userAccount.getWaitCash()-proxy.getFrozenEarn());
							Long nTime = Long.valueOf(String.valueOf(System.currentTimeMillis()))/1000;
							Long eTime = nTime+60*60*24*60;
							if(userAccount.getRepayTime()!=null){
								if(Long.valueOf(userAccount.getRepayTime())>=eTime){
									userAccount.setRepayTime(String.valueOf(eTime));
								}
							}else{
								userAccount.setRepayTime(String.valueOf(eTime));
							}
						}else{
							userAccount.setRemainCredit(userAccount.getRemainCredit()+proxy.getFrozenCredit());
							userAccount.setWaitCash(userAccount.getWaitCash()+proxy.getFrozenEarn()-ticketvalue);
						}
						this.userAccountManager.update(userAccount);
						proxy.setFrozenDeposit(0);
						proxy.setFrozenCredit(0);
						proxy.setTicketFrozenCredit(0);
						this.proxyManager.edit(proxy);
					}else if(goodsAmount*0.3<=sale&&sale<goodsAmount*0.5){
						if(ticketvalue+proxy.getFrozenCredit()>userAccount.getWaitCash()+proxy.getFrozenEarn()){
							userAccount.setRemainCredit(userAccount.getRemainCredit()+proxy.getFrozenCredit());
							userAccount.setWaitCash(0);
							userAccount.setRepay(userAccount.getRepay()+ticketvalue+proxy.getFrozenCredit()-userAccount.getWaitCash()-proxy.getFrozenEarn());
							Long nTime = Long.valueOf(String.valueOf(System.currentTimeMillis()))/1000;
							Long eTime = nTime+60*60*24*60;
							if(userAccount.getRepayTime()!=null){
								if(Long.valueOf(userAccount.getRepayTime())>=eTime){
									userAccount.setRepayTime(String.valueOf(eTime));
								}
							}else{
								userAccount.setRepayTime(String.valueOf(eTime));
							}
						}else{
							userAccount.setRemainCredit(userAccount.getRemainCredit()+proxy.getFrozenCredit());
							userAccount.setWaitCash(userAccount.getWaitCash()+proxy.getFrozenEarn()-ticketvalue-proxy.getFrozenCredit());
						}
						this.userAccountManager.update(userAccount);
						proxy.setFrozenDeposit(0);
						proxy.setFrozenCredit(0);
						proxy.setTicketFrozenCredit(0);
						this.proxyManager.edit(proxy);						
					}else if(sale<goodsAmount*0.3&&sale>=goodsAmount*0.1){
						if(ticketvalue+proxy.getFrozenCredit()>userAccount.getWaitCash()+proxy.getFrozenEarn()){
							userAccount.setRemainCredit(userAccount.getRemainCredit()+proxy.getFrozenCredit());
							userAccount.setWaitCash(0);
							userAccount.setRepay(userAccount.getRepay()+ticketvalue+proxy.getFrozenCredit()-userAccount.getWaitCash()-proxy.getFrozenEarn());
							Long nTime = Long.valueOf(String.valueOf(System.currentTimeMillis()))/1000;
							Long eTime = nTime+60*60*24*60;
							if(userAccount.getRepayTime()!=null){
								if(Long.valueOf(userAccount.getRepayTime())>=eTime){
									userAccount.setRepayTime(String.valueOf(eTime));
								}
							}else{
								userAccount.setRepayTime(String.valueOf(eTime));
							}
						}else{
							userAccount.setRemainCredit(userAccount.getRemainCredit()+proxy.getFrozenCredit());
							userAccount.setWaitCash(userAccount.getWaitCash()+proxy.getFrozenEarn()-ticketvalue-proxy.getFrozenCredit());
						}
						this.userAccountManager.update(userAccount);
						proxy.setFrozenDeposit(0);
						proxy.setFrozenCredit(0);
						proxy.setTicketFrozenCredit(0);
						this.proxyManager.edit(proxy);
					}else{
						if(ticketvalue+proxy.getFrozenCredit()>userAccount.getWaitCash()+proxy.getFrozenEarn()){
							userAccount.setRemainCredit(userAccount.getRemainCredit()+proxy.getFrozenCredit());
							userAccount.setWaitCash(0);
							userAccount.setRepay(userAccount.getRepay()+ticketvalue+proxy.getFrozenCredit()-userAccount.getWaitCash()-proxy.getFrozenEarn());
							Long nTime = Long.valueOf(String.valueOf(System.currentTimeMillis()))/1000;
							Long eTime = nTime+60*60*24*30;
							if(userAccount.getRepayTime()!=null){
								if(Long.valueOf(userAccount.getRepayTime())>=eTime){
									userAccount.setRepayTime(String.valueOf(eTime));
								}
							}else{
								userAccount.setRepayTime(String.valueOf(eTime));
							}
						}else{
							userAccount.setRemainCredit(userAccount.getRemainCredit()+proxy.getFrozenCredit());
							userAccount.setWaitCash(userAccount.getWaitCash()+proxy.getFrozenEarn()-ticketvalue-proxy.getFrozenCredit());
						}
						this.userAccountManager.update(userAccount);
						proxy.setFrozenDeposit(0);
						proxy.setFrozenCredit(0);
						proxy.setTicketFrozenCredit(0);
						this.proxyManager.edit(proxy);
					}
				}
				
			}			
		}
		userAccount = this.userAccountManager.getByMemberId(member.getMember_id());
		float credit = userAccount.getCredit();
		float remainCredit = userAccount.getRemainCredit();
		float waitCash = userAccount.getWaitCash();
		float repay = userAccount.getRepay();
		float newRemainCredit=0;
		float newWaitCash=0;
		float newRepay=0;
		if(waitCash>=credit-remainCredit&&userAccount.getRepayTime()!=null&&userAccount.getRepayTime()!=""){
			newRemainCredit = credit;
			newWaitCash = waitCash-(credit-remainCredit);
			userAccount.setRemainCredit(newRemainCredit);
			userAccount.setWaitCash(newWaitCash);
			userAccount.setRepay(newRepay);
			if(newRepay==0){
				userAccount.setRepayTime(null);
			}
			this.userAccountManager.update(userAccount);
		}else if(userAccount.getRepayTime()!=null&&userAccount.getRepayTime()!=""){
			newWaitCash = 0;
			newRemainCredit = remainCredit+waitCash;
			newRepay=credit-newRemainCredit;
			userAccount.setRemainCredit(newRemainCredit);
			userAccount.setWaitCash(newWaitCash);
			userAccount.setRepay(newRepay);
			if(newRepay==0){
				userAccount.setRepayTime(null);
			}
			this.userAccountManager.update(userAccount);
		}else{
			
		}   
    	}
    }
    
    public void checkProxyStatus(){
    	List<Proxy> pList = this.proxyManager.getAll();
		int total = pList.size();
		for(int i=0;i<total;i++){
			Proxy proxy = pList.get(i);
			String testTime = proxy.getProxyTestTime();
			Long tTime = Long.valueOf(testTime);
			Long nTime = System.currentTimeMillis()/1000;
			Goods goods = this.goodsProxyManager.getGoods(proxy.getGoodsId());
			if(goods.getDisabled()==1&&proxy.getStatus()!=0&&proxy.getStatus()!=5){
				List<GoodsDiscountTicketDetail> gList = this.goodsDiscountTicketDetailManager.getSaleProxy(proxy.getId());
				if(gList.size()!=0){
					for(int g=0;g<gList.size();g++){
						GoodsDiscountTicketDetail goodsDiscountTicketDetail = gList.get(g);
						OrderItem orderItem = this.newOrderItemsManager.get(goodsDiscountTicketDetail.getUseOrderItemId());
						Order order = this.newOrderManager.getById(orderItem.getOrder_id());
						if(order.getPay_status()==0){
							order.setStatus(8);
							this.newOrderManager.update(order);
							goodsDiscountTicketDetail.setUseStatus(2);
							goodsDiscountTicketDetail.setUseOrderItemId(0);
							this.goodsDiscountTicketDetailManager.update(goodsDiscountTicketDetail);
						}
					}
				}
				proxy.setStatus(5);
				proxy.setProxyTestTime(String.valueOf(Integer.valueOf(String.valueOf(System.currentTimeMillis()/1000))));
				this.proxyManager.edit(proxy);
			}			
			if(nTime>tTime&&proxy.getStatus()!=0&&proxy.getStatus()!=5){
				List<GoodsDiscountTicketDetail> gList = this.goodsDiscountTicketDetailManager.getSaleProxy(proxy.getId());
				if(gList.size()!=0){
					for(int g=0;g<gList.size();g++){
						GoodsDiscountTicketDetail goodsDiscountTicketDetail = gList.get(g);
						if(goodsDiscountTicketDetail.getUseOrderItemId()==0){
							goodsDiscountTicketDetail.setUseStatus(2);
							goodsDiscountTicketDetail.setUseOrderItemId(0);
							this.goodsDiscountTicketDetailManager.update(goodsDiscountTicketDetail);
						}else{
							OrderItem orderItem = this.newOrderItemsManager.get(goodsDiscountTicketDetail.getUseOrderItemId());
							Order order = this.newOrderManager.getById(orderItem.getOrder_id());
							if(order.getPay_status()==0){
								order.setStatus(8);
								this.newOrderManager.update(order);
								goodsDiscountTicketDetail.setUseStatus(2);
								goodsDiscountTicketDetail.setUseOrderItemId(0);
								this.goodsDiscountTicketDetailManager.update(goodsDiscountTicketDetail);
							}
						}
					}
				}
				proxy.setStatus(5);
				this.proxyManager.edit(proxy);
			}
		}	
    }
    public void checkVIPTime(){
    	List<MemberProxyVip> mList = this.memberProxyVipManager.getAllNotExpired();
    	int count = mList.size();
    	for(int i=0;i<count;i++){
    		MemberProxyVip m = mList.get(i);
    		long v3LT = Long.valueOf(m.getV3LT());
    		long v2LT = Long.valueOf(m.getV2LT());
    		long v1LT = Long.valueOf(m.getV1LT());
    		long bt = Long.valueOf(m.getVipBT());
    		long time = System.currentTimeMillis()/1000;
    		if(v3LT!=0){
    			if(v3LT>(time-bt)){
    				v3LT = v3LT-(time-bt);
    			}else{
    				v3LT = 0;
    			}    			
    		}
    		if(v2LT!=0){
    			if(v2LT>(time-bt)){
    				v2LT = v2LT-(time-bt);
    			}else{
    				v2LT = 0;
    			}    			
    		}
    		if(v1LT!=0){
    			if(v1LT>(time-bt)){
    				v1LT = v1LT-(time-bt);
    			}else{
    				v1LT = 0;
    			}    			
    		}
    		m.setV1LT(String.valueOf(v1LT));
    		m.setV2LT(String.valueOf(v2LT));
    		m.setV3LT(String.valueOf(v3LT));
    		m.setVipBT(String.valueOf(time));
    		this.memberProxyVipManager.update(m);
    	}    	
    }
    
    public void ticketOutOfTime(Proxy proxy){
    	List<GoodsDiscountTicketDetail> gList = this.goodsDiscountTicketDetailManager.getByProxyId(proxy.getId());
    	int count = gList.size();
    	for(int i=0;i<count;i++){
    		GoodsDiscountTicketDetail goodsDiscountTicketDetail = gList.get(i);
    		goodsDiscountTicketDetail.setUseStatus(3);
    		this.goodsDiscountTicketDetailManager.update(goodsDiscountTicketDetail);
    	}
    }
    
	public IMemberProxyVipManager getMemberProxyVipManager() {
		return memberProxyVipManager;
	}

	public void setMemberProxyVipManager(
			IMemberProxyVipManager memberProxyVipManager) {
		this.memberProxyVipManager = memberProxyVipManager;
	}

	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

	public IGoodsProxyManager getGoodsProxyManager() {
		return goodsProxyManager;
	}

	public void setGoodsProxyManager(IGoodsProxyManager goodsProxyManager) {
		this.goodsProxyManager = goodsProxyManager;
	}

	public IUserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(IUserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	public IGoodsDiscountTicketDetailManager getGoodsDiscountTicketDetailManager() {
		return goodsDiscountTicketDetailManager;
	}

	public void setGoodsDiscountTicketDetailManager(
			IGoodsDiscountTicketDetailManager goodsDiscountTicketDetailManager) {
		this.goodsDiscountTicketDetailManager = goodsDiscountTicketDetailManager;
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
    
}
