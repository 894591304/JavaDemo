package com.enation.app.b2b2c.component.agent.plugin.goods;

import com.enation.app.b2b2c.component.agent.service.IAgentManager;
import com.enation.app.b2b2c.component.agent.service.impl.AgentManager;
import com.enation.app.base.core.service.auth.IAdminUserManager;
import com.enation.app.shop.core.plugin.goods.IGoodsSearchFilter;
import com.enation.eop.resource.model.AdminUser;
import com.enation.framework.plugin.AutoRegisterPlugin;

public class GoodsAgentSearchPlugin extends AutoRegisterPlugin implements IGoodsSearchFilter {

	private IAdminUserManager adminUserManager;
	private IAgentManager agentManager;
	
	@Override
	public void filter(StringBuffer sql) {
		// TODO Auto-generated method stub
		AdminUser currentUser = this.adminUserManager.getCurrentUser();
		if(this.agentManager.checkAgentUser(currentUser.getUserid())>0){
			sql.append("and g.agentid='"+currentUser.getUserid()+"'");
		}
	}

	@Override
	public String getFrom() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getSelector() {
		// TODO Auto-generated method stub
		return "";
	}

	public IAdminUserManager getAdminUserManager() {
		return adminUserManager;
	}

	public void setAdminUserManager(IAdminUserManager adminUserManager) {
		this.adminUserManager = adminUserManager;
	}

	public IAgentManager getAgentManager() {
		return agentManager;
	}

	public void setAgentManager(IAgentManager agentManager) {
		this.agentManager = agentManager;
	}

}
