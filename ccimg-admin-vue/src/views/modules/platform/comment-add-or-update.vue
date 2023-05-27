<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" :label-width="$i18n.locale === 'en-US' ? '120px' : '80px'">
          <el-form-item label="" prop="mid">
          <el-input v-model="dataForm.mid" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="uid">
          <el-input v-model="dataForm.uid" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="rootId">
          <el-input v-model="dataForm.rootId" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="replyId">
          <el-input v-model="dataForm.replyId" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="level">
          <el-input v-model="dataForm.level" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="sort">
          <el-input v-model="dataForm.sort" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="content">
          <el-input v-model="dataForm.content" placeholder=""></el-input>
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
        mid: '',
        uid: '',
        rootId: '',
        replyId: '',
        level: '',
        sort: '',
        content: '',
        creator: '',
        createDate: ''
      }
    }
  },
  computed: {
    dataRule () {
      return {
        mid: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        uid: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        rootId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        replyId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        level: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        sort: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        content: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
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
      this.$http.get(`//comment/${this.dataForm.id}`).then(({ data: res }) => {
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
        this.$http[!this.dataForm.id ? 'post' : 'put']('//comment/', this.dataForm).then(({ data: res }) => {
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
