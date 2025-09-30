/*
 * @Description: 影视
 */
import request from '@/utils/request';

// 查询影视列表
export const pageDramas = (params: any) =>
  request({
    url: '/drama-series',
    method: 'get',
    params
  });

// 按分类查询影视数量
export const countDramasByType = (params: any) =>
  request({
    url: '/drama-series/countDramasByType',
    method: 'get',
    params
  });

// 保存影视
export const saveDrama = (params: any) =>
  request({
    url: '/drama-series',
    method: 'post',
    data: params
  });

// 修改影视
export const updateDrama = (params: any) =>
  request({
    url: '/drama-series/' + params.id,
    method: 'put',
    data: params
  });

// 批量删除影视
export const deleteDramas = (params: any) =>
  request({
    url: '/drama-series/batch-delete',
    method: 'post',
    data: params
  });

// 根据影视id查询影视
export const getDramaById = (id: any) =>
  request({
    url: '/drama-series/' + id,
    method: 'get'
  });
