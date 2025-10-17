/*
 * @Description:
 */
import request from '@/utils/request';

// 查询字典类型数
export const countDictTypes = () =>
  request({
    url: '/dict-types/count',
    method: 'get'
  });

// 查询字典类型列表
export const pageDictTypes = (params: any) =>
  request({
    url: '/dict-types',
    method: 'get',
    params
  });

// 根据id查询字典类型
export const getDictTypeById = (id: any) =>
  request({
    url: `/dict-types/${id}`,
    method: 'get'
  });

// 保存字典类型
export const saveDictType = (params: any) =>
  request({
    url: '/dict-types',
    method: 'post',
    data: params
  });

// 修改字典类型
export const updateDictType = (params: any) =>
  request({
    url: `/dict-types/${params.id}`,
    method: 'put',
    data: params
  });

// 批量删除字典类型
export const deleteDictTypes = (ids: any) =>
  request({
    url: '/dict-types/batch-delete',
    method: 'delete',
    data: ids
  });
