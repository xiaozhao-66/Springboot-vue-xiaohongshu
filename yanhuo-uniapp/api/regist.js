import {
	request
} from '../utils/request.js'
import {
	appConfig
} from '../config/config.js'


export function sendDm(dm) {
	return request.get(appConfig.WEB_API + `/util/dm/sendDm/${dm}`, null)
}

export function sendMsm(msm) {
	return request.get(appConfig.WEB_API + `/util/msm/sendMsm/${msm}`, null)
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