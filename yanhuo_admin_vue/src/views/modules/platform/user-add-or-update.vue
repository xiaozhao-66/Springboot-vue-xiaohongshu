<template>
    <el-dialog
      :title="!dataForm.id ? '新增' : '修改'"
      :close-on-click-modal="false"
      :visible.sync="visible">
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="" prop="userId">
        <el-input v-model="dataForm.userId" placeholder=""></el-input>
      </el-form-item>
      <el-form-item label="" prop="username">
        <el-input v-model="dataForm.username" placeholder=""></el-input>
      </el-form-item>
      <el-form-item label="" prop="password">
        <el-input v-model="dataForm.password" placeholder=""></el-input>
      </el-form-item>
      <el-form-item label="" prop="avatar">
        <el-input v-model="dataForm.avatar" placeholder=""></el-input>
      </el-form-item>
      <el-form-item label="" prop="gender">
        <el-input v-model="dataForm.gender" placeholder=""></el-input>
      </el-form-item>
      <el-form-item label="" prop="phone">
        <el-input v-model="dataForm.phone" placeholder=""></el-input>
      </el-form-item>
      <el-form-item label="" prop="email">
        <el-input v-model="dataForm.email" placeholder=""></el-input>
      </el-form-item>
      <el-form-item label="" prop="description">
        <el-input v-model="dataForm.description" placeholder=""></el-input>
      </el-form-item>
      <el-form-item label="" prop="status">
        <el-input v-model="dataForm.status" placeholder=""></el-input>
      </el-form-item>
      <el-form-item label="" prop="cover">
        <el-input v-model="dataForm.cover" placeholder=""></el-input>
      </el-form-item>
      <el-form-item label="" prop="birthday">
        <el-input v-model="dataForm.birthday" placeholder=""></el-input>
      </el-form-item>
      <el-form-item label="" prop="address">
        <el-input v-model="dataForm.address" placeholder=""></el-input>
      </el-form-item>
      <el-form-item label="" prop="trendCount">
        <el-input v-model="dataForm.trendCount" placeholder=""></el-input>
      </el-form-item>
      <el-form-item label="" prop="followCount">
        <el-input v-model="dataForm.followCount" placeholder=""></el-input>
      </el-form-item>
      <el-form-item label="" prop="fanCount">
        <el-input v-model="dataForm.fanCount" placeholder=""></el-input>
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
            userId: '',
            username: '',
            password: '',
            avatar: '',
            gender: '',
            phone: '',
            email: '',
            description: '',
            status: '',
            cover: '',
            birthday: '',
            address: '',
            trendCount: '',
            followCount: '',
            fanCount: '',
            creator: '',
            createDate: '',
            updater: '',
            updateDate: ''
          },
          dataRule: {
            userId: [
              { required: true, message: '不能为空', trigger: 'blur' }
            ],
            username: [
              { required: true, message: '不能为空', trigger: 'blur' }
            ],
            password: [
              { required: true, message: '不能为空', trigger: 'blur' }
            ],
            avatar: [
              { required: true, message: '不能为空', trigger: 'blur' }
            ],
            gender: [
              { required: true, message: '不能为空', trigger: 'blur' }
            ],
            phone: [
              { required: true, message: '不能为空', trigger: 'blur' }
            ],
            email: [
              { required: true, message: '不能为空', trigger: 'blur' }
            ],
            description: [
              { required: true, message: '不能为空', trigger: 'blur' }
            ],
            status: [
              { required: true, message: '不能为空', trigger: 'blur' }
            ],
            cover: [
              { required: true, message: '不能为空', trigger: 'blur' }
            ],
            birthday: [
              { required: true, message: '不能为空', trigger: 'blur' }
            ],
            address: [
              { required: true, message: '不能为空', trigger: 'blur' }
            ],
            trendCount: [
              { required: true, message: '不能为空', trigger: 'blur' }
            ],
            followCount: [
              { required: true, message: '不能为空', trigger: 'blur' }
            ],
            fanCount: [
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
                url: this.$http.adornUrl(`/generator/tuser/info/${this.dataForm.id}`),
                method: 'get',
                params: this.$http.adornParams()
              }).then(({data}) => {
                if (data && data.code === 0) {
                  this.dataForm.userId = data.tUser.userId
                  this.dataForm.username = data.tUser.username
                  this.dataForm.password = data.tUser.password
                  this.dataForm.avatar = data.tUser.avatar
                  this.dataForm.gender = data.tUser.gender
                  this.dataForm.phone = data.tUser.phone
                  this.dataForm.email = data.tUser.email
                  this.dataForm.description = data.tUser.description
                  this.dataForm.status = data.tUser.status
                  this.dataForm.cover = data.tUser.cover
                  this.dataForm.birthday = data.tUser.birthday
                  this.dataForm.address = data.tUser.address
                  this.dataForm.trendCount = data.tUser.trendCount
                  this.dataForm.followCount = data.tUser.followCount
                  this.dataForm.fanCount = data.tUser.fanCount
                  this.dataForm.creator = data.tUser.creator
                  this.dataForm.createDate = data.tUser.createDate
                  this.dataForm.updater = data.tUser.updater
                  this.dataForm.updateDate = data.tUser.updateDate
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
                url: this.$http.adornUrl(`/generator/tuser/${!this.dataForm.id ? 'save' : 'update'}`),
                method: 'post',
                data: this.$http.adornData({
                  'id': this.dataForm.id || undefined,
                  'userId': this.dataForm.userId,
                  'username': this.dataForm.username,
                  'password': this.dataForm.password,
                  'avatar': this.dataForm.avatar,
                  'gender': this.dataForm.gender,
                  'phone': this.dataForm.phone,
                  'email': this.dataForm.email,
                  'description': this.dataForm.description,
                  'status': this.dataForm.status,
                  'cover': this.dataForm.cover,
                  'birthday': this.dataForm.birthday,
                  'address': this.dataForm.address,
                  'trendCount': this.dataForm.trendCount,
                  'followCount': this.dataForm.followCount,
                  'fanCount': this.dataForm.fanCount,
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
  