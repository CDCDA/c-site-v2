/*
 * @Description:音乐
 */

import request from '@/utils/request';

// 查询音乐数
export const countMusic = () =>
  request({
    url: '/musics/count',
    method: 'get'
  });

//查询歌曲
export const getSongByKeyword = (params: any) =>
  request({
    url: '/song/music-api.php',
    method: 'get',
    params
  });

//网易云音乐查询接口
export const getSongByName = (params: any) =>
  request({
    url: 'wyy/api/search/get/web',
    method: 'get',
    params,
    baseURL: ''
  });

//网易云音乐详情接口
export const getSongById = (params: any) =>
  request({
    url: 'wyy/api/song/detail/?id=' + params.id + '&ids=[' + params.id + ']',
    method: 'get',
    baseURL: ''
  });

//网易云音乐根据id获取歌曲
//http://music.163.com/song/media/outer/url?id=2024416009.mp3

//音乐列表
export const listSong = (params: any) =>
  request({
    url: '/musics',
    method: 'get',
    params
  });

// 保存音乐
export const saveMusic = (params: any) =>
  request({
    url: '/musics',
    method: 'post',
    data: params
  });

// 批量删除音乐
export const delMusic = (params: any) =>
  request({
    url: '/musics/batch-delete',
    method: 'post',
    data: params
  });

// 根据音乐id查询音乐
export const getMusicById = (params: any) =>
  request({
    url: '/musics/getMusicById',
    method: 'get',
    params
  });
