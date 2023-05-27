import axios from 'axios'
import { appConfig } from '../config/config.js'
import { tokenUtil } from './token'
//(该方法暂未用到)
// 从localStorage中获取token
function getLocalToken () {
	const token = tokenUtil.get()
    return token
}

// 给实例添加一个setToken方法，用于登录后将最新token动态添加到header，同时将token保存在localStorage中
// instance.setToken = (token) => {
//   instance.defaults.headers['X-Token'] = token
//   tokenUtil.set(token)
// }

function refreshToken (token) {
    // instance是当前request.js中已创建的axios实例
    return instance.get(`/auth/auth/refreshToken?token=${token}`).then(res => {
		    console.log("新的二token",res.data)
	})
}

// 创建一个axios实例
const instance = axios.create({
  baseURL: appConfig.WEB_API,
  timeout: 300000,
  headers: {
    'Content-Type': 'application/json',
     'Jwt_token': getLocalToken() // headers塞token
  }
})

// 是否正在刷新的标记
let isRefreshing = false
// 重试队列，每一项将是一个待执行的函数形式
let requests = []

instance.interceptors.response.use(response => {
  const { code } = response.data
  console.log("response.data",response.data)
  if (code != 0) {
    const config = response.config
	 
	
    if (!isRefreshing) {
      console.log("返回结果111",response.data)
      isRefreshing = true
	  
	  
      return refreshToken(response.data.data.Jwt_token).then(res => {
		console.log("刷新token",res.data)
        const { token } = res.data
		
		//修改instance实例中的token
		instance.defaults.headers[appConfig.tokenKey] = token
		tokenUtil.set(token)
       
        config.headers[appConfig.tokenKey] = token
        config.baseURL = appConfig.WEB_API
        // 已经刷新了token，将所有队列中的请求进行重试
        requests.forEach(cb => cb(token))
        requests = []
        return instance(config)
      }).catch(res => {
        console.error('refreshtoken error =>', res)
        window.location.href = '/'
      }).finally(() => {
        isRefreshing = false
      })
    } else {
      // 正在刷新token，将返回一个未执行resolve的promise
      return new Promise((resolve) => {
        // 将resolve放进队列，用一个函数形式来保存，等token刷新后直接执行
		console.log(22222)
        requests.push((token) => {
          config.baseURL = appConfig.WEB_API
          config.headers[appConfig.tokenKey] = token
          resolve(instance(config))
        })
      })
    }
  }
  return response
}, error => {
  return Promise.reject(error)
})

export default instance