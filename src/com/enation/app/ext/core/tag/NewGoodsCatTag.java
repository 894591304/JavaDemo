package com.enation.app.ext.core.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.app.ext.core.service.INewGoodsCatManager;
import com.enation.app.shop.core.model.Cat;
import com.enation.app.shop.core.model.Goods;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.sun.mail.handlers.image_gif;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class NewGoodsCatTag extends BaseFreeMarkerTag{

	private INewGoodsCatManager newGoodsCatManager;
	private IGoodsProxyManager goodsProxyManager;
	private IProxyManager proxyManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Map result = new HashMap();
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[NewGoodsCatTag]");
		}
		List<Cat> cList = this.newGoodsCatManager.getAll();
		int count = cList.size();
		for(int i=count-1;i>=0;i--){
			Cat cat = cList.get(i);
			List<Goods> gList = this.goodsProxyManager.getByCatid(cat.getCat_id());
			int gcount = gList.size();
			int num = 0;
			for(int j=0;j<gcount;j++){
				if(this.proxyManager.checkProxy(gList.get(j).getGoods_id(), member.getMember_id())!=1){
					num++;
				}
			}
			if(num!=0){
				cList.get(i).setGoods_count(1);
				cList = showCat(cList, i);
			}else if(cList.get(i).getGoods_count()!=0){
				cList = showCat(cList, i);
			}else{
				cList.remove(i);
			}
		}
		int newCount = cList.size();
		List catList1 = new ArrayList();
		List catList2 = new ArrayList();
		for(int k=0;k<newCount;k++){
			Map catMap = new HashMap();
			Cat cat1 = cList.get(k);
			catMap.put("parentid", String.valueOf(cat1.getParent_id()));
			catMap.put("catname", cat1.getName());
			catMap.put("catid", String.valueOf(cat1.getCat_id()));
			if(cat1.getParent_id()==0){
				catList1.add(catMap);
			}else{
				catList2.add(catMap);
			}
		}
		result.put("catlist1",catList1);
		result.put("catlist2",catList2);
		return result;
	}
	
	public List<Cat> showCat(List<Cat> cList,int i){
		Cat cat = cList.get(i);
		int count = cList.size();
		if(cat.getParent_id()==0){
			return cList;
		}
		for(int j=count-1;j>=0;j--){
			int a = cList.get(j).getCat_id();
			int b = cat.getParent_id();
			if(a==b){
				cList.get(j).setGoods_count(1);
				return cList;
			}
		}
		return null;
	}

	public INewGoodsCatManager getNewGoodsCatManager() {
		return newGoodsCatManager;
	}

	public void setNewGoodsCatManager(INewGoodsCatManager newGoodsCatManager) {
		this.newGoodsCatManager = newGoodsCatManager;
	}

	public IGoodsProxyManager getGoodsProxyManager() {
		return goodsProxyManager;
	}

	public void setGoodsProxyManager(IGoodsProxyManager goodsProxyManager) {
		this.goodsProxyManager = goodsProxyManager;
	}


	public IProxyManager getProxyManager() {
		return proxyManager;
	}


	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

}
