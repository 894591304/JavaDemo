package com.enation.app.ext.component.ticketgive.tag;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.proxyorder.model.ProxyOrder;
import com.enation.app.ext.component.proxyorder.service.INewOrderItemsManager;
import com.enation.app.ext.component.proxyorder.service.IProxyOrderManager;
import com.enation.app.ext.component.ticketgive.model.TicketGive;
import com.enation.app.ext.component.ticketgive.service.ITicketGiveManager;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class TicketExchangeDetailTag extends BaseFreeMarkerTag{

	private ITicketGiveManager ticketGiveManager;
	private IGoodsProxyManager goodsProxyManager;
	private IProxyOrderManager proxyOrderManager;
	private INewOrderItemsManager newOrderItemsManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		Map result = new HashMap();
		if(member == null){
			result.put("re", 3);
			return result;
		}
		try
		{
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			Map<String, String> params = new HashMap();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String)iter.next();
				String[] values = (String[])requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}
			int proxyorderId = 0;
			if(request.getParameter("proxyorderId")==null){
				result.put("re", 0);
				return result;
			}
			proxyorderId = Integer.valueOf(request.getParameter("proxyorderId"));
			TicketGive ticketGive = this.ticketGiveManager.getByProxyOrderId(proxyorderId);
			if(ticketGive.getMemberId()!=member.getMember_id()){
				result.put("re", 0);
				return result;
			}
			ProxyOrder proxyOrder = this.proxyOrderManager.get(ticketGive.getProxyorderId());
			OrderItem orderItem = this.newOrderItemsManager.get(proxyOrder.getItemId());
			Goods goods = this.goodsProxyManager.getGoods(orderItem.getGoods_id());
			result.put("re", 1);
			result.put("img",goods.getOriginal());
			result.put("proxyid",String.valueOf(orderItem.getAddon()));
			result.put("name",goods.getName());
			result.put("num",orderItem.getNum());
		}catch (Exception e)
		{
			this.logger.error("查询失败！", e);
		}
		return result;
	}

	public ITicketGiveManager getTicketGiveManager() {
		return ticketGiveManager;
	}

	public void setTicketGiveManager(ITicketGiveManager ticketGiveManager) {
		this.ticketGiveManager = ticketGiveManager;
	}

	public IGoodsProxyManager getGoodsProxyManager() {
		return goodsProxyManager;
	}

	public void setGoodsProxyManager(IGoodsProxyManager goodsProxyManager) {
		this.goodsProxyManager = goodsProxyManager;
	}

	public IProxyOrderManager getProxyOrderManager() {
		return proxyOrderManager;
	}

	public void setProxyOrderManager(IProxyOrderManager proxyOrderManager) {
		this.proxyOrderManager = proxyOrderManager;
	}

	public INewOrderItemsManager getNewOrderItemsManager() {
		return newOrderItemsManager;
	}

	public void setNewOrderItemsManager(INewOrderItemsManager newOrderItemsManager) {
		this.newOrderItemsManager = newOrderItemsManager;
	}
}
