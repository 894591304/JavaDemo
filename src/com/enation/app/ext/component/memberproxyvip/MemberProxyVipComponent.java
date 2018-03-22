package com.enation.app.ext.component.memberproxyvip;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class MemberProxyVipComponent implements IComponent{

	@Override
	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/memberproxyvip/memberproxyvip_install.xml", "es_");
		System.out.println("memberproxyvip组件已安装");
	}

	@Override
	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/memberproxyvip/memberproxyvip_uninstall.xml", "es_");
		System.out.println("memberproxyvip组件已卸载");
	}

}
