package com.enation.app.ext.core.service.impl;

import java.util.List;

import com.enation.app.ext.core.service.ICartExtManager;
import com.enation.app.shop.core.model.Cart;
import com.enation.eop.sdk.database.BaseSupport;

public class CartExtManager extends BaseSupport<Cart> implements ICartExtManager
{

	public int getCartidByMidGid(int goodsid, String sessionid) {
		String sql = "select cart_id from cart where goods_id = ? and session_id = ?";
		int cartid = this.baseDaoSupport.queryForInt(sql, new Object[]{goodsid,sessionid});
		return cartid;
	}

	
	public void setProxyidInAddonByCartid(int cartid, int proxyid) {
		String sql = "select * from cart where cart_id = ?";
		Cart cart = this.baseDaoSupport.queryForObject(sql, Cart.class,new Object[]{cartid});
		cart.setAddon(String.valueOf(proxyid));
		cartUpdate(cart);
	}
	
	public void cartUpdate(Cart cart){
		this.baseDaoSupport.update("cart", cart, "cart_id = "+cart.getCart_id());
	}

	public int checkCart(int goodsid, int proxyid, String sessionid) {
		String sql1 = "select count(*) from cart where goods_id = ? and session_id = ?";
		String sql2 = "select count(*) from cart where goods_id = ? and session_id = ? and addon = ?";
		int count1 = this.baseDaoSupport.queryForInt(sql1, new Object[]{goodsid,sessionid});
		int count2 = this.baseDaoSupport.queryForInt(sql2, new Object[]{goodsid,sessionid,proxyid});
		if(count1!=0&&count2==0){
			return 0;
		}else{
			return 1;
		}
	}


	public void replace(int goodsid, int proxyid, String sessionid) {
		String sql = "update cart set addon = ? , num = '1' where goods_id = ? and session_id = ?";
		String sql1 = "select * from cart where goods_id = ? and session_id = ?";
		Cart cart = this.baseDaoSupport.queryForObject(sql1, Cart.class,new Object[]{goodsid,sessionid});
		if (cart!=null)
		{
			this.baseDaoSupport.execute(sql, new Object[]{proxyid,goodsid,sessionid});
		}
	}


	@Override
	public List<Cart> getBySid(String sessionid) {
		String sql = "select * from cart where session_id = ?";
		List<Cart> cList = this.baseDaoSupport.queryForList(sql, Cart.class,new Object[]{sessionid});
		return cList;
	}


	@Override
	public void cartDelete(Cart cart) {
		String sql = "delete from cart where cart_id = ?";
		this.baseDaoSupport.execute(sql, new Object[]{cart.getCart_id()});
	}

}
