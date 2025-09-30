package com.pw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.domain.Cate;
import com.pw.mapper.CateMapper;
import com.pw.service.CateService;
import org.springframework.stereotype.Service;

/***
 * @author cyd
 * @date 2023/5/18 17:52
 * @description <>
 **/
@Service
public class CateServiceImpl extends ServiceImpl<CateMapper, Cate> implements CateService {
}
