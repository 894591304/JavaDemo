package com.enation.app.ext.component.proxyorder.tag;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.goods.model.TicketDetail;
import com.enation.app.b2b2c.component.goods.service.ITicketDetailManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.proxyorder.model.ProxyOrder;
import com.enation.app.ext.component.proxyorder.service.INewOrderItemsManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderManager;
import com.enation.app.ext.component.proxyorder.service.IProxyOrderManager;
import com.enation.app.ext.component.ticketgive.model.TicketGive;
import com.enation.app.ext.component.ticketgive.service.ITicketGiveManager;
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
public class MyVipTicketTag extends BaseFreeMarkerTag{
	
	private INewOrderManager newOrderManager;
	private IProxyOrderManager proxyOrderManager;
	private INewOrderItemsManager newOrderItemsManager;
	private ITicketDetailManager ticketDetailManager;
	private IGoodsProxyManager goodsProxyManager;
	private ITicketGiveManager ticketGiveManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		Map result = new HashMap();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[MyPayOrderTag]");
		}
		try {
			Integer proxyorderid = getUrlId();
			ProxyOrder proxyOrder = this.proxyOrderManager.get(proxyorderid);
			if(proxyOrder.getGoodsId()!=499&&proxyOrder.getGoodsId()!=498&&proxyOrder.getGoodsId()!=497){
				result.put("ticketget",0);
				return result;
			}
			if(proxyOrder==null){
				result.put("ticketget",0);
				return result;
			}else{
				OrderItem orderItem = this.newOrderItemsManager.get(proxyOrder.getItemId());
				Order order = this.newOrderManager.getById(orderItem.getOrder_id());
				int m1 = order.getMember_id();
				int m2 = member.getMember_id();
				TicketGive ticketGive = this.ticketGiveManager.getByProxyOrderId(proxyorderid);
				int m3 = 0;
				if(ticketGive!=null){
					m3 = ticketGive.getMemberId();
				}
				if(m2!=m1&&m2!=m3){
					result.put("ticketget",0);
					return result;
				}else{
					result.put("ticketget",1);
					Goods goods = this.goodsProxyManager.getGoods(orderItem.getGoods_id());
					TicketDetail ticketDetail = this.ticketDetailManager.get(proxyOrder.getTicketId());
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
					long createtime = new Long(order.getCreate_time());
					Date ctime = new Date(createtime*1000);
					String ct = simpleDateFormat.format(ctime);
					Date vUseTime = new Date(createtime*1000+15*24*60*60*1000);
					String vu = simpleDateFormat.format(vUseTime);
					SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
					long limittime = new Long(ticketDetail.getEnddate());
					long begintime = new Long(ticketDetail.getStartdate());
					Date ndate = new Date();
					long nowtime = ndate.getTime();
					Date ldate = new Date(limittime);
					String lt = simpleDateFormat1.format(ldate);
					String thirdplatform = this.goodsProxyManager.getThird(orderItem.getGoods_id());
					if(thirdplatform==null){
						thirdplatform="";
					}
					if(nowtime>begintime&&nowtime<limittime){
						result.put("canuse",1);
					}else if(nowtime<begintime){
						result.put("canuse",2);
					}else{
						result.put("canuse",0);
					}
					if(proxyOrder.getStatus()==0){
						result.put("notuse",1);
					}else{
						if(proxyOrder.getStatus()==3){
							result.put("notuse",3);
						}else if(proxyOrder.getStatus()==4){
							if(m2==m3){
								result.put("notuse",6);
							}else{
								result.put("notuse",4);
								}
						}else if(proxyOrder.getStatus()==5){
							result.put("notuse",5);
						}else{
							result.put("notuse",0);
						}
					}
					if(proxyOrder.getGoodsId()==499){
						result.put("viptime", 360);
					}else if(proxyOrder.getGoodsId()==498){
						result.put("viptime", 90);
					}else{
						result.put("viptime", 30);
					}
					result.put("proxyorderid",String.valueOf(proxyorderid));
					result.put("img",goods.getOriginal());
					result.put("name",goods.getName());
					result.put("orderid",String.valueOf(order.getSn()));
					result.put("createtime",ct);
					result.put("limittime",lt);
					result.put("proxyid",String.valueOf(orderItem.getAddon()));
					result.put("proxymemberid",proxyOrder.getProxyMemberId());
					result.put("vusetime", vu);
					result.put("thirdplatform",thirdplatform);
					result.put("code",ticketDetail.getTicketCode());
					result.put("ticketid",String.valueOf(ticketDetail.getId()));
				}
			}			
		}
		catch (ObjectNotFoundException e) {
			throw new UrlNotFoundException();
		}
		return result;
	}

	private Integer getUrlId()
	{
		HttpServletRequest httpRequest = ThreadContextHolder.getHttpRequest();
		String url = RequestUtil.getRequestUrl(httpRequest);
		String urlid = paseUrlId(url);
		return Integer.valueOf(urlid);
	}
	
	private static String paseUrlId(String url){
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

	public ITicketDetailManager getTicketDetailManager() {
		return ticketDetailManager;
	}

	public void setTicketDetailManager(ITicketDetailManager ticketDetailManager) {
		this.ticketDetailManager = ticketDetailManager;
	}

	public IGoodsProxyManager getGoodsProxyManager() {
		return goodsProxyManager;
	}

	public void setGoodsProxyManager(IGoodsProxyManager goodsProxyManager) {
		this.goodsProxyManager = goodsProxyManager;
	}

	public ITicketGiveManager getTicketGiveManager() {
		return ticketGiveManager;
	}

	public void setTicketGiveManager(ITicketGiveManager ticketGiveManager) {
		this.ticketGiveManager = ticketGiveManager;
	}
}
