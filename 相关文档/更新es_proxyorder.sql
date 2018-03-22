-- 更新商品id
update es_proxyorder po set po.goodsId=(select t.goods_id from es_ticketdetail t where t.id=po.ticketId);
-- 更新售价
update es_proxyorder po set po.price=(select g.price from es_goods g where g.goods_id = po.goodsId);
-- 更新售出时间
update es_proxyorder po set po.soldTime=(select o.create_time from es_order_items oi,es_order o where oi.item_id=po.itemId and oi.order_id=o.order_id);
-- 更新代理价格
update es_proxyorder po set po.proxyPrice=(select goldPrice g from es_goods_agent g where g.goodsId=po.goodsId);
-- 更新代理商
-- update es_proxyorder po set po.proxyMemberId=(select oi.addon from es_order_items oi where oi.item_id=po.itemId);
-- 未找到代理上的用随机id
update es_proxyorder po set po.proxyMemberId=(select memberId from es_credit  order by rand() LIMIT 1) where po.proxyMemberId is null;
-- 查询
select c.*,count(0) as saleNum,sum(po.price) as price,sum(po.proxyPrice) as proxyPrice 
from es_proxyorder po,es_credit c where po.proxyMemberId=c.memberId group by c.memberId;