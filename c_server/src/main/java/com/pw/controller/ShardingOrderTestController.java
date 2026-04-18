package com.pw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pw.domain.Order;
import com.pw.mapper.OrderMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 分库分表订单测试控制器
 *
 * @author cyd
 * @date 2026/04/08
 * @description 测试 t_order 表的分库分表功能
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/sharding/order")
@Tag(name = "分库分表订单测试", description = "测试订单表分库分表功能")
public class ShardingOrderTestController {

    private final OrderMapper orderMapper;

    /**
     * 创建订单（测试分库分表）
     * 分库：user_id % 2
     * 分表：id % 2
     */
    @PostMapping("/create")
    @Operation(summary = "创建订单", description = "测试分库分表插入，根据 user_id 和 id 自动路由到对应库表")
    public String createOrder(
            @RequestParam Long userId,
            @RequestParam String orderNo,
            @RequestParam String productName,
            @RequestParam BigDecimal amount) {

        Order order = new Order();
        order.setUserId(userId);
        order.setOrderNo(orderNo);
        order.setProductName(productName);
        order.setAmount(amount);
        order.setStatus(0); // 待支付
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());

        int result = orderMapper.insert(order);

        if (result > 0) {
            log.info("订单创建成功，ID: {}, userId: {}, orderNo: {}, 路由到: db{}_t_order_{}",
                    order.getId(), userId, orderNo, userId % 2, order.getId() % 2);
            return String.format("订单创建成功！\n订单ID: %d\n订单号: %s\n用户ID: %d\n分库: db%d\n分表: t_order_%d\n(根据 userId=%d %% 2, id=%d %% 2 计算)",
                    order.getId(), orderNo, userId, userId % 2, order.getId() % 2, userId, order.getId());
        } else {
            return "订单创建失败";
        }
    }

    /**
     * 根据订单ID查询（测试分表路由）
     */
    @GetMapping("/getById")
    @Operation(summary = "根据ID查询订单", description = "测试分表路由，根据 id 自动查询对应表")
    public String getOrderById(@RequestParam Long id) {
        Order order = orderMapper.selectById(id);
        if (order != null) {
            return String.format("查询成功！\n订单ID: %d\n订单号: %s\n用户ID: %d\n商品: %s\n金额: %s\n状态: %d\n分库: db%d\n分表: t_order_%d",
                    order.getId(), order.getOrderNo(), order.getUserId(),
                    order.getProductName(), order.getAmount().toString(),
                    order.getStatus(), order.getUserId() % 2, order.getId() % 2);
        } else {
            return "订单不存在，ID: " + id;
        }
    }

    /**
     * 根据用户ID查询订单（测试分库路由）
     */
    @GetMapping("/getByUserId")
    @Operation(summary = "根据用户ID查询订单", description = "测试分库路由，根据 user_id 自动查询对应数据库")
    public List<Order> getOrdersByUserId(@RequestParam Long userId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        List<Order> orders = orderMapper.selectList(wrapper);
        log.info("用户ID: {}, 查询到 {} 条订单，应该都在 db{}",
                userId, orders.size(), userId % 2);
        return orders;
    }

    /**
     * 更新订单状态
     */
    @PutMapping("/updateStatus")
    @Operation(summary = "更新订单状态", description = "测试分库分表更新")
    public String updateOrderStatus(@RequestParam Long id, @RequestParam Integer status) {
        Order order = new Order();
        order.setId(id);
        order.setStatus(status);
        order.setUpdateTime(new Date());

        int result = orderMapper.updateById(order);
        if (result > 0) {
            return String.format("订单状态更新成功！\n订单ID: %d\n新状态: %d\n(0待支付,1已支付,2已发货,3已完成,4已取消)", id, status);
        } else {
            return "更新失败，订单不存在";
        }
    }

    /**
     * 批量创建测试订单
     */
    @PostMapping("/batchCreate")
    @Operation(summary = "批量创建测试订单", description = "创建多个订单测试分库分表")
    public String batchCreateTestOrders(
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(defaultValue = "1") Long startUserId) {

        StringBuilder sb = new StringBuilder();
        sb.append("批量创建订单开始...\n\n");

        for (int i = 0; i < count; i++) {
            Long userId = startUserId + i;
            String orderNo = "ORD" + System.currentTimeMillis() + i;

            Order order = new Order();
            order.setUserId(userId);
            order.setOrderNo(orderNo);
            order.setProductName("测试商品-" + (i + 1));
            order.setAmount(new BigDecimal("99.99"));
            order.setStatus(0);
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());

            int result = orderMapper.insert(order);
            if (result > 0) {
                sb.append(String.format("订单 %d: ID=%d, userId=%d -> db%d, t_order_%d\n",
                        i + 1, order.getId(), userId, userId % 2, order.getId() % 2));
            }
        }

        sb.append("\n批量创建完成！共 ").append(count).append(" 条订单");
        return sb.toString();
    }
}
