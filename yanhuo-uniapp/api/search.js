import {
	request
} from '../utils/request.js'
import {
	appConfig
} from '../config/config.js'


export function esSearch(page, limit, data) {
	return request.post(appConfig.WEB_API + `/search/imgDetailSearch/esSearch/${page}/${limit}`, data)
}


export function addSearchRecordData(parmas) {
	return request.get(appConfig.WEB_API + `/search/searchRecord/addSearchRecordData`, parmas)
}


export function esSearchRecord(parmas) {
	return request.get(appConfig.WEB_API + `/search/searchRecord/esSearchRecord`, parmas)
}