/*
用户管理相关的API请求函数
*/
import request from '@/utils/request'

const api_name = '/admin/system/sysUser'

export default {

  /*
  获取角色分页列表(带搜索)
  */
  getPageList(page, limit, searchObj) {
    return request({
      url: `${api_name}/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })},

    /*
  根据id获取角色
  */
  getUserById(id) {
    return request({
      url: `${api_name}/get/${id}`,
      method: 'get'
    })},

       /*
  分配post
  */
  allocatePost(userId, postId) {
    return request({
      url: `${api_name}/allocatePost/${userId}/${postId}`,
      method: 'get'
    })},
    
     /*
  根据id获取角色岗位
  */
  getPostById(userId) {
    return request({
      url: `${api_name}/getPostById/${userId}`,
      method: 'get'
    })},

    /*
  修改用户
  */
  updateUserById(sysUser) {
    return request({
      url: `${api_name}/update`,
      method: 'put',
      data: sysUser
    })},

    /*
  添加用户
  */
  addUser(sysUser) {
    return request({
      url: `${api_name}/add`,
      method: 'post',
      data: sysUser
    })},

    /*
  删除用户
  */
  deleteUserById(id) {
    return request({
      url: `${api_name}/delete/${id}`,
      method: 'delete'
    })},

      /*
  获取指定用户对应的角色id
  */
  getAllRoleFromUser(id) {
    return request({
      url: `${api_name}/getAllUserRole/${id}`,
      method: 'get'
    })},

      /*
  赋值指定的角色给用户
  */
  AssignRole(id, roleIDs) {
    return request({
      url: `${api_name}/assignRoles/${id}`,
      method: 'delete',
      data: roleIDs
    })},

    /*
  获取指定用户对应的角色
  */
  getAllRole(id) {
    return request({
      url: `${api_name}/getAllRole/${id}`,
      method: 'get'
    })},

      /*
  修改指定用户对应的状态
  */
  changeStatus(status,id) {
    return request({
      url: `${api_name}/changeStatus/${id}/${status}`,
      method: 'put'
    })},
}