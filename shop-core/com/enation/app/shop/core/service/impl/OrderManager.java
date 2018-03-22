/*      */ package com.enation.app.shop.core.service.impl;
/*      */ import java.io.File;
/*      */ import java.io.InputStream;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;

/*      */ import org.springframework.jdbc.core.RowMapper;
/*      */ import org.springframework.transaction.annotation.Propagation;
/*      */ import org.springframework.transaction.annotation.Transactional;
/*      */ 
/*      */ import com.enation.app.base.core.model.Member;
/*      */ import com.enation.app.base.core.service.auth.IAdminUserManager;
/*      */ import com.enation.app.base.core.service.auth.IPermissionManager;
/*      */ import com.enation.app.base.core.service.auth.IRoleManager;
/*      */ import com.enation.app.base.core.service.auth.impl.PermissionConfig;
/*      */ import com.enation.app.shop.core.model.DepotUser;
/*      */ import com.enation.app.shop.core.model.DlyType;
/*      */ import com.enation.app.shop.core.model.Goods;
/*      */ import com.enation.app.shop.core.model.Order;
/*      */ import com.enation.app.shop.core.model.OrderItem;
/*      */ import com.enation.app.shop.core.model.OrderLog;
/*      */ import com.enation.app.shop.core.model.PayCfg;
/*      */ import com.enation.app.shop.core.model.Promotion;
/*      */ import com.enation.app.shop.core.model.support.CartItem;
/*      */ import com.enation.app.shop.core.model.support.OrderPrice;
/*      */ import com.enation.app.shop.core.plugin.order.OrderPluginBundle;
/*      */ import com.enation.app.shop.core.service.ICartManager;
/*      */ import com.enation.app.shop.core.service.IDepotManager;
/*      */ import com.enation.app.shop.core.service.IDlyTypeManager;
/*      */ import com.enation.app.shop.core.service.IGoodsManager;
/*      */ import com.enation.app.shop.core.service.IOrderAllocationManager;
/*      */ import com.enation.app.shop.core.service.IOrderFlowManager;
/*      */ import com.enation.app.shop.core.service.IOrderManager;
/*      */ import com.enation.app.shop.core.service.IPaymentManager;
/*      */ import com.enation.app.shop.core.service.IPromotionManager;
/*      */ import com.enation.eop.processor.httpcache.HttpCacheManager;
/*      */ import com.enation.eop.resource.model.AdminUser;
/*      */ import com.enation.eop.resource.model.EopSite;
/*      */ import com.enation.eop.sdk.context.EopContext;
/*      */ import com.enation.eop.sdk.context.EopSetting;
/*      */ import com.enation.eop.sdk.database.BaseSupport;
/*      */ import com.enation.eop.sdk.user.IUserService;
/*      */ import com.enation.eop.sdk.user.UserServiceFactory;
/*      */ import com.enation.framework.context.spring.SpringContextHolder;
/*      */ import com.enation.framework.database.DoubleMapper;
/*      */ import com.enation.framework.database.Page;
/*      */ import com.enation.framework.database.StringMapper;
/*      */ import com.enation.framework.util.CurrencyUtil;
/*      */ import com.enation.framework.util.ExcelUtil;
/*      */ import com.enation.framework.util.FileUtil;
/*      */ import com.enation.framework.util.StringUtil;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class OrderManager
/*      */   extends BaseSupport
/*      */   implements IOrderManager
/*      */ {
/*      */   private ICartManager cartManager;
/*      */   private IDlyTypeManager dlyTypeManager;
/*      */   private IPaymentManager paymentManager;
/*      */   private IPromotionManager promotionManager;
/*      */   private OrderPluginBundle orderPluginBundle;
/*      */   private IPermissionManager permissionManager;
/*      */   private IAdminUserManager adminUserManager;
/*      */   private IRoleManager roleManager;
/*      */   private IGoodsManager goodsManager;
/*      */   private IOrderAllocationManager orderAllocationManager;
/*      */   private IDepotManager depotManager;
/*      */   
/*      */   public OrderManager() {}
/*      */   
/*      */   public IOrderAllocationManager getOrderAllocationManager()
/*      */   {
/*   84 */     return this.orderAllocationManager;
/*      */   }
/*      */   
/*      */   public void setOrderAllocationManager(IOrderAllocationManager orderAllocationManager) {
/*   88 */     this.orderAllocationManager = orderAllocationManager;
/*      */   }
/*      */   
/*   91 */   public IGoodsManager getGoodsManager() { return this.goodsManager; }
/*      */   
/*      */   public void setGoodsManager(IGoodsManager goodsManager) {
/*   94 */     this.goodsManager = goodsManager;
/*      */   }
/*      */   
/*   97 */   public IDepotManager getDepotManager() { return this.depotManager; }
/*      */   
/*      */ 
/*  100 */   public void setDepotManager(IDepotManager depotManager) { this.depotManager = depotManager; }
/*      */   
/*      */   @Transactional(propagation=Propagation.REQUIRED)
/*      */   public void savePrice(double price, int orderid) {
/*  104 */     Order order = get(Integer.valueOf(orderid));
/*  105 */     double amount = order.getOrder_amount().doubleValue();
/*      */     
/*  107 */     double discount = CurrencyUtil.sub(amount, price);
/*  108 */     this.baseDaoSupport.execute("update order set order_amount=?,need_pay_money=? where order_id=?", new Object[] { Double.valueOf(price), Double.valueOf(price), Integer.valueOf(orderid) });
/*      */     
/*      */ 
/*  111 */     this.baseDaoSupport.execute("update order set discount=discount+? where order_id=?", new Object[] { Double.valueOf(discount), Integer.valueOf(orderid) });
/*      */     
/*      */ 
/*  114 */     AdminUser adminUser = this.adminUserManager.getCurrentUser();
/*  115 */     double nowamount = get(Integer.valueOf(orderid)).getOrder_amount().doubleValue();
/*  116 */     log(Integer.valueOf(orderid), "修改订单价格从" + amount + "修改为" + nowamount, null, adminUser.getUsername());
/*      */   }
/*      */   
/*      */   @Transactional(propagation=Propagation.REQUIRED)
/*  120 */   public double saveShipmoney(double shipmoney, int orderid) { Order order = get(Integer.valueOf(orderid));
/*  121 */     double currshipamount = order.getShipping_amount().doubleValue();
/*      */     
/*  123 */     double shortship = CurrencyUtil.sub(shipmoney, currshipamount);
/*  124 */     double discount = CurrencyUtil.sub(currshipamount, shipmoney);
/*  125 */     this.baseDaoSupport.execute("update order set order_amount=order_amount+? where order_id=?", new Object[] { Double.valueOf(shortship), Integer.valueOf(orderid) });
/*      */     
/*      */ 
/*  128 */     this.baseDaoSupport.execute("update order set shipping_amount=? where order_id=?", new Object[] { Double.valueOf(shipmoney), Integer.valueOf(orderid) });
/*      */     
/*      */ 
/*  131 */     this.baseDaoSupport.execute("update order set discount=discount+? where order_id=?", new Object[] { Double.valueOf(discount), Integer.valueOf(orderid) });
/*      */     
/*      */ 
/*  134 */     AdminUser adminUser = this.adminUserManager.getCurrentUser();
/*      */     
/*  136 */     double lastestShipmoney = get(Integer.valueOf(orderid)).getShipping_amount().doubleValue();
/*  137 */     log(Integer.valueOf(orderid), "运费从" + currshipamount + "修改为" + lastestShipmoney, null, adminUser.getUsername());
/*  138 */     return get(Integer.valueOf(orderid)).getOrder_amount().doubleValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void log(Integer order_id, String message, Integer op_id, String op_name)
/*      */   {
/*  150 */     OrderLog orderLog = new OrderLog();
/*  151 */     orderLog.setMessage(message);
/*  152 */     orderLog.setOp_id(op_id);
/*  153 */     orderLog.setOp_name(op_name);
/*  154 */     orderLog.setOp_time(Long.valueOf(com.enation.framework.util.DateUtil.getDatelineLong()));
/*  155 */     orderLog.setOrder_id(order_id);
/*  156 */     this.baseDaoSupport.insert("order_log", orderLog);
/*      */   }
/*      */   
/*      */   @Transactional(propagation=Propagation.REQUIRED)
/*      */   public Order add(Order order, String sessionid) {
/*  161 */     String opname = "游客";
/*      */     
/*  163 */     if (order == null) {
/*  164 */       throw new RuntimeException("error: order is null");
/*      */     }
/*      */     
/*  167 */     IUserService userService = UserServiceFactory.getUserService();
/*  168 */     Member member = userService.getCurrentMember();
/*      */     
/*  170 */     if (member != null) {
/*  171 */       order.setMember_id(member.getMember_id());
/*  172 */       opname = member.getUname();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  177 */     boolean isProtected = order.getIs_protect().compareTo(Integer.valueOf(1)) == 0;
/*      */     
/*  179 */     OrderPrice orderPrice = this.cartManager.countPrice(sessionid, order.getShipping_id(), "" + order.getRegionid(), Boolean.valueOf(isProtected));
/*      */     
/*  181 */     order.setGoods_amount(orderPrice.getGoodsPrice());
/*  182 */     order.setWeight(orderPrice.getWeight());
/*      */     
/*  184 */     order.setDiscount(orderPrice.getDiscountPrice());
/*  185 */     order.setOrder_amount(orderPrice.getOrderPrice());
/*  186 */     order.setProtect_price(orderPrice.getProtectPrice());
/*  187 */     order.setShipping_amount(orderPrice.getShippingPrice());
/*  188 */     order.setGainedpoint(orderPrice.getPoint().intValue());
/*      */     
/*  190 */     order.setNeed_pay_money(orderPrice.getNeedPayMoney());
/*      */     
/*  192 */     DlyType dlyType = this.dlyTypeManager.getDlyTypeById(order.getShipping_id());
/*  193 */     if (dlyType == null)
/*  194 */       throw new RuntimeException("shipping not found count error");
/*  195 */     order.setShipping_type(dlyType.getName());
/*      */     
/*      */ 
/*  198 */     PayCfg payCfg = this.paymentManager.get(order.getPayment_id());
/*  199 */     order.setPaymoney(this.paymentManager.countPayPrice(order.getOrder_id()));
/*  200 */     order.setPayment_name(payCfg.getName());
/*      */     
/*  202 */     order.setPayment_type(payCfg.getType());
/*      */     
/*      */ 
/*  205 */     order.setCreate_time(Long.valueOf(com.enation.framework.util.DateUtil.getDatelineLong()));
/*      */     
/*  207 */     order.setSn(createSn());
/*  208 */     order.setStatus(Integer.valueOf(9));
/*  209 */     order.setDisabled(Integer.valueOf(0));
/*  210 */     order.setPay_status(Integer.valueOf(0));
/*  211 */     order.setShip_status(Integer.valueOf(2));
/*  212 */     order.setOrderStatus("订单已生效");
/*      */     
/*      */ 
/*  215 */     Integer depotId = Integer.valueOf(this.baseDaoSupport.queryForInt("select id from depot where choose=1", null));
/*  216 */     order.setDepotid(depotId);
/*      */     
/*  218 */     List<CartItem> itemList = this.cartManager.listGoods(sessionid);
/*      */     
/*  220 */     this.orderPluginBundle.onBeforeCreate(order, itemList, sessionid);
/*  221 */     this.baseDaoSupport.insert("order", order);
/*      */     
/*      */ 
/*      */ 
/*  225 */     if (itemList.isEmpty()) {
/*  226 */       throw new RuntimeException("创建订单失败，购物车为空");
/*      */     }
/*  228 */     Integer orderId = Integer.valueOf(this.baseDaoSupport.getLastId("order"));
/*      */     
/*      */ 
/*  231 */     saveGoodsItem(itemList, orderId);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  237 */     if (member != null) {
/*  238 */       this.promotionManager.applyOrderPmt(orderId, orderPrice.getOrderPrice(), member.getLv_id());
/*      */       
/*  240 */       List<Promotion> pmtList = this.promotionManager.list(orderPrice.getOrderPrice(), member.getLv_id());
/*      */       
/*  242 */       for (Promotion pmt : pmtList) {
/*  243 */         String sql = "insert into order_pmt(pmt_id,order_id,pmt_describe)values(?,?,?)";
/*  244 */         this.baseDaoSupport.execute(sql, new Object[] { Integer.valueOf(pmt.getPmt_id()), orderId, pmt.getPmt_describe() });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  251 */     OrderLog log = new OrderLog();
/*  252 */     log.setMessage("订单创建");
/*  253 */     log.setOp_name(opname);
/*  254 */     log.setOrder_id(orderId);
/*  255 */     addLog(log);
/*  256 */     order.setOrder_id(orderId);
/*  257 */     order.setOrderprice(orderPrice);
/*  258 */     this.orderPluginBundle.onAfterCreate(order, itemList, sessionid);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  263 */     IOrderFlowManager flowManager = (IOrderFlowManager)SpringContextHolder.getBean("orderFlowManager");
/*  264 */     flowManager.confirmOrder(orderId);
/*  265 */     this.cartManager.clean(sessionid);
/*  266 */     HttpCacheManager.sessionChange();
/*      */     
/*  268 */     return order;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void addLog(OrderLog log)
/*      */   {
/*  277 */     log.setOp_time(Long.valueOf(com.enation.framework.util.DateUtil.getDatelineLong()));
/*  278 */     this.baseDaoSupport.insert("order_log", log);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void saveGoodsItem(List<CartItem> itemList, Integer order_id)
/*      */   {
/*  288 */     for (int i = 0; i < itemList.size(); i++)
/*      */     {
/*  290 */       OrderItem orderItem = new OrderItem();
/*      */       
/*  292 */       CartItem cartItem = (CartItem)itemList.get(i);
/*  293 */       orderItem.setPrice(cartItem.getCoupPrice());
/*  294 */       orderItem.setName(cartItem.getName());
/*  295 */       orderItem.setNum(Integer.valueOf(cartItem.getNum()));
/*      */       
/*  297 */       orderItem.setGoods_id(cartItem.getGoods_id());
/*  298 */       orderItem.setShip_num(Integer.valueOf(0));
/*  299 */       orderItem.setProduct_id(cartItem.getProduct_id());
/*  300 */       orderItem.setOrder_id(order_id);
/*  301 */       orderItem.setGainedpoint(cartItem.getPoint().intValue());
/*  302 */       orderItem.setAddon(cartItem.getAddon());
/*      */       
/*      */ 
/*  305 */       orderItem.setSn(cartItem.getSn());
/*  306 */       orderItem.setImage(cartItem.getImage_default());
/*  307 */       orderItem.setCat_id(cartItem.getCatid());
/*      */       
/*  309 */       orderItem.setUnit(cartItem.getUnit());
/*      */       
/*  311 */       this.baseDaoSupport.insert("order_items", orderItem);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Transactional(propagation=Propagation.REQUIRED)
/*      */   private void saveGiftItem(List<CartItem> itemList, Integer orderid)
/*      */   {
/*  325 */     Member member = UserServiceFactory.getUserService().getCurrentMember();
/*  326 */     if (member == null) {
/*  327 */       throw new IllegalStateException("会员尚未登录,不能兑换赠品!");
/*      */     }
/*      */     
/*  330 */     int point = 0;
/*  331 */     for (CartItem item : itemList) {
/*  332 */       point += item.getSubtotal().intValue();
/*  333 */       this.baseDaoSupport.execute("insert into order_gift(order_id,gift_id,gift_name,point,num,shipnum,getmethod)values(?,?,?,?,?,?,?)", new Object[] { orderid, item.getProduct_id(), item.getName(), item.getPoint(), Integer.valueOf(item.getNum()), Integer.valueOf(0), "exchange" });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  339 */     if (member.getPoint().intValue() < point) {
/*  340 */       throw new IllegalStateException("会员积分不足,不能兑换赠品!");
/*      */     }
/*  342 */     member.setPoint(Integer.valueOf(member.getPoint().intValue() - point));
/*  343 */     this.baseDaoSupport.execute("update member set point=? where member_id=? ", new Object[] { member.getPoint(), member.getMember_id() });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Page listbyshipid(int pageNo, int pageSize, int status, int shipping_id, String sort, String order)
/*      */   {
/*  351 */     order = " ORDER BY " + sort + " " + order;
/*  352 */     String sql = "select * from order where disabled=0 and status=" + status + " and shipping_id= " + shipping_id;
/*      */     
/*  354 */     sql = sql + " order by " + order;
/*  355 */     Page page = this.baseDaoSupport.queryForPage(sql, pageNo, pageSize, Order.class, new Object[0]);
/*      */     
/*  357 */     return page;
/*      */   }
/*      */   
/*      */   public Page listConfirmPay(int pageNo, int pageSize, String sort, String order) {
/*  361 */     order = " order_id";
/*  362 */     String sql = "select * from order where disabled=0 and ((status = 5 and payment_type = 'cod') or status= 1  )";
/*      */     
/*      */ 
/*  365 */     sql = sql + " order by " + order;
/*      */     
/*  367 */     Page page = this.baseDaoSupport.queryForPage(sql, pageNo, pageSize, Order.class, new Object[0]);
/*  368 */     return page;
/*      */   }
/*      */   
/*      */   public Order get(Integer orderId) {
/*  372 */     String sql = "select * from order where order_id=?";
/*  373 */     Order order = (Order)this.baseDaoSupport.queryForObject(sql, Order.class, new Object[] { orderId });
/*      */     
/*  375 */     return order;
/*      */   }
/*      */   
/*  378 */   public Order get(String ordersn) { String sql = "select * from es_order where sn='" + ordersn + "'";
/*  379 */     Order order = (Order)this.baseDaoSupport.queryForObject(sql, Order.class, new Object[0]);
/*  380 */     return order;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<OrderItem> listGoodsItems(Integer orderId)
/*      */   {
/*  387 */     String sql = "select * from " + getTableName("order_items");
/*  388 */     sql = sql + " where order_id = ?";
/*  389 */     List<OrderItem> itemList = this.daoSupport.queryForList(sql, OrderItem.class, new Object[] { orderId });
/*  390 */     this.orderPluginBundle.onFilter(orderId, itemList);
/*  391 */     return itemList;
/*      */   }
/*      */   
/*      */   public List listGiftItems(Integer orderId)
/*      */   {
/*  396 */     String sql = "select * from order_gift where order_id=?";
/*  397 */     return this.baseDaoSupport.queryForList(sql, new Object[] { orderId });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List listLogs(Integer orderId)
/*      */   {
/*  405 */     String sql = "select * from order_log where order_id=?";
/*  406 */     return this.baseDaoSupport.queryForList(sql, new Object[] { orderId });
/*      */   }
/*      */   
/*      */   @Transactional(propagation=Propagation.REQUIRED)
/*      */   public void clean(Integer[] orderId) {
/*  411 */     String ids = StringUtil.arrayToString(orderId, ",");
/*  412 */     String sql = "delete from order where order_id in (" + ids + ")";
/*  413 */     this.baseDaoSupport.execute(sql, new Object[0]);
/*      */     
/*  415 */     sql = "delete from order_items where order_id in (" + ids + ")";
/*  416 */     this.baseDaoSupport.execute(sql, new Object[0]);
/*      */     
/*  418 */     sql = "delete from order_log where order_id in (" + ids + ")";
/*  419 */     this.baseDaoSupport.execute(sql, new Object[0]);
/*      */     
/*  421 */     sql = "delete from payment_logs where order_id in (" + ids + ")";
/*  422 */     this.baseDaoSupport.execute(sql, new Object[0]);
/*      */     
/*  424 */     sql = "delete from " + getTableName("delivery_item") + " where delivery_id in (select delivery_id from " + getTableName("delivery") + " where order_id in (" + ids + "))";
/*      */     
/*      */ 
/*      */ 
/*  428 */     this.daoSupport.execute(sql, new Object[0]);
/*      */     
/*  430 */     sql = "delete from delivery where order_id in (" + ids + ")";
/*  431 */     this.baseDaoSupport.execute(sql, new Object[0]);
/*      */     
/*  433 */     this.orderAllocationManager.clean(orderId);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  441 */     this.orderPluginBundle.onDelete(orderId);
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean exec(Integer[] orderId, int disabled)
/*      */   {
/*  447 */     if (cheack(orderId)) {
/*  448 */       String ids = StringUtil.arrayToString(orderId, ",");
/*  449 */       String sql = "update order set disabled = ? where order_id in (" + ids + ")";
/*      */       
/*  451 */       this.baseDaoSupport.execute(sql, new Object[] { Integer.valueOf(disabled) });
/*  452 */       return true;
/*      */     }
/*  454 */     return false;
/*      */   }
/*      */   
/*      */   private boolean cheack(Integer[] orderId) {
/*  458 */     boolean i = true;
/*  459 */     for (int j = 0; j < orderId.length; j++) {
/*  460 */       if (this.baseDaoSupport.queryForInt("select status from es_order where order_id=?", new Object[] { orderId[j] }) != 8) {
/*  461 */         i = false;
/*      */       }
/*      */     }
/*  464 */     return i;
/*      */   }
/*      */   
/*  467 */   public boolean delete(Integer[] orderId) { return exec(orderId, 1); }
/*      */   
/*      */ 
/*      */   public void revert(Integer[] orderId)
/*      */   {
/*  472 */     exec(orderId, 0);
/*      */   }
/*      */   
/*      */   public String createSn()
/*      */   {
/*  477 */     Date now = new Date();
/*  478 */     String sn = com.enation.framework.util.DateUtil.toString(now, "yyyyMMddhhmmss");
/*      */     
/*  480 */     return sn;
/*      */   }
/*      */   
/*      */   public ICartManager getCartManager() {
/*  484 */     return this.cartManager;
/*      */   }
/*      */   
/*      */   public void setCartManager(ICartManager cartManager) {
/*  488 */     this.cartManager = cartManager;
/*      */   }
/*      */   
/*      */   public IDlyTypeManager getDlyTypeManager() {
/*  492 */     return this.dlyTypeManager;
/*      */   }
/*      */   
/*      */   public void setDlyTypeManager(IDlyTypeManager dlyTypeManager) {
/*  496 */     this.dlyTypeManager = dlyTypeManager;
/*      */   }
/*      */   
/*      */   public IPaymentManager getPaymentManager() {
/*  500 */     return this.paymentManager;
/*      */   }
/*      */   
/*      */   public void setPaymentManager(IPaymentManager paymentManager) {
/*  504 */     this.paymentManager = paymentManager;
/*      */   }
/*      */   
/*      */   public List listOrderByMemberId(int member_id) {
/*  508 */     String sql = "select * from order where member_id = ? order by create_time desc";
/*  509 */     List list = this.baseDaoSupport.queryForList(sql, Order.class, new Object[] { Integer.valueOf(member_id) });
/*      */     
/*  511 */     return list;
/*      */   }
/*      */   
/*      */   public Map mapOrderByMemberId(int memberId) {
/*  515 */     Integer buyTimes = Integer.valueOf(this.baseDaoSupport.queryForInt("select count(0) from order where member_id = ?", new Object[] { Integer.valueOf(memberId) }));
/*      */     
/*  517 */     Double buyAmount = (Double)this.baseDaoSupport.queryForObject("select sum(paymoney) from order where member_id = ?", new DoubleMapper(), new Object[] { Integer.valueOf(memberId) });
/*      */     
/*      */ 
/*  520 */     Map map = new HashMap();
/*  521 */     map.put("buyTimes", buyTimes);
/*  522 */     map.put("buyAmount", buyAmount);
/*  523 */     return map;
/*      */   }
/*      */   
/*      */   public IPromotionManager getPromotionManager() {
/*  527 */     return this.promotionManager;
/*      */   }
/*      */   
/*      */   public void setPromotionManager(IPromotionManager promotionManager) {
/*  531 */     this.promotionManager = promotionManager;
/*      */   }
/*      */   
/*      */   public void edit(Order order) {
/*  535 */     this.baseDaoSupport.update("order", order, "order_id = " + order.getOrder_id());
/*      */   }
/*      */   
/*      */ 
/*      */   public List<Map> listAdjItem(Integer orderid)
/*      */   {
/*  541 */     String sql = "select * from order_items where order_id=? and addon!=''";
/*  542 */     return this.baseDaoSupport.queryForList(sql, new Object[] { orderid });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Map censusState()
/*      */   {
/*  552 */     Map<String, Integer> stateMap = new HashMap(7);
/*  553 */     String[] states = { "cancel_ship", "cancel_pay", "pay", "ship", "complete", "allocation_yes" };
/*  554 */     for (String s : states) {
/*  555 */       stateMap.put(s, Integer.valueOf(0));
/*      */     }
/*      */     
/*      */ 
/*  559 */     String sql = "select count(0) num,status  from " + getTableName("order") + " where disabled = 0 group by status";
/*      */     
/*      */ 
/*  562 */     List<Map<String, Integer>> list = this.daoSupport.queryForList(sql, new RowMapper()
/*      */     {
/*      */       public Object mapRow(ResultSet rs, int arg1) throws SQLException
/*      */       {
/*  566 */         Map<String, Integer> map = new HashMap();
/*  567 */         map.put("status", Integer.valueOf(rs.getInt("status")));
/*  568 */         map.put("num", Integer.valueOf(rs.getInt("num")));
/*  569 */         return map; } }, new Object[0]);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  574 */     for (Map<String, Integer> state : list) {
/*  575 */       stateMap.put(getStateString((Integer)state.get("status")), state.get("num"));
/*      */     }
/*      */     
/*  578 */     sql = "select count(0) num  from " + getTableName("order") + " where disabled = 0  and status=0 ";
/*  579 */     int count = this.daoSupport.queryForInt(sql, new Object[0]);
/*  580 */     stateMap.put("wait", Integer.valueOf(0));
/*      */     
/*  582 */     sql = "select count(0) num  from " + getTableName("order") + " where disabled = 0  and pay_status = 0 ";
/*  583 */     count = this.daoSupport.queryForInt(sql, new Object[0]);
/*  584 */     stateMap.put("not_pay", Integer.valueOf(count));
/*      */     
/*  586 */     sql = "select count(0) from es_order where disabled=0  and ( ( payment_type!='cod' and payment_id!=8  and  status=2)  or ( payment_type='cod' and  status=0))";
/*  587 */     count = this.baseDaoSupport.queryForInt(sql, new Object[0]);
/*  588 */     stateMap.put("allocation_yes", Integer.valueOf(count));
/*      */     
/*  590 */     return stateMap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String getStateString(Integer state)
/*      */   {
/*  600 */     String str = null;
/*  601 */     switch (state.intValue()) {
/*      */     case -2: 
/*  603 */       str = "cancel_ship";
/*  604 */       break;
/*      */     case -1: 
/*  606 */       str = "cancel_pay";
/*  607 */       break;
/*      */     case 1: 
/*  609 */       str = "pay";
/*  610 */       break;
/*      */     case 2: 
/*  612 */       str = "ship";
/*  613 */       break;
/*      */     case 4: 
/*  615 */       str = "allocation_yes";
/*  616 */       break;
/*      */     case 7: 
/*  618 */       str = "complete";
/*  619 */       break;
/*      */     case 0: case 3: case 5: case 6: default: 
/*  621 */       str = null;
/*      */     }
/*      */     
/*  624 */     return str;
/*      */   }
/*      */   
/*      */   public OrderPluginBundle getOrderPluginBundle()
/*      */   {
/*  629 */     return this.orderPluginBundle;
/*      */   }
/*      */   
/*      */   public void setOrderPluginBundle(OrderPluginBundle orderPluginBundle) {
/*  633 */     this.orderPluginBundle = orderPluginBundle;
/*      */   }
/*      */   
/*      */   public String export(Date start, Date end)
/*      */   {
/*  638 */     String sql = "select * from order where disabled=0 ";
/*  639 */     if (start != null) {
/*  640 */       sql = sql + " and create_time>" + start.getTime();
/*      */     }
/*      */     
/*  643 */     if (end != null) {
/*  644 */       sql = sql + "  and create_timecreate_time<" + end.getTime();
/*      */     }
/*      */     
/*  647 */     List<Order> orderList = this.baseDaoSupport.queryForList(sql, Order.class, new Object[0]);
/*      */     
/*      */ 
/*      */ 
/*  651 */     ExcelUtil excelUtil = new ExcelUtil();
/*      */     
/*      */ 
/*  654 */     InputStream in = FileUtil.getResourceAsStream("com/enation/app/shop/core/service/impl/order.xls");
/*      */     
/*  656 */     excelUtil.openModal(in);
/*  657 */     int i = 1;
/*  658 */     for (Order order : orderList)
/*      */     {
/*  660 */       excelUtil.writeStringToCell(i, 0, order.getSn());
/*  661 */       excelUtil.writeStringToCell(i, 1, com.enation.eop.sdk.utils.DateUtil.toString(new Date(order.getCreate_time().longValue()), "yyyy-MM-dd HH:mm:ss"));
/*  662 */       excelUtil.writeStringToCell(i, 2, order.getOrderStatus());
/*  663 */       excelUtil.writeStringToCell(i, 3, "" + order.getOrder_amount());
/*  664 */       excelUtil.writeStringToCell(i, 4, order.getShip_name());
/*  665 */       excelUtil.writeStringToCell(i, 5, order.getPayStatus());
/*  666 */       excelUtil.writeStringToCell(i, 6, order.getShipStatus());
/*  667 */       excelUtil.writeStringToCell(i, 7, order.getShipping_type());
/*  668 */       excelUtil.writeStringToCell(i, 8, order.getPayment_name());
/*  669 */       i++;
/*      */     }
/*      */     
/*      */ 
/*  673 */     String filename = "";
/*  674 */     if ("2".equals(EopSetting.RUNMODE)) {
/*  675 */       EopSite site = EopContext.getContext().getCurrentSite();
/*  676 */       filename = "/user/" + site.getUserid() + "/" + site.getId() + "/order";
/*      */     } else {
/*  678 */       filename = "/order";
/*      */     }
/*  680 */     File file = new File(EopSetting.IMG_SERVER_PATH + filename);
/*  681 */     if (!file.exists()) { file.mkdirs();
/*      */     }
/*  683 */     filename = filename + "/order" + com.enation.framework.util.DateUtil.getDatelineLong() + ".xls";
/*  684 */     excelUtil.writeToFile(EopSetting.IMG_SERVER_PATH + filename);
/*      */     
/*  686 */     return EopSetting.IMG_SERVER_DOMAIN + filename;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public OrderItem getItem(int itemid)
/*      */   {
/*  693 */     String sql = "select items.*,p.store as store from " + getTableName("order_items") + " items ";
/*      */     
/*  695 */     sql = sql + " left join " + getTableName("product") + " p on p.product_id = items.product_id ";
/*      */     
/*  697 */     sql = sql + " where items.item_id = ?";
/*      */     
/*  699 */     OrderItem item = (OrderItem)this.daoSupport.queryForObject(sql, OrderItem.class, new Object[] { Integer.valueOf(itemid) });
/*      */     
/*      */ 
/*  702 */     return item;
/*      */   }
/*      */   
/*      */   public IAdminUserManager getAdminUserManager()
/*      */   {
/*  707 */     return this.adminUserManager;
/*      */   }
/*      */   
/*      */   public void setAdminUserManager(IAdminUserManager adminUserManager) {
/*  711 */     this.adminUserManager = adminUserManager;
/*      */   }
/*      */   
/*      */   public IPermissionManager getPermissionManager() {
/*  715 */     return this.permissionManager;
/*      */   }
/*      */   
/*      */   public void setPermissionManager(IPermissionManager permissionManager) {
/*  719 */     this.permissionManager = permissionManager;
/*      */   }
/*      */   
/*      */   public IRoleManager getRoleManager() {
/*  723 */     return this.roleManager;
/*      */   }
/*      */   
/*      */   public void setRoleManager(IRoleManager roleManager) {
/*  727 */     this.roleManager = roleManager;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMemberOrderNum(int member_id, int payStatus)
/*      */   {
/*  736 */     return this.baseDaoSupport.queryForInt("SELECT COUNT(0) FROM order WHERE member_id=? AND pay_status=?", new Object[] { Integer.valueOf(member_id), Integer.valueOf(payStatus) });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Map> getItemsByOrderid(Integer order_id)
/*      */   {
/*  747 */     String sql = "select * from order_items where order_id=?";
/*  748 */     return this.baseDaoSupport.queryForList(sql, new Object[] { order_id });
/*      */   }
/*      */   
/*      */   public void refuseReturn(String orderSn)
/*      */   {
/*  753 */     this.baseDaoSupport.execute("update order set state = -5 where sn = ?", new Object[] { orderSn });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateOrderPrice(double price, int orderid)
/*      */   {
/*  762 */     this.baseDaoSupport.execute("update order set order_amount = order_amount-?,goods_amount = goods_amount- ? where order_id = ?", new Object[] { Double.valueOf(price), Double.valueOf(price), Integer.valueOf(orderid) });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String queryLogiNameById(Integer logi_id)
/*      */   {
/*  773 */     return (String)this.baseDaoSupport.queryForObject("select name from logi_company where id=?", new StringMapper(), new Object[] { logi_id });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Page searchForGuest(int pageNo, int pageSize, String ship_name, String ship_tel)
/*      */   {
/*  783 */     String sql = "select * from order where ship_name=? AND (ship_mobile=? OR ship_tel=?) ORDER BY order_id DESC";
/*  784 */     Page page = this.baseDaoSupport.queryForPage(sql.toString(), pageNo, pageSize, Order.class, new Object[] { ship_name, ship_tel, ship_tel });
/*      */     
/*  786 */     return page;
/*      */   }
/*      */   
/*      */   public Page listByStatus(int pageNo, int pageSize, int status, int memberid) {
/*  790 */     String filedname = "status";
/*  791 */     if (status == 0)
/*      */     {
/*  793 */       filedname = " status!=8 AND pay_status";
/*      */     }
/*      */     
/*  796 */     String sql = "select * from order where " + filedname + "=? AND member_id=? ORDER BY order_id DESC";
/*      */     
/*  798 */     Page page = this.baseDaoSupport.queryForPage(sql.toString(), pageNo, pageSize, Order.class, new Object[] { Integer.valueOf(status), Integer.valueOf(memberid) });
/*      */     
/*  800 */     return page;
/*      */   }
/*      */   
/*      */   public List<Order> listByStatus(int status, int memberid)
/*      */   {
/*  805 */     String filedname = "status";
/*  806 */     if (status == 0)
/*      */     {
/*  808 */       filedname = " status!=8 AND pay_status";
/*      */     }
/*      */     
/*  811 */     String sql = "select * from order where " + filedname + "=? AND member_id=? ORDER BY order_id DESC";
/*      */     
/*      */ 
/*  814 */     return this.baseDaoSupport.queryForList(sql, new Object[] { Integer.valueOf(status), Integer.valueOf(memberid) });
/*      */   }
/*      */   
/*      */   public int getMemberOrderNum(int member_id)
/*      */   {
/*  819 */     return this.baseDaoSupport.queryForInt("SELECT COUNT(0) FROM order WHERE member_id=?", new Object[] { Integer.valueOf(member_id) });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Page search(int pageNO, int pageSize, int disabled, String sn, String logi_no, String uname, String ship_name, int status, Integer paystatus)
/*      */   {
/*  826 */     StringBuffer sql = new StringBuffer("select * from " + getTableName("order") + " where disabled=?  ");
/*      */     
/*  828 */     if (status != -100) {
/*  829 */       if (status == -99)
/*      */       {
/*      */ 
/*      */ 
/*  833 */         sql.append(" and ((payment_type='cod' and status=0 )  or (payment_type!='cod' and status=1 )) ");
/*      */       }
/*      */       else {
/*  836 */         sql.append(" and status = " + status + " ");
/*      */       }
/*      */     }
/*  839 */     if ((paystatus != null) && (paystatus.intValue() != -100)) {
/*  840 */       sql.append(" and pay_status = " + paystatus + " ");
/*      */     }
/*      */     
/*  843 */     if (!StringUtil.isEmpty(sn)) {
/*  844 */       sql.append(" and sn = '" + sn + "' ");
/*      */     }
/*  846 */     if (!StringUtil.isEmpty(uname)) {
/*  847 */       sql.append(" and member_id  in ( SELECT  member_id FROM " + getTableName("member") + " where uname = '" + uname + "' )  ");
/*      */     }
/*      */     
/*  850 */     if (!StringUtil.isEmpty(ship_name)) {
/*  851 */       sql.append(" and  ship_name = '" + ship_name + "' ");
/*      */     }
/*  853 */     if (!StringUtil.isEmpty(logi_no)) {
/*  854 */       sql.append(" and order_id in (SELECT order_id FROM " + getTableName("delivery") + " where logi_no = '" + logi_no + "') ");
/*      */     }
/*      */     
/*  857 */     sql.append(" order by create_time desc ");
/*  858 */     Page page = this.daoSupport.queryForPage(sql.toString(), pageNO, pageSize, Order.class, new Object[] { Integer.valueOf(disabled) });
/*      */     
/*  860 */     return page;
/*      */   }
/*      */   
/*      */ 
/*      */   public Page search(int pageNO, int pageSize, int disabled, String sn, String logi_no, String uname, String ship_name, int status)
/*      */   {
/*  866 */     return search(pageNO, pageSize, disabled, sn, logi_no, uname, ship_name, status, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Order getNext(String next, Integer orderId, Integer status, int disabled, String sn, String logi_no, String uname, String ship_name)
/*      */   {
/*  873 */     StringBuffer sql = new StringBuffer("select * from " + getTableName("order") + " where  1=1  ");
/*      */     
/*      */ 
/*  876 */     StringBuffer depotsql = new StringBuffer("  ");
/*  877 */     AdminUser adminUser = this.adminUserManager.getCurrentUser();
/*  878 */     if (adminUser.getFounder() != 1)
/*      */     {
/*  880 */       boolean isShiper = this.permissionManager.checkHaveAuth(PermissionConfig.getAuthId("depot_ship"));
/*      */       
/*      */ 
/*  883 */       boolean haveAllo = this.permissionManager.checkHaveAuth(PermissionConfig.getAuthId("allocation"));
/*      */       
/*  885 */       boolean haveOrder = this.permissionManager.checkHaveAuth(PermissionConfig.getAuthId("order"));
/*      */       
/*  887 */       if ((isShiper) && (!haveAllo) && (!haveOrder)) {
/*  888 */         DepotUser depotUser = (DepotUser)adminUser;
/*  889 */         int depotid = depotUser.getDepotid().intValue();
/*  890 */         depotsql.append(" and depotid=" + depotid + "  ");
/*      */       }
/*      */     }
/*      */     
/*  894 */     StringBuilder sbsql = new StringBuilder("  ");
/*  895 */     if ((status != null) && (status.intValue() != -100)) {
/*  896 */       sbsql.append(" and status = " + status + " ");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  901 */     if ((!StringUtil.isEmpty(uname)) && (!uname.equals("undefined"))) {
/*  902 */       sbsql.append(" and member_id  in ( SELECT  member_id FROM " + getTableName("member") + " where uname = '" + uname + "' )  ");
/*      */     }
/*      */     
/*  905 */     if (!StringUtil.isEmpty(ship_name)) {
/*  906 */       sbsql.append("  and  ship_name = '" + ship_name.trim() + "'  ");
/*      */     }
/*  908 */     if ((!StringUtil.isEmpty(logi_no)) && (!logi_no.equals("undefined"))) {
/*  909 */       sbsql.append("  and order_id in (SELECT order_id FROM " + getTableName("delivery") + " where logi_no = '" + logi_no + "')  ");
/*      */     }
/*  911 */     if (next.equals("previous")) {
/*  912 */       sql.append("  and order_id IN (SELECT CASE WHEN SIGN(order_id - " + orderId + ") < 0 THEN MAX(order_id)  END AS order_id FROM " + getTableName("order") + " WHERE order_id <> " + orderId + depotsql.toString() + " and disabled=? " + sbsql.toString() + " GROUP BY SIGN(order_id - " + orderId + ") ORDER BY SIGN(order_id - " + orderId + "))   ");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*  918 */     else if (next.equals("next")) {
/*  919 */       sql.append("  and  order_id in (SELECT CASE WHEN SIGN(order_id - " + orderId + ") > 0 THEN MIN(order_id) END AS order_id FROM " + getTableName("order") + " WHERE order_id <> " + orderId + depotsql.toString() + " and disabled=? " + sbsql.toString() + " GROUP BY SIGN(order_id - " + orderId + ") ORDER BY SIGN(order_id - " + orderId + "))   ");
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*  925 */       return null;
/*      */     }
/*  927 */     sql.append(" order by create_time desc ");
/*      */     
/*  929 */     Order order = (Order)this.daoSupport.queryForObject(sql.toString(), Order.class, new Object[] { Integer.valueOf(disabled) });
/*      */     
/*  931 */     return order;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private double getOrderTotal(String sessionid)
/*      */   {
/*  941 */     List goodsItemList = this.cartManager.listGoods(sessionid);
/*  942 */     double orderTotal = 0.0D;
/*  943 */     if ((goodsItemList != null) && (goodsItemList.size() > 0)) {
/*  944 */       for (int i = 0; i < goodsItemList.size(); i++) {
/*  945 */         CartItem cartItem = (CartItem)goodsItemList.get(i);
/*  946 */         orderTotal += cartItem.getCoupPrice().doubleValue() * cartItem.getNum();
/*      */       }
/*      */     }
/*  949 */     return orderTotal;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private OrderItem getOrderItem(Integer itemid)
/*      */   {
/*  958 */     return (OrderItem)this.baseDaoSupport.queryForObject("select * from order_items where item_id = ?", OrderItem.class, new Object[] { itemid });
/*      */   }
/*      */   
/*      */   @Transactional(propagation=Propagation.REQUIRED)
/*      */   public boolean delItem(Integer itemid, Integer itemnum) {
/*  963 */     OrderItem item = getOrderItem(itemid);
/*  964 */     Order order = get(item.getOrder_id());
/*  965 */     boolean flag = false;
/*  966 */     int paymentid = order.getPayment_id().intValue();
/*  967 */     int status = order.getStatus().intValue();
/*  968 */     if (((paymentid == 1) || (paymentid == 3) || (paymentid == 4) || (paymentid == 5)) && ((status == 0) || (status == 1) || (status == 2) || (status == 3) || (status == 4))) {
/*  969 */       flag = true;
/*      */     }
/*  971 */     if ((paymentid == 2) && ((status == 0) || (status == 9) || (status == 3) || (status == 4))) {
/*  972 */       flag = true;
/*      */     }
/*  974 */     if (flag) {
/*      */       try {
/*  976 */         if (itemnum.intValue() <= item.getNum().intValue()) {
/*  977 */           Goods goods = this.goodsManager.getGoods(item.getGoods_id());
/*  978 */           double order_amount = order.getOrder_amount().doubleValue();
/*  979 */           double itemprice = item.getPrice().doubleValue() * itemnum.intValue();
/*  980 */           double leftprice = CurrencyUtil.sub(order_amount, itemprice);
/*  981 */           int difpoint = (int)Math.floor(leftprice);
/*  982 */           Double[] dlyprice = this.dlyTypeManager.countPrice(order.getShipping_id(), Double.valueOf(order.getWeight().doubleValue() - goods.getWeight().doubleValue() * itemnum.intValue()), Double.valueOf(leftprice), order.getShip_regionid().toString(), false);
/*  983 */           double sumdlyprice = dlyprice[0].doubleValue();
/*  984 */           this.baseDaoSupport.execute("update order set goods_amount = goods_amount- ?,shipping_amount = ?,order_amount =  ?,weight =  weight - ?,gainedpoint =  ? where order_id = ?", new Object[] { Double.valueOf(itemprice), Double.valueOf(sumdlyprice), Double.valueOf(leftprice), Double.valueOf(goods.getWeight().doubleValue() * itemnum.intValue()), Integer.valueOf(difpoint), order.getOrder_id() });
/*      */           
/*  986 */           this.baseDaoSupport.execute("update freeze_point set mp =?,point =?  where orderid = ? and type = ?", new Object[] { Integer.valueOf(difpoint), Integer.valueOf(difpoint), order.getOrder_id(), "buygoods" });
/*  987 */           if (itemnum.intValue() == item.getNum().intValue()) {
/*  988 */             this.baseDaoSupport.execute("delete from order_items where item_id = ?", new Object[] { itemid });
/*      */           } else {
/*  990 */             this.baseDaoSupport.execute("update order_items set num = num - ? where item_id = ?", new Object[] { Integer.valueOf(itemnum.intValue()), itemid });
/*      */           }
/*      */         }
/*      */         else {
/*  994 */           return false;
/*      */         }
/*      */       }
/*      */       catch (Exception e) {
/*  998 */         e.printStackTrace();
/*  999 */         return false;
/*      */       }
/*      */     }
/*      */     
/* 1003 */     return flag;
/*      */   }
/*      */   
/*      */   @Transactional(propagation=Propagation.REQUIRED)
/*      */   public boolean saveAddrDetail(String addr, int orderid)
/*      */   {
/* 1009 */     Order order = get(Integer.valueOf(orderid));
/* 1010 */     String oldAddr = order.getShip_addr();
/* 1011 */     if ((addr == null) || (StringUtil.isEmpty(addr))) {
/* 1012 */       return false;
/*      */     }
/* 1014 */     this.baseDaoSupport.execute("update order set ship_addr=?  where order_id=?", new Object[] { addr, Integer.valueOf(orderid) });
/* 1015 */     AdminUser adminUser = this.adminUserManager.getCurrentUser();
/* 1016 */     log(Integer.valueOf(orderid), "收货人详细地址从['" + oldAddr + "']修改为['" + addr + "']", null, adminUser.getUsername());
/* 1017 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean saveShipInfo(String remark, String ship_day, String ship_name, String ship_tel, String ship_mobile, String ship_zip, int orderid)
/*      */   {
/* 1023 */     Order order = get(Integer.valueOf(orderid));
/* 1024 */     AdminUser adminUser = this.adminUserManager.getCurrentUser();
/*      */     try {
/* 1026 */       if ((ship_day != null) && (!StringUtil.isEmpty(ship_day))) {
/* 1027 */         String oldShip_day = order.getShip_day();
/* 1028 */         this.baseDaoSupport.execute("update order set ship_day=?  where order_id=?", new Object[] { ship_day, Integer.valueOf(orderid) });
/* 1029 */         if ((remark != null) && (!StringUtil.isEmpty(remark)) && (!remark.equals("undefined"))) {
/* 1030 */           StringBuilder sb = new StringBuilder("");
/* 1031 */           sb.append("【配送时间：");
/* 1032 */           sb.append(remark.trim());
/* 1033 */           sb.append("】");
/* 1034 */           this.baseDaoSupport.execute("update order set remark= concat(remark,'" + sb.toString() + "')   where order_id=?", new Object[] { Integer.valueOf(orderid) });
/*      */         }
/* 1036 */         log(Integer.valueOf(orderid), "收货日期从['" + oldShip_day + "']修改为['" + ship_day + "']", null, adminUser.getUsername());
/* 1037 */         return true;
/*      */       }
/* 1039 */       if ((ship_name != null) && (!StringUtil.isEmpty(ship_name))) {
/* 1040 */         String oldship_name = order.getShip_name();
/* 1041 */         this.baseDaoSupport.execute("update order set ship_name=?  where order_id=?", new Object[] { ship_name, Integer.valueOf(orderid) });
/* 1042 */         log(Integer.valueOf(orderid), "收货人姓名从['" + oldship_name + "']修改为['" + ship_name + "']", null, adminUser.getUsername());
/* 1043 */         return true;
/*      */       }
/* 1045 */       if ((ship_tel != null) && (!StringUtil.isEmpty(ship_tel))) {
/* 1046 */         String oldship_tel = order.getShip_tel();
/* 1047 */         this.baseDaoSupport.execute("update order set ship_tel=?  where order_id=?", new Object[] { ship_tel, Integer.valueOf(orderid) });
/* 1048 */         log(Integer.valueOf(orderid), "收货人电话从['" + oldship_tel + "']修改为['" + ship_tel + "']", null, adminUser.getUsername());
/* 1049 */         return true;
/*      */       }
/* 1051 */       if ((ship_mobile != null) && (!StringUtil.isEmpty(ship_mobile))) {
/* 1052 */         String oldship_mobile = order.getShip_mobile();
/* 1053 */         this.baseDaoSupport.execute("update order set ship_mobile=?  where order_id=?", new Object[] { ship_mobile, Integer.valueOf(orderid) });
/* 1054 */         log(Integer.valueOf(orderid), "收货人手机从['" + oldship_mobile + "']修改为['" + ship_mobile + "']", null, adminUser.getUsername());
/* 1055 */         return true;
/*      */       }
/* 1057 */       if ((ship_zip != null) && (!StringUtil.isEmpty(ship_zip))) {
/* 1058 */         String oldship_zip = order.getShip_zip();
/* 1059 */         this.baseDaoSupport.execute("update order set ship_zip=?  where order_id=?", new Object[] { ship_zip, Integer.valueOf(orderid) });
/* 1060 */         log(Integer.valueOf(orderid), "收货人邮编从['" + oldship_zip + "']修改为['" + ship_zip + "']", null, adminUser.getUsername());
/* 1061 */         return true;
/*      */       }
/* 1063 */       return false;
/*      */     } catch (Exception e) {
/* 1065 */       e.printStackTrace(); }
/* 1066 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updatePayMethod(int orderid, int payid, String paytype, String payname)
/*      */   {
/* 1074 */     this.baseDaoSupport.execute("update order set payment_id=?,payment_type=?,payment_name=? where order_id=?", new Object[] { Integer.valueOf(payid), paytype, payname, Integer.valueOf(orderid) });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean checkProInOrder(int productid)
/*      */   {
/* 1084 */     String sql = "select count(0) from order_items where product_id=?";
/* 1085 */     return this.baseDaoSupport.queryForInt(sql, new Object[] { Integer.valueOf(productid) }) > 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean checkGoodsInOrder(int goodsid)
/*      */   {
/* 1094 */     String sql = "select count(0) from order_items where goods_id=?";
/* 1095 */     return this.baseDaoSupport.queryForInt(sql, new Object[] { Integer.valueOf(goodsid) }) > 0;
/*      */   }
/*      */   
/*      */   public List listByOrderIds(Integer[] orderids, String order)
/*      */   {
/*      */     try {
/* 1101 */       StringBuffer sql = new StringBuffer("select * from es_order where disabled=0 ");
/*      */       
/* 1103 */       if ((orderids != null) && (orderids.length > 0)) {
/* 1104 */         sql.append(" and order_id in (" + StringUtil.arrayToString(orderids, ",") + ")");
/*      */       }
/* 1106 */       if (StringUtil.isEmpty(order)) {
/* 1107 */         order = "create_time desc";
/*      */       }
/* 1109 */       sql.append(" order by  " + order);
/* 1110 */       return this.daoSupport.queryForList(sql.toString(), Order.class, new Object[0]);
/*      */     } catch (Exception e) {
/* 1112 */       e.printStackTrace(); }
/* 1113 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public Page list(int pageNO, int pageSize, int disabled, String searchkey, String searchValue, String order)
/*      */   {
/* 1119 */     StringBuffer sql = new StringBuffer("select * from order where disabled=? ");
/*      */     
/* 1121 */     if ((!StringUtil.isEmpty(searchkey)) && (!StringUtil.isEmpty(searchValue))) {
/* 1122 */       sql.append(" and ");
/* 1123 */       sql.append(searchkey);
/* 1124 */       sql.append("=?");
/*      */     }
/*      */     
/* 1127 */     AdminUser adminUser = this.adminUserManager.getCurrentUser();
/* 1128 */     if (adminUser.getFounder() != 1) {
/* 1129 */       boolean isShiper = this.permissionManager.checkHaveAuth(PermissionConfig.getAuthId("depot_ship"));
/* 1130 */       boolean haveAllo = this.permissionManager.checkHaveAuth(PermissionConfig.getAuthId("allocation"));
/*      */       
/* 1132 */       boolean haveOrder = this.permissionManager.checkHaveAuth(PermissionConfig.getAuthId("order"));
/*      */       
/* 1134 */       if ((isShiper) && (!haveAllo) && (!haveOrder)) {
/* 1135 */         DepotUser depotUser = (DepotUser)adminUser;
/* 1136 */         int depotid = depotUser.getDepotid().intValue();
/* 1137 */         sql.append(" and depotid=" + depotid);
/*      */       }
/*      */     }
/*      */     
/* 1141 */     order = StringUtil.isEmpty(order) ? "order_id desc" : order;
/* 1142 */     sql.append(" order by " + order);
/* 1143 */     Page page = null;
/* 1144 */     if ((!StringUtil.isEmpty(searchkey)) && (!StringUtil.isEmpty(searchValue)))
/*      */     {
/*      */ 
/* 1147 */       page = this.baseDaoSupport.queryForPage(sql.toString(), pageNO, pageSize, Order.class, new Object[] { Integer.valueOf(disabled), searchValue });
/*      */     }
/*      */     else {
/* 1150 */       page = this.baseDaoSupport.queryForPage(sql.toString(), pageNO, pageSize, Order.class, new Object[] { Integer.valueOf(disabled) });
/*      */     }
/*      */     
/* 1153 */     return page;
/*      */   }
/*      */   
/*      */   public Page list(int pageNo, int pageSize, int status, int depotid, String order)
/*      */   {
/* 1158 */     order = StringUtil.isEmpty(order) ? "order_id desc" : order;
/* 1159 */     String sql = "select * from order where disabled=0 and status=" + status;
/*      */     
/* 1161 */     if (depotid > 0) {
/* 1162 */       sql = sql + " and depotid=" + depotid;
/*      */     }
/* 1164 */     sql = sql + " order by " + order;
/* 1165 */     Page page = this.baseDaoSupport.queryForPage(sql, pageNo, pageSize, Order.class, new Object[0]);
/* 1166 */     return page;
/*      */   }
/*      */   
/*      */ 
/*      */   public Page listOrder(Map map, int page, int pageSize, String other, String order)
/*      */   {
/* 1172 */     String sql = createTempSql(map, other, order);
/* 1173 */     Page webPage = this.baseDaoSupport.queryForPage(sql, page, pageSize, new Object[0]);
/* 1174 */     return webPage;
/*      */   }
/*      */   
/*      */ 
/*      */   private String createTempSql(Map map, String other, String order)
/*      */   {
				String agentSql = "";
				boolean isAgent = false;
				AdminUser currentUser = this.adminUserManager.getCurrentUser();
				if(checkAgentUser(currentUser.getUserid())>0){
					isAgent = true;
					agentSql = " and g.agentid=" + currentUser.getUserid() + " ";
				}
/* 1180 */     Integer stype = (Integer)map.get("stype");
/* 1181 */     String keyword = (String)map.get("keyword");
/* 1182 */     String orderstate = (String)map.get("order_state");
/* 1183 */     String start_time = (String)map.get("start_time");
/* 1184 */     String end_time = (String)map.get("end_time");
/* 1185 */     Integer status = (Integer)map.get("status");
/* 1186 */     String sn = (String)map.get("sn");
/* 1187 */     String ship_name = (String)map.get("ship_name");
/* 1188 */     Integer paystatus = (Integer)map.get("paystatus");
/* 1189 */     Integer shipstatus = (Integer)map.get("shipstatus");
/* 1190 */     Integer shipping_type = (Integer)map.get("shipping_type");
/* 1191 */     Integer payment_id = (Integer)map.get("payment_id");
/* 1192 */     Integer depotid = (Integer)map.get("depotid");
/* 1193 */     String complete = (String)map.get("complete");
/*      */     
/* 1195 */     StringBuffer sql = new StringBuffer();
/* 1196 */     sql.append("select * from (select distinct o.* from es_order o,es_order_items i,es_goods g where i.order_id=o.order_id and i.goods_id=g.goods_id and o.disabled=0 ");
/*      */     
/* 1198 */     if ((stype != null) && (keyword != null) && 
/* 1199 */       (stype.intValue() == 0)) {
/* 1200 */       sql.append(" and (o.sn like '%" + keyword + "%'");
/* 1201 */       sql.append(" or o.ship_name like '%" + keyword + "%')");
/*      */     }
/*      */     
/*      */ 
/* 1205 */     if (status != null) {
/* 1206 */       sql.append("and o.status=" + status);
/*      */     }
/*      */     
/* 1209 */     if ((sn != null) && (!StringUtil.isEmpty(sn))) {
/* 1210 */       sql.append(" and o.sn like '%" + sn + "%'");
/*      */     }
/*      */     
/* 1213 */     if ((ship_name != null) && (!StringUtil.isEmpty(ship_name))) {
/* 1214 */       sql.append(" and o.ship_name like '" + ship_name + "'");
/*      */     }
/*      */     
/* 1217 */     if (paystatus != null) {
/* 1218 */       sql.append(" and o.pay_status=" + paystatus);
/*      */     }
/*      */     
/* 1221 */     if (shipstatus != null) {
/* 1222 */       sql.append(" and o.ship_status=" + shipstatus);
/*      */     }
/*      */     
/* 1225 */     if (shipping_type != null) {
/* 1226 */       sql.append(" and o.shipping_id=" + shipping_type);
/*      */     }
/*      */     
/* 1229 */     if (payment_id != null) {
/* 1230 */       sql.append(" and payment_id=" + payment_id);
/*      */     }
/*      */     
/* 1233 */     if ((depotid != null) && (depotid.intValue() > 0)) {
/* 1234 */       sql.append(" and o.depotid=" + depotid);
/*      */     }
/*      */     
/* 1237 */     if ((start_time != null) && (!StringUtil.isEmpty(start_time))) {
/* 1238 */       long stime = com.enation.framework.util.DateUtil.getDateline(start_time + " 00:00:00");
/* 1239 */       sql.append(" and o.create_time>" + stime);
/*      */     }
/* 1241 */     if ((end_time != null) && (!StringUtil.isEmpty(end_time))) {
/* 1242 */       long etime = com.enation.framework.util.DateUtil.getDateline(end_time + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
/* 1243 */       sql.append(" and o.create_time<" + etime * 1000L);
/*      */     }
/* 1245 */     if (!StringUtil.isEmpty(orderstate)) {
/* 1246 */       if (orderstate.equals("wait_ship")) {
/* 1247 */         sql.append(" and ( ( o.payment_type!='cod' and o.payment_id!=8  and  o.status=2) ");
/* 1248 */         sql.append(" or ( o.payment_type='cod' and  o.status=0)) ");
/* 1249 */       } else if (orderstate.equals("wait_pay")) {
/* 1250 */         sql.append(" and o.pay_status = 0");
/*      */       }
/* 1254 */       else if (orderstate.equals("wait_rog")) {
/* 1255 */         sql.append(" and o.status=5");
/*      */       } else {
/* 1257 */         sql.append(" and o.status=" + orderstate);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1262 */     if (!StringUtil.isEmpty(complete)) {
/* 1263 */       sql.append(" and o.status=7");
/*      */     }
/*      */     if(isAgent){
					sql.append(agentSql);
				}
/* 1266 */     sql.append(") temp ORDER BY " + other + " " + order);
/* 1269 */     return sql.toString();
/*      */   }
/*      */   
/* 1272 */   public void saveDepot(int orderid, int depotid) { String depotname = this.depotManager.get(depotid).getName();
/* 1273 */     Order order = get(Integer.valueOf(orderid));
/* 1274 */     String oldname = this.depotManager.get(order.getDepotid().intValue()).getName();
/* 1275 */     this.daoSupport.execute("update es_order set depotid=?  where order_id=?", new Object[] { Integer.valueOf(depotid), Integer.valueOf(orderid) });
/* 1276 */     AdminUser adminUser = this.adminUserManager.getCurrentUser();
/* 1277 */     log(Integer.valueOf(orderid), "修改仓库从" + oldname + "修改为" + depotname, adminUser.getUserid(), adminUser.getUsername());
/*      */   }
/*      */   
/* 1280 */   public void savePayType(int orderid, int paytypeid) { PayCfg cfg = this.paymentManager.get(Integer.valueOf(paytypeid));
/* 1281 */     String typename = cfg.getName();
/* 1282 */     String paytype = cfg.getType();
/* 1283 */     this.daoSupport.execute("update es_order set payment_id=?,payment_name=?,payment_type=? where order_id=?", new Object[] { Integer.valueOf(paytypeid), typename, paytype, Integer.valueOf(orderid) });
/*      */   }
/*      */   
/* 1286 */   public void saveShipType(int orderid, int shiptypeid) { String typename = this.dlyTypeManager.getDlyTypeById(Integer.valueOf(shiptypeid)).getName();
/* 1287 */     this.daoSupport.execute("update es_order set shipping_id=?,shipping_type=? where order_id=?", new Object[] { Integer.valueOf(shiptypeid), typename, Integer.valueOf(orderid) });
/*      */   }
/*      */   
/*      */   public void add(Order order)
/*      */   {
/* 1292 */     this.baseDaoSupport.insert("es_order", order);
/*      */   }
			public int checkAgentUser(int userid) {
				String sql = "select count(0) from adminuser where userid=? and isagent='1'";
				  int count = this.baseDaoSupport.queryForInt(sql, new Object[] { userid });
				  count = count > 0 ? 1 : 0;
				  return count;
			}
/*      */ }