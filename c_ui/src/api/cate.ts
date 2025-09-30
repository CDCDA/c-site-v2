/*
 * @Description: 美食
 */
import request from '@/utils/request';

// 查询美食列表
export const pageCates = (params: any) =>
  request({
    url: '/cates',
    method: 'get',
    params
  });

// 保存美食
export const saveCate = (params: any) =>
  request({
    url: '/cates',
    method: 'post',
    data: params
  });

// 修改美食
export const updateCate = (params: any) =>
  request({
    url: '/cates/' + params.id,
    method: 'put',
    data: params
  });

// 批量删除美食
export const deleteCates = (ids: any) =>
  request({
    url: '/cates/batch-delete',
    method: 'delete',
    data: ids
  });

// 根据美食id查询美食
export const getCateById = (id: String) =>
  request({
    url: '/cates/' + id,
    method: 'get'
  });
