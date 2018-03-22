package com.enation.app.ext.core.service;

import java.util.List;

import com.enation.app.shop.core.model.Cart;

public abstract interface ICartExtManager 
{
	public abstract int getCartidByMidGid(int goodid,String sessionid);
	
	public abstract void setProxyidInAddonByCartid(int cartid,int proxyid);
	
	public abstract void cartUpdate(Cart cart);
	
	public abstract int checkCart(int goodsid,int proxyid,String sessionid);
	
	public abstract void replace(int goodsid,int proxyid,String sessionid);
	
	public abstract List<Cart> getBySid(String sessionid);
	
	public abstract void cartDelete(Cart cart);
}
