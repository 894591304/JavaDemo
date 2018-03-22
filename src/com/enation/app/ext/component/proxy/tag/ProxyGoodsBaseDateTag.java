package com.enation.app.ext.component.proxy.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.ext.component.membershop.model.MemberShop;
import com.enation.app.ext.component.membershop.service.impl.MemberShopManager;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.shop.core.plugin.goods.GoodsDataFilterBundle;
import com.enation.app.shop.core.plugin.goods.GoodsPluginBundle;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.ObjectNotFoundException;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.RequestUtil;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class ProxyGoodsBaseDateTag extends BaseFreeMarkerTag{

	private IProxyManager proxyManager;
	private IGoodsManager goodsManager;
	private GoodsDataFilterBundle goodsDataFilterBundle;
	private GoodsPluginBundle goodsPluginBundle;
	private MemberShopManager memberShopManager;
	
	protected Object exec(Map params) throws TemplateModelException {
		try {
				Integer proxyId=(Integer)params.get("proxyid");
				if(proxyId == null){
					proxyId = getProxyId();
				}
				Proxy proxy = this.proxyManager.get(proxyId);
				MemberShop memberShop = this.memberShopManager.get(proxy.getMemberId());
				Map goodsMap = this.goodsManager.get(proxy.getGoodsId());
				if(memberShop.getShowOrHide()==0){
					goodsMap.put("show",0);
					return goodsMap;
				}
				goodsMap.put("show",1);
				if(goodsMap==null){
					throw new UrlNotFoundException();
				}
				
				if(goodsMap.get("market_enable").toString().equals("0")){
					throw new UrlNotFoundException();
				}
				
				if(goodsMap.get("disabled").toString().equals("1")){
					throw new UrlNotFoundException();
				}
				
				List<Map> goodsList = new ArrayList();
				goodsList.add(goodsMap);
				this.goodsDataFilterBundle.filterGoodsData(goodsList);
				
				goodsMap.put("proxyid", String.valueOf(proxy.getId()));
				getRequest().setAttribute("goods", goodsMap);
				this.goodsPluginBundle.onVisit(goodsMap);
				
				return goodsMap;
			}
			catch (ObjectNotFoundException e) {
				throw new UrlNotFoundException();
			}
		} 
	private Integer getProxyId()
	{
		HttpServletRequest httpRequest = ThreadContextHolder.getHttpRequest();
		String url = RequestUtil.getRequestUrl(httpRequest);
		String proxyId = paseProxyId(url);
		return Integer.valueOf(proxyId);
	}
	
	private static String paseProxyId(String url){
		String pattern = "(-)(\\d+)";
		String value = null;
		Pattern p = Pattern.compile(pattern,34);
		Matcher m = p.matcher(url);
		if(m.find()){
			value = m.group(2);
		}
		return value;
	}
	public IProxyManager getProxyManager() {
		return proxyManager;
	}
	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}
	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}
	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}
	public GoodsDataFilterBundle getGoodsDataFilterBundle() {
		return goodsDataFilterBundle;
	}
	public void setGoodsDataFilterBundle(GoodsDataFilterBundle goodsDataFilterBundle) {
		this.goodsDataFilterBundle = goodsDataFilterBundle;
	}
	public GoodsPluginBundle getGoodsPluginBundle() {
		return goodsPluginBundle;
	}
	public void setGoodsPluginBundle(GoodsPluginBundle goodsPluginBundle) {
		this.goodsPluginBundle = goodsPluginBundle;
	}
	public MemberShopManager getMemberShopManager() {
		return memberShopManager;
	}
	public void setMemberShopManager(MemberShopManager memberShopManager) {
		this.memberShopManager = memberShopManager;
	}

}
