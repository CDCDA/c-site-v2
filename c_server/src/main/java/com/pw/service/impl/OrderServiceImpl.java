package com.pw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.domain.Order;
import com.pw.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
 * 订单服务实现类
 *
 * @author cyd
 * @date 2026/04/07
 * @description 订单服务实现，提供批量操作支持
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> {
}
