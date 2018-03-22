package com.enation.app.ext.component.proxyMemberBankInfo;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class ProxyMemberBankInfoComponent implements IComponent{

	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/proxyMemberBankInfo/ProxyMemberBankInfo_install.xml", "es_");
		System.out.println("ProxyMemberBankInfo组件已安装");		
	}

	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/proxyMemberBankInfo/ProxyMemberBankInfo_uninstall.xml", "es_");
		System.out.println("ProxyMemberBankInfo组件已卸载");
	}

}
