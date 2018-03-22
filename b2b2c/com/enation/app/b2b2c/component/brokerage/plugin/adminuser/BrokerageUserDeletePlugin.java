package com.enation.app.b2b2c.component.brokerage.plugin.adminuser;

import com.enation.app.b2b2c.component.brokerage.service.IBrokerageManager;
import com.enation.app.base.core.plugin.user.IAdminUserDeleteEvent;
import com.enation.framework.plugin.AutoRegisterPlugin;

public class BrokerageUserDeletePlugin extends AutoRegisterPlugin implements IAdminUserDeleteEvent {
	
	private IBrokerageManager brokerageManager;
	
	@Override
	public void onDelete(int userid) {
		// 删除管理员的同时尝试删除商品企业企业
		this.brokerageManager.delete(userid);
	}

	public IBrokerageManager getBrokerageManager() {
		return brokerageManager;
	}

	public void setBrokerageManager(IBrokerageManager brokerageManager) {
		this.brokerageManager = brokerageManager;
	}

}
