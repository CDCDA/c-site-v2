package com.pw.controller;

import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import static com.pw.common.utils.ResultUtil.*;

@RestController
@Slf4j
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

        String dirPath = "/home/docker/nginx/static-files" + path;
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        String filePath = dirPath + "/" + fileName;
        log.info("上传文件：{}，路径：{}", fileName, filePath);
        Path dest = Paths.get(filePath);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
        }

        InetAddress address = InetAddress.getLocalHost();
        String host = address.getHostAddress();
        return resultData("https://cccc1203.top/file" + path + "/" + fileName);
    }
}
