package com.pw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pw.domain.DictData;
import org.springframework.stereotype.Repository;

@Repository//代表持久层
public interface DictDataMapper extends BaseMapper<DictData> {

}
