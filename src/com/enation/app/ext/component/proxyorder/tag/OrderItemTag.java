package com.enation.app.ext.component.proxyorder.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.goodsdiscountticketdetail.model.GoodsDiscountTicketDetail;
import com.enation.app.ext.component.goodsdiscountticketdetail.service.IGoodsDiscountTicketDetailManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderItemsManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderManager;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class OrderItemTag extends BaseFreeMarkerTag{

	private INewOrderManager newOrderManager;
	private INewOrderItemsManager newOrderItemsManager;
	private IGoodsProxyManager goodsProxyManager;
	private IGoodsDiscountTicketDetailManager goodsDiscountTicketDetailManager;
	
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
			String ordersn = null;
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
			}else{
				List<OrderItem> oList = this.newOrderItemsManager.getByOrderId(orderid);
				List oiList = new ArrayList();
				int count = oList.size();
				for(int i=0;i<count;i++){
					OrderItem o = oList.get(i);
					GoodsDiscountTicketDetail goodsDiscountTicketDetail = this.goodsDiscountTicketDetailManager.getByItemId(o.getItem_id());
					float ticketValue = 0;
					if(goodsDiscountTicketDetail!=null){
						ticketValue=goodsDiscountTicketDetail.getTicketValue();
					}				
					Goods goods = this.goodsProxyManager.getGoods(o.getGoods_id());
					Map oiMap = new HashMap();
					oiMap.put("ticket",String.valueOf(o.getAddon()));
					oiMap.put("proxyid",String.valueOf(o.getAddon()));
					oiMap.put("itemid",String.valueOf(o.getItem_id()));
					oiMap.put("img",goods.getOriginal());				
					oiMap.put("name",goods.getName());
					oiMap.put("price",goods.getPrice());
					oiMap.put("num",o.getNum());
					oiMap.put("ticket",ticketValue);
					oiMap.put("pay",goods.getPrice()*o.getNum()-ticketValue);
					oiList.add(oiMap);
				}
				result.put("orderitemlist",oiList);
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

	public IGoodsDiscountTicketDetailManager getGoodsDiscountTicketDetailManager() {
		return goodsDiscountTicketDetailManager;
	}

	public void setGoodsDiscountTicketDetailManager(
			IGoodsDiscountTicketDetailManager goodsDiscountTicketDetailManager) {
		this.goodsDiscountTicketDetailManager = goodsDiscountTicketDetailManager;
	}

}
