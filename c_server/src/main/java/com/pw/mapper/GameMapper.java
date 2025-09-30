package com.pw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pw.domain.Game;
import com.pw.vo.GameTypeCountVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository//代表持久层
public interface GameMapper extends BaseMapper<Game> {
    List<GameTypeCountVO> countGameByType(Long userId);
}
