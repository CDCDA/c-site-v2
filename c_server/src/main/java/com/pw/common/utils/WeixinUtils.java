package com.pw.common.utils;

import com.pw.common.constants.weixinConstants;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.domain.WeixinTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * @author cyd
 * @date 2024/10/11 14:28
 * @description <微信公众号相关>
 **/
@Component // 添加@Component注解，让Spring管理
public class WeixinUtils {

    private static RestTemplate restTemplate = null;
    private static ObjectMapper objectMapper = null;

    // 通过构造函数注入依赖
    public WeixinUtils(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 获取微信公众号token
     *
     * @return
     */
    public static String getToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + weixinConstants.APPID + "&secret=" + weixinConstants.SECRET;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            try {
                Map<String, Object> result = objectMapper.readValue(response.getBody(), Map.class);
                return (String) result.get("access_token");
            } catch (Exception e) {
                throw new RuntimeException("解析微信token响应失败", e);
            }
        }
        throw new RuntimeException("获取微信token失败: " + response.getStatusCode());
    }

    /**
     * 获取微信公众号消息模板列表
     *
     * @return
     */
    public static List<WeixinTemplate> getTemplateList() {
        String url = String.format("https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=%s", getToken());

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            try {
                Map<String, Object> result = objectMapper.readValue(response.getBody(), Map.class);
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> templateListMap = (List<Map<String, Object>>) result.get("template_list");

                List<WeixinTemplate> templateList = new ArrayList<>();
                for (Map<String, Object> templateMap : templateListMap) {
                    WeixinTemplate template = new WeixinTemplate();
                    template.setTemplate_id((String) templateMap.get("template_id"));
                    template.setTitle((String) templateMap.get("title"));
                    template.setContent((String) templateMap.get("content"));
                    template.setExample((String) templateMap.get("example"));
                    templateList.add(template);
                }
                return templateList;
            } catch (Exception e) {
                throw new RuntimeException("解析模板列表响应失败", e);
            }
        }
        throw new RuntimeException("获取模板列表失败: " + response.getStatusCode());
    }

    /**
     * 发送模板消息
     *
     * @return
     */
    public static String sendTemplateMessage(Map<String, Object> dataMap) {
        String url = String.format("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s", getToken());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(dataMap, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        throw new RuntimeException("发送模板消息失败: " + response.getStatusCode());
    }

    /*
       获取文本类型xml字符串
     */
    public static String getTextXml(String toUserName, String fromUserName, String content) {
        return "<xml>" +
                "<ToUserName><![CDATA[" + toUserName + "]]></ToUserName>" +
                "<FromUserName><![CDATA[" + fromUserName + "]]></FromUserName>" +
                "<CreateTime>" + System.currentTimeMillis() / 1000 + "</CreateTime>" +
                "<MsgType><![CDATA[text]]></MsgType>" +
                "<Content><![CDATA[" + content + "]]></Content>" +
                "</xml>";
    }

    /*
       解析模板参数
     */
    public static Map<String, String> parseTemplateParams(String content) {
        List<String> tempParams = Arrays.asList(content.split("\n"));
        Pattern pattern = Pattern.compile("\\{\\{(.*?)\\.DATA\\}\\}");
        Map<String, String> params = new HashMap<>();

        for (String tempParam : tempParams) {
            String[] str = tempParam.split(":");
            if (str.length >= 2) {
                Matcher matcher = pattern.matcher(str[1]);
                while (matcher.find()) {
                    params.put(str[0].trim(), matcher.group(1).trim());
                }
            }
        }
        return params;
    }
}
