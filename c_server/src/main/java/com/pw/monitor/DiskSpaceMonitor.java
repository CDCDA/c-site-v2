package com.pw.monitor;

/***
 * @author cyd
 * @date 2025/10/27 11:09
 * @description <>
 **/

import com.pw.domain.DiskInfo;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.DecimalFormat;

@Component
public class DiskSpaceMonitor {

    private static final long GB = 1024 * 1024 * 1024;
    private static final double WARNING_THRESHOLD = 0.3; // 10% 阈值

    public DiskInfo getDiskInfo(String path) {
        File file = new File(path);
        long totalSpace = file.getTotalSpace();
        long freeSpace = file.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;
        double usageRate = (double) usedSpace / totalSpace;

        return new DiskInfo(
                path,
                formatSize(totalSpace),
                formatSize(freeSpace),
                formatSize(usedSpace),
                String.format("%.2f%%", usageRate * 100),
                usageRate > WARNING_THRESHOLD
        );
    }

    public boolean isDiskSpaceLow(String path) {
        File file = new File(path);
        long totalSpace = file.getTotalSpace();
        long freeSpace = file.getFreeSpace();
        double freeRate = (double) freeSpace / totalSpace;
        return freeRate <= WARNING_THRESHOLD;
    }

    private String formatSize(long size) {
        if (size >= GB) {
            return new DecimalFormat("#.##").format((double) size / GB) + " GB";
        } else {
            return new DecimalFormat("#.##").format((double) size / (1024 * 1024)) + " MB";
        }
    }

}
