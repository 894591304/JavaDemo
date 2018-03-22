package com.enation.app.ext.component.zsydetail;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class ZsyDetailComponent implements IComponent{

	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/zsydetail/ZsyDetail_install.xml", "es_");
		System.out.println("ZsyDetail组件已安装");		
	}

	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/zsydetail/ZsyDetail_uninstall.xml", "es_");
		System.out.println("ZsyDetail组件已卸载");
	}

}
