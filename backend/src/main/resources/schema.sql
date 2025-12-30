SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `seckill_order`;
DROP TABLE IF EXISTS `seckill_activity`;
DROP TABLE IF EXISTS `goods`;
DROP TABLE IF EXISTS `sys_user`;
SET FOREIGN_KEY_CHECKS=1;

CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'User ID',
  `username` varchar(50) NOT NULL COMMENT 'Username',
  `password` varchar(100) NOT NULL COMMENT 'Password (Encrypted)',
  `nickname` varchar(50) DEFAULT NULL COMMENT 'Nickname',
  `balance` decimal(10,2) DEFAULT 10000.00 COMMENT 'Wallet Balance',
  `role` int(11) DEFAULT 0 COMMENT 'Role: 0-User, 1-Admin',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Create Time',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update Time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User Table';

CREATE TABLE `goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Goods ID',
  `name` varchar(100) NOT NULL COMMENT 'Goods Name',
  `title` varchar(200) DEFAULT NULL COMMENT 'Goods Title/Subtitle',
  `img` varchar(255) DEFAULT NULL COMMENT 'Image URL',
  `detail` text COMMENT 'Goods Detail',
  `category` varchar(50) DEFAULT NULL COMMENT 'Category',
  `price` decimal(10,2) NOT NULL COMMENT 'Original Price',
  `stock` int(11) NOT NULL DEFAULT 0 COMMENT 'Stock',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Goods Table';

CREATE TABLE `seckill_activity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Activity ID',
  `goods_id` bigint(20) NOT NULL COMMENT 'Goods ID',
  `seckill_price` decimal(10,2) NOT NULL COMMENT 'Seckill Price',
  `stock_count` int(11) NOT NULL COMMENT 'Seckill Stock',
  `start_time` datetime NOT NULL COMMENT 'Start Time',
  `end_time` datetime NOT NULL COMMENT 'End Time',
  `status` int(11) DEFAULT 1 COMMENT 'Status: 0-Disable, 1-Enable',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Seckill Activity Table';

CREATE TABLE `seckill_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Order ID',
  `user_id` bigint(20) NOT NULL COMMENT 'User ID',
  `activity_id` bigint(20) NOT NULL COMMENT 'Activity ID',
  `goods_id` bigint(20) NOT NULL COMMENT 'Goods ID',
  `order_no` varchar(64) NOT NULL COMMENT 'Order Number',
  `pay_amount` decimal(10,2) NOT NULL COMMENT 'Pay Amount',
  `status` int(11) DEFAULT 0 COMMENT 'Status: 0-Unpaid, 1-Paid, 2-Shipped, 3-Completed, -1-Cancelled',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `pay_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  UNIQUE KEY `uk_user_activity` (`user_id`, `activity_id`) COMMENT 'One order per user per activity'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Seckill Order Table';


