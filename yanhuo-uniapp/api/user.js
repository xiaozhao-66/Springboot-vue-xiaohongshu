import {
	request
} from '../utils/request.js'
import {
	appConfig
} from '../config/config.js'


export function getMe(params) {
	return request.get(appConfig.WEB_API + '/manage/user/getOne', params)
}


export function getTrendByUser(page, limit, params) {
	return request.get(appConfig.WEB_API + `/platform/user/getTrendByUser/${page}/${limit}`, params)
}

export function getUserInfo(params) {
	return request.get(appConfig.WEB_API + `/platform/user/getUserInfo`, params)
}


export function updateUser(data) {
	return request.post(appConfig.WEB_API + '/platform/user/updateUser', data)
}

export function searchUser(page, limit, params) {
	return request.get(appConfig.WEB_API + `/platform/user/searchUser/${page}/${limit}`, params)
}

export function searchUserByUsername(params) {
	return request.get(appConfig.WEB_API + '/platform/user/searchUserByUsername', params)
}

export function getUserRecord(params) {
	return request.get(appConfig.WEB_API + `/platform/user/getUserRecord`, params)
}

export function clearUserRecord(params) {
	return request.get(appConfig.WEB_API + `/platform/user/clearUserRecord`, params)
}