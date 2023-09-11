import {
	request
} from '../utils/request.js'
import {
	appConfig
} from '../config/config.js'


export function agree(data) {
	return request.post(appConfig.WEB_API + '/platform/agreeCollect/agree', data)
}

export function cancelAgree(data) {
	return request.post(appConfig.WEB_API + '/platform/agreeCollect/cancelAgree', data)
}


export function isAgree(data) {
	return request.post(appConfig.WEB_API + '/platform/agreeCollect/isAgree', data)
}


export function getAllAgreeAndCollection(page, limit, params) {
	return request.get(appConfig.WEB_API + `/platform/agreeCollect/getAllAgreeAndCollection/${page}/${limit}`, params)
}

export function getAllCollection(page, limit, params) {
	return request.get(appConfig.WEB_API + `/platform/agreeCollect/getAllCollection/${page}/${limit}`, params)
}


export function collection(data) {
	return request.post(appConfig.WEB_API + '/platform/agreeCollect/collection', data)
}

export function cancelCollection(data) {
	return request.post(appConfig.WEB_API + '/platform/agreeCollect/cancelCollection', data)
}