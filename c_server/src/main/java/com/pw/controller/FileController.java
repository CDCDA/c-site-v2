package com.pw.controller;

import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import static com.pw.common.utils.ResultUtil.*;

@RestController
@Tag(name = "文件")
@RequestMapping("/files")
public class FileController extends BaseController implements convertController {

    @PostMapping
    @Operation(summary = "上传文件")
    public Result upload(@RequestParam MultipartFile file, @RequestParam String path) throws IOException {
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUID.randomUUID() + suffix;

        if (ObjectUtils.isEmpty(path)) {
            path = "";
        } else {
            path = "/" + path;
        }

        String dirPath = "/www/wwwroot/files" + path;
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        String filePath = dirPath + "/" + fileName;
        file.transferTo(new File(filePath));

        InetAddress address = InetAddress.getLocalHost();
        String host = address.getHostAddress();
        return resultData("http://120.48.127.181/file/" + path + "/" + fileName);
    }
}
