import request from '@/utils/request';

// 分页
export const pageUsers = (params: any) =>
  request({
    url: '/users',
    method: 'get',
    params
  });

//用户详情
export const getUserById = (id: string) =>
  request({
    url: '/users/' + id,
    method: 'get'
  });

//保存用户信息
export const saveUser = (data: any) =>
  request({
    url: '/users',
    method: 'post',
    data
  });

export const updateUser = (data: any) =>
  request({
    url: '/users/' + data.id,
    method: 'put',
    data
  });

export const deleteUsers = (ids: any) =>
  request({
    url: '/users/batch-delete',
    method: 'delete',
    data: ids
  });
