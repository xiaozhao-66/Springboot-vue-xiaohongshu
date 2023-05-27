import request from '@/utils/request'
export default {
    getPage(page,limit,params) {
        return request({
            url: `/manage/user/getPage/${page}/${limit}`,
            method: 'post',
            data: params
          })
    },
}