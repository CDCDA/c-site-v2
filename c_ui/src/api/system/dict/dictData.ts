/*
 * @Description:
 */
import request from '@/utils/request';

// 根据dictType获取字典数据
export const getDictDataByDictType = (dictType: any) =>
  request({
    url: `/dict-datas/by-type/${dictType}`,
    method: 'get'
  });

// 根据dictType获取字典数据分页
export const pageDicts = (params: any) =>
  request({
    url: `/dict-datas/by-type/${params.dictType}/page`,
    method: 'get',
    params
  });

// 查询字典数据数
export const countDictDatas = () =>
  request({
    url: '/dict-datas/count',
    method: 'get'
  });

// 查询字典数据列表
export const pageDictDatas = (params: any) =>
  request({
    url: '/dict-datas',
    method: 'get',
    params
  });

// 根据id查询字典数据
export const getDictDataById = (id: String) =>
  request({
    url: `/dict-datas/${id}`,
    method: 'get'
  });

// 保存字典数据
export const saveDictData = (params: any) =>
  request({
    url: '/dict-datas',
    method: 'post',
    data: params
  });

// 修改字典数据
export const updateDictData = (params: any) =>
  request({
    url: `/dict-datas/${params.id}`,
    method: 'put',
    data: params
  });

// 批量删除字典数据
export const deleteDictDatas = (ids: any) =>
  request({
    url: '/dict-datas/batch-delete',
    method: 'delete',
    data: ids
  });
