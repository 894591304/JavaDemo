/*     */ package com.enation.app.shop.core.service.impl;
/*     */ 
/*     */ import com.enation.app.shop.core.model.Cat;
/*     */ import com.enation.app.shop.core.model.Goods;
/*     */ import com.enation.app.shop.core.model.GoodsStores;
/*     */ import com.enation.app.shop.core.model.support.GoodsEditDTO;
/*     */ import com.enation.app.shop.core.plugin.goods.GoodsDataFilterBundle;
/*     */ import com.enation.app.shop.core.plugin.goods.GoodsPluginBundle;
/*     */ import com.enation.app.shop.core.service.IDepotMonitorManager;
/*     */ import com.enation.app.shop.core.service.IGoodsCatManager;
/*     */ import com.enation.app.shop.core.service.IGoodsManager;
/*     */ import com.enation.app.shop.core.service.IMemberLvManager;
/*     */ import com.enation.app.shop.core.service.IMemberPriceManager;
/*     */ import com.enation.app.shop.core.service.IPackageProductManager;
/*     */ import com.enation.app.shop.core.service.ITagManager;
/*     */ import com.enation.app.shop.core.service.SnDuplicateException;
/*     */ import com.enation.eop.sdk.database.BaseSupport;
/*     */ import com.enation.eop.sdk.utils.UploadUtil;
/*     */ import com.enation.framework.context.webcontext.ThreadContextHolder;
/*     */ import com.enation.framework.database.IDaoSupport;
/*     */ import com.enation.framework.database.Page;
/*     */ import com.enation.framework.util.DateUtil;
/*     */ import com.enation.framework.util.StringUtil;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.beanutils.BeanUtils;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.springframework.jdbc.core.RowMapper;
/*     */ import org.springframework.transaction.annotation.Propagation;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GoodsManager
/*     */   extends BaseSupport
/*     */   implements IGoodsManager
/*     */ {
/*     */   private ITagManager tagManager;
/*     */   private GoodsPluginBundle goodsPluginBundle;
/*     */   private IPackageProductManager packageProductManager;
/*     */   private IGoodsCatManager goodsCatManager;
/*     */   private IMemberPriceManager memberPriceManager;
/*     */   private IMemberLvManager memberLvManager;
/*     */   private IDepotMonitorManager depotMonitorManager;
/*     */   private GoodsDataFilterBundle goodsDataFilterBundle;
/*     */   
/*     */   public GoodsManager() {}
/*     */   
/*     */   @Transactional(propagation=Propagation.REQUIRED)
/*     */   public void add(Goods goods)
/*     */   {
/*     */     try
/*     */     {
/*  61 */       Map goodsMap = po2Map(goods);
/*     */       
/*     */ 
/*  64 */       this.goodsPluginBundle.onBeforeAdd(goodsMap);
/*  65 */       goodsMap.put("disabled", Integer.valueOf(0));
/*  66 */       goodsMap.put("create_time", Long.valueOf(DateUtil.getDatelineLong()));
/*  67 */       goodsMap.put("view_count", Integer.valueOf(0));
/*  68 */       goodsMap.put("buy_count", Integer.valueOf(0));
/*  69 */       goodsMap.put("last_modify", Long.valueOf(DateUtil.getDatelineLong()));
/*  70 */       this.baseDaoSupport.insert("goods", goodsMap);
/*  71 */       Integer goods_id = Integer.valueOf(this.baseDaoSupport.getLastId("goods"));
/*  72 */       goods.setGoods_id(goods_id);
/*  73 */       goodsMap.put("goods_id", goods_id);
/*  74 */       this.goodsPluginBundle.onAfterAdd(goodsMap);
/*     */     } catch (RuntimeException e) {
/*  76 */       if ((e instanceof SnDuplicateException)) {
/*  77 */         throw e;
/*     */       }
/*  79 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Transactional(propagation=Propagation.REQUIRED)
/*     */   public void edit(Goods goods)
/*     */   {
/*     */     try
/*     */     {
/*  90 */       if (this.logger.isDebugEnabled()) {
/*  91 */         this.logger.debug("开始保存商品数据...");
/*     */       }
/*  93 */       Map goodsMap = po2Map(goods);
/*  94 */       this.goodsPluginBundle.onBeforeEdit(goodsMap);
/*  95 */       this.baseDaoSupport.update("goods", goodsMap, "goods_id=" + goods.getGoods_id());
/*     */       
/*  97 */       this.goodsPluginBundle.onAfterEdit(goodsMap);
/*  98 */       if (this.logger.isDebugEnabled()) {
/*  99 */         this.logger.debug("保存商品数据完成.");
/*     */       }
/*     */     } catch (RuntimeException e) {
/* 102 */       if ((e instanceof SnDuplicateException)) {
/* 103 */         throw e;
/*     */       }
/* 105 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GoodsEditDTO getGoodsEditData(Integer goods_id)
/*     */   {
/* 117 */     GoodsEditDTO editDTO = new GoodsEditDTO();
/* 118 */     String sql = "select * from goods where goods_id=?";
/* 119 */     Map goods = this.baseDaoSupport.queryForMap(sql, new Object[] { goods_id });
/*     */     
/* 121 */     String intro = (String)goods.get("intro");
/* 122 */     if (intro != null) {
/* 123 */       intro = UploadUtil.replacePath(intro);
/* 124 */       goods.put("intro", intro);
/*     */     }
/*     */     
/* 127 */     Map<Integer, String> htmlMap = this.goodsPluginBundle.onFillEditInputData(goods);
/*     */     
/* 129 */     editDTO.setGoods(goods);
/* 130 */     editDTO.setHtmlMap(htmlMap);
/*     */     
/* 132 */     return editDTO;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map get(Integer goods_id)
/*     */   {
/* 143 */     String sql = "select g.*,b.name as brand_name from " + getTableName("goods") + " g left join " + getTableName("brand") + " b on g.brand_id=b.brand_id ";
/*     */     
/*     */ 
/* 146 */     sql = sql + "  where goods_id=?";
/*     */     
/* 148 */     Map goods = this.daoSupport.queryForMap(sql, new Object[] { goods_id });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 154 */     String small = (String)goods.get("small");
/* 155 */     if (small != null) {
/* 156 */       small = UploadUtil.replacePath(small);
/* 157 */       goods.put("small", small);
/*     */     }
/* 159 */     String big = (String)goods.get("big");
/* 160 */     if (big != null) {
/* 161 */       big = UploadUtil.replacePath(big);
/* 162 */       goods.put("big", big);
/*     */     }
/*     */     
/*     */ 
/* 166 */     return goods;
/*     */   }
/*     */   
/*     */   public void getNavdata(Map goods)
/*     */   {
/* 171 */     int catid = ((Integer)goods.get("cat_id")).intValue();
/* 172 */     List list = this.goodsCatManager.getNavpath(catid);
/* 173 */     goods.put("navdata", list);
/*     */   }
/*     */   
/*     */   private String getListSql(int disabled)
/*     */   {
/* 178 */     String selectSql = this.goodsPluginBundle.onGetSelector();
/* 179 */     String fromSql = this.goodsPluginBundle.onGetFrom();
/*     */     
/* 181 */     String sql = "select g.*,b.name as brand_name ,t.name as type_name,c.name as cat_name " + selectSql + " from " + getTableName("goods") + " g left join " + getTableName("goods_cat") + " c on g.cat_id=c.cat_id left join " + getTableName("brand") + " b on g.brand_id = b.brand_id and b.disabled=0 left join " + getTableName("goods_type") + " t on g.type_id =t.type_id " + fromSql + " where g.goods_type = 'normal' and g.disabled=" + disabled;
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
/*     */ 
/*     */ 
/*     */ 
/* 195 */     return sql;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getBindListSql(int disabled)
/*     */   {
/* 205 */     String sql = "select g.*,b.name as brand_name ,t.name as type_name,c.name as cat_name from " + getTableName("goods") + " g left join " + getTableName("goods_cat") + " c on g.cat_id=c.cat_id left join " + getTableName("brand") + " b on g.brand_id = b.brand_id left join " + getTableName("goods_type") + " t on g.type_id =t.type_id" + " where g.goods_type = 'bind' and g.disabled=" + disabled;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 215 */     return sql;
/*     */   }
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
/*     */ 
/*     */   public Page searchBindGoods(String name, String sn, String order, int page, int pageSize)
/*     */   {
/* 230 */     String sql = getBindListSql(0);
/*     */     
/* 232 */     if (order == null) {
/* 233 */       order = "goods_id desc";
/*     */     }
/*     */     
/* 236 */     if ((name != null) && (!name.equals(""))) {
/* 237 */       sql = sql + "  and g.name like '%" + name + "%'";
/*     */     }
/*     */     
/* 240 */     if ((sn != null) && (!sn.equals(""))) {
/* 241 */       sql = sql + "   and g.sn = '" + sn + "'";
/*     */     }
/*     */     
/* 244 */     sql = sql + " order by g." + order;
/* 245 */     Page webpage = this.daoSupport.queryForPage(sql, page, pageSize, new Object[0]);
/*     */     
/* 247 */     List<Map> list = (List)webpage.getResult();
/*     */     
/* 249 */     for (Map map : list) {
/* 250 */       List productList = this.packageProductManager.list(Integer.valueOf(map.get("goods_id").toString()).intValue());
/*     */       
/* 252 */       productList = productList == null ? new ArrayList() : productList;
/* 253 */       map.put("productList", productList);
/*     */     }
/*     */     
/* 256 */     return webpage;
/*     */   }
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
/*     */ 
/*     */ 
/*     */   public Page pageTrash(String name, String sn, String order, int page, int pageSize)
/*     */   {
/* 272 */     String sql = getListSql(1);
/* 273 */     if (order == null) {
/* 274 */       order = "goods_id desc";
/*     */     }
/*     */     
/* 277 */     if ((name != null) && (!name.equals(""))) {
/* 278 */       sql = sql + "  and g.name like '%" + name + "%'";
/*     */     }
/*     */     
/* 281 */     if ((sn != null) && (!sn.equals(""))) {
/* 282 */       sql = sql + "   and g.sn = '" + sn + "'";
/*     */     }
/*     */     
/* 285 */     sql = sql + " order by g." + order;
/*     */     
/* 287 */     Page webpage = this.daoSupport.queryForPage(sql, page, pageSize, new Object[0]);
/*     */     
/* 289 */     return webpage;
/*     */   }
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
/*     */ 
/*     */ 
/*     */   public List<GoodsStores> storeWarnGoods(int warnTotal, int page, int pageSize)
/*     */   {
/* 305 */     String select_sql = "select gc.name as gc_name,b.name as b_name,g.cat_id,g.goods_id,g.name,g.sn,g.price,g.last_modify,g.market_enable,s.sumstore ";
/* 306 */     String left_sql = " left join " + getTableName("goods") + " g  on s.goodsid = g.goods_id  left join " + getTableName("goods_cat") + " gc on gc.cat_id = g.cat_id left join " + getTableName("brand") + " b on b.brand_id = g.brand_id ";
/* 307 */     List<GoodsStores> list = new ArrayList();
/*     */     
/* 309 */     String sql_2 = select_sql + " from  (select ss.* from (select goodsid,productid,sum(store) sumstore from " + getTableName("product_store") + "  group by goodsid,productid   ) ss " + "  left join " + getTableName("warn_num") + " wn on wn.goods_id = ss.goodsid  where ss.sumstore <=  (case when (wn.warn_num is not null or wn.warn_num <> 0) then wn.warn_num else ?  end )  ) s  " + left_sql;
/*     */     
/*     */ 
/*     */ 
/* 313 */     List<GoodsStores> list_2 = this.daoSupport.queryForList(sql_2, new RowMapper()
/*     */     {
/*     */       public Object mapRow(ResultSet rs, int arg1) throws SQLException
/*     */       {
/* 317 */         GoodsStores gs = new GoodsStores();
/* 318 */         gs.setGoods_id(Integer.valueOf(rs.getInt("goods_id")));
/* 319 */         gs.setName(rs.getString("name"));
/* 320 */         gs.setSn(rs.getString("sn"));
/* 321 */         gs.setRealstore(Integer.valueOf(rs.getInt("sumstore")));
/* 322 */         gs.setPrice(Double.valueOf(rs.getDouble("price")));
/* 323 */         gs.setLast_modify(Long.valueOf(rs.getLong("last_modify")));
/* 324 */         gs.setBrandname(rs.getString("b_name"));
/* 325 */         gs.setCatname(rs.getString("gc_name"));
/* 326 */         gs.setMarket_enable(Integer.valueOf(rs.getInt("market_enable")));
/* 327 */         gs.setCat_id(Integer.valueOf(rs.getInt("cat_id")));
/* 328 */         return gs; } }, new Object[] { Integer.valueOf(warnTotal) });
/*     */     
/*     */ 
/* 331 */     list.addAll(list_2);
/*     */     
/* 333 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Transactional(propagation=Propagation.REQUIRED)
/*     */   public void delete(Integer[] ids)
/*     */   {
/* 343 */     if (ids == null) {
/* 344 */       return;
/*     */     }
/* 346 */     for (Integer id : ids) {
/* 347 */       this.tagManager.saveRels(id, null);
/*     */     }
/* 349 */     String id_str = StringUtil.arrayToString(ids, ",");
/* 350 */     String sql = "update  goods set disabled=1  where goods_id in (" + id_str + ")";
/*     */     
/*     */ 
/* 353 */     this.baseDaoSupport.execute(sql, new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void revert(Integer[] ids)
/*     */   {
/* 362 */     if (ids == null)
/* 363 */       return;
/* 364 */     String id_str = StringUtil.arrayToString(ids, ",");
/* 365 */     String sql = "update  goods set disabled=0  where goods_id in (" + id_str + ")";
/*     */     
/* 367 */     this.baseDaoSupport.execute(sql, new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Transactional(propagation=Propagation.REQUIRED)
/*     */   public void clean(Integer[] ids)
/*     */   {
/* 377 */     if (ids == null)
/* 378 */       return;
/* 379 */     for (Integer id : ids) {
/* 380 */       this.tagManager.saveRels(id, null);
/*     */     }
/* 382 */     this.goodsPluginBundle.onGoodsDelete(ids);
/* 383 */     String id_str = StringUtil.arrayToString(ids, ",");
/* 384 */     String sql = "delete  from goods  where goods_id in (" + id_str + ")";
/* 385 */     this.baseDaoSupport.execute(sql, new Object[0]);
/*     */   }
/*     */   
/*     */   public List list(Integer[] ids) {
/* 389 */     if ((ids == null) || (ids.length == 0))
/* 390 */       return new ArrayList();
/* 391 */     String idstr = StringUtil.arrayToString(ids, ",");
/* 392 */     String sql = "select * from goods where goods_id in(" + idstr + ")";
/* 393 */     return this.baseDaoSupport.queryForList(sql, new Object[0]);
/*     */   }
/*     */   
/*     */   public GoodsPluginBundle getGoodsPluginBundle() {
/* 397 */     return this.goodsPluginBundle;
/*     */   }
/*     */   
/*     */   public void setGoodsPluginBundle(GoodsPluginBundle goodsPluginBundle) {
/* 401 */     this.goodsPluginBundle = goodsPluginBundle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Map po2Map(Object po)
/*     */   {
/* 411 */     Map poMap = new HashMap();
/* 412 */     Map map = new HashMap();
/*     */     try {
/* 414 */       map = BeanUtils.describe(po);
/*     */     }
/*     */     catch (Exception ex) {}
/* 417 */     Object[] keyArray = map.keySet().toArray();
/* 418 */     for (int i = 0; i < keyArray.length; i++) {
/* 419 */       String str = keyArray[i].toString();
/* 420 */       if ((str != null) && (!str.equals("class")) && 
/* 421 */         (map.get(str) != null)) {
/* 422 */         poMap.put(str, map.get(str));
/*     */       }
/*     */     }
/*     */     
/* 426 */     return poMap;
/*     */   }
/*     */   
/*     */   public IPackageProductManager getPackageProductManager() {
/* 430 */     return this.packageProductManager;
/*     */   }
/*     */   
/*     */   public void setPackageProductManager(IPackageProductManager packageProductManager)
/*     */   {
/* 435 */     this.packageProductManager = packageProductManager;
/*     */   }
/*     */   
/*     */   public Goods getGoods(Integer goods_id) {
/* 439 */     Goods goods = (Goods)this.baseDaoSupport.queryForObject("select * from goods where goods_id=?", Goods.class, new Object[] { goods_id });
/*     */     
/* 441 */     return goods;
/*     */   }
/*     */   
/*     */   public IGoodsCatManager getGoodsCatManager() {
/* 445 */     return this.goodsCatManager;
/*     */   }
/*     */   
/*     */   public void setGoodsCatManager(IGoodsCatManager goodsCatManager) {
/* 449 */     this.goodsCatManager = goodsCatManager;
/*     */   }
/*     */   
/*     */   @Transactional(propagation=Propagation.REQUIRED)
/*     */   public void batchEdit() {
/* 454 */     HttpServletRequest request = ThreadContextHolder.getHttpRequest();
/* 455 */     String[] ids = request.getParameterValues("goodsidArray");
/* 456 */     String[] names = request.getParameterValues("name");
/* 457 */     String[] prices = request.getParameterValues("price");
/* 458 */     String[] cats = request.getParameterValues("catidArray");
/* 459 */     String[] market_enable = request.getParameterValues("market_enables");
/* 460 */     String[] store = request.getParameterValues("store");
/* 461 */     String[] sord = request.getParameterValues("sord");
/*     */     
/* 463 */     String sql = "";
/*     */     
/* 465 */     for (int i = 0; i < ids.length; i++) {
/* 466 */       sql = "";
/* 467 */       if ((names != null) && (names.length > 0) && 
/* 468 */         (!StringUtil.isEmpty(names[i]))) {
/* 469 */         if (!sql.equals(""))
/* 470 */           sql = sql + ",";
/* 471 */         sql = sql + " name='" + names[i] + "'";
/*     */       }
/*     */       
/*     */ 
/* 475 */       if ((prices != null) && (prices.length > 0) && 
/* 476 */         (!StringUtil.isEmpty(prices[i]))) {
/* 477 */         if (!sql.equals(""))
/* 478 */           sql = sql + ",";
/* 479 */         sql = sql + " price=" + prices[i];
/*     */       }
/*     */       
/* 482 */       if ((cats != null) && (cats.length > 0) && 
/* 483 */         (!StringUtil.isEmpty(cats[i]))) {
/* 484 */         if (!sql.equals(""))
/* 485 */           sql = sql + ",";
/* 486 */         sql = sql + " cat_id=" + cats[i];
/*     */       }
/*     */       
/* 489 */       if ((store != null) && (store.length > 0) && 
/* 490 */         (!StringUtil.isEmpty(store[i]))) {
/* 491 */         if (!sql.equals(""))
/* 492 */           sql = sql + ",";
/* 493 */         sql = sql + " store=" + store[i];
/*     */       }
/*     */       
/* 496 */       if ((market_enable != null) && (market_enable.length > 0) && 
/* 497 */         (!StringUtil.isEmpty(market_enable[i]))) {
/* 498 */         if (!sql.equals(""))
/* 499 */           sql = sql + ",";
/* 500 */         sql = sql + " market_enable=" + market_enable[i];
/*     */       }
/*     */       
/* 503 */       if ((sord != null) && (sord.length > 0) && 
/* 504 */         (!StringUtil.isEmpty(sord[i]))) {
/* 505 */         if (!sql.equals(""))
/* 506 */           sql = sql + ",";
/* 507 */         sql = sql + " sord=" + sord[i];
/*     */       }
/*     */       
/* 510 */       sql = "update  goods set " + sql + " where goods_id=?";
/* 511 */       this.baseDaoSupport.execute(sql, new Object[] { ids[i] });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Map census()
/*     */   {
/* 518 */     String sql = "select count(0) from goods ";
/* 519 */     int allcount = this.baseDaoSupport.queryForInt(sql, new Object[0]);
/*     */     
/*     */ 
/* 522 */     sql = "select count(0) from goods where market_enable=1 and  disabled = 0";
/* 523 */     int salecount = this.baseDaoSupport.queryForInt(sql, new Object[0]);
/*     */     
/*     */ 
/* 526 */     sql = "select count(0) from goods where market_enable=0 and  disabled = 0";
/* 527 */     int unsalecount = this.baseDaoSupport.queryForInt(sql, new Object[0]);
/*     */     
/*     */ 
/* 530 */     sql = "select count(0) from goods where   disabled = 1";
/* 531 */     int disabledcount = this.baseDaoSupport.queryForInt(sql, new Object[0]);
/*     */     
/*     */ 
/* 534 */     sql = "select count(0) from comments where   for_comment_id is null  and commenttype='goods' and object_type='discuss'";
/* 535 */     int discusscount = this.baseDaoSupport.queryForInt(sql, new Object[0]);
/*     */     
/*     */ 
/* 538 */     sql = "select count(0) from comments where for_comment_id is null  and  commenttype='goods' and object_type='ask'";
/* 539 */     int askcount = this.baseDaoSupport.queryForInt(sql, new Object[0]);
/*     */     
/* 541 */     Map<String, Integer> map = new HashMap(2);
/* 542 */     map.put("salecount", Integer.valueOf(salecount));
/* 543 */     map.put("unsalecount", Integer.valueOf(unsalecount));
/* 544 */     map.put("disabledcount", Integer.valueOf(disabledcount));
/* 545 */     map.put("allcount", Integer.valueOf(allcount));
/* 546 */     map.put("discuss", Integer.valueOf(discusscount));
/* 547 */     map.put("ask", Integer.valueOf(askcount));
/* 548 */     return map;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List getRecommentList(int goods_id, int cat_id, int brand_id, int num)
/*     */   {
/* 556 */     return null;
/*     */   }
/*     */   
/*     */   public List list() {
/* 560 */     String sql = "select * from goods where disabled = 0";
/* 561 */     return this.baseDaoSupport.queryForList(sql, new Object[0]);
/*     */   }
/*     */   
/*     */   public IMemberPriceManager getMemberPriceManager() {
/* 565 */     return this.memberPriceManager;
/*     */   }
/*     */   
/*     */   public ITagManager getTagManager() {
/* 569 */     return this.tagManager;
/*     */   }
/*     */   
/*     */   public void setTagManager(ITagManager tagManager) {
/* 573 */     this.tagManager = tagManager;
/*     */   }
/*     */   
/*     */   public void setMemberPriceManager(IMemberPriceManager memberPriceManager) {
/* 577 */     this.memberPriceManager = memberPriceManager;
/*     */   }
/*     */   
/*     */   public IMemberLvManager getMemberLvManager() {
/* 581 */     return this.memberLvManager;
/*     */   }
/*     */   
/*     */   public void setMemberLvManager(IMemberLvManager memberLvManager) {
/* 585 */     this.memberLvManager = memberLvManager;
/*     */   }
/*     */   
/*     */   public void updateField(String filedname, Object value, Integer goodsid)
/*     */   {
/* 590 */     this.baseDaoSupport.execute("update goods set " + filedname + "=? where goods_id=?", new Object[] { value, goodsid });
/*     */   }
/*     */   
/*     */   public Goods getGoodBySn(String goodSn)
/*     */   {
/* 595 */     Goods goods = (Goods)this.baseDaoSupport.queryForObject("select * from goods where sn=?", Goods.class, new Object[] { goodSn });
/* 596 */     return goods;
/*     */   }
/*     */   
/*     */   public IDepotMonitorManager getDepotMonitorManager() {
/* 600 */     return this.depotMonitorManager;
/*     */   }
/*     */   
/*     */   public void setDepotMonitorManager(IDepotMonitorManager depotMonitorManager) {
/* 604 */     this.depotMonitorManager = depotMonitorManager;
/*     */   }
/*     */   
/*     */   public List listByCat(Integer catid)
/*     */   {
/* 609 */     String sql = getListSql(0);
/* 610 */     if (catid.intValue() != 0) {
/* 611 */       Cat cat = this.goodsCatManager.getById(catid.intValue());
/* 612 */       sql = sql + " and  g.cat_id in(";
/* 613 */       sql = sql + "select c.cat_id from " + getTableName("goods_cat") + " c where c.cat_path like '" + cat.getCat_path() + "%')  ";
/*     */     }
/*     */     
/*     */ 
/* 617 */     return this.daoSupport.queryForList(sql, new Object[0]);
/*     */   }
/*     */   
/*     */   public List listByTag(Integer[] tagid)
/*     */   {
/* 622 */     String sql = getListSql(0);
/* 623 */     if ((tagid != null) && (tagid.length > 0)) {
/* 624 */       String tagidstr = StringUtil.arrayToString(tagid, ",");
/* 625 */       sql = sql + " and g.goods_id in(select rel_id from " + getTableName("tag_rel") + " where tag_id in(" + tagidstr + "))";
/*     */     }
/*     */     
/*     */ 
/* 629 */     return this.daoSupport.queryForList(sql, new Object[0]);
/*     */   }
/*     */   
/*     */   public void incViewCount(Integer goods_id)
/*     */   {
/* 634 */     this.baseDaoSupport.execute("update goods set view_count = view_count + 1 where goods_id = ?", new Object[] { goods_id });
/*     */   }
/*     */   
/*     */   public List listGoods(String catid, String tagid, String goodsnum)
/*     */   {
/* 639 */     int num = 10;
/* 640 */     if (!StringUtil.isEmpty(goodsnum)) {
/* 641 */       num = Integer.valueOf(goodsnum).intValue();
/*     */     }
/*     */     
/* 644 */     StringBuffer sql = new StringBuffer();
/* 645 */     sql.append("select g.* from " + getTableName("tag_rel") + " r LEFT JOIN " + getTableName("goods") + " g ON g.goods_id=r.rel_id where g.disabled=0 and g.market_enable=1");
/*     */     
/* 647 */     if (!StringUtil.isEmpty(catid)) {
/* 648 */       Cat cat = this.goodsCatManager.getById(Integer.valueOf(catid).intValue());
/* 649 */       String cat_path = cat.getCat_path();
/* 650 */       if (cat_path != null) {
/* 651 */         sql.append(" and  g.cat_id in(");
/* 652 */         sql.append("select c.cat_id from " + getTableName("goods_cat") + " ");
/* 653 */         sql.append(" c where c.cat_path like '" + cat_path + "%')");
/*     */       }
/*     */     }
/*     */     
/* 657 */     if (!StringUtil.isEmpty(tagid)) {
/* 658 */       sql.append(" AND r.tag_id=" + tagid + "");
/*     */     }
/*     */     
/* 661 */     sql.append(" order by r.ordernum desc");
/*     */     
/* 663 */     List list = this.daoSupport.queryForListPage(sql.toString(), 1, num, new Object[0]);
/* 664 */     this.goodsDataFilterBundle.filterGoodsData(list);
/* 665 */     return list;
/*     */   }
/*     */   
/*     */   public GoodsDataFilterBundle getGoodsDataFilterBundle() {
/* 669 */     return this.goodsDataFilterBundle;
/*     */   }
/*     */   
/*     */   public void setGoodsDataFilterBundle(GoodsDataFilterBundle goodsDataFilterBundle) {
/* 673 */     this.goodsDataFilterBundle = goodsDataFilterBundle;
/*     */   }
/*     */   
/*     */   public List goodsBuyer(int goods_id, int pageSize)
/*     */   {
/* 678 */     String sql = "select distinct m.* from es_order o left join es_member m on o.member_id=m.member_id where order_id in (select order_id from es_order_items where goods_id=?)";
/*     */     
/*     */ 
/* 681 */     Page page = this.daoSupport.queryForPage(sql, 1, pageSize, new Object[] { Integer.valueOf(goods_id) });
/*     */     
/* 683 */     return (List)page.getResult();
/*     */   }
/*     */   
/*     */ 
/*     */   public Page searchGoods(Map goodsMap, int page, int pageSize, String other, String sort, String order)
/*     */   {
/* 689 */     String sql = creatTempSql(goodsMap, other);
/* 690 */     StringBuffer _sql = new StringBuffer(sql);
/* 691 */     this.goodsPluginBundle.onSearchFilter(_sql);
/* 692 */     _sql.append(" order by " + sort + " " + order);
/* 693 */     Page webpage = this.daoSupport.queryForPage(_sql.toString(), page, pageSize, new Object[0]);
/* 694 */     return webpage;
/*     */   }
/*     */   
/*     */   public List searchGoods(Map goodsMap)
/*     */   {
/* 699 */     String sql = creatTempSql(goodsMap, null);
/* 700 */     return this.daoSupport.queryForList(sql, Goods.class, new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */   private String creatTempSql(Map goodsMap, String other)
/*     */   {
/* 706 */     other = other == null ? "" : other;
/* 707 */     String sql = getListSql(0);
/* 708 */     Integer brandid = (Integer)goodsMap.get("brandid");
/* 709 */     Integer catid = (Integer)goodsMap.get("catid");
/* 710 */     Integer is_market = (Integer)goodsMap.get("is_market");
/* 711 */     String name = (String)goodsMap.get("name");
/* 712 */     String sn = (String)goodsMap.get("sn");
/* 713 */     Integer market_enable = (Integer)goodsMap.get("market_enable");
/* 714 */     Integer[] tagid = (Integer[])goodsMap.get("tagid");
/* 715 */     Integer stype = (Integer)goodsMap.get("stype");
/* 716 */     String keyword = (String)goodsMap.get("keyword");
/* 717 */     String order = (String)goodsMap.get("order");
/*     */     
/* 719 */     if ((brandid != null) && (brandid.intValue() != 0)) {
/* 720 */       sql = sql + " and g.brand_id = " + brandid + " ";
/*     */     }
/* 722 */     if ((is_market == null) || (is_market.intValue() == -1)) {
/* 723 */       sql = sql + "  ";
/* 724 */     } else if (is_market.intValue() == 1) {
/* 725 */       sql = sql + " and g.market_enable = 1 ";
/* 726 */     } else if (is_market.intValue() == 0) {
/* 727 */       sql = sql + " and g.market_enable = 0 ";
/*     */     }
/*     */     
/* 730 */     if ("1".equals(other))
/*     */     {
/* 732 */       sql = sql + " and g.no_discount=1";
/*     */     }
/* 734 */     if ("2".equals(other))
/*     */     {
/* 736 */       sql = sql + " and (select count(0) from " + getTableName("goods_lv_price") + " glp where glp.goodsid=g.goods_id) >0";
/*     */     }
/*     */     
/* 739 */     if ((stype != null) && (keyword != null) && 
/* 740 */       (stype.intValue() == 0)) {
/* 741 */       sql = sql + " and ( g.name like '%" + keyword + "%'";
/* 742 */       sql = sql + " or g.sn like '%" + keyword + "%')";
/*     */     }
/*     */     
/*     */ 
/* 746 */     if ((name != null) && (!name.equals(""))) {
/* 747 */       name = name.trim();
/* 748 */       String[] keys = name.split("\\s");
/* 749 */       for (String key : keys) {
/* 750 */         sql = sql + " and g.name like '%";
/* 751 */         sql = sql + key;
/* 752 */         sql = sql + "%'";
/*     */       }
/*     */     }
/*     */     
/* 756 */     if ((sn != null) && (!sn.equals(""))) {
/* 757 */       sql = sql + "   and g.sn like '%" + sn + "%'";
/*     */     }
/*     */     
/* 760 */     if (market_enable != null) {
/* 761 */       sql = sql + " and g.market_enable=" + market_enable + " ";
/*     */     }
/*     */     
/* 764 */     if ((catid != null) && (catid.intValue() != 0)) {
/* 765 */       Cat cat = this.goodsCatManager.getById(catid.intValue());
/* 766 */       sql = sql + " and  g.cat_id in(";
/* 767 */       sql = sql + "select c.cat_id from " + getTableName("goods_cat") + " c where c.cat_path like '" + cat.getCat_path() + "%')  ";
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 772 */     if ((tagid != null) && (tagid.length > 0)) {
/* 773 */       String tagidstr = StringUtil.arrayToString(tagid, ",");
/* 774 */       sql = sql + " and g.goods_id in(select rel_id from " + getTableName("tag_rel") + " where tag_id in(" + tagidstr + "))";
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 779 */     return sql;
/*     */   }
/*     */ }

/* Location:           /Users/root/Documents/yang.xiaolong/java/workspace/javamall/WebContent/WEB-INF/lib/component-shop.jar
 * Qualified Name:     com.enation.app.shop.core.service.impl.GoodsManager
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.0.1
 */