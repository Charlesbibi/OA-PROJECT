import request from '@/utils/request'

/*
菜单管理相关的API请求函数
*/
const api_name = '/admin/system/sysDept'

export default {

  /*
  获取部门列表
  */
  findNodes() {
    return request({
      url: `${api_name}/treeNode`,
      method: 'get'
    })
  },

  /*
  修改部门
  */
  allocateDept(id,deptId) {
    return request({
      url: `${api_name}/allocateDept/${id}/${deptId}`,
      method: 'put'
    })
  },

  /*
  获取路径id
  */
  findTreePath(id) {
    return request({
      url: `${api_name}/findTreePath/${id}`,
      method: 'get'
    })
  },

  /*
  删除一个项
  */
  removeById(id) {
    return request({
      url: `${api_name}/remove/${id}`,
      method: "delete"
    })
  },

  /*
  保存一个项
  */
  save(sysDept) {
    return request({
      url: `${api_name}/add`,
      method: "post",
      data: sysDept
    })
  },

  /*
  更新一个权限项
  */
  updateById(sysDept) {
    return request({
      url: `${api_name}/update`,
      method: "put",
      data: sysDept
    })
  },
}