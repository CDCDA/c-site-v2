package com.pw.controller;

import com.pw.domain.ReadWriteTest;
import com.pw.service.ReadWriteTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

/**
 * 读写分离测试控制器
 *
 * @author cyd
 * @date 2026/04/24
 * @description 测试读写分离功能
 * <p>
 * 配置说明：
 * - 主库 (C_PW): db_0，用于写入操作
 * - 从库 (C_PW2): db_2，用于读取操作
 * - 读写分离组: master_slave
 * </p>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/readwrite")
@Tag(name = "读写分离测试", description = "测试主从库读写分离功能")
public class ReadWriteTestController {

    private final ReadWriteTestService readWriteTestService;

    /**
     * 创建测试数据（走主库）
     */
    @PostMapping("/create")
    @Operation(summary = "创建数据", description = "向主库插入数据，测试写操作")
    public String create(
            @Parameter(description = "名称") @RequestParam String name,
            @Parameter(description = "内容") @RequestParam String content,
            @Parameter(description = "状态") @RequestParam(defaultValue = "0") Integer status) {

        ReadWriteTest entity = new ReadWriteTest();
        entity.setName(name);
        entity.setContent(content);
        entity.setStatus(status);

        ReadWriteTest result = readWriteTestService.insertToMaster(entity);

        return String.format("创建成功！\n数据ID: %d\n名称: %s\n内容: %s\n状态: %d\n写入数据库: C_PW (主库)",
                result.getId(), result.getName(), result.getContent(), result.getStatus());
    }

    /**
     * 从主库查询（强制读主库）
     */
    @GetMapping("/read/master")
    @Operation(summary = "主库读取", description = "强制从主库读取数据，测试主库读操作")
    public String readFromMaster(@Parameter(description = "数据ID") @RequestParam Long id) {
        ReadWriteTest result = readWriteTestService.selectFromMaster(id);
        if (result != null) {
            return String.format("主库查询成功！\nID: %d\n名称: %s\n内容: %s\n状态: %d\n创建时间: %s\n读取数据库: C_PW (主库)",
                    result.getId(), result.getName(), result.getContent(), result.getStatus(), result.getCreateTime());
        } else {
            return "数据不存在，ID: " + id;
        }
    }

    /**
     * 从从库查询（强制读从库）
     */
    @GetMapping("/read/slave")
    @Operation(summary = "从库读取", description = "强制从从库读取数据，测试从库读操作")
    public String readFromSlave(@Parameter(description = "数据ID") @RequestParam Long id) {
        ReadWriteTest result = readWriteTestService.selectFromSlave(id);
        if (result != null) {
            return String.format("从库查询成功！\nID: %d\n名称: %s\n内容: %s\n状态: %d\n创建时间: %s\n读取数据库: C_PW2 (从库)",
                    result.getId(), result.getName(), result.getContent(), result.getStatus(), result.getCreateTime());
        } else {
            return "数据不存在，ID: " + id + "\n\n注意：从库数据需要等待主从同步后才会显示";
        }
    }

    /**
     * 自动路由查询（默认读从库）
     */
    @GetMapping("/read/auto")
    @Operation(summary = "自动路由读取", description = "自动路由到从库读取数据")
    public String readAuto(@Parameter(description = "数据ID") @RequestParam Long id) {
        ReadWriteTest result = readWriteTestService.getById(id);
        if (result != null) {
            return String.format("自动路由查询成功！\nID: %d\n名称: %s\n内容: %s\n状态: %d\n创建时间: %s",
                    result.getId(), result.getName(), result.getContent(), result.getStatus(), result.getCreateTime());
        } else {
            return "数据不存在，ID: " + id;
        }
    }

    /**
     * 更新数据（走主库）
     */
    @PutMapping("/update")
    @Operation(summary = "更新数据", description = "更新主库数据，测试更新操作")
    public String update(
            @Parameter(description = "数据ID") @RequestParam Long id,
            @Parameter(description = "名称") @RequestParam String name,
            @Parameter(description = "内容") @RequestParam String content,
            @Parameter(description = "状态") @RequestParam Integer status) {

        ReadWriteTest entity = new ReadWriteTest();
        entity.setId(id);
        entity.setName(name);
        entity.setContent(content);
        entity.setStatus(status);

        boolean success = readWriteTestService.updateByIdManual(entity);

        if (success) {
            return String.format("更新成功！\n数据ID: %d\n名称: %s\n内容: %s\n状态: %d\n更新数据库: C_PW (主库)", id, name, content, status);
        } else {
            return "更新失败，数据不存在，ID: " + id;
        }
    }

    /**
     * 批量创建测试数据
     */
    @PostMapping("/batch/create")
    @Operation(summary = "批量创建测试数据", description = "批量插入测试数据用于读写分离验证")
    public String batchCreate(@Parameter(description = "数量") @RequestParam(defaultValue = "10") int count) {
        return readWriteTestService.batchInsert(count);
    }

    /**
     * 查询所有数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有数据", description = "查询所有测试数据")
    public List<ReadWriteTest> listAll() {
        return readWriteTestService.list();
    }

    /**
     * 删除数据
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除数据", description = "删除测试数据")
    public String delete(@Parameter(description = "数据ID") @RequestParam Long id) {
        boolean success = readWriteTestService.removeById(id);
        return success ? "删除成功，ID: " + id : "删除失败，ID: " + id;
    }

    /**
     * 获取配置信息
     */
    @GetMapping("/config")
    @Operation(summary = "获取配置信息", description = "查看当前读写分离配置")
    public String getConfig() {
        return """
                ========================================
                读写分离配置信息
                ========================================
                
                数据源配置:
                - 主库 (写): db_0 -> jdbc:mysql://120.48.127.181:3306/C_PW
                - 从库 (读): db_2 -> jdbc:mysql://120.48.127.181:3306/C_PW2
                
                读写分离组: master_slave
                
                路由规则:
                - 写操作 (INSERT/UPDATE/DELETE): 自动路由到主库 (C_PW)
                - 读操作 (SELECT): 默认路由到从库 (C_PW2)
                
                测试接口:
                - POST   /readwrite/create        - 创建数据 (主库写入)
                - GET    /readwrite/read/master   - 主库读取
                - GET    /readwrite/read/slave    - 从库读取
                - GET    /readwrite/read/auto     - 自动路由读取
                - PUT    /readwrite/update        - 更新数据 (主库)
                - DELETE /readwrite/delete        - 删除数据 (主库)
                - GET    /readwrite/list          - 查询所有
                - POST   /readwrite/batch/create  - 批量创建
                
                ========================================
                """;
    }
}
