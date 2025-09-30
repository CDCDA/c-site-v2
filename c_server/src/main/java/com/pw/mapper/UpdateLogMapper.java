package com.pw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pw.domain.UpdateLog;
import com.pw.vo.UpdateLogCountVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository//代表持久层
public interface UpdateLogMapper extends BaseMapper<UpdateLog> {

    List<UpdateLogCountVO> countUpdateLogByDateRange(String startTime, String endTime);
}
