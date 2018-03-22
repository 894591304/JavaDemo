package com.enation.app.ext.component.ticketgive;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class TicketGiveComponent implements IComponent{

	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/ticketgive/ticketgive_install.xml", "es_");
		System.out.println("ticketgive组件已安装");	
	}

	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/ticketgive/ticketgive_uninstall.xml", "es_");
		System.out.println("ticketgive组件已卸载");
	}

}
