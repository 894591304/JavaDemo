package com.enation.app.ext.component.proxy.action;


import javax.servlet.http.HttpServletRequest;

import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonMessageUtil;



public class ProxyAction extends WWAction
{
	
	private static final long serialVersionUID = -3678086328343768990L;
	
	private IProxyManager proxyManager;
	private int proxyid;
	
	public String checkStore(){
		try
		{
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			Proxy proxy = this.proxyManager.get(this.proxyid);
			int store = proxy.getOnSale()-proxy.getSale();
			this.json = JsonMessageUtil.getNumberJson("store",store);
		} catch (RuntimeException e) {
			this.logger.error("获取商品库存出现意外错误", e);
			showErrorJson("获取商品库存出现意外错误" + e.getMessage());
		}
		return "json_message";
	}
	
	public IProxyManager getProxyManager() {
		return proxyManager;
	}
	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

	public int getProxyid() {
		return proxyid;
	}

	public void setProxyid(int proxyid) {
		this.proxyid = proxyid;
	}

	
	
}
