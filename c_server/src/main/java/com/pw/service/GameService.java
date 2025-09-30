package com.pw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pw.domain.Game;
import com.pw.vo.GameTypeCountVO;

import java.util.List;

/***
 * @author cyd
 * @date 2023/5/18 17:51
 * @description <壁纸接口>
 **/
public interface GameService extends IService<Game> {
    List<GameTypeCountVO> countGameByType(Long userId);
}
