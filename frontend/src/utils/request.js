import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

const service = axios.create({
  baseURL: '/api',
  timeout: 5000
})

// Request interceptor
service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// Response interceptor
service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.message || '未知错误')
      
      // 401: Unauthorized
      if (res.code === 401) {
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        router.push('/login')
      }
      return Promise.reject(new Error(res.message || '未知错误'))
    } else {
      return res.data
    }
  },
  error => {
    console.log('err' + error)
    let message = error.message
    if (error.response) {
        if (error.response.status === 401) {
            message = '未授权：请重新登录'
            localStorage.removeItem('token')
            localStorage.removeItem('user')
            router.push('/login')
        } else if (error.response.data && error.response.data.message) {
            message = error.response.data.message
        }
    }
    ElMessage.error(message)
    return Promise.reject(error)
  }
)

export default service
