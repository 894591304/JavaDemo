package com.enation.app.ext.component.follow;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class FollowComponent implements IComponent{

	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/follow/follow_install.xml", "es_");
		System.out.println("用户关注组件已安装");
	}

	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/follow/follow_uninstall.xml", "es_");
		System.out.println("用户关注组件已卸载");
	}

}
