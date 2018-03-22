package com.enation.app.ext.component.proxycart.service;

import com.enation.app.ext.component.proxycart.model.ProxyCart;

public abstract interface IProxyCartManager 
{
	public abstract void add(ProxyCart proxyCart);
	
	public abstract void update(ProxyCart proxyCart);
	
	public abstract void delete(int id);
}
