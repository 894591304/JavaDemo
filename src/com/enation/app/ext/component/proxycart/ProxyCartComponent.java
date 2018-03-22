package com.enation.app.ext.component.proxycart;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class ProxyCartComponent implements IComponent{

	
	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/proxycart/ProxyCart_install.xml", "es_");
		System.out.println("proxycart组件已安装");		
	}

	
	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/proxycart/ProxyCart_uninstall.xml", "es_");
		System.out.println("proxycart组件已卸载");
	}

}
