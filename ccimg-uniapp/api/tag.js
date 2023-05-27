import { request } from '../utils/request.js'
import { appConfig } from '../config/config.js'


export function saveTag (tagInfo) {
  return request.post(appConfig.WEB_API + '/platform/tag/saveTag', tagInfo)  
}


export function getAllTag (params) {
  return request.get(appConfig.WEB_API + '/platform/tag/getAllTag', params)  
}
