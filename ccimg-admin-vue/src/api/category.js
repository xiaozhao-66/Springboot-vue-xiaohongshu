import request from '@/utils/request'
export default {
    getAllCategory() {
        return request({
            url: `/manage/category/getTreeCategory`,
            method: 'get',
          })
    },

    addCategory(data) {
        return request({
            url: `/manage/category/addCategory`,
            method: 'post',
            data:data
          })
    },
}