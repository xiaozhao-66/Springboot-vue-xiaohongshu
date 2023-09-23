import {
	request
} from '../utils/request.js'
import {
	appConfig
} from '../config/config.js'


export function sendDm(data) {
	return request.post(appConfig.WEB_API + `/util/dm/sendDm`, data)
}

export function sendMsm(data) {
	return request.post(appConfig.WEB_API + `/util/msm/sendMsm`, data)
}

export function register(data) {
	return request.post(appConfig.WEB_API + `/auth/register`, data)
}

export function check(data) {
	return request.post(appConfig.WEB_API + `/auth/check`, data)
}

export function updatePassword(data) {
	return request.post(appConfig.WEB_API + `/auth/updatePassword`, data)
}

export function isRegist(data) {
	return request.post(appConfig.WEB_API + `/auth/isRegist`, data)
}