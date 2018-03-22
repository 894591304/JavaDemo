package com.enation.app.b2b2c.component.agent.plugin.goods;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.agent.model.AgentUser;
import com.enation.app.b2b2c.component.agent.service.IAgentManager;
import com.enation.app.b2b2c.component.agent.service.IGoodsExtManager;
import com.enation.app.base.core.service.auth.IAdminUserManager;
import com.enation.app.shop.core.plugin.goods.AbstractGoodsPlugin;
import com.enation.app.shop.core.plugin.goods.IGoodsAfterAddEvent;
import com.enation.app.shop.core.plugin.goods.IGoodsAfterEditEvent;
import com.enation.app.shop.core.plugin.goods.IGoodsTabShowEvent;
import com.enation.eop.processor.core.freemarker.FreeMarkerPaser;
import com.enation.eop.resource.model.AdminUser;

@Component
public class GoodAgentPlugin extends AbstractGoodsPlugin
		implements IGoodsTabShowEvent, IGoodsAfterAddEvent, IGoodsAfterEditEvent {

	private IAgentManager agentManager;
	private IAdminUserManager adminUserManager;
	private IGoodsExtManager goodsExtManager;

	public void addTabs() {
	}

	public void registerPages() {
	}

	public String getAddHtml(HttpServletRequest request) {
		FreeMarkerPaser freeMarkerPaser = FreeMarkerPaser.getInstance();
		List<AgentUser> agentUserList = this.agentManager.getAgentList();
		freeMarkerPaser.setPageName("goods_agent");
		freeMarkerPaser.putData("agentUserList", agentUserList);
		AdminUser currentUser = this.adminUserManager.getCurrentUser();
		freeMarkerPaser.putData("isAgent", this.agentManager.checkAgentUser(currentUser.getUserid()));
		freeMarkerPaser.putData("currentUserid", currentUser.getUserid());
		return freeMarkerPaser.proessPageContent();
	}

	public String getEditHtml(Map goods, HttpServletRequest request) {
		FreeMarkerPaser freeMarkerPaser = new FreeMarkerPaser(getClass());
		List<AgentUser> agentUserList = this.agentManager.getAgentList();
		freeMarkerPaser.setPageName("goods_agent");
		freeMarkerPaser.putData("agentUserList", agentUserList);
		AdminUser currentUser = this.adminUserManager.getCurrentUser();
		freeMarkerPaser.putData("isAgent", this.agentManager.checkAgentUser(currentUser.getUserid()));
		freeMarkerPaser.putData("currentUserid", currentUser.getUserid());
		int goods_id = Integer.parseInt(goods.get("goods_id").toString()); 
		List<Map> agentList = this.goodsExtManager.getGoodsAgentId(goods_id);
		if (agentList != null && agentList.get(0).get("agentid") != null) {
			freeMarkerPaser.putData("agentid", agentList.get(0).get("agentid"));
		}
		return freeMarkerPaser.proessPageContent();
	}

	@Override
	public void onBeforeGoodsAdd(Map goods, HttpServletRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBeforeGoodsEdit(Map goods, HttpServletRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAfterGoodsEdit(Map goods, HttpServletRequest request) {
		updateGoodsAgent(goods, request);
	}

	@Override
	public void onAfterGoodsAdd(Map goods, HttpServletRequest request) throws RuntimeException {
		updateGoodsAgent(goods, request);
	}

	private void updateGoodsAgent(Map goods, HttpServletRequest request) {
		AdminUser currentUser = this.adminUserManager.getCurrentUser();
		int count = this.agentManager.checkAgentUser(currentUser.getUserid());
		int goodsid = Integer.parseInt(goods.get("goods_id").toString());
		if (count == 1) {
			this.goodsExtManager.updateGoodsAgent(goodsid, currentUser.getUserid());
		} else {
			String agentid = request.getParameter("agent_id");
			if (agentid != null) {
				this.goodsExtManager.updateGoodsAgent(goodsid, Integer.parseInt(agentid));
			}
		}
		String thirdPlatform = request.getParameter("thirdPlatform");
		this.goodsExtManager.updateGoodsThirdPlatform(goodsid, thirdPlatform);
	}

	public IGoodsExtManager getGoodsExtManager() {
		return goodsExtManager;
	}

	public void setGoodsExtManager(IGoodsExtManager goodsExtManager) {
		this.goodsExtManager = goodsExtManager;
	}

	public String getAuthor() {
		return "yang.xiaolong";
	}

	public String getId() {
		return "goodsagent";
	}

	public String getName() {
		return "商品所属企业插件";
	}

	public String getType() {
		return "";
	}

	public String getVersion() {
		return "1.0";
	}

	public void perform(Object... params) {
	}

	public String getTabName() {
		return "所属企业";
	}

	public int getOrder() {
		return 14;
	}

	public IAgentManager getAgentManager() {
		return agentManager;
	}

	public void setAgentManager(IAgentManager agentManager) {
		this.agentManager = agentManager;
	}

	public IAdminUserManager getAdminUserManager() {
		return adminUserManager;
	}

	public void setAdminUserManager(IAdminUserManager adminUserManager) {
		this.adminUserManager = adminUserManager;
	}

}