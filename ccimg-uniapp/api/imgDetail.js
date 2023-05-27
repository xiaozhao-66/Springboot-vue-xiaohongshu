import { request } from '../utils/request.js'
import { appConfig } from '../config/config.js'


export function publish (imgInfo) {
  return request.post(appConfig.WEB_API + '/platform/imgdetails/publish', imgInfo)  
}


export function getAllImgByAlbum (page,limit,params) {
  return request.get(appConfig.WEB_API + `/platform/imgdetails/getAllImgByAlbum/${page}/${limit}`, params)  
}


export function getOne (params) {
  return request.get(appConfig.WEB_API + `/platform/imgdetails/getOne`, params)  
}


export function getPage (page,limit,params) {
  return request.get(appConfig.WEB_API + `/platform/imgdetails/getPage/${page}/${limit}`, params)  
}

export function getRecommend (page,limit,params) {
  return request.get(appConfig.WEB_API + `/recommend/newRecommend/newRecommendToUser/${page}/${limit}`, params)  
}

export function deleteImgs (data,uid) {
  return request.post(appConfig.WEB_API + `/platform/imgdetails/deleteImgs/${uid}`, data)  
}

export function getHot (page,limit) {
  return request.get(appConfig.WEB_API + `/platform/imgdetails/getHot/${page}/${limit}`, null)  
}


export function updateImgDetail (data) {
  return request.post(appConfig.WEB_API + '/platform/imgdetails/updateImgDetail', data)  
}