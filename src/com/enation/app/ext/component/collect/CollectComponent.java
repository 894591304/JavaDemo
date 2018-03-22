package com.enation.app.ext.component.collect;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class CollectComponent implements IComponent{


	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/collect/collect_install.xml", "es_");
		System.out.println("collect组件已安装");		
	}


	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/collect/collect_uninstall.xml", "es_");
		System.out.println("collect组件已安装");
	}

}
