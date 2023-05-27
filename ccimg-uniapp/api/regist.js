import { request } from '../utils/request.js'
import { appConfig } from '../config/config.js'


export function sendDm(dm){
	return request.get(appConfig.WEB_API + `/utils/dm/sendDm/${dm}`, null)  
}

export function sendMsm(msm){
	return request.get(appConfig.WEB_API + `/utils/msm/sendMsm/${msm}`, null)  
}

export function register(data){
	return request.post(appConfig.WEB_API + `/auth/auth/register`, data)  
}

export function check(data){
	return request.post(appConfig.WEB_API + `/auth/auth/check`, data)  
}

export function updatePassword(data){
	return request.post(appConfig.WEB_API + `/auth/auth/updatePassword`, data)  
}

export function isRegist(data){
	return request.post(appConfig.WEB_API + `/auth/auth/isRegist`, data)  
}