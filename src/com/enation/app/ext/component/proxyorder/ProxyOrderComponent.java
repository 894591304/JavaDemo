package com.enation.app.ext.component.proxyorder;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class ProxyOrderComponent implements IComponent{

	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/proxyorder/proxyorder_install.xml", "es_");
		System.out.println("proxyorder组件已安装");		
	}

	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/proxyorder/proxyorder_uninstall.xml", "es_");
		System.out.println("proxyorder组件已卸载");
	}

}
