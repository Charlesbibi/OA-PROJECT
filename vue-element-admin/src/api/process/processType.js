import request from '@/utils/request'

const api_name = '/admin/process/processType'

export default {

  getPageList(page, limit) {
    return request({
      url: `${api_name}/${page}/${limit}`,
      method: 'get'
    })
  },

  findAll() {
    return request({
      url: `${api_name}/findAll`,
      method: 'get'
    })
  },

  getById(id) {
    return request({
      url: `${api_name}/get/${id}`,
      method: 'get'
    })
  },
  
  updateById(processType) {
    return request({
      url: `${api_name}/update`,
      method: 'put',
      data: processType
    })
  },

  save(processType) {
    return request({
      url: `${api_name}/add`,
      method: 'post',
      data: processType
    })
  },

  removeById(id) {
    return request({
      url: `${api_name}/delete/${id}`,
      method: 'delete',
    })
  },

}