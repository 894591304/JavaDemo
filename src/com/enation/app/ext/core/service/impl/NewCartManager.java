package com.enation.app.ext.core.service.impl;

import java.util.List;

import com.enation.app.ext.core.service.INewCartManager;
import com.enation.app.shop.core.model.mapper.CartItemMapper;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.app.shop.core.plugin.cart.CartPluginBundle;
import com.enation.eop.sdk.database.BaseSupport;

public class NewCartManager extends BaseSupport implements INewCartManager{

	private CartPluginBundle cartPluginBundle;
	
	public List<CartItem> listGoods(String sessionid) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("select g.cat_id as catid,g.goods_id,g.original as thumbnail,c.name ,  p.sn, p.specs  ,g.mktprice,g.unit,g.point,p.product_id,c.price,c.cart_id as cart_id,c.num as num,c.itemtype,c.addon  from " + getTableName("cart") + " c," + getTableName("product") + " p," + getTableName("goods") + " g ");
		sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id and c.session_id=?");
		List<CartItem> list = this.daoSupport.queryForList(sql.toString(), new CartItemMapper(), new Object[] { sessionid });
		int total = list.size();
		this.cartPluginBundle.filterList(list, sessionid);		
		return list;
	}

	public CartPluginBundle getCartPluginBundle() {
		return cartPluginBundle;
	}

	public void setCartPluginBundle(CartPluginBundle cartPluginBundle) {
		this.cartPluginBundle = cartPluginBundle;
	}

}
