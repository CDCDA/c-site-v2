package com.pw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.domain.DramaSeries;
import com.pw.mapper.DramaSeriesMapper;
import com.pw.service.DramaSeriesService;
import com.pw.vo.DramaTypeCountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * @author cyd
 * @date 2023/5/18 17:52
 * @description <>
 **/
@Service
public class DramaSeriesServiceImpl extends ServiceImpl<DramaSeriesMapper, DramaSeries> implements DramaSeriesService {

    @Autowired
    private DramaSeriesMapper dramaSeriesMapper;
    @Override
    public List<DramaTypeCountVO> countDramaByType(Long userId) {
        return dramaSeriesMapper.countDramaByType(userId);
    }
}
