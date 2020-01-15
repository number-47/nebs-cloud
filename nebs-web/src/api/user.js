import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/auth/oauth/token',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/auth/user',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/auth/signout',
    method: 'delete'
  })
}
