import { request } from '../utils/request.js'
import { appConfig } from '../config/config.js'



export function addBrowseRecord(data){
	return request.post(appConfig.WEB_API + '/platform/browserecord/addBrowseRecord', data)  
}

export function getAllBrowseRecordByUser (page,limit,params) {
  return request.get(appConfig.WEB_API + `/platform/browserecord/getAllBrowseRecordByUser/${page}/${limit}`, params)  
}

export function delRecord(data){
	return request.post(appConfig.WEB_API + '/platform/browserecord/delRecord', data)  
}