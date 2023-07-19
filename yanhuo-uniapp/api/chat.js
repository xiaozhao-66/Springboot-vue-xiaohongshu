import {
	request
} from '../utils/request.js'
import {
	appConfig
} from '../config/config.js'


export function getZimToken(params) {
	return request.get(appConfig.WEB_API + '/platform/chat/getZimToken', params)
}


export function getChatUserList(params) {
	return request.get(appConfig.WEB_API + '/platform/chat/getChatUserList', params)
}

export function addChatRecord(data) {
	return request.post(appConfig.WEB_API + '/platform/chat/addChatRecord', data)
}


export function getChatRecord(page, limit, params) {
	return request.get(appConfig.WEB_API + `/platform/chat/getChatRecord/${page}/${limit}`, params)
}

export function updateRecordCount(params) {
	return request.get(appConfig.WEB_API + `/platform/chat/updateRecordCount`, params)
}

export function deleteRecord(params) {
	return request.get(appConfig.WEB_API + `/platform/chat/deleteRecord`, params)
}