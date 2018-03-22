package com.enation.app.b2b2c.component.goods.action;

import com.enation.app.b2b2c.component.goods.service.IGoodsRulesManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.framework.action.WWAction;

public class GoodsRulesAction extends WWAction {

	private static final long serialVersionUID = -8618054430286090133L;
	private String proxyMembers;
	private String brokerageUsers;
	private IGoodsRulesManager goodsRulesManager;
	private String goodsid;
	
	public String setGoodsRules() {
		this.proxyMembers = this.goodsRulesManager.getProxyMembers(goodsid);
		this.brokerageUsers = this.goodsRulesManager.getBrokerageUsers(goodsid);
		return "set_goods_rules";
	}
	
	public String saveGoodsRules(){
		if(!this.brokerageUsers.equals("")){
			this.goodsRulesManager.setBrokerageUsers(this.brokerageUsers, this.goodsid);
		}else{
			this.goodsRulesManager.deleteBrokerageUsers(this.goodsid);
		}
		if(!this.proxyMembers.equals("")){
			this.goodsRulesManager.setProxyMembers(this.proxyMembers, this.goodsid);
		}else{
			this.goodsRulesManager.deleteProxyMembers(this.goodsid);
		}
		showSuccessJson("设置商品不可见规则成功");
		return "json_message";
	}

	private IGoodsManager goodsManager;

	public String getProxyMembers() {
		return proxyMembers;
	}

	public void setProxyMembers(String proxyMembers) {
		this.proxyMembers = proxyMembers;
	}

	public String getBrokerageUsers() {
		return brokerageUsers;
	}

	public void setBrokerageUsers(String brokerageUsers) {
		this.brokerageUsers = brokerageUsers;
	}

	public IGoodsRulesManager getGoodsRulesManager() {
		return goodsRulesManager;
	}

	public void setGoodsRulesManager(IGoodsRulesManager goodsRulesManager) {
		this.goodsRulesManager = goodsRulesManager;
	}

	public String getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}

	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}

}
