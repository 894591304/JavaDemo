/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 50635
 Source Host           : localhost
 Source Database       : javamall

 Target Server Type    : MySQL
 Target Server Version : 50635
 File Encoding         : utf-8

 Date: 07/30/2017 23:29:18 PM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `es_index_item`
-- ----------------------------
DROP TABLE IF EXISTS `es_index_item`;
CREATE TABLE `es_index_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `es_index_item`
-- ----------------------------
BEGIN;
INSERT INTO `es_index_item` VALUES ('1', '站点统计信息', '/core/admin/indexItem!base.do', '1'), ('2', '订单统计信息', '/shop/b2b2c/indexItemsExt!order.do', '2'), ('3', '商品统计信息', '/shop/b2b2c/indexItemsExt!goods.do', '3'), ('4', '网红统计信息', '/shop/b2b2c/indexItemsExt!credit.do', '4'), ('5', '销售统计信息', '/shop/b2b2c/indexItemsExt!sale.do', '5');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
