package com.pw.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.domain.Essay;
import com.pw.dto.EssayDTO;
import com.pw.mapper.EssayMapper;
import com.pw.service.EssayService;
import com.pw.vo.EssayCountVO;
import com.pw.vo.EssayVO;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EssayServiceImpl extends ServiceImpl<EssayMapper, Essay> implements EssayService {

    @Autowired
    EssayMapper essayMapper;


    @Override
    public IPage<EssayVO> page(IPage<EssayDTO> page, EssayDTO param) {
        return essayMapper.page(page, param);
    }

    @Override
    public EssayVO getEssayById(Long essayId) {
        return essayMapper.getEssayById(essayId);
    }

    @Override
    public Integer count(EssayDTO essayDTO) {
        return essayMapper.count(essayDTO);
    }

    @Override
    @Transactional
    public List<EssayCountVO> countEssayByDateRange(Long userId, String startTime, String endTime) {
        return essayMapper.countEssayByDateRange(userId, startTime, endTime);
    }
}
