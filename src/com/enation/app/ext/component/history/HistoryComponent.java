package com.enation.app.ext.component.history;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class HistoryComponent implements IComponent{

	
	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/history/history_install.xml", "es_");
		System.out.println("history组件已安装");			
	}

	
	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/history/history_uninstall.xml", "es_");
		System.out.println("history组件已卸载");			
	}

}
