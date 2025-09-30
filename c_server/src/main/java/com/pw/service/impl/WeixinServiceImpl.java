package com.pw.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pw.common.utils.WeixinUtils;
import com.pw.domain.WeixinTemplate;
import com.pw.service.WeixinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/***
 * @author cyd
 * @date 2023/5/18 17:52
 * @description <>
 **/
@Service
public class WeixinServiceImpl implements WeixinService {

    @Autowired RestTemplate restTemplate;
    @Override
    public List<WeixinTemplate> getTemplateList() {
        String url = String.format("https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?" +
                "access_token=%s", WeixinUtils.getToken());
        String result = String.valueOf(restTemplate.getForEntity(url, String.class));
        JSONObject obj = JSONObject.parseObject(result);
        List<WeixinTemplate> templateList = JSON.parseArray(obj.getString("template_list"), WeixinTemplate.class);
        return  templateList;
    }
}
