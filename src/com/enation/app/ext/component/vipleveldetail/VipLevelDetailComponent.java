package com.enation.app.ext.component.vipleveldetail;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class VipLevelDetailComponent implements IComponent{

	@Override
	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/vipleveldetail/vipleveldetail_install.xml", "es_");
		System.out.println("vipleveldetail组件已安装");
	}

	@Override
	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/vipleveldetail/vipleveldetail_uninstall.xml", "es_");
		System.out.println("vipleveldetail组件已卸载");
	}

}
