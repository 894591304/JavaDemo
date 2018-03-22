package com.enation.app.b2b2c.component.creditmember;

import org.springframework.stereotype.Component;
import com.enation.framework.component.IComponent;

@Component
public class CreditMemberComponent implements IComponent {

	@Override
	public void install() {
//		DBSolutionFactory.dbImport("file:com/enation/app/b2b2c/component/creditmember/creditmember_install.xml", "es_");
		System.out.println("CreditMemberComponent组件已安装");		
	}

	@Override
	public void unInstall() {
//		DBSolutionFactory.dbImport("file:com/enation/app/b2b2c/component/creditmember/creditmember_uninstall.xml", "es_");
		System.out.println("CreditMemberComponent组件已卸载");		
	}
}