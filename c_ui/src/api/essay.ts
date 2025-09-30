/*
 * @Description: 随笔
 */
import request from '@/utils/request';

// 查询随笔数
export const countEssays = () =>
  request({
    url: '/essays/count',
    method: 'get'
  });

// 查询随笔列表
export const pageEssays = (params: any) =>
  request({
    url: '/essays',
    method: 'get',
    params
  });

//根据日期范围查询随笔统计数据
export const countEssayByDate = (params: any) =>
  request({
    url: '/essays/countEssayByDate',
    method: 'get',
    params
  });

// 保存随笔
export const saveEssay = (params: any) =>
  request({
    url: '/essays',
    method: 'post',
    data: params
  });

// 保存随笔
export const updateEssay = (params: any) =>
  request({
    url: `/essays/${params.id}`,
    method: 'put',
    data: params
  });

// 批量删除随笔
export const deleteEssays = (params: any) =>
  request({
    url: '/essays/batch-delete',
    method: 'post',
    data: params
  });

// 根据随笔id查询随笔
export const getEssayById = (id: any) =>
  request({
    url: '/essays/' + id,
    method: 'get'
  });
