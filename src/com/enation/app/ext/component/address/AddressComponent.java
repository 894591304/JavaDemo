package com.enation.app.ext.component.address;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class AddressComponent implements IComponent{


	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/address/address_install.xml", "es_");
		System.out.println("address组件已安装");				
	}


	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/address/address_uninstall.xml", "es_");
		System.out.println("address组件已卸载");	
	}

}
