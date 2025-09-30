/*
 * @Description:
 */
import request from '@/utils/request';

// 根据dictType获取字典数据
export const getDictDataByDictType = (dictType: any) =>
  request({
    url: `/dictData/getDictDataByDictType/${dictType}`,
    method: 'get'
  });

// 根据dictType获取字典数据分页
export const pageDicts = (params: any) =>
  request({
    url: `/dictDataDictDataByDictType`,
    method: 'post',
    data: params
  });

// 查询字典数据数
export const countDictData = () =>
  request({
    url: '/dictData/count',
    method: 'get'
  });

// 查询字典数据列表
export const listDictData = (params: any) =>
  request({
    url: '/dictData',
    method: 'get',
    params
  });

// 根据id查询字典数据
export const getDictDataById = (id: String) =>
  request({
    url: `/dictData/${id}`,
    method: 'get'
  });

// 保存或修改字典数据
export const saveDictData = (params: any) =>
  request({
    url: '/dictData',
    method: 'post',
    data: params
  });

// 修改字典数据
export const updateDictData = (params: any) =>
  request({
    url: `/dictData/${params.id}`,
    method: 'put',
    data: params
  });

// 批量删除字典数据
export const deleteDictData = (params: any) =>
  request({
    url: '/dictData/batch-delete',
    method: 'delete',
    data: params
  });
