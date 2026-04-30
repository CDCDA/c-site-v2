package com.pw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pw.domain.ReadWriteTest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 读写分离测试Mapper接口
 *
 * @author cyd
 * @date 2026/04/24
 * @description 读写分离测试数据访问层
 */
@Mapper
public interface ReadWriteTestMapper extends BaseMapper<ReadWriteTest> {

    /**
     * 强制使用主库查询（写库）
     */
    @Select("SELECT * FROM t_read_write_test WHERE id = #{id}")
    ReadWriteTest selectByIdFromMaster(@Param("id") Long id);

    /**
     * 强制使用从库查询（读库）
     */
    @Select("SELECT * FROM t_read_write_test WHERE id = #{id}")
    ReadWriteTest selectByIdFromSlave(@Param("id") Long id);

    /**
     * 更新数据（自动走主库）
     */
    @Update("UPDATE t_read_write_test SET name = #{name}, content = #{content}, status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateByIdManual(@Param("id") Long id, @Param("name") String name, @Param("content") String content, @Param("status") Integer status);
}
