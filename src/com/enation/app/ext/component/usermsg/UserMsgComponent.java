package com.enation.app.ext.component.usermsg;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component 
public class UserMsgComponent implements IComponent{

	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/usermsg/usermsg_install.xml", "es_");
		System.out.println("UserMsg组件已安装");
	}

	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/usermsg/usermsg_uninstall.xml", "es_");
		System.out.println("UserMsg组件已卸载");
	}

}
