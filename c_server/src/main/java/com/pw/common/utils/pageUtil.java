package com.pw.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pw.common.entity.BaseEntity;
import org.apache.commons.lang3.ObjectUtils;

/***
 * @author cyd
 * @date 2023/5/19 17:55
 * @description <分页处理工具类>
 **/
public class pageUtil {
    public static <T extends BaseEntity> Page setPage(T entity) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (entity.getPageNum() != null && !entity.getPageNum().equals("")) {
            pageNum = entity.getPageNum();
        }
        if (entity.getPageSize() != null && !entity.getPageSize().equals("")) {
            pageSize = entity.getPageSize();
        }
        Page page = new Page(pageNum, pageSize);
        if (ObjectUtils.isEmpty(entity.getOrderBy())) {
            if (ObjectUtils.isEmpty(entity.getAsc())) {
                page.addOrder(OrderItem.desc("create_time"));
            } else {
                page.addOrder(OrderItem.asc("create_time"));
            }
        } else {
            if (ObjectUtils.isEmpty(entity.getAsc())) {
                page.addOrder(OrderItem.desc(entity.getOrderBy()));
            } else {
                page.addOrder(OrderItem.asc(entity.getOrderBy()));
            }
        }
        return page;
    }
}
