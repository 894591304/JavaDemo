package com.enation.app.ext.component.goodsagent;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class GoodsAgentComponent implements IComponent{

	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/goodsagent/goodsagent_install.xml", "es_");
		System.out.println("商品代理表相关组件已安装");
	}

	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/ext/component/goodsagent/goodsagent_uninstall.xml", "es_");
		System.out.println("商品代理表相关组件已卸载");
	}

}
