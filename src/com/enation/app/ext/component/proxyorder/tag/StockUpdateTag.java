package com.enation.app.ext.component.proxyorder.tag;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderItemsManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderManager;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.sun.mail.handlers.image_gif;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class StockUpdateTag extends BaseFreeMarkerTag{

	private INewOrderManager newOrderManager;
	private INewOrderItemsManager newOrderItemsManager;
	private IGoodsProxyManager goodsProxyManager;
	private IProxyManager proxyManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[MyPayOrderTag]");
		}
		Map result = new HashMap();
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
			if(request.getParameter("orderid")==null){
				result.put("orderget", "0");
				return result;
			}
			result.put("orderget", "1");
			int orderid = Integer.valueOf(request.getParameter("orderid"));
			Order order = this.newOrderManager.getById(orderid);
			if(order==null){
				result.put("orderget", "0");
				return result;
			}
			String check = order.getRemark();
			if(check==null||check==""){
				List<OrderItem> oList = this.newOrderItemsManager.getByOrderId(orderid);
				int total = oList.size();
				for(int i=0;i<total;i++){
					OrderItem orderItem = oList.get(i);
					Proxy proxy = this.proxyManager.get(Integer.valueOf(orderItem.getAddon()));
					proxy.setSale(proxy.getSale()+orderItem.getNum());
					this.proxyManager.edit(proxy);
					System.out.println("库存已更新！");
				}
				order.setRemark("1");
				this.newOrderManager.update(order);
			}else{
				System.out.println("第二次访问，无需更新！");
			}			
		}
		catch (Exception e)
		{
			this.logger.error("查询失败！", e);
		}
		return result; 
	}

	public INewOrderManager getNewOrderManager() {
		return newOrderManager;
	}

	public void setNewOrderManager(INewOrderManager newOrderManager) {
		this.newOrderManager = newOrderManager;
	}

	public INewOrderItemsManager getNewOrderItemsManager() {
		return newOrderItemsManager;
	}

	public void setNewOrderItemsManager(INewOrderItemsManager newOrderItemsManager) {
		this.newOrderItemsManager = newOrderItemsManager;
	}

	public IGoodsProxyManager getGoodsProxyManager() {
		return goodsProxyManager;
	}

	public void setGoodsProxyManager(IGoodsProxyManager goodsProxyManager) {
		this.goodsProxyManager = goodsProxyManager;
	}

	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

}
