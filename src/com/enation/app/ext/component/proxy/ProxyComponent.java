package com.enation.app.ext.component.proxy;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class ProxyComponent implements IComponent{

	@Override
	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/proxy/proxy_install.xml", "es_");
		System.out.println("proxy组件已安装");
	}

	@Override
	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/proxy/proxy_uninstall.xml", "es_");
		System.out.println("proxy组件已卸载");
	}

}
