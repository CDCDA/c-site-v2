package com.pw.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pw.domain.Essay;
import com.pw.dto.BlogPageDTO;
import com.pw.dto.EssayDTO;
import com.pw.vo.BlogVO;
import com.pw.vo.EssayCountVO;
import com.pw.vo.EssayVO;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;


/***
 * @author cyd
 * @date 2023/5/18 17:39
 * @description <随笔>UserService
 **/

public interface EssayService extends IService<Essay> {
    IPage<EssayVO> page(IPage<EssayDTO> page, @Param("param") EssayDTO param);

    EssayVO getEssayById(String essayId);

    Integer count(EssayDTO essayDTO);

    List<EssayCountVO> countEssayByDateRange(Long userId, String startTime, String endTime);

}
