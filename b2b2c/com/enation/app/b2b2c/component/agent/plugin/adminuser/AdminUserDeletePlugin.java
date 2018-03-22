package com.enation.app.b2b2c.component.agent.plugin.adminuser;

import com.enation.app.b2b2c.component.agent.service.IAgentManager;
import com.enation.app.base.core.plugin.user.IAdminUserDeleteEvent;
import com.enation.framework.plugin.AutoRegisterPlugin;

public class AdminUserDeletePlugin extends AutoRegisterPlugin implements IAdminUserDeleteEvent {
	
	private IAgentManager agentManager;
	
	@Override
	public void onDelete(int userid) {
		// 删除管理员的同时尝试删除商品企业企业
		this.agentManager.delete(userid);
	}

	public IAgentManager getAgentManager() {
		return agentManager;
	}

	public void setAgentManager(IAgentManager agentManager) {
		this.agentManager = agentManager;
	}

}
