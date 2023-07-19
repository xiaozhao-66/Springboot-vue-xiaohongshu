import {
	request
} from '../utils/request.js'
import {
	appConfig
} from '../config/config.js'


export function getAllSearchRecord(params) {
	return request.get(appConfig.WEB_API + '/platform/searchRecord/getAllSearchRecord', params)
}

export function addSearchRecord(data) {
	return request.post(appConfig.WEB_API + '/platform/searchRecord/addSearchRecord', data)
}

export function deleteSearchRecord(uid, data) {
	return request.post(appConfig.WEB_API + '/platform/searchRecord/deleteSearchRecord?uid=' + uid, data)
}