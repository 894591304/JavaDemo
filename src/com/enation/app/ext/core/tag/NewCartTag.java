package com.enation.app.ext.core.tag;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.goodsdiscountticketdetail.model.GoodsDiscountTicketDetail;
import com.enation.app.ext.component.goodsdiscountticketdetail.service.IGoodsDiscountTicketDetailManager;
import com.enation.app.ext.core.service.INewCartManager;
import com.enation.app.shop.core.model.Cart;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.context.webcontext.ThreadContextHolder;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class NewCartTag implements TemplateMethodModel{

	private INewCartManager newCartManager;
	private IGoodsDiscountTicketDetailManager goodsDiscountTicketDetailManager;
	
	public Object exec(List args) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[NewCartTag]");
		}
		Map result = new HashMap();
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		this.newCartManager = ((INewCartManager)SpringContextHolder.getBean("newCartManager"));
		String sessionid = request.getSession().getId();
		List<CartItem> gList = this.newCartManager.listGoods(sessionid);
		int total = gList.size();
		List goodsList = new ArrayList();
		int num = 0;
		for(int i=0;i<total;i++){
			Map cMap = new HashMap();
			CartItem cartItem = gList.get(i);
			int proxyid = Integer.valueOf(cartItem.getAddon());
			GoodsDiscountTicketDetail goodsDiscountTicketDetail = this.goodsDiscountTicketDetailManager.getHignValueByProxyid(proxyid,member.getMember_id());
			float ticketvalue = 0;
			if(goodsDiscountTicketDetail==null){
				cMap.put("ticket",0);
			}else{
				cMap.put("ticket",goodsDiscountTicketDetail.getTicketValue());
				ticketvalue = goodsDiscountTicketDetail.getTicketValue();
			}
			cMap.put("id",String.valueOf(cartItem.getId()));
			cMap.put("product_id", String.valueOf(cartItem.getProduct_id()));
			cMap.put("price", cartItem.getPrice());
			cMap.put("addon", String.valueOf(cartItem.getAddon()));
			cMap.put("image_default",cartItem.getImage_default());
			cMap.put("name",cartItem.getName());
			cMap.put("coupPrice",cartItem.getCoupPrice());
			cMap.put("subtotal",cartItem.getSubtotal());
			cMap.put("subtotal2",cartItem.getSubtotal()-ticketvalue);
			cMap.put("num",String.valueOf(cartItem.getNum()));
			goodsList.add(cMap);
		}		
		result.put("goodsList",goodsList);
		result.put("count",goodsList.size());
		return result;
	}

	public IGoodsDiscountTicketDetailManager getGoodsDiscountTicketDetailManager() {
		return goodsDiscountTicketDetailManager;
	}

	public void setGoodsDiscountTicketDetailManager(
			IGoodsDiscountTicketDetailManager goodsDiscountTicketDetailManager) {
		this.goodsDiscountTicketDetailManager = goodsDiscountTicketDetailManager;
	}

}
