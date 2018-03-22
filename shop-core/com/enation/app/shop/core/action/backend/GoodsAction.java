/*     */ package com.enation.app.shop.core.action.backend;
/*     */ 
/*     */ import com.enation.app.base.core.service.auth.IAdminUserManager;
/*     */ import com.enation.app.base.core.service.auth.IPermissionManager;
/*     */ import com.enation.app.shop.core.model.Goods;
/*     */ import com.enation.app.shop.core.model.Tag;
/*     */ import com.enation.app.shop.core.model.support.GoodsEditDTO;
/*     */ import com.enation.app.shop.core.plugin.goods.GoodsPluginBundle;
/*     */ import com.enation.app.shop.core.service.IBrandManager;
/*     */ import com.enation.app.shop.core.service.ICartManager;
/*     */ import com.enation.app.shop.core.service.IGoodsCatManager;
/*     */ import com.enation.app.shop.core.service.IGoodsManager;
/*     */ import com.enation.app.shop.core.service.IOrderManager;
/*     */ import com.enation.app.shop.core.service.IProductManager;
/*     */ import com.enation.app.shop.core.service.ITagManager;
/*     */ import com.enation.eop.sdk.context.EopSetting;
/*     */ import com.enation.framework.action.WWAction;
/*     */ import com.enation.framework.database.Page;
/*     */ import com.enation.framework.util.JsonMessageUtil;
/*     */ import com.enation.framework.util.StringUtil;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.sf.json.JSONArray;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GoodsAction
/*     */   extends WWAction
/*     */ {
/*     */   protected String name;
/*     */   protected String sn;
/*     */   protected String order;
/*     */   private Integer catid;
/*     */   protected Integer[] goods_id;
/*     */   protected List brandList;
/*     */   protected Integer brand_id;
/*     */   protected Integer is_market;
/*     */   protected Goods goods;
/*     */   protected Map goodsView;
/*     */   protected Integer goodsId;
/*     */   protected List catList;
/*     */   protected IGoodsCatManager goodsCatManager;
/*     */   protected IBrandManager brandManager;
/*     */   protected IGoodsManager goodsManager;
/*     */   private IProductManager productManager;
/*     */   private ICartManager cartManager;
/*     */   private IOrderManager orderManager;
/*     */   protected Boolean is_edit;
/*     */   protected String actionName;
/*     */   protected Integer market_enable;
/*     */   private Integer[] tagids;
/*     */   private GoodsPluginBundle goodsPluginBundle;
/*     */   private IPermissionManager permissionManager;
/*     */   private IAdminUserManager adminUserManager;
/*     */   private ITagManager tagManager;
/*     */   protected Map<Integer, String> pluginTabs;
/*     */   protected Map<Integer, String> pluginHtmls;
/*     */   private List<Tag> tagList;
/*     */   private int depotid;
/*     */   private String is_other;
/*     */   private String goodslistid;
/*     */   private Integer stype;
/*     */   private String keyword;
/*     */   private Map goodsMap;
/*  74 */   private String optype = "no";
/*     */   
/*     */   private List<String> tagHtmlList;
/*     */   
/*     */   public GoodsAction() {}
/*     */   
/*     */   public String searchGoods()
/*     */   {
/*  82 */     Map goodsMap = new HashMap();
/*  83 */     if (this.stype != null) {
/*  84 */       if (this.stype.intValue() == 0) {
/*  85 */         goodsMap.put("stype", this.stype);
/*  86 */         goodsMap.put("keyword", this.keyword);
/*  87 */       } else if (this.stype.intValue() == 1) {
/*  88 */         goodsMap.put("stype", this.stype);
/*  89 */         goodsMap.put("name", this.name);
/*  90 */         goodsMap.put("sn", this.sn);
/*  91 */         goodsMap.put("catid", this.catid);
/*     */       }
/*     */     }
/*  94 */     this.webpage = this.goodsManager.searchGoods(goodsMap, getPage(), getPageSize(), null, getSort(), getOrder());
/*  95 */     String s = JSONArray.fromObject(this.webpage.getResult()).toString();
/*  96 */     this.json = s;
/*  97 */     return "json_message";
/*     */   }
/*     */   
/*     */   public String list()
/*     */   {
/* 102 */     this.goodsMap = new HashMap();
/* 103 */     if (this.name != null) {
/* 104 */       String encoding = EopSetting.ENCODING;
/* 105 */       if (!StringUtil.isEmpty(encoding)) {
/* 106 */         this.name = StringUtil.to(this.name, encoding);
/*     */       }
/*     */     }
/*     */     
/* 110 */     this.goodsMap.put("brand_id", this.brand_id);
/* 111 */     this.goodsMap.put("is_market", this.market_enable);
/* 112 */     this.goodsMap.put("catid", this.catid);
/* 113 */     this.goodsMap.put("name", this.name);
/* 114 */     this.goodsMap.put("sn", this.sn);
/* 115 */     this.goodsMap.put("market_enable", this.market_enable);
/* 116 */     this.goodsMap.put("tagids", this.tagids);
/* 117 */     this.goodsMap.put("order", this.order);
/*     */     
/* 119 */     this.brandList = this.brandManager.list();
/* 120 */     this.tagList = this.tagManager.list();
/*     */     
/* 122 */     this.webpage = this.goodsManager.searchGoods(this.goodsMap, getPage(), getPageSize(), this.is_other, "goods_id", "desc");
/* 123 */     this.is_edit = (this.is_edit == null ? Boolean.FALSE : Boolean.TRUE);
/* 124 */     if (!this.is_edit.booleanValue()) {
/* 125 */       return "list";
/*     */     }
/* 127 */     return "edit_list";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String listJson()
/*     */   {
/* 138 */     if (this.name != null) {
/* 139 */       String encoding = EopSetting.ENCODING;
/* 140 */       if (!StringUtil.isEmpty(encoding)) {
/* 141 */         this.name = StringUtil.to(this.name, encoding);
/*     */       }
/*     */     }
/*     */     
/* 145 */     this.goodsMap = new HashMap();
			  this.goodsMap.put("market_enable", this.market_enable);
/* 146 */     if (this.stype != null) {
/* 147 */       if (this.stype.intValue() == 0) {
/* 148 */         this.goodsMap.put("stype", this.stype);
/* 149 */         this.goodsMap.put("keyword", this.keyword);
/* 150 */       } else if (this.stype.intValue() == 1) {
/* 151 */         this.goodsMap.put("stype", this.stype);
/* 152 */         this.goodsMap.put("name", this.name);
/* 153 */         this.goodsMap.put("sn", this.sn);
/* 154 */         this.goodsMap.put("catid", this.catid);
/*     */       }
/*     */     }
/*     */     
/* 158 */     this.webpage = this.goodsManager.searchGoods(this.goodsMap, getPage(), getPageSize(), null, getSort(), getOrder());
/* 159 */     showGridJson(this.webpage);
/*     */     
/* 161 */     return "json_message";
/*     */   }
/*     */   
/*     */   public String batchEdit()
/*     */   {
/*     */     try {
/* 167 */       this.goodsManager.batchEdit();
/* 168 */       this.json = "{result:1}";
/*     */     } catch (RuntimeException e) {
/* 170 */       e.printStackTrace();
/* 171 */       this.json = "{result:0}";
/*     */     }
/*     */     
/* 174 */     return "json_message";
/*     */   }
/*     */   
/*     */   public String getCatTree() {
/* 178 */     this.catList = this.goodsCatManager.listAllChildren(Integer.valueOf(0));
/* 179 */     return "cat_tree";
/*     */   }
/*     */   
/*     */   public String trash_list()
/*     */   {
/* 184 */     this.webpage = this.goodsManager.pageTrash(this.name, this.sn, this.order, getPage(), getPageSize());
/*     */     
/* 186 */     return "trash_list";
/*     */   }
/*     */   
/* 189 */   public String trash_listJson() { this.webpage = this.goodsManager.pageTrash(this.name, this.sn, this.order, getPage(), getPageSize());
/*     */     
/* 191 */     showGridJson(this.webpage);
/* 192 */     return "json_message";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String delete()
/*     */   {
/* 202 */     if (EopSetting.IS_DEMO_SITE) {
/* 203 */       for (Integer gid : this.goods_id) {
/* 204 */         if (gid.intValue() <= 261) {
/* 205 */           showErrorJson("抱歉，当前为演示站点，以不能修改这些示例数据，请下载安装包在本地体验这些功能！");
/* 206 */           return "json_message";
/*     */         }
/*     */       }
/*     */     }
/*     */     try {
/* 211 */       if (this.goods_id != null)
/* 212 */         for (Integer goodsid : this.goods_id) {
/* 213 */           if (this.cartManager.checkGoodsInCart(goodsid)) {
/* 214 */             showErrorJson("删除失败，此商品已加入购物车");
/* 215 */             return "json_message";
/*     */           }
/* 217 */           if (this.orderManager.checkGoodsInOrder(goodsid.intValue())) {
/* 218 */             showErrorJson("删除失败，此商品已经下单");
/* 219 */             return "json_message";
/*     */           }
/*     */         }
/* 222 */       this.goodsManager.delete(this.goods_id);
/* 223 */       showSuccessJson("删除成功");
/*     */     } catch (RuntimeException e) {
/* 225 */       showErrorJson("删除失败");
/* 226 */       this.logger.error("商品删除失败", e);
/*     */     }
/* 228 */     return "json_message";
/*     */   }
/*     */   
/*     */   public String revert()
/*     */   {
/*     */     try {
/* 234 */       this.goodsManager.revert(this.goods_id);
/* 235 */       showSuccessJson("还原成功");
/*     */     } catch (RuntimeException e) {
/* 237 */       showErrorJson("还原失败");
/* 238 */       this.logger.error("商品还原失败", e);
/*     */     }
/* 240 */     return "json_message";
/*     */   }
/*     */   
/*     */   public String clean()
/*     */   {
/*     */     try {
/* 246 */       this.goodsManager.clean(this.goods_id);
/* 247 */       showSuccessJson("清除成功");
/*     */     } catch (RuntimeException e) {
/* 249 */       showErrorJson("清除失败");
/* 250 */       this.logger.error("商品清除失败", e);
/*     */     }
/* 252 */     return "json_message";
/*     */   }
/*     */   
/*     */   public String selector_list_ajax() {
/* 256 */     return "selector";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String add()
/*     */   {
/* 265 */     this.actionName = "goods!saveAdd.do";
/* 266 */     this.is_edit = Boolean.valueOf(false);
/*     */     
/* 268 */     this.pluginTabs = this.goodsPluginBundle.getTabList();
/* 269 */     this.pluginHtmls = this.goodsPluginBundle.onFillAddInputData();
/*     */     
/* 271 */     return "input";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String addBind()
/*     */   {
/* 278 */     this.actionName = "goods!saveBindAdd.do";
/* 279 */     return "bind_goods_input";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String edit()
/*     */   {
/* 286 */     this.actionName = "goods!saveEdit.do";
/* 287 */     this.is_edit = Boolean.valueOf(true);
/*     */     
/* 289 */     this.catList = this.goodsCatManager.listAllChildren(Integer.valueOf(0));
/* 290 */     GoodsEditDTO editDTO = this.goodsManager.getGoodsEditData(this.goodsId);
/* 291 */     this.goodsView = editDTO.getGoods();
/*     */     
/* 293 */     this.pluginTabs = this.goodsPluginBundle.getTabList();
/* 294 */     this.pluginHtmls = editDTO.getHtmlMap();
/*     */     
/* 296 */     return "input";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String saveAdd()
/*     */   {
/*     */     try
/*     */     {
/* 306 */       this.goodsManager.add(this.goods);
/* 307 */       JsonMessageUtil.getNumberJson("goodsid", this.goods.getGoods_id());
/* 308 */       showSuccessJson("商品添加成功");
/*     */     }
/*     */     catch (RuntimeException e) {
/* 311 */       this.logger.error("添加商品出错", e);
/* 312 */       showErrorJson("添加商品出错" + e.getMessage());
/*     */     }
/*     */     
/* 315 */     return "json_message";
/*     */   }
/*     */   
/*     */   public String saveEdit() {
/*     */     try {
/* 320 */       this.goodsManager.edit(this.goods);
/* 321 */       showSuccessJson("商品修改成功");
/*     */     }
/*     */     catch (RuntimeException e) {
/* 324 */       this.logger.error("修改商品出错", e);
/* 325 */       showErrorJson("修改商品出错" + e.getMessage());
/*     */     }
/* 327 */     return "json_message";
/*     */   }
/*     */   
/*     */   public String updateMarketEnable()
/*     */   {
/*     */     try {
/* 333 */       this.goodsManager.updateField("market_enable", Integer.valueOf(1), this.goodsId);
/* 334 */       showSuccessJson("更新上架状态成功");
/*     */     } catch (RuntimeException e) {
/* 336 */       showErrorJson("更新上架状态失败");
/* 337 */       this.logger.error("商品更新上架失败", e);
/*     */     }
/* 339 */     return "json_message";
/*     */   }
/*     */   
/*     */   public String selector() {
/* 343 */     return "selector";
/*     */   }
/*     */   
/*     */   public List getCatList() {
/* 347 */     return this.catList;
/*     */   }
/*     */   
/*     */   public void setCatList(List catList) {
/* 351 */     this.catList = catList;
/*     */   }
/*     */   
/*     */   public void setGoodsCatManager(IGoodsCatManager goodsCatManager) {
/* 355 */     this.goodsCatManager = goodsCatManager;
/*     */   }
/*     */   
/*     */   public void setGoods(Goods goods) {
/* 359 */     this.goods = goods;
/*     */   }
/*     */   
/*     */   public IGoodsManager getGoodsManager() {
/* 363 */     return this.goodsManager;
/*     */   }
/*     */   
/*     */   public void setGoodsManager(IGoodsManager goodsManager) {
/* 367 */     this.goodsManager = goodsManager;
/*     */   }
/*     */   
/*     */   public Goods getGoods() {
/* 371 */     return this.goods;
/*     */   }
/*     */   
/*     */   public Integer getGoodsId() {
/* 375 */     return this.goodsId;
/*     */   }
/*     */   
/*     */   public void setGoodsId(Integer goodsId) {
/* 379 */     this.goodsId = goodsId;
/*     */   }
/*     */   
/*     */   public Map getGoodsView() {
/* 383 */     return this.goodsView;
/*     */   }
/*     */   
/*     */   public void setGoodsView(Map goodsView) {
/* 387 */     this.goodsView = goodsView;
/*     */   }
/*     */   
/*     */   public Boolean getIs_edit() {
/* 391 */     return this.is_edit;
/*     */   }
/*     */   
/*     */   public void setIs_edit(Boolean is_edit) {
/* 395 */     this.is_edit = is_edit;
/*     */   }
/*     */   
/*     */   public String getActionName() {
/* 399 */     return this.actionName;
/*     */   }
/*     */   
/*     */   public void setActionName(String actionName) {
/* 403 */     this.actionName = actionName;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 407 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 411 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getOrder() {
/* 415 */     return this.order;
/*     */   }
/*     */   
/*     */   public void setOrder(String order) {
/* 419 */     this.order = order;
/*     */   }
/*     */   
/*     */   public String getSn() {
/* 423 */     return this.sn;
/*     */   }
/*     */   
/*     */   public void setSn(String sn) {
/* 427 */     this.sn = sn;
/*     */   }
/*     */   
/*     */   public Integer[] getGoods_id() {
/* 431 */     return this.goods_id;
/*     */   }
/*     */   
/*     */   public void setGoods_id(Integer[] goods_id) {
/* 435 */     this.goods_id = goods_id;
/*     */   }
/*     */   
/*     */   public GoodsPluginBundle getGoodsPluginBundle() {
/* 439 */     return this.goodsPluginBundle;
/*     */   }
/*     */   
/*     */   public void setGoodsPluginBundle(GoodsPluginBundle goodsPluginBundle) {
/* 443 */     this.goodsPluginBundle = goodsPluginBundle;
/*     */   }
/*     */   
/*     */   public List<String> getTagHtmlList() {
/* 447 */     return this.tagHtmlList;
/*     */   }
/*     */   
/*     */   public void setTagHtmlList(List<String> tagHtmlList) {
/* 451 */     this.tagHtmlList = tagHtmlList;
/*     */   }
/*     */   
/*     */   public Integer getCatid() {
/* 455 */     return this.catid;
/*     */   }
/*     */   
/*     */   public void setCatid(Integer catid) {
/* 459 */     this.catid = catid;
/*     */   }
/*     */   
/*     */   public Integer getMarket_enable() {
/* 463 */     return this.market_enable;
/*     */   }
/*     */   
/*     */   public void setMarket_enable(Integer marketEnable) {
/* 467 */     this.market_enable = marketEnable;
/*     */   }
/*     */   
/*     */   public Integer[] getTagids() {
/* 471 */     return this.tagids;
/*     */   }
/*     */   
/*     */   public void setTagids(Integer[] tagids) {
/* 475 */     this.tagids = tagids;
/*     */   }
/*     */   
/*     */   public ITagManager getTagManager() {
/* 479 */     return this.tagManager;
/*     */   }
/*     */   
/*     */   public void setTagManager(ITagManager tagManager) {
/* 483 */     this.tagManager = tagManager;
/*     */   }
/*     */   
/*     */   public IProductManager getProductManager() {
/* 487 */     return this.productManager;
/*     */   }
/*     */   
/*     */   public void setProductManager(IProductManager productManager) {
/* 491 */     this.productManager = productManager;
/*     */   }
/*     */   
/*     */   public IPermissionManager getPermissionManager() {
/* 495 */     return this.permissionManager;
/*     */   }
/*     */   
/*     */   public void setPermissionManager(IPermissionManager permissionManager) {
/* 499 */     this.permissionManager = permissionManager;
/*     */   }
/*     */   
/*     */   public IAdminUserManager getAdminUserManager() {
/* 503 */     return this.adminUserManager;
/*     */   }
/*     */   
/*     */   public void setAdminUserManager(IAdminUserManager adminUserManager) {
/* 507 */     this.adminUserManager = adminUserManager;
/*     */   }
/*     */   
/*     */   public int getDepotid() {
/* 511 */     return this.depotid;
/*     */   }
/*     */   
/*     */   public void setDepotid(int depotid) {
/* 515 */     this.depotid = depotid;
/*     */   }
/*     */   
/*     */   public List getBrandList() {
/* 519 */     return this.brandList;
/*     */   }
/*     */   
/*     */   public void setBrandList(List brandList) {
/* 523 */     this.brandList = brandList;
/*     */   }
/*     */   
/*     */   public IBrandManager getBrandManager() {
/* 527 */     return this.brandManager;
/*     */   }
/*     */   
/*     */   public void setBrandManager(IBrandManager brandManager) {
/* 531 */     this.brandManager = brandManager;
/*     */   }
/*     */   
/*     */   public IGoodsCatManager getGoodsCatManager() {
/* 535 */     return this.goodsCatManager;
/*     */   }
/*     */   
/*     */   public Integer getBrand_id() {
/* 539 */     return this.brand_id;
/*     */   }
/*     */   
/*     */   public void setBrand_id(Integer brand_id) {
/* 543 */     this.brand_id = brand_id;
/*     */   }
/*     */   
/*     */   public Integer getIs_market() {
/* 547 */     return this.is_market;
/*     */   }
/*     */   
/*     */   public void setIs_market(Integer is_market) {
/* 551 */     this.is_market = is_market;
/*     */   }
/*     */   
/*     */   public String getIs_other() {
/* 555 */     return this.is_other;
/*     */   }
/*     */   
/*     */   public void setIs_other(String is_other) {
/* 559 */     this.is_other = is_other;
/*     */   }
/*     */   
/*     */   public List<Tag> getTagList() {
/* 563 */     return this.tagList;
/*     */   }
/*     */   
/*     */   public void setTagList(List<Tag> tagList) {
/* 567 */     this.tagList = tagList;
/*     */   }
/*     */   
/*     */   public Map<Integer, String> getPluginTabs() {
/* 571 */     return this.pluginTabs;
/*     */   }
/*     */   
/*     */   public void setPluginTabs(Map<Integer, String> pluginTabs) {
/* 575 */     this.pluginTabs = pluginTabs;
/*     */   }
/*     */   
/*     */   public Map<Integer, String> getPluginHtmls() {
/* 579 */     return this.pluginHtmls;
/*     */   }
/*     */   
/*     */   public void setPluginHtmls(Map<Integer, String> pluginHtmls) {
/* 583 */     this.pluginHtmls = pluginHtmls;
/*     */   }
/*     */   
/*     */   public ICartManager getCartManager() {
/* 587 */     return this.cartManager;
/*     */   }
/*     */   
/*     */   public void setCartManager(ICartManager cartManager) {
/* 591 */     this.cartManager = cartManager;
/*     */   }
/*     */   
/*     */   public IOrderManager getOrderManager() {
/* 595 */     return this.orderManager;
/*     */   }
/*     */   
/*     */   public void setOrderManager(IOrderManager orderManager) {
/* 599 */     this.orderManager = orderManager;
/*     */   }
/*     */   
/*     */   public String getGoodslistid() {
/* 603 */     return this.goodslistid;
/*     */   }
/*     */   
/*     */   public void setGoodslistid(String goodslistid) {
/* 607 */     this.goodslistid = goodslistid;
/*     */   }
/*     */   
/*     */   public Integer getStype() {
/* 611 */     return this.stype;
/*     */   }
/*     */   
/*     */   public void setStype(Integer stype) {
/* 615 */     this.stype = stype;
/*     */   }
/*     */   
/*     */   public String selectCat() {
/* 619 */     return "select_cat";
/*     */   }
/*     */   
/*     */   public String getKeyword() {
/* 623 */     return this.keyword;
/*     */   }
/*     */   
/*     */   public void setKeyword(String keyword) {
/* 627 */     this.keyword = keyword;
/*     */   }
/*     */   
/*     */   public Map getGoodsMap() {
/* 631 */     return this.goodsMap;
/*     */   }
/*     */   
/*     */   public void setGoodsMap(Map goodsMap) {
/* 635 */     this.goodsMap = goodsMap;
/*     */   }
/*     */   
/*     */   public String getOptype() {
/* 639 */     return this.optype;
/*     */   }
/*     */   
/*     */   public void setOptype(String optype) {
/* 643 */     this.optype = optype;
/*     */   }
/*     */ }