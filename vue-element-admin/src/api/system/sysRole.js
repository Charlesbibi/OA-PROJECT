/*
角色管理相关的API请求函数
*/
import request from '@/utils/request'

const api_name = '/admin/system/sysRole'

export default {
     /*
  获取所有role
  */
  getRoles() {
    return request({
      url: `${api_name}/getAllRole`,
      method: 'get'
    })},
  
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
   根据id删除指定用户 
   */
  deleteSysRoleById(id){
    return request({
        url: `${api_name}/deleteById/${id}`,
        method: 'delete'
    })},
   /*
   添加用户
   */ 
  addSysRole(sysRole){
    return request({
      url: `${api_name}/insert`,
      method: 'post',
      data: sysRole
    })},
    /*
   修改用户
   */ 
  updateSysRole(sysRole){
    return request({
      url: `${api_name}/update`,
      method: 'put',
      data: sysRole
    })},
     /*
   根据id查询用户
   */ 
  getSysRoleById(id){
    return request({
      url: `${api_name}/get/${id}`,
      method: 'get',
    })},
     /*
   根据idList批量删除
   */ 
   deleteByPatchId(idList){
    return request({
      url: `${api_name}/deleteByPatchId`,
      method: 'delete',
      data: idList
    })},
}