package com.enation.app.ext.component.goodsagent.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.app.ext.core.service.INewGoodsCatManager;
import com.enation.app.shop.core.model.Cat;
import com.enation.app.shop.core.model.Goods;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class MyAgentCatListTag extends BaseFreeMarkerTag{

	private IProxyManager proxyManager;
	private IGoodsProxyManager goodsProxyManager;
	private INewGoodsCatManager newGoodsCatManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[MyAgentCatListTag]");
		}
		Map result = new HashMap();
		List<Proxy> proxyList= this.proxyManager.getAllByMemberid(member.getMember_id());
		int total = proxyList.size();
		int[] catlist1 = new int[200];
		catlist1 = moren(catlist1);
		int[] catlist2 = new int[200];
		catlist2 = moren(catlist2);
		for(int i=0;i<total;i++){
			Proxy proxy = proxyList.get(i);
			Goods goods = this.goodsProxyManager.getGoods(proxy.getGoodsId());
			int catid = goods.getCat_id();
			Cat cat = this.newGoodsCatManager.getByCatid(catid);			
			catlist1 = checkAndSet(catlist1, cat.getParent_id());
			catlist2 = checkAndSet(catlist2, catid);
		}
		int cat1total = catlist1.length;
		List cat1list = new ArrayList();
		for(int c1=0;c1<cat1total;c1++){
			if(catlist1[c1]==-2){break;}
			Map catlist = new HashMap();
			Cat cat = this.newGoodsCatManager.getByCatid(catlist1[c1]);
			catlist.put("catid", String.valueOf(cat.getCat_id()));
			catlist.put("catname", cat.getName());
			catlist.put("pid",String.valueOf(cat.getParent_id()));
			cat1list.add(catlist);
		}		
		int cat2total = catlist2.length;
		List cat2list = new ArrayList();
		for(int c2=0;c2<cat2total;c2++){
			if(catlist2[c2]==-2){break;}
			Map catlist = new HashMap();
			Cat cat = this.newGoodsCatManager.getByCatid(catlist2[c2]);
			catlist.put("catid", String.valueOf(cat.getCat_id()));
			catlist.put("catname", cat.getName());
			catlist.put("pid",String.valueOf(cat.getParent_id()));
			cat2list.add(catlist);
		}
		result.put("cat1list",cat1list);
		result.put("cat2list",cat2list);		
		return result;
	}
	
	public int[] checkAndSet(int[] catlist,int cat){
		int count = catlist.length;
		for(int i=0;i<count;i++){
			if(catlist[i]==cat){
				return catlist;
			}else if(catlist[i]==-2){
				catlist[i]=cat;
				return catlist;
			}
		}
		return catlist;
	}

	public int[] moren(int[] list){
		int count = list.length;
		for(int i=0;i<count;i++){
			list[i]=-2;
		}
		return list;
	}
	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

	public IGoodsProxyManager getGoodsProxyManager() {
		return goodsProxyManager;
	}

	public void setGoodsProxyManager(IGoodsProxyManager goodsProxyManager) {
		this.goodsProxyManager = goodsProxyManager;
	}

	public INewGoodsCatManager getNewGoodsCatManager() {
		return newGoodsCatManager;
	}

	public void setNewGoodsCatManager(INewGoodsCatManager newGoodsCatManager) {
		this.newGoodsCatManager = newGoodsCatManager;
	}

}
