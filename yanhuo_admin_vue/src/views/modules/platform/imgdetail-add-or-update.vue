<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="" prop="content">
      <el-input v-model="dataForm.content" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="cover">
      <el-input v-model="dataForm.cover" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="userId">
      <el-input v-model="dataForm.userId" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="categoryId">
      <el-input v-model="dataForm.categoryId" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="categoryPid">
      <el-input v-model="dataForm.categoryPid" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="imgsUrl">
      <el-input v-model="dataForm.imgsUrl" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="count">
      <el-input v-model="dataForm.count" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="sort">
      <el-input v-model="dataForm.sort" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="status">
      <el-input v-model="dataForm.status" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="viewCount">
      <el-input v-model="dataForm.viewCount" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="agreeCount">
      <el-input v-model="dataForm.agreeCount" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="collectionCount">
      <el-input v-model="dataForm.collectionCount" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="commentCount">
      <el-input v-model="dataForm.commentCount" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="creator">
      <el-input v-model="dataForm.creator" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="createDate">
      <el-input v-model="dataForm.createDate" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="updater">
      <el-input v-model="dataForm.updater" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="updateDate">
      <el-input v-model="dataForm.updateDate" placeholder=""></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          id: 0,
          content: '',
          cover: '',
          userId: '',
          categoryId: '',
          categoryPid: '',
          imgsUrl: '',
          count: '',
          sort: '',
          status: '',
          viewCount: '',
          agreeCount: '',
          collectionCount: '',
          commentCount: '',
          creator: '',
          createDate: '',
          updater: '',
          updateDate: ''
        },
        dataRule: {
          content: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          cover: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          userId: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          categoryId: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          categoryPid: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          imgsUrl: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          count: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          sort: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          viewCount: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          agreeCount: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          collectionCount: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          commentCount: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          creator: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          createDate: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          updater: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          updateDate: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/platform/imgdetail/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.content = data.imgDetail.content
                this.dataForm.cover = data.imgDetail.cover
                this.dataForm.userId = data.imgDetail.userId
                this.dataForm.categoryId = data.imgDetail.categoryId
                this.dataForm.categoryPid = data.imgDetail.categoryPid
                this.dataForm.imgsUrl = data.imgDetail.imgsUrl
                this.dataForm.count = data.imgDetail.count
                this.dataForm.sort = data.imgDetail.sort
                this.dataForm.status = data.imgDetail.status
                this.dataForm.viewCount = data.imgDetail.viewCount
                this.dataForm.agreeCount = data.imgDetail.agreeCount
                this.dataForm.collectionCount = data.imgDetail.collectionCount
                this.dataForm.commentCount = data.imgDetail.commentCount
                this.dataForm.creator = data.imgDetail.creator
                this.dataForm.createDate = data.imgDetail.createDate
                this.dataForm.updater = data.imgDetail.updater
                this.dataForm.updateDate = data.imgDetail.updateDate
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/platform/imgdetail/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'content': this.dataForm.content,
                'cover': this.dataForm.cover,
                'userId': this.dataForm.userId,
                'categoryId': this.dataForm.categoryId,
                'categoryPid': this.dataForm.categoryPid,
                'imgsUrl': this.dataForm.imgsUrl,
                'count': this.dataForm.count,
                'sort': this.dataForm.sort,
                'status': this.dataForm.status,
                'viewCount': this.dataForm.viewCount,
                'agreeCount': this.dataForm.agreeCount,
                'collectionCount': this.dataForm.collectionCount,
                'commentCount': this.dataForm.commentCount,
                'creator': this.dataForm.creator,
                'createDate': this.dataForm.createDate,
                'updater': this.dataForm.updater,
                'updateDate': this.dataForm.updateDate
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
