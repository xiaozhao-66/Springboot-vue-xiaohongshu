import { request } from '../utils/request.js'
import { appConfig } from '../config/config.js'


export function agree(data){
	return request.post(appConfig.WEB_API + '/platform/agree/agree', data)  
}

export function cancelAgree(data){
	return request.post(appConfig.WEB_API + '/platform/agree/cancelAgree', data)  
}


export function isAgree(data){
	return request.post(appConfig.WEB_API + '/platform/agree/isAgree', data)  
}


export function getAllAgreeAndCollection (page,limit,params) {
  return request.get(appConfig.WEB_API + `/platform/agree/getAllAgreeAndCollection/${page}/${limit}`, params)  
}

