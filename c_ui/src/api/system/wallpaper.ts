/*
 * @Description:壁纸
 */
import request from '@/utils/request';

// 查询壁纸列表
export const pageWallpapers = (params: any) =>
  request({
    url: '/wallpapers',
    method: 'get',
    params
  });

export const getRandomWallpaper = () =>
  request({
    url: '/wallpapers/getRandomWallpaper',
    method: 'get'
  });

// 根据id查询壁纸
export const getWallpaperById = (id: any) =>
  request({
    url: `/wallpapers/${id}`,
    method: 'get'
  });

// 保存壁纸
export const saveWallpaper = (params: any) =>
  request({
    url: '/wallpapers',
    method: 'post',
    data: params
  });

// 修改壁纸
export const updateWallpaper = (params: any) =>
  request({
    url: `/wallpapers/${params.id}`,
    method: 'put',
    data: params
  });

// 批量删除壁纸
export const deleteWallpapers = (ids: any) =>
  request({
    url: '/wallpapers/batch-delete',
    method: 'delete',
    data: ids
  });
