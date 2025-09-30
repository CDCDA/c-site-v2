package com.pw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pw.domain.Essay;
import com.pw.dto.EssayDTO;
import com.pw.vo.EssayCountVO;
import com.pw.vo.EssayVO;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * @author cyd
 * @date 2023/11/7 21:46
 * @description <随笔>
 **/
@Repository
public interface EssayMapper extends BaseMapper<Essay> {
    IPage<EssayVO> page(IPage<EssayDTO> page, @Param("param") EssayDTO param);

    EssayVO getEssayById(String essayId);


    Integer count(EssayDTO essayDTO);

    List<EssayCountVO> countEssayByDateRange(Long userId, String startTime, String endTime);
}
