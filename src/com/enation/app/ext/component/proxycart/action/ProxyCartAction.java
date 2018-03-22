package com.enation.app.ext.component.proxycart.action;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.proxycart.model.ProxyCart;
import com.enation.app.ext.component.proxycart.service.IProxyCartManager;
import com.enation.app.ext.core.service.ICartExtManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;

public class ProxyCartAction extends WWAction{

	private static final long serialVersionUID = 6420949229964696904L;
	
	private ICartExtManager cartExtManager;
	private IProxyCartManager proxyCartManager;
	
	private int goodsid;
	private int proxyid;
	
	public String addCart(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		try
		{
			String sessionid = ThreadContextHolder.getHttpRequest().getSession().getId();
			int cartid = this.cartExtManager.getCartidByMidGid(this.goodsid, sessionid);
			ProxyCart proxyCart = new ProxyCart();
			proxyCart.setCartid(cartid);
			proxyCart.setProxyid(this.proxyid);
			proxyCart.setMemberid(member.getMember_id());
			proxyCart.setSessionid(sessionid);
			this.cartExtManager.setProxyidInAddonByCartid(cartid,this.proxyid);
			this.proxyCartManager.add(proxyCart);
			showSuccessJson("代理商品成功添加");
		}
		catch(RuntimeException e){
			this.logger.error("将代理货品添加至购物车出错", e);
			showErrorJson("将代理货品加至购物车出错[" + e.getMessage() + "]");
		}		
		return "json_message";
	}
	
	public String checkCart(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		try
		{
			String sessionid = ThreadContextHolder.getHttpRequest().getSession().getId();
			int re = this.cartExtManager.checkCart(this.goodsid,this.proxyid, sessionid);
			if(re==0){this.json=JsonMessageUtil.getStringJson("result", "0");}
			else{this.json=JsonMessageUtil.getStringJson("result", "1");}
		}
		catch(RuntimeException e){
			this.logger.error("error", e);
			showErrorJson("error]");
		}				
		return "json_message";
	}
	
	public String replace(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		try
		{
			String sessionid = ThreadContextHolder.getHttpRequest().getSession().getId();
			this.cartExtManager.replace(this.goodsid,this.proxyid, sessionid);
			this.json=JsonMessageUtil.getStringJson("result", "1");
		}
		catch(RuntimeException e){
			this.logger.error("error", e);
			showErrorJson("error]");
		}	
		return "json_message";
	}
	
	public ICartExtManager getCartExtManager() {
		return cartExtManager;
	}
	public void setCartExtManager(ICartExtManager cartExtManager) {
		this.cartExtManager = cartExtManager;
	}
	public IProxyCartManager getProxyCartManager() {
		return proxyCartManager;
	}
	public void setProxyCartManager(IProxyCartManager proxyCartManager) {
		this.proxyCartManager = proxyCartManager;
	}
	public int getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}
	public int getProxyid() {
		return proxyid;
	}
	public void setProxyid(int proxyid) {
		this.proxyid = proxyid;
	}
}
