package com.enation.app.ext.component.proxycount;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class ProxyCountComponent implements IComponent{

	
	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/proxycount/proxycount_install.xml", "es_");
		System.out.println("proxycount组件已安装");		
	}

	
	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/proxycart/proxycount_uninstall.xml", "es_");
		System.out.println("proxycount组件已卸载");
	}

}
