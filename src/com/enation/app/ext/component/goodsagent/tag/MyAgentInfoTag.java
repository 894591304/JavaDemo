package com.enation.app.ext.component.goodsagent.tag;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.goodsagent.model.GoodsAgent;
import com.enation.app.ext.component.goodsagent.service.IGoodsAgentManager;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderItemsManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderManager;
import com.enation.app.ext.component.useraccount.service.IUserAccountManager;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.ObjectNotFoundException;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.RequestUtil;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class MyAgentInfoTag extends BaseFreeMarkerTag{

	private IGoodsProxyManager goodsProxyManager;
	private IGoodsAgentManager goodsAgentManager;
	private IProxyManager proxyManager;
	private IUserAccountManager userAccountManager;
	private INewOrderItemsManager newOrderItemsManager;
	private INewOrderManager newOrderManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[MyAgentInfoTag]");
		}
		try {
			Integer proxyid = getProxyId();
			Proxy proxy = this.proxyManager.get(proxyid);
			Map result = new HashMap();
			int goodsid = proxy.getGoodsId();
			Goods goods = this.goodsProxyManager.getGoods(goodsid);
			GoodsAgent goodsAgent = this.goodsAgentManager.get(goodsid);
			List<OrderItem> oList = this.newOrderItemsManager.getByProxyId(proxyid);
			List orderList = new ArrayList();
			int count = oList.size();
			for(int i=0;i<count;i++){
				Map orderMap = new HashMap();
				OrderItem orderItem = oList.get(i);
				int orderid = orderItem.getOrder_id();
				Order order = this.newOrderManager.getById(orderid);
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				long ctime = new Long(order.getCreate_time());
				Date cdate = new Date(ctime);
				String ct = simpleDateFormat.format(cdate);
				orderMap.put("ordersn",String.valueOf(order.getSn()));
				orderMap.put("num",orderItem.getNum());
				orderMap.put("status",order.getPay_status());
				orderMap.put("ctime",ct);
				orderMap.put("proxyid",String.valueOf(orderItem.getAddon()));
				orderList.add(orderMap);
			}
			
			int level = this.userAccountManager.getLevel(member.getMember_id());
			if(level==1){
				result.put("agentprice",goodsAgent.getGoldPrice());
			}else if(level==2){
				result.put("agentprice",goodsAgent.getPlatinumPrice());
			}else{
				result.put("agentprice",goodsAgent.getBlackPrice());
			}
			
			result.put("proxyid",String.valueOf(proxyid));
			result.put("goodsid",String.valueOf(goodsid));
			result.put("price",goods.getPrice());
			result.put("Mktprice",goods.getMktprice());
			result.put("goodsname",goods.getName());
			result.put("goodsimg",goods.getOriginal());
			result.put("sale",proxy.getSale());
			result.put("notsale",proxy.getOnSale()-proxy.getSale());
			result.put("notonsale",proxy.getGoodsAmount()-proxy.getOnSale());
			result.put("frozendeposit",proxy.getFrozenDeposit());
			result.put("frozenCredit",proxy.getFrozenCredit());
			result.put("frozenearn",proxy.getFrozenEarn());
			result.put("ticketvalue",proxy.getTicketFrozenCredit());
			result.put("orderList",orderList);
			return result;
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
	
	public IGoodsProxyManager getGoodsProxyManager() {
		return goodsProxyManager;
	}

	public void setGoodsProxyManager(IGoodsProxyManager goodsProxyManager) {
		this.goodsProxyManager = goodsProxyManager;
	}

	public IGoodsAgentManager getGoodsAgentManager() {
		return goodsAgentManager;
	}

	public void setGoodsAgentManager(IGoodsAgentManager goodsAgentManager) {
		this.goodsAgentManager = goodsAgentManager;
	}

	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}
	public IUserAccountManager getUserAccountManager() {
		return userAccountManager;
	}
	public void setUserAccountManager(IUserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}
	public INewOrderItemsManager getNewOrderItemsManager() {
		return newOrderItemsManager;
	}
	public void setNewOrderItemsManager(INewOrderItemsManager newOrderItemsManager) {
		this.newOrderItemsManager = newOrderItemsManager;
	}
	public INewOrderManager getNewOrderManager() {
		return newOrderManager;
	}
	public void setNewOrderManager(INewOrderManager newOrderManager) {
		this.newOrderManager = newOrderManager;
	}

}
