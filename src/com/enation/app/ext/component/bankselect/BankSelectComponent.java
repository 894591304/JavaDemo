package com.enation.app.ext.component.bankselect;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class BankSelectComponent implements IComponent{

	@Override
	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/bankselect/bankselect_install.xml", "es_");
		System.out.println("bankselect组件已安装");	
	}

	@Override
	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/bankselect/bankselect_uninstall.xml", "es_");
		System.out.println("bankselect组件已卸载");	
	}

}
