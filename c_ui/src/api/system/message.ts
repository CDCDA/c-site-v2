/*
 * @Description: 更新日志
 */
import request from '@/utils/request';

// 数量
export const countMessages = () =>
  request({
    url: '/messages/count',
    method: 'get'
  });

export const listUserMessages = () =>
  request({
    url: '/messages/listUserMessages',
    method: 'get'
  });

// 分页
export const pageMessages = (params: any) =>
  request({
    url: '/messages',
    method: 'get',
    params
  });

// 按时间查询
export const countMessagesByDate = (params: any) =>
  request({
    url: '/messages/stats/date-range',
    method: 'get',
    params
  });

//批量删除
export const deleteMessages = (ids: any) =>
  request({
    url: '/messages/batch-delete',
    method: 'delete',
    data: ids
  });

// 保存
export const saveMessage = (data: any) =>
  request({
    url: '/messages',
    method: 'post',
    data
  });

// 更新
export const updateMessage = (id: string, data: any) =>
  request({
    url: '/messages/' + id,
    method: 'put',
    data
  });

// 修改已读状态
export const batchReadMessages = (ids: any) =>
  request({
    url: '/messages/batch-read',
    method: 'put',
    data: ids
  });

// 根据id查询
export const getMessageById = (id: string) =>
  request({
    url: '/messages/' + id,
    method: 'get'
  });
