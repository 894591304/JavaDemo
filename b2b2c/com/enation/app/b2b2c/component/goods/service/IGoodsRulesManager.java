package com.enation.app.b2b2c.component.goods.service;

public abstract interface IGoodsRulesManager {
	public String getProxyMembers(String goodsid);
	public String getBrokerageUsers(String goodsid);
	public void setProxyMembers(String members,String goodsids);
	public void setBrokerageUsers(String members,String goodsids);
	public void deleteBrokerageUsers(String goodsid);
	public void deleteProxyMembers(String goodsid);
}
