import { appConfig } from '../config/config.js'
import { tokenUtil } from './token'

const send = (url, data = {}, method = 'POST', showLoading = true) => {
   
	let expiration = uni.getStorageSync('expiration');
	
	// 时间格使用2020/10/10 12:00:00这种格式计算
	let tmp = expiration.toString().replace(/-/g, "/");
	
	let futureTime = new Date(tmp).valueOf();//转换成毫秒
	
	let nowTime = new Date().valueOf();//转换成毫秒

	// let jetLag = (futureTime - nowTime)/1000/60/60;
	
	let jetLag = (futureTime - nowTime)/1000/60/60
	
	let isRefresh = false
	
    return new Promise((resolve) => {
        uni.request({
            method: method,
            url: url,
            data: data,
            header: (() => {
                const tokeValue = tokenUtil.get()
                let config = {
                    
					'Content-Type': 'application/json'
                }
				
                if (tokeValue) {
                    config[appConfig.tokenKey] = tokeValue
                }
                return config
            })(),
            success: (res) => {
                uni.hideLoading()
	
				if(res.statusCode==401){
					 uni.showToast({
						icon: "none",
						title: "用户未认证或登录过期,请重新登录"
					});
					uni.removeStorageSync("userInfo")
					setTimeout(function() {
						uni.reLaunch({
							url: "/pages/login/login"
						});
					}, 1000);
				}else{
					
					//如果时间小于1小时，则刷新token
					if(jetLag<1&&!isRefresh){
						isRefresh = true
						let userInfo = uni.getStorageSync("userInfo")
						  uni.request({
							  method: 'post',
							  url: appConfig.WEB_API+"/auth/auth/refreshToken",
							  data: userInfo,
							  header: (() => {
							      let config = {
							          // 'Content-Type': 'application/x-www-form-urlencoded'
							  		'Content-Type': 'application/json'
							      }
							      return config
							  })(),
							   success: (response) => {
						
								   let refreshToken = response.data.data.Jwt_token
								   uni.setStorageSync("expiration", response.data.data.expiration)
								   tokenUtil.set(refreshToken)
								   resolve(res)
							   }
						  })
					}
				}
                resolve(res.data)
            }
        })
    })
}

export const request = {
    get: (url, data) => {
        return send(url, data, 'GET')
    },
    post: (url, data) => {
        return send(url, data, 'POST')
    }
}