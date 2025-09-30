package com.pw.common.mybatisPlus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.pw.common.utils.EmptyJugeUtil;
import com.pw.common.utils.JwtTokenUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * 自动填充创建人，创建时间，更新人，更新时间
 */

@Component
public class MybatisHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        Long userId = JwtTokenUtil.getLoginUserId();
        if (!EmptyJugeUtil.isEmpty(userId) && !"0".equals(userId)) {
            this.setFieldValByName("createBy", userId, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
        Long userId = JwtTokenUtil.getLoginUserId();
        if (!ObjectUtils.isEmpty(userId) && !"0".equals(userId)) {
            this.setFieldValByName("updateBy", userId, metaObject);
        }
    }
}
