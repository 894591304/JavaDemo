package com.enation.app.ext.component.proxycart.service.impl;

import com.enation.app.ext.component.proxycart.model.ProxyCart;
import com.enation.app.ext.component.proxycart.service.IProxyCartManager;
import com.enation.eop.sdk.database.BaseSupport;

public class ProxyCartManager extends BaseSupport<ProxyCart> implements IProxyCartManager{

	public void add(ProxyCart proxyCart) {
		String sql = "select count(*) from proxycart where cartId = ?";
		int count = this.baseDaoSupport.queryForInt(sql, new Object[]{proxyCart.getCartid()});
		if(count==0)
		{this.baseDaoSupport.insert("proxycart", proxyCart);}
	}

	public void update(ProxyCart proxyCart) {
		this.baseDaoSupport.update("proxycart", proxyCart,"cartId="+proxyCart.getCartid());
	}

	public void delete(int cartId) {
		String sql = "delete from proxycart where cartId = ?";
		this.baseDaoSupport.execute(sql, new Object[]{cartId});
	}

}
