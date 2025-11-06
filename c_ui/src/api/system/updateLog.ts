/*
 * @Description: 更新日志
 */
import request from '@/utils/request';

// 数量
export const countLogs = () =>
  request({
    url: '/update-logs/count',
    method: 'get'
  });

// 分页
export const pageLogs = (params: any) =>
  request({
    url: '/update-logs',
    method: 'get',
    params
  });

// 按时间查询
export const countLogsByDate = (params: any) =>
  request({
    url: '/update-logs/stats/date-range',
    method: 'get',
    params
  });

//批量删除
export const deleteLogs = (ids: any) =>
  request({
    url: '/update-logs/batch-delete',
    method: 'delete',
    data: ids
  });

// 保存
export const saveLog = (data: any) =>
  request({
    url: '/update-logs',
    method: 'post',
    data
  });

// 更新
export const updateLog = (data: any) =>
  request({
    url: '/update-logs/' + data.id,
    method: 'put',
    data
  });

// 根据id查询
export const getLogById = (id: string) =>
  request({
    url: '/update-logs/' + id,
    method: 'get'
  });
