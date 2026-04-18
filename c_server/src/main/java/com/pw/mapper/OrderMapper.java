package com.pw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pw.domain.Order;
import org.springframework.stereotype.Repository;

/**
 * 订单Mapper接口
 *
 * @author cyd
 * @date 2026/04/07
 * @description 订单数据访问层
 */
@Repository
public interface OrderMapper extends BaseMapper<Order> {
}
