package com.enation.app.ext.component.goodsagent.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.credit.model.Credit;
import com.enation.app.ext.component.credit.service.ICreditManager;
import com.enation.app.ext.component.follow.service.IFollowManager;
import com.enation.app.ext.component.goodsagent.model.GoodsAgent;
import com.enation.app.ext.component.goodsagent.service.IGoodsAgentManager;
import com.enation.app.ext.component.membershop.model.MemberShop;
import com.enation.app.ext.component.membershop.service.IMemberShopManager;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.component.proxyMemberBankInfo.model.ProxyMemberBankInfo;
import com.enation.app.ext.component.proxyMemberBankInfo.service.IProxyMemberBankInfoManager;
import com.enation.app.ext.component.proxycount.service.IProxyCountManager;
import com.enation.app.ext.component.useraccount.model.UserAccount;
import com.enation.app.ext.component.useraccount.service.IUserAccountManager;
import com.enation.app.ext.component.vipleveldetail.model.VipLevelDetail;
import com.enation.app.ext.component.vipleveldetail.service.IVipLevelDetailManager;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonMessageUtil;

import net.sf.json.JSONObject;

public class GoodsAgentAction extends WWAction {

	private static final long serialVersionUID = 8806752238256966348L;
	private Integer goods_id;
	private IGoodsAgentManager goodsAgentManager;
	private IProxyManager proxyManager;
	private IUserAccountManager userAccountManager;
	private IMemberShopManager memberShopManager;
	private IGoodsProxyManager goodsProxyManager;
	private IFollowManager followManager;
	private IProxyCountManager proxyCountManager;
	private IProxyMemberBankInfoManager proxyMemberBankInfoManager;
	private IVipLevelDetailManager vipLevelDetailManager;
	private ICreditManager creditManager;
	
	
	private int agentnum;
	private int nobzj;
	private int xsqx;
	private int goodsid;
	
	private int onsalenum;
	private String goodsLabel;
	private String status;
	private String goodsCategory;
	
	private int proxyid;

	/**
	 * 根据goods_id获取GoodsAgent
	 * 
	 * @return
	 */
	public String getGoodsAgent() {
		GoodsAgent goodsAgent = this.goodsAgentManager.get(this.goods_id);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", 1);
		if(goodsAgent!=null){
			JSONObject goodsAgentObj = new JSONObject();
			goodsAgentObj.put("price", goodsAgent.getPrice());
			goodsAgentObj.put("mktPrice", goodsAgent.getMktPrice());
			goodsAgentObj.put("goldPrice", goodsAgent.getGoldPrice());
			goodsAgentObj.put("platinumPrice", goodsAgent.getPlatinumPrice());
			goodsAgentObj.put("blackPrice", goodsAgent.getBlackPrice());
			goodsAgentObj.put("cost", goodsAgent.getCost());
			goodsAgentObj.put("defaultPrice", goodsAgent.getDefaultPrice());
			goodsAgentObj.put("timeOption", goodsAgent.getTimeOption());
			goodsAgentObj.put("ticketOption", goodsAgent.getTicketOption());
			goodsAgentObj.put("ticketCaption", goodsAgent.getTicketCaption());
			jsonObj.put("priceData",goodsAgentObj);
		}else{
			jsonObj.put("result", 0);
		}
		this.json = jsonObj.toString();
		return "json_message";
	}
	
	public String signAgent(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			showErrorJson("未登录不能申请代理！");
			return "json_message";
		}
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String registerip = request.getRemoteAddr();
		if(this.proxyManager.checkProxy(this.goodsid, member.getMember_id())==1){
			showErrorJson("您代理的此商品还未销售完，请销售完后再申请代理！");
			return "json_message";
		}
		Proxy proxy = new Proxy();
		UserAccount userAccount = new UserAccount();
		MemberShop memberShop = new MemberShop();
		userAccount = this.userAccountManager.getByMemberId(member.getMember_id());
		memberShop = this.memberShopManager.get(member.getMember_id());
		proxy.setMemberId(member.getMember_id());
		proxy.setGoodsId(this.goodsid);
		int store = this.goodsProxyManager.getGoods(this.goodsid).getStore();
		if(this.agentnum>store){
			showErrorJson("库存数量不足！");
			return "json_message";
		}
		Credit credit = this.creditManager.get(member.getMember_id());
		boolean b = this.proxyManager.checkCanProxy(member.getMember_id(),credit.getBrokerageId(),goodsid);
		if(b==false){
			this.json=JsonMessageUtil.getStringJson("result", "4");
			return "json_message";
		}
		Goods goods = this.goodsProxyManager.getGoods(this.goodsid);
		if(goods.getGoods_id()==99999999||goods.getGoods_id()==99999998||goods.getGoods_id()==99999997){
			List<ProxyMemberBankInfo> plist = this.proxyMemberBankInfoManager.getByMemberId(member.getMember_id());
			int count = plist.size();
			if(count==0){
				this.json=JsonMessageUtil.getStringJson("result", "3");
				return "json_message";
			}
		}
		goods.setStore(goods.getStore()-this.agentnum);
		proxy.setGoodsAmount(this.agentnum);
		proxy.setOnSale(0);
		proxy.setSale(0);
		float Rcredit = this.userAccountManager.getRemainCredit(member.getMember_id());
		if(/*this.nobzj==10*/0==1){					//无保证金代理隐藏，暂时无法触发
			float Fcredit = this.goodsAgentManager.get(this.goodsid).getDefaultPrice()*(this.agentnum)/10;
			float Fdeposit = 0;
			if(Fcredit<=Rcredit){
				proxy.setFrozenCredit(Fcredit);
				proxy.setFrozenDeposit(Fdeposit);
				userAccount.setRemainCredit(Rcredit-Fcredit);
			}else{
				showErrorJson("授信余额不足！");
				return "json_message";
			}
		}else{
			int level = this.userAccountManager.getLevel(member.getMember_id());
			if(level==1){
				float Fcredit = this.goodsAgentManager.get(this.goodsid).getGoldPrice()*(this.agentnum)/10;
				float Fdeposit = Fcredit;
				if(Fcredit<=Rcredit){
					proxy.setFrozenCredit(Fcredit);
					proxy.setFrozenDeposit(Fdeposit);
					userAccount.setRemainCredit(Rcredit-Fcredit);
				}else{
					showErrorJson("授信余额不足！");
					return "json_message";
				}	
			}else if(level==2){
				float Fcredit = this.goodsAgentManager.get(this.goodsid).getPlatinumPrice()*(this.agentnum)/10;
				float Fdeposit = Fcredit;
				if(Fcredit<=Rcredit){
					proxy.setFrozenCredit(Fcredit);
					proxy.setFrozenDeposit(Fdeposit);
					userAccount.setRemainCredit(Rcredit-Fcredit);
				}else{
					showErrorJson("授信余额不足！");
					return "json_message";
				}	
			}else if(level == 3){
				float Fcredit = this.goodsAgentManager.get(this.goodsid).getBlackPrice()*(this.agentnum)/10;
				float Fdeposit = Fcredit;
				if(Fcredit<=Rcredit){
					proxy.setFrozenCredit(Fcredit);
					proxy.setFrozenDeposit(Fdeposit);
					userAccount.setRemainCredit(Rcredit-Fcredit);
				}else{
					showErrorJson("授信余额不足！");
					return "json_message";
				}				
			}else{
				showErrorJson("未申请授信或授信未通过，不能申请代理！");
				return "json_message";
			}
		}
		long nt = System.currentTimeMillis();
		String t = String.valueOf(nt/1000);
		String testT = String.valueOf(nt/1000+60*60*24*7);
		String entT = String.valueOf(nt/1000+60*60*24*(this.xsqx));
		proxy.setFrozenEarn(0);
		proxy.setProxyBeginTime(t);
		proxy.setProxyTestTime(testT);
		proxy.setProxyEndTime(entT);
		proxy.setGoodsCategory("");
		proxy.setGoodsLabel("");
		proxy.setStatus(2);
		memberShop.setProxyNum(memberShop.getProxyNum()+1);
		this.proxyCountManager.addProxyTime(goodsid);
		this.proxyCountManager.addProxyAll(goodsid, agentnum);
		this.goodsProxyManager.update(goods);
		this.proxyManager.add(proxy);
		this.userAccountManager.update(userAccount);
		this.memberShopManager.edit(memberShop);
		this.json=JsonMessageUtil.getStringJson("result", "1");
		return "json_message";
	}

	public String onSale(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			showErrorJson("未登录不能申请代理！");
			return "json_message";
		}
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String registerip = request.getRemoteAddr();
		if(this.proxyManager.checkProxy(this.goodsid, member.getMember_id())!=1){
			showErrorJson("您没有代理此商品或代理已过期或已经售光！");
			return "json_message";
		}
		Proxy proxy = this.proxyManager.getForOnSale(this.goodsid, member.getMember_id());
		if(this.onsalenum>(proxy.getGoodsAmount()-proxy.getOnSale())){
			showErrorJson("您输入的代理数量有误！");
			return "json_message";
		}
		proxy.setOnSale(proxy.getOnSale()+this.onsalenum);
		proxy.setGoodsCategory(this.goodsCategory);
		proxy.setGoodsLabel(this.goodsLabel);
		proxy.setStatus(Integer.valueOf(this.status));
		this.proxyManager.edit(proxy);
		this.followManager.msgUp(member.getMember_id());
		VipLevelDetail vipLevelDetail = this.vipLevelDetailManager.getByMemberId(member.getMember_id());
		if(proxy.getGoodsId()==99999999||proxy.getGoodsId()==99999998||proxy.getGoodsId()==99999997){
			if(vipLevelDetail==null){
				this.json=JsonMessageUtil.getStringJson("result", "2");
				return "json_message";
			}
		}
		this.json=JsonMessageUtil.getStringJson("result", "1");
		return "json_message";
	}
	
	public String continueAgent(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			this.json=JsonMessageUtil.getStringJson("result", "2");
			return "json_message";
		}
		Proxy proxy = this.proxyManager.get(proxyid);
		if(proxy==null)
		{
			this.json=JsonMessageUtil.getStringJson("result", "0");
			return "json_message";
		}
		if(proxy.getProxyEndTime()==proxy.getProxyTestTime()){
			this.json=JsonMessageUtil.getStringJson("result", "3");
			return "json_message";
		}
		Long nTime = System.currentTimeMillis()/1000;
		Long lTime =(System.currentTimeMillis()+1000*60*60*24*2)/1000;			/*当前时间之前的三天*/ 
		Long tTime =Long.valueOf(proxy.getProxyTestTime());
		if(lTime<tTime){
			this.json=JsonMessageUtil.getStringJson("result", "4");
			return "json_message";
		}
		proxy.setProxyTestTime(proxy.getProxyEndTime());
		this.proxyManager.edit(proxy);
		this.json=JsonMessageUtil.getStringJson("result", "1");
		return "json_message";
	}
	
	
	
	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}

	public IGoodsAgentManager getGoodsAgentManager() {
		return goodsAgentManager;
	}

	public void setGoodsAgentManager(IGoodsAgentManager goodsAgentManager) {
		this.goodsAgentManager = goodsAgentManager;
	}
	public int getAgentnum() {
		return agentnum;
	}
	public void setAgentnum(int agentnum) {
		this.agentnum = agentnum;
	}
	public int getNobzj() {
		return nobzj;
	}
	public void setNobzj(int nobzj) {
		this.nobzj = nobzj;
	}
	public int getXsqx() {
		return xsqx;
	}
	public void setXsqx(int xsqx) {
		this.xsqx = xsqx;
	}

	public int getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}

	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

	public IUserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(IUserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

	public IMemberShopManager getMemberShopManager() {
		return memberShopManager;
	}

	public void setMemberShopManager(IMemberShopManager memberShopManager) {
		this.memberShopManager = memberShopManager;
	}

	public int getOnsalenum() {
		return onsalenum;
	}

	public void setOnsalenum(int onsalenum) {
		this.onsalenum = onsalenum;
	}

	public String getGoodsLabel() {
		return goodsLabel;
	}

	public void setGoodsLabel(String goodsLabel) {
		this.goodsLabel = goodsLabel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGoodsCategory() {
		return goodsCategory;
	}

	public void setGoodsCategory(String goodsCategory) {
		this.goodsCategory = goodsCategory;
	}

	public IGoodsProxyManager getGoodsProxyManager() {
		return goodsProxyManager;
	}

	public void setGoodsProxyManager(IGoodsProxyManager goodsProxyManager) {
		this.goodsProxyManager = goodsProxyManager;
	}

	public IFollowManager getFollowManager() {
		return followManager;
	}

	public void setFollowManager(IFollowManager followManager) {
		this.followManager = followManager;
	}

	public int getProxyid() {
		return proxyid;
	}

	public void setProxyid(int proxyid) {
		this.proxyid = proxyid;
	}

	public IProxyCountManager getProxyCountManager() {
		return proxyCountManager;
	}

	public void setProxyCountManager(IProxyCountManager proxyCountManager) {
		this.proxyCountManager = proxyCountManager;
	}

	public IProxyMemberBankInfoManager getProxyMemberBankInfoManager() {
		return proxyMemberBankInfoManager;
	}

	public void setProxyMemberBankInfoManager(
			IProxyMemberBankInfoManager proxyMemberBankInfoManager) {
		this.proxyMemberBankInfoManager = proxyMemberBankInfoManager;
	}

	public IVipLevelDetailManager getVipLevelDetailManager() {
		return vipLevelDetailManager;
	}

	public void setVipLevelDetailManager(
			IVipLevelDetailManager vipLevelDetailManager) {
		this.vipLevelDetailManager = vipLevelDetailManager;
	}

	public ICreditManager getCreditManager() {
		return creditManager;
	}

	public void setCreditManager(ICreditManager creditManager) {
		this.creditManager = creditManager;
	}
}
