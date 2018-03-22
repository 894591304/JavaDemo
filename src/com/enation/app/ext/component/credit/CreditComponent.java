package com.enation.app.ext.component.credit;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class CreditComponent implements IComponent{

	
	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/credit/credit_install.xml", "es_");
		System.out.println("credit组件已安装");		
	}

	
	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/credit/credit_uninstall.xml", "es_");
		System.out.println("credit组件已卸载");		
	}

}
