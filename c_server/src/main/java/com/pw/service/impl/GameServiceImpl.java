package com.pw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.domain.Game;
import com.pw.mapper.GameMapper;
import com.pw.service.GameService;
import com.pw.vo.GameTypeCountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * @author cyd
 * @date 2023/5/18 17:52
 * @description <>
 **/
@Service
public class GameServiceImpl extends ServiceImpl<GameMapper, Game> implements GameService {

    @Autowired
    private GameMapper gameMapper;
    @Override
    public List<GameTypeCountVO> countGameByType(Long userId) {
        return gameMapper.countGameByType(userId);
    }
}
