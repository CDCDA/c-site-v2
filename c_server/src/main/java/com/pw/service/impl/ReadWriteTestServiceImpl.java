package com.pw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.domain.ReadWriteTest;
import com.pw.mapper.ReadWriteTestMapper;
import com.pw.service.ReadWriteTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 读写分离测试服务实现类
 *
 * @author cyd
 * @date 2026/04/24
 * @description 读写分离测试服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReadWriteTestServiceImpl extends ServiceImpl<ReadWriteTestMapper, ReadWriteTest> implements ReadWriteTestService {

    @Override
    public ReadWriteTest insertToMaster(ReadWriteTest entity) {
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        baseMapper.insert(entity);
        log.info("[主库写入] 数据插入成功，ID: {}, 写入数据库: C_PW", entity.getId());
        return entity;
    }

    @Override
    public ReadWriteTest selectFromMaster(Long id) {
        ReadWriteTest result = baseMapper.selectByIdFromMaster(id);
        log.info("[主库读取] 查询ID: {}, 结果: {}", id, result != null ? "存在" : "不存在");
        return result;
    }

    @Override
    public ReadWriteTest selectFromSlave(Long id) {
        ReadWriteTest result = baseMapper.selectByIdFromSlave(id);
        log.info("[从库读取] 查询ID: {}, 结果: {}", id, result != null ? "存在" : "不存在");
        return result;
    }

    @Override
    public boolean updateByIdManual(ReadWriteTest entity) {
        entity.setUpdateTime(new Date());
        int result = baseMapper.updateByIdManual(entity.getId(), entity.getName(), entity.getContent(), entity.getStatus());
        log.info("[主库更新] ID: {}, 结果: {}", entity.getId(), result > 0 ? "成功" : "失败");
        return result > 0;
    }

    @Override
    public String batchInsert(int count) {
        StringBuilder sb = new StringBuilder();
        sb.append("批量插入测试数据开始...\n\n");

        for (int i = 1; i <= count; i++) {
            ReadWriteTest entity = new ReadWriteTest();
            entity.setName("测试数据-" + i);
            entity.setContent("这是第 " + i + " 条测试数据，用于验证读写分离功能");
            entity.setStatus(0);
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());

            baseMapper.insert(entity);
            sb.append(String.format("插入成功: ID=%d, name=%s\n", entity.getId(), entity.getName()));
        }

        sb.append("\n批量插入完成！共 ").append(count).append(" 条数据（全部写入主库 C_PW）");
        return sb.toString();
    }
}
