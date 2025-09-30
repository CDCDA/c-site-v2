/*
 * @Description: 游戏
 */
import request from '@/utils/request';

// 查询游戏列表
export const pageGames = (params: any) =>
  request({
    url: '/games',
    method: 'get',
    params
  });

// 按分类查询游戏数量
export const countGamesByType = (params: any) =>
  request({
    url: '/games/countGamesByType',
    method: 'get',
    params
  });

// 保存游戏
export const saveGame = (params: any) =>
  request({
    url: '/games',
    method: 'post',
    data: params
  });

// 修改游戏
export const updateGame = (params: any) =>
  request({
    url: `/games/${params.id}`,
    method: 'put',
    data: params
  });

// 批量删除游戏
export const deleteGames = (ids: any) =>
  request({
    url: '/games/batch-delete',
    method: 'post',
    data: ids
  });

// 根据游戏id查询游戏
export const getGameById = (id: any) =>
  request({
    url: `/games/${id}`,
    method: 'get'
  });
