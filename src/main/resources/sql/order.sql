CREATE TABLE `alipay_order` (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
                                `order_no` varchar(64) NOT NULL COMMENT '商户订单号',
                                `total_amount` decimal(10,2) NOT NULL COMMENT '订单金额',
                                `subject` varchar(256) NOT NULL COMMENT '订单标题',
                                `body` varchar(256) NOT NULL COMMENT '订单描述',
                                `status` int NOT NULL COMMENT '订单状态',
                                `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
                                `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '订单最后更新时间',
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付宝订单表';