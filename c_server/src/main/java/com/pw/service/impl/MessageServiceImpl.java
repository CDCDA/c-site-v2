package com.pw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.domain.Game;
import com.pw.domain.Message;
import com.pw.mapper.GameMapper;
import com.pw.mapper.MessageMapper;
import com.pw.service.GameService;
import com.pw.service.MessageService;
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
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

}
