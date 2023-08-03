<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="" prop="name">
      <el-input v-model="dataForm.name" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="pid">
      <el-input v-model="dataForm.pid" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="sort">
      <el-input v-model="dataForm.sort" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="count">
      <el-input v-model="dataForm.count" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="description">
      <el-input v-model="dataForm.description" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="分类的封面，如果是一级分类就是随便看看的封面，二级分类则是主封面" prop="cover">
      <el-input v-model="dataForm.cover" placeholder="分类的封面，如果是一级分类就是随便看看的封面，二级分类则是主封面"></el-input>
    </el-form-item>
    <el-form-item label="热门封面" prop="hotCover">
      <el-input v-model="dataForm.hotCover" placeholder="热门封面"></el-input>
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
          name: '',
          pid: '',
          sort: '',
          count: '',
          description: '',
          cover: '',
          hotCover: '',
          creator: '',
          createDate: '',
          updater: '',
          updateDate: ''
        },
        dataRule: {
          name: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          pid: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          sort: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          count: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          description: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          cover: [
            { required: true, message: '分类的封面，如果是一级分类就是随便看看的封面，二级分类则是主封面不能为空', trigger: 'blur' }
          ],
          hotCover: [
            { required: true, message: '热门封面不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/platform/category/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.name = data.category.name
                this.dataForm.pid = data.category.pid
                this.dataForm.sort = data.category.sort
                this.dataForm.count = data.category.count
                this.dataForm.description = data.category.description
                this.dataForm.cover = data.category.cover
                this.dataForm.hotCover = data.category.hotCover
                this.dataForm.creator = data.category.creator
                this.dataForm.createDate = data.category.createDate
                this.dataForm.updater = data.category.updater
                this.dataForm.updateDate = data.category.updateDate
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
              url: this.$http.adornUrl(`/platform/category/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'name': this.dataForm.name,
                'pid': this.dataForm.pid,
                'sort': this.dataForm.sort,
                'count': this.dataForm.count,
                'description': this.dataForm.description,
                'cover': this.dataForm.cover,
                'hotCover': this.dataForm.hotCover,
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
