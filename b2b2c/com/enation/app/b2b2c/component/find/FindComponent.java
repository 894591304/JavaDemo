package com.enation.app.b2b2c.component.find;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class FindComponent implements IComponent {

	@Override
	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/b2b2c/component/find/find_install.xml", "es_");
		System.out.println("FindComponent组件已安装");		
	}

	@Override
	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/b2b2c/component/find/find_uninstall.xml", "es_");
		System.out.println("FindComponent组件已卸载");		
	}
}
