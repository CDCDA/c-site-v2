/*
 * @Description:博客标签
 */
import request from '@/utils/request';

export const countTags = () =>
  request({
    url: '/blog-tags/count',
    method: 'get'
  });

// 分页
export const pageTags = (params: any) =>
  request({
    url: '/blog-tags',
    method: 'get',
    params
  });

// 删除标签分类
export const deleteTags = (ids: string[]) =>
  request({
    url: '/blog-tags/batch-delete',
    method: 'post',
    data: ids
  });

// 保存标签分类
export const saveTag = (data: any) =>
  request({
    url: '/blog-tags',
    method: 'post',
    data
  });

// 更新标签分类
export const updateTag = (data: any) =>
  request({
    url: '/blog-tags/' + data.id,
    method: 'put',
    data
  });
