import { request } from '../utils/request.js'
import { appConfig } from '../config/config.js'




export function followUser (data) {
  return request.post(appConfig.WEB_API + `/platform/follow/followUser`, data)  
}

export function clearFollow (data) {
  return request.post(appConfig.WEB_API + `/platform/follow/clearFollow`, data)  
}

export function isFollow (params) {
  return request.get(appConfig.WEB_API + `/platform/follow/isFollow`, params)  
}


export function getAllFanUser (page,limit,params) {
  return request.get(appConfig.WEB_API + `/platform/follow/getAllFanUser/${page}/${limit}`, params)  
}

export function getAllFriend (params) {
  return request.get(appConfig.WEB_API + `/platform/follow/getAllFriend`, params)  
}