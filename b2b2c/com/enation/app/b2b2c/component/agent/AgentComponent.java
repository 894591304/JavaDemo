package com.enation.app.b2b2c.component.agent;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.agent.service.IAgentManager;
import com.enation.app.base.core.service.auth.IAdminUserManager;
import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class AgentComponent implements IComponent {
	private IAdminUserManager adminUserManger;
	private IAgentManager agentManager;

	@Override
	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/b2b2c/component/agent/agent_install.xml", "es_");
		System.out.println("AgentComponent组件已安装");		
	}

	@Override
	public void unInstall() {
		// 删除商品企业字段之前，需要将所有的商品企业删除掉
//		agentManager.delteAllAgent();
		DBSolutionFactory.dbImport("file:com/enation/app/b2b2c/component/agent/agent_uninstall.xml", "es_");
		System.out.println("AgentComponent组件已卸载");		
	}

	public IAdminUserManager getAdminUserManger() {
		return adminUserManger;
	}

	public void setAdminUserManger(IAdminUserManager adminUserManger) {
		this.adminUserManger = adminUserManger;
	}

	public IAgentManager getAgentManager() {
		return agentManager;
	}

	public void setAgentManager(IAgentManager agentManager) {
		this.agentManager = agentManager;
	}
}
