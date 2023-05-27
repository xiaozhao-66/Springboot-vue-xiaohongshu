import { request } from '../utils/request.js'
import { appConfig } from '../config/config.js'


export function collection (data) {
  return request.post(appConfig.WEB_API + '/platform/collection/collection', data)  
}

export function getAllCollection (page,limit,params) {
  return request.get(appConfig.WEB_API + `/platform/collection/getAllCollection/${page}/${limit}`, params)  
}

export function cancalCollection (data) {
  return request.post(appConfig.WEB_API + '/platform/collection/cancalCollection', data)  
}