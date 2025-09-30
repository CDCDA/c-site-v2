package com.pw.service;

import com.pw.domain.WeixinTemplate;

import java.io.IOException;
import java.util.List;

/***
 * @author cyd
 * @date 2023/5/18 17:51
 * @description <微信公众号接口>
 **/
public interface WeixinService {


    /**
     * 获取模板列表
     * @param
     * @param
     * @throws IOException
     */
    public List<WeixinTemplate> getTemplateList ();

}
