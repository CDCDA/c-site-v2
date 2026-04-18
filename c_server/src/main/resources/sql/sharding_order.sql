-- ==========================================
-- 订单表分库分表初始化脚本
-- ==========================================
-- 分库策略：user_id % 2 (0 -> C_PW, 1 -> C_PW1)
-- 分表策略：id % 2 (0 -> _0, 1 -> _1)
-- ==========================================

-- 创建 C_PW 数据库（如果不存在）
-- 注意：如果数据库已存在且包含其他表，请注释掉 CREATE DATABASE 语句
CREATE DATABASE IF NOT EXISTS `C_PW` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建 C_PW1 数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `C_PW1` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用 C_PW
USE `C_PW`;

-- 创建 C_PW.t_order_0
DROP TABLE IF EXISTS `C_PW`.`t_order_0`;
CREATE TABLE `C_PW`.`t_order_0` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` VARCHAR(64) NOT NULL COMMENT '订单号',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `product_name` VARCHAR(256) DEFAULT NULL COMMENT '商品名称',
    `amount` DECIMAL(10,2) DEFAULT NULL COMMENT '订单金额',
    `status` TINYINT(4) DEFAULT 0 COMMENT '订单状态：0待支付，1已支付，2已发货，3已完成，4已取消',
    `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(512) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表-分片0';

-- 创建 C_PW.t_order_1
DROP TABLE IF EXISTS `C_PW`.`t_order_1`;
CREATE TABLE `C_PW`.`t_order_1` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` VARCHAR(64) NOT NULL COMMENT '订单号',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `product_name` VARCHAR(256) DEFAULT NULL COMMENT '商品名称',
    `amount` DECIMAL(10,2) DEFAULT NULL COMMENT '订单金额',
    `status` TINYINT(4) DEFAULT 0 COMMENT '订单状态：0待支付，1已支付，2已发货，3已完成，4已取消',
    `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(512) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表-分片1';

-- 使用 C_PW1
USE `C_PW1`;

-- 创建 C_PW1.t_order_0
DROP TABLE IF EXISTS `C_PW1`.`t_order_0`;
CREATE TABLE `C_PW1`.`t_order_0` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` VARCHAR(64) NOT NULL COMMENT '订单号',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `product_name` VARCHAR(256) DEFAULT NULL COMMENT '商品名称',
    `amount` DECIMAL(10,2) DEFAULT NULL COMMENT '订单金额',
    `status` TINYINT(4) DEFAULT 0 COMMENT '订单状态：0待支付，1已支付，2已发货，3已完成，4已取消',
    `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(512) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表-分片0';

-- 创建 C_PW1.t_order_1
DROP TABLE IF EXISTS `C_PW1`.`t_order_1`;
CREATE TABLE `C_PW1`.`t_order_1` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` VARCHAR(64) NOT NULL COMMENT '订单号',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `product_name` VARCHAR(256) DEFAULT NULL COMMENT '商品名称',
    `amount` DECIMAL(10,2) DEFAULT NULL COMMENT '订单金额',
    `status` TINYINT(4) DEFAULT 0 COMMENT '订单状态：0待支付，1已支付，2已发货，3已完成，4已取消',
    `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(512) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表-分片1';
