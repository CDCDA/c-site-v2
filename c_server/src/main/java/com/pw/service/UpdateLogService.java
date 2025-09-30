package com.pw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pw.domain.UpdateLog;
import com.pw.vo.UpdateLogCountVO;

import java.util.List;


/***
 * @author cyd
 * @date 2023/5/18 17:39
 * @description <更新日志接口>
 **/

public interface UpdateLogService extends IService<UpdateLog> {
    List<UpdateLogCountVO> countUpdateLogByDateRange(String startTime, String endTime);
}
