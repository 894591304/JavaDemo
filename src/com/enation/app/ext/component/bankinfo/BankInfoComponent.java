package com.enation.app.ext.component.bankinfo;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class BankInfoComponent implements IComponent{

	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/bankinfo/bankinfo_install.xml", "es_");
		System.out.println("bankinfo组件已安装");	
	}

	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/bankinfo/bankinfo_uninstall.xml", "es_");
		System.out.println("bankinfo组件已卸载");	
	}

}
