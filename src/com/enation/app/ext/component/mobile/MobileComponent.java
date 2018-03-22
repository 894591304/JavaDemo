package com.enation.app.ext.component.mobile;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class MobileComponent implements IComponent{

	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/mobile/mobile_install.xml", "es_");
		System.out.println("mobile组件已安装");
		}

	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/mobile/mobile_uninstall.xml", "es_");
		System.out.println("mobile组件已卸载");		
	}

}
