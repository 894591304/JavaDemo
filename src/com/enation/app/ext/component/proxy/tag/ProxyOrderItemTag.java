package com.enation.app.ext.component.proxy.tag;

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
import com.enation.app.ext.component.proxyorder.service.INewOrderItemsManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderManager;
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
public class ProxyOrderItemTag extends BaseFreeMarkerTag{

	private INewOrderManager newOrderManager;
	private INewOrderItemsManager newOrderItemsManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[MyAgentInfoTag]");
		}
		try {
			Integer proxyid = getProxyId();
			List<OrderItem> oiList = this.newOrderItemsManager.getByProxyId(proxyid);
			Map result = new HashMap();
			int total = oiList.size();
			List orderList = new ArrayList();
			for(int i=0;i<total;i++){
				Map orderMap = new HashMap();
				OrderItem orderItem = oiList.get(i);
				Order order = this.newOrderManager.getById(orderItem.getOrder_id());
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Long createtime = Long.valueOf(order.getCreate_time());
				String ctime = format.format(new Date(createtime*1000));
				orderMap.put("ordersn","HJYX"+order.getSn());
				orderMap.put("createtime",ctime);
				if(order.getStatus()==2||order.getPay_status()==2){
					if(order.getShip_status()==9){
						orderMap.put("status",2);
					}else{
						orderMap.put("status",1);
					}
				}else{
					orderMap.put("status",0);
				}
				orderList.add(orderMap);
			}
			result.put("orderitemlist",orderList);
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

}
