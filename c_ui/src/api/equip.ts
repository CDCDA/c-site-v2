/*
 * @Description: 设备
 */
import request from '@/utils/request';

// 查询设备列表
export const pageEquips = (params: any) =>
  request({
    url: '/equipments',
    method: 'get',
    params
  });

// 保存设备
export const saveEquip = (params: any) =>
  request({
    url: '/equipments',
    method: 'post',
    data: params
  });

// 保存设备
export const updateEquip = (params: any) =>
  request({
    url: '/equipments/' + params.id,
    method: 'put',
    data: params
  });

// 批量删除设备
export const delEquip = (ids: any) =>
  request({
    url: '/equipments/batch-delete',
    method: 'post',
    data: ids
  });

// 根据设备id查询设备
export const getEquipById = (id: any) =>
  request({
    url: '/equipments/' + id,
    method: 'get'
  });
