package com.pw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pw.domain.Equipment;
import org.springframework.stereotype.Repository;

@Repository//代表持久层
public interface EquipmentMapper extends BaseMapper<Equipment> {

}
