import {
	request
} from '../utils/request.js'
import {
	appConfig
} from '../config/config.js'


export function saveAlbum(albumInfo) {
	return request.post(appConfig.WEB_API + '/platform/album/saveAlbum', albumInfo)
}

export function updateAlbum(albumInfo) {
	return request.post(appConfig.WEB_API + '/platform/album/updateAlbum', albumInfo)
}

export function getAllAlbum(params) {
	return request.get(appConfig.WEB_API + '/platform/album/getAllAlbum', params)
}

export function getAlbum(params) {
	return request.get(appConfig.WEB_API + '/platform/album/getAlbum', params)
}


export function addAlbumImgRelation(data) {
	return request.post(appConfig.WEB_API + '/platform/albumImgRelation/addAlbumImgRelation', data)
}


export function isCollectImgToAlbum(params) {
	return request.get(appConfig.WEB_API + '/platform/albumImgRelation/isCollectImgToAlbum', params)
}

export function deleteAlbumImgRelation(data) {
	return request.post(appConfig.WEB_API + '/platform/albumImgRelation/deleteAlbumImgRelation', data)
}



export function deleteAlbum(params) {
	return request.get(appConfig.WEB_API + '/platform/album/deleteAlbum', params)
}