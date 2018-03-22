package com.enation.app.b2b2c.component.order;

import org.springframework.stereotype.Component;

import com.enation.framework.component.IComponent;

@Component
public class OrderExtComponent implements IComponent {
	@Override
	public void install() {
		System.out.println("OrderExtComponent组件已安装");		
	}

	@Override
	public void unInstall() {
		System.out.println("OrderExtComponent组件已卸载");		
	}
}
