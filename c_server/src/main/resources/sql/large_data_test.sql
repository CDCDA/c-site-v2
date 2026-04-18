-- ================================
-- 大批量数据测试表
-- ================================

-- 订单表（分库分表测试）
DROP TABLE IF EXISTS t_order;
CREATE TABLE t_order (
    id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID，自增主键',
    order_no varchar(32) NOT NULL COMMENT '订单号（业务唯一键）',
    user_id bigint(20) NOT NULL COMMENT '用户ID（分片键候选）',
    product_name varchar(128) DEFAULT NULL COMMENT '商品名称',
    amount decimal(12,2) NOT NULL COMMENT '订单金额',
    status tinyint(4) NOT NULL DEFAULT '0' COMMENT '订单状态：0待支付，1已支付，2已发货，3已完成，4已取消',
    pay_time datetime DEFAULT NULL COMMENT '支付时间',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark varchar(255) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_user_id (user_id),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表（分库分表测试）';

-- 用户表
DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
    id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    user_name varchar(64) NOT NULL COMMENT '用户名',
    email varchar(128) DEFAULT NULL COMMENT '邮箱',
    phone varchar(20) DEFAULT NULL COMMENT '手机号',
    register_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_name (user_name),
    KEY idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
