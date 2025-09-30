/*
 * @Description:相册
 */
import request from '@/utils/request';

// 查询相册数
export const countAlbum = () =>
  request({
    url: '/albums/count',
    method: 'get'
  });

// 查询相册列表
export const pageAlbums = (params: any) =>
  request({
    url: '/albums',
    method: 'get',
    params
  });

// 根据id查询相册
export const getAlbumById = (params: any) =>
  request({
    url: '/albums/getAlbumById',
    method: 'get',
    params
  });

// 保存相册
export const saveAlbum = (params: any) =>
  request({
    url: '/albums',
    method: 'post',
    data: params
  });

// 保存相册
export const updateAlbum = (params: any) =>
  request({
    url: '/albums/' + params.id,
    method: 'put',
    data: params
  });

// 批量删除相册
export const deleteAlbums = (ids: any) =>
  request({
    url: '/albums/batch-delete',
    method: 'delete',
    data: ids
  });
