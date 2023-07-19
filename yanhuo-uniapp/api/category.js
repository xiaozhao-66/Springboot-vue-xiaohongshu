import {
	request
} from '../utils/request.js'
import {
	appConfig
} from '../config/config.js'


export function getCategory() {
	return request.get(appConfig.WEB_API + '/platform/category/getTreeCategory', null)
}


export function getImgListByCategory(page, limit, params) {
	return request.get(appConfig.WEB_API + `/platform/category/getImgListByCategory/${page}/${limit}`, params)
}