package com.enation.app.ext.component.useraccount;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class UserAccountComponent implements IComponent{

	
	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/useraccount/useraccount_install.xml", "es_");
		System.out.println("useraccount组件已安装");		
	}

	
	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/useraccount/useraccount_uninstall.xml", "es_");
		System.out.println("useraccount组件已卸载");
	}

}
