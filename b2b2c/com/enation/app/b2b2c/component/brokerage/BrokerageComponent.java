package com.enation.app.b2b2c.component.brokerage;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.brokerage.service.IBrokerageManager;
import com.enation.app.base.core.service.auth.IAdminUserManager;
import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class BrokerageComponent implements IComponent {
	private IAdminUserManager adminUserManger;
	private IBrokerageManager brokerageManager;

	@Override
	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/b2b2c/component/brokerage/brokerage_install.xml", "es_");
		System.out.println("BrokerageComponent组件已安装");		
	}

	@Override
	public void unInstall() {
		// 删除商品企业字段之前，需要将所有的商品企业删除掉
//		brokerageManager.delteAllBrokerage();
		DBSolutionFactory.dbImport("file:com/enation/app/b2b2c/component/brokerage/brokerage_uninstall.xml", "es_");
		System.out.println("BrokerageComponent组件已卸载");		
	}

	public IAdminUserManager getAdminUserManger() {
		return adminUserManger;
	}

	public void setAdminUserManger(IAdminUserManager adminUserManger) {
		this.adminUserManger = adminUserManger;
	}

	public IBrokerageManager getBrokerageManager() {
		return brokerageManager;
	}

	public void setBrokerageManager(IBrokerageManager brokerageManager) {
		this.brokerageManager = brokerageManager;
	}
}
