package com.pw.controller;

import com.alibaba.fastjson.JSON;
import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.utils.Result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@Tag(name =  "node-red物联网对接")
@RequestMapping("/nodeRed")
public class NodeRedController extends BaseController implements convertController {


    @PostMapping("httpTest")
    @Operation(summary = "")
    public Result handleValidation(@RequestBody String params) {
        Object obj = JSON.parse(params);
        System.out.println(params);
        return Result.ok(200, "测试成功");
    }

}
