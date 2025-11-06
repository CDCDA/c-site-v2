package com.pw.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "磁盘信息")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DiskInfo {

    @Schema(description = "磁盘路径")
    private String diskPath;

    @Schema(description = "总空间")
    private String totalSpace;

    @Schema(description = "空闲空间")
    private String freeSpace;

    @Schema(description = "已占用空间")
    private String usedSpace;

    @Schema(description = "已占用百分比")
    private String usageRate;

    @Schema(description = "是否低空间")
    private boolean isLowSpace;

    public DiskInfo(String path, String totalSpace, String freeSpace, String usedSpace, String usageRate, boolean isLowSpace) {
        this.diskPath = path;
        this.totalSpace = totalSpace;
        this.freeSpace = freeSpace;
        this.usedSpace = usedSpace;
        this.usageRate = usageRate;
        this.isLowSpace = isLowSpace;
    }
}
