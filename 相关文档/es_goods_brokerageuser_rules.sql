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

 Date: 02/08/2018 22:23:46 PM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `es_goods_brokerageuser_rules`
-- ----------------------------
DROP TABLE IF EXISTS `es_goods_brokerageuser_rules`;
CREATE TABLE `es_goods_brokerageuser_rules` (
  `goods_id` int(8) DEFAULT NULL,
  `brokerages` varchar(4000) DEFAULT NULL,
  `id` int(8) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
