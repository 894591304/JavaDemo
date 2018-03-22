package com.enation.app.ext.component.proxy.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class ProxyCanBuyTag extends BaseFreeMarkerTag{

	private IProxyManager proxyManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Map result = new HashMap();
		HttpServletRequest request = getRequest();
		Map goods = (Map)request.getAttribute("goods");
		if (goods == null) { 
			throw new TemplateModelException("调用代理商品属性标签前，必须先调用代理商品基本信息标签");
		}
		Object pid = goods.get("proxyid");
		int proxysid = Integer.parseInt(pid.toString());
		int re = this.proxyManager.checkProxyCanBuy(proxysid);
		if(re==0){result.put("canbuy",0);}
		else{result.put("canbuy",1);}
		return result;
	}

	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

}
