package com.enation.app.ext.component.goodsagent.plugin;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.enation.app.ext.component.goodsagent.model.GoodsAgent;
import com.enation.app.ext.component.goodsagent.service.IGoodsAgentManager;
import com.enation.app.ext.component.proxycount.service.IProxyCountManager;
import com.enation.app.ext.component.proxycount.service.impl.ProxyCountManager;
import com.enation.app.shop.core.plugin.goods.AbstractGoodsPlugin;
import com.enation.app.shop.core.plugin.goods.IGoodsAfterAddEvent;
import com.enation.app.shop.core.plugin.goods.IGoodsAfterEditEvent;

@Component
public class GoodsAgentPricePlugin  extends AbstractGoodsPlugin implements IGoodsAfterAddEvent, IGoodsAfterEditEvent{

	private IGoodsAgentManager goodsAgentManager;
	private IProxyCountManager ProxyCountManager;
	
	@Override
	public String getAddHtml(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onBeforeGoodsAdd(Map arg0, HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getEditHtml(Map arg0, HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onBeforeGoodsEdit(Map arg0, HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAfterGoodsEdit(Map goods, HttpServletRequest request) {
		saveEditGoodsAgent(goods,request);
	}

	@Override
	public void onAfterGoodsAdd(Map goods, HttpServletRequest request) throws RuntimeException {
		saveEditGoodsAgent(goods,request);
	}
	
	private void saveEditGoodsAgent(Map goods, HttpServletRequest request){
		String goodsId = goods.get("goods_id").toString();
		String mktPrice = goods.get("mktprice").toString();
		String price = request.getParameter("price");
		String goldPrice = request.getParameter("goldPrice");
		String platinumPrice = request.getParameter("platinumPrice");
		String blackPrice = request.getParameter("blackPrice");
		String cost = request.getParameter("cost");
		String defaultPrice = request.getParameter("defaultPrice");
		String timeOption = request.getParameter("timeOption");
		String ticketOption = request.getParameter("ticketOption");
		String ticketCaption = request.getParameter("ticketCaption");
		GoodsAgent goodsAgent = this.goodsAgentManager.get(Integer.parseInt(goodsId));
		boolean isAdd = true;
		if(goodsAgent==null){
			goodsAgent = new GoodsAgent();
			goodsAgent.setGoodsId(Integer.parseInt(goodsId));
		}else{
			isAdd = false;
		}
		goodsAgent.setMktPrice(Float.parseFloat(mktPrice));
		goodsAgent.setPrice(Float.parseFloat(price));
		goodsAgent.setGoldPrice(Float.parseFloat(goldPrice));
		goodsAgent.setPlatinumPrice(Float.parseFloat(platinumPrice));
		goodsAgent.setBlackPrice(Float.parseFloat(blackPrice));
		goodsAgent.setCost(Float.parseFloat(cost));
		goodsAgent.setDefaultPrice(Float.parseFloat(defaultPrice));
		goodsAgent.setTimeOption(timeOption);
		goodsAgent.setTicketOption(ticketOption);;
		goodsAgent.setTicketCaption(ticketCaption);;
		if(isAdd){
			this.goodsAgentManager.add(goodsAgent);
			this.ProxyCountManager.addNew(Integer.parseInt(goodsId));
		}else{
			this.goodsAgentManager.edit(goodsAgent);
		}
	}

	public IGoodsAgentManager getGoodsAgentManager() {
		return goodsAgentManager;
	}

	public void setGoodsAgentManager(IGoodsAgentManager goodsAgentManager) {
		this.goodsAgentManager = goodsAgentManager;
	}

}
