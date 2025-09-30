package com.pw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pw.domain.DramaSeries;
import com.pw.vo.DramaTypeCountVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository//代表持久层
public interface DramaSeriesMapper extends BaseMapper<DramaSeries> {
    List<DramaTypeCountVO> countDramaByType(Long userId);
}
