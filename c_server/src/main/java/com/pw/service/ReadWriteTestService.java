package com.pw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pw.domain.ReadWriteTest;

/**
 * 读写分离测试服务接口
 *
 * @author cyd
 * @date 2026/04/24
 * @description 读写分离测试服务接口
 */
public interface ReadWriteTestService extends IService<ReadWriteTest> {

    /**
     * 插入数据（走主库）
     */
    ReadWriteTest insertToMaster(ReadWriteTest entity);

    /**
     * 从主库查询
     */
    ReadWriteTest selectFromMaster(Long id);

    /**
     * 从从库查询
     */
    ReadWriteTest selectFromSlave(Long id);

    /**
     * 更新数据（走主库）
     */
    boolean updateByIdManual(ReadWriteTest entity);

    /**
     * 批量插入测试数据
     */
    String batchInsert(int count);
}
