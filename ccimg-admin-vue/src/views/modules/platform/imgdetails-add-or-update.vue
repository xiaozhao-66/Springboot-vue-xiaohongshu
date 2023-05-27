<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" :label-width="$i18n.locale === 'en-US' ? '120px' : '80px'">
          <el-form-item label="" prop="title">
          <el-input v-model="dataForm.title" placeholder=""></el-input>
      </el-form-item>
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
          <el-form-item label="" prop="imgsUrl">
          <el-input v-model="dataForm.imgsUrl" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="sort">
          <el-input v-model="dataForm.sort" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="status">
          <el-input v-model="dataForm.status" placeholder=""></el-input>
      </el-form-item>
              <el-form-item label="" prop="updater">
          <el-input v-model="dataForm.updater" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="updateDate">
          <el-input v-model="dataForm.updateDate" placeholder=""></el-input>
      </el-form-item>
      </el-form>
    <template slot="footer">
      <el-button @click="visible = false">{{ $t('cancel') }}</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">{{ $t('confirm') }}</el-button>
    </template>
  </el-dialog>
</template>

<script>
import debounce from 'lodash/debounce'
export default {
  data () {
    return {
      visible: false,
      dataForm: {
        id: '',
        title: '',
        content: '',
        cover: '',
        userId: '',
        categoryId: '',
        imgsUrl: '',
        sort: '',
        status: '',
        creator: '',
        createDate: '',
        updater: '',
        updateDate: ''
      }
    }
  },
  computed: {
    dataRule () {
      return {
        title: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        content: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        cover: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        userId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        categoryId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        imgsUrl: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        sort: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        status: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        updater: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        updateDate: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init () {
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.getInfo()
        }
      })
    },
    // 获取信息
    getInfo () {
      this.$http.get(`/platform/imgdetails/${this.dataForm.id}`).then(({ data: res }) => {
        if (res.code !== 0) {
          return this.$message.error(res.msg)
        }
        this.dataForm = {
          ...this.dataForm,
          ...res.data
        }
      }).catch(() => {})
    },
    // 表单提交
    dataFormSubmitHandle: debounce(function () {
      this.$refs['dataForm'].validate((valid) => {
        if (!valid) {
          return false
        }
        this.$http[!this.dataForm.id ? 'post' : 'put']('/platform/imgdetails/', this.dataForm).then(({ data: res }) => {
          if (res.code !== 0) {
            return this.$message.error(res.msg)
          }
          this.$message({
            message: this.$t('prompt.success'),
            type: 'success',
            duration: 500,
            onClose: () => {
              this.visible = false
              this.$emit('refreshDataList')
            }
          })
        }).catch(() => {})
      })
    }, 1000, { 'leading': true, 'trailing': false })
  }
}
</script>
