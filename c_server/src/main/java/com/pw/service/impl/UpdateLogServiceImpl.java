package com.pw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.domain.UpdateLog;
import com.pw.mapper.UpdateLogMapper;
import com.pw.service.UpdateLogService;
import com.pw.vo.UpdateLogCountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * @author cyd
 * @date 2023/5/18 17:52
 * @description <>
 **/
@Service
public class UpdateLogServiceImpl extends ServiceImpl<UpdateLogMapper, UpdateLog> implements UpdateLogService {

    @Autowired
    UpdateLogMapper updateLogMapper;

    public List<UpdateLogCountVO> countUpdateLogByDateRange(String startTime, String endTime) {
        return updateLogMapper.countUpdateLogByDateRange(startTime, endTime);
    }
}
