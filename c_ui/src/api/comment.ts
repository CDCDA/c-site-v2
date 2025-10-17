/*
 * @Description:评论
 */
import request from '@/utils/request';

// 查询评论数
export const countComments = () =>
  request({
    url: '/comments/count',
    method: 'get'
  });

// 查询评论列表
export const pageComments = (params: any) =>
  request({
    url: '/comments',
    method: 'get',
    params
  });

// 查询树形评论列表
export const listTreeComments = (params: any) =>
  request({
    url: '/comments/tree',
    method: 'get',
    params
  });

// 根据id查询评论
export const getCommentById = (id: String) =>
  request({
    url: '/comments/' + id,
    method: 'get'
  });

// 保存评论
export const saveComment = (params: any) =>
  request({
    url: '/comments',
    method: 'post',
    data: params
  });

// 修改评论
export const updateComment = (params: any) =>
  request({
    url: '/comments/' + params.id,
    method: 'put',
    data: params
  });

// 批量删除评论
export const deleteComments = (ids: any) =>
  request({
    url: '/comments/batch-delete',
    method: 'delete',
    data: ids
  });
