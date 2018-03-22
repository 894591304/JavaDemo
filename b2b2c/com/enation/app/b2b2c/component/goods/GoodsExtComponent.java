package com.enation.app.b2b2c.component.goods;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;

@Component
public class GoodsExtComponent implements IComponent {

	@Override
	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/b2b2c/component/goods/goods_install.xml", "es_");
		System.out.println("GoodsExtComponent组件已安装");		
	}

	@Override
	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/b2b2c/component/goods/goods_uninstall.xml", "es_");
		System.out.println("GoodsExtComponent组件已卸载");		
	}
}
