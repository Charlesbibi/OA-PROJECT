/*
职位管理相关的API请求函数
*/
import request from '@/utils/request'

const api_name = '/admin/system/sysPost'

export default {
     /*
  获取所有post
  */
  getPosts(searchObj) {
    return request({
      url: `${api_name}/list`,
      method: 'post',
      params: searchObj
    })},

   
  
  /*
   根据id删除指定post
   */
  deleteSysPostById(id){
    return request({
        url: `${api_name}/deleteById/${id}`,
        method: 'delete'
    })},

    getsysPostById(id){
      return request({
          url: `${api_name}/findById/${id}`,
          method: 'get'
      })},
   /*
   添加用户
   */ 
  addSysPost(sysPost){
    return request({
      url: `${api_name}/add`,
      method: 'post',
      data: sysPost
    })},
    /*
   修改用户
   */ 
  updateSysPost(sysPost){
    return request({
      url: `${api_name}/updateById`,
      method: 'put',
      data: sysPost
    })},
     /*
   根据id查询用户
   */ 
   findById(id){
    return request({
      url: `${api_name}/findById/${id}`,
      method: 'get',
    })},
 /*
   根据id更改状态
   */ 
   changeStatus(id){
      return request({
        url: `${api_name}/changeStatus/${id}`,
        method: 'put',
      })},
}