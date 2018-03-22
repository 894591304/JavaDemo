package com.enation.app.ext.component.membershop;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class MemberShopComponent implements IComponent{

	@Override
	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/membershop/membershop_install.xml", "es_");
		System.out.println("membershop组件已安装");		
	}

	@Override
	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/membershop/membershop_uninstall.xml", "es_");
		System.out.println("membershop组件已卸载");			
	}

}
