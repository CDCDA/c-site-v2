/*
 * @Description: 待办
 */
import request from '@/utils/request';

// 数量
export const countTodos = () =>
  request({
    url: '/todos/count',
    method: 'get'
  });

// 分页
export const pageTodos = (params: any) =>
  request({
    url: '/todos',
    method: 'get',
    params
  });

// 按时间查询
export const countTodosByDate = (params: any) =>
  request({
    url: '/todos/stats/date-range',
    method: 'get',
    params
  });

//批量删除
export const deleteTodos = (ids: any) =>
  request({
    url: '/todos/batch-delete',
    method: 'delete',
    data: ids
  });

// 保存
export const saveTodo = (data: any) =>
  request({
    url: '/todos',
    method: 'post',
    data
  });

// 更新
export const updateTodo = (id: string, data: any) =>
  request({
    url: '/todos/' + id,
    method: 'put',
    data
  });

// 根据id查询
export const getTodoById = (id: string) =>
  request({
    url: '/todos/' + id,
    method: 'get'
  });
