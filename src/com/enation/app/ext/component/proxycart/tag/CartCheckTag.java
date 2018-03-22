package com.enation.app.ext.component.proxycart.tag;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderItemsManager;
import com.enation.app.ext.core.service.ICartExtManager;
import com.enation.app.ext.core.service.INewCartManager;
import com.enation.app.shop.core.model.Cart;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class CartCheckTag extends BaseFreeMarkerTag{

	private INewCartManager newCartManager;
	private IProxyManager proxyManager;
	private ICartExtManager cartExtManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String sessionid = request.getSession().getId();
		List<Cart> cList = this.cartExtManager.getBySid(sessionid);
		int total = cList.size();
		for(int i=0;i<total;i++){
			Cart cart = cList.get(i);
			Proxy proxy = this.proxyManager.get(Integer.valueOf(cart.getAddon()));
			if(cart.getNum()>(proxy.getOnSale()-proxy.getSale())){
				cart.setNum(proxy.getOnSale()-proxy.getSale());
				if(cart.getNum()==0){
					this.cartExtManager.cartDelete(cart);
				}
				this.cartExtManager.cartUpdate(cart);
			}
		}
		return 1;
	}

	public INewCartManager getNewCartManager() {
		return newCartManager;
	}

	public void setNewCartManager(INewCartManager newCartManager) {
		this.newCartManager = newCartManager;
	}

	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

	public ICartExtManager getCartExtManager() {
		return cartExtManager;
	}

	public void setCartExtManager(ICartExtManager cartExtManager) {
		this.cartExtManager = cartExtManager;
	}

}
