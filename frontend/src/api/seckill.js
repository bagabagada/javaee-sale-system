import request from '../utils/request'

export function getSeckillList() {
  return request({
    url: '/seckill/list',
    method: 'get'
  })
}

export function getSeckillDetail(id) {
  return request({
    url: '/seckill/' + id,
    method: 'get'
  })
}

export const executeSeckill = async (activityId) => {
    const response = await request.post(`/order/seckill/${activityId}`)
    return response
}

export const payOrder = async (orderId) => {
    const response = await request.post(`/order/pay/${orderId}`)
    return response
}

export const getOrderList = async () => {
    const response = await request.get('/order/list')
    return response
}

export const getUserInfo = async () => {
    const response = await request.get('/user/info')
    return response
}

export const recharge = async (amount) => {
    const response = await request.post('/user/recharge', { amount })
    return response
}
